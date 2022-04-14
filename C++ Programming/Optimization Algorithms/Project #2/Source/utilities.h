/*********************************************
*                                            *
* utilities.h                                *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #1                                 *
* Last modified on Wednesday April 17, 2019  *
*                                            *
*********************************************/
class matrix;
using namespace std;
#include <random>
class utilities
{
	public:
	/*
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
	
	/*
	 * write a 2d array to a csv file
	 *
	 * @param mat: a matrix containing the elements to write to the csv file
	 * @param file_name: the name of the file where data will be saved
	 *
	 * @return : None
	 */
	void write_to_file(matrix* mat, string file_name);
	
	//get a number from the user for the search algorithm to be run
	int get_algorithm_id();


	/*
	 * simulate all the functions
	 *
	 * @param num_dimensions: the numbers of dimensions to be simulated
	 * @param dimensions: an array containing the different dimensions to be simulated
	 * @param num_functions: the number of functions to be simulated
	 * @param ranges: an array containing values for the range of each function
	 * @param sample_size: the size of the sample space
	 *
	 * @return : a 2d array containg all the results of the simulation
	 *
	 */
	void simulate(int num_dimensions, double* dimensions, int num_functions, double* ranges, int sample_size, int algorithm_id, double delta, mt19937& mt_rand);

};
