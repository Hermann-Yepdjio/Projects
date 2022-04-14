/*********************************************
*                                            *
* search_functions.cpp                       *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #2                                 *
* Last modified on Wednesday April 17, 2019  *
*                                            *
*********************************************/



#include <iostream>
#include "matrix.h"
#include "functions.h"
#include <float.h>
#include <cstring>
#include "search_functions.h"


using namespace std;


double (functions::*functions_ptr[])(double*, int) = {&functions::Schwefel, &functions::first_De_Jong, &functions::Rosenbrock, &functions::Rastrigin, &functions::Greiwangk, &functions::Sine_Envelope_Sine_Wave, &functions::Stretched_V_Sine_Wave, &functions::Ackley_One, &functions::Ackley_Two, &functions::Egg_Holder, &functions::Rana, &functions::Pathological, &functions::Michalewicz, &functions::Masters_Cosine_Wave, &functions::Quartic, &functions::Levy, &functions::Step, &functions::Alpine}; //create an array containing pointers to all the 18 functions (makes code shorter, otherwise each function has to be called manually one a the time. Compare with project1 to see the difference)





/*
 * generate a neighborhood of solution spaces for the initial solution
 *
 * @param input: struct containing the best solution so far
 * @param neighbor: the neighbor of the best solution ( To be found)
 * @param function_id: the id of the function to run in order to get the fitness ( one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 * @param delta: the scaling factor 
 *
 * @return : no return
 *
 */
int  generate_neighbor(rw_input* input, double* neighbor, int function_id, int dimension, double l_b, double r_b, double delta)
{

	functions *func = new functions(); //needed to make accessible the functions in functions_ptr

	double* tmp_solution = new double[dimension]();//my Y vector
	double ts_fitness, ns_element;
	int count = 0;
	for(int i = 0; i < dimension; i++)
	{
               	if(input->best_solution[i] + delta >= l_b && input->best_solution[i] + delta <= r_b)
		{
			memcpy(tmp_solution, input->best_solution, sizeof(double) * dimension);
			tmp_solution[i] = tmp_solution[i] + delta;
               	 	ts_fitness = (func->*functions_ptr[function_id])( tmp_solution, dimension); //compute the fitness for tmp_solution
			ns_element = input->best_solution[i] - delta * (ts_fitness - input->best_fitness); //Compute an entry for the Z vector
		       	if(ns_element < l_b || ns_element > r_b) //check if ns_element is within the range
			{

				if(count >= 4 && ns_element < l_b)
					ns_element = l_b;
				else if (count >= 4 && ns_element > r_b)
					ns_element = r_b;
				else
				{

        				delete[] tmp_solution;
        				delete func;
					return 1;
				}

			}
			neighbor[i] = ns_element;
			count++;
		}
		else 
		{
			
        		delete[] tmp_solution;
        		delete func;
			return 1;
		}
	}


	delete[] tmp_solution;
	delete func;
	return 0;

}

/*
 * simulate the blind algorithm (Random Walk)
 *
 * @param input: struct containing the number of iterations, best solution found so far and its fitness
 * @param function_id: the id of the function to used to compute the fitness(one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 *
 * @return : no return
 *
 */
void random_walk(rw_input *input, int function_id, int dimension, double l_b, double r_b, mt19937& mt_rand)
{
	
	functions *func = new functions(); //needed to make accessible the functions in functions_ptr

	matrix* Matrix = new matrix(input->iterations, dimension, l_b, r_b, mt_rand);

	input->best_fitness = DBL_MAX;
	double fitness;
	

	for(int i = 0; i < input->iterations; i++)
	{

		fitness = (func->*functions_ptr[function_id])( Matrix->mat[i], dimension); //compute the fitness for one element in the sample
		if (fitness < input->best_fitness)
		{
			
			input->best_fitness = fitness; //copy the best fitness inside input
			memcpy(input->best_solution, Matrix->mat[i], sizeof(double) * dimension);
		}
	}

	delete Matrix;
	delete func;
}

/*
 * simulate the local search algorithm
 *
 * @param input: struct containing the initial solution, the best sollocal_search(rw_input *input, int function_id, int dimension, double l_b, double r_b, double delta)
ution found so far and its fitness
 * @param function_id: the id of the function to used to compute the fitness(one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 * @param delta: a double for the  scaling factor
 *
 * @return : no return
 *
 */
void local_search(rw_input *input, int function_id, int dimension, double l_b, double r_b, double delta, mt19937& mt_rand)
{

	functions *func = new functions(); //needed to make accessible the functions in functions_ptr 
	
	random_walk(input, function_id, dimension, l_b, r_b, mt_rand);

	double *neighbor = new double[dimension]();
	double neighbor_fitness;
	bool t = true;

	while(generate_neighbor(input, neighbor, function_id, dimension, l_b, r_b, delta))//repeat until the neighbor generated has all elements within the range
	{
		
		random_walk(input,  function_id, dimension, l_b, r_b, mt_rand);
	}
	
	while (t)
	{
		t = false;
		


		

		neighbor_fitness = (func->*functions_ptr[function_id])( neighbor, dimension); //compute the fitness for one element in the sample

		if (neighbor_fitness < input->best_fitness)
		{

			input->best_fitness = neighbor_fitness; //save the best fitness inside input
			memcpy(input->best_solution, neighbor, sizeof(double) * dimension); //save the best solution into input
			if(!generate_neighbor(input, neighbor, function_id, dimension, l_b, r_b, delta)) //if generate returns 0 meaning that the generated neighbor is valid then loop again and keep looking for a better solution.  Else consider the most recent best solution and exit the loop 
				t = true;
		}
	

		
	}
	delete[] neighbor;
	delete func;
}

/*
 * simulate the iterative local search algorithm
 * @param input: struct containing the number of iterations, the best solution found so far and its fitness
 * @param function_id: the id of the function to used to compute the fitness(one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 * @param delta: a double for the  scaling factor
 *
 * @return : no return
 *
 */
void iter_local_search(rw_input *input, int function_id, int dimension, double l_b, double r_b, double delta, mt19937& mt_rand)
{
	local_search(input, function_id, dimension, l_b, r_b, delta, mt_rand);

	
}




