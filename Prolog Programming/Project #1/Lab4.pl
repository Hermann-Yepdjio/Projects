median(A,B,C,X):-(X=A),(X=<B),(X>=C);
		 (X=A),(X>=B),(X=<C);
		 (X=B),(X=<A),(X>=C);
		 (X=B),(X>=A),(X=<C);
		 (X=C),(X=<A),(X>=B);
		 (X=C),(X>=A),(X=<B).
contains(L, X):-member(X, L).
largerEqual(L, X):- sort(L, Sorted_list), length(L, L_length), Index is L_length-1, nth0(Index, Sorted_list, Greatest_elt), X>=Greatest_elt.
max(L, X):- contains(L, X) , largerEqual(L, X).
