library("ggplot2")
library("reshape2")
plot_c_fixed_dimension<-function()
{
  data_1 = read.csv("experimentation_results/classical_clock()_fixed_dim.csv", sep = ",", head =F)
  colnames(data_1)<-c("dimension", "power", "time")
  data_2 = read.csv("experimentation_results/classical_gettimeofday()_fixed_dim.csv", sep = ",", head =F)
  colnames(data_2)<-c("dimension", "power", "time")
  data_3 = read.csv("experimentation_results/classical_time()_fixed_dim.csv", sep = ",", head =F)
  colnames(data_3)<-c("dimension", "power", "time")
  data_4 = read.csv("experimentation_results/dll_clock()_fixed_dim.csv", sep = ",", head =F)
  colnames(data_4)<-c("dimension", "power", "time")
  data_5 = read.csv("experimentation_results/dll_gettimeofday()_fixed_dim.csv", sep = ",", head =F)
  colnames(data_5)<-c("dimension", "power", "time")
  data_6 = read.csv("experimentation_results/dll_time()_fixed_dim.csv", sep = ",", head =F)
  colnames(data_6)<-c("dimension", "power", "time")
  
  data <- data.frame(data_1[,"power"], data_1[,"time"], data_2[,"time"], data_3[,"time"], data_4[,"time"], data_5[,"time"], data_6[,"time"])
  colnames(data) = c("power", "classical_clock()", "classical_gettimeofday()", "classical_time()", "dll_clock()", "dll_gettimeofday()", "dll_time()")
  print(data)
  data <- melt(data ,  id.vars = 'power', variable_name = 'matrix_structure_and_timing_function')
  print(data)
  
  my_graph<- ggplot(data, aes(power, value)) + geom_line(aes(colour = matrix_structure_and_timing_function), size = 2, position=position_dodge(width=70))
  print(my_graph)
  
  ggsave(filename = "images/C_fixed_dim.png", plot = my_graph)
}

plot_c_fixed_power<-function()
{
  data_1 = read.csv("experimentation_results/classical_clock()_fixed_power.csv", sep = ",", head =F)
  colnames(data_1)<-c("dimension", "power", "time")
  data_2 = read.csv("experimentation_results/classical_gettimeofday()_fixed_power.csv", sep = ",", head =F)
  colnames(data_2)<-c("dimension", "power", "time")
  data_3 = read.csv("experimentation_results/classical_time()_fixed_power.csv", sep = ",", head =F)
  colnames(data_3)<-c("dimension", "power", "time")
  data_4 = read.csv("experimentation_results/dll_clock()_fixed_power.csv", sep = ",", head =F)
  colnames(data_4)<-c("dimension", "power", "time")
  data_5 = read.csv("experimentation_results/dll_gettimeofday()_fixed_power.csv", sep = ",", head =F)
  colnames(data_5)<-c("dimension", "power", "time")
  data_6 = read.csv("experimentation_results/dll_time()_fixed_power.csv", sep = ",", head =F)
  colnames(data_6)<-c("dimension", "power", "time")
  
  data <- data.frame(data_1[,"dimension"], data_1[,"time"], data_2[,"time"], data_3[,"time"], data_4[,"time"], data_5[,"time"], data_6[,"time"])
  colnames(data) = c("dimension", "classical_clock()", "classical_gettimeofday()", "classical_time()", "dll_clock()", "dll_gettimeofday()", "dll_time()")
  print(data)
  data <- melt(data ,  id.vars = 'dimension', variable_name = 'matrix_structure_and_timing_function')
  print(data)
  
  my_graph<- ggplot(data, aes(dimension, value)) + geom_line(aes(colour = matrix_structure_and_timing_function), size = 2, position=position_dodge(width=70))
  print(my_graph)
  
  ggsave(filename = "images/C_fixed_power.png", plot = my_graph)
}

plot_r_fixed_power<-function()
{
  data_1 = read.csv("experimentation_results/R_classical_cpu_time_fixed_power.csv", sep = ",", head =F)
  colnames(data_1)<-c("dimension", "power", "time")
  data_2 = read.csv("experimentation_results/R_classical_total_time_fixed_power.csv", sep = ",", head =F)
  colnames(data_2)<-c("dimension", "power", "time")
  data_3 = read.csv("experimentation_results/R_pure_classical_cpu_time_fixed_power.csv", sep = ",", head =F)
  colnames(data_3)<-c("dimension", "power", "time")
  data_4 = read.csv("experimentation_results/R_pure_classical_total_time_fixed_power.csv", sep = ",", head =F)
  colnames(data_4)<-c("dimension", "power", "time")
  
  data <- data.frame(data_1[,"dimension"], data_1[,"time"], data_2[,"time"], data_3[,"time"], data_4[,"time"])
  colnames(data) = c("dimension", "classical_cpu_time", "classical_elapsed_time", "pure_classical_cpu_time", "pure_classical_elapsed_time")
  print(data)
  data <- melt(data ,  id.vars = 'dimension', variable_name = 'matrix_structure_and_time_type')
  print(data)
  
  my_graph<- ggplot(data, aes(dimension, value)) + geom_line(aes(colour = matrix_structure_and_time_type), size = 2, position=position_dodge(width=70))
  print(my_graph)
  
  ggsave(filename = "images/R_fixed_power.png", plot = my_graph)
}

