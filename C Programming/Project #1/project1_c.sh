#!/bin/bash
max_1=1000
max_2=200
max_3=500
gcc classical.c -o cl.out -pg
gcc double_linked_list.c -o dll.out -pg
for((i = 100; i <= $max_1; i = i + 100))
do
	./cl.out 2 $i $max_2
	gprof cl.out > profiles/classical_"$i"_"$max_2".txt
	./cl.out 1 $max_3 $i
	gprof cl.out > profiles/classical_"$max_3"_"$i".txt
	./dll.out 2 $i $max_2
	gprof cl.out > profiles/dll_"$i"_"$max_2".txt
	./dll.out 1 $max_3 $i
	gprof cl.out > profiles/dll_"$max_3"_"$i".txt
done


	
