import SExprLibrary._

case class Program(fdefs: List[FunDef], body: Expr) {
  override def toString() : String = Printer.print(this)
}

sealed abstract class Type {
  override def toString() : String = Printer.print(this)
}

case object NumTy extends Type
case object BoolTy extends Type
case class ListTy(elemTy:Type) extends Type

case class FunDef(fname:String,params:List[(String,Type)],resultTy:Type,body:Expr)  {
  override def toString() : String = Printer.print(this)
}

sealed abstract class Expr {
  override def toString() : String = Printer.print(this)
}
case class Num(i:Int) extends Expr
case class Bool(b:Boolean) extends Expr
case class Var(id:String) extends Expr
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
case class Apply(fname:String,args:List[Expr]) extends Expr
case class Let(id:String,defn:Expr,body:Expr) extends Expr
case class Empty(ty:Type) extends Expr   // scala library already defines 'Nil' 
case class Cons(h:Expr,t:Expr) extends Expr
case class IsNil(l:Expr) extends Expr
case class Head(l:Expr) extends Expr
case class Tail(l:Expr) extends Expr

case class ParseException(string: String) extends RuntimeException

object Parser {
  def parse(str:String,debug:Int = 0): Program = {
    try {
      val a = parseP(SExprReader.read(str))
      if (debug > 0) println("Parsed Program: " + a) 
      a
    } catch {
      case ex:ReadException => throw ParseException(ex.string)
    }
  }
   
  def parseT(sexpr: SExpr) : Type = sexpr match {
    case SSym("num") => NumTy
    case SSym("bool") => BoolTy
    case SList(SSym("list") :: t :: Nil) => ListTy(parseT(t))
    case _ => throw ParseException("Cannot parse type:" + sexpr)
  }

  def parseP(sexpr: SExpr) : Program = sexpr match {
    case SList(SList (fs) :: b :: Nil) => Program(fs map(parseF),parseE(b))
    case _ => throw ParseException("Cannot parse program:" + sexpr)
  }
      
  def parseF(sexpr: SExpr) : FunDef = sexpr match {
    case SList(SSym(f) :: SList(ps) :: rt :: b :: Nil) => FunDef(f,ps map(parseParam),parseT(rt),parseE(b))
    case _ => throw ParseException("Cannot parse function declaration:" + sexpr)
  }
 
  def parseParam(sexpr: SExpr) : (String,Type) = sexpr match {
    case SList(SSym(p) :: t :: Nil) => (p, parseT(t))
    case _ => throw ParseException("Cannot parse parameter declaration:" + sexpr)
  }

  def parseE(sexpr: SExpr) : Expr = sexpr match {
    case SNum(n) => Num(n)
    case SSym("true") => Bool(true)
    case SSym("false") => Bool(false)
    case SSym(id) => Var(id)
    case SList(SSym(":=") :: SSym(id) :: e :: Nil) => Assgn(id,parseE(e))
    case SList(SSym("while") :: c :: e :: Nil) => While(parseE(c),parseE(e))
    case SList(SSym("if") :: c :: t :: e :: Nil) => If(parseE(c),parseE(t),parseE(e))
    case SList(SSym("write") :: e :: Nil) => Write(parseE(e))
    case SList(SSym("block") :: es) => Block(es map(parseE))
    case SList(SSym("+") :: l :: r :: Nil) => Add(parseE(l),parseE(r))
    case SList(SSym("-") :: l :: r :: Nil) => Sub(parseE(l),parseE(r))
    case SList(SSym("*") :: l :: r :: Nil) => Mul(parseE(l),parseE(r))
    case SList(SSym("/") :: l :: r :: Nil) => Div(parseE(l),parseE(r))
    case SList(SSym("%") :: l :: r :: Nil) => Rem(parseE(l),parseE(r))
    case SList(SSym("<=") :: l :: r :: Nil) => Le(parseE(l),parseE(r))
    case SList(SSym("@") :: SSym(f) :: as) => Apply(f,as map(parseE))
    case SList(SSym("nil") :: t :: Nil) => Empty(parseT(t))
    case SList(SSym("cons") :: h :: t :: Nil) => Cons(parseE(h),parseE(t))
    case SList(SSym("isnil") :: l :: Nil) => IsNil(parseE(l))
    case SList(SSym("head") :: l :: Nil) => Head(parseE(l))
    case SList(SSym("tail") :: l :: Nil) => Tail(parseE(l))
    case SList(SSym("let") :: SSym(id) :: d :: b :: Nil) => Let(id,parseE(d),parseE(b))
    case _ => throw ParseException("Cannot parse expression:" + sexpr)
  }
}

