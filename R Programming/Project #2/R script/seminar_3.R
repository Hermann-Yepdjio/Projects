#setwd("C:\\Users\\YepdjioNkoH\\Downloads\\Winter 2019\\CS 567\\Seminars\\Seminar\\Seminar2_R_Script")
setwd("/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Winter 2019/CS 567/Seminars/Seminar3/R script")
#install.packages("readxl")
library(readxl)
data = read_excel("data.xlsx", col_names = FALSE) #read the excel file
data = data.frame(data$..1, data$..2, data$..4, data$..5, data$..7, data$..8 ) #combine the 6 columns 2 by 2 into one to make 3 columns
colnames(data) <- c('X1', 'X2', 'Y1', 'Y2', 'Z1', 'Z2') #assign names to columns
print(data)



#install.packages("ggplot2")
#install.packages("reshape")
library(ggplot2)
#library(reshape)

# install.packages("car")
# install.packages("pastecs")
# install.packages("psych")
# install.packages("Rcmdr")
#install.packages("boot")

library(car)
library(pastecs)
library(boot)
#library(psych)
#library(Rcmdr)



#compute covariance
cov(X_data$X1, X_data$X2)
cov(Y_data$Y1, Y_data$Y2)
cov(Z_data$Z1, Z_data$Z2)


#create separate dataframes for each 2 columns in the excel file
X_data = data[, c('X1', 'X2')]
Y_data = data[, c('Y1', 'Y2')]
Z_data = data[, c('Z1', 'Z2')]

#Scatterplot
plot( X_data$X1, X_data$X2, xlab = "X_1", ylab = "X_2", main = "Scatter Plot for Table #1", pch=20 )
plot( Y_data$Y1, Y_data$Y2, xlab = "Y_1", ylab = "Y_2", main = "Scatter Plot for Table #2", pch=20 )
plot( Z_data$Z1, Z_data$Z2, xlab = "Z_1", ylab = "Z_2", main = "Scatter Plot for Table #3", pch=20 )

#create a table of pearson correlation between the two variables of X_data, Y_data, Z_data
cor.test(X_data$X1, X_data$X2)
cor.test(Y_data$Y1, Y_data$Y2)
cor.test(Z_data$Z1, Z_data$Z2)

#create a table of Pearson coefficient of determination between the two variables of X_data, Y_data, Z_data
cor(X_data)^2
cor(Y_data)^2
cor(Z_data)^2

#create a table of Spearman's Rho correlation between the two variables for each dataframe
cor.test(X_data$X1, X_data$X2, method = "spearman")
cor.test(Y_data$Y1, Y_data$Y2, method = "spearman")
cor.test(Z_data$Z1, Z_data$Z2, method = "spearman")

#bootstrapping X_data with 1000 samples replicates
boot_X_data<-function(X_data, i) cor(X_data$X1[i], X_data$X2[i], use = "complete.obs", method  =  "pearson")
boot_X_data_pearson <- boot(X_data, boot_X_data, 1000)
boot_X_data_pearson
boot.ci(boot_X_data_pearson)

#bootstrapping Y_data with 1000 samples replicates
boot_Y_data<-function(Y_data, i) cor(Y_data$Y1[i], Y_data$Y2[i], use = "complete.obs", method  =  "pearson")
boot_Y_data_pearson <- boot(Y_data, boot_Y_data, 1000)
boot_Y_data_pearson
boot.ci(boot_Y_data_pearson)

#bootstrapping Z_data with 1000 samples replicates
boot_Z_data<-function(Z_data, i) cor(Z_data$Z1[i], Z_data$Z2[i], use = "complete.obs", method  =  "pearson")
boot_Z_data_pearson <- boot(Z_data, boot_Z_data, 1000)
boot_Z_data_pearson
boot.ci(boot_Z_data_pearson)

