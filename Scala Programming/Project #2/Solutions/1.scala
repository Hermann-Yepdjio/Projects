import SExprLibrary._

sealed abstract class Expr {
  override def toString() : String = Printer.print(this)
}
case class Bool(b:Boolean) extends Expr
case class Not(e:Expr) extends Expr
case class And(l:Expr,r:Expr) extends Expr
case class Or(l:Expr,r:Expr) extends Expr
case class Xor(l:Expr,r:Expr) extends Expr
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
    case SList(SSym("|") :: l :: r :: Nil) => Or(parse(l),parse(r))
    case SList(SSym("^") :: l :: r :: Nil) => Xor(parse(l),parse(r))
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
    case Or(l,r) => SList(SSym("|") :: unparse(l) :: unparse(r) :: Nil)
    case Xor(l,r) => SList(SSym("^") :: unparse(l) :: unparse(r) :: Nil)
    case Imp(l,r) => SList(SSym("->") :: unparse(l) :: unparse(r) :: Nil)
  }
}    


object Interp {
  def interp(e:Expr) : Boolean =
    e match {
      case Bool(b) => b
      case Not(e) => !interp(e)
      case And(l,r)=> interp(l) && interp(r)
      case Or(l,r) => interp(l) || interp(r)
      case Xor(l,r) => interp(l) ^ interp(r)
      case Imp(l,r) => !interp(l) || interp(r)
    }
}

object Process {
  def process (s:String,debug:Int = 0) : Boolean = {
    try {
      val e : Expr = Parser.parse(s,debug)
      val r = Interp.interp(e)
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

