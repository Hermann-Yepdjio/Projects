//test: Test

import org.scalatest.FunSuite

import Interp._
import Process._

class Test extends FunSuite {

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

// Some example tests using list programs (similar to those in Assignment 4)

test("'Typecheck and run list programs 1: `length` and `sum`'") {
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

test("'Typecheck and run list program 2: `append`'") {
  
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

test("'Typecheck and run list program 3: `reverse`'") {
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

test("'Typecheck and run list program 4: `zip`'") {
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


}


