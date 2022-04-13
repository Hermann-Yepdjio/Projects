//test: Test

import org.scalatest.FunSuite

import ListStuff._

class Test extends FunSuite {

  // tests for after42

  test ("after42 Nil") {
    assertResult (42) { after42(Nil) }
  }

  test ("after42 1") {
    assertResult (42) { after42(List(1)) }
  }

  test ("after42 42") {
    assertResult (42) { after42(List(42)) }
  }

  test ("after42 1,2,3") {
    assertResult (42) { after42(List(1,2,3)) }
  }

  test ("after42 42,1") {
    assertResult (1) { after42(List(42,1)) }
  }

  test ("after42 42,1,2") {
    assertResult (1) { after42(List(42,1,2)) }
  }

  test ("after42 3,42,1,2") {
    assertResult (1) { after42(List(3,42,1,2)) }
  }

  test ("after42 3,4,42,1") {
    assertResult (1) { after42(List(3,4,42,1)) }
  }

  test ("after42 42,42") {
    assertResult (42) { after42(List(42,42)) }
  }

  test ("after42 1,42,2,42,3") {
    assertResult (2) { after42(List(1,42,2,42,3)) }
  }

  test ("after42 1,2,3,4,5,6,7,8,9,42,10") {
    assertResult (10) { after42(List(1,2,3,4,5,6,7,8,9,42,10)) }
  }

  // tests for zap
  test ("zap Nil") {
    assertResult (Nil) { zap(Nil) }
  }

  test ("zap 1") {
    assertResult (List(1)) { zap(List(1)) }
  }

  test ("zap 1,2") {
    assertResult (List(1,2)) { zap(List(1,2)) }
  }

  test ("zap 1,2,3") {
    assertResult (List(1,2,3)) { zap(List(1,2,3)) }
  }

  test ("zap 1,2,3,4") {
    assertResult (List(1,2,3,4)) { zap(List(1,2,3,4)) }
  }

  test ("zap 0") {
    assertResult (List(0)) { zap(List(0)) }
  }

  test ("zap 0,0") {
    assertResult (List(0,0)) { zap(List(0,0)) }
  }

  test ("zap 0,0,0") {
    assertResult (List(0,0,0)) { zap(List(0,0,0)) }
  }
  
  test ("zap 0,0,0,0") {
    assertResult (List(0,0,0,0)) { zap(List(0,0,0,0)) }
  }


  test ("zap 0,0,0,0,0") {
    assertResult (List(0,0,0,0,0)) { zap(List(0,0,0,0,0)) }
  }
  
  test ("zap 0,1") {
    assertResult (List(0,1)) { zap(List(0,1)) }
  }

  test ("zap 0,1,1") {
    assertResult (List(0,1,1)) { zap(List(0,1,1)) }
  }

  test ("zap 0,1,0") {
    assertResult (List(0,0)) { zap(List(0,1,0)) }
  }

  test ("zap 0,0,1,0") {
    assertResult (List(0,0,0)) { zap(List(0,0,1,0)) }
  }

  test ("zap 1,0,1,0") {
    assertResult (List(1,0,0)) { zap(List(1,0,1,0)) }
  }

  test ("zap 1,2,0,1,0") {
    assertResult (List(1,2,0,0)) { zap(List(1,2,0,1,0)) }
  }
  
  test ("zap 0,42,0,43,0") {
    assertResult (List(0,0,0)) { zap(List(0,42,0,43,0)) }
  }

  test ("zap 0,42,0,0,43,0") {
    assertResult (List(0,0,0,0)) { zap(List(0,42,0,0,43,0)) }
  }

  test ("zap 0,42,0,0,0,43,0") {
    assertResult (List(0,0,0,0,0)) { zap(List(0,42,0,0,0,43,0)) }
  }

  test ("zap 0,42,0,0,0,43") {
    assertResult (List(0,0,0,0,43)) { zap(List(0,42,0,0,0,43)) }
  }

  test ("zap 1,0,2,3,0,4") {
    assertResult (List(1,0,2,3,0,4)) { zap(List(1,0,2,3,0,4))  }
  }

  test ("zap 1,2,0,42,0,5,6,7,0,43,0,8,9") {
    assertResult (List(1,2,0,0,5,6,7,0,0,8,9)) { zap(List(1,2,0,42,0,5,6,7,0,43,0,8,9)) }
  }


