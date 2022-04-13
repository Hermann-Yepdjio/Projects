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
case class Apply(f:String,es:List[String]) extends Expr
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
case class SetFst(p:Expr,e:Expr) extends Expr 
case class SetSnd(p:Expr,e:Expr) extends Expr 
case class Let(id:String,e:Expr,b:Expr) extends Expr

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
    case SList(SSym("@") :: SSym(f) :: xs) => Apply(f,parseIs(xs))
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
    case SList(SSym("setFst") :: p :: e :: Nil) => SetFst(parseE(p),parseE(e))
    case SList(SSym("setSnd") :: p :: e :: Nil) => SetSnd(parseE(p),parseE(e))
    case SList(SSym("let") :: SSym(id) :: e :: b :: Nil) => Let(id,parseE(e),parseE(b))
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
    case Apply(f,xs) => SList(SSym("@") :: SSym(f) :: unparseIs(xs))
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
    case SetFst(p,e) => SList(SSym("setFst") :: unparse(p) :: unparse(e) :: Nil) 
    case SetSnd(p,e) => SList(SSym("setSnd") :: unparse(p) :: unparse(e) :: Nil) 
    case Let(x,e,b) => SList(SSym("let") :: SSym(x) :: unparse(e) :: unparse(b) :: Nil) 
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
  case class LocationV(loc:Location) extends Value

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
      // plus the arguments whose addresses the caller pushed on the stack
      var benv = genv
      var offset = 0
      for (x <- fd.params) {
        val loc = getLocation(fp,StackFrameOffset(offset)) match {
          case LocationV(loc) => loc 
          case _ => throw new Error("impossible: actual argument is not location")
        }
        benv = benv + (x -> loc)
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
            case _ => "?"
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
        case Apply(f,xs) => {
          // push locations of arguments onto stack, creating a new frame
          val fpNew = stack.push(xs.length)
          var offset = 0
          for (x <- xs) {
            val loc = interpVar(env,x)
            val adjustedLoc = loc match {
              case StackFrameOffset(off) => StackFrameOffset(off + fp - fpNew) // adjust to new fp
              case _ => loc // globals don't move
            }
            setLocation(fpNew,StackFrameOffset(offset),LocationV(adjustedLoc))
            offset += 1
          }
          val v = interpFun(f,fpNew,xs.length)
          // restore stack
          stack.pop(xs.length)
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
        case SetFst(p,e) => interpE(env,fp,p) match {
          case PairV(a) => {
            heap.set(a,interpE(env,fp,e))
            PairV(a)
          }
          case _ => throw InterpException("non-pair argument to setFst")
        }
        case SetSnd(p,e) => interpE(env,fp,p) match {
          case PairV(a) => {
            heap.set(a+1,interpE(env,fp,e))
            PairV(a)
          }
          case _ => throw InterpException("non-pair argument to setSnd")
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

object Process {
  def process (s:String,debug:Int = 0,allowSetOps:Boolean = true) : Int = {
    try {
      val p : Program = Parser.parse(s,debug)
      Interp.interp(p,debug)
    } catch {
      case ex: InterpException => { println("Interp Error:" + ex.string) ; throw ex }
      case ex: ParseException => { println("Parser Error:" + ex.string) ; throw ex }
    }
  }
}


