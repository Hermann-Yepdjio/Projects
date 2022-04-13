#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
int
main(int argc, char*argv[]) {
	int *p = malloc(10 * sizeof(int));
	int i = 0, mypid = getpid();

	do {
		printf("%d:%d contents at location %p - %d\n", mypid, i, p, *p);
		p = p + 1;
		i = i + 1;
	} while (i < 10);

	return 0;
}
