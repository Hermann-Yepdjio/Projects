/*
The solution template contains an interpreter for an extended version of last week’s E3 expression language.
Let’s call this new language E4. Informal semantics for the new or changed features are as follows.

Programs manipulate values, which can be either integers or pairs. A pair in turn contains two values.

A program consists of a sequence of global definitions, a sequence of (top-level) function definitions, and a main body expression. It is evaluated by elaborating each global definition in order, and then evaluating the main body expression, whose value is the program result. The program result value must be an integer (not a pair).

A global definition (x e) is elaborated by evaluating its initializing expression e to a value v and then binding x to v. Each global is in scope from just below its definition to the end of the program, i.e. in subsequent global definitions, all function bodies, and the main body. If the program attempts to access a global’s value before it has been initialized, the result is undefined (and unspecified; no such programs are used in any of the specification tests).

A function definition (f (x1 ... xn) b) defines a function with name f, zero or more formal parameters x1,…,xn, and body expression b. All functions are in scope throughout the program, including within all global definitions, all function bodies (so functions can be recursive or mutually recursive), and the main body.

Functions and variables live in separate name spaces, so their names may overlap. The language uses static scope rules. Functions have zero or more formal parameters, whose scope is statically limited to the body of the function. If a formal parameter has the same name as a global, the parameter hides the global. It is a checked run-time error to use an undefined function or variable name. (Note that, with respect to variables, this is a change from E3.)

If the same name appears twice in the list of globals, the list of functions, or the parameters to a given function, the behavior of the program is undefined (and unspecified: no such programs are used in any of the specification tests).

A variable x can refer to either a formal parameter or a global.

Evaluating the function application expression (@ f e1 e2 ... en) evaluates e1,e2,…,en in that order, binds the resulting values to the n formal parameters of function f, evaluates the body of f in the resulting environment, and yields the resulting value. It is a checked run-time error if f doesn’t exist or has fewer or more than n parameters.

Evaluating (pair e1 e2) evaluates e1 and e2 (in that order) to values v1 and v2 and yields a new pair whose left element is v1 and right element is v2.

Evaluating (fst e) evaluates e to a pair value, and extracts and yields the left element value. It is a checked run-time error if e evaluates to a non-pair value.

Evaluating (snd e) evaluates e to a pair value, and extracts and yields the right element value. It is a checked run-time error if e evaluates to a non-pair value.

Evaluating (isPair e) evaluates e and yields 1 if the result is a pair and 0 otherwise.

Evaluating (== e1 e2) evalautes e1 and e2 (in that order) to values v1 and v2 and yields 1 if the two values are equal and 0 otherwise. Each value may be either a number or a pair. Two pair values are equal iff they refer to the same pair in memory (i.e. using reference equality, not structural equality).

The value tested by a while or if must be an integer; otherwise a checked run-time error results.

The value written by a write can be either an integer or a pair.

The arithmetic operators (+,-,*,/,%,<=) work only on integers; it is a checked run-time error to apply them to a pair.

Task 1. Your first task is to extend the interpreter to support local variables using a new expression form (let x e b). The informal semantics of this is as follows: evaluate e, bind the resulting value to the newly created local variable x, evaluate expression b in the resulting environment, and yield the resulting value. The scope of x is just the expression b. A local variable introduced by a let binding hides any global, formal parameter, or outer local variable with the same name.

For example, the program

(((a 10))
 ()
 (let a 1
    (let b a
       (block 
          (let a 100
             (block
                (:= b (+ a b))
                (:= a 0)))
          (+ a b)))))
evaluates to 102.

Implementation note: Allocate storage for the let-bound variable in the current stack frame. The currOffset method in StackStore should be useful. It should not be necessary to change any existing code.

Task 2. Your second task is to add support for making pairs mutable. Do this by adding two new expression forms (setFst p e) and (setSnd p e). The inf
ormal semantics for setFst are: evaluate expression p to a pair value pv, evaluate expression e to a value v, update the left component of pv with v, and yield the (mutated) pair pv as the result of the expression. The semantics for setSnd are similar except that the right component is changed. For either form, it is a checked runtime error is pv is not a pair.

Note that it is now possible write a program that causes a write expression to go into an infinite loop when trying to print a pair; don’t try to fix this problem, but be aware of it when testing!*/


import SExprLibrary._

case class Program(gdefs:List[GlobalDef], fdefs:List[FunDef], body:Expr) {
  override def toString() : String = Printer.print(this)
}

case class GlobalDef(id:String, d:Expr) {
  override def toString() : String = Printer.print(this)
}

case class FunDef(name:String, params:List[String], body:Expr) {
  override def toString() : String = Printer.print(this)
}

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
case class Apply(f:String,es:List[Expr]) extends Expr
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
case class Let(x:String, e:Expr, b:Expr) extends Expr
case class SetFst(p:Expr,e:Expr) extends Expr
case class SetSnd(p:Expr,e:Expr) extends Expr

