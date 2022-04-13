//test: Test

// test of the solution

import org.scalatest.FunSuite

import Solution._

class Test extends FunSuite 
{

  test ("f returns its argument") 
  {
    assertResult (42) { f(42) }
  }

}

