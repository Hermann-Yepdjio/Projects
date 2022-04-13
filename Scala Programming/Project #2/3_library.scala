case class MachineException(string: String) extends RuntimeException

// Stack Machine
object Machine {
  sealed abstract class Instr
  case class Const(b:Boolean) extends Instr
  case object Nand extends Instr
  case object Pop extends Instr
  case object Swap extends Instr
  case object Swapx extends Instr
  case object Dup extends Instr

  type Program = List[Instr]

  var stk : List[Boolean] = Nil
  def push(b:Boolean) = stk = b :: stk 
  def pop() : Boolean = stk match {
    case top :: rest =>
      { stk = rest
        top
      }
    case Nil => 
      throw MachineException("Pop from empty stack")
  }
  def dump() : String = stk.mkString(" ")

  def step (instr:Instr) = instr match {
    case Const(b) => push(b)
    case Nand => {
      val v2 = pop()
      val v1 = pop()
      push(!(v1 && v2))
    }
    case Pop => pop()
    case Swap => {
      val v2 = pop()
      val v1 = pop()
      push(v2)
      push(v1)
    }
    case Swapx => {
      val v1 = pop()
      val v2 = pop()
      val v3 = pop()
      push(v1)
      push(v2)
      push(v3)
    }
    case Dup => {
      val v = pop()
      push(v)
      push(v)
    }
  }


  def exec(p:Program,debug:Int = 0) : Boolean = {
    stk = Nil
    def steps (instrs:List[Instr]) : Unit = instrs match {
      case Nil => () 
      case instr::instrs => {
        step(instr)
        if (debug > 1) println("*" + instr + ":" + dump())
          steps(instrs)
      }
    }
    if (debug > 0) println("Machine code:" + p)
    steps(p)
    val r = pop()
    if (debug > 0) println("Result:" + r)
    if (stk.length > 0) throw MachineException("stack not empty at program termination")
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
          throw ReadException("Bad SExpr:" + msg + " (input left: \"" + n.source.toString.drop(n.offset) + "\")")
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

