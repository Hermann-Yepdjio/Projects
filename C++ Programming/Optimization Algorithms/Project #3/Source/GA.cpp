/******************************************
*                                         *
* GA.cpp                                  *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #3                              *
* Last modified on Saturday April 27, 2019*
*                                         *
******************************************/

//#include <random>
#include <stdio.h>
#include <iostream>
#include "functions.h"
#include <string.h>
#include <stdlib.h>
#include "matrix.h"
#include "GA.h"
using namespace std;

double (functions::*functions_ptr_1[])(double*, int) = {&functions::Schwefel, &functions::first_De_Jong, &functions::Rosenbrock, &functions::Rastrigin, &functions::Greiwangk, &functions::Sine_Envelope_Sine_Wave, &functions::Stretched_V_Sine_Wave, &functions::Ackley_One, &functions::Ackley_Two, &functions::Egg_Holder, &functions::Rana, &functions::Pathological, &functions::Michalewicz, &functions::Masters_Cosine_Wave, &functions::Quartic, &functions::Levy, &functions::Step, &functions::Alpine}; //create an array containing pointers to all the 18 functions (makes code shorter, otherwise each function has to be called manually one a the time. Compare with project1 to see the difference)


//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
double random(double l_b, double u_b, mt19937& mt_rand)
{
	uniform_real_distribution<double> rand_num(l_b, u_b); //filters the mt_rand output to generate pseudo-random double values (uniformly distributed within the interval [l_b, h_b]
         return  rand_num(mt_rand);
}

/**
 * select a parent using the roulette wheel selection approach
 *
 * @param fitnesses: a pointer to an array of doubles which contains all fitness for the population
 * @param pop_fitness: a double for the sum of all fitnesses in population
 * @param len: an integer for size of the population or len of the fitnesses array
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return i: an int representing the index of the chosen parent
 */
int roulette_wheel(double* fitnesses, double pop_fitness, int len, mt19937& mt_rand)
{
	//double rand_num = random(0, pop_fitness, mt_rand);
	double rand_num = ((double)rand() / (double)RAND_MAX) * (pop_fitness); //generate a random number between 0 and pop_fitness
	//cout << pop_fitness << "  " << rand_num << endl;
	for (int i = 0; i < len; i++)
	{
		rand_num -= fitnesses[i];
		if (rand_num <= 0)
			return i;
	}
	return len;
}

/**
 * select a parent using the tournament selection approach
 *
 * @param fitnesses: a pointer to an array of doubles which contains all fitness for the population
 * @param num_generations: an in for the number of tournaments 
 * @param len: an integer for size of the population or len of the fitnesses array
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return i: an int representing the index of the chosen parent
 */
int tournament_selection(double* fitnesses, double num_tournaments, int len, mt19937& mt_rand)
{
	int best = -1;
	for (int i = 0; i < num_tournaments; i++)
	{
		int rand_num = (int)random(0, len, mt_rand); // rand() % len; //generate random number between 0 and len
		if (best == -1 || fitnesses[best] < fitnesses[rand_num])
			best = rand_num;
	}

	return best;

}

/**
 * adjusts the fitnesses so they can be used in the selection algorithms
 *
 * @param fitnesses: a pointer to an array of doubles which contains all fitness for the population
 * @param adj_fitnesses: a pointer to an empty array of double to hold the adjusted fitnesses
 * @param len: an integer for size of the population or len of the fitnesses array
 *
 * @return : None
 */
void normalize_fitnesses(double* fitnesses, double* norm_fitnesses, int len)
{
	
	for (int i = 0; i < len; i++)
	{
		if (fitnesses[i] >= 0)
			norm_fitnesses[i] = 1 / (1 + fitnesses[i]);
		else
			norm_fitnesses[i] = 1 + abs(fitnesses[i]);
	}
}

/**
 * compute the sum of all fitnesses in the population
 *
 * @param fitnesses: a pointer to an array of doubles which contains all fitness for the population
 * @param len: an integer for size of the population or len of the fitnesses array
 *
 * @return sum: a double for the sum of fitnesses in the population
 */
double get_pop_fitness(double* fitnesses, int len)
{
	double sum = 0;
	for (int i = 0; i < len; i++)
		sum += fitnesses[i];
	return sum;
}

/**
 * randomly changes elements of S (mutation)
 *
 * @param S: a pointer an array of double representing the solution to be mutated
 * @param dim: an integer for the dimension of the solution
 * @param M: a struct holding mutation's information
 * @param l_b, r_b: the lowest and highest bounds for the elements of the solution
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return :None
 */
