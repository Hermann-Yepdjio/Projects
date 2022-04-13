case class MachineException(string: String) extends RuntimeException

// Stack Machine
object Machine {

  sealed abstract class Instr
  case class Const(n:Int) extends Instr
  case object Plus extends Instr
  case object Times extends Instr
  case object Divrem extends Instr
  case object Lessequ extends Instr
  case object Pop extends Instr
  case object Dup extends Instr
  case object Swap extends Instr
  case class Load(x:String) extends Instr
  case class Store(x:String) extends Instr
  case object Print extends Instr
  case class Label(l:Int) extends Instr
  case class Branch(l:Int) extends Instr
  case class Branchz(l:Int) extends Instr

  type Program = List[Instr]


  // see http://docs.scala-lang.org/overviews/collections/maps.html for details of Map class
  type VarStore = collection.mutable.Map[String,Int]

  def exec(prog:Program,debug: Int = 0) : Int = {
    val store : VarStore = collection.mutable.Map[String,Int]()  
    var pc = 0
    var stk : List[Int] = Nil
    def push(i:Int) = stk = i :: stk
    def pop() : Int = stk match {
      case top :: rest =>
        { stk = rest
          top
        }
      case Nil =>
        throw MachineException("Pop from empty stack")
    }
    def dump() : String = stk.mkString(" ")

    def step () : Int = 
      prog(pc) match {
        case Const(i) => {
          push(i)
          pc+1
        }
        case Plus => {
          val v2 = pop()
          val v1 = pop()
          push(v1 + v2)
          pc+1
        }
        case Times => {
          val v2 = pop()
          val v1 = pop()
          push(v1 * v2)
          pc+1
        }
        case Divrem => {
          val v2 = pop()
          val v1 = pop()
          if (v2 == 0) 
            throw MachineException("division by zero")
          else {
            push(v1/v2)
            push(v1 % v2)
          }
          pc+1
        }
        case Lessequ => {
          val v2 = pop()
          val v1 = pop()
          push(if (v1 <=v2) 1 else 0)
          pc+1
        }
        case Pop => {
          pop()
          pc+1
        }
        case Dup => {
          val v = pop()
          push(v)
          push(v)
          pc+1
        }
        case Swap => {
          val v2 = pop()
          val v1 = pop()
          push(v2)
          push(v1)
          pc+1
        }
        case Load(x) => {
          store get x match {
            case Some(v) => push(v)
            case None => push(0)
          }
          pc+1
        }
        case Store(x) => {
          val v = pop()
          store(x) = v
          pc+1
        }
        case Print => {
          val v = pop()
          println(v)
          pc+1
        }
        case Label(l) =>
          pc+1
        case Branch(l) =>
          findLabel(prog,l)
        case Branchz(l) => {
          val v = pop()
          if (v == 0)
            findLabel(prog,l)
          else
            pc+1
        }
      }
    
    def findLabel(prog:Program,l:Int) = {
      def f(n:Int,prog:Program) : Int = prog match {
        case Nil => throw MachineException("missing label " + l)
        case Label(l1)::rest if l == l1 => n
        case _::rest => f(n+1,rest)
      }
      f(0,prog)
    }

    if (debug > 0) println("Machine code:" + prog)

    while (pc < prog.length) {
      if (debug > 1) print("" + pc + "*" + prog(pc))
      pc = step()
      if (debug > 1) println (":" + dump())
    }
    val r = pop()
    if (stk.length > 0) throw MachineException("stack not empty at program termination")
    if (debug > 0) println("Result:" + r)
    r
  }        
}

object SExprLibrary {
  import scala.util.parsing.combinator._
  import collection.immutable.StringOps

  sealed abstract class SExpr {
    override def toString() : String = SExprPrinter.print(this)
  }
  case class SList(list: List[SExpr]) extends SExpr
  case class SSym(symbol: String) extends SExpr
  case class SNum(num: Int) extends SExpr
  case class SString(str: String) extends SExpr

  case class ReadException(string: String) extends RuntimeException

  object SExprReader extends JavaTokenParsers {

    def read(text: String): SExpr = {
      val result = parseAll(sexpr, uncomment(text)) 
      result match {
        case Success(r, _) => r
        case Failure(msg, n) =>
          throw ReadException("Bad SExpr:" + msg + " (input left: \"" + n.source.toString.drop(n.offset) + "\")")
        case Error(msg, n) =>
          throw ReadException("Bad SExprr:" + msg + " (input left: \"" + n.source.toString.drop(n.offset) + "\")")
      }
    }

    def sexpr: Parser[SExpr] = (num | symbol | slist | string)

    def symbol: Parser[SExpr] = not(wholeNumber | stringLiteral) ~> "[^\"()\\s]+".r ^^ SSym
    def slist: Parser[SExpr] = "(" ~> sexpr.* <~ ")" ^^ SList
    def num: Parser[SExpr] = wholeNumber ^^ { s => SNum(s.toInt) }
    def string : Parser[SExpr] = stringLiteral ^^ {s => SString(unquote(s)) }

    def uncomment(str: String): String = {
      def f (ds:(Int,String),c:Char) = {
        val (d,s) = ds
	c match {
        case '{' => (d+1,s)
	case '}' if d > 0 => (d-1,s)
	case _ if d == 0 => (d,s :+ c)
	case _ => (d,s)
	}
      }
      val (_,s) = ((0,"") /: str) (f)
      s
    }

    def unquote(str: String): String = {
      if (str != null && str.length >= 2 && str.charAt(0) == '\"' && str.charAt(str.length - 1) == '\"')
        str.substring(1, str.length - 1)
      else
        sys.error("unquote inconsistency:" + str)
    }

  }

  object SExprPrinter {
    def print(sexpr: SExpr): String = sexpr match {
      case SSym(str) => str
      case SNum(n)   => n.toString
      case SList(l)  => "(" ++ l.map(print).mkString(" ") ++ ")"
      case SString(str) => "\"" + str + "\""
    }
  }

}

