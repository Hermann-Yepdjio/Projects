#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int X = 0b1010101010101010101;
int Y = 0b1100110011001100110011;
int Z = 0b11100001111000011110000;
int num_bits_X = 19;
int num_bits_Y = 22;
int num_bits_Z = 23;

int X_mask = 0b1000000000000000000;
int Y_mask = 0b1000000000000000000000;
int Z_mask = 0b10000000000000000000000;

char keystream_bits_string[32];


int i, k;

//print the binary represenatation of an int
void print_bin_number(int num, int counter)
{
	if(num >> 1 == 0)
	{
		printf("%i", num & 1);
	}
	else
	{
		print_bin_number(num >> 1, ++counter);
		printf("%i", num & 1);

	}

	if (counter == 1)
		printf("\n");
}

//return the most frequent number in a list of 3 integers
int maj (int a, int b, int c)
{
	if (a == b || a == c)
		return a;
	if (b == c)
		return b;
}

void print_X_Y_Z(int X, int Y, int Z)
{
	printf("X = ");
	int count_bits =  (int)log2(X) + 1;
	for (k = 0; k < num_bits_X - count_bits; k++)
		putchar('0');
	print_bin_number(X, 0);

	printf("Y = ");
	count_bits =  (int)log2(Y) + 1;
	for (k = 0; k < num_bits_Y - count_bits; k++)
		putchar('0');
	print_bin_number(Y, 0);

	printf("Z = ");
	count_bits =  (int)log2(Z) + 1;
	for (k = 0; k < num_bits_Z - count_bits; k++)
		putchar('0');
	print_bin_number(Z, 0);
	
}

//return the value of the kth bit in number (n is the total number of bits in num)
int kth_bit(int num, int n, int k)
{
	return num >> (n - (k + 1)) & 1; // +1 because the indexing starts at 0
}

//compute the keystream bits for the next n generations
void A5_1( int num_iterations) //int *X, int *Y, int *Z, int num_iterations)
{

	for (i =0; i < num_iterations; i++)
	{
		printf("\n---------------------------------------------------------Iteration #%i%s", i + 1, "--------------------------------------------------------------\n");
		int m = maj(kth_bit(X, num_bits_X, 8), kth_bit(Y, num_bits_Y, 10), kth_bit(Z, num_bits_Z, 10));
		if(kth_bit(X, num_bits_X, 8) == m) //then X steps
		{
			printf("The X register has changed\n");
			int t = kth_bit(X, num_bits_X, 13) ^ kth_bit(X, num_bits_X, 16) ^ kth_bit(X, num_bits_X, 17) ^ kth_bit(X, num_bits_X, 18);
			X = X >> 1;
			if(t == 1)
			{
				X = X | X_mask; //sets the left most bit
			}	
			
		}

		if(kth_bit(Y, num_bits_Y, 10) == m) //then Y steps
		{
			printf("The Y register has changed\n");
			int t = kth_bit(Y, num_bits_Y, 20) ^ kth_bit(Y, num_bits_Y, 21);
			Y = Y >> 1;
			if(t == 1)
			{
				Y = Y | Y_mask; //sets the left most bit
			}
				
		}
		
		if(kth_bit(Z, num_bits_Z, 10) == m) //then Z steps
		{
			printf("The Z register has changed\n");
			int t = kth_bit(Z, num_bits_Z, 7) ^ kth_bit(Z, num_bits_Z, 20) ^ kth_bit(Z, num_bits_Z, 21) ^ kth_bit(Z, num_bits_Z, 22);
			Z = Z >> 1;
			if(t == 1)
			{
				Z = Z | Z_mask; //sets the left most bit
			}
			
		}
		int keystream_bit =  (kth_bit(X, num_bits_X, 18) ^ kth_bit(Y, num_bits_Y, 21)) ^ kth_bit(Z, num_bits_Z, 22);
		print_X_Y_Z(X, Y, Z);


		printf("keystream bit is :%i%s", keystream_bit, ".\n");
		sprintf(keystream_bits_string, "%s%d", keystream_bits_string, keystream_bit);
	}
	
	printf("\n-----------------------------------------------------Final result after 32 iterations----------------------------------------------------------\n");
	print_X_Y_Z(X, Y, Z);
	printf("concatenated 32 keystream bits: %s%s", keystream_bits_string, ".\n");

}


int main()
{
	
	print_X_Y_Z(X, Y, Z);
	A5_1(32);


	return 0;
}
