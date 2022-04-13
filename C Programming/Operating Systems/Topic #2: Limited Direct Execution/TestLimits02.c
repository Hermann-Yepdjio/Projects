#include <stdio.h>

#define INTERESTING_KERNEL_MEMORY_LOCATION 1024

int main(int argc, char *argv[])
{
  unsigned int *ptr = (unsigned int *) INTERESTING_KERNEL_MEMORY_LOCATION;

  printf("We know that the kernal address space includes the bytes at %p\n", ptr);
  printf(" and the value at that location is %x\n", *ptr);

  return (0);
}

