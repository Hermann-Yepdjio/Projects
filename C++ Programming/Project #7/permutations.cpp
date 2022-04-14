#include <iostream>
#include <stdlib.h>
using namespace std;

void swap (int*, int, int); 
void permutation (int*, int, int, int);
int main(int argc, char* argv[])
{
	int n = atoi(argv[1]);
	int k = atoi(argv[2]);
	if (k > n || k<1 || n>10) //check if inputs  are valid
	{
		cout << "Wrong inputs! The program will exit"<< endl;
		system("PAUSE");
		return 1;
	} 
	int* arr= new int[n]; //create an array of n elts
	for (int i=0; i<n; i++) //fill the array with numbers from 0 to n-1
	{
		arr[i] = i;
	}
	permutation(arr, 0, n-1, k); //swaps all the numbers in the array to obtain all possible permutations
	cout<<"  ";
	for (int i = 0; i<k; i++) //prints the k first digits which are not included in the permutations
		cout << i;
	cout<< "  "; //print empty space to make the output easy to read
	system("PAUSE");
	return 0;
}

void swap(int* arr1, int index1, int index2) //swaps two elemets of the array
{
	int temp = arr1[index2];
	arr1[index2] = arr1 [index1];
	arr1[index1] = temp;
}

void permutation(int* arr2, int index_begin, int index_end,int num_size) //generate all the possible permutations  with k digits 
								//the very first one where all digits are ordered (smallest number)
{
	for (int i = index_begin; i<index_end; i++) //for each elt at index i, swap with elt at index j>i
	{
		for (int j= i+1; j<=index_end; j++)
		{
			swap (arr2, i, j);
			if (i<=num_size-1) //after each swap print the k first numbers in the array to the screen
			{
				cout<<"  ";
				for (int k = 0; k<num_size; k++)
					cout<<arr2[k];
			}
			permutation(arr2, i+1, index_end, num_size); //after each swap swap the rest of the array to obtain all the
								//possible permutations for the previous swap
			swap(arr2, j ,i); //swap numbers at index i and j back to make sure that all the possibilities are examined

		}
	}
}
