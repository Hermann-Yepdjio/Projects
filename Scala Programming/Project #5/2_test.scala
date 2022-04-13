//test: Test

// test of the solution

import org.scalatest.FunSuite
import Solution._

class Test extends FunSuite {
  
  test("Quicksort works on the example array") {
      val a = Array(10,32,567,-1,789,3,18,0,-51)
      trace = true // comment this line to avoid seeing partition traces
      quicksort(a)
      assert(a sameElements Array(-51,-1,0,3,10,18,32,567,789))
  }

}


