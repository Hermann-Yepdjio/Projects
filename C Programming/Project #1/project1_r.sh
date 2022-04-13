#!/bin/bash
max_1=1000
max_2=200
max_3=500
for((i = 100; i <= $max_1; i = i + 100))
do
	Rscript classical.R 2 $i $max_2
	Rscript classical.R 1 $max_3 $i
	
	
done
