#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
#include <limits.h>

int num1, num2, num3, total_num_processes; //declare variables
int pid[9999], priority[9999], temp_pid[9999], temp_priority[9999], index1[9999]; //declare variables
int count_f=0, pid_f[9999], priority_f[9999]; //to hold pids and priorities of terminated processes
double burst_time_f[9999];//to hold burst_time of terminated processes
char *status_f[9999]; //to hold statuses of terminated processes
double burst_time[9999], temp_burst_time[9999]; //array of processes' cpu burstime
char *status[9999], *temp_status[9999]; //array of processes' statuses(temp arrays are to be used in load_balance function


int is_simulation_terminated()  //check if simulation is terminated and return 0 if yes. otherwise return 1
{
	int i;
	for(i=0; i<total_num_processes; i++)
	{
		if(burst_time[i]!=0)
			return 1;
	}
	return 0;
}

void *load_balance()  //function to perfor load balance
{
    while(is_simulation_terminated()==1)
    {
	int i, count=0, count1=0, count2=0, count3=0;//declare temporary variables
	for(i=0; i<total_num_processes; i++) //loop through all processes and collect information about processes that
						// still have burst_time and save the information in temp arrays
	{
		if(burst_time[i]!=0) //check if burst_time for a given process is not zero
		{
			temp_pid[count] = pid[i];
			temp_status[count] = status[i];
			temp_priority[count]= priority[i];
			temp_burst_time[count]= burst_time[i];
			index1[count]=i;
			count++;
		} 
		if(burst_time[i]!=0 && i<num1) //if i<num1 increment count1(counter from processes that are left in Round robin processor)
		{
			count1++;
			
		}
		else if(burst_time[i]!=0 && i<num1+num2) //increment count2 (counter for processes that are left in FCFS processor)
		{
			count2++;
		}
		else if(burst_time[i]!=0) //increment count3(counter for processes that are left in priority processor)
		{
			count3++;
		}
	}
	int new_total=count1 + count2 + count3; //temp variable
	int avg=new_total/3; //temp variable
	i=0;
	count=0;
	if(count1==0) //means Round robin called the load_balance functions and needs some work
	{
		int temp2=count2, temp3= count3;
		while(count<avg && count<num1)
		{
			if(temp2>avg) //move some processes from FCFS to Round robin if the first is overloaded(more processes than avg)
			{
				pid[count]=temp_pid[count];
				status[count] = temp_status[count];
				priority[count] = temp_priority[count];
				burst_time[count] = temp_burst_time[count];
				burst_time[index1[count]]=0;
				count++;
				temp2--;
			}
			if(temp3>avg && count<num1)//move some processes from priority to Round robin if overloaded
			{
				pid[count]=temp_pid[count2+i];
				status[count] = temp_status[count2+i];
				priority[count] = temp_priority[count2+i];
				burst_time[count] = temp_burst_time[count2+i];
				burst_time[index1[count2+i]]=0;
				count++;
				temp3--;
				i++;
			}
		}
		printf("\n\nLoad Balance was just applied.(Called by Round Robin processor)\n\n");	//print message to the user
	
	}
	else if(count2==0) //means FCFS called the function load_balance and needs some work
	{
		int temp1=count1, temp3=count3;
		while(count<avg && count<num2) 
		{ 
		       	if(temp1>avg) // move some processes from Round robin to FCFS if overloaded
			{
				pid[num1+count]=temp_pid[count];
				status[num1+count] = temp_status[count];
				priority[num1+count] = temp_priority[count];
				burst_time[num1+count] = temp_burst_time[count];
				burst_time[index1[count]]=0;
				count++;
				temp1--;
			}
			if(temp3>avg && count<num2) //move some proccesses from priority to FCFS if overloaded
			{
				pid[num1+count]=temp_pid[count1+i];
				status[num1+count] = temp_status[count1+i];
				priority[num1+count] = temp_priority[count1+i];
				burst_time[num1+count] = temp_burst_time[count1+i];
				burst_time[index1[count1+i]]=0;
				count++;
				temp3--;
				i++;
			}
		}	
		printf("\n\nLoad Balance was just applied.(Called by FCFS processor)\n\n");	//print message to the user

	}
	else if (count3==0) // means priority called the function load_balance and needs some work
	{
		int temp1=count1, temp2=count2;
		while(count<avg && count<num3) 
		{
			if(temp1>avg) //move some processes from Round robin to priority if overloaded
			{
				pid[num2+count]=temp_pid[count];
				status[num2+count] = temp_status[count];
				priority[num2+count] = temp_priority[count];
				burst_time[num2+count] = temp_burst_time[count];
				burst_time[index1[count]]=0;
				count++;
				temp1--;
			}
			if(temp2>avg && count<num3)//move some processes from FCFS to priority if overloaded
			{
				pid[num2+count]=temp_pid[count1+i];
				status[num2+count] = temp_status[count1+i];
				priority[num2+count] = temp_priority[count1+i];
				burst_time[num2+count] = temp_burst_time[count1+i];
				burst_time[index1[count1+i]]=0;
				count++;
				temp2--;
				i++;
			}
		}	
		printf("\n\nLoad Balance was just applied.(Called by priority processor)\n\n");	//print message to the user

	}
   }
}

