#include "timeout.h"

bool_t some_function_1_svc(int *result, struct svc_req *rqstp)
{
	printf("SERVER: some_function_1_svc start\n");
	// sleep(26); Exercise 5.A
	printf("SERVER: some_function_1_svc finish\n");
	return 1;
}

bool_t some_function_to_22_1_svc(int *result, struct svc_req *rqstp)
{
	printf("SERVER: some_function_to_22_1_svc start\n");
	sleep(22);
	printf("SERVER: some_function_to_22_1_svc finish\n");
	return 1;
}

bool_t some_function_to_26_1_svc(int *result, struct svc_req *rqstp)
{
	printf("SERVER: some_function_to_26_1_svc start\n");
	sleep(26);
	printf("SERVER: some_function_to_26_1_svc finish\n");
	return 1;
}

int
to_prog_1_freeresult (SVCXPRT *transp, xdrproc_t xdr_result, caddr_t result)
{
	xdr_free (xdr_result, result);

	return 1;
}
