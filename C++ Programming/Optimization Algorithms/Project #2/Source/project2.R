##############################################
#                                            #
# project2.R                                 #
# By Hermann Yepdjio                         #
# SID: 40917845                              #
# CS 471 Optimization                        #
# Project #2                                 #
# Last modified on Wednesday April 17, 2019  #
#                                            #
##############################################                                           

ranges  <- read.csv(file = "../Inputs/ranges.csv", header=TRUE, sep = ",")
inputs <-  read.csv(file = "../Inputs/input_file.csv", header = TRUE, sep = ",")


#arguments to be passed via command line
num_arguments = 8 # number of command line arguments to be expected
sample_size = inputs[1, "sample_size"] #size of the input space
num_dimensions = nrow(inputs) # how many dimensions are to be simulated
delta = inputs[1, "delta"]
delimiter = '_' # to know how dimensions_string and  ranges_string must be spllited inside the c** code in order to obtain correct values
dimensions_string = "" # a string representing the dimensions to be simulated (will be splitted inside c++ code to obtain actual values)
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


#constructs dimensions_string
for (row in 1: nrow(inputs))
{
  if(row == 1)
    dimensions_string = paste(dimensions_string, toString(inputs[row, "dimensions"]), sep = "")
  else
    dimensions_string = paste(dimensions_string, '_', toString(inputs[row, "dimensions"]), sep = "")
}



compile_cmd = "g++ main.cpp matrix.cpp functions.cpp search_functions.cpp utilities.cpp" #command to compile c++ code
run_cmd = paste("./a.out", num_arguments, sample_size, num_dimensions, delimiter, dimensions_string, num_functions, ranges_string, sep= " ", delta) #command to run the c++ code


system(compile_cmd)
system(run_cmd)
