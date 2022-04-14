/******************************************
*                                         *
* HS.h                                    *
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
	double *gBests, *gWorsts, HMCR, PAR, HMS, EOR, l_b, u_b, bw;
	int func_id, nh, dim, num_iters, std_devs_row;  
}HS_Params;



//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
//double random(double l_b, double u_b, mt19937& mt_rand);

/**
 * compute the fitnesses for all the particles in a population
 *
 * @param population: the matrix containing the harmonies which fitnesses need to be computed
 * @param fitnesses: a vector that will hold the fitness for each particle
 * @param func_id: the index for the function to be used to compute the fitness
 *
 * @return :None
 */
//void compute_fitnesses(matrix *population, double* fitnesses, int func_id);


/**
 * finds the lowest fitness in PBests (personnal bests)
 *
 * @param pBest: a vector containing the personnal best fitnesses for each particle
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the lowest fitness found
 */
//int get_gBest(double* fitnesses, int num_harmonies);

/**
 * finds the highest fitness in fitnesses
 *
 * @param fitnesses: a vector containing the fitnesses for each harmony
 * @param int num_particles: an int for number of particles)
 *
 * @return : an integer for the index the highest fitness found
 */
int get_gWorst(double *fitnesses, int num_harmonies);


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
void adjust_harmony(const double *old_h, double *new_h, int dim, double bw, double l_b, double u_b, mt19937& mt_rand);

/**
 * create a new harmony new_h using random values
 *
 * @param new_h: vector of doubles to hold the new harmony
 * @param dim: an integer for the dimension of the harmniesh
 * @params l_b, u_b: doubles for the lowest and upper bounds of an harmony's element
 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
 *
 * @return : None
 */
void create_random_harmony(double *new_h, int dim, double l_b, double u_b, mt19937& mt_rand);

/**
 * copy values of pop_2 into pop_1
 *
 * @params pop_1, pop_2: matrices where each row represents am harmony
 * @params num_harmonies, dim: integers for the number of rows and the number of columns in the populations
 *
 * @return : None
 */
void copy_population(matrix *pop_1, matrix *pop_2, int num_harmonies, int dim);


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
void HS(HS_Params params,  double* num_func_calls, double *stagnation_iter, mt19937& mt_rand );