object Printer {
  def print(prog: Program) : String = unparse(prog).toString()
  
  def unparse(prog:Program) : SExpr =
    SList(SList(prog.fdefs map (unparse)) :: unparse(prog.body) :: Nil)
  
  def unparse(ty:Type) : SExpr = ty match {
    case NumTy => SSym("num")
    case BoolTy => SSym("bool")
    case ListTy(elemTy) => SList(SSym("list") :: unparse(elemTy) :: Nil)
  }

  def print(ty:Type) : String = unparse(ty).toString()

  def print(fdef:FunDef) : String = unparse(fdef).toString()
  
  def unparse(fdef:FunDef) : SExpr = 
    SList(SSym(fdef.fname) :: SList(fdef.params map(unparse)) :: unparse(fdef.resultTy) :: unparse(fdef.body) :: Nil)
  
  def unparse(param:(String,Type)) : SExpr = {
    val (x,t) = param 
    SList(SSym(x) :: unparse(t) :: Nil)
  }

  def print(expr: Expr) : String = unparse(expr).toString()

  def unparse(expr: Expr) : SExpr = expr match {
    case Num(n) => SNum(n)
    case Bool(true) => SSym("true")
    case Bool(false) => SSym("false")
    case Var(x) => SSym(x)
    case Assgn(x,e) => SList(SSym(":=") :: SSym(x) :: unparse(e) :: Nil)
    case While(c,e) => SList(SSym("while") :: unparse(c) :: unparse(e) :: Nil)
    case If(c,t,e) => SList(SSym("if") :: unparse(c) :: unparse(t) :: unparse(e) :: Nil)
    case Write(e) => SList(SSym("write") :: unparse(e) :: Nil)
    case Block(es) => SList(SSym("block") :: (es map (unparse)))
    case Apply(f,es) => SList(SSym("@") :: SSym(f) :: (es map (unparse)))
    case Add(l,r) => SList(SSym("+") :: unparse(l) :: unparse(r) :: Nil)
    case Sub(l,r) => SList(SSym("-") :: unparse(l) :: unparse(r) :: Nil)
    case Mul(l,r) => SList(SSym("*") :: unparse(l) :: unparse(r) :: Nil)
    case Div(l,r) => SList(SSym("/") :: unparse(l) :: unparse(r) :: Nil)
    case Rem(l,r) => SList(SSym("%") :: unparse(l) :: unparse(r) :: Nil)
    case Le(l,r) => SList(SSym("<=") :: unparse(l) :: unparse(r) :: Nil)
    case Let(x,d,b) => SList(SSym("let") :: SSym(x) :: unparse(d) :: unparse(b) :: Nil)
    case Empty(t) => SList(SSym("nil") :: unparse(t) :: Nil)
    case Cons(h,t) => SList(SSym("cons") :: unparse(h) :: unparse(t) :: Nil)
    case IsNil(l) => SList(SSym("isnil") :: unparse(l) :: Nil)
    case Head(l) => SList(SSym("head") :: unparse(l) :: Nil)
    case Tail(l) => SList(SSym("tail") :: unparse(l) :: Nil)
  }
}    

case class TypingException(string: String) extends RuntimeException
case object CheckNotImplemented extends RuntimeException

object Check {
  type Env = Map[String,Type]

  val emptyEnv:Env = Map[String,Type]()

