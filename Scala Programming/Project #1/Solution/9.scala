/*Lists are a heavily used data type in Scala. A list is an immutable sequence of values, all drawn from one type, represented by a (singly) linked list of memory cells with a marker at the end.

In the Scala library, lists are defined by case classes roughly like this:

sealed abstract class List[+A]
case object Nil extends List[Nothing]
case class ::[A] (head: A, tail: list[A]) extends List[A]
Here the A is a type parameter that must be instantiated when the list is used. (Don’t worry about the [+A] or the [Nothing] at the moment.) A list is either empty (pronounced “nil”) or is a non-empty memory cell (pronounced “cons”) containing a value (the “head”) and another list (the “tail”). (The case object used for Nil is like a case class except that it has no parameters, and so does not need to be instantiated multiple times.)

We can create and pattern match on lists using the :: and Nil constructors, or using a multi-arity constructor List(). Note that :: is a somewhat special constructor in that it appears infix between its operands, and it is right-associative.
The solution template gives code that uses pattern matching to implement three simple functions over lists.

For more details, see the Programming in Scala book sections on patterns and list patterns, and the Scala documentation on pattern matching.

Your task is to complete the remaining three definitions in the solution template, so that the visible tests and hidden spec tests pass.
(Note: the template won’t compile in its present form.)*/

object ListStuff { 

  // return true iff x appears in xs
  def find (x:Int,xs:List[Int]) : Boolean =
    xs match {
      case y::rest => x==y || find(x,rest)
      case Nil => false
    }
      
  
  // return true iff List(1,2,3) appears as a sublist of xs
  def find123 (xs:List[Int]) : Boolean =
    xs match {
      case 1::2::3::_ => true
      case _::rest => find123(rest)
      case Nil => false
    }
  
  // return the new list that results from replacing each
  // adjacent pair of elements in xs with their sum
  // (if the list has odd length, the last element is undisturbed)
  def sumadj (xs:List[Int]) : List[Int] =
    xs match {
      case x::y::rest => (x+y)::sumadj(rest)
      case _ => xs
    }
  
  // return the first number that immediately follows a 42 in xs
  // if there is no such number, return 42
  def after42 (xs:List[Int]) : Int = 
    xs match {
      case 42::x::_ => x
      case _::rest => after42(rest)
      case Nil => 42
    }

  // return the new list that results from xs by removing each non-0 value
  // that directly separates two 0's in the list
  def zap (xs:List[Int]) : List[Int] =
    xs match {
      case 0::0::rest => 0::zap(0::rest)
      case 0::n::0::rest => 0::zap(0::rest)
      case h::t => h::zap(t)
      case Nil => Nil
    }

  // return the new list that results from xs by replacing each
  // adjacent pair of duplicate elements by their sum
  def sumeqadj(xs:List[Int]) : List[Int] =
    xs match {
      case x::y::rest if x == y => (x+y)::sumeqadj(rest)
      case x::y::rest => x::sumeqadj(y::rest)
      case _ => xs
    }
}


