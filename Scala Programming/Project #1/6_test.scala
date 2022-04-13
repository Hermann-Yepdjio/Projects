//test: Test

// test of the solution

import org.scalatest.FunSuite

import Ints._

class Test extends FunSuite 
{

  test ("max 0 0") 
  {
    assertResult (0) { max(0,0) }
  }

  test ("max 4 10") 
  {
    assertResult (10) { max(4, 10) }
  }

  test ("factorial 10") 
  {
    assertResult (3628800) { factorial(10) }
  }

  test ("sum 1 6") 
  {
    assertResult (21) { sum(1, 6) }
  }

  test ("sum 0 0") 
  {
    assertResult (0) { sum(0, 0) }
  }
 
  // Add your tests here!

}