plot_r_fixed_dimension<-function()
{
  data_1 = read.csv("experimentation_results/R_classical_cpu_time_fixed_dim.csv", sep = ",", head =F)
  colnames(data_1)<-c("dimension", "power", "time")
  data_2 = read.csv("experimentation_results/R_classical_total_time_fixed_dim.csv", sep = ",", head =F)
  colnames(data_2)<-c("dimension", "power", "time")
  data_3 = read.csv("experimentation_results/R_pure_classical_cpu_time_fixed_dim.csv", sep = ",", head =F)
  colnames(data_3)<-c("dimension", "power", "time")
  data_4 = read.csv("experimentation_results/R_pure_classical_total_time_fixed_dim.csv", sep = ",", head =F)
  colnames(data_4)<-c("dimension", "power", "time")
  
  data <- data.frame(data_1[,"power"], data_1[,"time"], data_2[,"time"], data_3[,"time"], data_4[,"time"])
  colnames(data) = c("power", "classical_cpu_time", "classical_elapsed_time", "pure_classical_cpu_time", "pure_classical_elapsed_time")
  print(data)
  data <- melt(data ,  id.vars = 'power', variable_name = 'matrix_structure_and_time_type')
  print(data)
  
  my_graph<- ggplot(data, aes(power, value)) + geom_line(aes(colour = matrix_structure_and_time_type), size = 2, position=position_dodge(width=70))
  print(my_graph)
  
  ggsave(filename = "images/R_fixed_dim.png", plot = my_graph)
}

plot_c_r_fixed_dimension<-function()
{
  data_1 = read.csv("experimentation_results/classical_clock()_fixed_dim_9_rows.csv", sep = ",", head =F)
  colnames(data_1)<-c("dimension", "power", "time")
  data_2 = read.csv("experimentation_results/R_pure_classical_cpu_time_fixed_dim.csv", sep = ",", head =F)
  colnames(data_2)<-c("dimension", "power", "time")
  
  data <- data.frame(data_1[,"power"], data_1[,"time"], data_2[,"time"])
  colnames(data) = c("power", "c_classical_cpu_time", "r_classical_cpu_time")
  print(data)
  data <- melt(data ,  id.vars = 'power', variable_name = 'language_matrix_structure_and_time_type')
  print(data)
  
  my_graph<- ggplot(data, aes(power, value)) + geom_line(aes(colour = language_matrix_structure_and_time_type), size = 2, position=position_dodge(width=70))
  print(my_graph)
  
  ggsave(filename = "images/C_R_fixed_dim.png", plot = my_graph)
}

plot_c_r_fixed_power<-function()
{
  data_1 = read.csv("experimentation_results/classical_clock()_fixed_power_9_rows.csv", sep = ",", head =F)
  colnames(data_1)<-c("dimension", "power", "time")
  data_2 = read.csv("experimentation_results/R_pure_classical_cpu_time_fixed_power.csv", sep = ",", head =F)
  colnames(data_2)<-c("dimension", "power", "time")
  
  data <- data.frame(data_1[,"dimension"], data_1[,"time"], data_2[,"time"])
  colnames(data) = c("dimension", "c_classical_cpu_time", "r_classical_cpu_time")
  print(data)
  data <- melt(data ,  id.vars = 'dimension', variable_name = 'language_matrix_structure_and_time_type')
  print(data)
  
  my_graph<- ggplot(data, aes(dimension, value)) + geom_line(aes(colour = language_matrix_structure_and_time_type), size = 2, position=position_dodge(width=70))
  print(my_graph)
  
  ggsave(filename = "images/C_R_fixed_power.png", plot = my_graph)
}


#plot_c_fixed_dimension()
# plot_c_fixed_power()
#plot_r_fixed_power()
#plot_r_fixed_dimension()
plot_c_r_fixed_power()
#plot_c_r_fixed_dimension()

