#include <stdio.h>
#include <time.h>
#include <stdlib.h>

int num_threads=0;
int max_threads = 25000;
pthread_mutex_t lock;
void merge(int A[], int p, int q, int r) //p = index of first elt, q = index of middle elt, r= index of last element
{
	int i, j, k;
	int n1 = q - p + 1; // number of elts btw first and middle elt (including both the first and middle elts)
	int n2 = r - q; //number of elts btw middle and last elt(the middle elt not included)

	int left[n1], right[n2];

	for ( i = 0; i < n1; i++) //copy elts of the first half into temporary array left 
		left[i] = A[p+i];

	for ( j = 0; j < n2; j++) //copy elts of the other half into temporary array right
		right[j] = A[q + j + 1];

	i = 0;
	j = 0;
	k = p;

	while (i<n1 && j<n2) //copy elts from left and right back into A in the correct order
	{
		if (left[i] <= right[j])
		{
			A[k] = left[i];
			i++;
		}
		else
		{
			A[k] = right[j];
			j++;
		}
		k++;
	}

	while (i < n1) //copy remaining elts of left into A
	{
		A[k] = left[i];
		i++;
		k++;
	}

	while (j < n2) //copy remaining elts of right into A
	{
		A[k] = right[j];
		j++;
		k++;
	}


}	


void merge_sort(int A[], int p, int r)
{
	if (p<r)
	{
		int q = (p + r)/2;
		merge_sort(A, p, q);
		merge_sort(A, q+1, r);
		merge(A, p, q, r);
	}
}

typedef struct 
{
	int p;
	int r;
	int *A;
}arguments;

void *merge_sort_2(arguments* arg)
{
       	if(arg->p < arg->r)
	{
		int q = (arg->p + arg->r)/2;
		arguments arg1;
		arguments arg2;
		arg1.A = arg->A;
		arg1.p = arg->p;
		arg1.r = q;
		arg2.A = arg->A;
		arg2.p = q+1;
		arg2.r = arg->r;

		if (num_threads < max_threads)
		{
			pthread_t tid;
			pthread_mutex_lock(&lock);
			num_threads += 1;
			pthread_mutex_unlock(&lock);
			pthread_create(&tid, NULL, merge_sort_2, &arg1);
			merge_sort_2(&arg2);

			pthread_join(tid, NULL);
		}
		else
		{
			merge_sort_2(&arg1);
			merge_sort_2(&arg2);
		}
		merge(arg->A, arg->p, q, arg->r);



	}	
}

void print_array(int A[], int len_A)
{
	int i;
	for (i = 0; i < len_A; i++)
		printf("\n%d ", A[i]);
	
	printf("\n");
}

void call_merge_sort (int A[], size_t len)
{

		int p = 0; 
		int r = len - 1;
		printf("Array of size : %ld%s", len,"\n"); 
		time_t time_elapsed = clock();
		merge_sort(A, p, r); 
		time_elapsed = clock() - time_elapsed ;
		printf("Time elapsed : %ld \n\n", time_elapsed); 
}

void call_merge_sort_2(int A[], size_t len)
{
	arguments data;
	data.p = 0;
	data.r = len - 1;
	data.A = A;
	printf("Array of size : %ld%s", len,"\n"); 
	time_t time_elapsed = clock();
	merge_sort_2(&data);
	time_elapsed = clock() - time_elapsed;
	printf("Time elapsed : %ld \n\n", time_elapsed); 

}


int main()
{
	int A[100], B[1000], C[10000], D[100000], i;
	for(i=0; i<100000; i++)
	{
		if(i<100)
		{
			int tmp = rand() % 10000000;
			A[i] = tmp;
			B[i] = tmp;
			C[i] = tmp;
			D[i]= tmp;
		}
		else if(i<1000)
		{
			int tmp = rand() % 10000000;
			B[i] = tmp;
			C[i] = tmp;
			D[i]= tmp;

		}
		else if(i<10000)
		{
			int tmp = rand() % 10000000;
			C[i] = tmp;
			D[i]= tmp;

		}
		else if(i<100000) 
		{
			int tmp = rand() % 10000000;
			D[i] = tmp;	
		}
	}

	printf("Parrallel merge_sort && sequential merge\n\n");
	call_merge_sort_2(A, 100);
	call_merge_sort_2(B, 1000);
	call_merge_sort_2(C, 10000);
	call_merge_sort_2(D, 100000);


	printf("sequential merge_sort && sequential merge\n");
	call_merge_sort(A, 100);
	call_merge_sort(B, 1000);
	call_merge_sort(C, 10000);
	call_merge_sort(D, 100000);


		return 0;
}

