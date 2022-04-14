/******************************************
*                                         *
* PSO.cpp                                 *
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
#include "PSO.h"
using namespace std;

/**
 *create an array containing pointers to all the 18 functions (makes code shorter, otherwise each function has to be called manually one a the time. Compare with project1 to see the difference)
*/
double (functions::*functions_ptr_1[])(double*, int) = {&functions::Schwefel, &functions::first_De_Jong, &functions::Rosenbrock, &functions::Rastrigin, &functions::Greiwangk, &functions::Sine_Envelope_Sine_Wave, &functions::Stretched_V_Sine_Wave, &functions::Ackley_One, &functions::Ackley_Two, &functions::Egg_Holder, &functions::Rana, &functions::Pathological, &functions::Michalewicz, &functions::Masters_Cosine_Wave, &functions::Quartic, &functions::Levy, &functions::Step, &functions::Alpine}; 

functions *func = new functions(); //needed to make accessible the functions in functions_ptr

//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
double random(double l_b, double u_b, mt19937& mt_rand)
{
	uniform_real_distribution<double> rand_num(l_b, u_b); //filters the mt_rand output to generate pseudo-random double values (uniformly distributed within the interval [l_b, h_b]
        return  rand_num(mt_rand);
}

/**
 * compute the fitnesses for all the particles in a population
 *
 * @param matrix: the matrix containing the particles which fitnesses need to be computed
 * @param fitnesses: a vector that will hold the fitness for each particle
 * @param func_id: the index for the function to be used to compute the fitness
 * @param num_func_calls: a vector of doubles to keep track of how many times each objective function is called
 *
 * @return :None
 */
void compute_fitnesses(matrix *swarm, double *fitnesses, int func_id, double *num_func_calls)
{
	for(int i = 0; i < swarm->num_rows; i++)
	{
		fitnesses[i] = (func->*functions_ptr_1[func_id])( swarm->mat[i], swarm->num_columns); //compute the fitness for a row in population and save in fitnesses
		num_func_calls[func_id] += 1;
	}

}

/**
 * compute the fitness of a particle
 *
 * @parmam particle: a vector of doubles for the particle to be evaluated
 * @param func_id: an integer for the index of the objective function to be used 
 * @param dim: an integer for the dimensionnality of the particle(the number of elements)
 * @param num_func_calls: a vector of doubles to keep track of how many times each objective function is called
 *
 * @return : a double which is the fitness of the particle provided as input 
 */
double compute_fitness(double *particle, int func_id, int dim, double *num_func_calls)
{
	num_func_calls[func_id] += 1;
	return (func->*functions_ptr_1[func_id])( particle, dim);
}

/**
 * sets the personnal best fitness for each particle
 *
 * @param fitnesses: a vector containing the fitnesses for each particle
 * @param pBest: a vector to hold the personnal best fitnesses for each particle
 * @param int num_particles: an int for number of particles)
 *
 * @return :None
 */
void set_pBests(double* fitnesses, double* pBest, int num_particles)
{
	for(int i = 0; i < num_particles; i++)
		pBest[i] = fitnesses[i];
}


/**
 * finds the lowest fitness in PBests (personnal bests)
 *
 * @param pBest: a vector containing the personnal best fitnesses for each particle
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the lowest fitness found
 */
int get_gBest(double* pBests, int num_particles)
{

	double l_fit = pBests[0];
	int index = 0;
	for (int i = 1; i < num_particles; i++)
	{
		if(pBests[i] < l_fit)
		{
			l_fit = pBests[i];
			index = i;
		}
	}
	return index;
}

/**
 * compute the new velocity of a particle
 *
 * @param p: a vector of dimension(length) dim  representing a particle 
 * @param v: a vector of dimension dim  containing the current velocity for each component of particle p
 * @param dim: the number of component of a particle
 * @params c1, c2: doubles
 * @param pBest: a double for the personnal best fitness so far for  particle p
 * @param gBest: a double for the global best fitness
 * @param k: a double
 * @param func_id: the id for the objective function (needed to decided which value of k to use
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void get_p_velocity(double *p, double* v, int dim, double c1, double c2, double pBest, double gBest, double k, int func_id, mt19937& mt_rand)
{
	double k_2 = k;
	if(func_id == 2 || func_id == 3 || func_id == 14)
		k_2 = 0.00001;

	double rand_1 = random(0, 1, mt_rand);
	for(int i = 0; i < dim; i++)
	{

		v[i] = (v[i] + c1 * rand_1 * (pBest - p[i]) + c2 * rand_1 * (gBest - p[i])) * k_2 ;  
	}
}

/**
 * update the components of a  particle
 * 
 * @param p: a vector of dimension(length) dim  representing a particle 
 * @param v: a vector of dimension dim  containing the current velocity for each component of particle p
 * @param l_b, u_b: the lower and upper bounds for the components of the particle p
 * @param dim: the number of component of a particle
 *
 * @return : None
 */
void update_particle(double *p, double *v, double l_b, double u_b,  int dim)
{
	for (int i = 0; i < dim; i++)
	{
		p[i] = p[i] + v[i];

		if(p[i] < l_b) //bring p[i] within the bound if it is not
		{
			p[i] = u_b;
		}
		else if( p[i] > u_b)
			p[i] = l_b;
	}
}


