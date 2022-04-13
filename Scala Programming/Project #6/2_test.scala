//test: Test

// test of the solution

import org.scalatest.FunSuite

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


  val factorialProgram = """
  (letRec fact (fun (x) (if (<= x 0) 1 (* x (@ fact (- x 1)))))
     (@ fact 6))
"""
 
  val example = """
     (let f (fun (x)(fun (y)(isPair x)))
        (let g (@ f (pair 41 42)) ( @ g 2 )
         ))
"""

  test("simple test of factorial program" ) {
    assertResult((720, "")) { resultAndOutput(factorialProgram) }
  }

  test("simple test of a program" ) {
    assertResult((1, "")) { resultAndOutput(example) }
  }
  


}



