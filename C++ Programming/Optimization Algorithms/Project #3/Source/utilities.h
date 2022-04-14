/*********************************************
*                                            *
* utilities.h                                *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #3                                 *
* Last modified on Wednesday May 1st, 2019   *
*                                            *
*********************************************/
class matrix;
using namespace std;
#include <random>
class utilities
{
	public:
	/**
	 * split a string into double tokens
	 *
	 * @param string: the string to be splitted
	 * @param delim: the character that separates the tokens in the string
	 * @param num_tokens: number of tokens to expect
	 *
	 * @return : an array of doubles
	 *
	 */
	double* str_to_tok(char* string, char* delim, int num_tokens);
	
	/**
	 * write a 2d array to a csv file
	 *
	 * @param mat: a matrix containing the elements to write to the csv file
	 * @param file_name: the name of the file where data will be saved
	 *
	 * @return : None
	 */
	void write_to_file(matrix* mat, string file_name);
	
	//get a number from the user for the evolutiinary algorithm to be run
	int get_algorithm_id();

	//get a number from the user for the selection algorithm to be run
	int get_selection_id();


	//find the lowest value in a list
	double find_lowest(const double *list, int len);



	/**
	 * simulate both the genetic algorithm and the differential evolution algorithm
	 *
	 * @param dim : an integer for the dimension of the solutions
	 * @param ns : an integer the number of solutions
	 * @param num_functions : an integer for the number of objective functions to be simulated (the 18 functions)
	 * @param ranges: an array of doubles containing the lower and upper bound for each of the objective functions
	 * @param algo_id: an integer for the evolutionary algorithm to be simulated
	 * @param select_id: an integer for the selection algorithm to be used
	 * @param num_gen : an integer for the number of generations for the evolutionary algorithms
	 * @param num_exp: an integer for the number of experimentations to be run
	 * @param num_trnmt: an integer for the number of tournaments for the tournameent selection algorithm
	 * @param cr: a double for crossover rate 
	 * @param er: a double for the elitism rate for the genetic algorithm
	 * @param m_range: a double for the mutation range for the genetic algorithm
	 * @param m_rate: a double for the mutation rate for the genetic algorithm
	 * @param m_precision: a double for the mutation precision for the genetic algorithm
	 * @param F: a double
	 * @param lambda: a double
	 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
	 *
	 * @return :  None
	 */


	void simulate(int dim, int ns, int num_functions, double* ranges, int algo_id, int select_id, int num_gen, int num_exp, int num_trnmt, double cr, double er, double m_range, double m_rate, double m_precision, double F, double lambda, mt19937& mt_rand);
};
