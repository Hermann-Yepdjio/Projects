#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/file.h>

int main(int argc, char *argv[])
{
	FILE *file;	
	struct timeval tv; //declare a structure timeval to handle client timeout
	tv.tv_sec=30;  //set default time out value
	tv.tv_usec=0;
	fd_set set; //declare file descriptor
	int converted_yes= htonl(0), //convert number from host to network byte order
	    converted_no=  htonl(1);
	struct sockaddr_in server;
	char message[1000];
	pthread_mutex_t mutex;
	int sock, converted_Number, converted_ValueBeingTested; //declare variables
	sock= socket(AF_INET, SOCK_STREAM, 0); //create socket 
	if (sock==-1)  //check if socket was created successfully or print error message
		 printf ("Sorry! Socket could not be created");
	puts("Socket created!"); //print msg to the user
	server.sin_family = AF_INET; //set client to communicate with ipv4 addresses
	if(argc==1 || argc<4) //check if default values should be used for connection
	{	
		int count=0;
		char* arg[100];
		if (argc==2)
		{	
			char str[1000];
			file= fopen(argv[1], "r");
			if (file)
			{

				if (fread(str, 1, sizeof(str), file)>0) //read arguments from file
				{
					char *token=strtok(str, "\n\t\r");
					while (token!=NULL&& count<4)
					{
						
						if (count == 0)
							server.sin_addr.s_addr = inet_addr(token);
						if (count == 1)
							server.sin_port=htons(atoi(token));
						if (count == 2)
							tv.tv_sec=atoi(token);
						
						count++;
						token= strtok(NULL, "\n\t\r");
					}
					if (ferror(file))
						perror ( "Sorry an error occured!");
				}
			}
			else
			{
				perror ("Sorry this file does not exist");
				return -1;
			}
			fclose(file);
		}	
		if(argc!=1 && argc!=2 || count<4) //check if user didn't provide enough argument and print error msg
		{
			puts(" Sorry! You didn't provide enough arguments. You should either provide no argument or provide at least 3 arguments in the following order\n the connection IP address, the port number and the timeout.\n Because you didn't provide enough arguments this client will use the default arguments.");
			setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, (const char*)&tv, sizeof(struct timeval)); //set timeout for receive
			setsockopt(sock, SOL_SOCKET, SO_SNDTIMEO, (const char*)&tv, sizeof(struct timeval)); //set timeout for send
			server.sin_addr.s_addr = inet_addr("127.0.0.1"); //set ip address connection
			server.sin_port = htons(8888); //set port connection
		}
	}
	else //check if the user provide enough arguments
	{ 
	       	tv.tv_sec= atoi(argv[3]); //convert from string to int
		setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, (const char*)&tv, sizeof(struct timeval)); //set timeout for receive
		setsockopt(sock, SOL_SOCKET, SO_SNDTIMEO, (const char*)&tv, sizeof(struct timeval)); //set timeout for send
		server.sin_addr.s_addr = inet_addr(argv[1]); //set ip address
		server.sin_port= htons(atoi(argv[2])); //set port
	}
	
	fcntl(sock, F_SETFL, O_NONBLOCK); //set sock in blocking mode
	int status=connect(sock, (struct sockaddr *)&server, sizeof(server)); //connect to server
	if (status<0)
	{ 
		if(errno!= EINPROGRESS)
		{
			perror("Failed to connect to the server! Error.\nPlease make sure u start the server first");
			return -1;
		}
		FD_ZERO(&set); //initialize file descriptor
		FD_SET(sock, &set); //set bits for file descriptor
		select(sock+1, NULL, &set, NULL, &tv);
	}
	fcntl(sock, F_SETFL, fcntl(sock, F_GETFL,0) & ~O_NONBLOCK); //set sock back in non-blocking mode
	puts ("Connected\n"); // print msg tothe user
	while(1) //keep communication with the server
	{
		if (recv(sock, &converted_Number, sizeof(converted_Number), 0)<0 || recv(sock, &converted_ValueBeingTested, sizeof(converted_ValueBeingTested),0)<0) //receive msg from server
		{	
				puts("No message received"); //print if no msg was received
				break;
		}
		printf("2 messages received from server : %i%s%i%s", ntohl(converted_Number), " and ", ntohl(converted_ValueBeingTested), ".\n\n" ); //print msg to user
		if (ntohl(converted_Number)%ntohl(converted_ValueBeingTested)==0) //check if divisor receiver from server divides "number"
		{
			if (write (sock, &converted_yes, sizeof(converted_yes))<0) //if yes  write 0 to the server
			{
				perror("Failed to send message"); //print error if any
				exit(-1);
			}
			exit(0);
		}
		else 
		{
			if (write(sock,&converted_no, sizeof(converted_no))<0) //if not send 1 to server
			{
				printf("Failed to send message"); //print error if any
				exit(-1);
			}
		}
	}
		close(sock); //close socket
		return 0;
}
