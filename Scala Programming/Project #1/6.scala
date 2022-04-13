// the solution 

object Ints { 

  /*
  * max(Int,Int):Int
  * @args
  *   two integers
  * @return
  *   the larger of the arguments
  */
  def max(a : Int, b : Int): Int =
  {
      if (a > b)
        return a
      b
  }
  
  /*
  * factorial(Int):Int
  * @args
  *   one integer n, assumed to be positive
  * @return
  *   the factorial of the argument n, i.e. 1*2*3*...*n
  */
  def factorial(a:Int): Int =
  {
      if(a == 0)
        return 1
      a * factorial(a - 1)
  }

  /* 
  * sum(Int,Int):Int
  * @args
  *   two integers n,m
  * @return
  *   the sum of the integers between n and m inclusive, i.e. n+(n+1)+(n+2)+...+m
  */
  def sum(a:Int, b:Int): Int =
  {
      (b - a + 1) * (a + b)/2
      /*if(a == b)
        return a 
      a + sum(a + 1, b)*/
  }
  
}



