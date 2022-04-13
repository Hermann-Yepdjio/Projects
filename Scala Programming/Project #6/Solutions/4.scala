// the solution 

object Folds {

  def snoc[A] (y:A) (xs:List[A]) : List[A] = (xs :\ (y::Nil)) (_ :: _)

  def reverse[A] (xs:List[A]) : List[A] = (xs :\ (Nil:List[A])) ((x,ys) => snoc(x)(ys))
  
  // This problem was more complicated than it was intended to be! 
  // The solution we had in mind was to compute the Ints for the sum and length of the list
  // using a single fold (producing a pair) and then compute the mean at the end as sum/length.
  // For example: 
  def mean (xs:List[Int]) = {
     val (t,c) = (xs :\ (0,0)) {case (x,(t,c)) => (t+x,c+1)} 
     t/c
     }

  // This algorithm was used as the standard of correctness in the ninth spec test, and 
  // you got full credit if you gave this or an equivalent solution.
  // However, if summing up the list elements results in overflow, this approach won't give
  // the expected result (i.e., the "true" mean calculated on unbounded mathematical integers).
  // For example,  mean(List(Int.MaxValue,Int.MaxValue)) 
  // [where Int.MaxValue = 2147483647 is the largest value representable as a Scala Int (32 bits)]
  // should produce Int.MaxValue, but instead produces -1. 
  // This is particularly unfortunate because the mean of any set of representable numbers 
  // is always a representable number.

  // An alternative that _will_ give the expected result is to convert all the numbers 
  // to double precision (64 bit) floats, which can be done without any loss of precision,
  // compute the sum and mean as Doubles, and then convert back to an integer at the end:
  def fmean(xs:List[Int]) = {
     val (t,c) = (xs :\ (0.0,0)) {case (x,(t,c)) => (t+x,c+1)} 
     (t/c).toInt
     }
  // We (eventually) disabled the ninth spec test to make sure that people who gave solutions along these lines
  // didn't lose credit for being more careful!  Sorry for the confusion.

}

