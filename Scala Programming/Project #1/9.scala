object ListStuff { 

  // EXAMPLE FUNCTIONS

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
    
  // EXERCISES FOR YOU TO COMPLETE

  // return the first number that immediately follows a 42 in xs
  // if there is no such number, return 42
  def after42 (xs:List[Int]) : Int = 
    xs match 
    {
        case 42::x::rest => x
        case _::rest => after42(rest)
        case _ => 42
        case Nil => 42
      // fill in here
    }

  // return the new list that results from xs by removing each non-0 value
  // that directly separates two 0's in the list
  def zap (xs:List[Int]) : List[Int] =
    xs match 
    {
        case 0::x::0::rest if x != 0 => 0::zap(0::rest)
        case x::rest => x::zap(rest)
       case Nil => xs
      // fill in here
    }

  // return the new list that results from xs by replacing each
  // adjacent pair of duplicate elements by their sum
  def sumeqadj(xs:List[Int]) : List[Int] =
    xs match 
    {
        case x::y::rest if x == y => (x + y)::sumeqadj(rest)
        case x::rest => x::sumeqadj(rest)
        case Nil => xs
      // fill in here
    }

}



