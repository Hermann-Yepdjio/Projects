//test: Test

import org.scalatest.FunSuite

import Interp._
import Check._
import Parser._
import Process._

class Test extends FunSuite {
  
// Extra assignment parts A and B:


def typecheck(s:String) = check(parse(s))
def run(s:String) = interp(parse(s))
def expr(s:String) = "(() %s)".format(s)
def chkE(s:String) = checkAndRun(expr(s))

def checkAndRun(s:String) = 
  Process.process(s)

def checkAndResultAndOutput(s:String) : (Int, String) = {
  val outputChannel = new java.io.ByteArrayOutputStream(10240)
  def testOutput = {
    val result = Process.process(s)
    outputChannel.flush()
    val output = outputChannel.toString()
    (result, output)
  }
  Console.withOut(outputChannel)(testOutput)
}

// Some test programs
  
val square = "(sq ((i num)) num (* i i))"
val const3 = "(const3 () num 3)"

// Basic sanity checks (not involving student solution code)
// (these are "free points", since these tests should pass unless the student did something horribly wrong)

test ("1. 'Parser and printer work for type annotations (and other sanity checks)'") {
  parse("((%s) (@ const3))".format(const3))
  parse("((%s) (@ sq 3))".format(square))
}

test ("2. 'Omitting type annotations entirely from expected locations results in a parse error'") {
  // Function param without type annotation fails
  intercept[ParseException] { parse("(((f (i) num 3)) 3)") }
  intercept[ParseException] { parse("(((f ((i)) num 3)) 3)") }
  // Function without result type fails
  intercept[ParseException] { parse("(((f () 3)) 3)") }
  // Function without any type annotations fail
  intercept[ParseException] { parse("(((f (i) 3)) 3)") }
  intercept[ParseException] { parse("(((f ((i)) 3)) 3)") }
}


// Typing: ensure that the correct type is returned,
//   implicitly checking that the definition of Type was not changed
//   and that none of the already-implemented checks were changed.


test ("3. 'Typing literal Num values'") {
  chkE("3")
  chkE("-20")
}

test ("4. 'Typing literal Bool values'") {
  chkE("true")
  chkE("false")
}

test("5. 'Typing simple arithmetic expressions returning Num'") {
  chkE("(* 3 2)")
  chkE("(+ 2 1)")
  chkE("(* 2 (+ 1 (/ 2 1)))")
}

test ("6. 'Typing simple arithmetic expressions returning Bool'") {
  chkE("(<= 3 3)")
  chkE("(<= 3 2)")
}

test("7. 'Typing simple expressions with `if`'") {
  chkE("(if false true false)")
  chkE("(if (<= 2 3) 2 3)")
}

test("8. 'Typing simple expressions with while'") {
  chkE("(while false true)")
  typecheck(expr("(while true 1)"))
}


test("9. 'While always returns `num`'") {
  chkE("(+ 0 (while false true))")
}

test("10. 'Typing simple expressions with block'") {
  chkE("(block true 1)")
  chkE("(block 1 true)")
  chkE("(block)")
  chkE("(block 1 2 true 3)")
}

test("11. 'Typing assignments'") {
  chkE("(let x 1 (:= x 2))")
  chkE("(let y 2 (let x 1 (:= x y)))")
}

test("13. Assignment type errors") {
  intercept[TypingException] { chkE("(let x 1 (:= x true))") }
  intercept[TypingException] { chkE("(let y true (let x 1 (:= x y)))") }
}

test("14. 'Typing lists of Nums and Bools'") {
  chkE("(nil num)")
  chkE("(cons 3 (nil num))")
  chkE("(cons 3 (cons 2 (nil num)))")
  chkE("(nil bool)")
  chkE("(cons true (nil bool))")
  chkE("(cons true (cons false (nil bool)))")
}

test("15. 'Typing lists of lists, and lists of lists of lists, of Nums") {
  chkE("(nil (list num))")
  chkE("(nil (list (list num)))")
  chkE("(cons (nil num) (nil (list num)))")
  chkE("(cons (nil (list num)) (nil (list (list num))))")
  chkE("(cons (cons 3 (nil num)) (nil (list num)))")
  chkE("(cons (cons (cons 3 (nil num))(nil (list num))) (nil (list (list num))))")
}

test("16. 'Typing value bound behind a `let` (for Num, List<Num>)'") {
  chkE("(let i 0 i)")
  chkE("(let i (* 2 3) (* i i))")
  chkE("(let l (nil num) l)")
  chkE("(let l (cons 3 (nil num)) l)")
}

test("17. 'Typing list operations'"){
  typecheck(expr("(head (nil num))"))
  typecheck(expr("(head (cons 3 (nil num)))"))
  typecheck(expr("(<= (head (nil num)) 3)"))
  typecheck(expr("(<= (head (cons 3 (nil num))) 3)"))
  typecheck(expr("(if false (head (nil num)) 3)"))
  typecheck(expr("(if false (head (cons 3 (nil num))) 3)"))
  typecheck(expr("(tail (nil num))"))
  typecheck(expr("(tail (cons 3 (nil num)))"))
  typecheck(expr("(isnil (nil num))"))
  typecheck(expr("(isnil (cons 3 (nil num)))"))
}

test("18. 'Typing function application results: identity functions'") {
  checkAndRun("(((idBool ((b bool)) bool b)) (@ idBool true))")
  checkAndRun("(((idNum ((b num)) num b)) (@ idNum 3))")
  checkAndRun("(((idNumList ((b (list num))) (list num) b)) (@ idNumList (cons 3 (nil num))))")
}

test("19. 'Typing function application results: constant functions'") {
  checkAndRun("(((constBoolNum ((b bool)) num 3)) (@ constBoolNum true))")
  checkAndRun("(((constNumBool ((b num)) bool true)) (@ constNumBool 3))")
  checkAndRun("(((constNumNum ((b num)) num 2)) (@ constNumNum 3))")
}

test("20. 'Recursive functions are permitted'") {
  typecheck("(((f () num (@ f))) (@ f))")
  checkAndRun("(((f ((i num)) num (if (<= 10 i) i (@ f (+ 1 i))))) (@ f 0))")
}

// Type errors:

test("21. 'Type error: undefined function name'") {
  intercept[TypingException] { typecheck("(() (@ f))") }
}

test("22. 'Type error: undefined variable name'") {
  intercept[TypingException] { typecheck("(() f)") }
  intercept[TypingException] { typecheck("(() (let g g g))") }
}

test("23. 'Type error: multiple functions declared with same name'") {
  intercept[TypingException] { typecheck("(((f () num 3) (f () num 2)) (@ f))") }
}

test("24. 'Type error: multiple formal parameters of function given same name'") {
  intercept[TypingException] { typecheck("(((f ((x num) (x num)) num 0)) (@ f 1 2))") }
}

test("25. 'Type error: type mismatch in `if` arms'") {
  intercept[TypingException] { typecheck(expr("(if true false 3)")) }
  intercept[TypingException] { typecheck(expr("(if true 3 false)")) }
}


test("26. 'Type error: Typing failures hidden in while body'") {
  intercept[TypingException] { typecheck(expr("(while false (+ 1 false))")) }
}

test("27. 'Type error: Typing failures hidden in block body'") {
  intercept[TypingException] { typecheck(expr("(block (while false (+ 1 false)) 0)")) }
 intercept[TypingException] { typecheck(expr("(block (+ 1 false) 0)")) }
}

test("28. 'Type error: Typing failures hidden in if arm'") {
 intercept[TypingException] { typecheck(expr("(if false (+ 1 true) 0)")) }
 intercept[TypingException] { typecheck(expr("(if true 0 (+ 1 true))")) }
}

test("29. 'Type error: non-Bool conditional in `if` or `while`'") {
  intercept[TypingException] { typecheck(expr("(if 3 false false)")) }
  intercept[TypingException] { typecheck(expr("(while 3 false)")) }
}

test("30. 'Type error: non-Num arguments to `<=`, `add`, `mul`, or `div`'") {
  intercept[TypingException] { typecheck(expr("(<= 1 false)")) }
  intercept[TypingException] { typecheck(expr("(<= false false)")) }
  intercept[TypingException] { typecheck(expr("(<= false 1)")) }
  intercept[TypingException] { typecheck(expr("(* 1 false)")) }
  intercept[TypingException] { typecheck(expr("(* false false)")) }
  intercept[TypingException] { typecheck(expr("(* false 1)")) }
  intercept[TypingException] { typecheck(expr("(+ 1 false)")) }
  intercept[TypingException] { typecheck(expr("(+ false false)")) }
  intercept[TypingException] { typecheck(expr("(+ false 1)")) }
  intercept[TypingException] { typecheck(expr("(/ 1 false)")) }
  intercept[TypingException] { typecheck(expr("(/ false false)")) }
  intercept[TypingException] { typecheck(expr("(/ false 1)")) }
  intercept[TypingException] { typecheck(expr("(<= (nil num) (nil num))")) }
}

test("31. 'Type error: incorrect return type'") {
  intercept[TypingException] { typecheck("(((f () bool 2)) 3)") }
  intercept[TypingException] { typecheck("(((f ((b bool)) num b)) 3)") }
  intercept[TypingException] { typecheck("(((f () bool (@ g)) (g () num 3)) 3)") } 
}

test("32. 'Type error: incorrect param type at application'") {
  intercept[TypingException] { typecheck("(((f ((b bool)) bool b)) (@ f 1))") }
  intercept[TypingException] { typecheck("(((f ((b bool) (i num)) bool b)) (@ f true true))") }
}

test("33. 'Type error: incorrect param type in use in function'") {
  intercept[TypingException] { typecheck("(((f ((b bool)) num (+ 1 b))) 3)") }
}

test("34. 'Type error: type mismatch between head and elements of tail of list (or list of lists)'") {
  intercept[TypingException] { typecheck("(() (cons 1 (nil bool)))") }
  intercept[TypingException] { typecheck("(() (cons 1 (nil (list num))))") }
  intercept[TypingException] { typecheck("(() (cons 1 (cons true (nil bool))))") }
  intercept[TypingException] { typecheck("(() (cons (cons true (nil bool)) (nil bool)))") }
  
  intercept[TypingException] { typecheck("(() (cons true (nil num)))") }
  intercept[TypingException] { typecheck("(() (cons true (nil (list bool))))") }
  intercept[TypingException] { typecheck("(() (cons true (cons 1 (nil num))))") }
}

test("35. 'Type error: non-List arguments to `tail`, `head`, or `isNil`, `cons x`'") {
  intercept[TypingException] { typecheck("(() (cons 1 1))") }
  intercept[TypingException] { typecheck("(() (head 1))") }
  intercept[TypingException] { typecheck("(() (tail 1))") }
  intercept[TypingException] { typecheck("(() (isnil 1))") }
  intercept[TypingException] { typecheck("(() (cons true true))") }
  intercept[TypingException] { typecheck("(() (head true))") }
  intercept[TypingException] { typecheck("(() (tail true))") }
  intercept[TypingException] { typecheck("(() (isnil true))") }
}

// Typechecking and running list programs

test("36. 'Typecheck and run list program 1: `length` and `sum`'") {
  assertResult(3) {
    checkAndRun("""
    (
      ((length ((l (list num))) num (if (isnil l) 0 (+ 1 (@ length (tail l))))))
      (@ length (cons 1 (cons 2 (cons 3 (nil num)))))
    )
    """)
  }
  
  assertResult(6) {
    checkAndRun("""
    (
      ((sum ((l (list num))) num (if (isnil l) 0 (+ (head l) (@ sum (tail l))))))
      (@ sum (cons 1 (cons 2 (cons 3 (nil num)))))
    )
    """)
  }
}

test("37. 'Typecheck and run list program 2: `append`'") {
  
  assertResult((0,"(1::(2::(3::(4::(5::(6::Nil))))))\n")) {
    checkAndResultAndOutput("""
    (
      ((append ((l1 (list num)) (l2 (list num))) (list num)
          (if (isnil l1) l2 (cons (head l1) (@ append (tail l1) l2)))))
      (write (@ append (cons 1 (cons 2 (cons 3 (nil num)))) (cons 4 (cons 5 (cons 6 (nil num))))))
    )
    """)
  }
   
}

test("38. 'Typecheck and run list program 3: `reverse`'") {
  assertResult((0,"(3::(2::(1::Nil)))\n")) {
    checkAndResultAndOutput("""
    (
      (
        (revHelper ((l (list num)) (acc (list num))) (list num)
          (if (isnil l) acc (@ revHelper (tail l) (cons (head l) acc))))
        (reverse ((l (list num))) (list num)
          (@ revHelper l (nil num)))
      )
      (write (@ reverse (cons 1 (cons 2 (cons 3 (nil num))))))
    )
    """) 
  }
}

test("39. 'Typecheck and run list program 4: `zip`'") {
  assertResult((0,"((1::(4::Nil))::((2::(5::Nil))::((3::(6::Nil))::Nil)))\n")) {
    checkAndResultAndOutput("""
    (
      ((zip ((l1 (list num)) (l2 (list num))) (list (list num)) 
        (if (isnil l1) (nil (list num)) (if (isnil l2) (nil (list num))
            (cons (cons (head l1) (cons (head l2) (nil num))) (@ zip (tail l1) (tail l2)))))))

      (write (@ zip (cons 1 (cons 2 (cons 3 (nil num)))) (cons 4 (cons 5 (cons 6 (cons 7 (nil num)))))))
    )
    """)
  }
}

// Runtime errors:
// only divide-by-0 and errors on Lists are expected to occur: the other errors
// SHOULD be protected against by typechecking.
test("40. 'Runtime error: head on empty list'") {
  intercept[InterpException] { chkE("(head (nil num))") }
  intercept[InterpException] { chkE("(head (nil bool))") }
  intercept[InterpException] { chkE("(head (nil (list num)))") }
}

test("41. 'Runtime error: tail on empty list'") {
  intercept[InterpException] { chkE("(tail (nil num))") }
  intercept[InterpException] { chkE("(tail (nil bool))") }
  intercept[InterpException] { chkE("(tail (nil (list num)))") }
}

test("42. Runtime error: divide by 0") {
  intercept[InterpException] { chkE("(/ 2 0)") }
}




}

