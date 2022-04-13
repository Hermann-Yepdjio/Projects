//test: Test

// test of the solution

import org.scalatest.FunSuite

import Process._


class Test extends FunSuite {
  
  
  
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
  def runsTo(v:Int, s:String, p:String) { 
    
    try {
      val (vr,sr) = resultAndOutput(p)
      assertResult ((v,s)) { (vr,sr) }
    } catch {
      case x:java.lang.StackOverflowError => assertResult("no stack overflow occurs") { "stack overflow occurs" }
      case x:InterpException => assertResult("no errors") { "error: " + x.string }
     }
  }
    val factorialProgram = """
  (letRec fact (fun (x) (if (<= x 0) 1 (* x (@ fact (- x 1)))))
     (@ fact 6))
"""

  test("simple test of factorial program" ) {
    assertResult((720, "")) { resultAndOutput(factorialProgram) }
  }
  

  test(" main return value must be int") {
     intercept[InterpException] { resultAndOutput("(fun (x) 0)") }
  }
  
/*
    test("Literals and arithmetic work as expected") {
    runsTo(10, "", "10")
    runsTo(7, "", "(+ 3 4)")
    runsTo(12, "", "(* 3 4)")
    runsTo(7, "", "(/ 14 2)" )
    runsTo(0, "", "(<= 1 0)")
    runsTo(1, "", "(<= 0 1)")
  }
  

  
  test("If, write, and block work as expected") { 
    runsTo(1, "1\n", "(write 1)")
    runsTo(2, "1\n2\n", "(block (write 1) (write 2))")
    runsTo(2, "2\n", "(if 0 (write 1) (write 2))")
    runsTo(1, "1\n", "(if 1 (write 1) (write 2))")
    intercept[InterpException] { resultAndOutput("(if (fun (x) 0) 0 0)") }
  }
  
  
  test("Let-bound variables work as expected") {
    intercept[InterpException]{ resultAndOutput("previouslyUnusedVar") }
    runsTo(0, "", "(let x 0 0)")
    runsTo(2, "", "(let x 2 x)")
    runsTo(1, "", "(let x 0 (let y 1 y))")
    runsTo(7, "", "(let x 7 (let y 1 x))")
    runsTo(7, "", "(let x 7 (let y x y))")
    runsTo(8, "", "(let x 8 (let y x x))")
    runsTo(10, "", "(let x (let y 10 y) x)")
    runsTo(11, "", "(let x 9 (let y 1 (let z (+ x y) (+ z 1))))")
  }
  
*/

  test("creating, returning, and applying a function that doesn't use anything from its closure environment works as expected") {
    runsTo(7, "3\n", "(let x (fun () (block (write 3) 7)) (@ x))")
    runsTo(7, "3\n", "(let y 6 (let x (fun () (block (write 3) 7)) (@ x)))")
  }
  
  test("creating and applying recursive function that doesn't use anything from its closure environment works as expected") {
  runsTo(720, "", "(let g (fun () (letRec fact (fun (x) (if (<= x 0) 1 (* x (@ fact (- x 1))))) (@ fact 6))) (@ g))")
  intercept[InterpException] { resultAndOutput("(letRec f 0 0)") }
  }
  
  test("creating and applying recursive function that builds closure containing a heap reference to another recursive function works as expected") {
  runsTo(720, "", "(letRec fact6 (fun () (letRec fact (fun (x) (if (<= x 0) 1 (* x (@ fact (- x 1))))) (@ fact 6))) (@ fact6))")
  }


  test("creating, returning, (but not applying) a function that uses closure environment works as expected") {
  runsTo(0, "", "(let f (fun (x) (fun () x)) (let g (@ f 3) 0))")
  runsTo(0, "", "(let x 3 (let f (fun () (fun () x)) (let g (@ f) 0)))")
  }
  
  test("creating, returning, and applying a function with closure environment holding a num (param of parent function) works as expected") {
  runsTo(3, "", "(let f (fun (x) (fun () x)) (let g (@ f 3) (let x 100 (@ g))))")
  }
  
  test("creating, returning, and applying a closure holding a num (bound in parent let) works as expected") {
  runsTo(3, "", "(let f (let x 3 (fun () (fun () x))) (let g (@ f) (let x 100 (@ g))))")
  }
  
  test("creating, returning, and applying a closure holding a fun (param of parent function) works as expected") {
    
    try {
      val (v, w) = resultAndOutput("(let f (fun (x) (fun () (@ x))) (let g (@ f (fun () 3)) (let x 100 (@ g))))")
      assertResult((3, "")) { (v, w) }
    } catch {
      case x:java.lang.StackOverflowError => assertResult("no stack overflow occurs") { "stack overflow occurs" }
     }
  }
  
  test("creating, returning, and applying a closure holding a fun (bound in parent let) works as expected") {
    
    try {
      val (v, w) = resultAndOutput("(let f (let x (fun () 3) (fun () x)) (let g (@ f) ( let x 100 (@ g))))")
      assertResult((3, "")) { (v, w) }
    } catch {
      case x:java.lang.StackOverflowError => assertResult("no stack overflow occurs") { "stack overflow occurs" }
     }
  }
  
  

/*
  val primesProgram = """
  (letRec f
     (fun (n)
        (if (<= n 25)
            (block
   	      (if (letRec g
	               (fun (d)
		            (if (<= d (- n 1))
		                (let m (* d (/ n d))
                  		       (if (<= n m)
		 	                   0
			                   (@ g (+ d 1))))
		                1))
                     (@ g 2))
   	        (write n)
	          (block))
    	      (@ f (+ n 1)))
  	    0))
   (@ f 2))	  
"""
  
  val primesOutput = "2\n3\n5\n7\n11\n13\n17\n19\n23\n"
  
  test("pure-functional primes programs works as expected") {
          runsTo(0, primesOutput, primesProgram)
  }
 */

  test("stack param") {
    runsTo(1,"","(@ (@ (fun (x) (fun (y) x)) 1) 2)")
  }

  
  test("let") {
    runsTo(1,"","(@ (@ (fun (x) (let y x (fun (z) y))) 1) (let z 2 3))")
  }
  
  test ("letRec") {
    runsTo(1,"","(@ (@ (fun (x) (letRec f (fun (z) 1) (fun (y) (@ f y)))) 1) (letRec g (fun (z) 2) 3))")
  }

  test("t5") {
    runsTo(61,"","(let z 42 (let w 19 (let g (fun (h) (@ h 100 w)) (let f (fun (x q) (+ q z)) (@ g f)))))")
  }

  test("t6") {
    runsTo(142,"","(let z 42 (let w 19 (letRec g (fun (n h) (if (== n 0) (@ h 100) (@ g (- n 1) h))) (let f (fun (x) (+ x z)) (@ g 10 f)))))")
  }

  test("t7") {
    runsTo(142,"","(let f (fun (x) (fun (y) (+ x y))) (@ (@ f 42) 100))")
  }

  test("t8") {
    runsTo(142,"","(letRec f (fun (x) (letRec h (fun (y) (+ x y)) h)) (let g (@ f 42) (@ g 100)))")
  }

}

