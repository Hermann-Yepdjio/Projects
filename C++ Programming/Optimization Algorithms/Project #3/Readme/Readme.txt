1 About the Program
	- Operating system used: Ubuntu 18.04 LTS.
	- Language used: C++ 11 for the experimentation and R for the statistical analysis and scripting
	- Compiler used: g++ 7
	- C++ standard: C++11

2 Inputs to the program
	- The inputs for the program are read from .csv files inside the inputs folder (ranges.csv and inputs.csv) and passed as command line arguments to the program's executable via an R script
	- The "ranges.csv" file contains the range for each objective function. (if modified, make sure to keep the same format and value types)
	- The "inputs.csv" file contains the default input values for the program. (if modified, make sure to keep the same format and value types)

3 How to Run the Program
	- Open the "Source" folder inside the project folder and execute the "project3.R" file either from an R environment or from command line using the command "r project3.R". 
	- You will be prompted to choose which algorithm to use
		+ enter 1 and press Enter to choose the Genetic Algorithm
			* if you entered 1, you will be prompted to select which selection algorithm is to be used
				> enter 1 and press Enter to choose the roulette Wheel Algorithm
				> enter 2 and press Enter to choose the tournament selection algorithm
		+ enter 2 and press Enter to choose the Differential Evolution Algorithm

4 Where to find the generated results (.csv files)
	- The results of the program are saved in .csv files inside the "Results/GA" or "Results/DE" folder depending on the algorithm ran in section 3
	- each ***best_fitness.csv file contains 50 (or number of experimentations) blocks of 18 rows separated by a row filled with 0s
		+ each of the 18 rows corresponds to one the 18 functions (respectively to the order the functions were given in the project 1's description)
		+ each row in "**_best_fitness.csv" file has 100 (or number of generations) columns which contain the best fistness found in the corresponding generation
		+ each row in "**_run_times.csv" file has a single column which contains the running time for the corresponding experiment and objective function 
	- each block of 18 rows corresponds to one experimentation

5 How to compute the statistics for the generated results
	- after completing the task in section 3 above, execute the "statistics.R" file that is in the Source directory, from an R environment or from command line as described above
	- statistics are saved in .csv file inside the "Results/GA/Statistics" or "Results/DE/Statistics" folder depending on the algorithm ran in section 3
