1 About the Program
	-Operating system used: Ubuntu 18.04 LTS.
	-Language used: C++ 11 for the experimentation and R for the statistical analysis and scripting
	- Compiler used: g++ 7

2 How to Run the Program
	- open the "Source" folder inside the project folder and execute the "project2.R" file either from an R environment or from command line using the command "r project2.R". 
	- you will prompt to choose which algorithm to use
		+ enter 1 and press Enter if to run Blind Search
		+ enter 2 and press Enter if to run Local Search
		+ enter 3 (or any other integer) and press Enter if to run Iterative Local Search
	- 5 .csv files will be generated inside the results folder
		+ fitness.csv contains the best fitness founds for each objective function
		+ run_times.csv contains the running times for each objective function
		+ solution_10.csv, solution_20.csv, solution_30.csv contain the solutions that produced the fitnesses respectively for dimensions 10, 20 and 30
	- execute the "statistics.R" file either from an R environment or from command line using the command "r statistics.R".
		+ this will compute the statistics from the previously generated .csv files and produce 3 additional .csv files containing the statistics for dimensions 10, 20 and 30.
