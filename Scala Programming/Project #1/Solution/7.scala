/*Case classes are a Scala language feature we will be using frequently throughout the course. This assignment focuses on the basic use of case classes. See the Scala documentation on case classes and on pattern matching.

For example, binary trees with odd numbers of values can be built from singleton trees (Leaf(v)) carrying a single value v and binary nodes (Node(l, v, r)) carrying an integer value v and left and right child trees (l and r). The sum function gives an example of how pattern matching and recursion can be used to compute over a tree. The test template shows how trees can be constructed.

Define the following two functions on these binary trees:

size(t : Tree) : Int returns the total number of leaves and internal nodes in the tree.

negcopy(t : Tree) : Tree returns a new tree, identical to the one passed as argument except that all the values are negated.

As a sanity check, for every tree t it should be the case that size(negcopy(t)) == size(t) and sum(negcopy(t)) == -sum(t).

The code should compile already, but you’ll need to implement the size and negcopy functions properly in order to make the specification tests pass. Don’t forget that you will get much better feedback about errors if you write your own tests!*/


abstract class Tree
case class Leaf(v: Int) extends Tree
case class Node(l: Tree, v: Int, r: Tree) extends Tree

object Tree { 

  def sum(t : Tree) : Int =
    t match {
      case Leaf(v) => v
      case Node(l,v,r) => sum(l) + v + sum(r)
    }
    
  def size(t : Tree) : Int =
    t match {
      case Leaf(_) => 1
      case Node(l,_,r) => 1 + size(l) + size(r)
    }

  def negcopy(t: Tree) : Tree =
    t match {
      case Leaf(v) => Leaf(-v)
      case Node(l,v,r) => Node(negcopy(l),-v,negcopy(r))
    }
}


