/******************************************
*					  *
* functions.h				  *
* By Hermann Yepdjio			  *
* SID: 40917845				  *
* CS 471 Optimazation			  *
* Project #1				  *
* Last modified on Monday April 1, 2018   *
*					  *
******************************************/



class functions
{
		
	public:

	/*
	 *
	 * Schwefel's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Schwefel's function
	 * 
	 */
	double Schwefel(double* X, int dimension);
	
	/*
	 *
	 * 1st De Jong's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of 1st De Jong's function
	 * 
	 */
	double first_De_Jong(double* X, int dimension);
	
	/*
	 *
	 * Rosenbrock's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Rosenbrock's function
	 * 
	 */
	double Rosenbrock(double* X, int dimension);
	
	/*
	 *
	 * Rastrigin's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Rastrigin's function
	 * 
	 */
	double Rastrigin(double* X, int dimension);
	
	/*
	 *
	 * Greiwangk's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Greiwangk's function
	 * 
	 */
	double Greiwangk(double* X, int dimension);
	
	/*
	 *
	 * Sine Envelope Sine Wave's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Sine Envelope Sine Wave's function
	 * 
	 */
	double Sine_Envelope_Sine_Wave(double* X,  int dimension);

	/*
	 *
	 * Stretched V Sine Wave's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Stretched V Since Wave's function
	 * 
	 */
	double Stretched_V_Sine_Wave(double* X, int dimension);
	
	/*
	 *
	 * Ackley's One function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Ackley's One function
	 * 
	 */
	double Ackley_One(double* X ,int dimension);

	/*
	 *
	 * Ackley's Two function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Ackley's Twofunction
	 * 
	 */
	double Ackley_Two(double* X, int dimension);
	
	/*
	 *
	 * Egg Holder's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Egg Holder's function
	 * 
	 */
	double Egg_Holder(double* X, int dimension);
	
	/*
	 *
	 * Rana's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Rana's function
	 * 
	 */
	double Rana(double* X, int dimension);
	
	/*
	 *
	 * Pathological's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Pathological's function
	 * 
	 */
	double Pathological(double* X, int dimension);
	
	/*
	 *
	 * Michalewicz's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Michalewicz's function
	 * 
	 */
	double Michalewicz(double* X, int dimension);
	
	/*
	 *
	 * Masters Cosine Wave's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : Masters Cosine Wave's function
	 * 
	 */
	double Masters_Cosine_Wave(double* X, int dimension);
	
	/*
	 *
	 * Quartic's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Quartic's function
	 * 
	 */
	double Quartic(double* X, int dimension);

	/*
	 *
	 * Levy's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Levy's function
	 * 
	 */
	double Levy(double* X, int dimension);
		
	/*
	 *
	 * Step's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Step's function
	 * 
	 */
	double Step(double* X, int dimension);

	/*
	 *
	 * Alpine's function
	 *
	 * @param X: the input space
	 * @param dimension: the size of the input space
	 *
	 * @return : result of Alpine's function
	 * 
	 */
	double Alpine(double* X, int dimension);

};

