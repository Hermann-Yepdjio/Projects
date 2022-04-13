#include <string.h>
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <pthread.h>
#include <math.h>
#include <limits.h>
#include <stdlib.h>

int socket_desc , c , read_size, client_message; //declare variables
struct sockaddr_in server, client;  //declare strutures server and clients
pthread_t thread[50];  // array to hold thread IDs
pthread_mutex_t mutex; //declare mutex
int i,number,sqrtNumber,valueBeingTested=2, numClients=0, num=0, testValue=0, array_flag[50];  //declare variables


void* communicateWithClient(void *id)	 //function to handle communication with clients
{
	int client_sock;  //declare local variable
	listen(socket_desc, 50); //listen network to see if a client wants to connect
	pthread_mutex_lock(&mutex); //lock mutex(just to avoid other thread to print before testvalue has been increased
	if (testValue==0)
	{
		puts("Waiting for incomming connectiions...");
		testValue++;
	}
	pthread_mutex_unlock(&mutex);  //unlock mutex
	c= sizeof(struct sockaddr_in); 
	client_sock = accept(socket_desc, (struct sockaddr *)&client, (socklen_t*)&c); //accept connection from client if any
	if (client_sock <0) //check for error and print msg to the user if any
	{
  		perror ("accept failed");
			return (void *)-1;
	}
	puts("connection accepted"); //print msg to the user
	printf("Client %i%s%i%s", num++, " is now Connected.\nNumber of Clients Connected : ", ++numClients, "\n\n"); //print msg to the user
	
	pthread_mutex_lock(&mutex); // lock mutex
	while(valueBeingTested<=sqrtNumber)  //test values from 0 up to square root of "number" to see if they divide "number"
	{      
	   if(array_flag[(int)(intptr_t)id]==0) //case 1: flag set to 0 meaning current client has not performed more divisions than all the the others
	   {
		int converted_Number = htonl(number);  //convert number from host to network byte order
		int converted_ValueBeingTested = htonl(valueBeingTested);	
		write(client_sock, &converted_Number , sizeof(converted_Number)); //send a large number to client
		write(client_sock, &converted_ValueBeingTested, sizeof(converted_ValueBeingTested)); //send a divisor to client
		if (read_size=recv(client_sock, &client_message, sizeof(&client_message), 0)>0) //receive answer from client
		{ 
		       	if (ntohl(client_message)==1) //case1: answer was 1(meaning that the divisor that was sent could not divide "number"
			{
				printf("%s%i%s%i%s%i%s","Message Received from Client :", (int)(intptr_t)id, " : ", number, " is not divisible by ", valueBeingTested,".\n\n"); //print msg to the user
				valueBeingTested++;  //increase divisor by 1
			}
			else  // case 2: answer was 0(meaning divisor could devide "number"
			{
				printf("Client %i%s%s%i%s",(int)(intptr_t)id, " found a divisor ", ". So, ",number , " is not prime.\n\n Server disconnected!\n"); //print successful msg to the user
				exit(0);
			}
		}
		if(read_size==0) //check if client was disconnected and print msg to the user
		{
			printf("Client :%i%s%i%s", (int)(intptr_t)id, " Disconnected. \nNumber of Clients currently connected: ", --numClients, "\n\n");
			close(client_sock); //close socket
			return id;
		}
		else if (read_size == -1) //check if error and print msg if any
			perror("reception failed");
	   }
	   else //case 2: flag is set to 1 meaning current client has performed more( or the same amount of) divisions than some/all of the others
	   {
		int count=0; 
		for (i=0;i<50;i++) //check if all client flags are set to 1
		{
			if (array_flag[i]==1)
				count++;
		}
		if (count==numClients) //reset all the flags if they were all set to 1
			memset(array_flag, 0 , sizeof(array_flag)); //set all values of the array to 0
	   }
		pthread_mutex_unlock(&mutex); //unlock mutex

	}
	printf ("No factor was found! So %i%s", number, " is prime.\n\nServer disconnected"); //print msg to the user
	exit(0); //exit
	return id;

}
int main(int argc, char *argv[])
{

	if (pthread_mutex_init(&mutex, NULL)!=0) //initialize mutex
	{
		puts("Mutex initialization failed!"); //print error if any
		exit(-1);
	}
	srand(clock()); //set rand function to generate numbers base on the clock
	number= 10000000 + rand()%(INT_MAX-10000000); //generate a random number
//	number= 314606891;  //example of prime number
	sqrtNumber= sqrt(number)+1; //calculate the squareroot of "number"
	printf("This program will determine if %i%s", number, " is prime or not.\n\n"); //print msg to the user
	socket_desc= socket(AF_INET, SOCK_STREAM, 0); //create socket 
	if(socket_desc == -1) //check if error and print message if any
	{
		printf("could not create socket");
		return -1;
	}
	puts("Socket created"); //print msg to the user
	
	server.sin_family = AF_INET;  //allows communication wiht ipv4 addresses
	server.sin_addr.s_addr= INADDR_ANY; //allows server to accept connection from any local IP address
	server.sin_port= htons(8888); //set port number
	
  	if(bind(socket_desc, (struct sockaddr *)&server, sizeof(server))<0) //bind the socket
	{
		perror("bind failed! Error"); //print error if any
		return -1;
	}
	printf("Bind established\n"); //print msg to the user
	for(i=0; i<50; i++) //create 50 threads
	{
		if (pthread_create(&thread[i], 0, communicateWithClient,(void *)(intptr_t)i)!=0)
		{
			perror ("sorry! Failed to create a thread"); //print error if any
			return -1;
		}
	}
	for (i=0; i<50; i++) //join 50 threads
		pthread_join(thread[i], 0);
	pthread_mutex_destroy(&mutex); //destroy mutex
	return 0;

}
