#include <limits.h>
#include <stdint.h>
#include <errno.h>
#include <pthread.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <sys/file.h>
FILE *file;	// create a file object
pthread_mutex_t mutex;
char *endPtr;
char line[255];  // to read input from the user
int i,n,M, matrixDegree, condition=1;   // i for "for loops", n for number of elements, M for number of threads
void prepBinaryFile()  // get input from the user and create a matrix based on inputs and store the matrix in a binary file
{
	   	  while (condition==1)  //repeat until condition is satisfied
	  {
		condition=0;
		printf("Please enter a perfect square number greater than '1' for the number of elements in the matrix. \n(Reminder: A perfect square is an integer which square root is also an integer)\n\n"); //perfect square so that we can form a sqaure matrix
		if(!fgets(line, 255, stdin)){break;}  //get input from the user
 		int size=strlen(line)-1;
		for (i=0; i<size; i++)  //check if input is an integer
		{
			if (!isdigit(line[i]))
			{
				condition=1;
				printf("Sorry invalid input! Please try again and make sure to enter a valid number. \n\n");
				 break;
			}
		}
		if(condition==0)  
		{
			n=atoi(line);
			condition=1;
			for(i=0; i<n; i++) // check if the input is a perfect square
			{
				if (i*i==n)
				{
					condition=0;
					matrixDegree=i;
					break;
				}
			}
			if (condition==1)  //print error message if input was not valid
			{
				printf("Sorry wrong input! Please try again and make sure to enter a valid number. \n\n");
			}
		}
	}
	while (condition==0)  //repeat until condition is satisfied
	{
		condition=1;
		printf("\n\nPlease enter a positive integer for the number of threads.\n\n");
		if (!fgets(line, 255, stdin)){break;} //get the number of threads from the user
		int size=strlen(line)-1;
		for(i=0; i<size; i++)   //check if input is an integer
		{
			if(!isdigit(line[i]))
			{
				condition=0;
				printf("Sorry wrong input! Please try again and make sure to enter a valid number. \n\n");
				break;
			}
		}
			
		if (condition==1)
		{	
		       long temp=strtol(line, &endPtr,  10);
			if (temp==-1 || temp<INT_MIN || temp>1000000)
			{
				printf("Sorry the number you entered is too large. Please try again");
				condition=0;
			}
			else
			{
				M=(int)temp;
				if(M==0) //print error message if input value is 0
				{
					printf("Sorry '0' is not an option. Please try again and make sure to enter a valid number. \n\n");
					condition=0;
				}
			}
		}
	
	}
	file= fopen("matrix.bin", "w");  //open the file
	char array[n];  //create an array of n elements
	for (i=0; i<n; i++)  // generate n random integers between 0 and 1 and stores them in the array
	{
		srand(clock());
		array[i]=rand()%2+'0';
	}
	fwrite(array,1 , n, file);  //copy the data from the array in the file
	fclose(file);  // close the file
		
}

void printFile()     //open the binary file and print the matrix to the screen
{
	file=fopen("matrix.bin", "r");
	char c,counter=0;
        while (fread(&c, 1 , 1, file)==1)
	{
		printf ("%s%c","   ", c);
		counter++;	
		if (counter==matrixDegree)
		{
			printf("\n\n");
			counter=0;
		}
	}
	fclose(file);

}

int isMatrixOk () //check if the matrix contains only os or only 1s and return 0 if thats the case otherwise return 1
{
        file= fopen("matrix.bin", "r");
        int checkValue= fgetc(file);
        int c;
        while((c=fgetc(file))!=EOF)
        {
                if (c!=checkValue)
                {
                        fclose(file);
                        return 1;
                }
        }
        fclose(file);
        return 0;
}

