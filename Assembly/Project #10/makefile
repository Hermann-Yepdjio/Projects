numarray: numarray.o
	g++ -nostartfiles -o numarray numarray.o
numarray.o: numarray.s
	as -o numarray.o numarray.s
clean:
	rm *.o numarray

