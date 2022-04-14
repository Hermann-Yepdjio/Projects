/******************************************
*                                         *
* PSO.h                                   *
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
	double *gBests, c1, c2, k, l_b, u_b;
	int func_id, np, dim, num_iters, std_devs_row;  
}PSO_Params;



//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
double random(double l_b, double u_b, mt19937& mt_rand);

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
void compute_fitnesses(matrix *swarm, double *fitnesses, int func_id, double *num_func_calls);

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
double compute_fitness(double *particle, int func_id, int dim, double *num_func_calls);


/**
 * sets the personnal best fitness for each particle
 *
 * @param fitnesses: a vector containing the fitnesses for each particle
 * @param pBest: a vector to hold the personnal best fitnesses for each particle
 * @param int num_particles: an int for number of particles)
 *
 * @return :None
 */
void set_pBests(long double* fitnesses, double* pBest, int num_particles);



/**
 * finds the lowest fitness in PBests (personnal bests)
 *
 * @param pBest: a vector containing the personnal best fitnesses for each particle
 * @param int num_particles: an int for number of particles)
 *
 * @return :an integer for the index of  the lowest fitness found
 */
int get_gBest(double* pBests, int num_particles);

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
void get_p_velocity(double *p, double* v, int dim, double c1, double c2, double pBest, double gBest, double k, int func_id, mt19937& mt_rand);

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
void update_particle(double *p, double *v, double l_b, double u_b,  int dim);

/**
 * extract a column from a matrixcompute_std_devs(matrix *swarm, matrix *std_devs, int index)
 *
 * @param swarm: a matrix where the column will be extracted from
 * @param column: a vector of double to hold the extracted column
 * @param col_index: an intger for the index of the column to be extracted
 *
 * @return : None
 */
void get_column(matrix* swarm, double* column, int col_index);

/**
 * compute the mean of elements in a vector
 *
 * @param vect: a vector of doubles containing the numbers to be averaged
 * @param size: an integer for the length of the vector vect
 *
 * @return : the mean of the numbers in vector vect
 */
double average(double* vect, int size);

/**
 * compute the standard deviation of elements in a vector
 *
 * @param vect: a vector of doubles containing the numbers which standard deviation needs to be calculated
 * @param size: an integer for the length of the vector vect
 *
 * @return : the standard deviation of the numbers in vector vect
 */
double std_dev(double* vect, int size);



/**
 * compute the standart deviation for each column in a matrix
 *
 * @param swarm: the matrix which columns will be used to compute the standard deviation
 * @param std_devs: a matrix where the computed standard deviation will be stored
 * @param index: the index of the row in the matrix where the computed standard deviations will be stored
 *
 * @return : None
 */
void compute_std_devs(matrix *swarm, matrix *std_devs, int index);

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
bool has_pop_stagnated(matrix *std_devs, int index, int iter, double percentage);

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
void PSO(PSO_Params params, double* num_func_calls, double *stagnation_iter, mt19937& mt_rand );


