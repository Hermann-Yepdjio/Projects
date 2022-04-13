//test: Test

// test of the solution

import org.scalatest.FunSuite

import Calc._

class Test extends FunSuite {

  test ("zero remainder") {
    assertResult (0) {
      rem(25,5)
    }
  }

  test ("non-zero remainder") {
    assertResult (3) {
      rem(28,5)
    }
  }

  test ("zero dividend") {
    assertResult (0) {
      rem(0,555)
    }
   }
  
  test ("neg by pos") {
    assertResult (-1) {
      rem(-10,3)
    }
  }

  test ("pos by neg") {
    assertResult (1) {
      rem(10,-3)
    }
  }


  test ("neg by neg") {
    assertResult (-1) {
      rem(-10,-3)
    }
  }

  test ("zero divisor") {
    intercept[ArithmeticException] {
      rem(2,0)
    }
  }

  test ("zero dividend and divisor") {
    intercept[ArithmeticException] {
      rem(0,0)
    }
  }

}

