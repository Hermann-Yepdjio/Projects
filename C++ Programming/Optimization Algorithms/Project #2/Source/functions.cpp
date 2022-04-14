/*********************************************
*					     *
* functions.cpp				     *
* By Hermann Yepdjio			     *
* SID: 40917845				     *
* CS 471 Optimization			     *
* Project #2				     *
* Last modified on Wednesday April 17, 2019  *
*				   	     *
*********************************************/

#include <iostream>
#include "functions.h"
#include <cmath>

using namespace std;


		

/**
 *
 * Schwefel's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Schwefel's function
 * 
 */
double functions::Schwefel(double* X, int dimension)
{
	double sum = 0;

	for(int i =0; i < dimension; i++)
		sum += -1 * X[i]*sin(sqrt(abs(X[i])));	

	return 418.9829 * dimension -  sum;
}

/**
 *
 * 1st De Jong's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of 1st De Jong's function
 * 
 */
double functions::first_De_Jong(double* X, int dimension)
{
	double sum = 0;

	for(int i =0; i < dimension; i++)
		sum += pow(X[i], 2);

	return sum;
}

/**
 *
 * Rosenbrock's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Rosenbrock's function
 * 
 */
double functions::Rosenbrock(double* X, int dimension)
{
	double sum = 0;

	for(int i =0; i < dimension - 1; i++)
		sum += 100 * pow((pow(X[i], 2) - X[i + 1]), 2) + pow((1 - X[i]), 2);

	return sum;
}

/**
 *
 * Rastrigin's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Rastrigin's function
 * 
 */
double functions::Rastrigin(double* X, int dimension)
{
	double sum = 0;

	for(int i =0; i < dimension; i++)
		sum += pow(X[i], 2) - 10 * cos(2 * M_PI * X[i]);	

	return 10 * dimension * sum;
}

/**
 *
 * Greiwangk's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Greiwangk's function
 * 
 */
double functions::Greiwangk(double* X, int dimension)
{
	double sum = 0, prod = 0;

	for(int i =0; i < dimension; i++)
		sum += pow(X[i], 2) / 4000 ;

	for(int i = 0; i < dimension; i++)
		prod *= cos(X[i] / sqrt(i + 1));

	return 1 + sum - prod; 
}

/**
 *
 * Sine Envelope Sine Wave's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Sine Envelope Sine Wave's function
 * 
 */
double functions::Sine_Envelope_Sine_Wave(double* X, int dimension)
{
	double sum = 0;

	for(int i =0; i < dimension - 1; i++)
		sum += 0.5 + sin(pow((pow(X[i],2) + pow(X[i + 1], 2) - 0.5), 2)) / pow((1 + 0.001 * (pow(X[i], 2) + pow(X[i + 1], 2))), 2);

	return -1 * sum;
}

/**
 *
 * Stretched V Sine Wave's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Stretched V Since Wave's function
 * 
 */
double functions::Stretched_V_Sine_Wave(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += pow((pow(X[i], 2) + pow(X[i + 1], 2)), 1/4) * sin(pow((50 * pow((pow(X[i], 2) + pow(X[i + 1], 2)), 1/10 )), 2)) + 1;

	return sum; 
}

/**
 *
 * Ackley's One function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Ackley's One function
 * 
 */
double functions::Ackley_One(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += (1 / exp(0.2)) * sqrt(pow(X[i], 2) + pow(X[i + 1], 2)) + 3 * cos(2 * X[i]) * sin(2 * X[i + 1]);

	return sum; 
}

/**
 *
 * Ackley's Two function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Ackley's Twofunction
 * 
 */
double functions::Ackley_Two(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += (20 + M_E - 20 / exp(0.2 * sqrt((pow(X[i], 2) + pow(X[i + 1], 2) )/2))) - exp(0.5 *  (cos(2 * M_PI * X[i]) + cos(2 * M_PI * X[i + 1])));

	return sum; 
}

/**
 *
 * Egg Holder's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Egg Holder's function
 * 
 */
double functions::Egg_Holder(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum +=  -1 * X[i] * sin(sqrt(abs(X[i] - X[i + 1] -47))) - 1 * (X[i + 1] + 47) * sin(sqrt(abs(X[i + 1] + 47 + X[i] / 2)));

	return sum; 
}

/**
 *
 * Rana's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Rana's function
 * 
 */
double functions::Rana(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += X[i] * sin(sqrt(abs(X[i + 1] - X[i] + 1))) * cos(sqrt(abs(X[i + 1] + X[i] + 1))) + (X[i + 1] + 1) * cos(sqrt(abs(X[i + 1] - X[i] + 1))) * sin(sqrt(abs(X[i + 1] + X[i] + 1)));

	return sum; 
}

/**
 *
 * Pathological's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Pathological's function
 * 
 */
double functions::Pathological(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += 0.5 + (sin(pow(sqrt(100 * pow(X[i], 2) + pow(X[i +1], 2)), 2)) - 0.5) / (1 + 0.001 * pow((pow(X[i], 2) - 2 * X[i] * X[i +1] + pow(X[i + 1], 2)), 2));

	return sum; 
}

/**
 *
 * Michalewicz's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Michalewicz's function
 * 
 */
double functions::Michalewicz(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension; i++)
		sum += sin(X[i]) * pow(sin((i + 1) * pow(X[i], 2)/M_PI), 20);

	return -1 * sum; 
}

/**
 *
 * Masters Cosine Wave's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : Masters Cosine Wave's function
 * 
 */
double functions::Masters_Cosine_Wave(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += exp((-1 / 8) * (pow(X[i], 2) + pow(X[i + 1], 2) + 0.5 * X[i+1] * X[i])) * cos(pow((pow(X[i], 2) + pow(X[i + 1], 2) + 0.5 * X[i] * X[i + 1]), 1/4));

	return -1 * sum; 
}

/**
 *
 * Quartic's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Quartic's function
 * 
 */
double functions::Quartic(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension; i++)
		sum += (i + 1) * pow(X[i], 4);

	return sum; 
}

/**
 *
 * Levy's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Levy's function
 * 
 */
double functions::Levy(double* X, int dimension)
{
	double sum= 0;
	
	double w_1 = 1 + (X[0] - 1) / 4;
	double w_n = 1 + (X[dimension -1] - 1) / 4;
	for(int i =0; i < dimension - 1; i++)
	{
		double w_i = 1 + (X[i] - 1) / 4;
		sum += pow((w_i - 1), 2) * (1 + 10 * pow(sin(M_PI * w_i + 1), 2)) + pow((w_n - 1), 2) * (1 + pow(sin(2 * M_PI * w_n), 2)); 
	}

	return pow(sin(M_PI * w_1), 2) + sum;

}

/**
 *
 * Step's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Step's function
 * 
 */
double functions::Step(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += pow((abs(X[i]) + 0.5), 2);

	return sum; 
}

/**
 *
 * Alpine's function
 *
 * @param X: the input space
 * @param dimension: the size of the input space
 *
 * @return : result of Alpine's function
 * 
 */
double functions::Alpine(double* X, int dimension)
{
	double sum= 0;

	for(int i =0; i < dimension - 1; i++)
		sum += abs(X[i] * sin(X[i]) + 0.1 * X[i]);

	return sum; 
}











	

