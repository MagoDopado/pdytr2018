#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <math.h>

/* Time in seconds from some point in the past */
double dwalltime();

void error(char *msg)
{
  perror(msg);
  exit(0);
}

void validateArgs(int argc, char* argv[]) {
  if (argc < 5) {
   fprintf(stderr,"usage %s hostname port filename repetitions\n", argv[0]);
   exit(0);
  }
}

char* readFromInput() {
  printf("Please enter the message: ");
  char* buffer = calloc(256, sizeof(char));
  fgets(buffer, 255, stdin);
  return buffer;
}

char* readFromFile(char* filename, int* size) {
  /*printf("Please enter the filename: ");
  char* filename = calloc(256, sizeof(char));
  fgets(filename, 255, stdin);
  filename[strlen(filename)-1]='\0';*/
  //openfile and get size
  FILE* fileD = fopen(filename, "r");
  if (fileD == 0) {
    error("fopen()");
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

int main(int argc, char *argv[])
{
  validateArgs(argc, argv);
  // Get target address && port.
  struct hostent *server = gethostbyname(argv[1]);
  if (server == NULL) {
    fprintf(stderr,"ERROR, no such host\n");
    exit(0);
  }
  int portno = atoi(argv[2]);

  // Fill socket address struct.
  struct sockaddr_in serv_addr;
  bzero((char *) &serv_addr, sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;
  bcopy((char *)server->h_addr,
    (char *)&serv_addr.sin_addr.s_addr,
    server->h_length);
  serv_addr.sin_port = htons(portno);

  // Data adquisition.
  int bytesToSend = 0;
  char* buffer = readFromFile(argv[3], &bytesToSend);
  int repetitions = atoi(argv[4]);
  double* times = calloc(repetitions, sizeof(double));
  double totalTime = 0;

  for (int i = 0; i < repetitions; i++) {
    // Get socket and connect.
    int sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) error("ERROR opening socket");
    if (connect(sockfd, (const struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
      error("ERROR connecting");

    double startTime = dwalltime();
    // Send.
    int n = write(sockfd, &bytesToSend, sizeof(int));
    if (n < 0) error("ERROR writing to socket");

    int currentBytes = 0;
    int writtenBytes = 0;
    do {
      int remainingBytes = bytesToSend - currentBytes;
      printf("Attempting to send %d\n", remainingBytes -1);
      writtenBytes = write(sockfd, buffer + currentBytes, remainingBytes);
      currentBytes += writtenBytes;
    } while(writtenBytes > 0 && currentBytes < bytesToSend);
    if (writtenBytes < 0)
    {
      error("ERROR writing to socket");
    }

    // Receive.
    char* responseBuffer = calloc(256, sizeof(char));
    n = read(sockfd, responseBuffer, 255);
    if (n < 0) error("ERROR reading from socket");

    double endTime = dwalltime();
    double delta = (endTime - startTime) / 2;
    times[i] = delta;
    totalTime += delta;
    printf("Send time %f ms\n", delta * 1000);

    //Output.
    //printf("%s\n", responseBuffer);
    free(responseBuffer);
  }
  free(buffer);
  double avgTime = totalTime / repetitions;
  printf("Avg Send time %f ms\n", avgTime * 1000);
  double stdDev = 0;
  for (int i = 0; i < repetitions; i++) {
    stdDev += pow(times[i] - avgTime, 2);
  }
  stdDev = stdDev / (repetitions -1);
  stdDev = sqrt(stdDev);
  printf("Standard deviation %f ms\n", stdDev * 1000);

  return 0;
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
