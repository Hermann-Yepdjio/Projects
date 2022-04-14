/******************************************
*                                         *
* HS.cpp                                  *
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
#include "HS.h"
#include "PSO.h"
using namespace std;

/**
 *create an array containing pointers to all the 18 functions (makes code shorter, otherwise each function has to be called manually one a the time. Compare with project1 to see the difference)
*/
//double (functions::*functions_ptr_3[])(double*, int) = {&functions::Schwefel, &functions::first_De_Jong, &functions::Rosenbrock, &functions::Rastrigin, &functions::Greiwangk, &functions::Sine_Envelope_Sine_Wave, &functions::Stretched_V_Sine_Wave, &functions::Ackley_One, &functions::Ackley_Two, &functions::Egg_Holder, &functions::Rana, &functions::Pathological, &functions::Michalewicz, &functions::Masters_Cosine_Wave, &functions::Quartic, &functions::Levy, &functions::Step, &functions::Alpine}; 

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
 * @param population: the matrix containing the harmonies which fitnesses need to be computed
 * @param fitnesses: a vector that will hold the fitness for each particle
 * @param func_id: the index for the function to be used to compute the fitness
 *
 * @return :None
 */
/*void compute_fitnesses(matrix *population, double* fitnesses, int func_id)
{
	for(int i = 0; i < population->num_rows; i++)
	{
		fitnesses[i] = (func->*functions_ptr_3[func_id])( population->mat[i], population->num_columns); //compute the fitness for a row in population and save in fitnesses
	}
	
}*/


/**
 * finds the lowest fitness in PBests (personnal bests)
 *
 * @param pBest: a vector containing the personnal best fitnesses for each particle
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the lowest fitness found
 */
/*int get_gBest(double* fitnesses, int num_harmonies)
{

	double l_fit = fitnesses[0];
	int index = 0;
	for (int i = 1; i < num_harmonies; i++)
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
 * finds the highest fitness in fitnesses
 *
 * @param fitnesses: a vector containing the fitnesses for each harmony
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the highest fitness found
 */
int get_gWorst(double *fitnesses, int num_harmonies)
{

	double h_fit = fitnesses[0];
	int index = 0;
	for (int i = 1; i < num_harmonies; i++)
	{
		if(fitnesses[i] > h_fit)
		{
			h_fit = fitnesses[i];
			index = i;
		}
	}
	return index;
}

/**
 * adjust the values of old_h to create a new harmony new_h
 *
 * @params old_h, new_h: vectors of doubles for the old harmony and the new harmony
 * @param dim: an integer for the dimension of the harmnies
 * @param bw: a double
 * @params l_b, u_b: doubles for the lowest and upper bounds of an harmony's element
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void adjust_harmony(const double *old_h, double *new_h, int dim, double bw, double l_b, double u_b, mt19937& mt_rand)
{
	for(int i = 0; i < dim; i++)
	{
		new_h[i] = old_h[i] + random(0, 1, mt_rand) * bw;
		if(new_h[i] < l_b)
			new_h[i] = l_b;
		else if (new_h[i] > u_b)
			new_h[i] = u_b;
	}
}

/**
 * create a new harmony new_h using random values
 *
 * @param new_h: vector of doubles to hold the new harmony
 * @param dim: an integer for the dimension of the harmnies
 * @params l_b, u_b: doubles for the lowest and upper bounds of an harmony's element
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void create_random_harmony(double *new_h, int dim, double l_b, double u_b, mt19937& mt_rand)
{
	for(int i = 0; i < dim; i++)
        {
                new_h[i] = random(0, 1, mt_rand);
                /*if(new_h[i] < l_b)
                        new_h[i] = u_b;
                else if (new_h[i] > u_b)
                        new_h[i] = l_b;*/
        }

}

/**
 * copy values of pop_2 into pop_1
 *
 * @params pop_1, pop_2: matrices where each row represents am harmony
 * @params num_harmonies, dim: integers for the number of rows and the number of columns in the populations
 *
 * @return : None
 */
void copy_population(matrix *pop_1, matrix *pop_2, int num_harmonies, int dim)
{
        for( int i = 0; i < num_harmonies; i++)
                memcpy(pop_1->mat[i], pop_2->mat[i], sizeof(double) * dim);
}

