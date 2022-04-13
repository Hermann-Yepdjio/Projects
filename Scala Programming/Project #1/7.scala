abstract class Tree
case class Leaf(v: Int) extends Tree
case class Node(l: Tree, v: Int, r: Tree) extends Tree

object Tree 
{ 

  def sum(t : Tree) : Int =
    t match 
    {
      case Leaf(v) => v
      case Node(l,v,r) => sum(l) + v + sum(r)
    }
    
  def size(t : Tree) : Int = 
  {
    t match 
    {
      case Leaf(v) => 1
      case Node(l,v,r) => size(l) + 1 + size(r)
    }
    //throw new Error("todo") // remove this line and put your code here
  }

  def negcopy(t: Tree) : Tree =
  {
    t match 
    {
      case Leaf(v) => Leaf(-v)
      case Node(l,v,r) => Node(negcopy(l), -v , negcopy(r))
    }
  }
    //throw new Error("todo") // remove this line and put your code here
}



