s_times <- read.csv("../Results/FSS/5_20_start_times.csv", header=FALSE)
c_times <- read.csv("../Results/FSS/5_20_comp_times.csv", header=FALSE)
b_seq <- read.csv("../Results/FSS/5_20.csv", header=FALSE)

# print(b_seq)
# print(as.vector(t(s_times)))
# b_seq = b_seq[1:(length(b_seq)-2)]
# print(b_seq)
# print(b_seq[9,])
# tmp <- as.character(b_seq[9,])
# tmp <- paste("J", tmp, sep="")
# print(tmp1)

Task = c()
count = 1
for(i in 1: nrow(s_times))
{
  for(j in 1:ncol(s_times))
  {
    Task[count] <- paste("M", i, sep = "")
    count <- count + 1
  }
}

summary <- data.frame("Start" = as.vector(t(s_times)), "End" = as.vector(t(c_times)), "Project" = rep(tmp, nrow(s_times)), "Task" = Task )


