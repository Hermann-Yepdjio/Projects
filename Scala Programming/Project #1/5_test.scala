//test: Test

// test of the solution

import org.scalatest.FunSuite

import Calc._

class Test extends FunSuite {

  test ("remainder of dividing 3 by 2") {
    assertResult (1) {
      rem(3,2)
    }
  }
  
  test ("remainder of dividing 3 by 2, again") {
    assert (rem(3,2) >= 1 && rem(3,2) <= 1)
  }

  test ("zero divisor") 
  {
    intercept[ArithmeticException] 
    {
      rem(2,0)
    }
  }

  test ("When I call rem(10,2) I expect the result to be 0") 
  {
    assertResult(0){ rem(10,2)} 
  }

  test ("When I call rem(0,2) I expect the result to be 0") 
  {
    assertResult(0){ rem(0,2)} 
  }

  test ("zero num and zero divisor") 
  {
    intercept[ArithmeticException] 
    {
      rem(0,0)
    }
  }
  
  // Experiment with additional tests here 

}



