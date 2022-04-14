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
	 * simulate the Particle Swarm Optimization, the Firefly algoritm and the Harmony Search algorithm
	 *
	 * @param dim : an integer for the dimension of the solutions
	 * @param ns : an integer the number of solutions
	 * @param num_functions : an integer for the number of objective functions to be simulated (the 18 functions)
	 * @param ranges: an array of doubles containing the lower and upper bound for each of the objective functions
	 * @param algo_id: an integer for the evolutionary algorithm to be simulated
	 * @param num_iters : an integer for the number of iterations for the swarm algorithms
	 * @param num_exp: an integer for the number of experimentations to be run
	 * @param c1, c2: doubles 
	 * @param k: a double 
	 * @param gamma: a double
	 * @param B0: a double 
	 * @param alpha: a double
	 * @param HMCR: a double
	 * @param PAR: a double 
	 * @param bw: a double
	 * @param mt_rand: a seeded random generator to generate random numbers (seeded once in main.cpp)
	 *
	 * @return :  None
	 */
	void simulate(int dim, int ns, int num_functions, double* ranges, int algo_id, int num_iters, int num_exp, double c1, double c2, double k, double gamma, double BO, double alpha, double HMCR, double PAR, double bw,  mt19937& mt_rand);
};
