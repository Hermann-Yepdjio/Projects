/********************************************
*                                           *
* main.cpp                                  *
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #4                                *
* Last modified on Wednesday May 20, 2019   *
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

	int dim = stoi(argv[2]);
	int ns = stoi(argv[3]); //mumber of rows for the matrix
	int num_gen = stoi(argv[4]);
	int num_exp = stoi(argv[5]);
	double c1 = stod(argv[6]);
	double c2 = stod(argv[7]);
	double k = stod(argv[8]);
	double gamma = stod(argv[9]);
	double B0 = stod(argv[10]);
	double alpha = stod(argv[11]);
	double HMCR = stod(argv[12]);
	double PAR = stod(argv[13]);
	double bw = stod(argv[14]);
	int num_functions = stoi(argv[15]); // number of functions to be tested
	char* delimiter = argv[16]; //delimiter used to separate tokens in strings that need to be splitted

	char* ranges_string = argv[17]; //string containing the ranges for each function
	double* ranges = util->str_to_tok(ranges_string, delimiter, num_functions * 2); //array containing the ranges for each function

	int algorithm_id = util->get_algorithm_id();

	static random_device rd;
	static mt19937 mt_rand(rd()); // create a new std::mt19937 object(mersene twister pseudo-random generator) and seed it with rd(),
	
	util->simulate(dim, ns, num_functions, ranges, algorithm_id, num_gen, num_exp, c1, c2, k, gamma, B0, alpha, HMCR, PAR, bw, mt_rand);
	delete util;


        return 0;
}

