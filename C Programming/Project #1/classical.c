
#include <stdio.h>
#include <stdlib.h>
#include<sys/time.h>
#include <time.h>
#include <errno.h>
#include <limits.h>

//define a matrix node type
typedef struct Matrix
{
	long double** values;
	int n_rows;
	int n_cols;
}Matrix;

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

//multiply two matrices and return the resulting matrix
void multiply_matrices(Matrix *result, Matrix *matrix_1, Matrix *matrix_2)
{
	int i, j, k;
	long double sum_values;
	//Matrix *result = create_matrix(matrix_1->n_rows, matrix_2->n_cols);
	int n_multiplications = min(matrix_1->n_cols, matrix_2->n_rows);  //number of multiplication to be done to fill out one cell of the resulting matrix
	for (i = 0; i < matrix_1->n_rows; i++)
	{
		for(j = 0; j < matrix_2->n_cols; j++)
		{
			sum_values = 0;
			for(k = 0; k < n_multiplications; k++)
			{
				sum_values += matrix_1->values[i][k] * matrix_2->values[k][j];
			}
			result->values[i][j] = sum_values;
		}
	}
	//return result;
}

//compute the power of a matrix
void pow_matrix(Matrix *result, Matrix* matrix, int power)
{
	if (power < 0)
	{
		perror("Sorry, the second argument of pow_matrix must be a positive integer");
		abort();
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
	else if (power == 2)
	{
		
		//result->values = 
		multiply_matrices(result, matrix, matrix);
		//return result;
	}
	else
	{

		Matrix *tmp = create_matrix(matrix->n_rows, matrix->n_cols);
		pow_matrix(tmp, matrix, power - 1);
		//tmp->values = result->values;
		multiply_matrices(result, matrix, tmp);
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
	long double start_time_1, time_elapsed_1, start_time_2, time_elapsed_2, time_elapsed_3;
	struct timeval start_time_3, end_time_3;	
	Matrix *matrix = create_rand_matrix(dim, dim);
	//printf("\n-----------------------------------------------------------matrix-----------------------------------------------------------\n");
	//print_matrix(matrix);
	//printf("\n\n----------------------------------------------------result of matrix to power:%i%s", power,"------------------------------------------\n");
	Matrix *result = create_matrix(dim, dim);
	start_time_1 = (long double)time(NULL);
	start_time_2 = (long double)clock();
	gettimeofday(&start_time_3, NULL);
	pow_matrix(result, matrix, power);
	time_elapsed_1 = ((long double)time(NULL) - start_time_1);
	time_elapsed_2 = ((long double)(clock() - start_time_2)/CLOCKS_PER_SEC);
	gettimeofday(&end_time_3, NULL);
	time_elapsed_3 = (end_time_3.tv_sec - start_time_3.tv_sec) + (end_time_3.tv_usec - start_time_3.tv_usec)/1000000.00;
	//print_matrix(result);	
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
		perror("Sorry you need to provide two integers as argument(version (1 for fixed dimensionality and sthg else for fixed power),M  for matrix dimensionality and N for the power of the matrix");
		return -1;
	}
	char *c;
	int power = strtol(argv[3], &c, 10);
	if (*c != '\0' || errno != 0 || power > INT_MAX) 
	{
		perror("Sorry wrong type for argument. Please make sure you provide integers for both arguments");
		return -1;
	}

	char *d;
	int dim = strtol(argv[2], &d, 10);
	if (*d != '\0' || errno != 0 || dim > INT_MAX) 
	{
		perror("Sorry wrong type for argument. Please make sure you provide integers for both arguments");
		return -1;
	}
	char *e;
	int version = strtol(argv[1], &e, 10);

	if (*e != '\0' || errno != 0 || version > INT_MAX) 
	{
		perror("Sorry wrong type for argument. Please make sure you provide integers for both arguments");
		return -1;
	}

	//srand(time(NULL));
	API(dim, power, version);
	return 0;

}
			
