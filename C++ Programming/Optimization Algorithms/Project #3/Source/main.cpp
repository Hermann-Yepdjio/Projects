/********************************************
*                                           *
* main.cpp                                  *
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #3                                *
* Last modified on Wednesday May 1st, 2019  *
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
	int num_trnmnt = stoi(argv[6]);
	double cr = stod(argv[7]);
	double er = stod(argv[8]);
	double m_range = stod(argv[9]);
	double m_rate = stod(argv[10]);
	double m_precision = stod(argv[11]);
	double F = stod(argv[12]);
	double lambda = stod(argv[13]);
	int num_functions = stoi(argv[14]); // number of functions to be tested
	char* delimiter = argv[15]; //delimiter used to separate tokens in strings that need to be splitted

	char* ranges_string = argv[16]; //string containing the ranges for each function
	double* ranges = util->str_to_tok(ranges_string, delimiter, num_functions * 2); //array containing the ranges for each function

	int algorithm_id = util->get_algorithm_id();
	int selection_id = 0;
	if (algorithm_id == 1 )
		selection_id = util->get_selection_id();
	static random_device rd;
	static mt19937 mt_rand(rd()); // create a new std::mt19937 object(mersene twister pseudo-random generator) and seed it with clock(), 
	util->simulate(dim, ns, num_functions, ranges, algorithm_id, selection_id, num_gen, num_exp, num_trnmnt, cr, er, m_range, m_rate, m_precision, F, lambda, mt_rand);
	delete util;


        return 0;
}

