#include <stdio.h>
#include <stdlib.h>

int vadd(int *ints, int n) {
	static int  result;
	int i;
	
	printf("Got request: adding %d numbers\n", n);

	result=0;
	for (i=0;i<n;i++)
	  result += ints[i];

	return (result);
}

int main( int argc, char *argv[]) {
  int *ints,n;
  int i;
  int res;
  if (argc<2) {
    fprintf(stderr,"Usage: %s num1 num2 ...\n",argv[0]);
    exit(0);
  }

  n = argc-1;
  ints = (int *) malloc(n * sizeof( int ));
  
  for (i=1;i<argc;i++) {
    ints[i-1] = atoi(argv[i]);
  }

  res = vadd(ints,n);
  printf("%d",ints[0]);
  for (i=1;i<n;i++) 
    printf(" + %d",ints[i]);
  printf(" = %d\n",res);
  return(0);
}