case class ParseException(string: String) extends RuntimeException

object Parser {
  def parse(str:String,debug:Int = 0): Program = {
    try {
      val a = parseP(SExprReader.read(str))
      if (debug > 0)
        println("Parsed program: " + a)
      a
    } catch {
      case ex:ReadException => throw ParseException(ex.string)
    }
  }
  
  def parseP(sexpr: SExpr) : Program = sexpr match {
    case SList(SList(gs) :: SList(fs) :: e :: Nil) => Program(parseGs(gs),parseFs(fs),parseE(e))
    case _ => throw ParseException("Cannot parse program:" + sexpr)
  }

  def parseG(sexpr: SExpr) : GlobalDef = sexpr match {
    case SList(SSym(id) :: e :: Nil) => GlobalDef(id,parseE(e))
    case _ => throw ParseException("Cannot parse global definition:" + sexpr)
  }

  def parseGs(sexprs : List [SExpr]) : List[GlobalDef] = sexprs match {
    case Nil => Nil
    case (d :: ds) => parseG(d) :: parseGs(ds)
  }

  def parseF(sexpr: SExpr): FunDef = sexpr match {
    case SList(SSym(id) :: SList(ids) :: e :: Nil) => FunDef(id,parseIs(ids),parseE(e))
    case _ => throw ParseException("Cannot parse function definition:" + sexpr)
  }
  
  def parseFs(sexprs : List [SExpr]) : List[FunDef] = sexprs match {
    case Nil => Nil
    case (d :: ds) => parseF(d) :: parseFs(ds)
  }

