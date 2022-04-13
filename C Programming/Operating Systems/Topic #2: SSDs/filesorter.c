#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <limits.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <libgen.h>
#include <dirent.h>
#include <string.h>

#define limit 10


int num_files_read = 0;
int num_files_sorted = 0;
int num_files_written = 0;
int tot_num_files = 0;
char *d, **file_names;
int t, **nums;
int num_ints_per_file = 0;
char dir_name[100];

pthread_mutex_t mutex_0 = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex_1 = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex_2 = PTHREAD_MUTEX_INITIALIZER;

void insertion_sort(int *arr, int arr_size)
{
	int tmp;
	for (int i = 0; i < arr_size; i++)
	{
		for(int j = i; j >= 0; j--)
		{
			if(arr[j] < arr[j - 1])
			{
				tmp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = tmp;
			}
		}
	}
}

//to read the numbers from the files and store them in nums
void *read_files(void *param)
{
	int t_id = *((int *)param);
        int tmp;
	while(num_files_read < tot_num_files)
	{
		pthread_mutex_lock(&mutex_0);
                if(num_files_read >= tot_num_files)
                {
                        pthread_mutex_unlock(&mutex_0);
                        break;
                }
		else
		{
			while (num_files_read - num_files_written >= limit)
			{
				printf(" The readers are waiting for the writers to catch up as they are 10 files behind");
				sleep(0.0001);
			}

			tmp = num_files_read;
                        num_files_read++;
                        printf("Thread #%d is reading numbers that are in file %s.\n\n", t_id, file_names[tmp]);
		}

		pthread_mutex_unlock(&mutex_0);

		char str[100]; 
		sprintf(str, "%s/%s", d, file_names[tmp]);

		int fd = open(str, O_RDONLY);
		if (fd < 0)
		{
			fprintf(stderr, "Make sure the directory exists and is writable.\n");
			exit(-1);

		}

		for (int i = 0; i < num_ints_per_file; i++)
		{
			read(fd, &nums[tmp][i], sizeof(int));
		}

		close(fd);
		
	}	
}

//sort the numbers
void *sort_file_content(void *param)
{
	int t_id = *((int *)param);
        int tmp;
	while(num_files_sorted < tot_num_files)
	{
		pthread_mutex_lock(&mutex_1);
                if(num_files_sorted >= tot_num_files)
                {
                        pthread_mutex_unlock(&mutex_1);
                        break;
                }
		else
		{
			//just in case the sorters are faster than the readers
                        while(num_files_sorted >= num_files_read)
			{
                                sleep(0.0001);
			}
			tmp = num_files_sorted;
			num_files_sorted++;
			printf("Thread #%d is sorting numbers for file %s.\n\n", t_id, file_names[tmp]);
		}
		pthread_mutex_unlock(&mutex_1);
		insertion_sort(nums[tmp], num_ints_per_file);
		
	}
}

//write sorted numbers to new file
void *write_to_file(void *param)
{
	int t_id = *((int *)param);
	int tmp;
	while(num_files_written < tot_num_files)
	{
		pthread_mutex_lock(&mutex_2);
		if(num_files_written >= tot_num_files)
		{
			pthread_mutex_unlock(&mutex_2);
			break;
		}
		else
		{
			//just in case the writers are faster than the sorters
			while(num_files_written >= num_files_sorted)
				sleep(0.0001);
			tmp = num_files_written;
			num_files_written++;
			printf("Thread #%d is writing sorted numbers for file %s to a new binary file.\n\n", t_id, file_names[tmp]);
		}
		pthread_mutex_unlock(&mutex_2);

		char path[100];
		
		sprintf(path, "%s/%s", dir_name, "sorted_");

		/* Just to extract the file unique identification number from the file name*/
		for(int i = 9; i < 100; i++)
		{
			if(file_names[tmp][i] != '.')
				sprintf(path, "%s%c", path, file_names[tmp][i]);
			else
				break;
		}
		sprintf(path, "%s.bin", path);
		int fd = open(path, O_CREAT|O_WRONLY);
		if (fd < 0)
		{
			fprintf(stderr, "Make sure the directory exists and is writable.\n");
			exit(-1);

		}

		for( int i = 0; i < num_ints_per_file; i++)
		{
			size_t rv = write(fd, &nums[tmp][i], sizeof(int));

			if(rv == 0)
			{
				fprintf(stderr, "Not enough physical memory space.\n");
				exit(-1);

			}
		}
		close(fd);


	}
}

