#include <pthread.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/io.h>
#include <sys/mman.h>
#include <stdlib.h>
#include <dirent.h>


int t;
int num_ints_per_file = 0;
char* d;
int num_files_verified = 0;
int tot_num_files = 0;

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *check_files(void *param)
{
	int *u_nums, *s_nums, tmp;
	int t_id = *((int *)param);
	char unsorted[40], sorted[40];
	while(num_files_verified < tot_num_files)
	{
		pthread_mutex_lock(&mutex);
		if(num_files_verified >= tot_num_files)
		{
			pthread_mutex_unlock(&mutex);
			break;
		}
		else
		{
			tmp = num_files_verified;
			num_files_verified++;
			printf("Thread #%d is checking if sorted_%d.bin is the sorted version of unsorted_%d.bin.\n", t_id, tmp, tmp);
		}
		
		pthread_mutex_unlock(&mutex);

		sprintf(unsorted, "%s/%s%d%s", d, "unsorted_", tmp, ".bin");
		sprintf(sorted, "%s/%s%d%s", d, "sorted/sorted_", tmp, ".bin");

		int u_fd = open(unsorted, O_RDONLY);	
		int s_fd = open(sorted, O_RDONLY);

		if (u_fd < 0 || s_fd < 0)
		{
			fprintf(stderr, "Make sure the directory exists and is readable.\n");
			exit(-1);
		}

		u_nums = (int *) mmap (0, num_ints_per_file, PROT_READ, MAP_PRIVATE, u_fd, 0);
		s_nums = (int *) mmap (0, num_ints_per_file, PROT_READ, MAP_PRIVATE, s_fd, 0);

		int *visited = calloc(num_ints_per_file, sizeof(int));
		for(int i = 0; i < num_ints_per_file; i++)
		{
			int j;
			for(j = 0; j < num_ints_per_file; j++)
			{
				//printf("%d %d\n", u_nums[i], s_nums[j]);
				if(u_nums[i] == s_nums[j] && visited[j] == 0)
				{
					visited[j] = 1;
					break;
				}
			}

			if ( j == num_ints_per_file)
			{
				fprintf(stderr, "%s is not the sorted version of %s.\n", sorted, unsorted);
				exit(-1);
			}
		}
		
		free(visited);

		//unmap files
		munmap(u_nums, num_ints_per_file);
		munmap(s_nums, num_ints_per_file);

		//close files
		close(u_fd);
		close(s_fd);

	}
}

//get the total number of files to be checked
void get_num_files(char* path)
{
	struct dirent *de;  // Pointer for directory entry

	// opendir() returns a pointer of DIR type.  1st time just to count how many files there are
	DIR *dr = opendir(path);

	if (dr == NULL)  // opendir returns NULL if couldn't open directory
   	{
       		printf("Could not access the specified location" );
		exit(-1);
	}

	while ((de = readdir(dr)) != NULL)
	{
		if(de->d_type!=DT_DIR)
            		tot_num_files++;
	}

    	closedir(dr);
}

//find how many integers are in each file
void get_num_ints_per_file(char* path)
{
	struct stat sb;
	char tmp[100];
	sprintf(tmp, "%s/%s", path, "unsorted_0.bin");
	if (stat(tmp, &sb) == -1)
	{
		fprintf(stderr, "Sorry could not find the first unsorted file %s. Please make sure it exists in the specify directory.\n", tmp);
		exit(-1);
	}
	else
		num_ints_per_file = sb.st_size / sizeof(int);

	printf("\n\nnumber of integer per file is: %d\n\n", num_ints_per_file);

}
int main(int argc, char *argv[])
{
	if (argc != 3) 
	{
		fprintf(stderr, "USAGE: %s <Location of Directory> <# sets of threads> \n", argv[0]);
		exit(-1);
	}
	
	d = argv[1];
	t = atoi(argv[2]);
	if (t < 1) 
	{
		fprintf(stderr, "ERROR: numthreads must be >= 1 \n");
		exit(-1);
	}

	get_num_files(d);
	get_num_ints_per_file(d);

	//record thread ids for debugging purposes
	int *thread_ids = malloc(sizeof(int) * t * 3);
	for(int i = 0; i < t * 3; i++)
		thread_ids[i] = i;

	pthread_t threads[t];
	int rc;

	// start all the threads
	for (int i = 0; i < t; i++)
	{
		rc = pthread_create(&threads[i], NULL, check_files, (void *) &thread_ids[i]);

		if (rc)
		{
      			printf("ERROR; return code from pthread_create() is %d\n", rc);
      			exit(-1);
    		}
	}

	//wait for threads to complete
	for (int i = 0; i < t; i++)
	{
		rc = pthread_join(threads[i], NULL);
		if (rc != 0)
		{
			fprintf(stderr, "ERROR joining with thread %d (error==%d)\n", thread_ids[i], rc);
			exit(-1);
		}
	}

	free(thread_ids);
	//check_files();

	printf("\n\nThe check is complete! All unsorted files have their corresponding sorted files in folder 'sorted'.\n\n");

	return 0;

}
