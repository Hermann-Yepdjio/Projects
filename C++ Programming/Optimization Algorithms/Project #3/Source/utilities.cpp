/*********************************************
*                                            *
* utilities.cpp                              *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #3                                 *
* Last modified on Wednesday May 1st, 2019   *
*                                            *
*********************************************/



#include <iostream>
#include <fstream>
#include "matrix.h"
#include <cstring>
#include "utilities.h"
#include <chrono>
#include <float.h>
#include <time.h>
#include "GA.h"
#include "DE.h"

using namespace std;
using namespace std::chrono;



/**
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

/**
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


//get a number from the user for the evolutiinary algorithm to be run
int utilities::get_algorithm_id()
{
	int algorithm_id;
	cout << "\nWhich evolutionary algorithm would you like to run? \n" << "Enter 1 for Genetic Algorithm or any other number for Differential Evolution Algorithm: ";
	cin >> algorithm_id;
	if(cin.fail())
	{
		perror("Sorry invalid input. Please Try again and make sure to enter an integer");
		exit(-1);
	}

	if (algorithm_id == 1)
		cout<<"\n-----------------------------------------------------------Starting Genetic Algorithm-------------------------------------------------------------------------\n";
	else 
		cout<<"\n-------------------------------------------------Starting Differential Evolution Algorithm------------------------------------------------------------------------\n";

	return algorithm_id;

}

//get a number from the user for the selection algorithm to be run
int utilities::get_selection_id()
{
        int selection_id;
        cout << "\nWhich selection algorithm would you like to run? \n" << "Enter 1 for Roulette wheel or any other number for Tournament selection: ";
        cin>>selection_id;
        if(cin.fail())
        {
                perror("Sorry invalid input. Please Try again and make sure to enter an integer");
                exit(-1);
        }

        if (selection_id == 1)
		return 0;
        else
		return 1;
	return 0;
}


//find the lowest value in a list
double utilities::find_lowest(const double *list, int len)
{
	double lowest = INFINITY;
	for (int i = 0; i < len; i++)
	{
		if (list[i] < lowest)
			lowest = list[i];
	}

	return lowest;
}




/**
 * simulate both the genetic algoritm and the differencial evolution algorithm
 *
 * @param dim : an integer for the dimension of the solutions
 * @param ns : an integer the number of solutions
 * @param num_functions : an integer for the number of objective functions to be simulated (the 18 functions)
 * @param ranges: an array of doubles containing the lower and upper bound for each of the objective functions
 * @param algo_id: an integer for the evolutionary algorithm to be simulated
 * @param select_id: an integer for the selection algorithm to be used
 * @param num_gen : an integer for the number of generations for the evolutionary algorithms
 * @param num_exp: an integer for the number of experimentations to be run
 * @param num_trnmt: an integer for the number of tournaments for the tournameent selection algorithm
 * @param cr: a double for crossover rate 
 * @param er: a double for the elitism rate for the genetic algorithm
 * @param m_range: a double for the mutation range for the genetic algorithm
 * @param m_rate: a double for the mutation rate for the genetic algorithm
 * @param m_precision: a double for the mutation precision for the genetic algorithm
 * @param F: a double
 * @param lambda: a double
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return :  None
 */

void utilities::simulate(int dim, int ns, int num_functions, double* ranges, int algo_id, int select_id, int num_gen, int num_exp, int num_trnmt, double cr, double er, double m_range, double m_rate, double m_precision, double F, double lambda, mt19937& mt_rand)
{
 
	matrix* best_fitnesses = new matrix(num_exp * num_functions + num_exp, num_gen); //matrix that will hold all the best fitnesses accross all generations (for 1 strategy for the DE)
	int index = 0, count = 0; //index is used to find the correct bound, count is used to know where to insert results of one experimentation in best_fitnesses or run_times
	matrix* run_times = new matrix(num_exp * num_functions + num_exp, 1); //To hold the run time for each function for each specific dimension
	string functions_names[num_functions] = {"Schwefel", "first_De_Jong", "Rosenbrock", "Rastrigin", "Greiwangk", "Sine_Envelope_Sine_Wave", "Stretched_V_Sine_Wave", "Ackley_One", "Ackley_Two", "Egg_Holder", "Rana", "Pathological", "Michalewicz", "Masters_Cosine_Wave", "Quartic", "Levy", "Step", "Alpine"};
	clock_t start, elapsed;
	M_data M; M.rate = m_rate; M.range = m_range; M.precision = m_precision;
	GA_params ga_params; ga_params.ns = ns; ga_params.dim = dim; ga_params.t_max = num_gen;  ga_params.cr = cr; ga_params.er = er; ga_params.M = M;
	DE_params de_params; de_params.ns = ns; de_params.dim = dim; de_params.t_max = num_gen;  de_params.cr = cr; de_params.F = F; de_params.lambda = lambda;
	if (algo_id == 1)
	{
		for(int i = 0; i < num_exp; i++)
		{
			index = 0; //to know where the lowest and highest bound for each function is located in the vector
			
			for(int j = 0; j < num_functions; j++)
			{
				ga_params.l_b = ranges[index++]; ga_params.u_b = ranges[index++];

				start = clock();

				GA(ga_params, j , best_fitnesses->mat[count], num_trnmt, select_id, mt_rand);

				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				run_times->mat[count][0] = elapsed; 
			
				cout<<"\n" << functions_names[j] << "(dimension = " << dim << ", experiment #" << i + 1 << ")  best fitness = " << find_lowest(best_fitnesses->mat[count], num_gen) << ". time taken: " << elapsed << " ms." ; //print to standard output
				count++;
			}	
			cout << endl;
			count++;

		}
			cout<<"\n---------------------------------------------------------------End Genetic Algorithm-------------------------------------------------------------------------------\n";

		write_to_file(best_fitnesses, "../Results/GA/GA_best_fitness.csv");
		write_to_file(run_times, "../Results/GA/GA_run_times.csv");
	}

	else
	{
		for(int k = 0; k < 10; k++)
		{
cout<<"\n---------------------------------------------------------------Start strategy #" << k + 1 << "-------------------------------------------------------------------------------\n";
			count = 0;
			for(int i = 0; i < num_exp; i++)
			{
				index = 0; //to know where the lowest and highest bound for each function is located in the vector
				
				for(int j = 0; j < num_functions; j++)
				{
					de_params.l_b = ranges[index++]; de_params.u_b = ranges[index++];

					start = clock();

					DE(de_params, j , best_fitnesses->mat[count],  mt_rand, k);

					elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
					run_times->mat[count][0] = elapsed; 

					
					cout<<"\n" << functions_names[j] << "(dimension = " << dim << ", experiment #" << i << ")  best fitness = " << find_lowest(best_fitnesses->mat[count], num_gen) << ". time taken: " << elapsed << " ms." ; //print to standard output
						
					count++;
				}
				
				cout << endl;	
				count++;

			}
			cout<<"\n---------------------------------------------------------------End strategy #" << k + 1 << "-------------------------------------------------------------------------------\n";


			write_to_file(best_fitnesses, "../Results/DE/DE_best_fitness_" + to_string(k) + ".csv");
			write_to_file(run_times, "../Results/DE/DE_run_times_" + to_string(k) + ".csv");

		}
		cout<<"\n---------------------------------------------------------------End Differential Evolution Algorithm-------------------------------------------------------------------------------\n";
	}

	delete best_fitnesses;
	delete  run_times;
}

