fitnesses  <- read.csv(file = "../Results/fitness.csv", header=FALSE, sep = ",")
run_times <-  read.csv(file = "../Results/run_times.csv", header = FALSE, sep = ",")


#data_frames to hold statistics for each dimension
stats_dim_10 =  data.frame(Average = double(), Std_Dev = double(), Range = double(), Median = double(), Time = double())
stats_dim_20 =  data.frame(Average = double(), Std_Dev = double(), Range = double(), Median = double(), Time = double())
stats_dim_30 =  data.frame(Average = double(), Std_Dev = double(), Range = double(), Median = double(), Time = double())


#compute statistics for dimension 1
for(i in seq(1, ncol(fitnesses), 3))
{
  avg = mean(fitnesses[ ,i])
  st_dev = sd(fitnesses[ ,i])
  range = range(fitnesses[ ,i])
  diff_range = range[2] - range[1]
  med = median(fitnesses[ ,i])
  
  stats_dim_10[nrow(stats_dim_10) + 1 ,] <- c(avg, st_dev, diff_range, med, 0)
}
stats_dim_10$Time <- run_times$V1
print(stats_dim_10)

#compute statistics for dimension 2
for(i in seq(2, ncol(fitnesses), 3))
{
  avg = mean(fitnesses[ ,i])
  st_dev = sd(fitnesses[ ,i])
  range = range(fitnesses[ ,i])
  diff_range = range[2] - range[1]
  med = median(fitnesses[ ,i])
  
  stats_dim_20[nrow(stats_dim_20) + 1 ,] <- c(avg, st_dev, diff_range, med, 0)
}
stats_dim_20$Time <- run_times$V2
print(stats_dim_20)

#compute statistics for dimension 30
for(i in seq(3, ncol(fitnesses), 3))
{
  avg = mean(fitnesses[ ,i])
  st_dev = sd(fitnesses[ ,i])
  range = range(fitnesses[ ,i])
  diff_range = range[2] - range[1]
  med = median(fitnesses[ ,i])
  
  stats_dim_30[nrow(stats_dim_30) + 1 ,] <- c(avg, st_dev, diff_range, med, 0)
}
stats_dim_30$Time <- run_times$V3
print(stats_dim_30)

#write the stats dataframes to csv files
write.csv(stats_dim_10, "../Results/stats_dim_10.csv", row.names=FALSE)
write.csv(stats_dim_20, "../Results/stats_dim_20.csv", row.names=FALSE)
write.csv(stats_dim_30, "../Results/stats_dim_30.csv", row.names=FALSE)