change(w, e).
change(e, w).
counter(T,V):- T is V.
move([X, X, Me, Charlotte, (X, T)], [adam, brianna], [Y, Y, Me, Charlotte, (Y, A)]):-change(X, Y), counter(A,T+ 2).
move([X, Brianna, X, Charlotte, (X, T)], [adam, me], [Y, Brianna, Y, Charlotte, (Y, A)]):- change(X, Y), counter(A, T+ 5).
move([X, Brianna, Me, X, (X, T)], [adam, charlotte], [Y, Brianna, Me, Y, (Y, A)]):- change(X, Y), counter(A, T+10).
move([X, Brianna, Me, Charlotte, (X, T)], [adam], [Y, Brianna, Me, Charlotte, (Y, A)]):- change(X, Y), counter(A, T+1).
move([Adam, X, X, Charlotte, (X, T)], [brianna, me], [Adam, Y, Y, Charlotte, (Y, A)]):- change(X, Y), counter(A, T+5).
move([Adam, X, Me, X, (X, T)], [brianna, charlotte], [Adam, Y, Me, Y, (Y, A)]):- change(X, Y), counter(A, T+10). 
move([Adam, X, Me, Charlotte, (X, T)], [brianna], [Adam, Y, Me, Charlotte, (Y, A)]):- change(X, Y), counter(A, T+2).
move([Adam, Brianna, X, X, (X, T)], [me, charlotte], [Adam, Brianna, Y, Y, (Y, A)]):- change(X, Y), counter(A, T+10).
move([Adam, Brianna, X, Charlotte, (X, T)], [me], [Adam, Brianna, Y, Charlotte, (Y, A)]):- change(X, Y), counter(A, T+ 5).
move([Adam, Brianna, Me, X, (X, T)], [charlotte], [Adam, Brianna, Me, Y, (Y, A)]):- change(X, Y), counter(A, T+10).

	
solution([e,e,e,e,(e, T)], [], T ).
solution(Config, [Move|Rest], T):-
	move(Config, Move,[A, B, C, D, (E, F)]),
	solution([A, B, C, D, (E, F)], Rest, T).





puzzle_help(X, N):- num(N), length(X, N),  solution([w,w,w,w, (w, 0)], X, _),!.
puzzle(X, T):- num(T), puzzle_help(_, N), length(X, N), solution([w, w, w, w, (w, 0)], X, T),!.
num(1).
num(X):- num(Y), X is Y + 1.
