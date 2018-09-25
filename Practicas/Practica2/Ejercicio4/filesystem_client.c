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

	int buffer_size = 255;

	read_request request;
	request.name = filename;
	request.offset = 0;
	request.size = buffer_size;

	read_response* response = malloc(sizeof(read_response));
	response->buffer = calloc(buffer_size, sizeof(char));
	enum clnt_stat status = read_1(request, response, client);

	if (status != RPC_SUCCESS) {
		clnt_perror (client, "call failed");
	}

	printf("%d\n", response->size);
	printf("%s\n", response->buffer);

	free(response->buffer);
	free(response);

	clnt_destroy (client);

	exit (0);
}
