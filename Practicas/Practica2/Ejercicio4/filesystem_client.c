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

	CLIENT*	clnt = clnt_create (host, FS_PROG, FS_VERSION, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}

	read_request request;
	request.name = filename;
	request.offset = 0;
	request.ammount = 255;

	read_response response;
	enum clnt_stat status = read_1(request, &response, clnt);
	if (status != RPC_SUCCESS) {
		clnt_perror (clnt, "call failed");
	}

	printf("%s\n", response.buffer);

	clnt_destroy (clnt);

	exit (0);
}
