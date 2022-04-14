#include <stdio.h>
#include <stdlib.h>

int const key[4] =  {0xA56BABCD, 0x00000000, 0xFFFFFFFF, 0xABCDEF01};
int const plain_text[2] = {0x01234567, 0x89ABCDEF};
int delta = 0x9e3779b9;
int i;

int* encrypt(const int key[], const int plain_text1[], const int delta)
{
	int sum = 0;
	int* cipher_text = malloc(sizeof(int) * 2);
	for (i=0; i<2; cipher_text[i] = plain_text1[i], i++); //copy plain_text in cipher_text	
	for(i = 0; i < 32; i++)
	{
		sum += delta;
		cipher_text[0] += ((cipher_text[1] << 4) + key[0]) ^ (cipher_text[1] + sum) ^ ((cipher_text[1] >> 5) + key[1]);
		cipher_text[1] += ((cipher_text[0] << 4) + key[2]) ^ (cipher_text[0] + sum) ^ ((cipher_text[0] >> 5) + key[3]);
	
	}

	return cipher_text;
}

int* decrypt(const int key[], int* cipher_text, const int delta)
{	
	int sum = delta << 5;
	int* plain_text1 = malloc(sizeof(int) * 2);
	for (i=0; i<2; plain_text1[i] = cipher_text[i], i++); //copy cipher_text into plain_text;
	for(i = 0; i < 32; i++)
	{
		plain_text1[1] -= ((plain_text1[0] << 4) + key[2]) ^ (plain_text1[0] + sum) ^ ((plain_text1[0] >> 5) + key[3]);
		plain_text1[0] -= ((plain_text1[1] << 4) + key[0]) ^ (plain_text1[1] + sum) ^ ((plain_text1[1] >> 5) + key[1]);
		sum -= delta;
	}

	return plain_text1;
}


int main()
{
	int* c_t = encrypt(key, plain_text, delta);
	int* p_t = decrypt(key, c_t, delta);
	printf("\n Plain text before encryption: %08X%08X.\n", plain_text[0], plain_text[1]); 
	printf("\n Cipher text: %08X%08X.\n", c_t[0], c_t[1]);
	printf("\n Plain text after encryption and decryption: %08X%08X.\n", p_t[0], p_t[1]);
}


