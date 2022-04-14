/******************************************
*                                         *
* DE.cpp                                  *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #3                              *
* Last modified on Wednesday May 1st, 2019*
*                                         *
******************************************/

//#include <random>
#include <iostream>
#include <string.h>
#include "functions.h"
#include <stdlib.h>
#include "matrix.h"
#include "DE.h"
#include "GA.h" // for sort(), cmp(), functions_ptr, random etc...
using namespace std;


double (functions::*functions_ptr_2[])(double*, int) = {&functions::Schwefel, &functions::first_De_Jong, &functions::Rosenbrock, &functions::Rastrigin, &functions::Greiwangk, &functions::Sine_Envelope_Sine_Wave, &functions::Stretched_V_Sine_Wave, &functions::Ackley_One, &functions::Ackley_Two, &functions::Egg_Holder, &functions::Rana, &functions::Pathological, &functions::Michalewicz, &functions::Masters_Cosine_Wave, &functions::Quartic, &functions::Levy, &functions::Step, &functions::Alpine}; //create an array containing pointers to all the 18 functions (makes code shorter, otherwise each function has to be called manually one a the time. Compare with project1 to see the difference)






/**
 * add or subtract 2 vectors
 *
 * @param v1, v2: the 2 vectors to be added
 * @param v3: vector to save the result of the addition
 * @param dim: an int for the dimension of the vectors
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param add: boolean specifying that addition is to be performed if true else substraction
 *
 * @return : None
 */
void bin_add_or_sub(double *v1, double *v2, double *v3, int dim, double l_b, double u_b, bool add, double cr,  mt19937& mt_rand)
{
	if (add)
	{
		for(int i = 0; i < dim; i++)
		{
			//double rand_num = random(0, 1, mt_rand);
			//int j = (int)floor(random(0, dim, mt_rand));

			double rand_num = (double)rand() / (double) RAND_MAX;
			int j = rand() % dim;
			if (rand_num <= cr || j == i )
			{
				v3[i] = v1[i] + v2[i];
				if(v3[i] < l_b)
					v3[i] = l_b;
				else if (v3[i] > u_b)
					v3[i] = u_b;
			}
		}
	}
	else
	{
		for(int i = 0; i < dim; i++)
		{
			//double rand_num = random(0, 1, mt_rand);
			//int j = (int)floor(random(0, dim, mt_rand))

                        double rand_num = (double)rand() / (double) RAND_MAX;
			int j = rand() % dim;
                        if (rand_num <= cr || j == i)
                        {
				v3[i] = v1[i] - v2[i];
				if(v3[i] < l_b)
					v3[i] = l_b;
				else if (v3[i] > u_b)
					v3[i] = u_b;
			}

		}
	}
}

/**
 * multiply a vector by a number
 *
 * @param F: a double for the number that multiplies the vector
 * @param v1: the vector to be multiplied
 * @param v2: a vector to save the results of the multiplication
 * @param dim: an int for the dimension of the vectors
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 *
 * @return: None
 */
void bin_const_mult(double F, double *v1, double *v2, int dim, double l_b, double u_b, double cr,  mt19937& mt_rand)
{
        
        
	for(int i = 0; i < dim; i++)
	{
		//double rand_num = random(0, 1, mt_rand);
		//int j = (int)floor(random(0, dim, mt_rand))

		double rand_num = (double)rand() / (double) RAND_MAX;
		int j = rand() % dim;
		if (rand_num <= cr || j ==i)
		{
			v2[i] = F * v1[i];
			if(v2[i] < l_b)
				v2[i] = l_b;
			else if (v2[i] > u_b)
				v2[i] = u_b;
		}
	}
        
}

/**
 * add or subtract 2 vectors
 *
 * @param v1, v2: the 2 vectors to be added
 * @param v3: vector to save the result of the addition
 * @param dim: an int for the dimension of the vectors
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param add: boolean specifying that addition is to be performed if true else substraction
 *
 * @return : None
 */
