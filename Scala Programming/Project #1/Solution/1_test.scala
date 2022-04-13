//test: Test

// test of the solution

import org.scalatest.FunSuite

import Speak._

class Test extends FunSuite {

  test("greet returns proper greeting")
  {
    assertResult ("Hello world") { greet() }
  }

}
