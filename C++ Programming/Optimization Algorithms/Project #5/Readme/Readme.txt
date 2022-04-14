/********************************************
*                                           *                                  
* By Hermann Yepdjio                        *
* SID: 40917845                             *
* CS 471 Optimization                       *
* Project #4                                *
* Last modified on Wednesday May 20, 2019   *
*                                           *
*********************************************/


1. About the Program
	- Operating system used: Ubuntu 18.04 LTS.
	- Language used: C++ 11 for the experimentation and R for the statistical analysis and scripting
	- Compiler used: g++ 7
	- C++ standard: C++11

2. Inputs to the program
	- The inputs for the program are read from .txt files inside the inputs folder (ranges.txt and inputs.txt) and passed as command line arguments to the program's executable via an R script
	- The "ranges.txt" file contains the range for each objective function. (if modified, make sure to keep the same format and value types)
	- The "inputs.txt" file contains the default input values for the program. (if modified, make sure to keep the same format and value types)

3. About the algorithms
	- PSO optimizes the objective functions by mimicking the flocking and schooling patterns of birds and fish. Over a number of iterations, the algorithm adjusts the values of the less fitted solutions to get them closer to those of the solution which is the closest to the target values.
	-FA optimizes the objective functions by mimicking the behavior of fireflies. Over a number of iterations, the algorithm adjusts the values of the less fitted solutions (less bright fireflies) to get them closer to those of the solution which is the closest (the brightest firefly) to the target values.
	-HS optimizes the objective functions by adjusting the values of the less fitted solutions (bad harmonies) to get them closer to those of the solution (best harmony) which is the closest to the target values just as skilled musician adjust the pitches to produce better harmonies.

4. How to Run the Program
	- Open the "Source" folder inside the project folder and execute the "project3.R" file either from an R environment or from command line using the command "r project3.R". 
	- You will be prompted to choose which algorithm to use
		+ enter 1 and press Enter to choose the Particle Swarm Optimization
		+ enter 2 and press Enter to choose the Firefly Algorithm
		+ enter 3 and press Enter to choose the Harmony Search Algorithm

5. Where to find the generated results (.csv files)
	- The results of the program are saved in .csv files inside the "Results/PSO" or "Results/FA" or "Results/HS" folder depending on the algorithm ran in section 3
	- each ***best_fitness.csv file contains 30 (or number of experimentations) blocks of 18 rows separated by a row filled with 0s
		+ each of the 18 rows corresponds to one the 18 functions (respectively to the order the functions were given in the project 1's description)
		+ each row in "**_best_fitness.csv" file has 100 (or number of generations) columns which contain the best fistness found in the corresponding generation
		+ each row in "**_run_times.csv" file has a single column which contains the running time for the corresponding experiment and objective function 
	- each block of 18 rows corresponds to one experimentation


