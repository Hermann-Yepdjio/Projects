#include <stdio.h>
#include <stdlib.h>
#include <sys/file.h>
#include <string.h>
#include <unistd.h>

char *file_name;
FILE *file;
int is_simulation_terminated()  //check if all processes in file are executed
{
	file= fopen(file_name, "r");		//open file for reading
	if(!file)   //check if file exists. If not, print error message 
	{
		perror("Sorry the file you have specified does not exist. Please make sure to provide a valid .bin file as argument");
		exit(-1);
	}
	fseek(file, 20 , SEEK_SET); //position file pointer at offset 20
	char c;  //declare char var
	while (!feof(file))   // go through all process in the file and check if they are all terminated. if not return 1, otherwise return 0
	{
	 	fread(&c, 1, 1, file);	
		if (c!='\0' && c!='t') //check if empty char  or char different from 't' at position offset 
		{
			fclose(file);
			return 1;
		}
		fseek(file, 36, SEEK_CUR);
		fread(&c, 1, 1, file);
	}
	fclose(file);  //close file
	return 0;
}

int main(int argc, char *argv[])
{
	file_name=argv[1]; //string variable to hold file past to the consol as argument 1
	char *p_name, priority, highest_priority, status, read_value[100], executing = 'e', terminated= 't', ready='r'; //declar char variables
	int  pid, base_register, burst_time, quantum, offset, offset1, offset2; //declare int variables
	long limit_register, total_mem_alloc=0, total_num_proc=0;  //declare long variables
	while (is_simulation_terminated()==1 )  //Simulate CPU scheduling process using round robin and priority scheduling
	{
		quantum=0; //number of quantum used 
		int count=0, count1=0, total_num_proc=0, total_mem_alloc=0, offset=0; //declar int variables
		printf("\n\nRound Robin Scheduling. \n\n");  //print message to user
        	while ( quantum<30)  //similuate round robin scheduling for a total quantum number of 30
		{
			count=0; //initialize count to 0
			file = fopen(file_name, "r+");  //open file for read and write
			if(!file) //check if file exists and print message
			{
				perror("Sorry the file you have specified is does not exist. Please make sure to provide a valid .bin file as argument");
				return -1;
			}
			while(!feof(file) && quantum<30) //loop through the file until the end of the file is reached
			{
				if (count==0)  //if count = 0, get process name
				{
					memset(read_value, 0, sizeof read_value);
					fread(read_value, 16 , 1 , file);
					printf("name : %s%s", read_value, "  ");
					p_name=read_value;
					offset=offset+16;
				}
				else if (count==1)  //if count = 1, get process id
				{
					fread(&pid, 4 , 1 , file);
					printf("pid : %i%s", pid, "  ");
					offset=offset+4;
				}
				else if (count==2) //if count = 2, get process status
				{
					fread(&status, 1, 1, file);
					printf("status : %c%s", status, "  ");
					offset=offset+1;
				}
				else if (count==3) //if count=3, get process burst time
				{ 
					fread(&burst_time, 4 , 1 , file);
					printf("burst time : %i%s", burst_time, "  \n");
					if (burst_time>0)
					{
						fseek(file, -5, SEEK_CUR);
						fwrite(&executing, 1, 1, file);
						printf("process %i%s", pid, " is currently executing... Status changed to 'e'. \n");
						usleep(1000); //simualte process running running for 1 millisecond
						printf("process %i%s", pid, " was interrupted and placed at the end of the ready queue. Status changed to 'w' \n");
						burst_time--;
						fseek(file, -1, SEEK_CUR);
						fwrite(&ready, 1, 1, file);
						fwrite(&burst_time, 4, 1, file);
						if (burst_time<=0) //check if process is terminated. if yes do nothing. otherwise run process for on quantum
						{
							printf("process %i%s", pid, " just terminated. Status changed to 't' \n");
							fseek(file, -5, SEEK_CUR);
							fwrite(&terminated, 1, 1, file);
							fseek(file, 4, SEEK_CUR);
						}
						quantum++; //increament quantum number if process was executed
					} 	
					offset=offset+4;
				}
				else if (count ==4) //if count=4, get process base register
				{
					fread(&base_register, 4 , 1 , file);
					printf("base register  : %i%s", base_register, "  ");
					offset=offset+4;
				}
				else if (count ==5) //if count=5, get process limit register
				{
					fread(&limit_register, 8 , 1 , file);
					printf("limit register  : %ld%s", limit_register, "  ");
					offset=offset+8;
				}
				else if (count==6) //if count=6, get process priority value
				{
					fread(&priority, 1, 1, file);
					printf("priority  : %c%s", priority, " \n\n\n");
					count=-1;
					offset=offset+1;
				}
				count++;
		//		usleep(2000);
			}
			fclose(file); //close file
			if(is_simulation_terminated()==0) //check if all processes are terminated
				break;	
		}
		if(quantum<30)  //check if the loop was broken because all the processes were terminated or because quantun reached 30
			break;
		printf("\n\n Switching to Priority Scheduling. \n\n");
		while(quantum<60) //simulate priority scheduling 
		{ 
			count=0;  //initialize count to 0
			file = fopen(file_name, "r+"); //open file for read and write
			if(!file) //if file does not exist print error message
			{
				perror("Sorry the file you have specified is does not exist. Please make sure to provide a valid .bin file as argument");
				return -1;
			}

			while(!feof(file)) //loop through the file until end of file is reached to get process with highest priority
			{
				fseek(file, 21, SEEK_CUR);  //position file pointer at offset 21
				fread(&burst_time, 4, 1, file); //read process burst time
				if(burst_time>0) //consider process if burst time is greater than 0 otherwise ignore
				{    
					fseek(file, 12, SEEK_CUR);
					fread(&priority, 1, 1, file);
					if(count==0) //if first process in file set priority temporarily as the highest on in the file
					{
						highest_priority=priority;
						count++;
						offset=ftell(file)-1;
					}
					else if(priority<highest_priority) //otherwise check if priority is greater than highest priority
					{
						highest_priority=priority; //set priority as highest priority
						offset= ftell(file)-1;
					}
				}
				else
				{
					fseek(file, 12, SEEK_CUR); //move file point 12 bytes forward from current position
					fread(&priority, 1, 1, file); //perform a read just to check if end of file was reached
				} 
			}
			if(count==0) //if count is still 0 it means no process was considered so all process are terminated
				break;
			else //there are some processes that still have burst time
			{ 
			 	fseek(file, offset-37, SEEK_SET); 
				fread(read_value, 16, 1, file);
				fread(&pid, 4, 1, file);
				fread(&status, 1, 1, file);
				fread(&burst_time, 4, 1, file);
				fread(&base_register, 4, 1, file);
				fread(&limit_register, 8, 1, file);
				fread(&priority, 1, 1, file);
				printf("name : %s%s", read_value, "  ");
				printf("pid : %i%s", pid, "  ");
				printf("status : %c%s", status, "  ");
				printf("burst time : %i%s", burst_time, "  \n");
				fseek(file, -18, SEEK_CUR);
				fwrite(&executing, 1, 1, file);
				printf("process %i%s", pid, " is currently executing... Status changed to 'e'. \n");
				usleep(1000); // simulate process execution for 1 millisecond
				printf("process %i%s", pid, " was interrupted and placed at the end of the ready queue. Status changed to 'w' \n");
				burst_time--;
				fseek(file, -1, SEEK_CUR);
				fwrite(&ready, 1, 1, file);
				fwrite(&burst_time, 4, 1, file);
				if (burst_time<=0)
				{
					printf("process %i%s", pid, " just terminated. Status changed to 't' \n");
					fseek(file, -5, SEEK_CUR);
					fwrite(&terminated, 1, 1, file);
					fseek(file, 4, SEEK_CUR);
				}
				printf("base register  : %i%s", base_register, "  ");
				printf("limit register  : %ld%s", limit_register, "  ");
				printf("priority  : %c%s", priority, " \n\n\n");
				quantum++;
				if (count1==0) //if 0 do not apply aging
				{
					offset1=offset;
					count1++;
				}
				else //apply aging 
				{
					offset2=offset;
					count1=0;
					offset=37;
					fseek(file, 0, SEEK_SET);
					while(!feof(file))
					{
						fseek(file, 37, SEEK_CUR);
						fread(&priority, 1, 1, file);
						if (offset!=offset1 && offset!=offset2 && !feof(file))
						{
							fseek(file, -1, SEEK_CUR);
						//	if (priority!= '\0');
								priority--;
							fwrite(&priority, 1, 1, file);
						}
						offset=offset+38;
					}
					printf("\n\nAging was just applied to prevent Starvation! \n\n");
				}
			}
			fclose(file); // close file
		}
			
		
	}
	printf("\n\n CPU Scheduling simulation terminated\n\n");
	file = fopen(file_name, "r");		 //open file to read
	int count=0;
	printf("\n\nBelow is a list of all the processes in the file with information. \n\n ");
	if(!file) //if file does not exist print error message
	{
		perror("Sorry the file you have specified is does not exist. Please make sure to provide a valid .bin file as argument");
		return -1;
	}


	while (!feof(file))  //to count how many process are available in file and how much memory allocation is need and print information about all processes
	{
		memset(read_value, 0 , sizeof read_value);
		if (count==0)
		{
			fread(read_value, 16 , 1 , file);
			printf("name : %s%s", read_value, "  ");
			p_name=read_value;
		}
		else if (count==1)
		{
			fread(&pid, 4 , 1 , file);
			printf("pid : %i%s", pid, "  ");
		}
		else if (count==2)
		{
			fread(&status, 1, 1, file);
			printf("status : %c%s", status, "  ");
		}
		else if (count==3)
		{ 
			fread(&burst_time, 4 , 1 , file);
			printf("burst time : %i%s", burst_time, "  ");
		}
		else if (count ==4)
		{
			fread(&base_register, 4 , 1 , file);
			printf("base register  : %i%s", base_register, "  ");
		}
		else if (count ==5)
		{
			fread(&limit_register, 8 , 1 , file);
			printf("limit register  : %ld%s", limit_register, "  ");
			total_mem_alloc = total_mem_alloc + limit_register-base_register;
		}
		else if (count==6)
		{
			fread(&priority, 1, 1, file);
			printf("priority  : %c%s", priority, " \n");
			count=-1;
			total_num_proc++;
		}
		count++;
	}
	fclose(file);
	printf ("\n\nNumber of processes available :%ld%s", total_num_proc, " \n");
	printf ("Total number of memory allocated :%ld%s", total_mem_alloc, "  \n");
		


}