void *modifyMatrix(void *arg) //open the file and modify the matrix
{
	int j=1;
   while(j==1) //repeat until matrix contains either only 0s or only 1s
   {
	j=0;
	
	int row =rand() % matrixDegree + 1,  //generate a row number randomly
            column = rand() % matrixDegree + 1, //generate a column number randomly
	    elementOffset = (matrixDegree*(row-1)+column-1); // position of the element in the file
        pthread_mutex_lock(&mutex);
	if (isMatrixOk()==1)
	{
        printf("%s%i%s%i%s", "Operation on (row, column)= (", row, " , ",column, ")\n"); //print a message to let the user
								//know what element of the matrix the program is working on
        file = fopen("matrix.bin", "r+");  //open the file containing the matrix
	if (row==1 && column==1)   //performs checks and modification on element (1,1)
	{	
		int currentValue,  counter1=0, counter2=0, neighbors[3];
		currentValue =  fgetc(file)-48;
		neighbors[0]=  fgetc(file)-48;
		fseek(file, matrixDegree-1, SEEK_CUR);
		neighbors[1]=  fgetc(file)-48;
		neighbors[2]=  fgetc(file)-48;
		for (i=0;i<3;i++)
		{
			if (neighbors[i]==currentValue)
				counter1++;
			else 
				counter2++;
		}
		rewind(file);
		if (counter1<=counter2)
		{
			if(currentValue==0)
				fputc('1', file);
			else
				fputc('0', file);
		}
	 }		
	 else if (row==1 && column==matrixDegree) //check and perform modification on element (1, matrixDegree)
	 {	
		int currentValue,  counter1=0, counter2=0, neighbors[3];
		fseek(file, elementOffset-1, SEEK_SET);
		neighbors[0]=  fgetc(file)-48;
		currentValue =  fgetc(file)-48;
		fseek(file, matrixDegree-2, SEEK_CUR);
		neighbors[1]=  fgetc(file)-48;
		neighbors[2]=  fgetc(file)-48;
		for (i=0;i<3;i++)
		{
			if (neighbors[i]==currentValue)
				counter1++;
			else 
				counter2++;
		}
		fseek(file, elementOffset, SEEK_SET);
		if (counter1<=counter2)
		{
			if(currentValue==0)
				fputc('1', file);
			else
				fputc('0', file);
		}
	  }
	  else if (row==matrixDegree && column==matrixDegree) //check and perform modification on element (matrixDegree, matrixDegree)
	  {	
		int currentValue,  counter1=0, counter2=0, neighbors[3];
		fseek(file, elementOffset-1-matrixDegree, SEEK_SET);
		neighbors[0]=  fgetc(file)-48;
		neighbors[1]=  fgetc(file)-48;
		fseek(file, -2, SEEK_END);
		neighbors[2]=  fgetc(file)-48;
		currentValue =  fgetc(file)-48;
		for (i=0;i<3;i++)
		{
			if (neighbors[i]==currentValue)
				counter1++;
			else 
				counter2++;
		}
		fseek(file, elementOffset, SEEK_SET);
		if (counter1<=counter2)
		{
			if(currentValue==0)
				fputc('1', file);
			else
				fputc('0', file);
		}
	   }
	   else if (row==matrixDegree && column==1) // check and perform modification on element (matrixDegree, 1)
	   {	
		int currentValue,  counter1=0, counter2=0, neighbors[3];
		fseek(file, elementOffset-matrixDegree, SEEK_SET);
		neighbors[0]=  fgetc(file)-48;
		neighbors[1]=  fgetc(file)-48;
		fseek(file, elementOffset, SEEK_SET);
		currentValue =  fgetc(file)-48;
		neighbors[2]=  fgetc(file)-48;
		for (i=0;i<3;i++)
		{
			if (neighbors[i]==currentValue)
				counter1++;
			else 
				counter2++;
		}
		fseek(file, elementOffset, SEEK_SET);
		if (counter1<=counter2)
		{
			if(currentValue==0)
				fputc('1', file);
			else
				fputc('0', file);
		}
	   } 
  	


	   else if (row==1 && column!=1 && column!=matrixDegree) // check and perform modification on element (1, 1<column<matrixDegree)
           {
                int currentValue,  counter1=0, counter2=0, neighbors[5];
                fseek(file, elementOffset-1, SEEK_SET);
                neighbors[0]=  fgetc(file)-48;
                currentValue=  fgetc(file)-48;
		neighbors[1]=  fgetc(file)-48;
                fseek(file, matrixDegree-3, SEEK_CUR);
                neighbors[2]=  fgetc(file)-48;
                neighbors[3]=  fgetc(file)-48;
		neighbors[4]=  fgetc(file)-48;
                for (i=0;i<5;i++)
                {
                        if (neighbors[i]==currentValue)
                                counter1++;
                        else
                                counter2++;
                }
                fseek(file, elementOffset, SEEK_SET);
                if (counter1<=counter2)
                {
                        if(currentValue==0)
                                fputc('1', file);
                        else
                                fputc('0', file);
                }
	    }
	   else if (row==matrixDegree && column!=1 && column!=matrixDegree) // check and perform modification on element (matrixDegree,
 														//1<column<matrixDegree
            {
                int currentValue,  counter1=0, counter2=0, neighbors[5];
                fseek(file, elementOffset-1-matrixDegree, SEEK_SET);
                neighbors[0]=  fgetc(file)-48;
                neighbors[1]=  fgetc(file)-48;
                neighbors[2]=  fgetc(file)-48;
                fseek(file, matrixDegree-3, SEEK_CUR);
                neighbors[3]=  fgetc(file)-48;
                currentValue=  fgetc(file)-48;
                neighbors[4]=  fgetc(file)-48;
                for (i=0;i<5;i++)
                {
                        if (neighbors[i]==currentValue)
                                counter1++;
                        else
                                counter2++;
                }
                fseek(file, elementOffset, SEEK_SET);
                if (counter1<=counter2)
                {
                        if(currentValue==0)
                                fputc('1', file);
                        else
                                fputc('0', file);
                }
             }
	     else if (row!=1 && row!=matrixDegree && column==1) //check and perform modification on element (1<row<matrixDegree, 1)
             {
                int currentValue,  counter1=0, counter2=0, neighbors[5];
                fseek(file, elementOffset-matrixDegree, SEEK_SET);
                neighbors[0]=  fgetc(file)-48;
                neighbors[1]=  fgetc(file)-48;
                fseek(file, matrixDegree-2, SEEK_CUR);
                currentValue=  fgetc(file)-48;
                neighbors[2]=  fgetc(file)-48;
		fseek(file,matrixDegree-2, SEEK_CUR);
                neighbors[3]=  fgetc(file)-48;
		neighbors[4]=  fgetc(file)-48;
                for (i=0;i<5;i++)
                {
                        if (neighbors[i]==currentValue)
                                counter1++;
                        else
                                counter2++;
                }
                fseek(file, elementOffset, SEEK_SET);
                if (counter1<=counter2)
                {
                        if(currentValue==0)
                                fputc('1', file);
                        else
                                fputc('0', file);
                }
             }
 	     else if (row!=1 && row!=matrixDegree && column==matrixDegree) // check and perform operation on element (1<row<matrixDegree,
													// matrixDegree
             {
                int currentValue,  counter1=0, counter2=0, neighbors[5];
                fseek(file, elementOffset-1-matrixDegree, SEEK_SET);
                neighbors[0]=  fgetc(file)-48;
                neighbors[1]=  fgetc(file)-48;
                fseek(file, matrixDegree-2, SEEK_CUR);
                neighbors[2]=  fgetc(file)-48;
		currentValue=  fgetc(file)-48;
		fseek(file, matrixDegree-2, SEEK_CUR);
                neighbors[3]=  fgetc(file)-48;
                neighbors[4]=  fgetc(file)-48;
                for (i=0;i<5;i++)
                {
                        if (neighbors[i]==currentValue)
                                counter1++;
                        else
                                counter2++;
                }
                fseek(file, elementOffset, SEEK_SET);
                if (counter1<=counter2)
                {
                        if(currentValue==0)
                                fputc('1', file);
                        else
                                fputc('0', file);
                }
              }
	      else  //perform operation on any other element
              {
                int currentValue,  counter1=0, counter2=0, neighbors[8];
                fseek(file, elementOffset-1-matrixDegree, SEEK_SET);
                neighbors[0]=  fgetc(file)-48;
                neighbors[1]=  fgetc(file)-48;
                neighbors[2]=  fgetc(file)-48;
                fseek(file, matrixDegree-3, SEEK_CUR);
                neighbors[3]=  fgetc(file)-48;
                currentValue=  fgetc(file)-48;
                neighbors[4]=  fgetc(file)-48;
		fseek(file, matrixDegree-3, SEEK_CUR);
		neighbors[5]=  fgetc(file)-48;
		neighbors[6]=  fgetc(file)-48;
		neighbors[7]=  fgetc(file)-48;
                for (i=0;i<8;i++)
                {
                        if (neighbors[i]==currentValue)
                                counter1++;
                        else
                                counter2++;
                }
                fseek(file, elementOffset, SEEK_SET);
                if (counter1<=counter2)
                {
                        if(currentValue==0)
                                fputc('1', file);
                        else
                                fputc('0', file);
                }
              }
	fclose(file); //close the file
	printFile();
	j=isMatrixOk();
	printf("%s%i%s", "\n Thread number: ", (int)(intptr_t)arg, "  Just printed. \n");
	printf("\n\n");
	}
	pthread_mutex_unlock(&mutex);  //unlock the section
   }
		
}
int main()   
{
	if (pthread_mutex_init(&mutex, NULL)!=0)
	{
		printf("\nSorry mutex init failed\n");
		return 1;
	}
 		prepBinaryFile(); //create the matrix and prepare the the binary file 
	printFile();  //print the matrix 
	printf("\n\n"); 
	int error;
	pthread_t thread[M]; //create an array of t thread IDs
	for (i=0; i<M; i++) //launch t threads
	{
	        if( error= pthread_create(&thread[i], 0, modifyMatrix,(void *)(intptr_t)i))
	  	{
			fprintf(stderr, "pthread_create: %s", strerror(error));
			exit(1);
		}
	}
	for (i=0; i<M; i++)
		pthread_join(thread[i], 0);
	pthread_mutex_destroy (&mutex);
		
	return 0;
} //main method
