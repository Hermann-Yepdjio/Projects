/******************************************
*                                         *
* DE.h                                    *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #3                              *
* Last modified on Wednesday May 1st, 2019*
*                                         *
******************************************/

#include <random>
using namespace std;



//to hold parameters for the DE function
typedef struct
{
	double cr, F, lambda, l_b, u_b;
	int dim, ns, t_max;
}DE_params;

//generate a double random number between l_b and r_b using Mersen Twister pseudo random number generator
//double random(int l_b, int u_b, mt19937& mt_rand);

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
void bin_add_or_sub(double *v1, double *v2, double *v3, int dim, double l_b, double u_b, bool add, double cr,  mt19937& mt_rand);

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
void bin_const_mult(double F, double *v1, double *v2, int dim, double l_b, double u_b, double cr,  mt19937& mt_rand);

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
void exp_add_or_sub(double *v1, double *v2, double *v3, int dim, double l_b, double u_b, bool add, double cr,  mt19937& mt_rand);

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
void exp_const_mult(double F, double *v1, double *v2, int dim, double l_b, double u_b, double cr,  mt19937& mt_rand);

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
void strategy_1(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);


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
void strategy_2(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);

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
void strategy_3(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);

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
void strategy_4(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);


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
void strategy_5(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);

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
void strategy_6(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);

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
void strategy_7(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);

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
void strategy_8(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);

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
void strategy_9(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr, mt19937& mt_rand);


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
void strategy_10(matrix* population, int best, int i, double* v, double l_b, double u_b, double F, double lambda, double cr,  mt19937& mt_rand);


/**
 * to be used by the qsort function to compare two rows of the matrix
 *
 * @param P1, P2: 2 rows of the matrix
 *
 * @return :1 for P1 > P2, -1 for P1 < P2, 0 for P1 == P2
 */
//int cmp ( const void* P1, const void* P2 );


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
//void sort(matrix* m, double *fitnesses, int NS, int dim);


/**
 * select which solution should be in the next generation
 *
 * @param v1_fitness, v2_fitness: doubles for the fitnesses of the vectors subject to the selection
 *
 * @return : an integer (1 means that vector v1 is selected, and -1 means that vector v2 is selected)
 */
int selection(double v1_fitness, double v2_fitness);


void DE(DE_params params, int function_id, double* best_fitnesses, mt19937& mt_rand, int strategy_id);

