
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>
#include <unistd.h>
#include <limits.h>
#include <errno.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>

bool is_ready_1 = false;
bool is_ready_2 = false;
long double time_elapsed_1 = 0;
long double time_elapsed_2 = 0;
long double time_elapsed_3 = 0;
long double start_time_1, start_time_2;
struct timeval start_time_3, end_time_3;

//define a matrix node type
typedef struct Node
{
	long double value;
	struct Node *next_right;
        struct Node *next_down;
}Node;

//create a new node for the matix
Node* create_node(long double value)
{
	//printf("I am here");
	Node* new_node = malloc(sizeof(Node));
	if(!new_node)
	{
		perror("Memory allocation error");
		abort();
	}

	new_node->next_right = NULL;
	new_node->next_down = NULL;
	new_node->value = value; 
	return new_node;
}


//create a matrix using double linked lists
Node* create_rand_matrix(int n_rows, int n_cols)
{
	Node *head, *node, *matrix[n_cols];
	int i, j;
        long double rand_value;
	for(i = 0; i < n_rows; i++)
	{
		for(j = 0; j < n_cols; j++)
		{
			rand_value = -1.0 + ((long double)rand() / (long double)RAND_MAX ) * 2.0 ; // generate a pseudo random integer between -1 and 1
			node = create_node(rand_value); //create a new node with value = rand_value
			if (j > 0) //if not the first elt in row, assign new cated node to previous_element_in_matrix->next_right
				matrix[j - 1]->next_right = node; 
			if (i > 0)//if not the first elt in colomn, assign new created node to current_element_in_matrix->next_down
				matrix[j]->next_down = node;
			if (j == 0 && i == 1) //assign first element in the matrix to head(here because we know that head already has a next_down and next_right elt)
			       head = matrix[j];	
			matrix[j] = node;

		}
	}
	if(!head)
		head = create_node(rand_value);
	return head;
}

//create a matrix using double linked lists
Node* create_matrix(int n_rows, int n_cols)
{
	Node *head, *node, *matrix[n_cols];
	int i, j;
        long double value;
	for(i = 0; i < n_rows; i++)
	{
		for(j = 0; j < n_cols; j++)
		{
			value = 0; 
			node = create_node(value); //create a new node with value = rand_value
			if (j > 0) //if not the first elt in row, assign new cated node to previous_element_in_matrix->next_right
				matrix[j - 1]->next_right = node; 
			if (i > 0)//if not the first elt in colomn, assign new created node to current_element_in_matrix->next_down
				matrix[j]->next_down = node;
			if (j == 0 && i == 1) //assign first element in the matrix to head(here because we know that head already has a next_down and next_right elt)
			       head = matrix[j];	
			matrix[j] = node;
		}
	}
	if(!head)
		head = node;
	return head;
}


//create an identity matrix using double linked lists
Node* create_id_matrix(int n_rows, int n_cols)
{
	Node *head, *node, *matrix[n_cols];
	int i, j, value;
	for(i = 0; i < n_rows; i++)
	{
		for(j = 0; j < n_cols; j++)
		{
			if (i == j)
				value = 1;
			else
				value = 0;
			node = create_node(value); //create a new node with value = rand_value
			if (j > 0) //if not the first elt in row, assign new cated node to previous_element_in_matrix->next_right
				matrix[j - 1]->next_right = node; 
			if (i > 0)//if not the first elt in colomn, assign new created node to current_element_in_matrix->next_down
				matrix[j]->next_down = node;
			if (j == 0 && i == 1) //assign first element in the matrix to head(here because we know that head already has a next_down and next_right elt)
			       head = matrix[j];	
			matrix[j] = node;
		}
	}
	if(!head)
		head = node;
	return head;
}



//count the number of rows in the matrix
int get_num_rows(Node *head)
{
	int count = 0;
	while (head!=NULL)
	{
		count++;
		head = head->next_down;
	}

	return count;
}

//count the number of column in the matrix
int get_num_cols(Node *head)
{
	int count = 0;
	while (head!=NULL)
	{
		count++;
		head = head->next_right;
	}

	return count;
}

//return smallest value between a and b
int min(int a, int b)
{
	if(a < b)
		return a;
	else
		return b;
}


//delete a row of a matrix
void delete_row(Node *head)
{
	if (head->next_right == NULL)
		free(head);
	else
	{
		delete_row(head->next_right);
		head->next_right = NULL;
		free(head);
	}
}

//delete entire matrix
void delete_matrix(Node *head)
{
	if (head->next_down == NULL)
		delete_row(head);
	else
	{
		delete_matrix(head->next_down);
		head->next_down = NULL;
		delete_row(head);
	}
}

