/*The solution template contains a different implementation of the E3 language. This version first translates the AST to stack machine code, and then executes it.

Once again, your task is to add support for a for expression, with the same semantics as in the first problem. This time you must achieve this by extending the translator from AST to stack code. The stack machine, whose code is visible under the “Library Code” tab, is broadly similar in style to the one used in Lab 2, but with many differences (especially that it supports arithmetic and a store). You must not change the stack machine.

This problem is a little tricky! You may find DUP and SWAP particularly useful. As before, setting debug level 1 will show you the generated stack machine code, and level 2 will trace the behavior of the machine step-by-step.

The same tests you used for the first problem should be work again here.*/

import SExprLibrary._

sealed abstract class Expr {
  override def toString() : String = Printer.print(this)
}
case class Var(id:String) extends Expr
case class Num(i:Int) extends Expr
case class Assgn(id:String,e:Expr) extends Expr
case class While(c:Expr,e:Expr) extends Expr
case class If(c:Expr,t:Expr,e:Expr) extends Expr
case class Write(e:Expr) extends Expr
case class Block(es:List[Expr]) extends Expr
case class Add(l:Expr,r:Expr) extends Expr
case class Sub(l:Expr,r:Expr) extends Expr
case class Mul(l:Expr,r:Expr) extends Expr
case class Div(l:Expr,r:Expr) extends Expr
case class Rem(l:Expr,r:Expr) extends Expr
case class Le(l:Expr,r:Expr) extends Expr


case class ParseException(string: String) extends RuntimeException

object Parser {
  def parse(str:String,debug:Int = 0): Expr = {
    try {
      val a = parseE(SExprReader.read(str))
      if (debug > 0) println("Parsed expression: " + a) 
      a
    } catch {
      case ex:ReadException => throw ParseException(ex.string)
    }
  }
  
  def parseE(sexpr: SExpr) : Expr = sexpr match {
    case SNum(n) => Num(n)
    case SSym(id) => Var(id)
    case SList(SSym(":=") :: SSym(id) :: e :: Nil) => Assgn(id,parseE(e))
    case SList(SSym("while") :: c :: e :: Nil) => While(parseE(c),parseE(e))
    case SList(SSym("if") :: c :: t :: e :: Nil) => If(parseE(c),parseE(t),parseE(e))
    case SList(SSym("write") :: e :: Nil) => Write(parseE(e))
    case SList(SSym("block") :: es) => Block(parseEs(es))
    case SList(SSym("+") :: l :: r :: Nil) => Add(parseE(l),parseE(r))
    case SList(SSym("-") :: l :: r :: Nil) => Sub(parseE(l),parseE(r))
    case SList(SSym("*") :: l :: r :: Nil) => Mul(parseE(l),parseE(r))
    case SList(SSym("/") :: l :: r :: Nil) => Div(parseE(l),parseE(r))
    case SList(SSym("%") :: l :: r :: Nil) => Rem(parseE(l),parseE(r))
    case SList(SSym("<=") :: l :: r :: Nil) => Le(parseE(l),parseE(r))
  case _ => throw ParseException("Cannot parse expression:" + sexpr)
  }
  
  // Note: Later on, we'll see that this would be easier to write using a `map` expression
  def parseEs(sexprs : List[SExpr]) : List[Expr] = sexprs match {
    case Nil => Nil
    case (e :: es) => parseE(e) :: parseEs(es)
  }

}

object Printer {
  def print(expr: Expr) : String = unparse(expr).toString()

  def unparse(expr: Expr) : SExpr = expr match {
    case Num(n) => SNum(n)
    case Var(x) => SSym(x)
    case Assgn(x,e) => SList(SSym(":=") :: SSym(x) :: unparse(e) :: Nil)
    case While(c,e) => SList(SSym("while") :: unparse(c) :: unparse(e) :: Nil)
    case If(c,t,e) => SList(SSym("if") :: unparse(c) :: unparse(t) :: unparse(e) :: Nil)
    case Write(e) => SList(SSym("write") :: unparse(e) :: Nil)
    case Block(es) => SList(SSym("block") :: unparseEs(es))
    case Add(l,r) => SList(SSym("+") :: unparse(l) :: unparse(r) :: Nil)
    case Sub(l,r) => SList(SSym("-") :: unparse(l) :: unparse(r) :: Nil)
    case Mul(l,r) => SList(SSym("*") :: unparse(l) :: unparse(r) :: Nil)
    case Div(l,r) => SList(SSym("/") :: unparse(l) :: unparse(r) :: Nil)
    case Rem(l,r) => SList(SSym("%") :: unparse(l) :: unparse(r) :: Nil)
    case Le(l,r) => SList(SSym("<=") :: unparse(l) :: unparse(r) :: Nil)
  }
  
  def unparseEs(exprs: List[Expr]) : List[SExpr] = exprs match {
    case Nil => Nil
    case (e::es) => unparse(e)::unparseEs(es)
  }
  
}    

object Compile {
  import Machine._
  var nextLabel: Int = 0
  def comp (e:Expr) : Machine.Program = e match {
    case Var(x) => Load(x)::Nil
    case Num(i) => Const(i)::Nil
    case Assgn(x,e) => comp(e) ::: Dup::Store(x)::Nil
    case While(c,b) => {
      val topLab = newLabel()
      val bottomLab = newLabel()
      Label(topLab) :: comp(c) :::
      Branchz(bottomLab) :: comp(b) :::  Pop ::  // throw away value of body
      Branch(topLab) :: Label(bottomLab) :: Const(0) :: Nil // overall expression evaluates to 0
    }
    case If(c,t,f) => {
      val falseLab = newLabel()
      val joinLab = newLabel()
      comp(c) ::: Branchz(falseLab) :: comp(t) :::
      Branch(joinLab) :: Label(falseLab) :: comp(f) :::
      Label(joinLab) :: Nil
    }
    case Write(e) => comp(e) ::: Dup :: Print :: Nil
    case Block(es) => {
      def c(es:List[Expr]) : List[Instr] = es match {
        case Nil => Const(0)::Nil
        case e::Nil => comp(e)
        case e::es => comp(e) ::: Pop :: c(es)
      }
      c(es)
    }
    case Add(e1,e2) => comp(e1) ::: comp(e2) ::: Plus::Nil
    case Sub(e1,e2) => comp(e1) ::: comp(e2) ::: Const(-1)::Times::Plus::Nil
    case Mul(e1,e2) => comp(e1) ::: comp(e2) ::: Times::Nil
    case Div(e1,e2) => comp(e1) ::: comp(e2) ::: Divrem::Pop::Nil
    case Rem(e1,e2) => comp(e1) ::: comp(e2) ::: Divrem::Swap::Pop::Nil
    case Le(e1,e2)  => comp(e1) ::: comp(e2) ::: Lessequ::Nil
  }
  def newLabel() = {
    val next = nextLabel
    nextLabel = nextLabel + 1
    next
  }
  def compile(e:Expr) = {
    nextLabel = 0
    comp(e)
  }
}

object Process {
  def process (s:String,debug:Int = 0) : Int = {
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
object Imperative {
  import scala.io.Source
  def main (argv: Array[String]) = {
    val s = Source.fromFile(argv(0)).getLines.mkString("\n")
    val d = if (argv.length > 1) argv(1).toInt else 0
    Process.process(s,d)
    ()
  }
}

