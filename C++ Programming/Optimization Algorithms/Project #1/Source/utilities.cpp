/******************************************
*                                         *
* utilities.cpp                           *
* By Hermann Yepdjio                      *
* SID: 40917845                           *
* CS 471 Optimazation                     *
* Project #1                              *
* Last modified on Monday April 1, 2018   *
*                                         *
******************************************/



#include <iostream>
#include <fstream>
#include "matrix.h"
#include "functions.h"
#include <cstring>
#include "utilities.h"
#include <chrono>
#include <time.h>

using namespace std;
using namespace std::chrono;

/*
 * split a string into double tokens
 *
 * @param string: the string to be splitted
 * @param delim: the character that separates the tokens in the string
 * @param num_tokens: number of tokens to expect
 *
 * @return : an array of doubles
 *
 */

double* utilities::str_to_tok(char* string, char* delim, int num_tokens)
{
	double* tokens = new double[num_tokens];
	char* token= strtok(string, delim); //returns first token
	int index = 0;

	//keep reading tokens until there is no more left or index reaches the number of dimensions we want to test the functions on
	while(token != NULL and index < num_tokens) //
	{
		tokens[index] = stod(token);
		token = strtok(NULL, delim);
		index++;	
	}

	return tokens;


}

/*
 * write a 2d array to a csv file
 *
 * @param mat: a matrix containing the elements to write to the csv file
 * @param file_name: the name of the file where data will be saved
 *
 * @return : None
 */
void utilities::write_to_file(matrix* mat, string file_name)
{
	ofstream file_writer;
	file_writer.open(file_name);
	for(int i = 0; i < mat->num_rows; i++)
	{
		for(int j = 0; j < mat->num_columns; j++)
		{
			if( j < mat->num_columns - 1)
				file_writer << mat->mat[i][j] << ",";
			else
				file_writer << mat->mat[i][j] << "\n";
		}
	}

	file_writer.close();

}

/*
 * simulate all the functions
 *
 * @param num_dimensions: the numbers of dimensions to be simulated
 * @param dimensions: an array containing the different dimensions to be simulated
 * @param num_functions: the number of functions to be simulated
 * @param ranges: an array containing values for the range of each function
 * @param sample_size: the size of the sample space
 *
 * @return : a 2d array containg all the results of the simulation
 *
 */
void utilities::simulate(int num_dimensions, double* dimensions, int num_functions, double* ranges, int sample_size)
{
	matrix* result = new matrix(sample_size, num_functions * num_dimensions); //matrix that will hold all the results for all the calculations
	functions* func = new functions();
	int index, count, func_counter; //index is used to find the correct bound, count is used to know on which total_time should be inserted in the matrix
	matrix* run_times = new matrix(num_functions, num_dimensions); //To hold the run time for each function for each specific dimension
	double fitness, elapsed, total_time;
	matrix* tmp_matrix;
	clock_t start;

	//runs all the functions for each number of dimensions
	for (int i = 0; i < num_dimensions; i++)
	{
		index = 0; //to know where the lowest and highest bound for each function is located in the vector
		count = 0; //to know in which row of the matrix, the running time for each function must be inserted
		func_counter = 0; //to know if a function must be run or not

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Schwefel-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
			
				start = clock();
				fitness = func->Schwefel(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 0] = fitness; //store the fitness in the result matrix
				cout<<"\nSchefel(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting first_De_Jong-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->first_De_Jong(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 1] = fitness; //store the fitness in the result matrix
				cout<<"\nfirst_De_Jong(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Rosenbrock-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Rosenbrock(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 2] = fitness; //store the fitness in the result matrix
				cout<<"\nRosenbrock(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}


		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Rastrigin-------------------------------------------\n\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Rastrigin(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 3] = fitness; //store the fitness in the result matrix
				cout<<"\nRastrigin(dimension = " << dimensions[i] << ", sample #" << j  + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Greiwangk-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Greiwangk(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 4] = fitness; //store the fitness in the result matrix
				cout<<"\nGreiwangk(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Sine_Envelope_Sine_Wave-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Sine_Envelope_Sine_Wave(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 5] = fitness; //store the fitness in the result matrix
				cout<<"\nSine_Envelope_Sine_Wave(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Stretched_V_Sine_Wave-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Stretched_V_Sine_Wave(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 6] = fitness; //store the fitness in the result matrix
				cout<<"\nStretched_V_Sine_Wave(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Ackley_One-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Ackley_One(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 7] = fitness; //store the fitness in the result matrix
				cout<<"\nAckley_One(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Ackley_Two-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Ackley_Two(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 8] = fitness; //store the fitness in the result matrix
				cout<<"\nAckley_Two(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Egg_Holder-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Egg_Holder(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 9] = fitness; //store the fitness in the result matrix
				cout<<"\nEgg_Holder(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Rana-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Rana(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 10] = fitness; //store the fitness in the result matrix
				cout<<"\nRana(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n-------------------------------Starting Pathological-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Pathological(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 11] = fitness; //store the fitness in the result matrix
				cout<<"\nPathological(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Michalewicz-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Michalewicz(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 12] = fitness; //store the fitness in the result matrix
				cout<<"\nMichalewicz(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Masters_Cosine_Wave-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Masters_Cosine_Wave(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 13] = fitness; //store the fitness in the result matrix
				cout<<"\nMasters_Cosine_Wave(dimension = " << dimensions[i] << ", sample #" << j + 1<< ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Quartic-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Quartic(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 14] = fitness; //store the fitness in the result matrix
				cout<<"\nQuartic(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Levy-------------------------------------------\n";
			for(int j = 0; j < sample_size-1; j++)
			{
				start = clock();
				fitness = func->Levy(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 15] = fitness; //store the fitness in the result matrix
				cout<<"\nLevy(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Step-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Step(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 16] = fitness; //store the fitness in the result matrix
				cout<<"\nStep(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}

		if(func_counter < num_functions)
		{
			func_counter++;
			tmp_matrix = new matrix(sample_size, dimensions[i], ranges[index++], ranges[index++]);
			total_time = 0;
			cout << "\n\n-------------------------------Starting Alpine-------------------------------------------\n";
			for(int j = 0; j < sample_size; j++)
			{
				start = clock();
				fitness = func->Alpine(tmp_matrix->mat[j], (int)(dimensions[i])); //compute the fitness for one element in the sample
				elapsed = 1000.0 * ((double)clock() - (double)(start)) / CLOCKS_PER_SEC ;
				result->mat[j][i + num_dimensions * 17] = fitness; //store the fitness in the result matrix
				cout<<"\nAlpine(dimension = " << dimensions[i] << ", sample #" << j + 1 << ") = " << fitness << ". time taken: " << elapsed << " ms." ; //print to standard output
				total_time += elapsed;
			}
			run_times->mat[count][i] = total_time;
			count++;
			delete tmp_matrix;
		}


	}
	delete func;
	write_to_file(result, "../Results/fitness.csv");
	write_to_file(run_times, "../Results/run_times.csv");
}
