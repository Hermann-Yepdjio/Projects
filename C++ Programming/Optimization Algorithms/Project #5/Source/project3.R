##############################################
#                                            #
# project3.R                                 #
# By Hermann Yepdjio                         #
# SID: 40917845                              #
# CS 471 Optimization                        #
# Project #2                                 #
# Last modified on Wednesday MAY 1st, 2019   #
#                                            #
##############################################                                           

ranges  <- read.csv(file = "../Inputs/ranges.txt", header=TRUE, sep = ",")
inputs  <-  read.csv(file = "../Inputs/input_file.txt", header = TRUE, sep = ",")


#arguments to be passed via command line
num_arguments = 16 # number of command line arguments to be expected
dim = inputs[1, "dimensions"]
ns = inputs[1, "num_sols"] #size of the input space
num_gen = inputs[1, "num_gen"]
num_exp = inputs[1, "num_exp"]
c1 = inputs[1, "c1"]
c2 = inputs[1, "c2"]
k = inputs[1, "k"]
gamma = inputs[1, "gamma"]
B0 = inputs[1, "B0"]
alpha = inputs[1, "alpha"]
HMCR = inputs[1, "HMCR"]
PAR = inputs[1, "PAR"]
bw = inputs[1, "bw"]
delimiter = '_' # to know how dimensions_string and  ranges_string must be spllited inside the c** code in order to obtain correct values
num_functions = nrow(ranges) #number of functions to simulated
ranges_string = ""  # a string representing the ranges for each function (will be splitted inside c++ code to obtain actual values)

#construct ranges_string
for (row in 1: nrow(ranges))
{
  if (row == 1)
    ranges_string = paste(ranges_string, toString(ranges[row, "low_bound"]), '_', toString(ranges[row, "hi_bound"]), sep = "")
  else
    ranges_string = paste(ranges_string, '_', toString(ranges[row, "low_bound"]), '_', toString(ranges[row, "hi_bound"]), sep = "")
}


compile_cmd = "g++ main.cpp matrix.cpp functions.cpp utilities.cpp PSO.cpp FA.cpp HS.cpp" #command to compile c++ code
run_cmd = paste("./a.out", num_arguments, dim, ns, num_gen, num_exp, c1, c2, k, gamma, B0, alpha, HMCR, PAR, bw, num_functions, delimiter, ranges_string, sep= " ") #command to run the c++ code


system(compile_cmd)
system(run_cmd)
