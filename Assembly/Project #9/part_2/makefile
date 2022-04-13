game: game.o
	gcc  -o game game.o
game.o: game.s
	as -o game.o game.s
clean:
	rm *.o game -vf