  def check(p:Program,debug:Int = 0) = {

    // check functions
    // first check for duplicate function names
    val fnames = collection.mutable.Set[String]()
    for (FunDef(fname,_,_,_) <- p.fdefs)
      if (fnames.contains(fname))
        throw TypingException("Duplicate function definition name:" + fname)
      else 
        fnames += fname
    // now check each function
    for (FunDef(fname,params,resultTy,body) <- p.fdefs) {
      if (params.length != params.map(_._1).distinct.length)
         throw TypingException("Duplicate parameter names in declaration of function:" + fname)
      val ty = checkE(emptyEnv ++ params,body)
      if (ty != resultTy)
        throw TypingException("Function " + fname + " has declared result type: " + resultTy + " but body has type:" + ty) 
    }

    def lookupFun(fname:String) : (List[Type],Type) = {
      for (FunDef(fname0,params,resultTy,_) <- p.fdefs)
        if (fname0 == fname)
          return (params map (_._2),resultTy)
      throw TypingException("undefined function:" + fname)
    }

    def checkVar(env:Env,x:String) : Type =
      env.getOrElse(x, throw TypingException("undefined variable:" + x))
  
    def checkE(env:Env,expr:Expr) : Type = expr match {
      case Num(_) => NumTy
      case Bool(_) => BoolTy
      case Var(x) => checkVar(env,x)
      case Assgn(x,e) => {
        val t = checkVar(env,x)
        val te = checkE(env,e)
        if (t == te)
          t
        else
          throw TypingException("Assignment RHS type does not match variable type")
      }
      case While(c,e2) => checkE(env,c) match {
        case BoolTy => {
          checkE(env,e2)
          NumTy
        }
        case _ => throw TypingException("While test expression is non-boolean")
      }
      case If(c,t,e) => checkE(env,c) match {
        case BoolTy => {
            val tt = checkE(env,t)
            val et = checkE(env,e)
            if (tt == et)
              tt
            else
              throw TypingException("Arms of If have different types: " + tt + " and " + et)
        }
        case ct => throw TypingException("If applied to non-boolean")
      }
      case Write(e) => checkE(env,e)
      case Block(es) => {
        var t : Type = NumTy
        for (e <- es)
          t = checkE(env,e)
        t
      }
      case Add(l,r) => arithBinOp(l,r,env,NumTy)
      case Sub(l,r) => arithBinOp(l,r,env,NumTy)
      case Mul(l,r) => arithBinOp(l,r,env,NumTy)
      case Div(l,r) => arithBinOp(l,r,env,NumTy)
      case Rem(l,r) => arithBinOp(l,r,env,NumTy)
      case Le(l,r) => arithBinOp(l,r,env,BoolTy)
      case Apply(f,as) => {
        val (paramTys,resultTy) = lookupFun(f)
        val ats = as map(checkE(env,_))
        if (ats == paramTys)
          resultTy
        else
          throw TypingException("Formal parameters to " + f + " have declared types: " + paramTys + " but actual parameters have types: " + ats)
      }
      case Empty(t) => ListTy(t)
      case Cons(h,t) => {
        val tt = checkE(env,t)
        tt match {
          case ListTy(elemTy) => {
              val ht = checkE(env,h)
              if (ht == elemTy) 
                tt 
              else
                throw TypingException("First argument to Cons has type: " + ht + " but second argument has type: "  + tt)
          }
          case _ => throw TypingException("Second argument to Cons should be a list, but has type: " + tt)
        }
      }
      case Let(x,d,b) => checkE(env + (x -> checkE(env,d)),b)
      case IsNil(l) => checkE(env,l) match {
        case ListTy(_) => BoolTy
        case lt =>  throw TypingException("Argument to IsNil should be a list, but has type: " + lt)
      }
      case Head(l) => checkE(env,l) match {
        case ListTy(ty) => ty
        case lt =>  throw TypingException("Argument to Head should be a list, but has type: " + lt)
      }
      case Tail(l) => checkE(env,l) match {
        case ListTy(ty) => ListTy(ty)
        case lt =>  throw TypingException("Argument to Tail should be a list, but has type: " + lt)
      }
    }
    
    def arithBinOp(l: Expr,r:Expr,env:Env,resTy: Type) = (checkE(env,l),checkE(env,r)) match {
      case (NumTy,NumTy) => resTy
      case _ => throw TypingException("arithmetic binary operator applied to non-integer")
    }   

    // check body (must be well-typed, but specific type doesn't matter)
    checkE(emptyEnv,p.body)
    if (debug > 0) println("Checked")
  }
}

case class InterpException(string: String) extends RuntimeException

object Interp {

  sealed abstract class Value
  case class NumV(num:Int) extends Value
  case class BoolV(b:Boolean) extends Value
  sealed abstract class ListValue extends Value
  case object NilV extends ListValue
  case class ConsV(heapIndex:Int) extends ListValue

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

  type Env = Map[String,Location]

  var emptyEnv:Env = Map[String,Location]()

