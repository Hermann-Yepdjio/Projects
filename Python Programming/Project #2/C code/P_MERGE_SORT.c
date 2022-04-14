#include <stdio.h>
#include <time.h>
#include <stdlib.h>
//#include <pthread.h>

int num_threads1 = 0;
int max_threads = 16000;
pthread_mutex_t lock1;
int max(int a, int b)
{
	if (a > b)
		return a;
	else
		return b;
}

void swap(int *a, int *b)
{
	int tmp = *a;
	*a = *b;
	*b = tmp;
}

typedef struct
{
	int *T;
	int p1;
	int r1;
	int p2;
	int r2;
	int *A;
	int p3;
} argument1;

typedef struct
{
	int *A;
	int p;
	int r;
	int *B;
	int s;	
} argument2;
int binary_search(int x, int T[], int p, int r)
{
	int low = p;
	int high = max(p, r+1);
	while(low < high)
	{
		int mid = (low + high)/2;
		if (x <= T[mid])
			high = mid;
		else
			low = mid + 1;
	}
	return high;
}


int *p_merge(argument1 *arg)
{
	int n1 = arg->r1 - arg->p1 + 1;
	int n2 = arg->r2 - arg->p2 + 1;
	if (n1 < n2) //ensure that n1 >= n2
	{
		swap(&arg->p1, &arg->p2);
		swap(&arg->r1, &arg->r2);
		swap(&n1, &n2);

	}

	if(n1==0)
		return 0;
	else
	{
		int q1 = (arg->p1 + arg->r1)/2;
		int q2 = binary_search(arg->T[q1], arg->T, arg->p2, arg->r2);
		int q3 = arg->p3 + (q1 - arg->p1) + (q2 - arg->p2);
		arg->A[q3] = arg->T[q1];
		argument1 arg1, arg2;

		arg1.T = arg->T;
		arg1.p1 = arg->p1;
		arg1.r1 = q1-1;
	        arg1.p2 = arg->p2;
		arg1.r2 = q2 -1;
		arg1.A= arg->A;
		arg1.p3 = arg->p3;

		arg2.T = arg->T;
		arg2.p1 = q1 + 1;
		arg2.r1 = arg->r1;
	        arg2.p2 = q2;
		arg2.r2 = arg->r2;
		arg2.A= arg->A;
		arg2.p3 = q3 + 1;
		

		pthread_mutex_lock(&lock1);
		if(num_threads1<max_threads)
		{
			pthread_t tid;
			num_threads1 += 1;
			pthread_mutex_unlock(&lock1);
			pthread_create(&tid, NULL, p_merge, &arg1);
			p_merge(&arg2);
			pthread_join(tid, NULL);
		}
		else
		{
			pthread_mutex_unlock(&lock1);
			p_merge(&arg1);	
			p_merge(&arg2);

		}
	}
	return 0;

}

int merge(int T[], int p1, int r1, int  p2, int r2, int A[], int p3)	
{
	int n1 = r1 - p1 + 1;
	int n2 = r2 - p2 + 1;
	if (n1 < n2) //ensure that n1 >= n2
	{
		swap(&p1, &p2);
		swap(&r1, &r2);
		swap(&n1, &n2);
	}

	if(n1==0)
		return 0;
	else
	{
		int q1 = (p1 + r1)/2;
		int q2 = binary_search(T[q1], T, p2, r2);
		int q3 = p3 + (q1 - p1) + (q2 -p2);
		A[q3] = T[q1];
		merge(T, p1, q1 - 1, p2, q2 - 1, A, p3);
		merge(T, q1 + 1, r1, q2, r2, A, q3 + 1);
	}
	return 0;
}

void *p_merge_sort(argument2 *arg)
{
	int n = arg->r - arg->p + 1;
	if (n==1)
		arg->B[arg->s] = arg->A[arg->p];
	else
	{

		int T[n];
		int q1 = (arg->p + arg->r)/2;
		int q2 = q1 - arg->p + 1;
		argument2 arg1, arg2;
		argument1 arg3;

		arg1.A = arg->A;
		arg1.p = arg->p;
		arg1.r = q1;
		arg1.B = T;
		arg1.s = 1;

		arg2.A = arg->A;
		arg2.p = q1 + 1;
		arg2.r = arg->r;
		arg2.B = T;
		arg2.s = q2+1;
		
		arg3.T = T;
		arg3.p1 = 1;
		arg3.r1 = q2;
	        arg3.p2 = q2+1;
		arg3.r2 = n;
		arg3.A= arg->B;
		arg3.p3 = arg->s;

		
		pthread_mutex_lock(&lock1);
		if(num_threads1<max_threads)
		{
			num_threads1 += 1;
			pthread_t tid;
			pthread_mutex_unlock(&lock1);
			pthread_create(&tid, NULL, p_merge_sort, &arg1);
			p_merge_sort(&arg2);
			pthread_join(tid, NULL);

		}
		else
		{
			pthread_mutex_unlock(&lock1);
			p_merge_sort(&arg1);
			p_merge_sort(&arg2);
		}

		p_merge(&arg3);
	}

}

void merge_sort(int A[], int p, int r, int B[], int s)
{
	int n = r - p + 1;
	if (n==1)
		B[s] = A[p];
	else
	{
		int T[n];
		int q1 = (p + r)/2;
		int q2 = q1 - p + 1;
		merge_sort(A, p, q1, T, 1);
		merge_sort(A, q1+1, r, T, q2 + 1);
		merge(T, 1, q2, q2+1, n, B, s);
	}
}


void print_array(int A[], int len_A)
{
	int i;
	for (i = 0; i < len_A; i++)
		printf("\n%d ", A[i]);
	
	printf("\n");
}
void run_p_merge_sort(int A[], size_t size, int L[])
{
	argument2 data;
	data.A = A;
	data.p = 0;
	data.r = size-1;
	data.B = L;
	data.s = 0;
	printf("Array of size: %ld%s", size, "\n");
	time_t time_elapsed = clock();
	p_merge_sort(&data);
	time_elapsed = clock() - time_elapsed;
	printf("Time elapsed : %ld \n\n", time_elapsed); 

}

void run_merge_sort(int A[], size_t size, int L[])
{
	printf("Array of size: %ld%s", size, "\n");
	time_t time_elapsed = clock();
	merge_sort(A, 0, size-1, L, 0);
	time_elapsed = clock() - time_elapsed;
	printf("Time elapsed : %ld \n\n", time_elapsed); 

}
void main()
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
		else if (i<100000)
		{
			int tmp = rand() % 10000000;
			D[i] = tmp;
		}
	}

	printf("P_merge_sort && P_merge\n\n");
	int L[100000];
	run_p_merge_sort(D, 100000, L);
	run_p_merge_sort(C, 10000, L);
	run_p_merge_sort(B, 1000, L);
	run_p_merge_sort(A, 100, L);

	printf("sequential merge_sort && sequential merge\n\n");
	run_merge_sort(D, 100000, L);
	run_merge_sort(C, 10000, L);
	run_merge_sort(B, 1000, L);
	run_merge_sort(A, 100, L);







}
