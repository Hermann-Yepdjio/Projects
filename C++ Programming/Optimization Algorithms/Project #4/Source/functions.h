/*********************************************
*					     *
* functions.h				     *
* By Hermann Yepdjio			     *
* SID: 40917845				     *
* CS 471 Optimization			     *
* Project #5				     *
* Last modified on Wednesday May 22nd, 2019  *
*				   	     *
*********************************************/

/**
 * Compute the Completion times for each job on each machine using the Flow Shop Scheduling algorithm
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 *
 * @return makespan: an integer for the makespan
 */

int FSS(const matrix *P_time);

matrix* FSS_2(const matrix *P_time);


/**
 * Compute the Completion times for each job on each machine using the Flow Shop Scheduling with Blocking algorithm
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 *
 * @return makespan: an integer for the makespan
 */
int FSSB(const matrix *P_time);

matrix* FSSB_2(const matrix *P_time);

/**
 * Compute the Completion times for each job on each machine using the Flow Shop Scheduling with no Wait algorithm
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 *
 * @return tot: an integer for the total flow time
 */
int FSSNW(const matrix *P_time);

matrix* FSSNW_2(const matrix *P_time);

/**
 * Compute the starting times for each job on each machine 
 *
 * @param P_time: a matrix containing the precessing times for each job on each machine
 * @param C_time: a matrix containing the completion times for each job on each machine
 *
 * @return S_time: a matrix containing the start times for each job on each machine
 */
matrix* compute_start_times(const matrix *P_time, const matrix *C_time);