void exp_add_or_sub(double *v1, double *v2, double *v3, int dim, double l_b, double u_b, bool add, double cr,  mt19937& mt_rand)
{
	if (add)
	{
		for(int i = 0; i < dim; i++)
		{
			//double rand_num = random(0, 1, mt_rand);
			double rand_num = (double)rand() / (double) RAND_MAX;
			if (rand_num > cr)
				break;
			v3[i] = v1[i] + v2[i];
			if(v3[i] < l_b)
				v3[i] = l_b;
			else if (v3[i] > u_b)
				v3[i] = u_b;
		}
	}
	else
	{
		for(int i = 0; i < dim; i++)
		{
			//double rand_num = random(0, 1, mt_rand);
                        double rand_num = (double)rand() / (double) RAND_MAX;
                        if (rand_num > cr)
                                break;
                        v3[i] = v1[i] - v2[i];
			if(v3[i] < l_b)
                                v3[i] = l_b;
                        else if (v3[i] > u_b)
                                v3[i] = u_b;

		}
	}
}

/**
 * multiply a vector by a number
 *
 * @param F: a double for the number that multiplies the vector
 * @param v1: the vector to be multiplied
 * @param v2: a vector to save the results of the multiplication
 * @param dim: an int for the dimension of the vectors
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 *
 * @return: None
 */
void exp_const_mult(double F, double *v1, double *v2, int dim, double l_b, double u_b, double cr,  mt19937& mt_rand)
{
        
        
	for(int i = 0; i < dim; i++)
	{
		v2[i] = F * v1[i];
		if(v2[i] < l_b)
			v2[i] = l_b;
		else if (v2[i] > u_b)
			v2[i] = u_b;
	}
        
}