//to get the list of all unsorted files
void get_file_names(char *path)
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

	//create an array of string to hold files names
	file_names = malloc(sizeof(char *) * tot_num_files);
	for (int i = 0; i < tot_num_files; i++)
		file_names[i] = malloc(sizeof(char) * 100);

	// opendir() returns a pointer of DIR type.  2nd time to actually get the names and save them in array file_path
        dr = opendir(path);
	int count = 0;

        if (dr == NULL)  // opendir returns NULL if couldn't open directory
        {
                printf("Could not open the specified directory" );
		exit(-1);
        }

        while ((de = readdir(dr)) != NULL)
        {
		if(de->d_type!=DT_DIR)
		{
			//file_names[count] = de->d_name;
			strcpy(file_names[count],  de->d_name);
			count++;
		}
	}
	closedir(dr);


	printf("\nIn total there are %d files which contents need to be sorted.\n\n", tot_num_files);
	printf("\nList of files to be sorted: \n\n");
	for(int i = 0; i < tot_num_files; i++)
		printf("%s\n", file_names[i]);


}

//find how many integers are in each file
void get_num_ints_per_file(char* path)
{
	struct stat sb;
	char tmp[100];
	sprintf(tmp, "%s/%s", path, file_names[0]);
	if (stat(tmp, &sb) == -1)
		fprintf(stderr, "Sorry the file you specified does not exits. \n");
	else
		num_ints_per_file = sb.st_size / sizeof(int);

	//initialize the 2d array to hold the values to be sorted
	nums = malloc(sizeof(int *) * tot_num_files);
	for(int i = 0; i < tot_num_files; i++)
		nums[i] = malloc(sizeof(int) * num_ints_per_file);

	printf("\n\nnumber of integer per file is: %d\n\n", num_ints_per_file);

}

//create a directory to save files with sorted numbers if it does not already exits
void create_directory()
{
	struct stat st = {0};
	sprintf(dir_name, "%s/%s", d, "sorted");

	if (stat(dir_name, &st) == -1) 
	{
    		mkdir(dir_name, 0777);
	}

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
	
	get_file_names(d); //get a list of all unsorted file names
	get_num_ints_per_file(d); //read one of the unsorted files to find out how many integers are in there
	create_directory(); //create a subdirectory to store the file containing sorted numbers



	//record thread ids for debugging purposes
	int *thread_ids = malloc(sizeof(int) * t * 3);
	for(int i = 0; i < t * 3; i++)
		thread_ids[i] = i;

	pthread_t threads[t * 3];
	int rc;
	
	// start all the threads
	for (int i = 0; i < 3 * t; i++)
	{
		if(i < t) //start t threads to read the binary files 
			rc = pthread_create(&threads[i], NULL, read_files, (void *) &thread_ids[i]);
		else if (i < 2 * t) // start t threads to sort the numbers 
			rc = pthread_create(&threads[i], NULL, sort_file_content, (void *) &thread_ids[i]);
		else // start t thread to write the sorted number into new binary files 
			rc = pthread_create(&threads[i], NULL, write_to_file, (void *) &thread_ids[i]);

    		if (rc)
		{
      			printf("ERROR; return code from pthread_create() is %d\n", rc);
      			exit(-1);
    		}

	}

	//wait for threads to complete 
	for (int i = 0; i < 3 * t; i++) 
	{
		rc = pthread_join(threads[i], NULL);
		if (rc != 0) 
		{
			fprintf(stderr, "ERROR joining with thread %d (error==%d)\n", thread_ids[i], rc);
			exit(-1);
		}
	}

	//for(int i = 0; i < num_ints_per_file; i++)
	//	printf("%d ", nums[0][i]);



	//free all the allocated memory
	free(thread_ids);

	for(int i = 0; i < tot_num_files; i++)
	{
		free(file_names[i]);
		free(nums[i]);
	}
	free(nums);
	free(file_names);



	
	
	return 0;
}
