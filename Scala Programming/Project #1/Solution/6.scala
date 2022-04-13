/*In the “Solution” editor define a few functions:

Ints.max(Int,Int):Int
Ints.factorial(Int):Int
Ints.sum(Int,Int):Int
Write some tests for them. Don’t forget to run [Spec-test] before [Submit].*/


// the solution 

object Ints { 

  /*
  * max(Int,Int):Int
  * @args
  *   two integers
  * @return
  *   the larger of the arguments
  * 
  * A simple solution using conditionals. You might also have discovered a library function for this.
  */
  def max(l : Int, r : Int) = if (l > r) l else r
  
  /*
  * factorial(Int):Int
  * @args
  *   one integer n, assumed to be positive
  * @return
  *   the factorial of the argument n, i.e. 1*2*3*...*n
  * 
  * Here is a recursive solution.   
  * Note that because of the assumption on n, we are free to return whatever we find convenient 
  * for non-positive valued of n (and we won't write tests for such values).
  *  
  */
  def factorial(n : Int) : Int = if (n <= 1)  1 else n * (factorial (n-1))

  /* 
  * sum(Int,Int):Int
  * @args
  *   two integers n,m
  * @return
  *   the sum of the integers between n and m inclusive, i.e. n+(n+1)+(n+2)+...+m
  * 
  * Here is one possible solution, which uses Gauss's formula.
  * Since there were no assumptions on n and m, it is important to consider degenerate cases where m < n.
  */
  def sum(n: Int,m : Int) = if (n <= m)  (m * (m+1) - (n-1) * n) / 2 else 0
  
}


