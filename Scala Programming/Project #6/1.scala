/*
The solution template contains contains an interpreter for a language E7 that is similar to the E4 language lab week 4, but with the following changes:

There are no longer any top-level globals or named function definitions, so a program is now just a top-level expression.

The variable assignment (:=) and while expressions have been removed.

There are anonymous fun function expressions (“lamba” expressions). To introduce a named function, use let to bind the function name to a fun expression.

There is support for recursive letRec expressions to build recursive functions. Writing (letRec f b e) puts f in scope within b as well as within e. Although the grammar allows b to be any expression, the interpreter only works when b is a fun expression. For example, we could define and use a recursive factorial function as follows:

(letRec 
   fact 
   (fun (x) (if (<= x 0) 1 (* x (@ fact (- x 1)))))
   (@ fact 6))
Your task for this section:

(a) As provided, the interpreter code correctly supports nested function definitions and “downward funargs.” But it does not support truly first-class functions, because closures can refer to stack-allocated values. Write a simple example program that runs to completion (i.e. does not raise an interpreter exception or execute a non-terminating loop) but produces an incorrect integer answer. Provide the program as a complete s-expression at the indicated spot at the bottom of the solution template. Also state the correct answer that you would expect from a fully-implemented interpreter.

Your example will pass the (sole) spec test if it produces a different integer answer when run with the interpreter given here than it does using a correct working version of the interpreter (which is in the hidden library).*/


import SExprLibrary._

case class Program(body:Expr) {
  override def toString() : String = Printer.print(this)
}

sealed abstract class Expr {
  override def toString() : String = Printer.print(this)
}
case class Var(id:String) extends Expr
case class Num(i:Int) extends Expr
case class If(c:Expr,t:Expr,e:Expr) extends Expr
case class Write(e:Expr) extends Expr
case class Block(es:List[Expr]) extends Expr
case class Apply(f:Expr,es:List[Expr]) extends Expr
case class Add(l:Expr,r:Expr) extends Expr
case class Sub(l:Expr,r:Expr) extends Expr
case class Mul(l:Expr,r:Expr) extends Expr
case class Div(l:Expr,r:Expr) extends Expr
case class Rem(l:Expr,r:Expr) extends Expr
case class Le(l:Expr,r:Expr) extends Expr
case class Pair(l:Expr,r:Expr) extends Expr
case class Fst(e:Expr) extends Expr
case class Snd(e:Expr) extends Expr
case class IsPair(e:Expr) extends Expr
case class Eq(l:Expr,r:Expr) extends Expr
case class Let(id:String,e:Expr,b:Expr) extends Expr
case class Fun(params:List[String],body:Expr) extends Expr
case class LetRec(id:String,b:Expr,e:Expr) extends Expr

case class ParseException(string: String) extends RuntimeException

object Parser {
  def parse(str:String,debug:Int = 0): Program = {
    try {
      val a = parseP(SExprReader.read(str))
      if (debug > 0) println("Parsed program: " + a) 
      a
    } catch {
      case ex:ReadException => throw ParseException(ex.string)
    }
  }
  
  def parseP(sexpr: SExpr) : Program = Program(parseE(sexpr))

  def parseE(sexpr: SExpr) : Expr = sexpr match {
    case SNum(n) => Num(n)
    case SSym(id) => Var(id)
    case SList(SSym("if") :: c :: t :: e :: Nil) => If(parseE(c),parseE(t),parseE(e))
    case SList(SSym("write") :: e :: Nil) => Write(parseE(e))
    case SList(SSym("block") :: es) => Block(parseEs(es))
    case SList(SSym("@") :: e :: es) => Apply(parseE(e),parseEs(es))
    case SList(SSym("+") :: l :: r :: Nil) => Add(parseE(l),parseE(r))
    case SList(SSym("-") :: l :: r :: Nil) => Sub(parseE(l),parseE(r))
    case SList(SSym("*") :: l :: r :: Nil) => Mul(parseE(l),parseE(r))
    case SList(SSym("/") :: l :: r :: Nil) => Div(parseE(l),parseE(r))
    case SList(SSym("%") :: l :: r :: Nil) => Rem(parseE(l),parseE(r))
    case SList(SSym("<=") :: l :: r :: Nil) => Le(parseE(l),parseE(r))
    case SList(SSym("pair") :: l :: r :: Nil) => Pair(parseE(l),parseE(r))
    case SList(SSym("fst") :: e :: Nil) => Fst(parseE(e))
    case SList(SSym("snd") :: e :: Nil) => Snd(parseE(e))
    case SList(SSym("isPair") :: e :: Nil) => IsPair(parseE(e))
    case SList(SSym("==") :: l :: r :: Nil) => Eq(parseE(l),parseE(r))
    case SList(SSym("let") :: SSym(id) :: e :: b :: Nil) => Let(id,parseE(e),parseE(b))
    case SList(SSym("fun") :: SList(fps) :: e :: Nil) => Fun(parseIs(fps),parseE(e))
    case SList(SSym("letRec") :: SSym(id) :: b :: e :: Nil) => LetRec(id,parseE(b),parseE(e))
    case _ => throw ParseException("Cannot parse expression:" + sexpr)
  }
  
