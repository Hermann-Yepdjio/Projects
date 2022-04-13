//test: TestFor
import org.scalatest.FunSuite
import SExprLibrary._
import Parser._
import Process._

class NullStream extends java.io.OutputStream { def write(x:Int) = {} }

class TestFor extends FunSuite {

  def run(s:String) : String = Process.process(s).toString
  def runSilent(s:String) : String = {
    val ignore = new NullStream
    Console.withOut(ignore)(run(s))
  }
  def runWithLog(s:String): (Int, String) = {
    var result = 0
    val logger = new java.io.ByteArrayOutputStream(10240)
    Console.withOut(logger) { result = Process.process(s) }
    logger.flush()
    (result, logger.toString() )
  }

  /* Replace the console output with a `ByteArrayOutputStream`,
     in order to intercept the interpreter's `write` side-effect and log the results. */

  def pp_iso(s:String) = { 
    val e = parse(s)
    assertResult (e) { parse(e.toString) }
  }
  
  // Programs used in the tests
  // this should print 1 5 and evaluate to 0
  val bizarreExpression = "(for i (block (:= j (+ i 9)) 1) (- j i) (block (write i) (:= i (+ i 3))))"
  // this should print 2 6 10 and evaluate to 0
  val trickyExampleProgram = "(for i 0 (block (:= i (+ i 2)) 10) (block (write i) (:= i (+ i 3))))"
  val easyLoop = "(for i 1 10 (write i))"
  val easyProgram2 = "(for i 0 9 (for j 0 9 (write (+ j (* 10 i)))))"
  val sum1to10 = "(block (for i 0 10 (block (write s) (:= s (+ s i)))) s)"
  val trickyProg2 = "(block (:= s 7) (for i 3 s (block (write i) (:= s 5))) s)"
  val trickyProg3 = "(for i (+ j 1) (block (:= j 2) 4) (write i))"
  val trickyProg4 = "(block (for i 1 4 (:= j i)) j)" 

  val primesCode = """
(block
  (:= n 2)
  (while (<= n 100)
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
      (:= n (+ n 1)))))
"""

  val primesResult = "2\n3\n5\n7\n11\n13\n17\n19\n23\n29\n31\n37\n41\n43\n47\n53\n59\n61\n67\n71\n73\n79\n83\n89\n97\n"

/*

  // Deprecated framework
  def expect(x:Any)(e:Any) = assertResult(x)(e) // compatibility with old ScalaTest

  def expectConsoleOutput(s:String, p:String) = {
    val output = new java.io.ByteArrayOutputStream(10240)
    def testOutput = expect(p) { run(s); output.flush(); output.toString() }
    Console.withOut(output)(testOutput)
  }

  // Basic tests
  test("variable implicit init") {
    expect("0") { runSilent("x") }
    expect("0") { runSilent("foo") }
    expect("0") { runSilent("bar1") }
  }

  test("int literals") {
    expect("0") { runSilent("0") }
    expect("-1") { runSilent("-1") }
    expect("12") { runSilent("12") }
  }
  
  test("arithmetic") {
    expect("0") { runSilent("(* 0 7)") }
    expect("-3") { runSilent("(- 0 3)") }
    expect("12") { runSilent("(* 6 2)") }
    expect("12") { runSilent("(/ 24 2)") }
    intercept[InterpException]{ runSilent("(/ 7 0)") }
  }

  test("le comparison") {
    expect("0") { runSilent("(<= 1 0)") }
    expect("1") { runSilent("(<= 0 0)") }
    expect("1") { runSilent("(<= 0 1)") }
  }

  test("simple assignments") {
    expect("0") { runSilent("(:= s s)") }
    expect("1") { runSilent("(:= s 1)") }
  }
  
  test("while returns 0") {
    expect("0") { runSilent("(while 0 0)") }
    expect("0") { runSilent("(block (:= s 3) (while s (:= s (- s 1))))") }
  }
  
  test("while + assignment + write") {
    expectConsoleOutput("(block (:= s 3) (while s (block (write s) (:= s (- s 1)))))", "3\n2\n1\n")
  }

  test("if result") {
    expect("0") { runSilent("(if 1 0 1)") }
    expect("0") { runSilent("(if 0 1 0)") }
    expect("0") { runSilent("(if 2 0 1)") }
  }
  
  test("if evaluates only one branch") {
    expectConsoleOutput("(if 0 (write 1) (write 0))","0\n")
    expectConsoleOutput("(if 1 (write 0) (write 1))", "0\n")
  }
  
  test("write") {
    expectConsoleOutput("(write 3)", "3\n")
    expectConsoleOutput("0", "")
    expectConsoleOutput("(block (write 2) (write 1))", "2\n1\n")
  }

  test("empty block returns 0") {
    expect("0") { runSilent("(block)") }
  }
  
  test("block of several exprs returns last") {
    expect("2") { runSilent("(block 2)") }
    expect("3") { runSilent("(block 4 3 2 1 3)") }
  }
  
 */

  // prime number program
  test("primes code prints out the right result") {
    assertResult((0,primesResult)) {runWithLog(primesCode)}
  }
  
  // For loop tests

  test("parse and print `for` loop") {
    pp_iso(trickyExampleProgram)
    pp_iso(easyLoop)
    pp_iso(easyProgram2)
    pp_iso(sum1to10)
    
  }
  test("`for` loop returns 0") {
    assertResult("0") { runSilent(trickyExampleProgram) }
    assertResult("0") { runSilent(easyLoop) }
    assertResult("0") { runSilent(easyProgram2) }
  }
  
  test("store side effects of `for` loop modifying variables from outside") {
    assertResult("55") { runSilent(sum1to10) }
  }
  
  /*
    The remaining tests intercept console output 
    and use `write` to trace the loop executions to ensure correct behavior.
  */
  
  test("writes by straightforward `for` loop") {
    val s1 = ((1 to 10).map(_.toString) :\ "") ((x,ys) => x+"\n"+ys)
    val s2 = ((0 to 99).map(_.toString) :\ "") ((x,ys) => x+"\n"+ys)
    assertResult(s1) { runWithLog(easyLoop)._2 }
    assertResult(s2) { runWithLog(easyProgram2)._2 }
  }
  
  test("bizarre expression from problem description") {
    assertResult((0,"1\n5\n")) { runWithLog(bizarreExpression) }
  }

  test("writes by `for` loop mutating the loop variable, in a tricky way") {
    assertResult("2\n6\n10\n") { runWithLog(trickyExampleProgram)._2 }
  }
  
  test("writes by `for` loop mutating a single thing from outside of the loop") {
    assertResult("0\n0\n1\n3\n6\n10\n15\n21\n28\n36\n45\n") { runWithLog(sum1to10)._2 } 
  }
  
  test("writes and output for trickyprog2") {
    assertResult((5,"3\n4\n5\n6\n7\n")) { runWithLog(trickyProg2) }
  }

  test("writes and output for trickyprog3") {
    assertResult((0,"1\n2\n3\n4\n")) { runWithLog(trickyProg3) }
  }

  test("output for trickyprog4") {
    assertResult ("4") { runSilent(trickyProg4) }
  }

}

