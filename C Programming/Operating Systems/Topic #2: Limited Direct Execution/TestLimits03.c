#include <stdio.h>

#define FILENAME "/mynewfile.txt"

int main(int argc, char *argv[]) 
{
  FILE *fp;

  printf("Attempting to create and write a file in the root directory '%s'\n", FILENAME);

  fp  = fopen (FILENAME, "w");
  if (fp == NULL) {
	  fprintf(stderr, "ERROR: unable to open file '%s' for writing!\n", FILENAME);
	  return(-1);
  }
  fprintf(fp, "Hello Portland State\n");
  fclose(fp);
  printf("Success!\n");
  return(0);
}

