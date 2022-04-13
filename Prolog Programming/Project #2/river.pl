
change(e, w).
change(w, e).

% move(Config, Move, NextConfig)
% Config is a configuration (like [w,w,w,w])
% Move is a move (like wolf)
% NextConfig is the resulting configuration (in this case, [e,e,w,w])

move([X,X,G,C], wolf, [Y,Y,G,C]) :- change(X,Y).
move([X,W,X,C], goat, [Y,W,Y,C]) :- change(X,Y).
move([X,W,G,X], cabbage, [Y,W,G,Y]) :- change(X,Y).
move([X,W,G,C], nothing, [Y,W,G,C]) :- change(X,Y).

oneEq(X,X,_).
oneEq(X,_,X).

safe([M,W,G,C]) :- oneEq(M,G,W), oneEq(M,G,C).

solution([e,e,e,e],[]).
    
solution(Config,[Move|Rest]) :-
    move(Config,Move,NextConfig),
    safe(NextConfig),
    solution(NextConfig,Rest).


puzzle(X) :- num(N), length(X,N), solution([w,w,w,w],X),!.


num(1).
num(X) :- num(Y), X is Y + 1.
