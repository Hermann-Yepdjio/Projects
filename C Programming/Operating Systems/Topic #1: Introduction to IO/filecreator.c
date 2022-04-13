#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <limits.h>
#include <fcntl.h>

#define f_name "unsorted"
volatile unsigned int file_id = 0ULL;
char* d;
int r, t, f;
int num_file_created = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

int *num_file_created_per_thread; //to keep track of how many files each thread creates
int max_num_file_per_thread;
unsigned int seed;

void *create_file(void* param)
{
	
	while(num_file_created < f)
	{

		char path[100]; 
		int t_id = *((int *)param);
		pthread_mutex_lock(&mutex);
		if(num_file_created >= f || num_file_created_per_thread[t_id] >= max_num_file_per_thread)
		{
			pthread_mutex_unlock(&mutex);
			break;
		}
		else
		{
			num_file_created++;
			num_file_created_per_thread[t_id]++;

			//constitute the path to the file where the numbers will be stored
			sprintf(path, "%s%s%d%s", d, "/unsorted_", num_file_created - 1, ".bin");
			printf("Thread #%d is creating file #%d.\n\n", t_id, num_file_created - 1);
		}
		pthread_mutex_unlock(&mutex);
		
		int fd = open(path, O_CREAT|O_WRONLY);
		if (fd < 0)
		{
			fprintf(stderr, "Make sure the directory exists and is writable \n");
			exit(-1);

		}
		unsigned int rand_int;
		for(int i = 0; i < r; i++)
		{
			rand_int = (rand_r(&seed) % INT_MAX); //generate a 32-bit random int
			size_t rv = write(fd, &rand_int, sizeof(rand_int));
			
			if(rv == 0)	
			{
				fprintf(stderr, "Not enough physical memory space \n");
				exit(-1);

			}
		}

		close(fd);
	}

	return NULL;
	
}

int main(int argc, char *argv[])
{
	if (argc != 5) 
	{
		fprintf(stderr, "USAGE: %s <Location of Directory> <# files to create> <# integers per file> <Nthreads> \n", argv[0]);
		exit(-1);
	}
	
	d = argv[1];
	f = atoi(argv[2]);
	r = atoi(argv[3]);
	t = atoi(argv[4]);
	if (t < 1) 
	{
		fprintf(stderr, "ERROR: numthreads must be >= 1 \n");
		exit(-1);
	}

	if (f < 1)
        {
                fprintf(stderr, "ERROR: numfiles must be >= 1 \n");
                exit(-1);
        }

	if (r < 1)
        {
                fprintf(stderr, "ERROR: numints must be >= 1 \n");
                exit(-1);
        }
	
	seed = time(NULL);	
	pthread_t threads[t];
	int rc;
	max_num_file_per_thread = (f + t - 1) / t;



	//record thread ids for debugging purposes
	int *thread_ids = malloc(sizeof(int) * t);
	for(int i = 0; i < t; i++)
		thread_ids[i] = i;

	num_file_created_per_thread = calloc(t, sizeof(int)); //to keep track of how many files each thread creates

	/* start all of the threads */
	for (int i = 0; i < t; i++)
	{
		rc = pthread_create(&threads[i], NULL, create_file, (void *) &thread_ids[i]);
    		if (rc)
		{
      			printf("ERROR; return code from pthread_create() is %d\n", rc);
      			exit(-1);
    		}
  
	}

	/* wait for threads to complete */
	for (int i = 0; i < t; i++) 
	{
		rc = pthread_join(threads[i], NULL);
		if (rc != 0) 
		{
			fprintf(stderr, "ERROR joining with thread %d (error==%d)\n", thread_ids[i], rc);
			exit(-1);
		}
	}


	//print a summary of the number of files that was created by each thread
	printf("\n\n\n-----------------------------------------Summary------------------------------------------------\n\n");
	for(int i = 0; i < t; i++)
		printf("Thread #%d created %d files.\n", i, num_file_created_per_thread[i]);

	free(thread_ids);
	free (num_file_created_per_thread);

	
	return(0);
}