  def parseEs(sexprs : List[SExpr]) : List[Expr] = sexprs match {
    case Nil => Nil
    case (e :: es) => parseE(e) :: parseEs(es)
  }

  def parseIs(sexprs : List[SExpr]) : List[String] = sexprs match {
    case Nil => Nil
    case (SSym(x) :: is) => x :: parseIs(is)
    case (i::_)  => throw ParseException("Cannot parse identifier:" + i)
  }
}

object Printer {
  // These methods are distinguished by the type of their parameter.
  def print(p: Program) : String = unparse(p).toString()
  def print(e: Expr) : String = unparse(e).toString()

  // These methods are distinguished by the type of their parameter.
  def unparse(p: Program) : SExpr = unparse(p.body)
  def unparse(expr: Expr) : SExpr = expr match {
    case Num(n) => SNum(n)
    case Var(x) => SSym(x)
    case If(c,t,e) => SList(SSym("if") :: unparse(c) :: unparse(t) :: unparse(e) :: Nil)
    case Write(e) => SList(SSym("write") :: unparse(e) :: Nil)
    case Block(es) => SList(SSym("block") :: unparseEs(es))
    case Apply(e,es) => SList(SSym("@") :: unparse(e) :: unparseEs(es))
    case Add(l,r) => SList(SSym("+") :: unparse(l) :: unparse(r) :: Nil)
    case Sub(l,r) => SList(SSym("-") :: unparse(l) :: unparse(r) :: Nil)
    case Mul(l,r) => SList(SSym("*") :: unparse(l) :: unparse(r) :: Nil)
    case Div(l,r) => SList(SSym("/") :: unparse(l) :: unparse(r) :: Nil)
    case Rem(l,r) => SList(SSym("%") :: unparse(l) :: unparse(r) :: Nil)
    case Le(l,r) => SList(SSym("<=") :: unparse(l) :: unparse(r) :: Nil)
    case Pair(l,r) => SList(SSym("pair") :: unparse(l) :: unparse(r) :: Nil)
    case Fst(e) => SList(SSym("fst") :: unparse(e)  :: Nil)
    case Snd(e) => SList(SSym("snd") :: unparse(e)  :: Nil)
    case IsPair(e) => SList(SSym("isPair") :: unparse(e) :: Nil)
    case Eq(l,r) => SList(SSym("==") :: unparse(l) :: unparse(r) :: Nil)
    case Let(x,e,b) => SList(SSym("let") :: SSym(x) :: unparse(e) :: unparse(b) :: Nil) 
    case Fun(xs,b) => SList(SSym("fun") :: SList(unparseIs(xs)) :: unparse(b) :: Nil)
    case LetRec(x,b,e) => SList(SSym("letRec") :: SSym(x) :: unparse(b) :: unparse(e) :: Nil)
  }
  
  def unparseEs(exprs: List[Expr]) : List[SExpr] = exprs match {
    case Nil => Nil
    case (e::es) => unparse(e)::unparseEs(es)
  }
  
  def unparseIs(is: List[String]) : List[SExpr] = is match {
    case Nil => Nil
    case (i :: is) => SSym(i) ::unparseIs(is) 
  }
}    

case class InterpException(string: String) extends RuntimeException

object Interp {

  sealed abstract class Value
  case class NumV(num:Int) extends Value
  case class PairV(heapIndex:Int) extends Value
  case class ClosureV(xs:List[String],b:Expr,env:Env,fp:Int) extends Value

  class Store {
    case class UndefinedStoreContents(string : String) extends RuntimeException
    private val contents = collection.mutable.SortedMap[Int,Value]()
    def get(i:Int) = contents.getOrElse(i, throw UndefinedStoreContents("" + i))
    def set(i:Int,v:Value) = contents += (i->v)
    override def toString : String = contents.toString
  }

