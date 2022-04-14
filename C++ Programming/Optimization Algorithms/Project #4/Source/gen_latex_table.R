#install.packages('xtable')
library(xtable)
bold <- function(x) {paste('{\\textbf{',x,'}}', sep ='')}
italic <- function(x) {paste('{\\textit{',x,'}}', sep ='')}
functions  <- read.csv(file = "best_makeSpan1.csv", header=TRUE, sep = ",")
print(xtable(functions, digits = 2), scalebox = 0.9, latex.environments = "left", sanitize.rownames.function=italic, sanitize.colnames.function=bold, caption.width = 0.8 )
