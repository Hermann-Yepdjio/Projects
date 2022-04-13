
args <- commandArgs(trailingOnly=TRUE)
if(length(args) < 3)
{
  stop("Sorry you need to provide two integers as argument(version (1 for fixed dimensionality and sthg else for fixed power),M  for matrix dimensionality and N for the power of the matrix")
}

version = as.integer(args[1])
dim = as.integer(args[2])
power = as.integer(args[3])

if(anyNA(suppressWarnings(as.numeric(args))))
  stop("Sorry wrong type for arguments! All arguments must be integers")

      
set.seed(0)
create_rand_matrix<-function( n_rows, n_cols)
{
  mat <- matrix(runif(n_rows * n_cols, -1, 1), n_rows, n_cols) #generate a vector of n_rows * n_cols elts and devides it into n_rows rows and n_cols columns
  return (mat)
}

create_matrix<-function( n_rows, n_cols)
{
  mat <- matrix(runif(n_rows * n_cols, 0, 0), n_rows, n_cols) #generate a vector of n_rows * n_cols elts and devides it into n_rows rows and n_cols columns
  return (mat)
}

create_id_matrix<-function( n_rows, n_cols)
{
  mat <- matrix(runif(n_rows * n_cols, 0, 0), n_rows, n_cols) #generate a vector of n_rows * n_cols elts and devides it into n_rows rows and n_cols columns
  for (i in 1:n_rows)
  {
    for(j in 1:n_cols)
    {
      if (i == j)
        mat[i, j] <- 1
    }
  }
  return (mat)
}

multiply_matrices<-function(mat_1, mat_2)
{
  result = create_matrix(nrow(mat_1), ncol(mat_2))
  for(i in 1:nrow(mat_1))
  {
    
    for(j in 1:ncol(mat_2))
    {
      sum = 0
      for(k in 1:min(ncol(mat_1), nrow(mat_2)))
      {
        sum <- sum + mat_1[i, k] * mat_2[k, j]
      }
      result[i, j] <- sum
      
    }
  }
  return (result)
}

multiply_matrices_2<-function(mat_1, mat_2)
{
  return (mat_1 %*% mat_2)
}


pow_matrix<-function(mat, power)
{
  result = mat
  for(i in 1:(power-1))
  {
    #result = mat %*% result
    result = multiply_matrices(mat, result)
  }
  return(result)
}

write_to_file<-function(file_name, time, dim, power)
{ 
  write.table(data.frame(dim, power, time), file_name, col.names = F, row.names = F, sep = ",", append = T)
}

API<-function(dim, power, version)
{
  mat <- create_rand_matrix(dim, dim)
  time = system.time({result = pow_matrix(mat, power)})
  cat("------------------------running timees for dimentionality = ", dim, " and exponent = ", power, "-----------------------------")
  print(result)
  if(version == 1)
  {
    write_to_file("experimentation_results/R_pure_classical_total_time_fixed_dim.csv", as.numeric(time[3]), dim, power)
    write_to_file("experimentation_results/R_pure_classical_cpu_time_fixed_dim.csv", as.numeric(time[1]), dim, power)
  }else{
    write_to_file("experimentation_results/R_pure_classical_total_time_fixed_power.csv", as.numeric(time[3]), dim, power)
    write_to_file("experimentation_results/R_pure_classical_cpu_time_fixed_power.csv", as.numeric(time[1]), dim, power)
  }
  print(time)
  
}

API(dim, power, version)


