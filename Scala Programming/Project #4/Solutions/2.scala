/*
This question uses a full implementation of the E4 interpreter from the previous question (which is hidden in library code). Refer to the previous question to see the language features available in E4, including local variables and mutable pairs.

Show that you can write an ordinary function eq within language E4 that performs structural equality testing on two values that can each be a number or a pair. (In other words, eq should take two arguments and return true (1) if they are structurally equal and false (0) otherwise.)

Note that setfst or setsnd can be used to create cyclic structures, but for this exercise you should just assume that won’t happen. (Why does it matter?)

See the solution template for instructions on how to package your function.

By the way, it is also possible to write a reference equality test within language E4 (i.e., the built-in == operator is not strictly needed). But this is much less straightforward! Can you see how to do it?*/




/* How the code and tests for this assignment work:

This solution file includes an object defining a single Scala value, "definition"
which is a string representing a fragment of a program in E4, this week's toy language.
This fragment should define the function `eq`.

The library code includes a full implementation of the interpreter, supporting, among other things, mutable pairs.
The test harness constructs a program of the following form:

(() ((eq ...)) p)

where `eq ...` is your definition below
and `p` is an expression to test whether `eq` is working.
See the test template for an example.

Your job is to fill in the definition of `eq`.
--- and as usual, to make all of the spec tests pass!

*/

object MyEqDefinition {
val definition = """
  (eq (x y) (if (isPair x)
                (if (isPair y)
		                (if (@ eq (fst x) (fst y))
          		      	  (@ eq (snd x) (snd y))
			                  0)
	                  0)
          		  (if (isPair y)
		                0
		                (== x y))))
"""

  
}
