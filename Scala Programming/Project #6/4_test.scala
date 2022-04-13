//test: Test

// test of the solution

import org.scalatest.FunSuite

import Folds._

class Test extends FunSuite {
  
  test("'Length appears to work correctly for a few inputs'") {
    assertResult(0) {length(List())}
    assertResult(1) {length(List(1))}
    assertResult(3) {length(List(1,2,3))}
  }

  test("'Snoc appears to work correctly for a few inputs'") 
  {
    assertResult(List(1,2,3,4)) {snoc(4)(List(1,2,3))}
  }

  test("'Snoc nil appears to work correctly for a few inputs'") 
  {
    assertResult(List(4)) {snoc(4)(Nil)}
  }

  test("'Reverse appears to work correctly for a few inputs'") 
  {
    assertResult(List(1,2,3,4)) {reverse(List(4,3,2,1))}
  }

  test("'Reverse Nil appears to work correctly for a few inputs'") 
  {
    assertResult(Nil) {Nil}
  }

  test("'Mean neg appears to work correctly for a few inputs'") {
    assertResult(0) {mean(List(-3,-5,10))}

  }

  test("'Mean appears to work correctly for a few inputs'") 
  {
    assertResult(-842892370) {mean(List(-1193191224, -1440868480, -2077106510, 272120899, -397575778, -1667812085, -1201124672, -464547885, -1026620432, 767802459))}
  }

  test("'Mean large appears to work correctly for a few inputs'") 
  {
    assertResult(3) {mean(List(1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5))}
  }

  test("'Mean empty list appears to work correctly for a few inputs'") {
    assertResult(-1) {mean(List())}

  }
  
  /* FIXME: Add some relevant tests! */

}

