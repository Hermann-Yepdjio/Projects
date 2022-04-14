/******************************************
*                                         *
* FA.h                                    *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #4                              *
* Last modified on Tuesday May 14, 2019   *
*                                         *
******************************************/

#include <random>
using namespace std;


//to hold the inputs to the PSO function
typedef struct
{
	matrix *std_devs;
	double *gBests, gamma, B0, alpha, l_b, u_b;
	int func_id, nf, dim, num_iters, std_devs_row;  
}FA_Params;


//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
//double random(double l_b, double u_b, mt19937& mt_rand);


/**
 * compute the fitnesses for all the particles in a population
 *
 * @param matrix: the matrix containing the particles which fitnesses need to be computed
 * @param fitnesses: a vector that will hold the fitness for each particle
 * @param func_id: the index for the function to be used to compute the fitness
 *
 * @return :None
 */
//void compute_fitnesses(matrix *swarm, double* fitnesses, int func_id);

/**
 * finds the lowest fitness in fitnesses
 *
 * @param fitnesses: a vector containing the fitnesses for each firefly
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the lowest fitness found
 */
//int get_gBest(double* fitnesses, int num_fireflies);

/**
 *compute the attractiveness of a firefly
 *  
 * @param r: a double for the distance between two fireflies
 * @param B0: a double for the attractiveness when r = 0
 * @param gamma: a double constant
 *
 * @return : a double for the attractiveness of a firefly
 */
double eq_2(double r, double B0, double gamma);


/**
 * compute the distance between two fireflies
 *
 * @params firefly_1, firefly_2: vectors of doubles for the two fireflies
 * @param dim: an intege for the dimension of each firefliy vector
 *
 * @return :a double for the distance betweem the two fireflies
 */
double eq_3(const double *firefly_1, const double *firefly_2, const int dim);



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
void eq_4(double *firefly_1, const double *firefly_2, double beta, double gamma, double r, double alpha, int dim, double l_b, double u_b, mt19937& mt_rand);

/**
 * copy values of swarm_2 into swarm_1
 *
 * @params swarm_1, swarm_2: matrices where each row represents a firefly
 * @params num_fireflies, dim: integers for the number of rows and the number of columns in the swarms
 *
 * @return : None
 */
void copy_swarm(matrix *swarm_1, matrix *swarm_2, int num_fireflies, int dim);

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
void FA(FA_Params params,  double* num_func_calls, double *stagnation_iter, mt19937& mt_rand );

