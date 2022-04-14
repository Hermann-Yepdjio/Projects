/******************************************
*                                         *
* FA.cpp                                  *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #4                              *
* Last modified on Tuesday May 14, 2019   *
*                                         *
******************************************/

//#include <random>
#include <stdio.h>
#include <iostream>
#include "functions.h"
#include <string.h>
#include <stdlib.h>
#include "matrix.h"
#include <cmath>
#include "FA.h"
#include "PSO.h"
using namespace std;

/**
 *create an array containing pointers to all the 18 functions (makes code shorter, otherwise each function has to be called manually one a the time. Compare with project1 to see the difference)
*/
//double (functions::*functions_ptr_2[])(double*, int) = {&functions::Schwefel, &functions::first_De_Jong, &functions::Rosenbrock, &functions::Rastrigin, &functions::Greiwangk, &functions::Sine_Envelope_Sine_Wave, &functions::Stretched_V_Sine_Wave, &functions::Ackley_One, &functions::Ackley_Two, &functions::Egg_Holder, &functions::Rana, &functions::Pathological, &functions::Michalewicz, &functions::Masters_Cosine_Wave, &functions::Quartic, &functions::Levy, &functions::Step, &functions::Alpine}; 

//functions *func = new functions(); //needed to make accessible the functions in functions_ptr

//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
/*double random(double l_b, double u_b, mt19937& mt_rand)
{
	uniform_real_distribution<double> rand_num(l_b, u_b); //filters the mt_rand output to generate pseudo-random double values (uniformly distributed within the interval [l_b, h_b]
        return  rand_num(mt_rand);
}*/

/**
 * compute the fitnesses for all the particles in a population
 *
 * @param matrix: the matrix containing the particles which fitnesses need to be computed
 * @param fitnesses: a vector that will hold the fitness for each particle
 * @param func_id: the index for the function to be used to compute the fitness
 *
 * @return :None
 */
/*void compute_fitnesses(matrix *swarm, double* fitnesses, int func_id)
{
	for(int i = 0; i < swarm->num_rows; i++)
	{
		fitnesses[i] = (func->*functions_ptr_2[func_id])( swarm->mat[i], swarm->num_columns); //compute the fitness for a row in population and save in fitnesses
	}
	
}*/

/**
 * finds the lowest fitness in fitnesses
 *
 * @param fitnesses: a vector containing the fitnesses for each firefly
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the lowest fitness found
 */
/*int get_gBest(double* fitnesses, int num_fireflies)
{

        double l_fit = fitnesses[0];
        int index = 0;
        for (int i = 1; i < num_fireflies; i++)
        {
                if(fitnesses[i] < l_fit)
                {
                        l_fit = fitnesses[i];
                        index = i;
                }
        }
        return index;
}*/

/**
 *compute the attractiveness of a firefly
 *  
 * @param r: a double for the distance between two fireflies
 * @param B0: a double for the attractiveness when r = 0
 * @param gamma: a double constant
 *
 * @return : a double for the attractiveness of a firefly
 */
double eq_2(double r, double B0, double gamma)
{
	return B0 * exp( -1 * gamma * pow(r, 2));
}


/**
 * compute the distance between two fireflies
 *
 * @params firefly_1, firefly_2: vectors of doubles for the two fireflies
 * @param dim: an intege for the dimension of each firefliy vector
 *
 * @return :a double for the distance betweem the two fireflies
 */
double eq_3(const double *firefly_1, const double *firefly_2, const int dim)
{
	double sum = 0;
	for(int i = 0; i < dim; i++)
		sum += pow(firefly_2[i] - firefly_1[i], 2);
	return pow(sum, 0.5);
}



/**
 * compute the movement of an attraction between 2 fireflies
 *
 * @params firefly_1, firefly_2: vectors of doubles for the two fireflies
 * @param B0: a double for the attractiveness when r = 0
 * @param gamma: a double constant
 * @param r: a double for the distance between two fireflies
 * @param alpha: a double
 * @param dim: an intege for the dimension of each firefliy vector
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None 
 */
void eq_4(double *firefly_1, const double *firefly_2, double beta, double gamma, double r, double alpha, int dim, double l_b, double u_b, mt19937& mt_rand)
{
	for(int i = 0; i < dim; i++)
	{
		firefly_1[i] = firefly_1[i] + beta * (firefly_2[i] - firefly_1[i]) + alpha * random(0, 1, mt_rand);// - 0.5 * (u_b - l_b));  
		if(firefly_1[i] < l_b)
			firefly_1[i] = u_b;
		else if(firefly_1[i] > u_b)
			firefly_1[i] = l_b;
	}
}

