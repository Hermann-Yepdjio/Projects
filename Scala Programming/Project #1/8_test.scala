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

}

