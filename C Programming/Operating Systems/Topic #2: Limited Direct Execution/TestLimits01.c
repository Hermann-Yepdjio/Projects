#include <stdio.h>

int main(int argc, char *argv[])
{
  printf("Testing the limits by looping forever. Can the OS and other other processes still run?\n");

  while (1) {}
	
  return (0);
}

