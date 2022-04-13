//test: Test

// test of the solution

import org.scalatest.FunSuite

import Ints._

class Test extends FunSuite {

  test ("max 0 0") {
    assertResult (0) { max(0,0) }
  }

  test ("max -5 3") {
    assertResult (3) { max(-5,3) }
   }
   
  test ("max 3 -5") {
    assertResult (3) { max(3,-5) }
   }
   
  test ("max 5 5 ") {
    assertResult (5) { max(5,5) }
   }

  test ("1!") {
    assertResult (1) { factorial(1) }
  }

  test ("6!") {
    assertResult (720) { factorial(6) }
  }
  
  test ("sum 3 to 8") {
    assertResult (3+4+5+6+7+8) { sum(3,8) }
  }
  
  test ("sum -2 to 2") {
    assertResult (0) { sum(-2,2) }
  }

  test ("sum 10 to 10") {
    assertResult (10) { sum(10,10) }
  }

  test ("sum 10 to 9") {
    assertResult (0) { sum(10,9) }
  }

}


