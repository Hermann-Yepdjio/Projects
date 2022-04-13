
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>
#include <limits.h>
#include <errno.h>

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

//multiply 2 matrices
void multiply_matrices(Node *result, Node *matrix_1, Node *matrix_2)
{
	int i, j;
	long double sum_values;
	int n_rows = get_num_rows(matrix_1); //min(get_num_cols(matrix_1), get_num_rows(matrix_2));
	int n_cols = get_num_cols(matrix_2); //min(get_num_cols(head_1), get_num_cols(head_2));
	Node *node, *matrix[n_cols];
	Node *matrix_1_row_head, *matrix_2_col_head;
	Node *tmp_matrix_1 = matrix_1;
	Node *tmp_matrix_2 = matrix_2;


	for(i = 0; i < n_rows; i++)
	{
		for(j = 0; j < n_cols; j++)
		{
			
			matrix_1_row_head = tmp_matrix_1;
			matrix_2_col_head = tmp_matrix_2;
			sum_values = 0; // hold value for one node of the resulting matrix from matrices product
			while(matrix_1_row_head != NULL || matrix_2_col_head != NULL)
			{
				sum_values += matrix_1_row_head->value * matrix_2_col_head->value;
				matrix_1_row_head = matrix_1_row_head->next_right;
			       	matrix_2_col_head = matrix_2_col_head->next_down;
			}
			tmp_matrix_2 = tmp_matrix_2->next_right;
	

			node = create_node(sum_values); //create a new node with value = rand_value
			if (j > 0) //if not the first elt in row, assign new cated node to previous_element_in_matrix->next_right
				matrix[j - 1]->next_right = node; 
			if (i > 0)//if not the first elt in colomn, assign new created node to current_element_in_matrix->next_down
				matrix[j]->next_down = node;
			if (j == 0 && i == 1) //assign first element in the matrix to head(here because we know that head already has a next_down and next_right elt)
			{
				result->value = matrix[j]->value;
				result->next_right = matrix[j]->next_right;
				result->next_down = matrix[j]->next_down;
			}
			matrix[j] = node;
		}
		tmp_matrix_1 = tmp_matrix_1->next_down;
		tmp_matrix_2 = matrix_2;
	}
	

	if(result == NULL)
	{
		result = node;
	}

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
		result = create_id_matrix(n_rows, n_cols);
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
		Node *tmp = create_matrix(get_num_rows(matrix), get_num_cols(matrix));
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
        long double start_time_1, time_elapsed_1, start_time_2, time_elapsed_2, time_elapsed_3;
        struct timeval start_time_3, end_time_3;
	Node *head = create_rand_matrix(dim, dim);
	//printf("\n-----------------------------------------------------------matrice(s)-----------------------------------------------------------\n");
	//print_matrix(head);
	//printf("\n\n----------------------------------------------------result of matrix to power:%i%s", power,"------------------------------------------\n");
	Node *result = create_matrix(dim, dim);
        start_time_1 = (long double)time(NULL);
        start_time_2 = (long double)clock();
        gettimeofday(&start_time_3, NULL);
	pow_matrix(result, head, power);
        time_elapsed_1 = ((long double)time(NULL) - start_time_1);
        time_elapsed_2 = ((long double)(clock() - start_time_2)/CLOCKS_PER_SEC);
        gettimeofday(&end_time_3, NULL);
        time_elapsed_3 = (end_time_3.tv_sec - start_time_3.tv_sec) + (end_time_3.tv_usec - start_time_3.tv_usec)/1000000.00;
	print_matrix(result);	
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
	if(argc < 3)
        {
                perror("Sorry you need to provide two integers as argument(M for matrix dimensionality and N for the power of the matrix");
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
			
