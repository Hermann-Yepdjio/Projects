/*This assignment asks you to demonstrate the use of the toString method and string coercion to pretty-print a simple datatype.

In Scala, the string concatenation operators + and += are conveniently overloaded to support use on objects that define toString. So it is worthwhile to make sure that every datatype gets a useful definition of this method.

Finish the template so that print prints out useful values that make the visible and spec tests pass. The visible tests will show you the intended format. (Hint: use pattern matching to implement print.)

In fact, the case class declarations automatically generate toString methods. To see what these look like, you can comment out the override def toString ... line. As you will see, the default methods generate more verbose output than our custom ones.

Incidentally, note that since toString takes an empty list of parameters, it is not necessary to use parentheses when invoking it. (In fact, it was not really even necessary to give the parentheses in the override def ...)*/

abstract class Tree {
    override def toString() : String = Printer.print(this)
}
case class Leaf(v: Int) extends Tree
case class Node(l: Tree, v: Int, r: Tree) extends Tree

object Printer {
  def print(x:Tree) = x match {
    case Leaf(v) => v.toString
    case Node(l,v,r) => "(" + l + " " + v + " " + r + ")"
  }
}
