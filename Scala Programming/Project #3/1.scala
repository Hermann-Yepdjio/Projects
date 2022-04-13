/*The solution template contains an interpreter for a simple imperative expression language, which we’ll call E3. An informal semantics for this language is as follows. The evaluation of each expression yields a single integer result.

A variable x yields its current value. Every variable is implicitly initialized to 0 at the beginning of program execution.
An integer i yields itself.
Evaluating the assignment expression (:= x e) evaluates e, assigns the resulting value into variable x, and yields that value.
Evaluating (while c b) evaluates expression c; if the result is non-zero, expression b is evaluated and the entire while expression is evaluated again; otherwise the evaluation of the while is complete. A while expression always yields the value 0.
Evaluating (if c t f) evaluates c;if the result is non-zero, then expression t is evaluated and the resulting value is yielded as the value of the if expression; otherwise expression f is evaluated and the resulting value is yielded as the value of the if expression.
Evaluating (write e) evaluates e, prints the resulting value (followed by a newline) to standard output, and yields that value.
Evaluating (block e1 e2 ... en) evaluates e1,e2,…,en in that order, and yields the value of en. If n = 0 the block expression yields 0.
Evaluating (+ e1 e2) evaluates e1 and e2 and yields the sum of their values.
The other arithmetic operations are similar.
Evaluating (<= e1 e2) evaluates e1 and e2 and compares their values. If the first is less than or equal to the second, the expression yields 1; otherwise it yields 0.
An example program that computes prime numbers is included as part of the test template.

Your task is to add support for a new for expression to this interpreter by adding a new case class, parsing and unparsing code, and interpretation code.

In detail, the concrete form (for x e1 e2 e3) (where x is an identifier and e1,e2,e3 are expressions) is evaluated as follows:

First evaluate e1 to a value v1 and store into x.
Then evaluate e2 to a value v2.
Then repeat the following steps
– Fetch the value of x (call that vx)
– If vx > v2 then terminate evaluation of the for loop yielding the value 0
– Otherwise, evaluate e3 and discard the yielded result; then fetch the value of x, add 1 to it, and store the result back into x.
For example, (for i 1 10 (write i)) writes the numbers from 1 to 10 and yields the value 0.

Make sure you get the order of evaluation right. For example, the bizarre expression

(for i (block (:= j (+ i 9)) 1) (- j i) (block (write i) (:= i (+ i 3))))

writes the numbers 1 and 5 and yields the value 0.*/

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

case class InterpException(string: String) extends RuntimeException

object Interp {
  // see http://docs.scala-lang.org/overviews/collections/maps.html for details of Map class
  type Store = collection.mutable.Map[String,Int]
  
  def interp(e:Expr,debug:Int = 0): Int = {
    
    val st : Store = collection.mutable.Map[String,Int]()

    def interpE(e:Expr) : Int = {
      if (debug > 1) {
        println("  expr = "+ e);
        println("  store = " + st)
      }
      e match {
        case Num(n) => n
        case Add(l,r) => {
          val vr = interpE(r)
          val vl = interpE(l)
          vl + vr
        }
        case Sub(l,r) => {
          val vr = interpE(r)
          val vl = interpE(l)
          vl - vr
        }
        case Mul(l,r) => {
          val vr = interpE(r)
          val vl = interpE(l)
          vl * vr
        }
        case Div(l,r) => {
          val vr = interpE(r)
          val vl = interpE(l)
          if (vr == 0)
            throw InterpException("divide by zero")
          else
            vl/vr
        }
        case Rem(l,r) => {
          val vr = interpE(r)
          val vl = interpE(l)
          if (vr == 0)
            throw InterpException("divide by zero")
          else
            vl%vr
        }
        case If(c,t,e) => if (interpE(c) != 0) interpE(t) else interpE(e)
        case Le(l,r) => {
          val vr = interpE(r)
          val vl = interpE(l)
          if (vl <= vr) 1 else 0
        }
        case Var(x) => st get x match {
          case Some(v) => v
          case None => 0
        }
        case Assgn(x,e) => {
          val v = interpE(e)
          st += (x->v)
          v
        }
        case While(c,b) =>
          if (interpE(c) == 0)
            0
          else {
            interpE(b);
            interpE(e)
          }
        case Write(e) => {
          val v = interpE(e)
      	  println(v);
          v
        }
        case Block(es) => {
          var v = 0
          for (e <- es) 
            v = interpE(e)
          v
        }
      }
    }

    val v = interpE(e)
    if (debug > 0) println("Evaluates to: " + v)
    v
  } 
  
}

object Process {
  def process (s:String,debug:Int = 0) : Int = {
    try {
      val e : Expr = Parser.parse(s,debug)
      Interp.interp(e,debug)
    } catch {
      case ex: InterpException => { println("Interp Error:" + ex.string) ; throw ex }
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

