//test: Test

// test of the solution

import org.scalatest.FunSuite
import scala.util.Random

import Folds._

class Test extends FunSuite {
  def rng = new Random
  def randBool = rng.nextBoolean
  def times(t:Int, r: =>Int):Int = { if (t <= 0) 0 else (r + times(t-1, r)) }
  def randInt = rng.nextInt
  def randList[A](l:Int, r : =>A) : List[A] = { if (l <= 0) Nil else r +: randList(l-1, r) } 
  
  test("1. [snoc] Appending to random list produces same result as standard library :+") {
    for(i <- 0 until 100) {
      val ri = randInt;
      val rl = randList(100, randInt)
      assertResult(rl :+ ri) { snoc(ri)(rl) }
    }
  }
  
    test("2. [snoc] Appending to nil produces a singleton") {
    for(i <- 0 until 100) {
      val ri = randInt;
      assertResult(List(ri)) { snoc(ri)(Nil) }
    }
  }
  
  test("3. [reverse] 'Reversing an empty list is the empty list'") {
    assertResult(Nil) { reverse(Nil) }
    assertResult(Nil) { reverse(List()) }
    assertResult(Nil) { reverse(List[Int]()) }
    assertResult(Nil) { reverse(List[Boolean]()) }
    assertResult(Nil) { reverse(List[Any]()) }
  }
  
  test("4. [reverse] `Reversing a singleton list is the same list`") {
    for(i <- 0 until 100) {
      val ri = randInt;
      val rb = randBool;
      assertResult(ri::Nil) { reverse(ri::Nil) }
      assertResult(rb::Nil) { reverse(rb::Nil) }
      assertResult((ri,rb)::Nil) { reverse((ri,rb)::Nil) }
    }
  }
 
  test("5. [reverse] 'Reversing random lists gets the same result as the standard library'") {
    for(i <- 0 until 100) {
      val r = randList(100,randInt)
      assertResult(r.reverse) {reverse(r)} 
    }
  }
  
  test("6. [mean] 'Mean of a singleton'") {
    assertResult(1) { mean(List[Int](1)) }
    assertResult(42) { mean(List[Int](42)) }
  }
  
  test("7. [mean] 'Mean of 2-element lists'") {
    assertResult(2) { mean(List[Int](1,3)) }
    assertResult(42) { mean(List[Int](42,42)) }
  }
    
  test("8. [mean] 'Mean of 11-element list'") {
    assertResult(6) { mean(List[Int](1,3,5,7,9,11,2,4,6,8,10)) }
  }
  
  /* Removed because of problems with overflow.
  test("9. [mean] 'Mean on random lists of Int'") {
    for (i <- 0 until 10) {
      val rs = randList(100, randInt)
      var t = 0
      for (j <- rs)
        t += j
      assertResult(t/100) {mean(rs)}
    }
  }
*/  

}


