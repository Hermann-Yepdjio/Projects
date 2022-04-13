//test: Test

import Solution._

import org.scalatest.FunSuite

class Test extends FunSuite {

  test("Should be 42") {
    assert(fortyPlusTwo() == 42)
  }
  
  test("Should be 12") {
    assert(threeTimesFour() == 12)
  }

}

