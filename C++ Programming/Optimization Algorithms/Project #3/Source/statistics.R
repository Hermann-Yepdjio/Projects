##############################################
#                                            #
# statistics.R                               #
# By Hermann Yepdjio                         #
# SID: 40917845                              #
# CS 471 Optimization                        #
# Project #3                                 #
# Last modified on Wednesday May 1st, 2019   #
#                                            #
##############################################                                           

file_names_1 = files <- list.files("../Results/GA/")
file_names_2 = files <- list.files("../Results/DE/")

#compute statistics for the genetic algorithm results 
for(i in 1 : ((length(file_names_1) - 1) / 2))
{
    #print(file_names[i])
    fitnesses  <- read.csv(file = paste("../Results/GA/", file_names_1[i], sep = ""), header=FALSE, sep = ",")
    run_times <-  read.csv(file = paste("../Results/GA/", file_names_1[i + (length(file_names_1) - 1) / 2], sep = ""), header = FALSE, sep = ",")
    #print(paste("../Results/GA/", file_names_1[i], sep = ""))


    #data_frames to hold statistics for each dimension
    stats_dim_30 =  data.frame(Average = double(), Std_Dev = double(), Range = double(), Median = double(), Avg_Time = double())
  
    #compute statistics for dimension 30
    for(j in 1:18)
    {
      tmp_vector_fit = c()
      tmp_vector_run_time = c()
      for(k in seq(j, nrow(fitnesses), 19))
      {
            tmp_vector_fit = c(tmp_vector_fit, fitnesses[k, ncol(fitnesses)])   
            tmp_vector_run_time = c(tmp_vector_run_time, run_times[k, ncol(run_times)])
      }
      avg = mean(tmp_vector_fit)
      st_dev = sd(tmp_vector_fit)
      range = range(tmp_vector_fit)
      diff_range = range[2] - range[1]
      med = median(tmp_vector_fit)
      avg_time = mean(tmp_vector_run_time)
      
      stats_dim_30[nrow(stats_dim_30) + 1 ,] <- c(avg, st_dev, diff_range, med, avg_time)
    }


    write.csv(stats_dim_30, paste("../Results/GA/Statistics/", "stats_", file_names_1[i], sep = ""), row.names=FALSE)
}

for(i in 1 : ((length(file_names_2) - 1) / 2))
{
  #print(file_names[i])
  fitnesses  <- read.csv(file = paste("../Results/DE/", file_names_2[i], sep = ""), header=FALSE, sep = ",")
  run_times <-  read.csv(file = paste("../Results/DE/", file_names_2[i + (length(file_names_2) - 1) / 2], sep = ""), header = FALSE, sep = ",")
  #print(paste("../Results/GA/", file_names_1[i], sep = ""))
  
  
  #data_frames to hold statistics for each dimension
  stats_dim_30 =  data.frame(Average = double(), Std_Dev = double(), Range = double(), Median = double(), Avg_Time = double())
  
  #compute statistics for dimension 30
  for(j in 1:18)
  {
    tmp_vector_fit = c()
    tmp_vector_run_time = c()
    for(k in seq(j, nrow(fitnesses), 19))
    {
      tmp_vector_fit = c(tmp_vector_fit, fitnesses[k, ncol(fitnesses)])   
      tmp_vector_run_time = c(tmp_vector_run_time, run_times[k, ncol(run_times)])
    }
    avg = mean(tmp_vector_fit)
    st_dev = sd(tmp_vector_fit)
    range = range(tmp_vector_fit)
    diff_range = range[2] - range[1]
    med = median(tmp_vector_fit)
    avg_time = mean(tmp_vector_run_time)
    
    stats_dim_30[nrow(stats_dim_30) + 1 ,] <- c(avg, st_dev, diff_range, med, avg_time)
  }
  
#compute statistics for the differential evolution algorithm results  
write.csv(stats_dim_30, paste("../Results/DE/Statistics/", "stats_", file_names_2[i], sep = ""), row.names=FALSE)
}