#include "timeout.h"

double dwalltime();

void timeout_22(CLIENT* clnt) {
	enum clnt_stat retval_22;
	int result_22;

	printf("CLIENT: some_function_to_22_1 start\n");

	double startTime = dwalltime();
	retval_22 = some_function_to_22_1(&result_22, clnt);
	double endTime = dwalltime();
	double delta = (endTime - startTime);

	printf("CLIENT: some_function_to_22_1 finish\n");

	printf("timeout_22: Send time %f ms\n", delta * 1000);
}

void timeout_25(CLIENT* clnt) {
	enum clnt_stat retval_25;
	int result_25;

	printf("CLIENT: some_function_to_26_1 start\n");

	double startTime = dwalltime();
	retval_25 = some_function_to_26_1(&result_25, clnt);
	double endTime = dwalltime();
	double delta = (endTime - startTime);

	if (retval_25 != RPC_SUCCESS) {
		clnt_perror (clnt, "call failed - should be timeout");
	}

	printf("CLIENT: some_function_to_26_1 finish\n");

	printf("some_function_to_26_1: Send time %f ms\n", delta * 1000);
}

int main (int argc, char *argv[])
{
	char *host;

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];

	CLIENT* clnt;
	enum clnt_stat retval_1;
	int result_1;

	clnt = clnt_create (host, TO_PROG, TO_VERSION, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}


	int iterations = 10;
	double* times = calloc(iterations, sizeof(double));
  double totalTime = 0;

	for (int i = 0; i < iterations; i++) {
		printf("# %d\n", i);
		printf("CLIENT: to_prog_1 start\n");

		double startTime = dwalltime();

		retval_1 = some_function_1(&result_1, clnt);

		double endTime = dwalltime();
		double delta = (endTime - startTime) / 2;
		times[i] = delta;
		totalTime += delta;
		printf("Send time %f ms\n", delta * 1000);

		printf("CLIENT: to_prog_1 finish\n");
	}
	printf("\nAverage time %f ms\n", (totalTime * 1000) / iterations);
	fflush(stdout);
	timeout_22(clnt);
	timeout_25(clnt);

	clnt_destroy (clnt);

exit (0);
}

/*****************************************************************/

#include <sys/time.h>

double dwalltime()
{
	double sec;
	struct timeval tv;

	gettimeofday(&tv,NULL);
	sec = tv.tv_sec + tv.tv_usec/1000000.0;
	return sec;
}
