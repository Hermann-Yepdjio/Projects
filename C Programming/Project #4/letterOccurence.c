#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <unistd.h>
#include <sys/wait.h>
int main()
{
    while (1)
    {
	int c;   //to hold  the value of char tokens in the file 
	char fileName[120];   
	FILE *file;
	printf ("\n\nPlease enter a file name then press 'enter' to get a statistic for each letter occurence in the file. (Or type 'exit' then press 'enter' to exit the program) \n\n\n");         //prints the welcome message
      	if(!fgets(fileName, 120, stdin)) {break;}     //gets the file name from the user
	if (strcmp(fileName, "exit\n")==0)
		exit(0);

	strtok (fileName, "\n");            //removes the new line char at the end of the string
		for (int i=0; i<26; i++)
		{
			file = fopen(fileName, "r");      //opens the file
			if (file)          //if the file exists and is opened
			{
				int statVal;
				int pid= fork();  // create a child process
				if (pid==0)  //check if child process is running
				{
					int charOccurence=0; //to count the number of occurence of a specific character
					int counter=0;   // to count how many characters are in the file "spaces excluded"
					while (( c = getc(file))!=EOF)    //iterate through the file char by char
					{
						char ch=c;
 		     				if (c==65+i || c==97+i)     //if c is a letter increase its occurence in the array and increase the counter
				                 {
							charOccurence++; //increament character occurence
							counter++;  // increment counter
						 }
					       	 else if (ch!='\n' && ch!=' ' && ch!='\t' && ch!= '\n' && ch!='\f')   // if c is not an empty space and is not
					         counter++;                                  //a letter, just increase the counter
					}
					if (counter!=0)
					{    
						if (i==0)
							printf("\n\n            Statistics\n\n");
						double stat=(double) (charOccurence*100)/counter;  // compute the frequency of each letter
						char ch=65+i;
						printf("%s%c%s%.3f%s","The letter '", ch,"' occurs ",stat,"% of the time\n");  // print statistics
						if(i==25)
							printf("\n Statistics printed successfully! Now what do you want to do next?");
						return stat;
					}
					else
					{
						printf("\n\nSorry the file you specified does not contain any letter or punctuation mark. Please Try Again. \n\n");
						fclose(file);
						return 15; //return 15 if file is empty (15 was chosen randomly by me)
					}
				}
				else
				{
					wait(&statVal); //wait until child process terminates and get exit status
					int ReturnValue = WEXITSTATUS(statVal); //gets the value returned by the child process
					if (ReturnValue==15) //check and break for loop if returnvalue is 15
						break;
				}
					
			}
			else
			{
				printf("\n\nSorry this file does not exist. Please try again \n\n"); //print error message if file contain can't be accessed
				break;
			}
			fclose(file);  //close the file

	
		}
     }
}