//print a double linked list matrix
void print_matrix(Node *matrix)
{
	Node *tmp_row_head = matrix, *tmp_first_col_head = matrix;
	//printf("%i     ", tmp_row_head->value); //print first element in the matrix
	
	while(tmp_first_col_head != NULL)
	{

		while(tmp_row_head != NULL)  //print a row of the matrix
		{
			if (tmp_row_head->value < 0.0)
				printf("%Lf%s",tmp_row_head->value, "  "); 
			else
				printf("+%Lf%s",tmp_row_head->value, "  ");
			tmp_row_head = tmp_row_head->next_right;

		}
		tmp_first_col_head = tmp_first_col_head->next_down;
		tmp_row_head = tmp_first_col_head;
		printf("\n");

	}
}

//argument type for function construc_matrix_from_rows_heads
typedef struct Arg1
{
	Node **rows_heads;
	int i;	
}Arg1;

//construct a matrix knowing only the heads of each rows
void* construct_matrix_from_rows_heads(void *arg1)
{
	while(!is_ready_1)
        {
                sleep(0.001);
        }
	Arg1 *arg = (Arg1 *)arg1; 
	Node *node_1 = arg->rows_heads[arg->i];
	Node *node_2 = arg->rows_heads[arg->i + 1];
	node_1->next_down = arg->rows_heads[arg->i + 1];
	while(node_1->next_right)
	{

		node_1 = node_1->next_right;
		node_1->next_down = node_2->next_right;
		node_2 = node_2->next_right;

	}

	free((Arg1 *)arg1);
}

//argument type for function compute_one_row
typedef struct Arg
{
        int n_cols, i;
	Node *matrix_1;
	Node *matrix_2;
	Node **rows_heads;
}Arg;

//compute one row of the matrix resulting from matrix multiplication
void *compute_one_row(void *arg1)
{
	while(!is_ready_1)
	{
		sleep(0.001);
	}
	Arg *arg = (Arg *)arg1;
	Node *node_1 = arg->matrix_1;
	Node *init_node_1 = arg->matrix_1;
	Node *node_2 = arg->matrix_2;
	Node *init_node_2 = arg->matrix_2;
	Node *tmp_node, *head;
	long double sum = 0;
	for (int i = 0; i < arg->i; i++)
	{
		node_1 = node_1->next_down;
		init_node_1 = init_node_1->next_down;
	}

	for (int j = 0; j < arg->n_cols; j++)
	{
		while(node_1)
		{
			sum += node_1->value * node_2->value;
			node_1 = node_1->next_right;
			node_2 = node_2->next_down;
		}
		Node *node = create_node(sum);
		if (j == 0)
		{
			head = node;
			tmp_node = node;
		}
		else
		{
			tmp_node->next_right = node;
			tmp_node = tmp_node->next_right;
		}
		node_1 = init_node_1;
		node_2 = init_node_2->next_right;
		init_node_2 = init_node_2->next_right;
		sum = 0;

	}

	arg->rows_heads[arg->i] = head;

	free((Arg *)arg1);
	
}

