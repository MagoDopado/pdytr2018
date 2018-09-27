#include "filesystem.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

typedef struct svc_req rpc_request;

int sizeFromFile(char* filename) {
	//Validate file size.
	FILE* fileD = fopen(filename, "r");
	if (fileD == 0) {
		return 0;
	}

	// Get filesize and validate request sizes.
	fseek(fileD, 0L, SEEK_END);
	int file_size = ftell(fileD);

	fclose(fileD);
	return file_size;
}

// Open file to append data
FILE* openOrCreateFile(char* filename) {
	char* newFilename = calloc(strlen(filename)+7, sizeof(char));
	newFilename = strcat(newFilename, "server-");
	newFilename = strcat(newFilename, filename);
	return fopen(newFilename, "a");
}

void closeFile(FILE* fileD) {
	fclose(fileD);
}

int updateFile(FILE* fileD, char* buffer, int size) {
	return fwrite(buffer, sizeof(char), size, fileD);
}

int readFromFile(char* filename, int offset, int size, char** buffer) {
	*buffer = (char*) NULL;
	// Assume size already validated.

	//Validate file size.
	FILE* fileD = fopen(filename, "r");
	if (fileD == 0) {
		return 0;
	}

	// Get filesize and validate request sizes.
	fseek(fileD, 0L, SEEK_END);
	int file_size = ftell(fileD);

	// Don't read out of stream.
	if (file_size < offset) {
		fclose(fileD);
		return 0;
	}
	if (file_size < offset + size) {
		size = file_size - offset;
	}

	// File reading.
	*buffer = calloc(size, sizeof(char));
	fseek(fileD, offset, SEEK_SET);
	int correctlyRead = fread(*buffer, sizeof(char), size, fileD);
	fclose(fileD);

	return correctlyRead;
}

bool_t pdytr_read_1_svc(read_request request, read_response* result,  rpc_request* rpc)
{
	if (request.size <= 0) {
		printf("Invalid file size requested.\n");
		return (bool_t) FALSE;
	}

	char* buffer;
	int correctlyRead = readFromFile(request.name, request.offset, request.size, &buffer);
	if (correctlyRead == 0 || buffer == (char*) NULL) {
		printf("Error opening/reading file.\n");
		return (bool_t) FALSE;
	}

	result->buffer = buffer;
	result->size = correctlyRead;
	printf("Served %d bytes\t", result->size);

	return (bool_t) TRUE;
}

bool_t pdytr_write_1_svc(write_request req, int *result,  rpc_request* rpc)
{

	FILE* fileD = openOrCreateFile(req.name);
	*result = updateFile(fileD, req.buffer, req.size);
	closeFile(fileD);

	printf("Recibe %d bytes\n", req.size);

	return (bool_t) TRUE;
}

bool_t pdytr_file_size_1_svc(char* filename, int* result,  rpc_request* rpc)
{
	*result = sizeFromFile(filename);
	return (bool_t) TRUE;
}

int
fs_prog_1_freeresult (SVCXPRT *transp, xdrproc_t xdr_result, caddr_t result)
{
	xdr_free (xdr_result, result);

	return 1;
}
