//test: Test

// test of the solution

import org.scalatest.FunSuite

import Solution._

class Test extends FunSuite {

val primesOutput = "2\n3\n5\n7\n11\n13\n17\n19\n23\n29\n31\n"

  def resultAndOutput(s:String) : (Int, String) = { 
    val outputChannel = new java.io.ByteArrayOutputStream
    def testOutput = {
      val result = Process.process(s)
      outputChannel.flush()
      val output = outputChannel.toString()
      (result, output)
    }
    Console.withOut(outputChannel)(testOutput)
  }
  
  test("primes output") {
    assertResult((0, primesOutput)) { resultAndOutput(answer) }
  }
  
  test("no more than 1 write statement used") {
    val (a,w) = Parser.parseW(answer)
    assertResult(1) { w }
  }
}


