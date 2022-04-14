#install.packages("Cairo")

library(tidyverse)
library(lubridate)
library(scales)
library(Cairo)

# Alternatively you can put all this in a CSV file with the same columns and
# then load it with read_csv()
s_times <- read.csv("../Results/FSSB/10_200_start_times.csv", header=FALSE)
c_times <- read.csv("../Results/FSSB/10_200_comp_times.csv", header=FALSE)
b_seq <- read.csv("../Results/FSSB/10_200.csv", header=FALSE)

# print(b_seq)
# print(as.vector(t(s_times)))
 b_seq = b_seq[1:(length(b_seq)-2)]
# print(b_seq)
# print(b_seq[9,])
 tmp <- as.character(b_seq[9,])
 tmp <- paste("J", tmp, sep="")
# print(tmp1)

Machines = c()
count = 1
for(i in 1: nrow(s_times))
{
  for(j in 1:ncol(s_times))
  {
    Machines[count] <- paste("M", i, sep = "")
    count <- count + 1
  }
}

#print(rep(tmp, nrow(s_times)))
tasks<- data.frame("Start" = as.vector(t(s_times)), "End" = as.vector(t(c_times)), "Project" = rep(tmp, nrow(s_times)), "Task" = Machines )


# Convert data to long for ggplot
tasks.long <- tasks %>%
  mutate(Start = Start,
         End = End) %>%
  gather(date.type, task.date, -c(Project, Task)) %>%
  arrange(date.type, task.date) %>%
  mutate(Task = factor(Task, levels=rev(unique(Task)), ordered=TRUE))

# Custom theme for making a clean Gantt chart
theme_gantt <- function(base_size=11, base_family="Source Sans Pro Light") {
  ret <- theme_bw(base_size, base_family) %+replace%
    theme(panel.background = element_rect(fill="#ffffff", colour=NA),
          axis.title.x=element_text(vjust=-0.2), axis.title.y=element_text(vjust=1.5),
          title=element_text(vjust=1.2, family="Source Sans Pro Semibold"),
          panel.border = element_blank(), axis.line=element_blank(),
          panel.grid.minor=element_blank(),
          panel.grid.major.y = element_blank(),
          panel.grid.major.x = element_line(size=0.5, colour="grey80"),
          axis.ticks=element_blank(),
          legend.position="bottom",
          axis.title=element_text(size=rel(0.8), family="Source Sans Pro Semibold"),
          strip.text=element_text(size=rel(1), family="Source Sans Pro Semibold"),
          strip.background=element_rect(fill="#ffffff", colour=NA),
          panel.spacing.y=unit(1.5, "lines"),
          legend.key = element_blank())

  ret
}

# Calculate where to put the dotted lines that show up every three entries
x.breaks <- seq(length(tasks$Task) + 0.5 - 3, 0, by=-3)

# Build plot
timeline <- ggplot(tasks.long, aes(x=Task, y=task.date, colour=Project)) +
  geom_line(size=6) +
  geom_vline(xintercept=x.breaks, colour="grey80", linetype="dotted") +
  guides(colour=guide_legend(title=NULL)) +
  labs(x=NULL, y=NULL) + coord_flip() +

  theme(legend.position='none')+
  #theme_gantt() +
  theme(axis.text.x=element_text(angle=45, hjust=1))
timeline
#print(timeline)


# Save plot as PDF with embedded fonts (the secret is "device=cairo_pdf")
ggsave(timeline, filename="../Results/FSSB/10_200_GC.pdf",
       width=6.5, height=6.5, units="in", device=cairo_pdf)

# Save plot as high resolution PNG (the secret is 'type="cairo", dpi=300')
ggsave(timeline, filename="../Results/FSSB/10_200_GC.png",
       width=6.5, height=6.5, units="in", type="cairo", dpi=300)