void mutate(double* S, int dim, M_data M, double l_b, double r_b, mt19937& mt_rand)
{
	for (int i = 0; i < dim; i++)
	{
		//double rand = rand_num(mt_rand);
		double rand_num = (double)rand() / (double)RAND_MAX;
		if(rand_num < M.rate)
		{
			//S[i] += random(-1, 1, mt_rand) * (r_b - l_b) * M.range * pow(2, (-1 * random(0, 1, mt_rand) * M.precision));
			S[i] += (((double)rand() * 2 / (double) RAND_MAX) - 1) + (r_b - l_b) * M.range * pow(2, -1 * ((double)rand() / RAND_MAX) * M.precision);
			if (S[i] < l_b)
				S[i] = l_b;
			else if(S[i] > r_b)
				S[i] = r_b;
		}	
	}
}

/**
 * performs the crossover
 *
 * @param P1, P2: pointers to arrays of doubles for the 2 parents to be used for crossover
 * @param O1, O2: pointers to empty arrays of doubles to hold the offsprings resulting from the crossover
 * @param dim: an in for the dimension or length of P1
 * @param CR: a double for the crossover rate
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return :None
 */
void crossover(double* P1, double* P2, double* O1, double* O2, int dim,  double CR, mt19937& mt_rand)
{
	//double rand_num = random(0, 1, mt_rand)
	double rand_num = (double)rand() / (double)RAND_MAX;
	if(rand_num < CR)
	{
		//int d = (int)floor(random(1, dim));
		int d = rand() % (dim - 1) + 1;
		for(int i = 0; i < d; i++)
		{
			O1[i] = P1[i];
			O2[i] = P2[i];
		}
		for(int i= d; i < dim; i++)
		{
			O1[i] = P2[i];
			O2[i] = P1[i];
		}
	}
	else
	{
		memcpy(O1, P1, sizeof(double) * dim);
		memcpy(O2, P2, sizeof(double) * dim);
	}
	
}

/**
 * to be used by the qsort function to compare two rows of the matrix
 *
 * @param P1, P2: 2 rows of the matrix
 *
 * @return :1 for P1 > P2, -1 for P1 < P2, 0 for P1 == P2
 */
int cmp ( const void* P1, const void* P2 ) 
{
	const double(*S1) = *(const double **)P1;
	const double(*S2) = *(const double **)P2;
        if (S1[1] > S2[1])
		return 1;
	else if (S1[1] < S2[1])
	       	return -1;
        return 0;
}

/**
 * Sort the rows of a matrix bases on their fitnesses (row with the smallest fitness be on top)
 *
 * @param m: the matrix to be sorted
 * @param fitnesses: an array of double containing the fitnesses for each row of the matrix to be sorted
 * @param NS: an integer for the number of solutions or rows in the matrix m
 * @param dim: an integer for the dimension of the solutions or number of columns in matrix m
 *
 * @return : None
 */ 
void sort(matrix* m, double *fitnesses, int NS, int dim)
{
	matrix* sorted_fitnesses = new matrix(NS, 2);
	matrix* tmp_mat = new matrix(NS, dim);

        for(int i = 0; i < NS; i++)
        {
                sorted_fitnesses->mat[i][0] = i;
                sorted_fitnesses->mat[i][1] = fitnesses[i];
        }

	qsort(sorted_fitnesses->mat, NS, sizeof(sorted_fitnesses->mat[0]), cmp);
	for(int i = 0; i < NS; i++)
	{
		memcpy(tmp_mat->mat[i], m->mat[(int)(sorted_fitnesses->mat[i][0])], sizeof(double) * dim); //temporary save m's rows in order
		fitnesses[i] = sorted_fitnesses->mat[i][1];
	}
	for(int i = 0; i < NS; i++)
		memcpy(m->mat[i], tmp_mat->mat[i], sizeof(double) * dim); //copy rows of tmp_mat to m keeping the same order

	delete sorted_fitnesses;
	delete tmp_mat;
	
}

/**
 * sort both the initial population and the new population then apply ellitism and save results in initial population
 *
 * @param population: a matrix for the initial population
 * @param pop_fitness: an array of double containing the fitnesses for each solution in population
 * @param new_population: a matrix for the new population
 * @param new_population_fitness: an array of double containing the fitnesses for each solution in new population
 * @param EliteSN: an int for the number of element from the initial population to be saved in the new population
 * @param NS, dim: integers for the number of solutions in population and number of columns or dimension for each solution
 *
 * @return :None
 */
