/*********************************************
*                                            *
* utilities.cpp                              *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #4                                 *
* Last modified on Wednesday May 15, 2019    *
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
#include "PSO.h"
#include "FA.h"
#include "HS.h"

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


//get a number from the user for the swarm algorithm to be run
int utilities::get_algorithm_id()
{
	int algorithm_id;
	cout << "\nWhich algorithm would you like to run? \n" << "Enter \n1 for the Particle Swarm Optimization Algorithm \n2 for the Firefly Algorithm \n3 or any other number for the Harmony Search Algorithm: ";
	cin >> algorithm_id;
	if(cin.fail())
	{
		perror("Sorry invalid input. Please Try again and make sure to enter an integer");
		exit(-1);
	}

	if (algorithm_id == 1)
		cout<<"\n-----------------------------------------------------------Starting PSO Algorithm-------------------------------------------------------------------------\n";
	else if (algorithm_id == 2)
		cout<<"\n-------------------------------------------------Starting Firefly Algorithm Algorithm------------------------------------------------------------------------\n";
	else
		cout<<"\n-------------------------------------------------Starting Harmony Search Algorithm------------------------------------------------------------------------\n";

	return algorithm_id;

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
 * simulate the Particle Swarm Optimization, the Firefly algoritm and the Harmony Search algorithm
 *
 * @param dim : an integer for the dimension of the solutions
 * @param ns : an integer the number of solutions
 * @param num_functions : an integer for the number of objective functions to be simulated (the 18 functions)
 * @param ranges: an array of doubles containing the lower and upper bound for each of the objective functions
 * @param algo_id: an integer for the evolutionary algorithm to be simulated
 * @param num_iters : an integer for the number of iterations for the swarm algorithms
 * @param num_exp: an integer for the number of experimentations to be run
 * @param c1, c2: doubles
 * @param k: a double 
 * @param gamma: a double
 * @param B0: a double 
 * @param alpha: a double
 * @param HMCR: a double
 * @param PAR: a double 
 * @param bw: a double
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return :  None
 */