/**
 * simulate the Particle Swarm Optimization algorithm
 *
 * @param params: a struct containing the inputs for the PSO algorithm
 * @param params.HMCR: a double for the Harmony Size Considering Rate
 * @param params.PAR: a double for the Pitch Ajusting Rate
 * @param params.bw: a double for the bandwidth
 * @param params.nh: an integer for the number of harmomies in the solution space
 * @param params.dim: an integer for the dimension of the solution space
 * @param params.num_iters: an integer for the number of iterations
 * @param params.std_devs: a matrix to hold the standard deviations of each column of the swarm after each iteration
 * @param params.gBests: a vector of doubles to hold the best global fitnesses after each iterations
 * @param params.gWorsts: a vector of doubles to hold the worst global fitnesses after each iterations
 * @params params.l_b, params.u_b: doubles for the lower and upper bounds for the elements of a particle
 * @param params.func_id: an integer for the index of the objective function to be used to compute the fitnesses
 * @param num_func_calls: a vector to hold the number of objective functions (how many times each of the 18 functions is called)
 * @param stagnation_iter: a vector of double to hold the iterations at which the population stagnates
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void HS(HS_Params params,  double* num_func_calls, double *stagnation_iter, mt19937& mt_rand )
{
	matrix *pop = new matrix(params.nh, params.dim, params.l_b, params.u_b, mt_rand); //initial population
	matrix *new_pop = new matrix(params.nh, params.dim); //new population
	copy_population(new_pop, pop, params.nh, params.dim);
	double *fitnesses = new double[params.nh];
	double *new_fitnesses = new double[params.nh];
	double *tmp_h = new double[params.dim];
	double tmp_h_fit;
	int tmp_gWorst, tmp_newgBest;
	compute_fitnesses(pop, fitnesses, params.func_id, num_func_calls);
	for(int i = 0; i < params.num_iters; i++)
	{

		params.gBests[i] = fitnesses[get_gBest(fitnesses, params.nh)];
		params.gWorsts[i] = fitnesses[get_gWorst(fitnesses, params.nh)];

		for(int j = 0; j < params.nh; j++)
		{
			new_fitnesses[j] = fitnesses[j];
			memcpy(new_pop->mat[j], pop->mat[j], sizeof(double) * params.dim);
			if(random(0, 1, mt_rand) <= params.HMCR)
			{

				if(random(0, 1, mt_rand) <= params.PAR)
					adjust_harmony(pop->mat[j], tmp_h, params.dim, params.bw, params.l_b, params.u_b, mt_rand);
				tmp_h_fit = compute_fitness(tmp_h, params.func_id, params.dim, num_func_calls);
				if((tmp_h_fit < fitnesses[j] && tmp_h_fit > 0) || (tmp_h_fit > fitnesses[j] && tmp_h_fit < 0))
				{
					memcpy(new_pop->mat[j], tmp_h, sizeof(double) * params.dim);
					new_fitnesses[j] = tmp_h_fit;
				}

			}
			else
			{
				create_random_harmony(tmp_h, params.dim, params.l_b, params.u_b, mt_rand);
				tmp_h_fit = compute_fitness(tmp_h, params.func_id, params.dim, num_func_calls);
                                if((tmp_h_fit < fitnesses[j] && tmp_h_fit > 0) || (tmp_h_fit > fitnesses[j] && tmp_h_fit < 0))
                                {
                                        memcpy(new_pop->mat[j], tmp_h, sizeof(double) * params.dim);
                                        new_fitnesses[j] = tmp_h_fit;
                                }
			}

		}

		//compute_fitnesses(new_pop, new_fitnesses, params.func_id, num_func_calls);
		tmp_gWorst = get_gWorst(fitnesses, params.nh);
		tmp_newgBest = get_gBest(new_fitnesses, params.nh);
		memcpy(pop->mat[tmp_gWorst], new_pop->mat[tmp_newgBest], sizeof(double) * params.dim);
		fitnesses[tmp_gWorst] = new_fitnesses[tmp_newgBest];

		compute_std_devs(pop, params.std_devs, params.std_devs_row + i);
		if(has_pop_stagnated(params.std_devs, params.std_devs_row + i, i, 0.5))
                        stagnation_iter[params.func_id] = i + 1;
	}
	params.gBests[params.num_iters] = fitnesses[get_gBest(fitnesses, params.nh)];
	params.gWorsts[params.num_iters] = fitnesses[get_gWorst(fitnesses, params.nh)];

	delete pop;
	delete new_pop;
	delete[] fitnesses;
	delete[] new_fitnesses;


}
