abstract class Tree {
  override def toString() : String = Printer.print(this)
}
case class Leaf(v: Int) extends Tree
case class Node(l: Tree, v: Int, r: Tree) extends Tree

object Printer 
{
  def print(x:Tree) : String = 
  {
    x match 
    {
      case Leaf(v) => v.toString
      case Node(l,v,r) => '(' + print(l) + ' ' + v.toString + ' ' + print(r) + ')'
    }
  }
      //throw new Error("todo")  // replace this line with your code
}


