/*********************************************
*                                            *
* utilities.h                                *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #5                                 *
* Last modified on Wednesday May 27th, 2019  *
*                                            *
*********************************************/


#include <fstream>
#include <time.h>
#include <glob.h>
#include "NEH.h"
	


/**
 * write a 2d array to a csv file
 *
 * @param mat: a matrix containing the elements to write to the csv file
 * @param file_name: the name of the file where data will be saved
 *
 * @return : None
 */
void write_to_file(matrix* mat, string file_name);


/**
 * set job numbers from 1 to number of jobs for each row
 *
 * @param m: a matrix containing a set of jobs orderings
 *
 * @return : None
 */ 
void init_jobs(matrix *m);


/**
 * read data from a text file a create a matrix containing the processing times for each job on each machine
 *
 * @param filename: a string for the name of the file to read from
 *
 * @return : a  matrix containing the processing times for each job on each machine
 */
matrix* get_P_times(string file_name);

/**
 * compute the makespan or total flow time for a specific job ordering
 *
 * @param :None
 *
 * @return : None
 */
void compute_cmax();

/**
 * similute the 3 flow shop scheduling algorithm
 * 
 *@param index: an integer for the index of the algorithm to be run (0 for FSS, 1 for FSSB and 2 for FSSNW)
 *
 * @return :None
 */
void* simulate(void *index);




