/*
 * CS 532: pingpong.c: play a game
 */

/*
 * pingpong: two threads take turns playing a game
 *
 * build like this ---> gcc -g -Wall -Werror pingpong.c -o pingpong -lpthread
 * 
 * the code as-is has no synchronization, and so it fails.
 * the correct output should look like this:

PING
PONG
PING
PONG
.
.
.

 * your assignment is to add synchronization so that the two threads take turns correctly.
 *
 */

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define PING 0
#define PONG 1

volatile unsigned NumRounds = 0;
volatile unsigned PrevVal = PONG;

// Declaration of thread condition variable
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

// declaring mutex
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;


char *Message[2] = {"PING", "PONG"};

/*
 * pring the message, either "PING" or "PONG".
 * also checks to make sure that the correct message is being printed
 */
void pingpongprint(int thisval)
{
  /* if the threads are taking turns then PrevVal should always be different from thisval */
  if (PrevVal == thisval) {
    fprintf(stderr, "ERROR: received '%s' but expected '%s'\n", Message[thisval], Message[!thisval]);
    exit(-1);
  }

  printf("%s\n", Message[thisval]);
  PrevVal = thisval;
}

void *PingerPonger(void *tidptr)
{
	int tid = *((int *) tidptr);
	
	for (unsigned dex = 0; dex <= NumRounds; dex++) 
	{
		if(PrevVal == tid)
		{
			//acquire a lock
			pthread_mutex_lock(&lock);
			while(PrevVal == tid)
				pthread_cond_wait(&cond, &lock);
			//release the lock
			pthread_mutex_unlock(&lock);
		}
		
		//acquire a lock
		pthread_mutex_lock(&lock);
		pingpongprint(tid);
		pthread_cond_signal(&cond);
		//relese the lock
		pthread_mutex_unlock(&lock);
		
	}

  	return NULL;
}


int main(int argc, char *argv[])
{
  if (argc != 2) {
    fprintf(stderr, "USAGE: %s <numrounds>\n", argv[0]);
    exit(-1);
  }

  NumRounds = atoi(argv[1]);
  if (NumRounds < 1) {
    fprintf(stderr, "ERROR: NumRounds must be >= 1\n");
    exit(-1);
  }

  int ping_tid = PING;
  int pong_tid = PONG;

  pthread_t pingthread;
  pthread_t pongthread;
  int rc;
 
  rc = pthread_create(&pingthread, NULL, PingerPonger, (void *) &ping_tid);
  if (rc) {
    fprintf(stderr, "ERROR; could not create PING thread. return code from pthread_create() is %d\n", rc);
    exit(-1);
  }

  rc = pthread_create(&pongthread, NULL, PingerPonger, (void *) &pong_tid);
  if (rc) {
    fprintf(stderr, "ERROR; could not create PONG thread. return code from pthread_create() is %d\n", rc);
    exit(-1);
  }

  rc = pthread_join(pingthread, NULL);
  if (rc != 0) {
    fprintf(stderr, "ERROR joining with PING (rc==%d)\n", rc);
    exit(-1);
  }

  rc = pthread_join(pongthread, NULL);
  if (rc != 0) {
    fprintf(stderr, "ERROR joining with PONG (rc==%d)\n", rc);
    exit(-1);
  }

  printf("SUCCESS!  (parent exiting)\n");
  return(0);
}

