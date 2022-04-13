//test: Test

// test of the solution

import org.scalatest.FunSuite

import Tree._

class Test extends FunSuite {
  
  test ("sum 42") {
    assertResult (42) {sum(Leaf(42))}
  }

  test ("size 5r") {
    assertResult (5) (size(Node(Leaf(1),2,Node(Leaf(3),4,Leaf(5)))))
  }

  test ("negcopy 123") {
    assertResult (Node(Leaf(-1),-2,Leaf(-3))) {negcopy(Node(Leaf(1),2,Leaf(3)))}
  }

  def sanity(t:Tree) : Boolean = size(negcopy(t)) == size(t) && sum(negcopy(t)) == -sum(t)

  test ("sanity13l") {
    assert (sanity(Node(Leaf(1),2,Node(Leaf(3),4,Node(Leaf(5),6,Node(Leaf(7),8,Node(Leaf(9),10,Node(Leaf(11),12,Leaf(13)))))))))
  }

}



