#include "utilities.h"
#include <pthread.h>

int main()
{

	int input;
	cout << "\n\nWelcome to Project 5. Please Enter \n\n1 if you just want to compute the makespan for a specific job ordering \n\n2 or any other integer if you want to run the whole program \n\nEnter a value: ";
	cin >> input;
	if(cin.fail())
	{
		cerr << "\n\nSorry wrong input type! Please try again and make sure to provide an integer\n\n";
	}

	if (input == 1)
		compute_cmax();
	else
	{
	
		pthread_t thread_id[3];
		

		for(long i = 0; i < 3; i++)
		{
			

			int ret = pthread_create(&thread_id[i], NULL, simulate, (void *)i);
			if (ret)
			{
				perror("Error during thread creation");
				exit(-1);
			}
		}

		for(long i = 0; i < 3; i++)
		{
			int ret = pthread_join(thread_id[i], NULL);
			if (ret)
			{
				perror("Error during thread creation");
				exit(-1);
			}
		}
	}

	
	return 0;

}