//multiply 2 matrices
void multiply_matrices(Node *result, Node *matrix_1, Node *matrix_2)
{

	int n_rows = get_num_rows(matrix_1); //min(get_num_cols(matrix_1), get_num_rows(matrix_2));
	int n_cols = get_num_cols(matrix_2); //min(get_num_cols(head_1), get_num_cols(head_2));
	Node **rows_heads = malloc(sizeof(Node *) * n_rows);

	pthread_t thread_IDs[n_rows];
	int count = 0;


	for(int i = 0; i < n_rows; i++)
	{

		Arg *arg = malloc(sizeof(Arg));

		arg->matrix_1 = matrix_1;
		arg->matrix_2 = matrix_2;
		arg->i = i;
		arg->n_cols = n_cols;
		arg->rows_heads = rows_heads;


		int thread_return = pthread_create(&thread_IDs[count], NULL, compute_one_row,  (void *)arg);

		if(thread_return)//check if thread was created
                {
                        perror("Error during thread creation");
                        exit(-1);
                }
                count++;


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


	pthread_t thread_IDs_2[n_rows];
	count = 0;
	

	for(int i = 0; i < n_rows - 1; i++)
	{
		Arg1 *arg1 = malloc(sizeof(Arg1));
		arg1->rows_heads = rows_heads;
		arg1->i = i;

		int thread_return = pthread_create(&thread_IDs_2[count], NULL, construct_matrix_from_rows_heads,  (void *)arg1);

                if(thread_return)//check if thread was created
                {
                        perror("Error during thread creation");
                        exit(-1);
                }
                count++;
	}
	
	while(!is_ready_2)
	{
		is_ready_2 = true;
		for(int i = 0; i < count; i++)
		{
			if(!thread_IDs_2[i])
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
                pthread_join(thread_IDs_2[i], NULL);

	time_elapsed_1 += ((long double)time(NULL) - start_time_1);
        time_elapsed_2 += ((long double)(clock() - start_time_2)/CLOCKS_PER_SEC);
        gettimeofday(&end_time_3, NULL);
        time_elapsed_3 += (end_time_3.tv_sec - start_time_3.tv_sec) + (end_time_3.tv_usec - start_time_3.tv_usec)/1000000.00;


	
	/*delete_matrix(result);
	result = create_node(0);*/
	result->value = rows_heads[0]->value;
	result->next_down = rows_heads[0]->next_down;
	result->next_right = rows_heads[0]->next_right;

	for(int i = 0; i < n_rows; i++)
	{
		rows_heads[i] = NULL;
		
	}
	free(rows_heads);
}

//compute the power of a matrix
void pow_matrix(Node *result, Node* matrix, int power)
{
	if (power < 0)
	{
		perror("Sorry, the second argument of pow_matrix must be a positive integer");
		abort();
	}
	else if(power == 0 )
	{
		int n_cols = get_num_cols(matrix);
		int n_rows = get_num_rows(matrix);
		Node *tmp = create_id_matrix(n_rows, n_cols);
		result->value = tmp->value;
		result->next_right = tmp->next_right;
		result->next_down = tmp->next_down;
	}
	else if (power == 1)
	{
		result->value = matrix->value;
		result->next_down = matrix->next_down;
		result->next_right = matrix->next_right;
	}
	else if (power == 2)
	{

		multiply_matrices(result, matrix, matrix);
	}
	else
	{
		Node *tmp = create_node(0);
		pow_matrix(tmp, matrix, power - 1);
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

//create a matrix, compute its power and print the results 
void API(int dim, int power, int version)
{
        //long double start_time_1, time_elapsed_1, start_time_2, time_elapsed_2, time_elapsed_3;
        //struct timeval start_time_3, end_time_3;
	Node *head = create_rand_matrix(dim, dim);
	//printf("\n-----------------------------------------------------------matrice(s)-----------------------------------------------------------\n");
	//print_matrix(head);
	//printf("\n\n----------------------------------------------------result of matrix to power:%i%s", power,"------------------------------------------\n");
	Node *result = create_node(0);
        //start_time_1 = (long double)time(NULL);
        //start_time_2 = (long double)clock();
        //gettimeofday(&start_time_3, NULL);
	pow_matrix(result, head, power);
        //time_elapsed_1 = ((long double)time(NULL) - start_time_1);
        //time_elapsed_2 = ((long double)(clock() - start_time_2)/CLOCKS_PER_SEC);
        //gettimeofday(&end_time_3, NULL);
        //time_elapsed_3 = (end_time_3.tv_sec - start_time_3.tv_sec) + (end_time_3.tv_usec - start_time_3.tv_usec)/1000000.00;
	//print_matrix(head);
	printf("\n");
	//print_matrix(result);	
        printf("\n\n---------------------------running times for dimensionality = %i%s%i%s", dim, " and exponent = ", power,"---------------------------------\n");
        if(version == 1)
        {
                write_to_file("experimentation_results/dll_time()_fixed_dim.csv", time_elapsed_1, dim, power);
                write_to_file("experimentation_results/dll_clock()_fixed_dim.csv", time_elapsed_2, dim, power);
                write_to_file("experimentation_results/dll_gettimeofday()_fixed_dim.csv", time_elapsed_3, dim, power);
        }
        else
        {
                write_to_file("experimentation_results/dll_time()_fixed_power.csv", time_elapsed_1, dim, power);
                write_to_file("experimentation_results/dll_clock()_fixed_power.csv", time_elapsed_2, dim, power);
                write_to_file("experimentation_results/dll_gettimeofday()_fixed_power.csv", time_elapsed_3, dim, power);
        }
        printf("\nRunning time using the function time(): %Lf%s", time_elapsed_1, " second(s).\n");
        printf("\nRunning time using the function clock(): %Lf%s", time_elapsed_2, " second(s).\n");
        printf("\nRunning time using the function gettimeofday(): %Lf%s", time_elapsed_3, " second(s).\n");
	delete_matrix(head);
	delete_matrix(result);
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
			