/**
 * perform crossover and mutation using strategy 1 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F: 
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_1(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
	int ind_1 = -1, ind_2 = -1;
	while (ind_1 == ind_2 || ind_1 == i || ind_2 == i)
	{
		//ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		//ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		ind_1 = rand() % population->num_rows;
		ind_2 = rand() % population->num_rows;
	}	

	exp_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
	exp_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	exp_add_or_sub(population->mat[best], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

}

/**
 * perform crossover and mutation using strategy 2 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F: 
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_2(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
        int ind_1 = -1, ind_2 = -1, ind_3 = -1;
        while (ind_1 == ind_2 || ind_1 == ind_3 || ind_1 == i || ind_2 == ind_3 || ind_2 == i || ind_3 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		//ind_3 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
		ind_3 = rand() % population->num_rows;
        }       

        exp_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        exp_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	exp_add_or_sub(population->mat[ind_3], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

}

/**
 * perform crossover and mutation using strategy 3 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F:
 * @param lambda:
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_3(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
        int ind_1 = -1, ind_2 = -1;
        while (ind_1 == ind_2 || ind_1 == i || ind_2 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
        }
	double *tmp = new double[population->num_columns];

        exp_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        exp_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	exp_add_or_sub(population->mat[best], population->mat[i], tmp, population->num_columns, l_b, u_b, false, cr, mt_rand);
        exp_const_mult(lambda, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	exp_add_or_sub(tmp, v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);
	exp_add_or_sub(population->mat[i], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

	delete[] tmp;

}

/**
 * perform crossover and mutation using strategy 4 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F:
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_4(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
        int ind_1 = -1, ind_2 = -1, ind_3 = -1, ind_4 = -1;
        while (ind_1 == ind_2 || ind_1 == ind_3 || ind_1 == ind_4 || ind_1 == i || ind_2 == ind_3 || ind_2 == ind_4 || ind_2 == i || ind_3 == ind_4 || ind_3 == i || ind_4 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_3 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_4 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
		ind_3 = rand() % population->num_rows;
                ind_4 = rand() % population->num_rows;
        }


        exp_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, true, cr, mt_rand);
	exp_add_or_sub(v, population->mat[ind_3], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
	exp_add_or_sub(v, population->mat[ind_4], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        exp_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	exp_add_or_sub(population->mat[best], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

}

/**
 * perform crossover and mutation using strategy 5 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F:
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_5(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
	int ind_1 = -1, ind_2 = -1, ind_3 = -1, ind_4 = -1, ind_5 = -1;
        while (ind_1 == ind_2 || ind_1 == ind_3 || ind_1 == ind_4 || ind_1 == ind_5 || ind_1 == i || ind_2 == ind_3 || ind_2 == ind_4 || ind_2 == ind_5 || ind_2 == i || ind_3 == ind_4 || ind_3 == ind_5 ||  ind_3 == i || ind_4 == ind_5 || ind_4 == i || ind_5 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_3 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_4 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		//ind_5 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
                ind_3 = rand() % population->num_rows;
                ind_4 = rand() % population->num_rows;
		ind_5 = rand() % population->num_rows;
        }


        exp_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, true, cr, mt_rand);
        exp_add_or_sub(v, population->mat[ind_3], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        exp_add_or_sub(v, population->mat[ind_4], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        exp_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
        exp_add_or_sub(population->mat[ind_5], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);
}

/**
 * perform crossover and mutation using strategy 6 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F: 
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_6(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
	int ind_1 = -1, ind_2 = -1;
	while (ind_1 == ind_2 || ind_1 == i || ind_2 == i)
	{
		//ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		//ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		ind_1 = rand() % population->num_rows;
		ind_2 = rand() % population->num_rows;
	}	

	bin_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
	bin_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	bin_add_or_sub(population->mat[best], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

}

/**
 * perform crossover and mutation using strategy 7 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F: 
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_7(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
        int ind_1 = -1, ind_2 = -1, ind_3 = -1;
        while (ind_1 == ind_2 || ind_1 == ind_3 || ind_1 == i || ind_2 == ind_3 || ind_2 == i || ind_3 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		//ind_3 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
		ind_3 = rand() % population->num_rows;
        }       

        bin_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        bin_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	bin_add_or_sub(population->mat[ind_3], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

}

/**
 * perform crossover and mutation using strategy 8 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F:
 * @param lambda:
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_8(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
        int ind_1 = -1, ind_2 = -1;
        while (ind_1 == ind_2 || ind_1 == i || ind_2 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
        }
	double *tmp = new double[population->num_columns];

        bin_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        bin_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	bin_add_or_sub(population->mat[best], population->mat[i], tmp, population->num_columns, l_b, u_b, false, cr, mt_rand);
        bin_const_mult(lambda, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	bin_add_or_sub(tmp, v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);
	bin_add_or_sub(population->mat[i], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

	delete[] tmp;

}

/**
 * perform crossover and mutation using strategy 9 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F:
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_9(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
        int ind_1 = -1, ind_2 = -1, ind_3 = -1, ind_4 = -1;
        while (ind_1 == ind_2 || ind_1 == ind_3 || ind_1 == ind_4 || ind_1 == i || ind_2 == ind_3 || ind_2 == ind_4 || ind_2 == i || ind_3 == ind_4 || ind_3 == i || ind_4 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_3 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_4 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
		ind_3 = rand() % population->num_rows;
                ind_4 = rand() % population->num_rows;
        }


        bin_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, true, cr, mt_rand);
	bin_add_or_sub(v, population->mat[ind_3], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
	bin_add_or_sub(v, population->mat[ind_4], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        bin_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
	bin_add_or_sub(population->mat[best], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);

}

/**
 * perform crossover and mutation using strategy 10 of the differential evolution algorithm
 *
 * @param population: a matrix representing the current generation
 * @param best: an int for the index of the best solution(lowest fitness) in population
 * @param i: an int for the index of the solution being mutated in population
 * @param v: a vector to save the result of the mutation or crossover
 * @param l_b, u_b: doubles for the range (lower and upper bounds) for elements in the vectors
 * @param F:
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void strategy_10(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr,  mt19937& mt_rand)
{
	memcpy(v, population->mat[i], sizeof(double) * population->num_columns);
	int ind_1 = -1, ind_2 = -1, ind_3 = -1, ind_4 = -1, ind_5 = -1;
        while (ind_1 == ind_2 || ind_1 == ind_3 || ind_1 == ind_4 || ind_1 == ind_5 || ind_1 == i || ind_2 == ind_3 || ind_2 == ind_4 || ind_2 == ind_5 || ind_2 == i || ind_3 == ind_4 || ind_3 == ind_5 ||  ind_3 == i || ind_4 == ind_5 || ind_4 == i || ind_5 == i)
        {
                //ind_1 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_2 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_3 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                //ind_4 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
		//ind_5 = (int)floor(random(0, population->num_rows - 0.1, mt_rand));
                ind_1 = rand() % population->num_rows;
                ind_2 = rand() % population->num_rows;
                ind_3 = rand() % population->num_rows;
                ind_4 = rand() % population->num_rows;
		ind_5 = rand() % population->num_rows;
        }


        bin_add_or_sub(population->mat[ind_1], population->mat[ind_2], v, population->num_columns, l_b, u_b, true, cr, mt_rand);
        bin_add_or_sub(v, population->mat[ind_3], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        bin_add_or_sub(v, population->mat[ind_4], v, population->num_columns, l_b, u_b, false, cr, mt_rand);
        bin_const_mult(F, v, v, population->num_columns, l_b, u_b, cr, mt_rand);
        bin_add_or_sub(population->mat[ind_5], v, v, population->num_columns, l_b, u_b, true, cr, mt_rand);
}



/**
 * select which solution should be in the next generation
 *
 * @param v1_fitness, v2_fitness: doubles for the fitnesses of the vectors subject to the selection
 *
 * @return : an integer (1 means that vector v1 is selected, and -1 means that vector v2 is selected)
 */
