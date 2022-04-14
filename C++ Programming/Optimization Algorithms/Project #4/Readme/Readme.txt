/********************************************
*                                           *                                  
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #5                                *
* Last modified on Wednesday May 31st, 2019 *
*                                           *
*********************************************/


1. About the Program
	- Operating system used: Ubuntu 18.04 LTS.
	- Language used: C++ 11 for the experimentation and R for the statistical analysis and scripting
	- Compiler used: g++ 7
	- C++ standard: C++11

2. Inputs to the program
	- The inputs for the program are read from .txt files inside the DataFiles folder

3. About the algorithms
	- FSS computes the makespan following the regular flow shop scheduling algorithm
	- FSSB computes the makespan following the flow shop scheduling with blocking algorithm
	- FSSNW computes the total flow following the flow shop scheduling with no wait algorithm

4. How to Run the Program
	- before running the program, make sure the DataFiles Repository exist (contains appropriate text files for the inputs to the system) in the same folder as the Source folder.
	- from command line, compile the code using the command: g++ main.cpp NEH.cpp functions.cpp matrix.cpp utilities.cpp -fopenmp -pthread
	- After compiling the code, run it using the command ./a.out
	- You will be asked to choose between either providing a string for evaluation or running the whole program.
		+If providing a string is chosen, 
			*you will be asked to provide the name of a file (including the extension) existing in the DataFiles folder and which contains information about the processing times
			* you will asked  to provide a string for the order in with the jobs should be executed. (Ex. 1 2 3 4 5 (i.e job1 then job2 then job3 etc...))
			* in the previous step the program will wait until enough argument is provided (as many numbers as there are jobs)
			* you will be asked to select which objective function should be use to perform the evaluation
		+ If running the whole program is selected, the will start running until completion
		

5. Where to find the generated results (.csv files)
	- The results of the program are saved in .csv files inside the "Results/FSS" or "Results/FSSB" or "Results/FSSNW" folder depending on the algorithm 
	- Gantt charts can be found in the gantt_charts repository inside those folders ("Results/FSS" or "Results/FSSB" or "Results/FSSNW" folder depending on the algorithm) 


