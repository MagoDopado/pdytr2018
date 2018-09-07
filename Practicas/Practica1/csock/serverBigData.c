/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#ifndef true
#define true 1
#endif
#ifndef false
#define false 0
#endif

/* Time in seconds from some point in the past */
double dwalltime();
uint32_t adler32(unsigned char *data, size_t len);

void error(char *msg)
{
    perror(msg);
    exit(1);
}

void validateArgs(int argc, char* argv[]) {
  if (argc < 2) {
      fprintf(stderr,"ERROR, no port provided\n");
      exit(1);
  }
}

int main(int argc, char *argv[])
{
  validateArgs(argc, argv);
  int portno = atoi(argv[1]);
  // Generate biding socket.
  int sockfd = socket(AF_INET, SOCK_STREAM, 0);
  if (sockfd < 0) error("ERROR opening socket");

  struct sockaddr_in serv_addr;
  bzero((char *) &serv_addr, sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_addr.s_addr = INADDR_ANY;
  serv_addr.sin_port = htons(portno);
  if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
    error("ERROR on binding");

  // Process messages.
  do {
    // Get a connection socket.
    listen(sockfd, 5);
    struct sockaddr cli_addr;
    int cliLen = sizeof(cli_addr);
    int newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &cliLen);
    if (newsockfd < 0) {
      printf("ERROR on accept");
      continue;
    }

    // Receive data size.
    double startTime = dwalltime();
    int size = 0;
    int readBytes = read(newsockfd, &size, sizeof(int));
    if (readBytes < 0) {
      printf("ERROR reading from socket\n");
      continue;
    }

    // allocate data buffer.
    if (size <= 0) {
      printf("Bad buffer size\n");
      continue;
    }
    // Assuere null terminated buffer.
    char* buffer = calloc(size + 1, sizeof(char));

    // Read Data.
    int currentBytes = 0;
    readBytes = 0;
    do {
      int missingBytes = size - currentBytes;
      printf("atempting to read %d\n", missingBytes -1);
      readBytes = read(newsockfd, buffer + currentBytes, missingBytes);
      currentBytes += readBytes;
    } while(readBytes > 0 && currentBytes < size);
    if (readBytes < 0) {
      free(buffer);
      printf("ERROR reading from socket\n");
      continue;
    }

    uint32_t integrity = 0;
    readBytes = read(newsockfd, &integrity, sizeof(uint32_t));
    if (readBytes < 0) {
      printf("ERROR reading from socket\n");
      continue;
    }

    double endTime = dwalltime();
    double delta = endTime - startTime;
    printf("Receive time %f ms.\n", delta);

    if (integrity == adler32(buffer, size)) {
      // Send correct checksum.
      int writtenBytes = write(newsockfd, &integrity, sizeof(uint32_t));
      if (writtenBytes < 0) error("ERROR writing to socket");
    }
    else {
      char* errorMessage = "Bad checksum.";
      int writtenBytes = write(newsockfd, errorMessage, strlen(errorMessage));
      if (writtenBytes < 0) error("ERROR writing to socket");
    }

    //Output.
    // printf("Here is the message: %s\n", buffer);
    fflush(stdout);
    free(buffer);
    close(newsockfd);
  } while(true);
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

const uint32_t MOD_ADLER = 65521;
uint32_t adler32(unsigned char *data, size_t len)
{
    uint32_t a = 1, b = 0;
    size_t index;

    // Process each byte of the data in order
    for (index = 0; index < len; ++index)
    {
        a = (a + data[index]) % MOD_ADLER;
        b = (b + a) % MOD_ADLER;
    }

    return (b << 16) | a;
}
