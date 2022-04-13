#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <unistd.h>
#include <sys/wait.h>
#define maxLength 1024
int main ()
{
	char line[maxLength];
	char *token;
	char *endptr;
	int  statval;
	int  returnvalue=0;
	char cmd[maxLength];
	while(1)
	{
		printf("cwushell> ");
		char* argv[100];
	        if(!fgets(line, maxLength, stdin)){break;} //reads the command
		strcpy(cmd, line);
		token= strtok(line, " \n\t\r");  //parse the input into words
		int i=0;
		if (token!=NULL && strcmp(token, "exit")==0) //exit command
		{
			while (token!=NULL)
                        {
                                argv[i]=token;
                                token=strtok(NULL, " \n\t\r");
                                i++;
                        }
			argv[i]=NULL; //to signal that there's no more argument to pass
			if (argv[1]!=NULL && argv[2]==NULL)
			{
				long num= strtol(argv[1], &endptr, 10); //converts string to long
				if (errno || *endptr != '\0' || argv[1]== endptr || num < -2147483648 || num > 2147483648)//check for errors
					 perror("Please check the argument and make sure it is an integer");
				else
					return num; //return exit value
			}
			else if (argv[1]==NULL)
				return returnvalue;
			else
				printf("Something wrong with the operands. Please check the sintax\n"); //error detected

		}
		else if (token!=NULL && strcmp(token, "ls")==0)  //ls command
		{
			while (token!=NULL)
                        {
                                argv[i]=token;
                                token=strtok(NULL, " \n\t\r");
                                i++;
                        }
                        argv[i]=NULL;
                        int pid=fork(); //spawn child process  and save the pid
                        if(pid==0)
                        {
                                execvp(argv[0], argv); //launch executable 

                        }
                        else
                        {
                                wait(&statval);  //wait for child process to terminate
                                returnvalue=WEXITSTATUS(statval);  //save value returned by child process
                        }

		}
		else if (token != NULL && strcmp(token, "mkdir")==0) //command mkdir
		{
			while (token!=NULL)
			{
				argv[i]=token;
				token=strtok(NULL, " \n\t\r"); 
				i++;
			}
			argv[i]=NULL;
			int pid=fork(); //spawn child process  and save the pid
			if(pid==0)
			{
				execvp(argv[0], argv); //launch executable 
				
			}
			else
			{
				wait(&statval);		//wait for child process to terminate
				returnvalue=WEXITSTATUS(statval);  //save value returned by child process
			}
		}
		else if (token!=NULL &&strcmp(token, "cmp")==0) //compare command
                {
                        while (token!=NULL)
                        {
                                argv[i]=token;
                                token=strtok(NULL, " \n\t\r");
                                i++;
                        }
                        argv[i]=NULL;
                        int pid=fork();
                        if(pid==0)
                        {
                               execvp(argv[0], argv);   //launch executable
                        }
                        else
                        {
                                wait(&statval);        //wait for child process to terminate
				returnvalue=WEXITSTATUS(statval); 	//save value returned by child process
                        }
                }
		else if (token!=NULL)
			printf("Sorry! '%s%s", token, "'  is not a valid command. Please enter a valid command\n");

	}
 }
