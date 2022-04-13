years([], []).
years([H|T1], [[H|_]|T2]):- years(T1, T2).

claimed_jobs([], _).
claimed_jobs([H|T1], Sol):- H = CJ, member([_, CJ, _, _], Sol), claimed_jobs(T1, Sol).

regions([], _).
regions([H|T1], Sol):- H = Rg, member([_, _, Rg, _], Sol), regions(T1, Sol).

actual_jobs([], _).
actual_jobs([H|T1], Sol):- H = AJ, member([_, _, _, AJ], Sol), actual_jobs(T1, Sol).


hint1(X):- member([_, foreign_legionnaire, east_africa, _], X).

hint2(X):- member([Y1, _, _, mail_man], X),  member([Y2, _, _, server], X), 3 is Y2 - Y1.

hint3(X):- member([1976, _, middle_east, _], X).

hint4(X):- member([Y1, treasure_hunter, _, hotel_page], X), member([Y2, _, soviet_union, _], X), 3 is Y2 - Y1.

hint5(X):- member([1982, _, _, taxi_driver], X).

hint6(X):- member([_, spy, _, AJ], X), AJ \= server.


puzzle(X):- length(X, 4), years([1973, 1976, 1979, 1982], X), claimed_jobs([foreign_legionnaire, spy, bodyguard, treasure_hunter], X), regions([middle_east, east_africa, soviet_union, south_america], X), actual_jobs([mail_man, hotel_page, server, taxi_driver], X), hint1(X), hint2(X), hint3(X), hint4(X), hint5(X), hint6(X).


/*
?- puzzle(X).
X = [[1973, foreign_legionnaire, east_africa, mail_man], [1976, bodyguard, middle_east, server], [1979, treasure_hunter, south_america, hotel_page], [1982, spy, soviet_union, taxi_driver]] ;
false.*/