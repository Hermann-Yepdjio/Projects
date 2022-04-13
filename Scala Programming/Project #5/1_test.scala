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

  // These two tests will fail under the initial template solution, but should succeed when you are done.
  // Remember to add more tests!

  test("swap function with locals") {
    runsTo(2, "1\n2\n", "(() ((swap (x y) (let z x (block (:= x y) (:= y z))))) (let x 2 (let y 1 (block (@ swap x y) (write x) (write y)))))")
  }

  test("swap function with globals") {
    runsTo(2, "1\n2\n", "(((x 2) (y 1)) ((swap (x y) (let z x (block (:= x y) (:= y z))))) (block (@ swap x y) (write x) (write y)))")
  }
  
}

// //test: Test
// 
// import org.scalatest.FunSuite
// import SExprLibrary._
// import Process._
// 
// class Test extends FunSuite {
// 
//   // some shorthands
//   def expect(x:Any)(e:Any) = assertResult(x)(e) // compatibility with old ScalaTest
// 
//   def run(s:String,debug:Int = 0) = Process.process(s,debug)
//   
//   def expr(s:String) : String = "(() () "+ s + ")"
// 
//   def runExpr(s:String,debug:Int = 0) : Int = run(expr(s),debug)
//   
//   def resultAndOutput(s:String) : (Int, String) = { 
//     val outputChannel = new java.io.ByteArrayOutputStream(10240)
//     def testOutput = {
//       val result = Process.process(s)
//       outputChannel.flush()
//       val output = outputChannel.toString()
//       (result, output)
//     }
//     Console.withOut(outputChannel)(testOutput)
//   }
// 
//   def runsTo(v:Int, s:String, p:String) { expect ((v,s)) {resultAndOutput(p)} }
// 
//   test("function definitions and application passing locals without assignment to parameters") {
//     // note: arguments to functions must be variables (not arbitrary expressions)    
//     intercept[ParseException] { resultAndOutput("(() ((f (x) 0)) (@ f (+ 2 1)))") }
//     runsTo(0, "", "(() ((f () (write 1))) 0)")
//     runsTo(1, "1\n", "(() ((f () (write 1))) (@ f))")
//     runsTo(2, "", "(() ((f (x) 2)) (let x 1 (@ f x)))")
//     runsTo(1, "", "(() ((f (x) x)) (let x 1 (@ f x)))")
//     runsTo(5, "", "(() ((f (x y) (+ x y))) (let x 2 (let y 3 (@ f x y))))")
//   }
// 
//   test("function definitions and application passing globals without assignment to parameters") {
//     // note: arguments to functions must be variables (not arbitrary expressions)    
//     runsTo(2, "", "(((y 1)) ((f (x) 2)) (@ f y))")
//     runsTo(1, "", "(((y 1)) ((f (x) x)) (@ f y))")
//     runsTo(5, "", "(((a 2) (b 3)) ((f (x y) (+ x y))) (@ f a b))")
//   }
// 
//   // These two tests will fail under the initial template solution, but should succeed when you are done.
//   // Remember to add more tests!
// 
//   test("swap function with locals") {
//     runsTo(2, "1\n2\n", "(() ((swap (x y) (let z x (block (:= x y) (:= y z))))) (let x 2 (let y 1 (block (@ swap x y) (write x) (write y)))))")
//   }
// 
//   test("swap function with globals") {
//     runsTo(2, "1\n2\n", "(((x 2) (y 1)) ((swap (x y) (let z x (block (:= x y) (:= y z))))) (block (@ swap x y) (write x) (write y)))")
//   }
//   
// }
// 
// // //test: Test
// // 
// // import org.scalatest.FunSuite
// // import SExprLibrary._
// // import Process._
// // 
// // class Test extends FunSuite {
// // 
// //   // some shorthands
// //   def expect(x:Any)(e:Any) = assertResult(x)(e) // compatibility with old ScalaTest
// // 
// //   def run(s:String,debug:Int = 0) = Process.process(s,debug)
// //   
// //   def expr(s:String) : String = "(() () "+ s + ")"
// // 
// //   def runExpr(s:String,debug:Int = 0) : Int = run(expr(s),debug)
// //   
// //   def resultAndOutput(s:String) : (Int, String) = { 
// //     val outputChannel = new java.io.ByteArrayOutputStream(10240)
// //     def testOutput = {
// //       val result = Process.process(s)
// //       outputChannel.flush()
// //       val output = outputChannel.toString()
// //       (result, output)
// //     }
// //     Console.withOut(outputChannel)(testOutput)
// //   }
// // 
// //   def runsTo(v:Int, s:String, p:String) { expect ((v,s)) {resultAndOutput(p)} }
// // 
// //   test("function definitions and application passing locals without assignment to parameters") {
// //     // note: arguments to functions must be variables (not arbitrary expressions)    
// //     intercept[ParseException] { resultAndOutput("(() ((f (x) 0)) (@ f (+ 2 1)))") }
// //     runsTo(0, "", "(() ((f () (write 1))) 0)")
// //     runsTo(1, "1\n", "(() ((f () (write 1))) (@ f))")
// //     runsTo(2, "", "(() ((f (x) 2)) (let x 1 (@ f x)))")
// //     runsTo(1, "", "(() ((f (x) x)) (let x 1 (@ f x)))")
// //     runsTo(5, "", "(() ((f (x y) (+ x y))) (let x 2 (let y 3 (@ f x y))))")
// //   }
// // 
// //   test("function definitions and application passing globals without assignment to parameters") {
// //     // note: arguments to functions must be variables (not arbitrary expressions)    
// //     runsTo(2, "", "(((y 1)) ((f (x) 2)) (@ f y))")
// //     runsTo(1, "", "(((y 1)) ((f (x) x)) (@ f y))")
// //     runsTo(5, "", "(((a 2) (b 3)) ((f (x y) (+ x y))) (@ f a b))")
// //   }
// // 
// //   // These two tests will fail under the initial template solution, but should succeed when you are done.
// //   // Remember to add more tests!
// // 
// //   /*test("swap function with locals") {
// //     runsTo(2, "1\n2\n", "(() ((swap (x y) (let z x (block (:= x y) (:= y z))))) (let x 2 (let y 1 (block (@ swap x y) (write x) (write y)))))")
// //   }
// // 
// //   test("swap function with globals") {
// //     runsTo(2, "1\n2\n", "(((x 2) (y 1)) ((swap (x y) (let z x (block (:= x y) (:= y z))))) (block (@ swap x y) (write x) (write y)))")
// //   }*/
// //   
// // }
// // 