  def interp(p:Program,debug:Int = 0): Int = {
    if (debug > 0) println("Program: " + p)

    val heap = new HeapStore()
    val stack = new StackStore()

    def getLocation(fp:Int,a:Location) = a match {
      case StackFrameOffset(offset) => stack.get(fp+offset)
    }

    def setLocation(fp:Int,a:Location,v:Value) = a match {
      case StackFrameOffset(offset) => stack.set(fp+offset,v)
    }

    def lookupFun(fname:String) : (List[String],Expr) = 
      p.fdefs filter (_.fname == fname) match {
        case fd::Nil =>
          if (fd.params.length != fd.params.map(_._1).distinct.length)
            throw InterpException("Duplicate parameter names in declaration of function:" + fname)
          return (fd.params map (_._1),fd.body)
        case Nil =>  throw InterpException("undefined function:" + fname)
        case _ => throw InterpException("ambiguous function name:" + fname)
      }

    def interpVar(env:Env,x:String) : Location =
      env.getOrElse(x, throw InterpException("undefined variable:" + x))

    def interpFun(fname:String,fp:Int,argCount:Int) : Value = {
      if (debug > 1) 
        println("entering " + fname + " at frame pointer = " + fp + " with " + argCount + " args")
      val (params,body) = lookupFun(fname)   // find function definition
      if (params.length != argCount)
        throw InterpException("wrong number of arguments in application of:" + fname)
      // define the environment for the body to contain the arguments
      // that the caller pushed on the stack
      var benv = emptyEnv
      var offset = 0
      for (x <- params) {
        benv = benv + (x -> StackFrameOffset(offset))
        offset += 1
      }
      // evaluate function body 
      val v = interpE(benv,fp,body)
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
        println("stack = " + stack)
        println("heap = " + heap)
      } 
      val r = e match {
        case Num(n) => NumV(n)
        case Bool(b) => BoolV(b)
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
          case (NumV(lv),NumV(rv)) => BoolV(lv <= rv) 
          case _ => throw InterpException("non-numeric argument to arithmetic operator")
        }
        case If(c,t,e) => interpE(env,fp,c) match {
          case BoolV(false) => interpE(env,fp,e)
          case BoolV(true) => interpE(env,fp,t)
          case _ => throw InterpException("non-boolean argument to If")
        }
        case Assgn(x,e) => {
          val a = interpVar(env,x)
          val v = interpE(env,fp,e)
          setLocation(fp,a,v)
          v
        }
        case While(c,b) => interpE(env,fp,c) match {
          case BoolV(false) => NumV(0)
          case BoolV(true) => {
            interpE(env,fp,b)
            interpE(env,fp,e)
          }
          case _ => throw InterpException("non-boolean argument to While")
        }
        case Write(e) => {
          val v = interpE(env,fp,e)
          def show(v:Value) : String = v match {
            case NumV(i) => "" + i
            case BoolV(b) => "" + b
            case NilV => "Nil"
            case ConsV(a) => "(" ++ show(heap.get(a)) + "::" + show(heap.get(a+1)) ++ ")"
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
          // evaluate actual arguments and push them on stack, creating a new frame
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
        case Let(x,e,b) => {
          val v = interpE(env,fp,e)
          val loc = StackFrameOffset(stack.currOffset(fp))
          stack.push(1)
          setLocation(fp,loc,v)
          val r = interpE(env + (x->loc),fp,b)
          stack.pop(1)
          r
        }
        case Empty(_) => NilV
        case Cons(h,t) => {
          val hv = interpE(env,fp,h)
          val tv = interpE(env,fp,t)
          val a = heap.allocate(2)
          heap.set(a,hv)
          heap.set(a+1,tv) 
          ConsV(a)
        }
        case Head(e) => interpE(env,fp,e) match {
          case NilV => throw InterpException("Head of empty list")
          case ConsV(a) => heap.get(a)
          case _ => throw InterpException("Head applied to non-list") 
        }
        case Tail(e) => interpE(env,fp,e) match {
          case NilV => throw InterpException("Tail of empty list")
          case ConsV(a) => heap.get(a+1)
          case _ => throw InterpException("Tail applied to non-list")
        }
        case IsNil(e) => interpE(env,fp,e) match {
          case NilV => BoolV(true)
          case ConsV(_) => BoolV(false)
          case _ => throw InterpException("IsNil applied to non-list")
        }
      }
      if (debug > 1) 
        println("result = "+ r)
      r
    }

    // process the main body expression
    val v = interpE(emptyEnv,0,p.body)
    if (debug > 0)
      println("Body evaluates to: " + v)
    v match {
      case NumV(n) => n
      case BoolV(true) => 1  // encoded return value for bools
      case BoolV(false) => 0
      case _ => 0  // arbitrarily return 0 for other types 
    }
  }
}

object Process {
  def process (s:String,debug:Int = 0) : Int = {
    try {
      val p : Program = Parser.parse(s,debug)
      Check.check(p,debug)
      Interp.interp(p,debug)
    } catch {
      case ex: InterpException => { println("Interp Error:" + ex.string) ; throw ex }
      case ex: TypingException => { println("Typing error: " + ex.string); throw ex }
      case ex: ParseException => { println("Parser Error:" + ex.string) ; throw ex }
    }
  }
}

// The following code may be useful for stand-alone development and
// testing from the command line. (It is not useful when developing
// or testing within WebLab.)
object Typing {
  import scala.io.Source
  def main (argv: Array[String]) = {
    val s = Source.fromFile(argv(0)).getLines.mkString("\n")
    val d = if (argv.length > 1) argv(1).toInt else 0
    val r = Process.process(s,d)
    println("result = " + r)
    ()
  }
}