/**
 * copy values of swarm_2 into swarm_1
 *
 * @params swarm_1, swarm_2: matrices where each row represents a firefly
 * @params num_fireflies, dim: integers for the number of rows and the number of columns in the swarms
 *
 * @return : None
 */
void copy_swarm(matrix *swarm_1, matrix *swarm_2, int num_fireflies, int dim)
{
	for( int i = 0; i < num_fireflies; i++)
		memcpy(swarm_1->mat[i], swarm_2->mat[i], sizeof(double) * dim);
}

/**
 * simulate the Particle Swarm Optimization algorithm
 *
 * @param params: a struct containing the inputs for the PSO algorithm
 * @param params.gamma: a double
 * @param params.B0: a double
 * @param params.alpha: a double
 * @param params.nf: an integer for the number of fireflies in the swarm
 * @param params.dim: an integer for the dimension of the swarm
 * @param params.num_iters: an integer for the number of iterations
 * @param params.std_devs: a matrix to hold the standard deviations of each column of the swarm after each iteration
 * @param params.gBests: a vector of doubles to hold the best global fitnesses after each iterations
 * @params params.l_b, params.u_b: doubles for the lower and upper bounds for the elements of a particle
 * @param params.func_id: an integer for the index of the objective function to be used to compute the fitnesses
 * @param num_func_calls: a vector to hold the number of objective functions (how many times each of the 18 functions is called)
 * @param stagnation_iter: a vector of double to hold the iterations at which the population stagnates
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void FA(FA_Params params, double* num_func_calls, double *stagnation_iter,  mt19937& mt_rand)
{
	matrix *swarm = new matrix(params.nf, params.dim, params.l_b, params.u_b, mt_rand); //initial swarm
	matrix *new_swarm = new matrix(params.nf, params.dim); //new swarm for the next generation
	double *tmp_firefly = new double[params.dim];
	double *fitnesses = new double[params.nf]();
	double *new_fitnesses = new double[params.nf]();
	double r, beta, tmp_f_fit;
	compute_fitnesses(swarm, fitnesses, params.func_id, num_func_calls);

	for(int i = 0; i < params.num_iters; i++)
	{
		for(int j = 0; j < params.nf; j++)
		{
			memcpy(tmp_firefly, swarm->mat[j], sizeof(double) * params.dim);
			new_fitnesses[j] = fitnesses[j];
			for( int k = 0; k < params.nf; k++)
			{	
				if(fitnesses[k] < fitnesses[j])
				{
					r = eq_3(tmp_firefly, swarm->mat[k], params.dim);
					beta = eq_2(r, params.B0, params.gamma);
					eq_4(tmp_firefly, swarm->mat[k], beta, params.gamma, r, params.alpha, params.dim, params.l_b, params.u_b, mt_rand);
					tmp_f_fit = compute_fitness(tmp_firefly, params.func_id, params.dim, num_func_calls);
					if((tmp_f_fit < new_fitnesses[j] and tmp_f_fit > 0) || (tmp_f_fit > new_fitnesses[j] and new_fitnesses[j] < 0))
					{
						memcpy(new_swarm->mat[j], tmp_firefly, sizeof(double) * params.dim);
						new_fitnesses[j] = tmp_f_fit;
						break;
					}
					memcpy(tmp_firefly, swarm->mat[j], sizeof(double) * params.dim);

				}
			}



		}
		params.gBests[i] = fitnesses[get_gBest(fitnesses, params.nf)];
		copy_swarm(swarm, new_swarm, swarm->num_rows, swarm->num_columns);
		memcpy(fitnesses, new_fitnesses, sizeof(double) * params.nf);

		compute_std_devs(swarm, params.std_devs, params.std_devs_row + i);
		if(has_pop_stagnated(params.std_devs, params.std_devs_row + i, i, 0.5))
                        stagnation_iter[params.func_id] = i + 1;
		
	}
	params.gBests[params.num_iters] = fitnesses[get_gBest(fitnesses, params.nf)];

	delete swarm;
	delete new_swarm;
	delete[] tmp_firefly;
	delete[] fitnesses;


}

