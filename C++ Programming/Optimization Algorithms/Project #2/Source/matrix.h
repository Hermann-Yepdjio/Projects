/********************************************
*                                           *
* matrix.h                                  *
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #2                                *
* Last modified on Wednesday April 17, 2019 *
*                                           *
********************************************/	
using namespace std;
#include <random>

class matrix
{
	
	public:	

	const int num_rows; //number of rows/vectors in the matrix
        const int num_columns; //number of columns in the matrix
        const int l_b, h_b; //lowest and highest bound: range to search for random number
	mt19937 mt_rand; //the seed for the Mersen Twister Random Generator	
        double** mat; //the matrix object itself 

	/*
         * generate an empty matrix and fill it up with randomly generated numbers within some range
         * 
         * @param num_rows: integer respresenting the number of rows in the matrix
         * @param dim: integer representing the dimension or number of columns in the matrix
         * @param l_b: double representing the lowest bound for the random generator
         * @param h_b: double representing the highest bound for the random generator
         *
         * @return : a matrix of randomly generated numbers
         */
        matrix(int num_rows, int num_columns, int l_b, int h_b, mt19937& mt_rand);
	
	/*
	 * generate an empty matrix 
	 * 
	 * @param num_rows: integer respresenting the number of rows in the matrix
	 * @param dim: integer representing the dimension or number of columns in the matrix
	 *
	 * @return : an empty matrix 
	 */
	matrix(int num_rows, int num_columns);
	

        

        //destructor 
        ~matrix();	
	
	
};
