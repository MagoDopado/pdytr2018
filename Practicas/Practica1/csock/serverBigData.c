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
    if (newsockfd < 0) error("ERROR on accept");

    // Receive.
    int size = 0;
    int readBytes = read(newsockfd, &size, sizeof(int));
    if (readBytes < 0) {
      printf("ERROR reading from socket");
      continue;
    }
    if (size < 0) {
      printf("Bad buffer size");
      continue;
    }

    char* buffer = malloc(size);
    bzero(buffer, size);
    int totalBytes = 0;
    int currentBytes = 0;
    do {
      int missingBytes = size - totalBytes;
      printf("atempting to read %d\n", missingBytes -1);
      currentBytes = read(newsockfd, buffer, missingBytes);
      totalBytes += currentBytes;
    } while(currentBytes && totalBytes < size);

    // Send.
    int writtenBytes = write(newsockfd, "I got your message", 18);
    if (writtenBytes < 0) error("ERROR writing to socket");

    //Output.
    printf("Here is the message: %s\n", buffer);
  } while(true);
  return 0;
}
