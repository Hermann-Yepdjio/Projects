/*
The solution template for the previous problem, “Typechecker”, gives an interpreter and an incomplete type-checker for the E8 language.

This problem’s test harness allows you to run a complete version of the E8 interpreter and type-checker, hidden in the library code.

Using this complete version, do the following two tasks:

(a) Define a program, not involving if expressions, that raises a TypingException when checked but nonetheless interprets to a value without raising an InterpException. Write down this program as the Sacal string value partA in the object Solution in your solution file.

(b) Define a program, not involving division by zero, that passes the checker without raising a TypingException but does raise an InterpException when interpreted. Write down this program as the Scala string value partB in the object Solution in your solution file. Note that for this assignment, divide-by-zero exceptions are treated as a separate type of exception from InterpException.*/



object Solution { 

  // FIXME: Replace the definition of `partA` with a program that raises a TypingException, yet could be run without throwing an InterpException.
  // NOTE: The original definition of `partA` given in the template will throw an InterpException when run, since the variable `TODO` is not defined.
  val partA = "(() (cons 1 (cons true (cons 3 (nil num)))))"
  
  // FIXME: Replace the definition of `partB` with a program that type checks, yet will produce an InterpException when run.
  // NOTE: The original definition of `partB` given in the template will throw a TypingException when checked, since the variable `TODO` is not defined.
  val partB = "(() (tail (nil num)))"
}
