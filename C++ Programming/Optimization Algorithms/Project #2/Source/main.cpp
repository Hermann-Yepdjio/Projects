/********************************************
*                                           *
* main.cpp                                  *
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #2                                *
* Last modified on Wednesday April 17, 2019 *
*                                           *
*********************************************/



#include <iostream>
#include <cstring>
#include "utilities.h"
#include <random>
using namespace std;

int main(int argc, char **argv)
{
     
	if (argc > 1 and argc < stoi(argv[1]) + 1)
	{
		cerr<<"Not enough argument was provided. Please try again and provide all the arguments \n";
		exit(1);

	}
	
	utilities* util = new utilities();

	int sample_size = stoi(argv[2]); //mumber of rows for the matrix
	int num_dimensions  = stoi(argv[3]); //number of dimensions to be tested
	char* delimiter = argv[4]; //delimiter used to separate tokens in strings that need to be splitted
	char* dimensions_string = argv[5]; //string containing the dimensions to be tested
	double* dimensions = util->str_to_tok(dimensions_string, delimiter, num_dimensions); //array containing the dimensions to be used
	int num_functions = stoi(argv[6]); // number of functions to be tested
	char* ranges_string = argv[7]; //string containing the ranges for each function
	double* ranges = util->str_to_tok(ranges_string, delimiter, num_functions * 2); //array containing the ranges for each function
	double delta = stod(argv[8]); //the scaling factor
	int algorithm_id = util->get_algorithm_id();
	static random_device rd;
	static mt19937 mt_rand(rd()); // create a new std::mt19937 object(mersene twister pseudo-random generator) and seed it with clock(), 
	util->simulate(num_dimensions, dimensions, num_functions, ranges, sample_size, algorithm_id, delta, mt_rand);
	delete util;


        return 0;
}

