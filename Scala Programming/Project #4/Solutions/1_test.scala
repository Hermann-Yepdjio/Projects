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
  
  def expectConsoleOutput(p:String, s:String) = {
    val output = new java.io.ByteArrayOutputStream(10240)
    def testOutput = expect(p) { run(s); output.flush(); output.toString() }
    Console.withOut(output)(testOutput)
  }
  
  def expectExprOutput(p:String, s:String) = expectConsoleOutput(p, expr(s))
  
  val primesCode = """
(((n 0) (d 0) (m 0) (prime 0))
 ()
(block
  (:= n 2)
  (while (<= n 25)
    (block 
      (:= prime 1)
      (:= d 2) 
      (while (<= d (- n 1))
        (block
          (:= m (* d (/ n d)))  
          (if (<= n m)
              (:= prime 0)
              (block))
          (:= d (+ d 1))))
      (if prime (write n) (block))
      (:= n (+ n 1))))))
"""
val primesResult = "2\n3\n5\n7\n11\n13\n17\n19\n23\n"

/*

  // Basic tests
 
  test("arithmetic") {
    expect(0) { runExpr("0") }
    expect(-1) { runExpr("-1") }
    expect(12) { runExpr("12") }
    expect(0) { runExpr("(* 0 7)") }
    expect(-3) { runExpr("(- 0 3)") }
    expect(12) { runExpr("(* 6 2)") }
    expect(12) { runExpr("(/ 24 2)") }
    intercept[InterpException] { runExpr("(/ 1 0)") }
    expect(0) { runExpr("(<= 1 0)") }
    expect(1) { runExpr("(<= 0 0)") }
    expect(1) { runExpr("(<= 0 1)") }
  }
  
  test("if/while") {
    expect(0) { runExpr("(while 0 0)") }
    expect(0) { run("(((s 0)) () (block (:= s 3) (while s (:= s (- s 1)))))") }
    expectConsoleOutput("3\n2\n1\n", "(((s 0)) () (block (:= s 3) (while s (block (write s) (:= s (- s 1))))))")
    expect(0) { runExpr("(if 1 0 1)") }
    expect(0) { runExpr("(if 0 1 0)") }
    expect(0) { runExpr("(if 2 0 1)") }
    expectExprOutput("3\n", "(if 0 (write 1) (write 3))")
    expectExprOutput("0\n", "(if 1 (write 0) (write 1))")
    intercept[InterpException] { runExpr("(if (pair 1 2) 0 1)") }
    intercept[InterpException] { runExpr("(while (pair 1 2) 0)") }
  }
  
  test("write") {
    expectExprOutput("3\n", "(write 3)")
    expectExprOutput("", "0")
    expectExprOutput("2\n1\n", "(block (write 2) (write 1))")
  }
  
  test("blocks") {
    expect(0) { runExpr("(block)") }
    expect(2) { runExpr("(block 2)") }
    expect(3) { runExpr("(block 4 3 2 1 3)") }
  }
  
  // prime number program
  test("primes code prints out the right result") {
    expectConsoleOutput(primesResult, primesCode) 
  }
  

  test("simple assignments involving globals") {
    expect(0) { run("(((s 0)) () (:= s s))") }
    expect(1) { run("(((s 0)) () (:= s 1))") }
    expect(1) { run("(((s 0) (t 1)) () (:= s t))") }
    intercept[InterpException] { run("(() ((a () 0)) a)") }
  }
  
// We explicitly avoid checking for this.
//  test("unitialized global") {
//    intercept[InterpException] { run ("(((a (@ f))) ((f () a)) a)") }
//  }

  // Tests added (rather than adapted) for this assignment are below.

  test("test some simple functions") {
    expect(0) { run("(() ((f () 0)) (@ f))") }
    expect(3) { run("(() ((f (a b) (+ a b))) (@ f 1 2))") }
    expectConsoleOutput("1\n2\n", "(() ((f (a b) 0)) (@ f (write 1) (write 2)))")
    expect(1) { run("(((a 3)) ((a () 2)) (- a (@ a)))") }
    intercept[InterpException] { run("(((a 3)) () (@ a))") }
    intercept[InterpException] { run("(() ((a (a) a)) (@ a))") }
    intercept[InterpException] { run("(() ((a (a) a)) (@ a 1 2))") }
    expect(0) { run("(() ((a (i) (if (<= 3 i) 0 (@ a (- i 1))))) (@ a 5))") }
  }


  test("test nested function calls") {
    expect (3) { run("(() ((f (x) x)) (+ (@ f 1) (@ f 2)))") }
    expect (38) {  run("(() ((f (x y) (+ x y))) (@ f (@ f 3 5) (@ f 10 20)))") }
    expect (38) {  run("(() ((g (u) u) (f (x y) (+ (@ g x) (@ g y)))) (@ f (@ f 3 5) (@ f 10 20)))") }
    expect (34) {  run("(() ((g (u v) (* u v)) (f (x y) (+ (@ g x x) (@ g y y)))) (@ f 3 5))") }
    expect (342) {  run("(() ((g (u v w) (+ u (+ v w))) (f (x y) (+ (@ g x x x) (@ g y y y)))) (@ f (@ f 3 5) (@ f 10 20)))") }
  }

  test("test some simple pairs") {
    intercept[InterpException] { runExpr("(pair 1 2)") }
    expectExprOutput("(1.2)\n", "(block (write (pair 1 2)) 0)")
    expectExprOutput("(1.(2.0))\n", "(block (write (pair 1 (pair 2 0))) 0)")
    expect(3) { runExpr("(fst (pair 3 2))") }
    expect(6) { runExpr("(snd (pair 2 6))") }
    expect(-4) { run("(((a (pair 3 7))) () (- (fst a) (snd a)))") }
    expect(6) { runExpr("(snd (snd (pair 2 (pair 2 6))))") }
    expect(1) { runExpr("(isPair (pair 2 6))") }
    expect(0) { runExpr("(isPair 3)") }
    expectExprOutput("1\n2\n", "(block (pair (write 1) (write 2)) 0)")
    intercept[InterpException] { runExpr("(fst 0)") }
    intercept[InterpException] { runExpr("(snd 2)") }
    intercept[InterpException] { runExpr("(+ 0 (pair 1 2))") }
    intercept[InterpException] { runExpr("(<= (pair 1 2) (pair 3 4))") }    
  }
  

  test("test global/arg scoping") {
//    expect(3) { run("(((x 2) (x (+ x 1))) () x)") }  -- we explicitly don't test for repeated global definitions
    expect(2) { run("(((x 3)) ((f (x) x)) (@ f 2))") }
    expect(5) { run("(((x 2)) ((f (x) (:= x 3))) (+ (@ f 0) x))") }
    expect(6) { run("(((x 2)) ((f (y) (:= x 4))) (+ x (@ f 0)))") } 
    expect(8) { run("(((x 2)) ((f (y) (:= x 4))) (+ (@ f 0) x))") } 
  }

 */

