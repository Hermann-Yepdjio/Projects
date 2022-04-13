/*
This question uses a full implementation of the solution interpreter from question one (which is hidden in library code). Refer to question one to see the language features available in this toy language, including local variables and mutable pairs.

As noted in lecture, pairs can be used to encode lists within the language. The test template shows one simple way to do this, defining (ordinary) functions nil, cons, isnil, head, tail, sethead and settail in terms of the underlying pairs, together with some examples of their use.

Your task is to use this set of functions to implement and test the following list operations. You may find it easier (or just more fun!) to write your solutions in recursive functional style (like the append, countdown, or length examples) rather than in imperative style (like countupi and lengthi).

(a) (@ flatten l) takes a list of lists l and returns a fresh list containing the elements of l concatenated together. For example, (@ flatten (@ list3 (@ list2 1 2) (@ nil) (@ list1 3))) yields the list (1.(2.(3.0))). (Note that lists are printed out using the underlying pair representation since the interpreter doesn’t know they are intended to be lists.) The original list l must not be changed. You must not use setFst or setSnd operations (or any other function defined using those operations).

(b) (@ unzip l) takes a list l of pairs and returns a pair of lists, the first containing the first elements of l and the second containing the second elements of l. For example, (@ unzip (@ list3 (pair 1 2) (pair 3 4) (pair 5 6))) yields ((1.(3.(5.0))).((2.(4.(6.))))) Again, l must not be changed, and you must not use setFst or setSnd operations. Hint: let can be useful here.

(c) (@ nreverse l) should reverse list l in place, i.e., without constructing any new pairs, and return the resulting reversed list. After this call, the list pointed to by parameter l will be changed and typically no longer useful. For example, (let l (@ list3 1 2 3) (@ nreverse l)) yields (3.(2.(1.0))) and changes the list l. Hints: You’ll need to use settail but not sethead.

To test and submit these responses, save your function definitions as Scala in the solution template. We provide an example test harness in the test template, which you can use to test your solution. This harness makes it easy to see list-valued results.*/


/* How the code and tests for this assignment work:

This solution file includes an object defining a single Scala value, "definitions", 
which is a string representing a fragment of a program in this week's toy language, E4.
In particular, this program fragment is a list of function definitions.
See below for an example.

The library code includes a full implementation of the interpreter solution (including
support for `let` and mutable pairs). 

The test harness constructs a program of the following form:

(() (fs...) p)

where `fs...` is a space-separated list of function definitions
of the form `(name (arg1 arg2 ...) body)` 
(exactly like you would write in the function definitions list in a whole program)
and `p` is an expression being used to test the function definitions.

`p` may mention none, some, or all of the functions defined in `fs...`.

Also, note that the setup described above always has an empty global variable list,
so your functions (and our tests) cannot rely on any global variables.

See the test harness for an example of usage.

Your job is to add additional lines to the string "definitions", 
containing the definitions for `reverse`, `zip`, and `nreverse`
as described in the assignment 
--- and as usual, to make all of the spec tests pass!

*/

object MyListDefinitions {
val definitions = """
(not (b) (if b 0 1))

(nil () 0)

(cons (h t) (pair h t))

(head (l) (fst l))

(tail (l) (snd l))

(isnil (l) (== l (@ nil))) 

(sethead (l x) (setFst l x))

(settail (l x) (setSnd l x))

(list1 (x) (@ cons x (@ nil)))

(list2 (x y) (@ cons x (@ cons y (@ nil))))

(list3 (x y z) (@ cons x (@ cons y (@ cons z (@ nil)))))

(length (l) 
    (if (@ isnil l) 
        0 
        (+ 1 (@ length (@ tail l)))))

(sum (l) 
    (if (@ isnil l) 
        0 
        (+ (@ head l) (@ sum (@ tail l)))))

(countdown (n) 
    (if (<= n 0) 
        (@ nil) 
        (@ cons n (@ countdown (- n 1)))))

(countupHelper (n c) 
    (if (<= c n) 
        (@ cons c (@ countupHelper n (+ c 1))) 
        (@ nil)))
(countup (n) (@ countupHelper n 1))

(append (l m) 
    (if (@ isnil l) 
        m 
        (@ cons (@ head l) (@ append (@ tail l) m))))

(countupi (n) 
    (let l (@ nil) 
         (block (while (@ not (== n 0)) 
                       (block (:= l (@ cons n l)) 
                              (:= n (- n 1)))) 
                l)))

(lengthi (l) 
    (let n 0 
         (block (while (@ not (@ isnil l)) 
                       (block (:= n (+ n 1)) 
                              (:= l (@ tail l)))) 
                n)))

(flatten (l) 
    (if (@ not (isPair l))
            l
            (if( @ not (@ isnil (@ flatten (fst l))))
                (if( @ not (@ isnil (@ flatten (snd l))))
                    (@ cons (@ flatten (fst l)) (@ flatten (snd l)))
                    (@ flatten (fst l))
                )
                (@ flatten (snd l))
            )
    )
)    


(unzip (l) 
    (if (@ isnil l)
        (pair (@ nil) (@ nil)) 
        l
    )
)


(nreverse (l) 
    (let n 0 
         (block (while (@ not (@ isnil l)) 
                       (block (:= n (+ n 1)) 
                              (:= l (@ tail l)))) 
                n)))
"""
  
}