  def parseE(sexpr: SExpr) : Expr = sexpr match {
    case SNum(n) => Num(n)
    case SSym(id) => Var(id)
    case SList(SSym(":=") :: SSym(id) :: e :: Nil) => Assgn(id,parseE(e))
    case SList(SSym("while") :: c :: e :: Nil) => While(parseE(c),parseE(e))
    case SList(SSym("if") :: c :: t :: e :: Nil) => If(parseE(c),parseE(t),parseE(e))
    case SList(SSym("write") :: e :: Nil) => Write(parseE(e))
    case SList(SSym("block") :: es) => Block(parseEs(es))
    case SList(SSym("@") :: SSym(f) :: es) => Apply(f,parseEs(es))
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
    case SList(SSym("let") :: SSym(x) :: e :: b :: Nil) => Let(x, parseE(e), parseE(b))
    case SList(SSym("setFst") :: p :: e :: Nil) => SetFst(parseE(p), parseE(e))
    case SList(SSym("setSnd") :: p :: e :: Nil) => SetSnd(parseE(p), parseE(e))
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
  def print(g: GlobalDef) : String = unparse(g).toString()
  def print(f: FunDef) : String = unparse(f).toString()
  def print(e: Expr) : String = unparse(e).toString()

  // These methods are distinguished by the type of their parameter.
  def unparse(p: Program) : SExpr =
    SList(SList(unparseGs(p.gdefs)) :: SList(unparseFs(p.fdefs)) :: unparse(p.body) :: Nil)

  def unparse(g: GlobalDef) : SExpr = 
    SList(SSym(g.id) :: unparse(g.d) :: Nil) 

  def unparseGs(gs: List[GlobalDef]) : List[SExpr] = gs match {
    case Nil => Nil
    case d :: ds => unparse(d) :: unparseGs(ds)
  }

  def unparse(f: FunDef) : SExpr =
    SList(SSym(f.name) :: SList(unparseIs(f.params)) :: unparse(f.body) :: Nil)

  def unparseFs(fs: List[FunDef]) : List[SExpr] = fs match {
    case Nil => Nil
    case d :: ds => unparse(d) :: unparseFs(ds)
  }

  def unparse(expr: Expr) : SExpr = expr match {
    case Num(n) => SNum(n)
    case Var(x) => SSym(x)
    case Assgn(x,e) => SList(SSym(":=") :: SSym(x) :: unparse(e) :: Nil)
    case While(c,e) => SList(SSym("while") :: unparse(c) :: unparse(e) :: Nil)
    case If(c,t,e) => SList(SSym("if") :: unparse(c) :: unparse(t) :: unparse(e) :: Nil)
    case Write(e) => SList(SSym("write") :: unparse(e) :: Nil)
    case Block(es) => SList(SSym("block") :: unparseEs(es))
    case Apply(f,es) => SList(SSym("@") :: SSym(f) :: unparseEs(es))
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
    case Let(x, e, b) => SList(SSym("let") :: SSym(x) :: unparse(e) :: unparse(b) :: Nil)
    case SetFst(p,e) => SList(SSym("setFst") :: unparse(p) :: unparse(e) :: Nil)
    case SetSnd(p,e) => SList(SSym("setSnd") :: unparse(p) :: unparse(e) :: Nil)
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
  case class GlobalAddr(a:Int) extends Location {
  }
  case class StackFrameOffset(offset:Int) extends Location {
  }

  type Env = Map[String,Location]

  def interp(p:Program,debug:Int = 0): Int = {
    if (debug > 0)
      println("Program: " + p)

    val globals = new Store()
    val heap = new HeapStore()
    val stack = new StackStore()

    def getLocation(fp:Int,a:Location) = a match {
      case GlobalAddr(i) => globals.get(i)
      case StackFrameOffset(offset) => stack.get(fp+offset)
    }

    def setLocation(fp:Int,a:Location,v:Value) = a match {
      case GlobalAddr(i) => globals.set(i,v)
      case StackFrameOffset(offset) => stack.set(fp+offset,v)
    }

    var genv : Env = Map[String,Location]() // environment containing just the global defs

    def lookupFun(fname:String) : FunDef = {
      for (fdef <- p.fdefs)
        if (fdef.name == fname)
          return fdef
      throw InterpException("undefined function:" + fname)
    }

    def interpVar(env:Env,x:String) : Location =
      env.getOrElse(x, throw InterpException("undefined variable:" + x))

    def interpFun(fname:String,fp:Int,argCount:Int) : Value = {
      if (debug > 1) 
        println("entering " + fname + " at frame pointer = " + fp + " with " + argCount + " args")
      val fd = lookupFun(fname)   // find function definition
      if (fd.params.length != argCount)
        throw InterpException("wrong number of arguments in application of:" + fname)
      // define the environment for the body to contain the globals
      // plus the arguments that the caller pushed on the stack
      var benv = genv
      var offset = 0
      for (x <- fd.params) {
        benv = benv + (x -> StackFrameOffset(offset))
        offset += 1
      }
      // evaluate function body 
      val v = interpE(benv,fp,fd.body)
      // return result
      if (debug > 1)
        println("returning " + v)
      v
    }

    def interpE(env:Env,fp:Int,e:Expr) : Value = {
      if (debug > 1) {
        println("expr = "+ e)
        println("env = " + env)
        println("frame pointer = " + fp)
        println("globals = " + globals)
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
        case Assgn(x,e) => {
          val a = interpVar(env,x)
          val v = interpE(env,fp,e)
          setLocation(fp,a,v)
          v
        }
        case While(c,b) => interpE(env,fp,c) match {
          case NumV(0) => NumV(0)
          case NumV(_) => {
            interpE(env,fp,b)
            interpE(env,fp,e)
          }
          case _ => throw InterpException("non-numeric argument to While")
        }
        case Write(e) => {
          val v = interpE(env,fp,e)
          def show(v:Value) : String = v match {
            case NumV(i) => "" + i
            case PairV(a) => "(" + show(heap.get(a)) + "." + show(heap.get(a+1)) + ")"
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
        case Apply(f,es) => {
          // evaluate actual arguments and push them on the stack, creating a new frame
          val fpNew = stack.push(es.length)
          var offset = 0
          for (e <- es) {
            val v = interpE(env,fp,e)
            setLocation(fpNew,StackFrameOffset(offset),v)
            offset += 1
          }
          val v = interpFun(f,fpNew,es.length)
          // restore stack
          stack.pop(es.length)
          v
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
          NumV(if (lv == rv) 1 else 0)
        }

        case Let(x, e, b) => 
        {
                        
            val fpNew = stack.push(1) 
       
            var benv = env + (x -> StackFrameOffset(stack.currOffset(fp) + 1))
            
            setLocation(fp, StackFrameOffset(stack.currOffset(fp) + 1), interpE(env, fp, e))

            val v1 = interpE(benv, fp, b)

            stack.pop(1)

            v1

        }
        
        case SetFst(p, e) => interpE(env,fp,p) match 
        {
            case PairV(a) => 
            {
                heap.set(a, interpE(env, fp, e))
                PairV(a)
            }
            case _ => throw InterpException("non-pair argument to fst")
        }
        

        case SetSnd(p, e) => interpE(env,fp,p) match 
        {
            case PairV(a) => 
            {
                heap.set(a + 1, interpE(env, fp, e))
                PairV(a)
            }
            case _ => throw InterpException("non-pair argument to fst")
        }


      }
      if (debug > 1) 
        println("result = "+ r)
      r
    }


    // process the global definitions
    var index = 0
    for (gdef <- p.gdefs) {
      val v = interpE(genv,0,gdef.d)
      genv = genv + (gdef.id -> GlobalAddr(index))
      globals.set(index,v)
      if (debug > 0)
        println("Global definition:" + gdef.id + " evaluates to: " + v)
      index += 1
    }

    // process the main body expression
    val v = interpE(genv,0,p.body)
    if (debug > 0)
      println("Body evaluates to: " + v)
    v match {
      case NumV(n) => n
      case _ => throw InterpException("main body returns non-integer")
    }
  }
}

object Process 
{
  def process (s:String,debug:Int =0) : Int = {
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


