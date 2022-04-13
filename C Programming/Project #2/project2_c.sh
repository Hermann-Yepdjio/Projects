#!/bin/bash
max_1=500
max_2=90
gcc classical_one_elt.c -o cl_e.out -pthread
gcc classical_one_row.c -o cl_c.out -pthread
gcc classical_one_column.c -o cl_r.out -pthread
gcc double_linked_list.c -o dll.out -pthread

for((i = 60; i <= $max_2; i = i + 10))
do
	./cl_e.out 2 $i $max_1
	./dll.out 2 $i $max_1
done

for((i = 100; i <= $max_1; i = i + 100))
do
	./cl_e.out 1 $max_2 $i
	./dll.out 1 $max_2 $i
done

for((i = 60; i <= $max_2; i = i + 10))
do
	./cl_r.out 2 $i $max_1
done

for((i = 100; i <= $max_1; i = i + 100))
do
	./cl_r.out 1 $max_2 $i
done

for((i = 60; i <= $max_2; i = i + 10))
do
	./cl_c.out 2 $i $max_1
done

for((i = 100; i <= $max_1; i = i + 100))
do
	./cl_c.out 1 $max_2 $i
done


	
