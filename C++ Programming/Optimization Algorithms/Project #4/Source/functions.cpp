/*********************************************
*					     *
* functions.cpp				     *
* By Hermann Yepdjio			     *
* SID: 40917845				     *
* CS 471 Optimization			     *
* Project #5				     *
* Last modified on Wednesday May 22nd, 2019  *
*				   	     *
*********************************************/

#include <iostream>
#include "matrix.h"
#include "functions.h"
#include <cmath>

using namespace std;

/**
 * Compute the Completion times for each job on each machine using the Flow Shop Scheduling algorithm
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 *
 * @return makespan: an integer for the makespan
 */

int FSS(const matrix *P_time)
{
	matrix *C_time = new matrix(P_time->num_rows, P_time->num_columns); // a matrix to hold the completion times for each job on each machine
	for(int i = 0; i < P_time->num_rows; i++)
	{
		for(int j = 0; j < P_time->num_columns; j++)
		{
			if(i == 0 && j == 0)
				C_time->mat[i][j] = P_time->mat[i][j];
			else if(j == 0)
				C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
			else if (i == 0)
				C_time->mat[i][j] = C_time->mat[i][j - 1] + P_time->mat[i][j];
			else if (C_time->mat[i][j-1] > C_time->mat[i - 1][j] )
				C_time->mat[i][j] = C_time->mat[i][j-1] + P_time->mat[i][j];
			else
				C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
		}
	}

	//C_time->print();
	int makespan =  C_time->mat[C_time->num_rows - 1][C_time->num_columns - 1];
	delete C_time;

	return makespan;
}

matrix* FSS_2(const matrix *P_time)
{
	matrix *C_time = new matrix(P_time->num_rows, P_time->num_columns); // a matrix to hold the completion times for each job on each machine
	for(int i = 0; i < P_time->num_rows; i++)
	{
		for(int j = 0; j < P_time->num_columns; j++)
		{
			if(i == 0 && j == 0)
				C_time->mat[i][j] = P_time->mat[i][j];
			else if(j == 0)
				C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
			else if (i == 0)
				C_time->mat[i][j] = C_time->mat[i][j - 1] + P_time->mat[i][j];
			else if (C_time->mat[i][j-1] > C_time->mat[i - 1][j] )
				C_time->mat[i][j] = C_time->mat[i][j-1] + P_time->mat[i][j];
			else
				C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
		}
	}

	//C_time->print();
	int makespan =  C_time->mat[C_time->num_rows - 1][C_time->num_columns - 1];
	return C_time;

	//return makespan;
}

/**
 * Compute the Completion times for each job on each machine using the Flow Shop Scheduling with Blocking algorithm
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 *
 * @return makespan: an integer for the makespan
 */
int FSSB(const matrix *P_time)
{
        matrix *C_time = new matrix(P_time->num_rows, P_time->num_columns); // a matrix to hold the completion times for each job on each machine
        for(int j = 0; j < P_time->num_columns; j++)
        {
                for(int i = 0; i < P_time->num_rows; i++)
                {
                        if(i == 0 && j == 0)
                                C_time->mat[i][j] = P_time->mat[i][j];
                        else if(j == 0)
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                        else if (i == 0)
			{
                                C_time->mat[i][j] = C_time->mat[i][j - 1] + P_time->mat[i][j];
				if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
					C_time->mat[i][j] = C_time->mat[i + 1][j - 1];
			}
                        else if (i < P_time->num_rows - 1 )
			{
                                C_time->mat[i][j] = C_time->mat[i -1][j] + P_time->mat[i][j];
				if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
                                        C_time->mat[i][j] = C_time->mat[i + 1][j - 1];
			}
                        else
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                }
        }

	//C_time->print();

	int makespan =  C_time->mat[C_time->num_rows - 1][C_time->num_columns - 1];
	delete C_time;

	return makespan;

}