void utilities::simulate(int dim, int ns, int num_functions, double* ranges, int algo_id, int num_iters, int num_exp, double c1, double c2, double k, double gamma, double B0, double alpha, double HMCR, double PAR, double bw,  mt19937& mt_rand)
{

	matrix* best_fitnesses = new matrix(num_exp * num_functions + num_exp, num_iters + 1); //matrix that will hold all the best fitnesses accross all generations
	matrix* worst_fitnesses = new matrix(num_exp * num_functions + num_exp, num_iters + 1); //matrix that will hold all the best fitnesses accross all generations
        matrix* stagnation_iter = new matrix(num_exp, num_functions); //matrix that will hold the iteration at which the population stagnates for each of the objective function (column) and each experiment (rows)	
	int index = 0, count = 0, count_2 = 0; //index is used to find the correct bound, count is used to know where to insert results of one experimentation in best_fitnesses or run_times, count_2 is used to know where to insert the calculated standard deviations in std_devs
	matrix* run_times = new matrix(num_exp * num_functions + num_exp, 1); //To hold the run time for each function for each specific dimension
	matrix* std_devs = new matrix(num_exp * num_functions * num_iters + num_exp * num_functions + num_exp, dim);
	matrix* num_func_calls = new matrix(num_exp, num_functions);
	string functions_names[num_functions] = {"Schwefel", "first_De_Jong", "Rosenbrock", "Rastrigin", "Greiwangk", "Sine_Envelope_Sine_Wave", "Stretched_V_Sine_Wave", "Ackley_One", "Ackley_Two", "Egg_Holder", "Rana", "Pathological", "Michalewicz", "Masters_Cosine_Wave", "Quartic", "Levy", "Step", "Alpine"};
	clock_t start, elapsed;
	

	if (algo_id == 1)
	{

		PSO_Params P; P.c1 = c1, P.c2 = c2, P.k = k, P.np = ns, P.dim = dim, P.num_iters = num_iters, P.std_devs = std_devs;
		for(int i = 0; i < num_exp; i++)
		{
			index = 0; //to know where the lowest and highest bound for each function is located in the vector
			
			for(int j = 0; j < num_functions; j++)
			{
				P.l_b = ranges[index++]; P.u_b = ranges[index++];
				P.gBests = best_fitnesses->mat[count];
				P.std_devs_row = count_2; 
				P.func_id = j;

				start = clock();

				PSO(P, num_func_calls->mat[i], stagnation_iter->mat[i], mt_rand);
				count_2 += num_iters + 1;

				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				run_times->mat[count][0] = elapsed; 
			
				cout<<"\n" << functions_names[j] << "(dimension = " << dim << ", experiment #" << i + 1 << ")  best fitness = " << find_lowest(best_fitnesses->mat[count], num_iters) << ". time taken: " << elapsed << " ms." << "  Ranges: " << ranges[index - 2] << "  " << ranges[index - 1] << endl; //print to standard output
				count++;
			}
			count_2 += 1;	
			cout << endl;
			count++;

		}
			cout<<"\n---------------------------------------------------------------End PSO Algorithm-------------------------------------------------------------------------------\n";

		write_to_file(best_fitnesses, "../Results/PSO/PSO_best_fitness.csv");
		write_to_file(run_times, "../Results/PSO/PSO_run_times.csv");
		write_to_file(std_devs, "../Results/PSO/PSO_std_devs.csv");
		write_to_file(num_func_calls, "../Results/PSO/PSO_num_func_calls.csv");
		write_to_file(stagnation_iter, "../Results/PSO/PSO_stagnation_iters.csv");


	}

	else if(algo_id == 2)
	{

		FA_Params F; F.gamma = gamma, F.B0 = B0, F.alpha = alpha, F.nf = ns, F.dim = dim, F.num_iters = num_iters, F.std_devs = std_devs;
		for(int i = 0; i < num_exp; i++)
                {
                        index = 0; //to know where the lowest and highest bound for each function is located in the vector

                        for(int j = 0; j < num_functions; j++)
                        {
                                F.l_b = ranges[index++]; F.u_b = ranges[index++];
                                F.gBests = best_fitnesses->mat[count];
				F.std_devs_row = count_2;
                                F.func_id = j;

                                start = clock();

                                FA(F, num_func_calls->mat[i], stagnation_iter->mat[i], mt_rand);
				count_2 += num_iters + 1;

                                elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
                                run_times->mat[count][0] = elapsed;

                                cout<<"\n" << functions_names[j] << "(dimension = " << dim << ", experiment #" << i + 1 << ")  best fitness = " << find_lowest(best_fitnesses->mat[count], num_iters) << ". time taken: " << elapsed << " ms." << "  Ranges: " << ranges[index - 2] << "  " << ranges[index - 1] << endl; //print to standard output
                                count++;
                        }
			count_2 += 1;
                        cout << endl;
                        count++;

                }
                        cout<<"\n---------------------------------------------------------------End Firefly Algorithm-------------------------------------------------------------------------------\n";

                write_to_file(best_fitnesses, "../Results/FA/FA_best_fitness.csv");
                write_to_file(run_times, "../Results/FA/FA_run_times.csv");
		write_to_file(std_devs, "../Results/FA/FA_std_devs.csv");
		write_to_file(num_func_calls, "../Results/FA/FA_num_func_calls.csv");
		write_to_file(stagnation_iter, "../Results/FA/FA_stagnation_iters.csv");

	}
	else
	{

		HS_Params H; H.HMCR = HMCR, H.PAR = PAR, H.bw = bw, H.nh = ns, H.dim = dim, H.num_iters = num_iters, H.std_devs = std_devs;
		for(int i = 0; i < num_exp; i++)
                {
                        index = 0; //to know where the lowest and highest bound for each function is located in the vector

                        for(int j = 0; j < num_functions; j++)
                        {
                                H.l_b = ranges[index++]; H.u_b = ranges[index++];
                                H.gBests = best_fitnesses->mat[count];
				H.gWorsts = worst_fitnesses->mat[count];
				H.std_devs_row = count_2;
                                H.func_id = j;

                                start = clock();

                                HS(H, num_func_calls->mat[i], stagnation_iter->mat[i], mt_rand);
				count_2 += num_iters + 1;

                                elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
                                run_times->mat[count][0] = elapsed;

                                cout<<"\n" << functions_names[j] << "(dimension = " << dim << ", experiment #" << i + 1 << ")  best fitness = " << find_lowest(best_fitnesses->mat[count], num_iters) << ". time taken: " << elapsed << " ms." << "  Ranges: " << ranges[index - 2] << "  " << ranges[index - 1] << endl; //print to standard output
                                count++;
                        }
			count_2 += 1;
                        cout << endl;
                        count++;

                }
                        cout<<"\n---------------------------------------------------------------End Harmony Search Algorithm-------------------------------------------------------------------------------\n";

                write_to_file(best_fitnesses, "../Results/HS/HS_best_fitness.csv");
                write_to_file(run_times, "../Results/HS/HS_run_times.csv");
		write_to_file(std_devs, "../Results/HS/HS_std_devs.csv");
		write_to_file(num_func_calls, "../Results/HS/HS_num_func_calls.csv");
		write_to_file(worst_fitnesses, "../Results/HS/HS_worst_fitness.csv");
		write_to_file(stagnation_iter, "../Results/HS/HS_stagnation_iters.csv");
	}

	delete best_fitnesses;
	delete  run_times;
	delete num_func_calls;
	delete std_devs;
	delete worst_fitnesses;
}

