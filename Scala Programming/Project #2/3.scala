/*Here’s another way to evaluate E2 programs. We first compile them into instructions for a small stack machine, and then execute those instructions. Thanks to the fact that Nand (“and” followed by “not”) is a universal logic gate, the stack machine needs just one logical operation! The other operations handle the boolean constants and provide general-purpose stack manipulation operations.

Once again, your job is to add support for “or” and “imp” operators. Once again, you will need to add new case classes Or and Imp for these two expression forms (with associated additions to the parser and pretty-printer). But this time you will implement the new operators by adding new clauses to the compile function.

The code for the stack machine is visible under the “Library Code” tab. You are not allowed to change the definition of the stack machine (e.g. by adding new instructions). This will require a bit of creativity. This wikipedia page about NAND logic may be useful.

Note that the top-level interface to the implementation is still called Process.process. Thus you can again re-use your tests from the previous interpreters to exercise these operators. The debug flag now has two useful non-zero values: setting it to 1 will print out the original parsed expression and the compiled code; setting it to 2 will also print a trace of the machine execution steps.*/

import SExprLibrary._

sealed abstract class Expr {
  override def toString() : String = Printer.print(this)
}
case class Bool(b:Boolean) extends Expr
case class Not(e:Expr) extends Expr
case class And(l:Expr,r:Expr) extends Expr
case class Xor(l:Expr,r:Expr) extends Expr
case class Or(l:Expr,r:Expr) extends Expr
case class Imp(l:Expr,r:Expr) extends Expr

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

object Printer {
  def print(expr: Expr) : String = unparse(expr).toString()

  def unparse(expr: Expr) : SExpr = expr match {
    case Bool(true) => SSym("T")
    case Bool(false) => SSym("F")
    case Not(e) => SList(SSym("~") :: unparse(e) :: Nil)
    case And(l,r) => SList(SSym("&") :: unparse(l) :: unparse(r) :: Nil)
    case Xor(l,r) => SList(SSym("^") :: unparse(l) :: unparse(r) :: Nil)
    case Or(l,r) => SList(SSym("|") :: unparse(l) :: unparse(r) :: Nil)
    case Imp(l,r) => SList(SSym("->") :: unparse(l) :: unparse(r) :: Nil)
  }
}    


object Compile {
  import Machine._
  def compile (e:Expr) : Machine.Program = e match {
    case Bool(b) => Const(b)::Nil
    case Not(e) => compile(e) ::: (Dup::Nand::Nil) 
    case And(e1,e2) => compile(e1) ::: compile(e2) ::: (Nand::Dup::Nand::Nil)
    case Xor(e1,e2) => compile(e1) ::: compile(e2) ::: (Dup::Swapx::Dup::Swapx::Nand::Dup::Swapx::Nand::Swapx::Nand::Nand::Nil)
    case Or(e1,e2) => compile(e1) ::: compile(e2) ::: (Dup::Swapx::Dup::Nand::Swapx::Nand::Nand::Nil)
    case Imp(e1,e2) => compile(e1) ::: compile(e2) ::: (Swap::Dup::Swapx::Swap::Dup::Swap::Dup::Nand::Nand::Nand::Nand::Nil)
 }
}

object Process {
  def process (s:String,debug:Int = 0) : Boolean = {
    try {
      val e : Expr = Parser.parse(s,debug)
      val p : Machine.Program = Compile.compile(e)
      Machine.exec(p,debug)
    } catch {
      case ex: MachineException => { println("Machine Error:" + ex.string) ; throw ex }
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


