	
#include <stdio.h>
#include <stdlib.h>
#include<sys/time.h>
#include <time.h>
#include <string.h>
#include <errno.h>
#include <stdbool.h>
#include <limits.h>
#include <pthread.h>
#include <unistd.h>

bool is_ready_1 = false;
bool is_ready_2 = false;
long double time_elapsed_1 = 0;
long double time_elapsed_2 = 0;
long double time_elapsed_3 = 0;
long double start_time_1, start_time_2;
struct timeval start_time_3, end_time_3;

//define a matrix node type
typedef struct Matrix
{
	long double** values;
	int n_rows;
	int n_cols;
}Matrix;

typedef struct Arg
{
	Matrix *result;
	Matrix *matrix_1;
       	Matrix *matrix_2;
       	int i, j, n_mult;
}Arg;

//create a matrix of size n_rows * n_cols and populate it with random numbers between -1 and 1
Matrix* create_rand_matrix(int n_rows, int n_cols)
{

        Matrix *matrix = malloc(sizeof(Matrix *));
	if(!matrix)
		perror("Memory Allocation Error!");
	matrix->n_rows = n_rows;
	matrix->n_cols = n_cols;
	matrix->values = malloc(sizeof(long double *) * n_rows);
	if(!matrix->values)
		perror("Memory allocation error!");

	int i, j;
	for (i = 0; i < n_rows; i++)
	{
		matrix->values[i] = malloc(sizeof(long double)*n_cols);
		if(!matrix->values[i])
			perror("Memory allocation error!");
	}

	for(i = 0; i < n_rows; i++)
	{
		for(j = 0; j < n_cols; j++)
		{
			matrix->values[i][j] = -1.0 + ((long double)rand() / (long double)RAND_MAX ) * 2.0 ; // generate a pseudo random integer between 0 and 10
			
		}
	}
	return matrix;
}

//create a matrix of size n_rows * n_cols and populate it with 0s
Matrix* create_matrix(int n_rows, int n_cols)
{

        Matrix *matrix = malloc(sizeof(Matrix *));
	if(!matrix)
		perror("Memory Allocation Error!");
	matrix->n_rows = n_rows;
	matrix->n_cols = n_cols;
	matrix->values = malloc(sizeof(long double *) * n_rows);
	if(!matrix->values)
		perror("Memory allocation error!");

	int i, j;
	for (i = 0; i < n_rows; i++)
	{
		matrix->values[i] = calloc(n_cols, sizeof(long double));
		if(!matrix->values[i])
			perror("Memory allocation error!");
	}
	return matrix;
}


//create an identity matrix 
Matrix* create_id_matrix(int n_rows, int n_cols)
{
	Matrix *matrix = malloc(sizeof(Matrix *));
	if(!matrix)
		perror("Memory Allocation Error!");
	matrix->n_rows = n_rows;
	matrix->n_cols = n_cols;
	matrix->values = malloc(sizeof(long double *) * n_rows);
	if(!matrix->values)
		perror("Memory allocation error!");

	int i, j;
	for (i = 0; i < n_rows; i++)
	{
		matrix->values[i] = malloc(sizeof(long double)*n_cols);
		if(!matrix->values[i])
			perror("Memory allocation error!");
	}
	for(i = 0; i < n_rows; i++)
	{
		for(j = 0; j < n_cols; j++)
		{
			if (i == j)
				matrix->values[i][j] = 1.0;
			else
				matrix->values[i][j] = 0.0;
			
		}
	}
	return matrix;

}



//return smallest value between a and b
int min(int a, int b)
{
	if(a < b)
		return a;
	else
		return b;
}



//delete entire matrix
void delete_matrix(Matrix *matrix)
{
	int i;
	for (i = 0; i < matrix->n_rows; i++)
	{
		free(matrix->values[i]);
	}
	free(matrix->values);
}

