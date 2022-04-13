/*
Implement the following functions in Scala using the Scala library’s version of “fold right” (written :\) and without using any imperative features or explicit recursion.

(a) snoc[A](newLastElem: A) (xs: List[A]) : List[A] appends a single element at the end of an arbitrary list. Note: there is already a library function that has this effect, but you must not use it!

(b) reverse[A](xs:List[A]) : List[A] reverses an arbitrary list. Hint: you may use your snoc function from part (a).

(c) mean[A](xs:List[Int]) : Int returns the the mean value of the integers in list xs. (Of course, the mean may not be an exact integer, so in fact you should return just the integer part of the real mean.) You may only use one fold operation. You may not use any recursive auxiliary functions such as length or sum. It is acceptable for your function to raise an exception if it is given an empty list. Hint: have your fold return a pair of things.

In addition to the spec tests, your code will be manually inspected to make sure that it meets the requirements above.

General Hint: Consult the Scala List API and the Scala book chapter on “Working with Lists” for guidance and examples. Warning: in the online version (at least) of the Scala book there is a consistent type-setting error: the string :\ incorrectly appears as :~.*/




object Folds 
{
  
  // An example function implemented using "fold right"
  def length[A] (xs:List[A]) : Int = (xs :\ 0) ((next,acc) => acc + 1)
  
  // FIXME: Add definitions for `snoc`, `reverse`, and `mean`
  def snoc[A](newLastElem: A) (xs: List[A]) = (xs :\ List[A](newLastElem)) ((y,ys) => y :: ys)
  
  def reverse[A](xs:List[A]) = (xs :\ List[A]()) ((y,ys) =>ys:::List(y))                   //snoc(y)(ys))


  def mean[A](xs:List[Int]) : Int = xs match
  {

      case h::t => (xs :\ (0.0, 0)) ((y,ys) => ((y + (ys._1 * ys._2))/(ys._2 + 1), ys._2 + 1))._1.toInt
      case Nil => -1
  }
  
  
}
