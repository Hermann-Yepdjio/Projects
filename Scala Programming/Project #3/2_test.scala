//test: TestFor
import org.scalatest.FunSuite
import SExprLibrary._
import Parser._
import Process._

class TestFor extends FunSuite {

  // first some "tests" that just illustrate how to get debug output about the stack machine
  test ("showing the machine code") {
    Process.process("(:= x (+ 1 2))",1)
  }
  test ("showing each execution step") {
    Process.process("(:= x (+ 1 2))",2)
  }

  /* Replace the console output with a `ByteArrayOutputStream`,
     in order to intercept the interpreter's `write` side-effect and log the results. */
  def runWithLog(s:String): (Int, String) = {
    var result = 0
    val logger = new java.io.ByteArrayOutputStream(10240)
    Console.withOut(logger) { result = Process.process(s) }
    logger.flush()
    (result, logger.toString() )
  }

  /* Triple quotes can be used to define strings with embedded newline characters */
  val primesCode = """
{ write out all primes in [2..100], using 
  inefficient algorithm from lecture 1. }
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
 
"""

 val primesResult = "2\n3\n5\n7\n11\n13\n17\n19\n23\n29\n31\n37\n41\n43\n47\n53\n59\n61\n67\n71\n73\n79\n83\n89\n97\n"

  test("prime number generator") {
    assertResult((0, primesResult)) { runWithLog(primesCode) }
  }

  val easyLoop = "(for i 1 10 (write i))"
  test("easy loop using for") {
    assertResult((0, "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n")) { runWithLog(easyLoop) }
  }

  val bizarreExpression = "(for i (block (:= j (+ i 9)) 1) (- j i) (block (write i) (:= i (+ i 3))))"
  test("bizarre expression given in the assignment description") {
    assertResult((0, "1\n5\n")) { runWithLog(bizarreExpression) }
  }

}


