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
	int c;   //takes the value of char tokens in the file 
	char fileName[120];   
	int counter=0;    //to count how many chars are in the fie
	FILE *file;
	int charOccurence[26];        //array to hold number of occurences for each char in the file
	printf ("Please type the file name then press enter. \n");         //prints the welcome message
       	if(!fgets(fileName, 120, stdin)) {break;}     //gets the file name from the user
	strtok (fileName, "\n");            //removes the new line char at the end of the string
	file = fopen(fileName, "r");      //opens the file
	for(int i=0; i<26; i++)     //fill the array with zeros
	   charOccurence[i]=0;
	if (file)          //if the file exists and is opened
	{
		while (( c = getc(file))!=EOF)    //iterate through the file char by char
		{
			char ch=c;
 		     	if (isalpha(c))     //if c is a letter increase its occurence in the array and increase the counter
			{
				if (isupper(c))
				{
				    charOccurence[c-65]++;
				    counter++;
				}
				else
				{
				    charOccurence[c-97]++;
				    counter++;
				}
			}
			else if (ch!='\n' && ch!=' ' && ch!='\t' && ch!= '\n' && ch!='\f')   // if c is not an empty space and is not
				counter++;                                  //a letter, just increase the counter
		}
		fclose(file);  //close the file
		printf ("\n\n       STATISTICS\n\n");
		for (int i=0; i<26; i++)        //launch 26 child processes to calculate frequencies for each of the 26 letters 
		{
			double result=0;
			int pid= fork();  //create a child process
			if (pid==0)  //check if child process was created successfully
			{
				double stat=  (double)charOccurence[i]*100/counter;
				char c=65+i;  //assign appropriate char to c based on ASCII number
                                printf("%s%c%s%.3f%s","The letter '", c,"' occurs ",stat,"% of the time\n");  // print statistics
				return stat;
			}
			else
  			{	
				wait(NULL);  //wait until child process finishes

			}
		}
		return 0;
	}
	else
		printf("Sorry this file does not exist. Please try again \n");
    }

}
