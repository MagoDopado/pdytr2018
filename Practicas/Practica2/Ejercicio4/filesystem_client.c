#include <stdio.h>
#include "filesystem.h"

void parse_args(int argc, char* argv[], char** host, char** filename)
{
	if (argc < 3) {
		printf ("usage: %s hostname filename\n", argv[0]);
		exit (1);
	}
	*host = argv[1];
	*filename = argv[2];
}

// Open file to append data
FILE* createOrOpenFile(char* filename) {
	return fopen(filename, "a");
}

void closeFile(FILE* fileD) {
	fclose(fileD);
}

int updateFile(FILE* fileD, char* buffer, int size) {
	return fwrite(buffer, sizeof(char), size, fileD);
}

void sentFileToServer(char* newFilename, CLIENT* client, int buffer_size) {
	FILE* fileD = fopen(newFilename, "r");
	printf ("newFilename: %s\n", newFilename);

	fseek(fileD, 0L, SEEK_END);
	int file_size = ftell(fileD);

	char* buffer = calloc(buffer_size, sizeof(char));
	int currentReadBytes = 0;
	enum clnt_stat status;

	printf ("currentReadBytes: %d \t file_size: %d\n", currentReadBytes, file_size);

	do {
		int readSize = buffer_size;
		if (readSize > (file_size - currentReadBytes)) {
			readSize = file_size - currentReadBytes;
		}

		//read to buffer.
		fseek(fileD, currentReadBytes, SEEK_SET);
		int readBytes = fread(buffer, sizeof(char), readSize, fileD);

		write_request w_request;
		w_request.name = newFilename;
		w_request.size = readBytes;
		w_request.buffer = buffer;

		int serverReadSize = 0;
		status = pdytr_write_1(w_request, &serverReadSize, client);

		if (status != RPC_SUCCESS) {
			clnt_perror (client, "call failed");
		}

		currentReadBytes += serverReadSize;
		printf ("currentReadBytes: %d \t serverReadSize: %d\t readBytes: %d\n", currentReadBytes, serverReadSize, readBytes);

	} while (currentReadBytes < file_size);

	free(buffer);
	closeFile(fileD);
}

int main (int argc, char* argv[])
{
	char* host;
	char* filename;
	parse_args(argc, argv, &host, &filename);

	CLIENT*	client = clnt_create (host, FS_PROG, FS_VERSION, "udp");
	if (client == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}

	int file_size = 0;
	enum clnt_stat status = pdytr_file_size_1(filename, &file_size, client);
	if (status != RPC_SUCCESS) {
		clnt_perror (client, "call failed");
	}

	int buffer_size = 255;

	read_request request;
	request.name = filename;
	request.offset = 0;
	request.size = buffer_size;

	read_response* response = malloc(sizeof(read_response));
	response->buffer = calloc(buffer_size, sizeof(char));

	int currentBytes = 0;
	char* newFilename = calloc(strlen(filename)+7, sizeof(char));
	newFilename = strcat(newFilename, "client-");
	newFilename = strcat(newFilename, filename);

	FILE* fileD = createOrOpenFile(newFilename);
	do {
		status = pdytr_read_1(request, response, client);

		if (status != RPC_SUCCESS) {
			clnt_perror (client, "call failed");
		}

		currentBytes+= response->size;
		request.offset = currentBytes;

		updateFile(fileD, response->buffer, response->size);

	} while (currentBytes < file_size);
	closeFile(fileD);

	free(response->buffer);
	free(response);

	sentFileToServer(newFilename, client, buffer_size);

	clnt_destroy (client);

	exit (0);
}