//delete an Arg struct
void delete_Arg(Arg *arg)
{
	delete_matrix(arg->result);
	delete_matrix(arg->matrix_1);
	delete_matrix(arg->matrix_2);

}

//print a matrix
void print_matrix(Matrix *matrix)
{
	int i, j;
	for(i = 0; i < matrix->n_rows; i++)
	{
		for(j = 0; j < matrix->n_cols; j++)
		{
			if (matrix->values[i][j] < 0)
				printf("%Lf%s", matrix->values[i][j], "  ");
			else
				printf("%s%Lf%s","+", matrix->values[i][j], "  ");
		}
		printf("\n");
	}
}

//compute one element of the resulting matrix from matrix  multiplication
void *compute_one_elt( void *arg1)
{
	while(!is_ready_1)
	{
		sleep(0.001);
	}
	Arg *arg = (Arg *)arg1; 
	long double sum_values = 0;
	for(int k = 0; k < arg->n_mult; k++)
		sum_values += arg->matrix_1->values[arg->i][k] * arg->matrix_2->values[k][arg->j];
	arg->result->values[arg->i][arg->j] = sum_values;
	if((Arg *)arg1)
		free((Arg *)arg1);
}


//multiply two matrices and return the resulting matrix
void multiply_matrices(Matrix *result, Matrix *matrix_1,  Matrix *matrix_2)
{
	pthread_t thread_IDs[result->n_rows* result->n_cols];
	int count = 0;
	int n_multiplications = min(matrix_1->n_cols, matrix_2->n_rows);  //number of multiplication to be done to fill out one cell of the resulting matrix
	for (int i = 0; i < matrix_1->n_rows; i++)
	{
		
	
		for(int j = 0; j < matrix_2->n_cols; j++)
		{
			
			Arg *arg = malloc(sizeof(Arg));
			if(!arg)
				perror("Memory Allocation Error!");

			arg->result = result, arg->matrix_1 = matrix_1, arg->matrix_2 = matrix_2, arg->i = i, arg->j = j, arg->n_mult = n_multiplications;
			int thread_return = pthread_create(&thread_IDs[count], NULL, compute_one_elt, (void *)arg);
			//sleep(0.001);

			if(thread_return)
                	{
                        	perror("Error during thread creation");
                        	exit(-1);
               	 	}


			count++;

		}
	}

	while(!is_ready_2)
        {
                is_ready_2 = true;
                for(int i = 0; i < count; i++)
                {
                        if(!thread_IDs[i])
			{
				is_ready_2 = false;
				break;
			}
                }
        }
	is_ready_2 = false;
	is_ready_1 = true;
	
	start_time_1 = (long double)time(NULL);
        start_time_2 = (long double)clock();
        gettimeofday(&start_time_3, NULL);

	for(int i = 0; i < count; i++)
                pthread_join(thread_IDs[i], NULL);

	time_elapsed_1 += ((long double)time(NULL) - start_time_1);
        time_elapsed_2 += ((long double)(clock() - start_time_2)/CLOCKS_PER_SEC);
        gettimeofday(&end_time_3, NULL);
        time_elapsed_3 += (end_time_3.tv_sec - start_time_3.tv_sec) + (end_time_3.tv_usec - start_time_3.tv_usec)/1000000.00;

}

//compute the power of a matrix
void pow_matrix(Matrix *result, Matrix* matrix, int power)
{
	if (power < 0)
	{
		perror("Sorry, the second argument of pow_matrix must be a positive integer");
		exit(-1);
	}
	else if(power == 0 )
	{
		int n_cols = matrix->n_cols;
		int n_rows = matrix->n_rows;
		result->values = create_id_matrix(n_rows, n_cols)->values;
	}
	else if (power == 1)
	{
		result->values = matrix->values;
	}
	else
	{

		Matrix *tmp = create_matrix(matrix->n_rows, matrix->n_cols);
		for(int i = 0; i < result->n_rows; i++)
                        {
                                memcpy(result->values[i], matrix->values[i], sizeof(long double) * result->n_cols);
                        }

		
		for(int i = 0; i < power - 1; i++)
		{
			for(int j = 0; j < result->n_rows; j++)
			{
				memcpy(tmp->values[j], result->values[j], sizeof(long double) * result->n_cols);
			}
			multiply_matrices(result, tmp, matrix);
		}

		delete_matrix(tmp);

	}


}