void reduce(matrix* population, double* pop_fitnesses, matrix* new_population, double* new_pop_fitnesses, int EliteSN, int NS, int dim)
{
	sort(population, pop_fitnesses, NS, dim);
	sort(new_population, new_pop_fitnesses, NS, dim);
	for(int i = 0; i < EliteSN; i++)
	{
		memcpy(new_population->mat[NS -1 -i], population->mat[i], sizeof(double) * dim);
		new_pop_fitnesses[NS -1 - i] = pop_fitnesses[i];
	}
	

	for(int i = 0; i < NS; i++)
	{
		memcpy(population->mat[i], new_population->mat[i], sizeof(double) * dim);
		pop_fitnesses[i] = new_pop_fitnesses[i];
	}
}	

int(*select_ptr[])(double*, double, int, mt19937&) = {&roulette_wheel, &tournament_selection};

/**
 * simulate the genetic algorithm
 *
 * @param params: a struct containing some arguments for genetic algorithm
 * @param func_id: an integer for the index of the objective function to be used
 * @param best_fitness: an array of doubles to keep track of the best fitnesses after each generation
 * @param num_tournaments: an integer for the number of tournaments for the tournament selection algorithm
 * @param selection_funct_id: an integer which specifies which selection algorithm is to be used
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void GA(GA_params params, int function_id, double* best_fitnesses, int num_tournaments, int selection_funct_id, mt19937& mt_rand )
{
	functions *func = new functions(); //needed to make accessible the functions in functions_ptr
	int EliteSN = floor(params.ns * params.er);
	matrix *population = new matrix(params.ns, params.dim, params.l_b, params.u_b, mt_rand); //initial population
	matrix *new_population = new matrix(params.ns, params.dim); //to temporarilty hold the population for the new generation
	double *pop_fits = new double[params.ns]; //to hold fitnesses of solutions in population
	double *pop_norm_fits = new double[params.ns]; //normalized fitnesses
	double *new_pop_fits = new double[params.ns]; //to hold fitnesses of solutions in new_population
        double *new_pop_norm_fits = new double[params.ns]; //normalized fitnesses
	double *O1 = new double[params.dim];
	double *O2 = new double[params.dim];
	double tot_pop_fitness; //sum of all fitnesses in pop_norm_fits;
	for(int i = 0; i < params.t_max; i++)
	{
		for(int j = 0; j < params.ns; j++)
                	pop_fits[j] = (func->*functions_ptr_1[function_id])( population->mat[j], params.dim); //compute the fitness for a row in population and save in pop_fits
		normalize_fitnesses(pop_fits, pop_norm_fits, params.ns);
		tot_pop_fitness = get_pop_fitness(pop_norm_fits, params.ns);
		for(int j = 0; j < params.ns; j += 2)
		{
			int ind_P1, ind_P2;
			if (selection_funct_id == 0)
			{
				ind_P1 = select_ptr[selection_funct_id](pop_norm_fits, tot_pop_fitness, params.ns, mt_rand);
				ind_P2 = select_ptr[selection_funct_id](pop_norm_fits, tot_pop_fitness, params.ns, mt_rand);
				while(ind_P1 == ind_P2) //to make sure that the same parent is not chosen twice
                                	ind_P1 = select_ptr[selection_funct_id](pop_norm_fits, tot_pop_fitness, params.ns, mt_rand);

			}
			else
			{
				ind_P1 = select_ptr[selection_funct_id](pop_norm_fits, num_tournaments, params.ns, mt_rand);
                                ind_P2 = select_ptr[selection_funct_id](pop_norm_fits, num_tournaments, params.ns, mt_rand);
				while(ind_P1 == ind_P2) //to make sure that the same parent is not chosen twice
                                	ind_P1 = select_ptr[selection_funct_id](pop_norm_fits, num_tournaments, params.ns, mt_rand);

			}

			crossover(population->mat[ind_P1], population->mat[ind_P2], O1, O2, params.dim, params.cr, mt_rand);
			mutate(O1, params.dim, params.M, params.l_b, params.u_b, mt_rand);
			mutate(O2, params.dim, params.M, params.l_b, params.u_b, mt_rand);

			memcpy(new_population->mat[j], O1, sizeof(double) * params.dim);
			memcpy(new_population->mat[j + 1], O2, sizeof(double) * params.dim);

		}
		
		for(int i = 0; i < params.ns; i++)
                        new_pop_fits[i] = (func->*functions_ptr_1[function_id])( new_population->mat[i], params.dim); //compute the fitness for a row in new_population and save in pop_fits
		reduce(population, pop_fits, new_population, new_pop_fits, EliteSN, params.ns, params.dim); //apply elitism (combine best of pop and new pop) and save results in population
		sort(population, pop_fits, params.ns, params.dim);
		best_fitnesses[i] = pop_fits[0];

	}

	delete func;
	delete population;
	delete new_population;
	delete[] pop_fits;
	delete[] pop_norm_fits;
	delete[] new_pop_fits;
	delete[] new_pop_norm_fits;
	delete[] O1;
	delete[] O2;



}

