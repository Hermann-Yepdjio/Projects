/*********************************************
*                                            *
* utilities.cpp                              *
* By Hermann Yepdjio                         *
* SID: 40917845                              *
* CS 471 Optimization                        *
* Project #5                                 *
* Last modified on Wednesday May 22nd, 2019  *
*                                            *
*********************************************/

#include "utilities.h"
//#include "NEH.cpp"
//#include <string.h>
#include <sstream>

using namespace std;

int(*functions_ptr_2[])(const matrix*) = {&FSS, &FSSB, &FSSNW};
matrix* (*functions_ptr_3[])(const matrix*) = {&FSS_2, &FSSB_2, &FSSNW_2};

/**
 * write a 2d array to a csv file
 *
 * @param mat: a matrix containing the elements to write to the csv file
 * @param file_name: the name of the file where data will be saved
 *
 * @return : None
 */
void write_to_file(matrix* mat, string file_name)
{
	ofstream file_writer;
	file_writer.open(file_name, ios_base::app);
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

/**
 * set job numbers from 1 to number of jobs for each row
 *
 * @param m: a matrix containing a set of jobs orderings
 *
 * @return : None
 */ 
void init_jobs(matrix *m)
{
	for(int i = 0; i < m->num_rows; i++)
	{
		for(int j = 0; j < m->num_columns - 2; j++)
		{
			m->mat[i][j] = j + 1;
		}
	}
}

/**
 * read data from a text file a create a matrix containing the processing times for each job on each machine
 *
 * @param filename: a string for the name of the file to read from
 *
 * @return : a  matrix containing the processing times for each job on each machine
 */
matrix* get_P_times(string file_name)
{
	ifstream File;
	int num_machines, num_jobs, *tmp_arr;
	//File.exceptions ( ifstream::badbit ); // No need to check failbit
  	try 
	{
		File.open(file_name);
		if(!File.good())
		{
			cerr << "Error opening/reading file! Please make sure that the DataFiles folder exists and the file you provide is in there.\n\n\n";
                	exit(-1);
		}
		File >> num_machines;
		File >> num_jobs;

		tmp_arr = new int[num_machines * num_jobs]();

		for(int i = 0; i < num_machines * num_jobs; i++)
			File >> tmp_arr[i];
		File.close();
	}
	
	catch (const ifstream::failure& e) 
	{
    		cerr << "Error opening/reading file";
		exit(-1);
 	}
	
	return new matrix(num_machines, num_jobs, tmp_arr);

}

/**
 * compute the makespan or total flow time for a specific job ordering
 *
 * @param :None
 *
 * @return : None
 */
void compute_cmax()
{

	string file_name;
	int *jobs_sequence, algo_id;

	cout << "\n\nPlease enter a string for the file name (include the file extension): ";
	cin >> file_name;
	
		
	matrix *P_times = get_P_times("../DataFiles/" + file_name);
	matrix *new_P_times = new matrix(P_times->num_rows, P_times->num_columns);
	
	cout << "\n\nPlease enter an integer for the algorithm to be used. Enter  \n\n1 for Flow Shop Scheduling \n\n2 for Flow Shop Scheduling with blocking \n\n3 for Flow Shop Scheduling with no wait \n\nEnter a value: ";
	cin >> algo_id;
	if(cin.fail() || (algo_id != 1 && algo_id != 2 && algo_id != 3))
	{
		cerr << "\n\nSorry wrong input! Please try again and make sure to enter a valid input.";
		exit(-1);
	}
       
	
	cout << "\n\nPlease enter a string for the jobs ordering (the order in which the jobs should be executed. only integers separated by a space): ";
	int i = 0;
	jobs_sequence = new int[P_times->num_columns]();
	while(i < P_times->num_columns)
	{
		cin >> jobs_sequence[i];
		if(cin.fail())
		{
			cerr << "\n\nSorry wrong input! Please try again and make sure to enter a valid input.\n\n";
                	exit(-1);	
		}

		if(jobs_sequence[i] > P_times->num_columns || jobs_sequence[i] < 1)
                {
                        cerr << "\n\nSorry wrong input! You have provided a job number that is bigger than the number of jobs available or smaller than 1.\n\n";
                        exit(-1);
                }

		i++;
	}
	
	for(int j = 0; j < P_times->num_rows; j++)
	{
		for(int k = 0; k < P_times->num_columns; k++)
		{
			new_P_times->mat[j][k] = P_times->mat[j][jobs_sequence[k]-1];	
		}
	}

	cout << "The makespan or total flow time is : " << functions_ptr_2[algo_id - 1](new_P_times) << "\n\n" ;

	delete[] jobs_sequence;
	delete P_times;
	delete new_P_times;


}

	




/**
 * similute the 3 flow shop scheduling algorithm
 *
 * *@param index: an integer for the index of the algorithm to be run (0 for FSS, 1 for FSSB and 2 for FSSNW)
 *
 * @return :None
 */
void* simulate(void* index)
{
	long i = (long)index;
	
	clock_t start;
	glob_t glob_result;
	glob("../DataFiles/*",GLOB_TILDE,NULL,&glob_result); //get the list of all the file in ../DataFiles

	matrix *best_seq, *P_times, *new_P_times, *start_times, *completion_times;
        matrix *run_times = new matrix(1, 1);
	matrix *num_func_calls = new matrix(glob_result.gl_pathc, 3);


	//for(int i = 0; i < 3; i++) //run the 3 objective functions
	//{
		if(i == 0)
			cout << "\n\n-----------------------------------------------------------------Starting Flow Shop Scheduling--------------------------------------------------------------------------\n\n";
		else if(i == 1)
			cout << "\n\n-----------------------------------------------------------Starting Flow Shop Scheduling with Blocking------------------------------------------------------------------\n\n";
		else
			cout <<"\n\n ------------------------------------------------------------Starting Flow Shop Scheduling with No Wait------------------------------------------------------------------\n\n";
		for(int j=0; j<glob_result.gl_pathc; ++j) //run every file in the DataFiles directory
		{
	
			P_times = get_P_times(glob_result.gl_pathv[j]);
			best_seq = new matrix(1, P_times->num_columns + 2);
			init_jobs(best_seq);
			start = clock();
			NEH(P_times, best_seq->mat[0], num_func_calls->mat[j], i);
			run_times->mat[0][0] = clock() - start;


			if((string)glob_result.gl_pathv[j] == "../DataFiles/1.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/11.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/21.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/31.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/41.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/51.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/61.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/71.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/81.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/91.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/101.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/111.txt")
			{

				new_P_times = new matrix(P_times->num_rows, P_times->num_columns);
				
				for(int l = 0; l < P_times->num_rows; l++)
				{
					for(int k = 0; k < P_times->num_columns; k++)
					{
						new_P_times->mat[l][k] = P_times->mat[l][best_seq->mat[0][k]-1];
					}
				}
				
				completion_times = functions_ptr_3[i](new_P_times);
				start_times = compute_start_times(new_P_times, completion_times);
			}

			if (i == 0)
			{
				write_to_file(best_seq, "../Results/FSS/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + ".csv");
				write_to_file(run_times, "../Results/FSS/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_run_times.csv");
				if (j == glob_result.gl_pathc - 1) //at the end of the simulation, saves the matrix contening the number of function calls then delete it
				{
					write_to_file(num_func_calls, "../Results/FSS/num_func_calls.csv");
					delete num_func_calls;
				}

				if((string)glob_result.gl_pathv[j] == "../DataFiles/1.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/11.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/21.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/31.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/41.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/51.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/61.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/71.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/81.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/91.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/101.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/111.txt")
				{

					write_to_file(completion_times, "../Results/FSS/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_comp_times"+".csv");
					write_to_file(start_times, "../Results/FSS/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_start_times"+".csv");

					delete new_P_times;
					delete completion_times;
					delete start_times;
                                }
				ofstream file_writer;
        			file_writer.open("../Results/FSS/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_file_order"+".csv", ios_base::app);
				file_writer << (string)glob_result.gl_pathv[j]<< "\n";
				file_writer.close();
			}

			else if (i == 1)
                        {
                                write_to_file(best_seq, "../Results/FSSB/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + ".csv");
                                write_to_file(run_times, "../Results/FSSB/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_run_times.csv");
				if (j == glob_result.gl_pathc - 1) //at the end of the simulation, saves the matrix contening the number of function calls then delete it
                                {
                                        write_to_file(num_func_calls, "../Results/FSSB/num_func_calls.csv");
                                        delete num_func_calls;
                                }

				if((string)glob_result.gl_pathv[j] == "../DataFiles/1.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/11.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/21.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/31.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/41.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/51.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/61.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/71.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/81.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/91.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/101.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/111.txt")	
				{
					write_to_file(completion_times, "../Results/FSSB/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_comp_times"+".csv");
                                        write_to_file(start_times, "../Results/FSSB/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_start_times"+".csv");
                                        delete new_P_times;
                                        delete completion_times;
                                        delete start_times;
                                }
				ofstream file_writer;
                                file_writer.open("../Results/FSSB/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_file_order"+".csv", ios_base::app);
                                file_writer << (string)glob_result.gl_pathv[j]<< "\n";
                                file_writer.close();
                        }

			else if (i == 2)
                        {
                                write_to_file(best_seq, "../Results/FSSNW/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + ".csv");
                                write_to_file(run_times, "../Results/FSSNW/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_run_times.csv");
				if (j == glob_result.gl_pathc - 1) //at the end of the simulation, saves the matrix contening the number of function calls then delete it
                                {
                                        write_to_file(num_func_calls, "../Results/FSSNW/num_func_calls.csv");
                                        delete num_func_calls;
                                }

				if((string)glob_result.gl_pathv[j] == "../DataFiles/1.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/11.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/21.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/31.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/41.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/51.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/61.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/71.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/81.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/91.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/101.txt" || (string)glob_result.gl_pathv[j] == "../DataFiles/111.txt")
				{
					write_to_file(completion_times, "../Results/FSSNW/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_comp_times"+".csv");
                                        write_to_file(start_times, "../Results/FSSNW/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_start_times"+".csv");
                                        delete new_P_times;
                                        delete completion_times;
                                        delete start_times;
				}
				ofstream file_writer;
                                file_writer.open("../Results/FSSNW/" + to_string(P_times->num_rows) + "_" + to_string(P_times->num_columns) + "_file_order"+".csv", ios_base::app);
                                file_writer << (string)glob_result.gl_pathv[j]<< "\n";
                                file_writer.close();
			
                        }


			cout << "File " << glob_result.gl_pathv[j] << ".................. Best C_max: " << best_seq->mat[0][P_times->num_columns + 1] << ".\n";

			delete best_seq;	
			delete P_times;
		}

		if(i == 0)
                        cout <<"\n\n -----------------------------------------------------------------Ending Flow Shop Scheduling--------------------------------------------------------------------------\n\n";
                else if(i == 1)
                        cout << "\n\n-----------------------------------------------------------Ending Flow Shop Scheduling with Blocking------------------------------------------------------------------\n\n";
                else
                        cout << "\n\n------------------------------------------------------------Ending Flow Shop Scheduling with No Wait------------------------------------------------------------------\n\n";
	//}
	
	delete run_times;

}