void *round_robin() //simulate round robin scheduling
{	
	while(1)
	{
		int i=0, count=0;
		while(i<num1)
		{
			if (burst_time[i]==0)
				i++;
			else if( strcmp(status[i], "running")==0)
			{
				printf( "(Round Robin Scheduling) pid: %i%s%s%s%i%s%f%s",pid[i], ". state: ", status[i], 
				". priority: ", priority[i], ". burst time: ", burst_time[i]-.5, ". \n");
				burst_time[i] = burst_time[i] - .5;
				if (burst_time[i]<=0) //save data of terminated process for final printing to the user
				{
					pid_f[count_f]= pid[i];
					status_f[count_f]= "terminated";
					priority_f[count_f]= priority[i];
					burst_time_f[count_f] = burst_time[i];
					count_f++;
				}	
				usleep(500000);
				i++;
				count++;
			}
		}	
		if (count==0)
		{
			if(is_simulation_terminated()==0)
				break;
			/*else
			{
				printf("\nProcessor 1 calling load_balance.\n");
				load_balance();
			}*/
		}
	}
}

void *FCFS() //simulate FCFS scheduling
{
	while(1)
	{
		int i;
		for (i= num1; i<num1+num2; i++)
		{
			while(burst_time[i]!=0)
			{
				if (strcmp(status[i], "running")==0)
				{
					printf( "(FCFS Scheduling) pid: %i%s%s%s%i%s%f%s",pid[i], ". state: ", status[i], 
					". priority: ", priority[i], ". burst time: ", burst_time[i]-.5, ". \n");
					burst_time[i] = burst_time[i]-.5;
					if (burst_time[i]<=0) //save data of terminated process for final printing to the user
					{
						pid_f[count_f]= pid[i];
						status_f[count_f]= "terminated";
						priority_f[count_f]= priority[i];
						burst_time_f[count_f] = burst_time[i];
						count_f++;
					}	
					usleep(500000);
						
				}
			}
		}
		if (is_simulation_terminated()==0)
			break;
		/*else
		{
			printf("\nProcessor 2 is calling load_balance.\n");
			load_balance();
		}*/
	}
}

void *Priority() //simulate priority scheduling
{
	while(1)
	{
		int highest_priority = 128, index=-1, i;
		for (i=num1+num2; i<total_num_processes; i++)
		{
			if (priority[i]<highest_priority && burst_time[i]!=0)
			{
				highest_priority = priority[i];
				index=i;
			}
		}
		if(index!=-1)
		{
			if(strcmp(status[index], "running")==0)
			{
				printf( "(Priority Scheduling) pid: %i%s%s%s%i%s%f%s",pid[index], ". state: ", status[index], 
				". priority: ", priority[index], ". burst time: ", burst_time[index]-.5, ". \n");
				burst_time[index]=burst_time[index]-.5;
				if (burst_time[index]<=0) //save data of terminated process for final printing to the user
				{
					pid_f[count_f]= pid[index];
					status_f[count_f]= "terminated";
					priority_f[count_f]= priority[index];
					burst_time_f[count_f] = burst_time[index];
					count_f++;
				}	
				usleep(500000);
					
			}
		}

		else if (is_simulation_terminated()==0)
			break;
		/*else 
		{
			printf("\nprocessor 3 is calling load_balance.\n");
			load_balance();
				
		}*/
	}
}

void *aging() //apply againg every 3 seconds
{
	while(1)
	{
		sleep(3);
		int i;
		for(i=0; i<total_num_processes; i++)
		{
			if (priority[i] !=0 && burst_time[i]!=0)
			{
				priority[i]--;
				printf( "(Aging applied) pid: %i%s%s%s%i%s%f%s",pid[i], ". state: ", status[i], 
				". priority: ", priority[i], ". burst time: ", burst_time[i], ". \n");
			}
		}
		if(is_simulation_terminated()==0)
			break;
		printf("\n\nAging was just applied.\n\n");
	}
}