  // tests for sumeqadj
  test ("sumeqadj Nil") {
    assertResult (Nil) { sumeqadj(Nil) }
  }

  test ("sumeqadj 1") {
    assertResult (List(1)) { sumeqadj(List(1)) }
  }

  test ("sumeqadj 1,2") {
    assertResult (List(1,2)) { sumeqadj(List(1,2)) }
  }

  test ("sumeqadj 1,1") {
    assertResult (List(2)) { sumeqadj(List(1,1)) }
  }

  test ("sumeqadj 1,2,3") { 
    assertResult (List(1,2,3)) { sumeqadj(List(1,2,3)) }
  }

  test ("sumeqadj 1,2,2") {
    assertResult (List(1,4)) { sumeqadj(List(1,2,2)) }
  }

  test ("sumeqadj 1,1,2") {
    assertResult (List(2,2)) { sumeqadj(List(1,1,2)) }
  }

  test ("sumeqadj 1,1,1") {
    assertResult (List(2,1)) { sumeqadj(List(1,1,1)) }
  }

  test ("sumeqadj 1,1,2,3") { 
    assertResult (List(2,2,3)) { sumeqadj(List(1,1,2,3)) }
  }

  test ("sumeqadj 1,1,2,2") {
    assertResult (List(2,4)) { sumeqadj(List(1,1,2,2)) }
  }

  test ("sumeqadj 1,2,3,4,5,6,6,7,8,9,9,10,11")  {
    assertResult (List(1,2,3,4,5,12,7,8,18,10,11)) { sumeqadj(List(1,2,3,4,5,6,6,7,8,9,9,10,11)) }
  }

  test ("sumeqadj 1,2,3,4,5,6,6,6,7,7,7,8,9") {
    assertResult (List(1,2,3,4,5,12,6,14,7,8,9)) { sumeqadj(List(1,2,3,4,5,6,6,6,7,7,7,8,9)) }
  }

/*

  // tests for find
  test ("find 2 in 1,2,3") {
    assertResult (true) { find(2,List(1,2,3)) }
  }

  test ("find 42 in empty") {
    assertResult (false) { find(42,Nil) }
  }
  
  test ("find 1 in 0,0,0") {
    assertResult (false) { find(1,0::0::0::Nil) }
  }
  
  test ("find 42 in 42") {
    assertResult (true) { find(42,List(42)) }
  }

  test ("find 43 in 42") {
    assertResult (false) { find(43,List(42)) }
  }

  // tests for find123
  test ("find123 0,1,2,3,4,5") {
    assertResult (true) { find123(List(0,1,2,3,4,5)) }
  }
  
  test ("find123 0,1,2,1,2,3,4,5") {
    assertResult (true) { find123(List(0,1,2,1,2,3,4,5)) }
  }
  
  test ("find123 1,2,4,3,1,2") {
    assertResult (false) { find123(1::2::4::3::1::2::Nil) }
  }
  
  test ("find123 empty") {
    assertResult(false) { find123(Nil) }
  }

  test ("find123 1") {
    assertResult(false) { find123(List(1)) }
  }

  test ("find 123 1,2") {
    assertResult (false) {find123(1::2::Nil) }
  }
 
  test ("find123 1,2,3") {
    assertResult (true) { find123(List(1,2,3)) }
  }

  test ("find123 1,2,3,4") {
    assertResult (true) { find123(List(1,2,3,4)) }
  }

  test ("find123 0,1,4,5") {
    assertResult (false) { find123(List(0,1,4,5)) }
  }

  test ("find123 0,1,2,4") {
    assertResult (false) { find123(List(0,1,2,4)) }
  }

  // tests for sumadj

  test ("sumadj Nil") {
    assertResult (Nil) { sumadj(Nil) }
  }

  test ("sumadj 1") {
    assertResult (List(1)) { sumadj(List(1)) }
  }

  test ("sumadj 1,2") {
    assertResult (List(3)) { sumadj(List(1,2)) }
  }

  test ("sumadj 1,2,3") {
    assertResult (List(3,3)) { sumadj(List(1,2,3)) }
  }

  test ("sumadj 1,2,3,4") {
    assertResult (List(3,7)) { sumadj(List(1,2,3,4)) }
  }

  test ("sumadj 1,-1,2,-2,3,-3,4,-4,5,-5") {
    assertResult (List(0,0,0,0,0)) { sumadj(List(1,-1,2,-2,3,-3,4,-4,5,-5)) }
  }

 */
  
}

