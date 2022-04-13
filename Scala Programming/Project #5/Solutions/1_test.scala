//test: Test

import org.scalatest.FunSuite
import SExprLibrary._
import Process._

class Test extends FunSuite {

  // some shorthands
  def expect(x:Any)(e:Any) = assertResult(x)(e) // compatibility with old ScalaTest

  def run(s:String,debug:Int = 0) = Process.process(s,debug)
  
  def expr(s:String) : String = "(() () "+ s + ")"

  def runExpr(s:String,debug:Int = 0) : Int = run(expr(s),debug)
  
  def resultAndOutput(s:String) : (Int, String) = { 
    val outputChannel = new java.io.ByteArrayOutputStream(10240)
    def testOutput = {
      val result = Process.process(s)
      outputChannel.flush()
      val output = outputChannel.toString()
      (result, output)
    }
    Console.withOut(outputChannel)(testOutput)
  }

  def runsTo(v:Int, s:String, p:String) { expect ((v,s)) {resultAndOutput(p)} }

  test("function definitions and application passing locals without assignment to parameters") {
    // note: arguments to functions must be variables (not arbitrary expressions)    
    intercept[ParseException] { resultAndOutput("(() ((f (x) 0)) (@ f (+ 2 1)))") }
    runsTo(0, "", "(() ((f () (write 1))) 0)")
    runsTo(1, "1\n", "(() ((f () (write 1))) (@ f))")
    runsTo(2, "", "(() ((f (x) 2)) (let x 1 (@ f x)))")
    runsTo(1, "", "(() ((f (x) x)) (let x 1 (@ f x)))")
    runsTo(5, "", "(() ((f (x y) (+ x y))) (let x 2 (let y 3 (@ f x y))))")
  }

  test("function definitions and application passing globals without assignment to parameters") {
    // note: arguments to functions must be variables (not arbitrary expressions)    
    runsTo(2, "", "(((y 1)) ((f (x) 2)) (@ f y))")
    runsTo(1, "", "(((y 1)) ((f (x) x)) (@ f y))")
    runsTo(5, "", "(((a 2) (b 3)) ((f (x y) (+ x y))) (@ f a b))")
  }

  test("parameters are by reference: zeroing function") {
    runsTo(0, "", "(() ((zero (x) (:= x 0))) (let x 1 (block (@ zero x) x)))")
  }

  test("parameters are by reference: zeroing function with global") {
    runsTo(0, "", "(((x 1)) ((zero (x) (:= x 0))) (block (@ zero x) x))")
  }

  test("parameters are by reference: rotate function") {
    runsTo(2, "3\n5\n2\n", "(() ((rot (x y z) (let w z (block (:= z x) (:= x y) (:= y w)))))  (let x 2 (let y 3 (let z 5 (block (@ rot x y z) (block (write x) (write y) (write z)))))))")
  }
  
  test("parameters are by reference: rotate function with globals") {
    runsTo(2, "3\n5\n2\n", "(((x 2) (y 3) (z 5)) ((rot (x y z) (let w z (block (:= z x) (:= x y) (:= y w)))))  (block (@ rot x y z) (block (write x) (write y) (write z))))")
  }
  

  test("swap function using call-by-reference") {
    runsTo(2, "1\n2\n", "(() ((swap (x y) (let z x (block (:= x y) (:= y z))))) (let x 2 (let y 1 (block (@ swap x y) (write x) (write y)))))")
  }

  test("swap function using call-by-reference with globals") {
    runsTo(2, "1\n2\n", "(((x 2) (y 1)) ((swap (x y) (let z x (block (:= x y) (:= y z))))) (block (@ swap x y) (write x) (write y)))")
  }
  
  test("can do aliasing through call-by-reference") {
    runsTo(2, "", "(() ((f (x y) (:= x (+ x y)))) (let x 1 (@ f x x)))")
  }

  test("can do aliasing through call-by-reference with globals") {
    runsTo(2, "", "(((x 1)) ((f (x y) (:= x (+ x y)))) (@ f x x))")
  }
  
  test("let stores actual values: copying a value into a variable using let, outside function call, prevents modifying it using CBR") {
    runsTo(1, "", "(() ((zero (x) (:= x 0))) (let y 1 (let x y (block (@ zero x) y))))")
  }

  test("let stores actual values: copying a value into a variable using let, outside function call, prevents modifying it using CBR with globals") {
    runsTo(1, "", "(((y 1)) ((zero (x) (:= x 0))) (let x y (block (@ zero x) y)))")
  }
  
  test("let stores actual values: copying a value into a variable using let, inside function body, prevents modifying it using CBR") {
    runsTo(1, "", "(() ((zero (x) (let y x (:= y 0)))) (let x 1 (block (@ zero x) x)))")
  }

  test("let stores actual values: copying a value into a variable using let, inside function body, prevents modifying it using CBR with globals") {
    runsTo(1, "", "(((x 1)) ((zero (x) (let y x (:= y 0)))) (block (@ zero x) x))")
  }

  test("nested access") {
    runsTo(1, "","(() ((f (x y) (@ g y x)) (g (u v) (- u v))) (let a 1 (let b 2 (@ f a b))))")
  }

  test("nested access with assignment") {
    runsTo(1,"","(() ((f (x y) (@ g y x)) (g (u v) (:= u (- u v)))) (let a 1 (let b 2 (block (@ f a b) b))))")
  }

  test("deeper nested access") {
    runsTo(-1, "","(() ((h (p q) (@ f p p q q)) (f (x y z w) (@ g y z)) (g (u v) (- u v))) (let a 1 (let b 2 (@ h a b))))")
  }
  
}