int selection(double v1_fitness, double v2_fitness)
{
	if (v1_fitness <= v2_fitness)
		return 1;
	return -1;
}

void(*strategy_ptr[])(matrix*, int, int, double*, double, double, double, double, double, mt19937&) = {&strategy_1, &strategy_2, &strategy_3, &strategy_4, &strategy_5, &strategy_6, &strategy_7, &strategy_8, &strategy_9, &strategy_10};

void DE(DE_params params, int function_id, double* best_fitnesses, mt19937& mt_rand, int strategy_id)
{
        functions *func = new functions(); //needed to make accessible the functions in functions_ptr
        matrix *population = new matrix(params.ns, params.dim, params.l_b, params.u_b, mt_rand); //initial population
        matrix *new_population = new matrix(params.ns, params.dim); //to temporarilty hold the population for the new generation
        double *pop_fits = new double[params.ns]; //to hold fitnesses of solutions in population
        double *new_pop_fits = new double[params.ns]; //to hold fitnesses of solutions in new_population
        double *O = new double[params.dim]; //offspring
	int best, tmp_fit;
        for(int j = 0; j < params.ns; j++)
        	pop_fits[j] = (func->*functions_ptr_2[function_id])( population->mat[j], params.dim); //compute the fitness for a row in population and save in pop_fits
	sort(population, pop_fits, params.ns, params.dim);
	best = 0;

        //loops params.t_max times for params.t_max generations
        for(int i = 0; i < params.t_max; i++)
        {
		
                for(int j = 0; j < params.ns; j++)
                {
			strategy_ptr[strategy_id](population, best, j, O, params.l_b, params.u_b, params.F, params.lambda, params.cr, mt_rand);
			tmp_fit = (func->*functions_ptr_2[function_id])(O, params.dim); //compute the fitness for offspring O
			if (selection(tmp_fit, pop_fits[j]) == 1)
				memcpy(new_population->mat[j], O, sizeof(double) * params.dim);
			else
				memcpy(new_population->mat[j], population->mat[j], sizeof(double) * params.dim);

                }


                for(int j = 0; j < params.ns; j++)
		{
                        new_pop_fits[j] = (func->*functions_ptr_2[function_id])( new_population->mat[j], params.dim); //compute the fitness for a row in new_population and save in pop_fits
			memcpy(population->mat[j], new_population->mat[j], sizeof(double) * params.dim);
		}
		memcpy(pop_fits, new_pop_fits, sizeof(double) * params.ns);
                sort(population, pop_fits, params.ns, params.dim);
                best_fitnesses[i] = pop_fits[0];

        }

	delete func;
        delete population;
        delete new_population;
        delete[] pop_fits;
        delete[] new_pop_fits;
        delete[] O;


}



