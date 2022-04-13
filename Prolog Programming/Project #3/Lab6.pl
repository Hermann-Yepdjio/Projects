subseq([], []).
subseq([I|RestX], [I|RestY]):- subseq(RestX, RestY).
subseq(X, [_|RestY]):- subseq(X, RestY).

contains(Elt, [Head|Tail]):- (Elt = Head ; contains(Elt, Tail)).

findAllUnions(SetA, SetB, Result):- subseq(SetA, Result), subseq(SetB, Result).
myUnion([], [], []):-!.
myUnion(SetA, SetB, Result):- num(L), length(Result, L), findAllUnions(SetA, SetB, Result), !.

unionSet([], []).
unionSet([A], A).
unionSet([Head|[Head2|Tail]], U):-myUnion(Head, Head2, V), unionSet(Tail, W), myUnion(V, W, U).

isHittingSet([[]],[]).
isHittingSet([], _).
isHittingSet([Head|Tail], Set):- contains(X, Head), contains(X, Set), isHittingSet(Tail, Set).


findAll(Set, X):- unionSet(Set, Y), subseq(X, Y), isHittingSet(Set, X).

minHittingSet([], _):- !, fail.
% minHittingSet([[]], []):-!.
% minHittingSet([[]|_], _):-!, fail.
minHittingSet(Sets, Solution):- not(contains([], Sets)), num(L), length(Solution, L), findAll(Sets, Solution), !.

num(1).
num(X):- num(Y), X is Y+1.

