/*********************************************
*                                            *
* search_functions.h                         *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #2                                 *
* Last modified on Wednesday April 17, 2019  *
*                                            *
*********************************************/

#include <random>
using namespace std;

//to hold special inputs for random walk
typedef struct rw_input
{
        int iterations;
        double* best_solution;
        double best_fitness;

	~rw_input()
        {
                if (best_solution)
                        delete[] best_solution;
                best_solution = nullptr;
        }


}rw_input;





/*
 * generate a neighborhood of solution spaces for the initial solution
 *
 * @param input: struct containing the best solution so far
 * @param neighbor: the neighbor of the best solution ( To be found)
 * @param function_id: the id of the function to run in order to get the fitness ( one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 * @param delta: the scaling factor 
 *
 * @return : no return
 *
 */
int  generate_neighbor(rw_input* input, double* neighbor, int function_id, int dimension, double l_b, double r_b, double delta);

/*
 * simulate the blind algorithm (Random Walk)
 *
 * @param input: struct containing the number of iterations, best solution found so far and its fitness
 * @param function_id: the id of the function to used to compute the fitness(one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 *
 * @return : no return
 *
 */
void random_walk(rw_input *input, int function_id, int dimension, double l_b, double r_b, mt19937& mt_rand);

/*
 * simulate the local search algorithm
 *
 * @param input: struct containing the initial solution, the best solution found so far and its fitness
 * @param function_id: the id of the function to used to compute the fitness(one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 * @param delta: double for the  scaling factor
 *
 * @return : no return
 *
 */
void local_search(rw_input *input, int function_id, int dimension, double l_b, double r_b, double delta, mt19937& mt_rand);

/*
 * simulate the iterative local search algorithm
 * @param input: struct containing the number of iterations, the best solution found so far and its fitness
 * @param function_id: the id of the function to used to compute the fitness(one of the 18 functions)
 * @param dimension: the dimension for the solution space
 * @param l_b, rb: left and right bounds for the possible values of the solution space
 * @param delta: a double for the  scaling factor
 *
 * @return : no return
 *
 */
void iter_local_search(rw_input *input, int function_id, int dimension, double l_b, double r_b, double delta, mt19937& mt_rand);
