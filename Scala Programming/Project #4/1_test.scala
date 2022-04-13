//test: Test

import org.scalatest.FunSuite
import SExprLibrary._
import Process._

class Test extends FunSuite {
  
   def expectResult(p:Int, s:String, debug:Int = 0) = assertResult(p) { Process.process(s,debug) }
  
  // expectConsoleOutput: intercept console output and aggregate it so it can be easily tested
  def expectConsoleOutput(p:String, s:String, debug:Int = 0) = {
    val output = new java.io.ByteArrayOutputStream(10240)
    def testOutput = assertResult(p) { Process.process(s,debug); output.flush(); output.toString() }
    Console.withOut(output)(testOutput)
  }
  
  // Here are some tests which illustrate the top-level functions introduced in this assignment.
  test("test some simple functions") {
    expectResult(0,"(() ((f () 0)) (@ f))") 
    expectConsoleOutput("1\n2\n", "(() ((f (a b) 0)) (@ f (write 1) (write 2)))")
    expectResult(1,"(((a 3)) ((a () 2)) (- a (@ a)))") 
    intercept[InterpException] { Process.process("(((a 3)) () (@ a))") }
    intercept[InterpException] { Process.process("(() ((a (a) a)) (@ a))") }
  }

  // Here is a test which turns on debugging information 
  test("test with debug ouput") {
    expectResult(3,"(() ((f (a b) (+ a b))) (@ f 1 2))",2)  // debug = 2 displays useful info during evaluation
  }
  
  // Here's a familiar program that has been translated (to a pretty minimal extent)
  // to fit the new syntax of this week's toy language.
  val primesCode = """
{ write out all primes in [2..100], using 
  inefficient algorithm from lecture 1. }
(((n 0) (d 0) (m 0) (p 0))
 ()
(block
  (:= n 2)
  (while (<= n 100)
    (block 
      (:= p 1) { flag indicating primeness: initialize to true }
      (:= d 2) 
      (while (<= d (- n 1))
        (block
          (if (<= (% n d) 0) { always have (% n d) >= 0 }
              (:= p 0)       { i.e., (% n d) == 0, so d divides n, so set p false }
              (block))       { (block) is a no-op}
          (:= d (+ d 1))))
      (if p (write n) (block))
      (:= n (+ n 1)))))
)      
"""
val primesResult = "2\n3\n5\n7\n11\n13\n17\n19\n23\n29\n31\n37\n41\n43\n47\n53\n59\n61\n67\n71\n73\n79\n83\n89\n97\n"


  test("primes code prints out the right result") {
    expectConsoleOutput(primesResult, primesCode) 
  }


  // Here's the example program from the assignment description.
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

  test("example program gives the expected result") {
    expectResult(102, exampleProgram)
  }  

}


