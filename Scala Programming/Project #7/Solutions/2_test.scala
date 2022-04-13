//test: Test

// test of the solution

import org.scalatest.FunSuite

import Solution._
import Library._

class Test extends FunSuite {

// These functions use a complete version of the previous part of the lab
def typecheck(s:String) = Check.check(Parser.parse(s))
def run(s:String) = Interp.interp(Parser.parse(s))

// Test the additional parts A and B.
// These 2 tests are the exact same as the specification tests we're using to check this problem.

test("""'
      Additional Part A: 
      (
        Program not involving `if`
        that raises a TypingException,
        yet can be interpreted
        without a runtime error
      )
      '
     """) {
  intercept[TypingException] {typecheck(Solution.partA)}
  run(Solution.partA)
}


test("""'
      Additional Part B
      (
        Program that passes typechecker
        but raises an InterpException
      )
      '
     """) {
  typecheck(Solution.partB)
  intercept[InterpException] {run(Solution.partB)}
}


}


