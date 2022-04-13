//test: Test

import org.scalatest.FunSuite

import ListStuff._

class Test extends FunSuite {

  // sample tests for after42

  test ("after42 1") {
    assertResult (42) { after42(List(1)) }
  }

  test ("after42 42,1") {
    assertResult (1) { after42(List(42,1)) }
  }

  test ("after42 1,42,2,42,3") {
    assertResult (2) { after42(List(1,42,2,42,3)) }
  }

  // sample tests for zap
  test ("zap 0,0,0") {
    assertResult (List(0,0,0)) { zap(List(0,0,0)) }
  }
  
  test ("zap 0,0,1,0") {
    assertResult (List(0,0,0)) { zap(List(0,0,1,0)) }
  }

  test ("zap 0,42,0,43,0") {
    assertResult (List(0,0,0)) { zap(List(0,42,0,43,0)) }
  }

  // sample tests for sumeqadj

  test ("sumeqadj 1,1,2") {
    assertResult (List(2,2)) { sumeqadj(List(1,1,2)) }
  }

  test ("sumeqadj 1,1,1") {
    assertResult (List(2,1)) { sumeqadj(List(1,1,1)) }
  }

  test ("sumeqadj 1,2,3,4,5,6,6,7,8,9,9,10,11")  {
    assertResult (List(1,2,3,4,5,12,7,8,18,10,11)) { sumeqadj(List(1,2,3,4,5,6,6,7,8,9,9,10,11)) }
  }
  
}


