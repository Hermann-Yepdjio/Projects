#install.packages('xtable')
library(xtable)
bold <- function(x) {paste('{\\textbf{',x,'}}', sep ='')}
italic <- function(x) {paste('{\\textit{',x,'}}', sep ='')}
functions  <- read.csv(file = "stats_GA_best_fitness.csv", header=TRUE, sep = ",")
rownames(functions) <- c("f1 Schwefel", "f2 De Jong 1", "f3 Rosenbrock", "f4 Rastrigin", "f5 Griewangk", "f6 Sine Envelope", "f7 Stretch V Sine", "f8 Ackley One", "f9 Ackley Two", "f10 Egg Holder", "f11 Rana", "f12 Pathological", "f13 Michalewicz", "f14 Masters’ Cosine", "f15 Quartic", "f16 Levy", "f17 Step", "f18 Alpine" )
print(xtable(functions, digits = 2), scalebox = 0.9, latex.environments = "left", sanitize.rownames.function=italic, sanitize.colnames.function=bold, caption.width = 0.8 )
