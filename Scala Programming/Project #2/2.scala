/*Here is another version of the E2 interpreter that uses object-oriented-style dynamic dispatch, rather than pattern matching, to traverse the AST and evaluate the expression. It should behave just like the last interpreter.

Make the same language extensions (“or” and “implies”) as in the previous exercise. Thanks to the wrapper definition Process.process you should be able to re-use the tests you wrote for that exercise.

Again, you will need to add new case classes called Or and Imp but the code for interpreting these classes and for unparsing them goes in a different place than before. (The parser changes are the same as before, though.)

By the way, note that we continue to use case classes to represent the various forms of expressions (even though we no longer perform pattern matching over them) because that gives us simple and convenient object constructors for free.*/

import SExprLibrary._

case class InterpException(string: String) extends RuntimeException

sealed abstract class Expr {
  def unparse() : SExpr
  def interp() : Boolean
  
  override def toString() : String = unparse().toString
}
case class Bool(b:Boolean) extends Expr {
  def unparse() = SSym(if (b) "T" else "F")
  def interp() = b
}
case class Not(e:Expr) extends Expr {
  def unparse() = SList(SSym("~") :: e.unparse() :: Nil)
  def interp() = !e.interp()
}
case class And(l:Expr,r:Expr) extends Expr {
  def unparse() = SList(SSym("&") :: l.unparse() :: r.unparse() :: Nil)
  def interp() = l.interp() && r.interp()
}

case class Xor(l:Expr,r:Expr) extends Expr {
  def unparse() = SList(SSym("^") :: l.unparse() :: r.unparse() :: Nil)
  def interp() = l.interp() ^ r.interp()
}

case class Or(l:Expr,r:Expr) extends Expr 
{
  def unparse() = SList(SSym("|") :: l.unparse() :: r.unparse() :: Nil)
  def interp() = l.interp() | r.interp()
}

case class Imp(l:Expr,r:Expr) extends Expr {
  def unparse() = SList(SSym("->") :: l.unparse() :: r.unparse() :: Nil)
  def interp() = l.interp() <= r.interp()
}

case class ParseException(string: String) extends RuntimeException

object Parser {
  def parse(str:String,debug:Int = 0): Expr = {
    try {
      val a = parse(SExprReader.read(str))
      if (debug > 0) println("Parsed expression: " + a) 
      a
    } catch {
      case ex:ReadException => throw ParseException(ex.string)
    }	
  }

  def parse(sexpr: SExpr) : Expr = sexpr match {
    case SSym("T") => Bool(true)
    case SSym("F") => Bool(false)
    case SList(SSym("~") :: e :: Nil) => Not(parse(e))
    case SList(SSym("&") :: l :: r :: Nil) => And(parse(l),parse(r))
    case SList(SSym("^") :: l :: r :: Nil) => Xor(parse(l),parse(r))
    case SList(SSym("|") :: l :: r :: Nil) => Or(parse(l),parse(r))
    case SList(SSym("->") :: l :: r :: Nil) => Imp(parse(l),parse(r))
    case _ => throw ParseException("Cannot parse:" + sexpr)
  }
}

object Process {
  def process (s:String,debug:Int = 0) : Boolean = {
    try {
      val e : Expr = Parser.parse(s,debug)
      val r = e.interp()
      if (debug > 0) println("Result:" + r)
      r
    } catch {
      case ex: ParseException => { println("Parser Error:" + ex.string) ; throw ex }
      case ex: Exception => { println("Uncaught Exception:" + ex); throw ex }
    }
  }
}

// The following code may be useful for stand-alone development and
// testing from the command line. (It is not useful when developing
// or testing within WebLab.)
object Bools {
  import scala.io.Source
  def main (argv: Array[String]) = {
    val s = Source.fromFile(argv(0)).getLines.mkString("\n")
    val d = if (argv.length > 1) argv(1).toInt else 0
    Process.process(s,d)
    ()
  }
}


