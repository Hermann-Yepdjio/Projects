/******************************************
*                                         *
* GA.h                                    *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #3                              *
* Last modified on Saturday April 27, 2019*
*                                         *
******************************************/

#include <random>
using namespace std;


//to hold parameters for the mutation function
typedef struct
{
	double rate, range, precision; 
}M_data;

//to hold parameters for the GA function 
typedef struct
{
	int ns, dim, t_max;
	double l_b, u_b, cr, er;
      	M_data M;	
}GA_params;


//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
double random(double l_b, double u_b, mt19937& mt_rand);

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
int roulette_wheel(double* fitnesses, double pop_fitness, int len, mt19937& mt_rand);

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
int tournament_selection(double* fitnesses, double num_tournaments, int len, mt19937& mt_rand);


/**
 * adjusts the fitnesses so they can be used in the selection algorithms
 *
 * @param fitnesses: a pointer to an array of doubles which contains all fitness for the population
 * @param adj_fitnesses: a pointer to an empty array of double to hold the adjusted fitnesses
 * @param len: an integer for size of the population or len of the fitnesses array
 *
 * @return : None
 */
void normalize_fitnesses(double* fitnesses, double* norm_fitnesses, int len);


/**
 * compute the sum of all fitnesses in the population
 *
 * @param fitnesses: a pointer to an array of doubles which contains all fitness for the population
 * @param len: an integer for size of the population or len of the fitnesses array
 *
 * @return sum: a double for the sum of fitnesses in the population
 */
double get_pop_fitness(double* fitnesses, int len);


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
void mutate(double* S, int dim, M_data M, double l_b, double r_b, mt19937& mt_rand);

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
void crossover(double* P1, double* P2, double* O1, double* O2, int dim,  double CR, mt19937& mt_rand);


/**
 * to be used by the qsort function to compare two rows of the matrix
 *
 * @param P1, P2: 2 rows of the matrix
 *
 * @return :1 for P1 > P2, -1 for P1 < P2, 0 for P1 == P2
 */
int cmp ( const void* P1, const void* P2 ); 


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
void sort(matrix* m, double *fitnesses, int NS, int dim);

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
void reduce(matrix* population, double* pop_fitnesses, matrix* new_population, double* new_pop_fitnesses, int EliteSN, int NS, int dim);

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
void GA(GA_params params, int function_id, double* best_fitnesses, int num_tournaments, int selection_funct_id, mt19937& mt_rand );


