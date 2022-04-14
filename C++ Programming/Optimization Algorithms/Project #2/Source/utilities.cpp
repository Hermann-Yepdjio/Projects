/*********************************************
*                                            *
* utilities.cpp                              *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #2                                 *
* Last modified on Wednesday April 17, 2019  *
*                                            *
*********************************************/



#include <iostream>
#include <fstream>
#include "matrix.h"
#include "functions.h"
#include <cstring>
#include "utilities.h"
#include <chrono>
#include <float.h>
#include <time.h>
#include "search_functions.h"

using namespace std;
using namespace std::chrono;



/*
 * split a string into double tokens
 *
 * @param string: the string to be splitted
 * @param delim: the character that separates the tokens in the string
 * @param num_tokens: number of tokens to expect
 *
 * @return : an array of doubles
 *
 */

double* utilities::str_to_tok(char* string, char* delim, int num_tokens)
{
	double* tokens = new double[num_tokens];
	char* token= strtok(string, delim); //returns first token
	int index = 0;

	//keep reading tokens until there is no more left or index reaches the number of dimensions we want to test the functions on
	while(token != NULL and index < num_tokens) //
	{
		tokens[index] = stod(token);
		token = strtok(NULL, delim);
		index++;	
	}

	return tokens;


}

/*
 * write a 2d array to a csv file
 *
 * @param mat: a matrix containing the elements to write to the csv file
 * @param file_name: the name of the file where data will be saved
 *
 * @return : None
 */
void utilities::write_to_file(matrix* mat, string file_name)
{
	ofstream file_writer;
	file_writer.open(file_name);
	for(int i = 0; i < mat->num_rows; i++)
	{
		for(int j = 0; j < mat->num_columns; j++)
		{
			if( j < mat->num_columns - 1)
				file_writer << mat->mat[i][j] << ",";
			else
				file_writer << mat->mat[i][j] << "\n";
		}
	}

	file_writer.close();

}


//get a number from the user for the search algorithm to be run
int utilities::get_algorithm_id()
{
	int algorithm_id;
	cout << "\nWhich search algorithm would you like to run? \n" << "Enter 1 for Randon Walk, 2 for Local Search or any other number for Iterative Local Seach: ";
	cin>>algorithm_id;
	if(cin.fail())
	{
		perror("Sorry invalid input. Please Try again and make sure to enter an integer");
		exit(-1);
	}

	if (algorithm_id == 1)
		cout<<"\n-----------------------------------------------------------Starting Random Walk-------------------------------------------------------------------------\n";
	else if (algorithm_id == 2)
		cout<<"\n---------------------------------------------------------Starting Local Search--------------------------------------------------------------------------\n";
	else 
		cout<<"\n-------------------------------------------------Starting Iterative Local Search------------------------------------------------------------------------\n";

	return algorithm_id;

}



/*
 * simulate all the functions
 *
 * @param num_dimensions: the numbers of dimensions to be simulated
 * @param dimensions: an array containing the different dimensions to be simulated
 * @param num_functions: the number of functions to be simulated
 * @param ranges: an array containing values for the range of each function
 * @param sample_size: the size of the sample space
 *
 * @return : a 2d array containg all the results of the simulation
 *
 */
void utilities::simulate(int num_dimensions, double* dimensions, int num_functions, double* ranges, int sample_size, int algorithm_id, double delta, mt19937& mt_rand)
{
	matrix* result = new matrix(sample_size, num_functions * num_dimensions); //matrix that will hold all the results for all the calculations
	int index, count, count_2; //index is used to find the correct bound, count is used to know on which total_time should be inserted in the matrix
	matrix* run_times = new matrix(num_functions, num_dimensions); //To hold the run time for each function for each specific dimension
	double fitness, elapsed, total_time;
	double* solution;
	matrix* best_solutions;
	rw_input* input;
	string functions_names[num_functions] = {"Schwefel", "first_De_Jong", "Rosenbrock", "Rastrigin", "Greiwangk", "Sine_Envelope_Sine_Wave", "Stretched_V_Sine_Wave", "Ackley_One", "Ackley_Two", "Egg_Holder", "Rana", "Pathological", "Michalewicz", "Masters_Cosine_Wave", "Quartic", "Levy", "Step", "Alpine"};
	clock_t start;

	for(int i = 0; i < num_dimensions; i++)
	{
		index = 0; //to know where the lowest and highest bound for each function is located in the vector
		count = 0;
		count_2 = 0; //to know where to insert a new best solution into the best_solution matrix
		best_solutions = new matrix(sample_size * num_functions + num_functions, dimensions[i]); //To hold the best solutions found for a specific dimension
		
		for(int j = 0; j < num_functions; j++)
		{

			total_time = 0;
			cout << "\n\n-------------------------------Starting " << functions_names[j] <<"-------------------------------------------\n";

			for(int k = 0; k < sample_size; k++)
			{
				solution = new double[(int)dimensions[i]]();
				start = clock();
				if(algorithm_id == 1)
				{
					input = new rw_input();
                                	input->iterations = sample_size;
					input->best_solution = new double[(int)dimensions[i]]();
					random_walk(input, j, (int)dimensions[i], ranges[index], ranges[index + 1], mt_rand);

					fitness = input->best_fitness;
					memcpy(solution, input->best_solution, sizeof(double) * (int)dimensions[i]);
					delete input;
				}
				else if(algorithm_id == 2)
				{

					input = new rw_input();
                                	input->iterations = sample_size;
					input->best_solution = new double[(int)dimensions[i]]();
                                	local_search(input, j, (int)dimensions[i], ranges[index], ranges[index + 1], delta, mt_rand);

					fitness = input->best_fitness;
					memcpy(solution, input->best_solution, sizeof(double) * (int)dimensions[i]);
                                        delete input;

				}
				else
				{
					input = new rw_input();
                                	input->iterations = sample_size;
					input->best_solution = new double[(int)dimensions[i]]();
					iter_local_search(input, j, (int)dimensions[i], ranges[index], ranges[index + 1], delta, mt_rand);
					fitness = input->best_fitness;
					memcpy(solution, input->best_solution, sizeof(double) * (int)dimensions[i]);
                                        delete input;

				}
				
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;

				result->mat[k][i + num_dimensions * j] = fitness; //store the fitness in the result matrix
				
				memcpy(best_solutions->mat[count_2], solution, sizeof(double) * (int)dimensions[i]);
				/*for(int l = 0; l < dimensions[i]; l++)
					cout << solution[l] << " ";
				cout << "\n";
				cout << "delta: " << delta << "\n";*/
                                delete[] solution;
				count_2++;


				cout<<"\n" << functions_names[j] << "(dimension = " << dimensions[i] << ", sample #" << k + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
	

				total_time += elapsed;

				if (algorithm_id == 2)
					k = sample_size;
				
			}

			run_times->mat[count][i] = total_time;
			count++;
			count_2++;
			index = index + 2;
		}	
		write_to_file(best_solutions, "../Results/solutions_" + to_string((int)dimensions[i]) + ".csv");
		delete best_solutions;

	}
	if (algorithm_id == 1)
		cout<<"\n---------------------------------------------------------------End Random Walk-------------------------------------------------------------------------------\n";
	else if (algorithm_id == 2)
		cout<<"\n---------------------------------------------------------------End Local Search------------------------------------------------------------------------------\n";
	else
		cout<<"\n-------------------------------------------------------End Iterative Local Search----------------------------------------------------------------------------\n";


	write_to_file(result, "../Results/fitness.csv");
	write_to_file(run_times, "../Results/run_times.csv");

	delete result;
	delete run_times;

}

