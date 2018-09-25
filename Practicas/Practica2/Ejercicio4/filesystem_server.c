#include "filesystem.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct svc_req rpc_request;

char* readFromFile(char* filename, int* size) {
  //openfile and get size
  FILE* fileD = fopen(filename, "r");
  if (fileD == 0) {
    return (char*) NULL;
  }

  fseek(fileD, 0L, SEEK_END);
  *size = ftell(fileD);
  fseek(fileD, 0L, SEEK_SET);
  char* buffer = calloc(*size, sizeof(char));

  //read to buffer.
  fread(buffer, sizeof(char), *size, fileD);

  //free(filename);
  fclose(fileD);

  return buffer;
}

bool_t read_1_svc(read_request request, read_response* result,  rpc_request* rpc)
{
	int size = 0;

	char* buffer = readFromFile(request.name, &size);
	if (buffer == (char*) NULL) {
    printf("Error opening file\n");
		return (bool_t) 0;
	}
	buffer += request.offset;
	int bytes_to_read = request.ammount;
	if (size < (request.offset + request.ammount)) {
		bytes_to_read = size - request.offset;
	}

	result->buffer = calloc(bytes_to_read + 1, sizeof(char));
	memcpy(result->buffer, buffer, bytes_to_read);
	result->ammount = bytes_to_read;


	return (bool_t) 1;
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