/**
 * extract a column from a matrixcompute_std_devs(matrix *swarm, matrix *std_devs, int index)
 *
 * @param swarm: a matrix where the column will be extracted from
 * @param column: a vector of double to hold the extracted column
 * @param col_index: an intger for the index of the column to be extracted
 *
 * @return : None
 */
void get_column(matrix* swarm, double* column, int col_index)
{
	for(int i = 0; i < swarm->num_rows; i++)
	{
		column[i] = swarm->mat[i][col_index];
	}
}

/**
 * compute the mean of elements in a vector
 *
 * @param vect: a vector of doubles containing the numbers to be averaged
 * @param size: an integer for the length of the vector vect
 *
 * @return : the mean of the numbers in vector vect
 */
double average(double* vect, int size)
{
	double tmp = 0;
	for (int i = 0; i < size; i++)
		tmp += vect[i];
	return tmp/size;
}

/**
 * compute the standard deviation of elements in a vector
 *
 * @param vect: a vector of doubles containing the numbers which standard deviation needs to be calculated
 * @param size: an integer for the length of the vector vect
 *
 * @return : the standard deviation of the numbers in vector vect
 */
double std_dev(double* vect, int size)
{
	double tmp = 0;
	double mean = average(vect, size);
	for(int i = 0; i < size; i++)
		tmp += pow(vect[i] - mean, 2);

	return sqrt(tmp/(size - 1));
}

/**
 * compute the standart deviation for each column in a matrix
 *
 * @param swarm: the matrix which columns will be used to compute the standard deviation
 * @param std_devs: a matrix where the computed standard deviation will be stored
 * @param index: the index of the row in the matrix where the computed standard deviations will be stored
 *
 * @return : None
 */
void compute_std_devs(matrix *swarm, matrix *std_devs, int index)
{
	double *column = new double[swarm->num_rows];
	for(int i = 0; i < swarm->num_columns; i++)
	{
		get_column(swarm, column, i);
		std_devs->mat[index][i] = std_dev(column, swarm->num_rows);
	}

	delete column;
}

/**
 * checks if a given population has stagnated
 *
 * @param std_devs: a matrix containing the standard deviations of each dimension after each iteration
 * @param index: an integer for the index of the current last row in the matrix
 * @param iter: an integer for the current iteration number
 * @param percentage: a double for the percentage of number of dimensions that need to be stable in order to decide if the population has stagnated or not
 *
 * @return : boolean saying wheter the population has stagnated or not
 */
bool has_pop_stagnated(matrix *std_devs, int index, int iter, double percentage)
{
	int count = 0;

	if (iter != 0)
	{
		for (int i = 0; i < std_devs->num_columns; i++)
		{
			if (std_devs->mat[index][i] == std_devs->mat[index - 1][i])
				count++;
		}
	}

	if (count >= std_devs->num_columns * percentage)
		return true;
	return false;
}

/**
 * simulate the Particle Swarm Optimization algorithm
 *
 * @param params: a struct containing the inputs for the PSO algorithm
 * @params params.c1, params.c2: doubles
 * @param params.k : a double
 * @param params.np: an integer for the number of particles in the swarm
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
void PSO(PSO_Params params, double *num_func_calls,  double *stagnation_iter, mt19937& mt_rand )
{
	matrix *swarm = new matrix(params.np, params.dim, params.l_b, params.u_b, mt_rand); //initial swarm
	matrix *velocities = new matrix(params.np, params.dim, 0, 0.5 * (params.u_b - params.l_b) , mt_rand); //initial velocities
	double *fitnesses = new double[params.np];
	double *pBests = new double[params.np];
	compute_fitnesses(swarm, fitnesses, params.func_id, num_func_calls);
	set_pBests(fitnesses, pBests, params.np);


	for(int i = 0; i < params.num_iters; i++)
	{

		params.gBests[i] = pBests[get_gBest(pBests, params.np)];

		for(int j = 0; j < params.np; j++)
		{
			get_p_velocity(swarm->mat[j], velocities->mat[j], params.dim, params.c1, params.c2, pBests[j], params.gBests[i], params.k, params.func_id,  mt_rand);
			update_particle(swarm->mat[j], velocities->mat[j], params.l_b, params.u_b, params.dim);
			fitnesses[j] = (func->*functions_ptr_1[params.func_id])( swarm->mat[j], swarm->num_columns);
			num_func_calls[params.func_id] += 1;
			if (fitnesses[j] < pBests[j] && fitnesses[j] > 0)
				pBests[j] = fitnesses[j];


		}

		compute_std_devs(swarm, params.std_devs, params.std_devs_row + i);
		if(has_pop_stagnated(params.std_devs, params.std_devs_row + i, i, 0.5))
			stagnation_iter[params.func_id] = i + 1;

	}
	params.gBests[params.num_iters] = pBests[get_gBest(pBests, params.np)];

	delete swarm;
	delete velocities;
	delete[] fitnesses;
	delete[] pBests;


}

