/*********************************************
*                                            *
* NEH.cpp                                    *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #5                                 *
* Last modified on Wednesday May 22nd, 2019  *
*                                            *
*********************************************/

/*#include <iostream>
#include "matrix.h"
#include "functions.h"
#include "string.h"*/
#include "NEH.h"
#include <cmath>
#include <omp.h>

using namespace std;


/**
 * compute the total processing time for each job 
 *
 * @param P_times: a matrix containing the processing times for each job on each machine
 * @param tpts: a vector of integers to hold the computed total processing times for each job
 *
 * @return : None
 */
void compute_tpts(const matrix *P_times, int *tpts)
{
	int sum = 0;
	for(int j = 0; j < P_times->num_columns; j++)
	{
		for( int i = 0; i < P_times->num_rows; i++)
			sum += P_times->mat[i][j];
		tpts[j] = sum;
		sum = 0;
	}

}

/**
 * to be used by the qsort function to compare two rows of the matrix
 *
 * @param P1, P2: 2 rows of the matrix
 *
 * @return :1 for P1 > P2, -1 for P1 < P2, 0 for P1 == P2
 */
int cmp ( const void* P1, const void* P2 )
{
	const int(*S1) = *(const int **)P1;
	const int(*S2) = *(const int **)P2;
        if (S1[1] < S2[1])
		return 1;
	else if (S1[1] > S2[1])
	       	return -1;
        return 0;
}

/**
 * Sort the columns of a matrix based on their total processing time (column with the smallest total processing time be at right end)
 *
 * @param m: the matrix to be sorted
 * @param tfts: an array of integers containing the total processing time for each column of the matrix to be sorted
 *
 * @return : None
 */
void sort(matrix* m, int *tpts)
{
	int NS = m->num_rows, dim = m->num_columns;
	matrix* sorted_tpts = new matrix(dim, 2);
	matrix* tmp_mat = new matrix(NS, dim);

        for(int i = 0; i < dim; i++)
        {
                sorted_tpts->mat[i][0] = i;
                sorted_tpts->mat[i][1] = tpts[i];
        }

	qsort(sorted_tpts->mat, dim, sizeof(sorted_tpts->mat[0]), cmp);
	for(int j = 0; j < dim; j++)
	{
		for(int i = 0; i < NS; i++)
		{
			tmp_mat->mat[i][j] = m->mat[i][(int)sorted_tpts->mat[j][0]];
		}
		tpts[j] = sorted_tpts->mat[j][1];
	}
	
	for(int i = 0; i < NS; i++)
		memcpy(m->mat[i], tmp_mat->mat[i], sizeof(int) * dim); //copy rows of tmp_mat to m keeping the same order

	delete sorted_tpts;
	delete tmp_mat;

}

/*
 * find a subsequence of matrix m by moving one column from index old_col to another index new_col (new_col < old_col) and shifting the other columns (between new_col and old_col) to the  right by one
 *
 * @param m: the original matrix
 * @param subseq: a matrix to hold the subsequence
 * @params best_seq, tmp_seq: vectors of integers to hold the best jobs ordering and the temporary jobs ordering
 * @params old_col, new_col: integers for the old and new index of the column to be moved
 *
 * @return : None
 */
void create_subseq(const matrix *m, matrix *subseq, const int *best_seq, int *tmp_seq, const int old_col, const int new_col)
{
	for(int i = 0; i < m->num_rows; i++)
	{


		for(int j = 0; j < new_col; j++)
			subseq->mat[i][j] = m->mat[i][j];

		subseq->mat[i][new_col] = m->mat[i][old_col];
		for(int j = new_col + 1; j < m->num_columns; j++)
		{

			if (j < old_col + 1)
				subseq->mat[i][j] = m->mat[i][j - 1];
			else
				subseq->mat[i][j] = m->mat[i][j];
		}



	}

	for(int j = 0; j < new_col; j++)
		tmp_seq[j] = best_seq[j];

	tmp_seq[new_col] = best_seq[old_col];
	for(int j = new_col + 1; j < m->num_columns; j++)
	{
		if (j < old_col + 1)
			tmp_seq[j] = best_seq[j - 1];
		else
			tmp_seq[j] = best_seq[j];
	}
}

int(*functions_ptr[])(const matrix*) = {&FSS, &FSSB, &FSSNW};


/**
 * finds the ordering of jobs that will produce the lowest total flow time or makespan
 *
 * @param P_times: a matrix containing the processing time for each job on each machine
 * @param best_seq: a vector of integers to hold the best sequence of jobs
 * @param num_func_calls: a vector of size 3 to hold the number of function class for the 3 objective functions
 * @param func_id: an integer for the index of the objective function to use to compute the makespan or the total flow time
 *
 * @return : None
 */
void NEH(const matrix *P_times, int *best_seq, int *num_func_calls, int func_id)
{
	omp_lock_t writelock;
	omp_init_lock(&writelock);

	matrix *tmp = new matrix(*P_times);
	matrix *global_best_sol = new matrix(*P_times);

	int *tpts = new int[P_times->num_columns]();
	compute_tpts(P_times, tpts);
	sort(tmp, tpts);

	omp_set_dynamic(0);     // Explicitly disable dynamic teams
	omp_set_num_threads(8); // Use 8 threads for all consecutive parallel regions
	#pragma omp parallel for 
	for(int i = 1; i < P_times->num_columns; i++)
	{
		matrix *tmp_sol = new matrix(*tmp);
		matrix *local_best_sol = new matrix(*tmp);
		int *tmp_seq = new int[P_times->num_columns]();
		int tmp_1, tmp_2, tmp_3;

		for(int j = i - 1; j >= 0; j--)
		{
			create_subseq(local_best_sol,	tmp_sol, best_seq, tmp_seq, i, j);
			tmp_1 = functions_ptr[func_id](tmp_sol); 
			tmp_2 = functions_ptr[func_id](local_best_sol);
			num_func_calls[func_id] += 2;
			if(tmp_1 < tmp_2)
			{
				delete local_best_sol;
				local_best_sol = new matrix(*tmp_sol);
				tmp_2 = tmp_1;
			}

			omp_set_lock(&writelock);
			tmp_3 = functions_ptr[func_id](global_best_sol);
			num_func_calls[func_id] += 1;

			
			if(tmp_2 < tmp_3)
			{
				delete global_best_sol;
				global_best_sol = new matrix(*tmp_sol);
				memcpy(best_seq, tmp_seq, sizeof(int) * P_times->num_columns);
				best_seq[P_times->num_columns + 1] = tmp_1;
			}
			else if(tmp_3 < tmp_2)
			{
				delete local_best_sol;
                                local_best_sol = new matrix(*global_best_sol);
				tmp_2 = tmp_3;
			}
			omp_unset_lock(&writelock);
		}
		delete local_best_sol;
		delete tmp_sol;
		delete[] tmp_seq;
	}
	#pragma omp barrier //master waits here until all threads finish before deleting 
	omp_destroy_lock(&writelock);
	delete global_best_sol;
	delete tmp;
	delete[] tpts;

}
