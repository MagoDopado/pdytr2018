/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include <memory.h> /* for memset */
#include "filesystem.h"

/* Default timeout can be changed using clnt_control() */
static struct timeval TIMEOUT = { 25, 0 };

enum clnt_stat 
pdytr_read_1(read_request req, read_response *clnt_res,  CLIENT *clnt)
{
	return (clnt_call(clnt, pdytr_read,
		(xdrproc_t) xdr_read_request, (caddr_t) &req,
		(xdrproc_t) xdr_read_response, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
pdytr_write_1(write_request req, int *clnt_res,  CLIENT *clnt)
{
	return (clnt_call(clnt, pdytr_write,
		(xdrproc_t) xdr_write_request, (caddr_t) &req,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
pdytr_file_size_1(filename name, int *clnt_res,  CLIENT *clnt)
{
	return (clnt_call(clnt, pdytr_file_size,
		(xdrproc_t) xdr_filename, (caddr_t) &name,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}
