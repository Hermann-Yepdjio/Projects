/********************************************
*                                           *
* matrix.cpp                                *
* By Hermann Yepdjio                        *
* SID: 40917845				    *
* CS 471 Optimization                       *
* Project #2                                *
* Last modified on Wednesday April 17, 2019  *
*                                           *
********************************************/


#include <iostream>
#include <cstring>
#include <random>
#include "matrix.h"


using namespace std;

/**
 * generate an empty matrix and fill it up with randomly generated numbers within some range
 * 
 * @param num_rows: integer respresenting the number of rows in the matrix
 * @param dim: integer representing the dimension or number of columns in the matrix
 * @param l_b: double representing the lowest bound for the random generator
 * @param h_b: double representing the highest bound for the random generator
 *
 * @return : a matrix of randomly generated numbers
 */
matrix::matrix(int num_rows, int num_columns, int l_b, int h_b,  mt19937& mt_rand): num_rows(num_rows), num_columns(num_columns), l_b(l_b), h_b(h_b), mt_rand(mt_rand)
{
	mat = new double*[num_rows];

	if(!mat)
		cerr<<"Memory Allocation failed!"<<endl;
	for(int i = 0; i < num_rows; i++)
	{
		mat[i] = new double[num_columns]();
		if(!mat[i])
			cerr<<"Memory Allocation failed!"<<endl;
	
	}
	//mt19937 mt_rand(clock()); // create a new std::mt19937 object(mersene twister pseudo-random generator) and seed it with clock()
	//uniform_real_distribution<double> rand_num(l_b, h_b); //filters the mt_rand output to generate pseudo-random double values (uniformly distributed within the interval [l_b, h_b]
	
	//fill the matrix with uniformly randomly generated numbers
	for(int i=0; i<num_rows; i++)
	{

		for(int j  = 0; j < num_columns; j++)
		{
			uniform_real_distribution<double> rand_num(l_b, h_b); //filters the mt_rand output to generate pseudo-random double values (uniformly distributed within the interval [l_b, h_b]
			mat[i][j] = rand_num(mt_rand);
		}
	}	
	

}

/**
 * generate an empty matrix 
 * 
 * @param num_rows: integer respresenting the number of rows in the matrix
 * @param dim: integer representing the dimension or number of columns in the matrix
 *
 * @return : an empty matrix 
 */
matrix::matrix(int num_rows, int num_columns): num_rows(num_rows), num_columns(num_columns), l_b(0), h_b(0)
{
	mat = new double*[num_rows];

	if(!mat)
		cerr<<"Memory Allocation failed!"<<endl;
	for(int i = 0; i < num_rows; i++)
	{
		mat[i] = new double[num_columns]();
		if(!mat[i])
			cerr<<"Memory Allocation failed!"<<endl;
	
	}
}

//destructor 
matrix::~matrix()
{
	for (int i = 0; i < num_rows; i++)
		delete[] mat[i];
	delete[] mat;
}	
		