  class HeapStore extends Store {
    private var nextFreeIndex:Int = 0
    def allocate(n:Int) : Int = {
      val i = nextFreeIndex
      nextFreeIndex += n
      i
    }
    // there is no mechanism for deallocation, but pretend there is a garbage collector
    override def toString : String = "[next=" + nextFreeIndex + "] " + super.toString
  }

  class StackStore extends Store {
    private var stackPointer:Int = 0   // next free index in stack
    // allocate specified number of slots on stack, returning base of alocation
    def push(n:Int) : Int = {
      val base = stackPointer
      stackPointer += n
      base
    }
    // pop specified number of slots off stack
    def pop(n:Int) : Unit = {
      stackPointer -= n
    }
    // return offset of current stack pointer from specified base
    def currOffset(base:Int) : Int =
      stackPointer - base
    override def toString : String = "[sp=" + stackPointer + "] " + super.toString
  }

  abstract class Location() {
  }
  case class StackFrameOffset(offset:Int) extends Location {
  }
  case class HeapAddr(addr:Int) extends Location {  
  }

  type Env = Map[String,Location]

  val emptyEnv : Env =  Map[String,Location]() 

  def interp(p:Program,debug:Int = 0): Int = {
    if (debug > 0) println("Program: " + p)

    val heap = new HeapStore()
    val stack = new StackStore()

    def getLocation(fp:Int,a:Location) = a match {
      case StackFrameOffset(offset) => stack.get(fp+offset)
      case HeapAddr(addr) => heap.get(addr)  
    }

    def setLocation(fp:Int,a:Location,v:Value) = a match {
      case StackFrameOffset(offset) => stack.set(fp+offset,v)
      case HeapAddr(addr) => heap.set(addr,v)  
    }

    def interpVar(env:Env,x:String) : Location =
      env.getOrElse(x, throw InterpException("undefined variable:" + x))

    def buildClosure(fp:Int,params:List[String],body:Expr,env:Env) = 
      ClosureV(params,body,env,fp)

    def interpFun(params:List[String],body:Expr,cenv:Env,cfp:Int,fp:Int,argCount:Int) : Value = {
      if (debug > 1)
        println("entering function at frame pointer = " + fp)
      if (params.length != argCount)
        throw InterpException("wrong number of arguments in function application") 
      // define the environment for the body to contain the closure bindings
      // adjusted for the different frame pointer
      var benv = emptyEnv
      for ((x,l) <- cenv) l match {
        case StackFrameOffset(offset) =>
          benv += (x -> StackFrameOffset(cfp-fp+offset))
        case HeapAddr(_) => // never actually happens in current code
          benv += (x -> l)
      }
      // plus the arguments that the caller pushed on the stack 
      var offset = 0
      for (x <- params) {
        benv += (x -> StackFrameOffset(offset))
        offset += 1
      }
      // evaluate function body 
      val v = interpE(benv,fp,body)
      if (debug > 1)
        println("returning " + v)
      v
    }

    def interpE(env:Env,fp:Int,e:Expr) : Value = {
      if (debug > 1) {
        println("expr = "+ e)
        println("env = " + env)
        println("frame pointer = " + fp)
        println("stack = " + stack)
        println("heap = " + heap)
      } 
      val r = e match {
        case Num(n) => NumV(n)
        case Var(x) => getLocation(fp,interpVar(env,x))
        case Add(l,r) => (interpE(env,fp,l),interpE(env,fp,r)) match {
          case (NumV(lv),NumV(rv)) => NumV(lv+rv)
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case Sub(l,r) => (interpE(env,fp,l),interpE(env,fp,r)) match {
          case (NumV(lv),NumV(rv)) => NumV(lv-rv)
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case Mul(l,r) => (interpE(env,fp,l),interpE(env,fp,r)) match {
          case (NumV(lv),NumV(rv)) => NumV(lv*rv)
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case Div(l,r) => (interpE(env,fp,l),interpE(env,fp,r)) match {
          case (NumV(lv),NumV(rv)) => if (rv!=0) NumV(lv/rv) else throw InterpException("divide by zero")
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case Rem(l,r) => (interpE(env,fp,l),interpE(env,fp,r)) match {
          case (NumV(lv),NumV(rv)) => if (rv!=0) NumV(lv%rv) else throw InterpException("divide by zero")
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case Le(l,r) => (interpE(env,fp,l),interpE(env,fp,r)) match {
          case (NumV(lv),NumV(rv)) => NumV(if (lv <= rv) 1 else 0)
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case If(c,t,e) => interpE(env,fp,c) match {
          case NumV(0) => interpE(env,fp,e)
          case NumV(_) => interpE(env,fp,t)
          case _ => throw InterpException("non-numeric argument to If")
        }
        case Write(e) => {
          val v = interpE(env,fp,e)
          def show(v:Value) : String = v match {
            case NumV(i) => "" + i
            case PairV(a) => "(" + show(heap.get(a)) + "." + show(heap.get(a+1)) + ")"
            case ClosureV(_,_,_,_) => "<function>"
          }
          println(show(v)); 
          v
        }
        case Block(es) => {
          var v:Value = NumV(0)
          for (e <- es) 
            v = interpE(env,fp,e)
          v
        }
        case Apply(e,es) => interpE(env,fp,e) match {
          case ClosureV(xs,b,cenv,cfp) => {
            // evaluate actual arguments and push them on stack
            val fpNew = stack.push(es.length)
            var offset = 0
            for (e <- es) {
              val v = interpE(env,fp,e)
              setLocation(fpNew,StackFrameOffset(offset),v)
              offset += 1
            }
            val v = interpFun(xs,b,cenv,cfp,fpNew,es.length)
            // restore stack
            stack.pop(es.length)
            v
          }
          case _ => throw InterpException("attempt to apply non-function val ue")
        }
        case Pair(l,r) => {
          val lv = interpE(env,fp,l)
          val rv = interpE(env,fp,r)
          val a = heap.allocate(2)
          heap.set(a,lv)
          heap.set(a+1,rv) 
          PairV(a)
        }
        case Fst(e) => interpE(env,fp,e) match {
          case PairV(a) => heap.get(a)
          case _ => throw InterpException("non-pair argument to fst")
        }
        case Snd(e) => interpE(env,fp,e) match {
          case PairV(a) => heap.get(a+1)
          case _ => throw InterpException("non-pair argument to snd")
        }
        case IsPair(e) => interpE(env,fp,e) match {
          case PairV(_) => NumV(1)
          case _ => NumV(0)
        }
        case Eq(l,r) => {
          val lv = interpE(env,fp,l)
          val rv = interpE(env,fp,r)
          NumV(if (lv == rv) 1 else 0)   // reference equality on ints and pairs; not useful on closures
        }
        case Let(x,e,b) => {
          val v = interpE(env,fp,e)
          val loc = StackFrameOffset(stack.currOffset(fp))
          stack.push(1)
          setLocation(fp,loc,v)
          val r = interpE(env + (x->loc),fp,b)
          stack.pop(1)
          r
        }
        case Fun(xs,b) => buildClosure(fp,xs,b,env)
        case LetRec(f,Fun(xs,b),e) => {
          val loc = StackFrameOffset(stack.currOffset(fp))
          val renv = env + (f -> loc)
          stack.push(1)
          setLocation(fp,loc,buildClosure(fp,xs,b,renv))
          val r = interpE(renv,fp,e)
          stack.pop(1)
          r
        }
        case LetRec(f,_,_) => throw InterpException("body of letRec must be fun")
      }
      if (debug > 1) 
        println("result = "+ r)
      r
    }

    // process the main body expression
    val v = interpE(emptyEnv,0,p.body)
    if (debug > 0) println("Body evaluates to: " + v)
    v match {
      case NumV(n) => n
      case _ => throw InterpException("main body returns non-integer")
    }
  }
}

object Process {
  def process (s:String,debug:Int = 0) : Int = {
    try {
      val p : Program = Parser.parse(s,debug)
      Interp.interp(p,debug)
    } catch {
      case ex: InterpException => { println("Interp Error:" + ex.string) ; throw ex }
      case ex: ParseException => { println("Parser Error:" + ex.string) ; throw ex }
    }
  }
}

// The following code may be useful for stand-alone development and
// testing from the command line. (It is not useful when developing
// or testing within WebLab.)
object ProcessFile {
  import scala.io.Source
  def main (argv: Array[String]) = {
    val s = Source.fromFile(argv(0)).getLines.mkString("\n")
    val d = if (argv.length > 1) argv(1).toInt else 0
    val r = Process.process(s,d)
    println("result = " + r)
    ()
  }
}

object Example {
  
    val example = """
     (let f (fun (x)(fun (y)(isPair x)))
        (let g (@ f (pair 41 42)) ( @ g 2 )
         ))
"""

  // This program should produce the following result: [correct output should be 1]

}




