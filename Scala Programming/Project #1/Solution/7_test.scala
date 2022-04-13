//test: Test

// test of the solution

import org.scalatest.FunSuite

import Tree._

class Test extends FunSuite {
  
  test ("sum 42") {
    assertResult (42) {sum(Leaf(42))}
  }

  test ("sum 5r") {
    assertResult (15) (sum(Node(Leaf(1),2,Node(Leaf(3),4,Leaf(5)))))
  }

  test ("size 1") {
    assertResult (1) (size(Leaf(1)))
  }

  test ("size 3") {
    assertResult (3) (size(Node(Leaf(1),2,Leaf(3))))
  }

  test ("size 5l") {
    assertResult (5) (size(Node(Node(Leaf(1),2,Leaf(3)),4,Leaf(5))))
  }

  test ("size 5r") {
    assertResult (5) (size(Node(Leaf(1),2,Node(Leaf(3),4,Leaf(5)))))
  }

  test ("size 7") {
    assertResult (7) (size(Node(Leaf(1),2,Node(Leaf(3),4,Node(Leaf(5),6,Leaf(7))))))
  }

  test ("size 13l") {
    assertResult (13) (size(Node(Node(Node(Node(Node(Node(Leaf(1),2,Leaf(3)),4,Leaf(5)),6,Leaf(7)),8,Leaf(9)),10,Leaf(11)),12,Leaf(13))))
  }

  test ("size 13r") {
    assertResult (13) (size(Node(Leaf(1),2,Node(Leaf(3),4,Node(Leaf(5),6,Node(Leaf(7),8,Node(Leaf(9),10,Node(Leaf(11),12,Leaf(13)))))))))
  }

  test ("negcopy 42") {
    assertResult (Leaf(-42)) {negcopy(Leaf(42))}
  }

  test ("negcopy -42") {
    assertResult (Leaf(42)) {negcopy(Leaf(-42))}
  }

  test ("negcopy 0") {
    assertResult (Leaf(0)) {negcopy(Leaf(0))}
  }

  test ("negcopy 123") {
    assertResult (Node(Leaf(-1),-2,Leaf(-3))) {negcopy(Node(Leaf(1),2,Leaf(3)))}
  }

  test ("negcopy  12345l") {
    assertResult (Node(Node(Leaf(-1),-2,Leaf(-3)),-4,Leaf(-5))) {negcopy(Node(Node(Leaf(1),2,Leaf(3)),4,Leaf(5)))}
  }

  test ("negcopy 12345r") {
    assertResult (Node(Leaf(-1),-2,Node(Leaf(-3),-4,Leaf(-5)))) {negcopy(Node(Leaf(1),2,Node(Leaf(3),4,Leaf(5))))}
  }

  def sanity(t:Tree) : Boolean = size(negcopy(t)) == size(t) && sum(negcopy(t)) == -sum(t)

  test ("sanity13l") {
    assert (sanity(Node(Leaf(1),2,Node(Leaf(3),4,Node(Leaf(5),6,Node(Leaf(7),8,Node(Leaf(9),10,Node(Leaf(11),12,Leaf(13)))))))))
  }

  test ("sanity13r") {
    assert (sanity(Node(Leaf(1),2,Node(Leaf(3),4,Node(Leaf(5),6,Node(Leaf(7),8,Node(Leaf(9),10,Node(Leaf(11),12,Leaf(13)))))))))
  }
}


