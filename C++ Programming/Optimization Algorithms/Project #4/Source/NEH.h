/*********************************************
*                                            *
* NEH.h                                      *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #5                                 *
* Last modified on Wednesday May 22nd, 2019  *
*                                            *
*********************************************/

#include <iostream>
#include "matrix.h"
#include "functions.h"
#include "string.h"
/**
 * compute the total processing time for each job 
 *
 * @param P_times: a matrix containing the processing times for each job on each machine
 * @param tpts: a vector of integers to hold the computed total processing times for each job
 *
 * @return : None
 */
void compute_tpts(const matrix *P_times, int *tpts);

/**
 * to be used by the qsort function to compare two rows of the matrix
 *
 * @param P1, P2: 2 rows of the matrix
 *
 * @return :1 for P1 > P2, -1 for P1 < P2, 0 for P1 == P2
 */
int cmp ( const void* P1, const void* P2 );

/**
 * Sort the columns of a matrix based on their total processing time (column with the smallest total processing time be at right end)
 *
 * @param m: the matrix to be sorted
 * @param tfts: an array of integers containing the total processing time for each column of the matrix to be sorted
 *
 * @return : None
 */
void sort(matrix* m, int *tpts);

/*
 * find a subsequence of matrix m by moving one column from index old_col to another index new_col (new_col < old_col) and shifting the other columns (between new_col and old_col) to the  right by one
 *
 * @param m: the original matrix
 * @param subseq: a matrix to hold the subsequence
 * @params best_seq, tmp_seq: vectors of integers to hold the best jobs ordering and the temporary jobs ordering
 * @param old_col, new_col: integers for the old and new index of the column to be moved
 *
 * @return : None
 */
void create_subseq(const matrix *m, matrix *subseq, const int *best_seq, int *tmp_seq, const int old_col, const int new_col);


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
void NEH(const matrix *P_times, int *best_seq, int *num_func_calls, int func_id);
