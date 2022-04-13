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
case class For(id:String,e1:Expr,e2:Expr,e3:Expr) extends Expr

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
    case SList(SSym("for") :: SSym(id) :: e1 :: e2 :: e3 :: Nil) => For(id,parseE(e1),parseE(e2),parseE(e3))
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
    case For(x,e1,e2,e3) => SList(SSym("for") :: SSym(x) :: unparse(e1) :: unparse(e2) :: unparse(e3) :: Nil)
  }
  
  def unparseEs(exprs: List[Expr]) : List[SExpr] = exprs match {
    case Nil => Nil
    case (e::es) => unparse(e)::unparseEs(es)
  }
  
}    

case class InterpException(string: String) extends RuntimeException

object Interp {
  // see http://docs.scala-lang.org/overviews/collections/maps.html for details of Map class
  type VarStore = collection.mutable.Map[String,Int]
  
  def interp(e:Expr,debug:Int = 0): Int = {
    
    val st : VarStore = collection.mutable.Map[String,Int]()

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
        case For(x,e1,e2,e3) => {
          val v1 = interpE(e1)
          st += (x->v1)
          val v2 = interpE(e2)
          while (st(x) <= v2) {
            interpE(e3)
            st += (x->(st(x) + 1))
          }
          0
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