matrix* FSSB_2(const matrix *P_time)
{
        matrix *C_time = new matrix(P_time->num_rows, P_time->num_columns); // a matrix to hold the completion times for each job on each machine
        for(int j = 0; j < P_time->num_columns; j++)
        {
                for(int i = 0; i < P_time->num_rows; i++)
                {
                        if(i == 0 && j == 0)
                                C_time->mat[i][j] = P_time->mat[i][j];
                        else if(j == 0)
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                        else if (i == 0)
			{
                                C_time->mat[i][j] = C_time->mat[i][j - 1] + P_time->mat[i][j];
				if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
					C_time->mat[i][j] = C_time->mat[i + 1][j - 1];
			}
                        else if (i < P_time->num_rows - 1 )
			{
                                C_time->mat[i][j] = C_time->mat[i -1][j] + P_time->mat[i][j];
				if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
                                        C_time->mat[i][j] = C_time->mat[i + 1][j - 1];
			}
                        else
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                }
        }

	//C_time->print();

	int makespan =  C_time->mat[C_time->num_rows - 1][C_time->num_columns - 1];
	return C_time;

	//return makespan;

}

/**
 * Compute the Completion times for each job on each machine using the Flow Shop Scheduling with no Wait algorithm
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 *
 * @return tot: an integer for the total flow time
 */
int FSSNW(const matrix *P_time)
{
        matrix *C_time = new matrix(P_time->num_rows, P_time->num_columns); // a matrix to hold the completion times for each job on each machine
	int tmp = 0;
        for(int j = 0; j < P_time->num_columns; j++)
        {
                for(int i = 0; i < P_time->num_rows; i++)
                {
                        if(i == 0 && j == 0)
                                C_time->mat[i][j] = P_time->mat[i][j];
                        else if(j == 0)
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                        else if (i == 0)
                        {
                                C_time->mat[i][j] = C_time->mat[i][j - 1] + P_time->mat[i][j];
                                if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
                                        C_time->mat[i][j] = C_time->mat[i + 1][j - 1];
                        }
                        else if (i < P_time->num_rows - 1 )
                        {
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                                if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
				{
					tmp = C_time->mat[i + 1][j - 1] - C_time->mat[i][j] ;
					for ( int k = i; k >= 0; k--)
                                        	C_time->mat[k][j] += tmp; 
				}
                        }
                        else
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                }
        }
	
	//C_time->print();
        int tot =  C_time->mat[C_time->num_rows - 1][C_time->num_columns - 1];
        delete C_time;

        return tot;

}

matrix* FSSNW_2(const matrix *P_time)
{
        matrix *C_time = new matrix(P_time->num_rows, P_time->num_columns); // a matrix to hold the completion times for each job on each machine
	int tmp = 0;
        for(int j = 0; j < P_time->num_columns; j++)
        {
                for(int i = 0; i < P_time->num_rows; i++)
                {
                        if(i == 0 && j == 0)
                                C_time->mat[i][j] = P_time->mat[i][j];
                        else if(j == 0)
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                        else if (i == 0)
                        {
                                C_time->mat[i][j] = C_time->mat[i][j - 1] + P_time->mat[i][j];
                                if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
                                        C_time->mat[i][j] = C_time->mat[i + 1][j - 1];
                        }
                        else if (i < P_time->num_rows - 1 )
                        {
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                                if(C_time->mat[i][j] < C_time->mat[i + 1][j - 1])
				{
					tmp = C_time->mat[i + 1][j - 1] - C_time->mat[i][j] ;
					for ( int k = i; k >= 0; k--)
                                        	C_time->mat[k][j] += tmp; 
				}
                        }
                        else
                                C_time->mat[i][j] = C_time->mat[i - 1][j] + P_time->mat[i][j];
                }
        }
	
	//C_time->print();
        int tot =  C_time->mat[C_time->num_rows - 1][C_time->num_columns - 1];
        return C_time;

        //return tot;

}

/**
 * Compute the starting times for each job on each machine 
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 * @param C_time: a matrix containing the completion times for each job on each machine
 *
 * @return S_time: a matrix containing the start times for each job on each machine
 */
matrix* compute_start_times(const matrix *P_time, const matrix *C_time)
{
	matrix *S_time = new matrix(P_time->num_rows, P_time->num_columns); //matrix to hold the starting time for each job on each machine
	for(int j = 0; j < P_time->num_columns; j++)
        {
                for(int i = 0; i < P_time->num_rows; i++)
                {
			S_time->mat[i][j] = C_time->mat[i][j] - P_time->mat[i][j];
		}
	}
	return S_time;
}