//write data to a file
void write_to_file(char* file_name, long double time, int dim, int power)
{
	FILE *file = fopen(file_name, "a");
	if (file == NULL)
	{
		perror("Error opening file");
		abort();
	}
	fprintf(file, "%i%s%i%s%Lf%s", dim, ",", power, ",", time, "\n");
       fclose(file);	
}

//create a matrix, compute its  power and print the results
void API(int dim, int power, int version)
{
	Matrix *matrix = create_rand_matrix(dim, dim);
	//printf("\n-----------------------------------------------------------matrix-----------------------------------------------------------\n");
	//print_matrix(matrix);
	//printf("\n\n----------------------------------------------------result of matrix to power:%i%s", power,"------------------------------------------\n");
	Matrix *result = create_matrix(dim, dim);
	pow_matrix(result, matrix, power);

	printf("\n\n---------------------------running times for dimensionality = %i%s%i%s", dim, " and exponent = ", power,"---------------------------------\n");
	if(version == 1)
	{
		write_to_file("experimentation_results/classical_time()_fixed_dim.csv", time_elapsed_1, dim, power);
		write_to_file("experimentation_results/classical_clock()_fixed_dim.csv", time_elapsed_2, dim, power);
		write_to_file("experimentation_results/classical_gettimeofday()_fixed_dim.csv", time_elapsed_3, dim, power);
	}
	else
	{
		write_to_file("experimentation_results/classical_time()_fixed_power.csv", time_elapsed_1, dim, power);
		write_to_file("experimentation_results/classical_clock()_fixed_power.csv", time_elapsed_2, dim, power);
		write_to_file("experimentation_results/classical_gettimeofday()_fixed_power.csv", time_elapsed_3, dim, power);
	}
	printf("\nRunning time using the function time(): %Lf%s", time_elapsed_1, " second(s).\n");
	printf("\nRunning time using the function clock(): %Lf%s", time_elapsed_2, " second(s).\n");
	printf("\nRunning time using the function gettimeofday(): %Lf%s", time_elapsed_3, " second(s).\n");
	if(result)
		delete_matrix(result);
	if(matrix)
		delete_matrix(matrix);

}

int main(int argc, char *argv[])
{
	if(argc < 4)
	{
		perror("Sorry you need to provide three integers as argument \n argument 1: an intger for the version of the algorithm to be run. Enter 1 for fixed dimensionality or any other integer for fixed power) \n argument 2: an integer M  for the matrix dimensionality  (the matrix will have the size M*M) \n argument 3: an integer N for the power of the matrix");
		
		return -1;
	}
	char *c;
	int power = strtol(argv[3], &c, 10);
	if (*c != '\0' || errno != 0 || power > INT_MAX) 
	{
		perror("Sorry wrong type for argument. Please make sure you provide integers for all arguments");
		return -1;
	}

	char *d;
	int dim = strtol(argv[2], &d, 10);
	if (*d != '\0' || errno != 0 || dim > INT_MAX) 
	{
		perror("Sorry wrong type for argument. Please make sure you provide integers for all arguments");
		return -1;
	}
	char *e; //this argument is just to know which file the results should be saved in
	int version = strtol(argv[1], &e, 10);

	if (*e != '\0' || errno != 0 || version > INT_MAX) 
	{
		perror("Sorry wrong type for argument. Please make sure you provide integers for all arguments");
		return -1;
	}

	//srand(time(NULL));
	API(dim, power, version);
	return 0;

}
			