// Tests added that pertain directly to the student code
  
  // Let:
  test ("constant let body returns correctly") {
    expect(3) { runExpr("(let x 1 3)",2) } // for debug output
  }
  
  test("let variable shows up in environment") {
    expect(7) { runExpr("(let x 1 (block x 7))") }
  }

  test("let variable evaluated correctly") {
    expect(13) { runExpr("(let x 13 x)") }
  }
  
  test("let variable shadows global/function parameter variables") {
    expect(5) { run("(((a 3)) () (let a 5 a))") }
    expect(3) { run("(() ((a (i) (let i 3 i))) (@ a 7))") }
  }
  
  test("let variable shadows outer-scoped let variables") {
    expect(4) { runExpr("(let x 3 (let x 4 x))") }
    expect(4) { runExpr("(let x 3 (let x (+ 1 x) x))") }
  }
  
  test("test global/let scoping") {
    expect(2) { run("(((x 3)) () (let x 2 x))") }
    expect(5) { run("(((x 4)) () (+ (let x (:= x 5) 0) x))") }
    expect(6) { run("(((x 2)) () (+ x (let y (:= x 4) x)))") }
    expect(4) { run("(((x 1)) () (+ (let y 0 (:= x 2)) x))") }
    expect(8) { run("(((x 2)) () (+ (let y (:= x 4) x) x))") }
  }

  test("test arg/let scoping") {
    expect(2) { run("(() ((f (x) (let x 2 x))) (@ f 3))") }
    expect(5) { run("(() ((f (x) (+ (let x (:= x 5) 0) x))) (@ f 4))") }
    expect(6) { run("(() ((f (x) (+ x (let y (:= x 4) x)))) (@ f 2))") }
    expect(4) { run("(() ((f (x) (+ (let y 0 (:= x 2)) x))) (@ f 1))") }
    expect(8) { run("(() ((f (x) (+ (let y (:= x 4) x) x))) (@ f 2))") }
  }

  test("let order-of-evaluation: e is evaluated exactly once, before b") {
    expectExprOutput("1\n2\n", "(let x (write 1) (block x x x (write 2) x x))")
  }

  test("let as operand") {
    expect(6) { runExpr("(+ (let x 2 (+ x 1)) (let x 4 (- x 1)))") }
  }

  test("let binding function call") {
    expect(6) { run("(() ((f (x) (+ x 1))) (let y (@ f 2) (+ y 3)))") }
  }

  test("let as argument") {
    expect (6) { run("(() ((f (x) (+ x 1))) (@ f (let y 2 (+ y 3))))") }
  }

  test("let as second of two arguments (pop?)") {
    expect (3) { run("(() ((f (x y) (+  x y))) (@ f 1 (let z 2 z)))") }
  }

  test("let in function body") {
    expect (6) { run("(() ((f (x) (let y (+ x 1) (+ y 2)))) (@ f 3))") }
  }

  test("let and parameters") {
    expect (6) { run("(() ((f (x) (let y 2 (+ x y)))) (@ f 4))") }
  }

  test("let and function call") {
      expect (15)  { run("(() ((f (x) (+ x 1))) (let a 10 (+ (@ f 4) a)))") }
  }

  test("setFst/setSnd order of evaluation") {
    expectExprOutput("(1.2)\n3\n", "(block (setFst (write (pair 1 2)) (write 3)) 0)")
    expectExprOutput("(1.2)\n3\n", "(block (setSnd (write (pair 1 2)) (write 3)) 0)")
  }
  
  test("setFst/setSnd runtime error if first arg not pair") {
    intercept[InterpException] { runExpr("(setFst 0 0)") }
    intercept[InterpException] { runExpr("(setSnd 0 0)") }
  }

  test("setFst updates appropriate component") {
    expect(3) { run("(((a (pair 1 2))) () (block (setFst a 3) (fst a)))") }
  }

  test("setSnd updates appropriate component") {
    expect(3) { run("(((a (pair 1 2))) () (block (setSnd a 3) (snd a)))") }
  }

  test("setFst/setSnd do not touch the irrelevant component and return the modified pair value") {
    expect(3) { run("(((a (pair 0 0))) () (block (setFst a 3) (- (fst a) (snd a))))") }
    expect(-3) { run("(((a (pair 0 0))) () (block (setSnd a 3) (- (fst a) (snd a))))") }
    expect(3) { runExpr("(fst (setSnd (pair 3 1) 7))") }
    expect(3) { runExpr("(snd (setFst (pair 1 3) 7))") }
    expect(7) { runExpr("(fst (setFst (pair 1 3) 7))") }
    expect(7) { runExpr("(snd (setSnd (pair 3 1) 7))") }
  }
  
 
  val exampleProgram = """
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
"""
test("example program for let") { expect(102) { run(exampleProgram) } }

  
}

