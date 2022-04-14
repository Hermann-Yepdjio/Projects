/********************************************
*                                           *
* matrix.h                                  *
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #5                                *
* Last modified on Wednesday May 22nd, 2019 *
*                                           *
********************************************/	
using namespace std;
#include <random>

class matrix
{
	
	public:	

	int num_rows; //number of rows/vectors in the matrix
        int num_columns; //number of columns in the matrix
	int *numbers; //vector of integers containing the values to be inserted into the matrix	
        int** mat; //the matrix object itself 


	/**
	 * generate an empty matrix 
	 * 
	 * @param num_rows: integer respresenting the number of rows in the matrix
	 * @param dim: integer representing the dimension or number of columns in the matrix
	 * @param numbers: a vector of integers for the numbers to be inserted into the matrix
	 *
	 * @return : an empty matrix 
	 */
	matrix(int num_rows, int num_columns, int *numbers);
	
	/**
	 * generate an empty matrix 
	 * 
	 * @param num_rows: integer respresenting the number of rows in the matrix
	 * @param dim: integer representing the dimension or number of columns in the matrix
	 *
	 * @return : an empty matrix 
	 */
	matrix(int num_rows, int num_columns);

	/**
	 * Copy constructor
	 *
	 *@param m: the matrix to be copied
	 *
	 *@return : a matrix which is a copy of matrix m
	 */
	matrix(const matrix &m);

	/**
	 * print a matrix to the standart output (on the screen)
	 *
	 *@return : None
	 */
	void print();

	//destructor 
	~matrix();
	
};
