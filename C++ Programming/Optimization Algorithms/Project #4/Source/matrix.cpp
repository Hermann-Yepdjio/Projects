/********************************************
*                                           *
* matrix.cpp                                *
* By Hermann Yepdjio                        *
* SID: 40917845				    *
* CS 471 Optimization                       *
* Project #5                                *
* Last modified on Wednesday May 22nd, 2019 *
*                                           *
********************************************/


#include <iostream>
#include <cstring>
#include <random>
#include "matrix.h"


using namespace std;


/**
 * generate an empty matrix 
 * 
 * @param num_rows: integer respresenting the number of rows in the matrix
 * @param dim: integer representing the dimension or number of columns in the matrix
 * @param numbers: a vector of integers for the numbers to be inserted into the matrix
 *
 * @return : an empty matrix 
 */
matrix::matrix(int num_rows, int num_columns, int *numbers): num_rows(num_rows), num_columns(num_columns), numbers(numbers)
{
	mat = new int*[num_rows];

	if(!mat)
		cerr<<"Memory Allocation failed!"<<endl;
	int count = 0;
	for(int i = 0; i < num_rows; i++)
	{
		mat[i] = new int[num_columns]();
		if(!mat[i])
			cerr<<"Memory Allocation failed!"<<endl;
		for (int j = 0; j < num_columns; j++)
		{
			mat[i][j] = numbers[count];
			count++;
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
matrix::matrix(int num_rows, int num_columns): num_rows(num_rows), num_columns(num_columns), numbers(NULL)
{
	mat = new int*[num_rows];

	if(!mat)
		cerr<<"Memory Allocation failed!"<<endl;
	for(int i = 0; i < num_rows; i++)
	{
		mat[i] = new int[num_columns]();
		if(!mat[i])
			cerr<<"Memory Allocation failed!"<<endl;
	
	}
}

/**
 * Copy constructor
 *
 *@param m: the matrix to be copied
 *
 *@return : a matrix which is a copy of matrix m
 */
matrix::matrix(const matrix &m): num_rows(m.num_rows), num_columns(m.num_columns)
{
	mat = new int*[num_rows];

	if(!mat)
		cerr<<"Memory Allocation failed!"<<endl;
	for(int i = 0; i < num_rows; i++)
	{
		mat[i] = new int[num_columns]();
		if(!mat[i])
			cerr<<"Memory Allocation failed!"<<endl;
	
	}

	for(int i = 0; i < m.num_rows; i++)
		memcpy(mat[i], m.mat[i], sizeof(int) * m.num_columns);
}

/**
 * print a matrix to the standart output (on the screen)
 *
 *@return : None
 */
void matrix::print()
{
	for(int i =0; i < num_rows; i++)
	{
		for(int j = 0; j < num_columns; j++)
		{
			cout << mat[i][j] << "  ";
		}
		cout << endl;
	}
	cout << endl;
}


//destructor 
matrix::~matrix()
{
	for (int i = 0; i < num_rows; i++)
		delete[] mat[i];
	delete[] mat;
}	
		