void *status_mimicking() //mimicking mechanism that randomly changes processes' states according to the process state diagram
{
	while(1)
	{
		sleep(3);
		int i;
		for(i=0; i<total_num_processes; i++)
		{
			if(strcmp(status[i], "new")==0 || strcmp(status[i], "waiting")==0)
				status[i]="ready";
			else if(strcmp(status[i], "running")==0 && burst_time[i]==0)
				status[i]="terminated";
			else if(strcmp(status[i], "running")==0 && burst_time[i]!=0)
			{
				int random = rand()%2 ;
				if(random==0)
					status[i] = "waiting";
				else
					status[i] = "ready";
			}
			else if (strcmp(status[i], "ready")==0)
				status[i] = "running";
			printf( "(status changed) pid: %i%s%s%s%i%s%f%s",pid[i], ". state: ", status[i], 
				". priority: ", priority[i], ". burst time: ", burst_time[i], ". \n");

		}
		if(is_simulation_terminated()==0)
			break;
		printf("\n\nStatus changes mimicking was just applied.\n\n");
	}
}

int main(int argc, char *argv[])
{
	if (argc!=4) // check if 3 arguments were passed to the terminal
	{
		printf ("Sorry you should provide exactly 3 integers as argument. These numbers represent the number"
			" of precesses to be executed by Round Robin processor, FCFS processor"
			 " and Priority processor(all in this order).\n\n"); 
		return -1;
	}
	num1 = atoi(argv[1]); //converts argument1 from type string to int
	num2 = atoi(argv[2]); //converts argument2 from type string to int
	num3 = atoi(argv[3]); //converts argument3 from type string to int
	total_num_processes= atoi(argv[1]) +atoi(argv[2]) + atoi(argv[3]);
	srand(clock()); //set rand() to randomly pick numbers based on clock
	int pid_1 = rand()%10000000 + 10000000 ;	//pick a random number between 9999999 and 199999999
	int i;
	for (i=0; i<total_num_processes; i++) //fill arrays with processes' information
	{
		pid[i] = pid_1+i;  //fill pid array with different numbers
		priority[i] = rand()%128; //fill array of priority with random number chosen between 0 and 127
		burst_time[i] = rand()%6; //fill burst_time array with numbers chosen between 0 and 5
		status[i] = "new"; //fill status array with string "new"
		if (burst_time[i]==0) //save data of terminated process for final printing to the user
		{
			pid_f[count_f]= pid[i];
			status_f[count_f]= "terminated";
			priority_f[count_f]= priority[i];
			burst_time_f[count_f] = burst_time[i];
			count_f++;
		}	

	}
	for (i=0; i<total_num_processes; i++)//print all the processes and information to the user
	{
		printf( "pid: %i%s%s%s%i%s%f%s",pid[i], ". state: ", status[i], 
			". priority: ", priority[i], ". burst time: ", burst_time[i], ". \n");
	}
	int error;
	pthread_t thread_1, thread_2, thread_3, thread_4, thread_5, thread_6; 
	if (error=pthread_create(&thread_1, NULL, round_robin, NULL)) //create a thread to handle round robin scheduling
	{
		fprintf(stderr, "pthread_create: %s", strerror(error));
		exit(-1);
	}
	if (error= pthread_create(&thread_2, NULL, FCFS, NULL)) //create a thread to handle FCFS scheduling
	{
		fprintf(stderr, "pthread_create: %s", strerror(error));
		exit(-1);
	}
	if (error= pthread_create(&thread_3, NULL, Priority, NULL))  //create a thread to handle priority scheduling
	{
		fprintf(stderr, "pthread_create: %s", strerror(error));
		exit(-1);
	}
	if (error= pthread_create(&thread_4, NULL, status_mimicking, NULL)) //create a process to handle status change mimicking mechanism
	{
		fprintf(stderr, "pthread_create: %s", strerror(error));
		exit(-1);
	}
	if(error= pthread_create(&thread_5, NULL, aging, NULL)) //create a process to handle againg mechanism
	{
		fprintf(stderr, "pthread_create: %s", strerror(error));
		exit(-1);
	}
	if (error=pthread_create(&thread_6, NULL, load_balance, NULL)) //create a thread to handle round robin scheduling
	{
		fprintf(stderr, "pthread_create: %s", strerror(error));
		exit(-1);
	}
	pthread_join(thread_1, NULL);
	pthread_join(thread_2, NULL);
	pthread_join(thread_3, NULL);
	pthread_join(thread_4, NULL);
	pthread_join(thread_5, NULL);
	pthread_join(thread_6, NULL);
	/*printf("\nSimulation terminated.\n\n Below is the list of all the processes that have been executed\n\n");
	for (i=0; i<total_num_processes; i++)//print all the processes and information to the user
	{
		printf( "pid: %i%s%s%s%i%s%f%s",pid_f[i], ". state: ", status_f[i], 
			". priority: ", priority_f[i], ". burst time: ", burst_time_f[i], ". \n");
	}*/

}
