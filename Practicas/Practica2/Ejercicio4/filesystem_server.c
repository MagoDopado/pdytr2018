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

int readFromFile(char* filename, int offset, int ammount, char** buffer) {
	*buffer = (char*) NULL;
	// Assume ammount already validated.

	//Validate file size.
	FILE* fileD = fopen(filename, "r");
	if (fileD == 0) {
		return 0;
	}

	// Get filesize and validate request ammounts.
	fseek(fileD, 0L, SEEK_END);
	int file_size = ftell(fileD);

	// Don't read out of stream.
	if (file_size < offset) {
		fclose(fileD);
		return 0;
	}
	if (file_size < offset + ammount) {
		ammount = file_size - offset;
	}

	// File reading.
	*buffer = calloc(ammount, sizeof(char));
	fseek(fileD, offset, SEEK_SET);
	int correctlyRead = fread(*buffer, sizeof(char), ammount, fileD);
	fclose(fileD);

	return correctlyRead;
}

bool_t read_1_svc(read_request request, read_response* result,  rpc_request* rpc)
{
	if (request.ammount <= 0) {
		printf("Invalid file size requested.\n");
		return (bool_t) FALSE;
	}
	
	char* buffer;
	int correctlyRead = readFromFile(request.name, request.offset, request.ammount, &buffer);
	if (correctlyRead == 0 || buffer == (char*) NULL) {
		printf("Error opening/reading file.\n");
		return (bool_t) FALSE;
	}

	result->buffer = buffer;
	result->ammount = correctlyRead;
	printf("Served %d bytes\n", result->ammount);

	return (bool_t) TRUE;
}

bool_t write_1_svc(write_request req, int *result,  rpc_request* rpc)
{
	bool_t retval;

	/*
	 * insert server code here
	 */

	return retval;
}

int
fs_prog_1_freeresult (SVCXPRT *transp, xdrproc_t xdr_result, caddr_t result)
{
	xdr_free (xdr_result, result);

	/*
	 * Insert additional freeing code here, if needed
	 */

	return 1;
}
