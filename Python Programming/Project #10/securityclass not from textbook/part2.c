#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>

int find_highest(int arr[], int len)
{
	int index_highest = 0;
	for(int i = 1; i < len; i++)
	{
		if(arr[i] > arr[index_highest])
			index_highest = i;
	}

	return index_highest;
}

int main()
{
	time_t start, elapsed, highest_time = 0;
	int best_char, count[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, tmp[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	char SN[7] = "0000000";
	SN[1] = (char)(48 + 2); 
	printf("Best Serial Number found: %s  %i.\n", SN, count[5]);
	for(int i = 0; i < 7; i++) 
	{
		for(int j = 0; j < 50; j++)
		{
			for(int k = 0; k < 10; k++)
			{
				SN[i] = (char)(48 + k);
				char cmd[100] = "securityclass.exe ";
				strcat(cmd, SN);
				start = clock();
				system(cmd);
				elapsed = clock() - start;
				if(elapsed > highest_time) 
				{
					highest_time = elapsed;
					best_char = k;
				}
			}

			count[best_char]++;
			highest_time = 0;
			
		}
		SN[i] = (char)(48 + find_highest(count, 10));
		memcpy(count, tmp, sizeof(int) * 10);
	}
		
	printf("Best Serial Number found: %s.\n", SN);
	return 0;
}
