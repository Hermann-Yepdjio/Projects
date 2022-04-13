//test: Test

// test of the solution

import org.scalatest.FunSuite

import Printer._

class Test extends FunSuite {
  
  test ("leaf 42") {
    assertResult ("42") { Leaf(42).toString }
  }
  
  test ("1 2 3") {
    assertResult ("(1 2 3)") { Node(Leaf(1),2,Leaf(3)).toString }
  }
  
  test ("leftist") {
    assertResult ("(((1 2 3) 4 5) 6 7)") 
    { Node(Node(Node(Leaf(1),2,Leaf(3)),4,Leaf(5)),6,Leaf(7)).toString }
  }
  
  test ("rightist") {
    assertResult ("(1 2 (3 4 (5 6 7)))")
    { Node(Leaf(1),2,Node(Leaf(3),4,Node(Leaf(5),6,Leaf(7)))).toString } 
  }

  test ("balanced") {
    assertResult ("((1 2 3) 4 (5 6 7))")
    { Node(Node(Leaf(1),2,Leaf(3)),4,Node(Leaf(5),6,Leaf(7))).toString } 
  }

  test ("negative") {
    assertResult("-42")
    { Leaf(-42).toString }
  }

  test("varied") {
    assertResult ("((3 1 2) -42 (4 2 (6 8 -11)))")
    { Node(Node(Leaf(3),1,Leaf(2)),-42,Node(Leaf(4),2,Node(Leaf(6),8,Leaf(-11)))).toString }
  }

  test ("13l") {
    assertResult ("((((((1 2 3) 4 5) 6 7) 8 9) 10 11) 12 13)")
    { Node(Node(Node(Node(Node(Node(Leaf(1),2,Leaf(3)),4,Leaf(5)),6,Leaf(7)),8,Leaf(9)),10,Leaf(11)),12,Leaf(13)).toString }
  }

}

