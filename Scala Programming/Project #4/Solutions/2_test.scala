//test: TestEq
import org.scalatest.FunSuite
import SExprLibrary._
import Process._

class TestEq extends FunSuite {
  
  def useLibraryDefinitions(s: String) = "(() (" + MyEqDefinition.definition + ") " + s + ")"
  def runWithLib(s: String) = Process.process(useLibraryDefinitions(s))
  
 test("Test some basic behavior") {
  assertResult (0) { runWithLib("(@ eq 0 1)") }
  assertResult (1) { runWithLib("(@ eq (pair 1 2) (pair 1 2))") }
 }
 test("Test integers against integers") {
  assertResult (1) { runWithLib("(@ eq 42 42)") }
  assertResult (1) { runWithLib("(@ eq 99 99)") }
  assertResult (0) { runWithLib("(@ eq 42 41)") }
  assertResult (0) { runWithLib("(@ eq 0 99)") }
 }   
 test("Test integers against pairs") {
  assertResult (0) { runWithLib("(@ eq 42 (pair 1 2))") }
  assertResult (0) { runWithLib("(@ eq (pair 0 10) 0)") }
 } 
 test("Test shallow pairs against pairs") {
  assertResult (1) { runWithLib("(@ eq (pair 1 2) (pair 1 2))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 0) (pair 1 2))") }
  assertResult (0) { runWithLib("(@ eq (pair 0 2) (pair 1 2))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 2) (pair 0 2))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 2) (pair 1 0))") }
  }
 test ("Test deeper pairs") {
  assertResult (1) { runWithLib("(@ eq (pair 1 (pair 3 4)) (pair 1 (pair 3 4)))") }
  assertResult (1) { runWithLib("(@ eq (pair 1 (pair (pair 5 6) 4)) (pair 1 (pair (pair 5 6) 4)))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 (pair (pair 5 6) 4)) (pair 1 (pair (pair 5 7) 4)))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 (pair (pair 5 6) 4)) (pair 1 (pair 5 4)))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 (pair (pair 5 6) 4)) (pair 1 (pair (pair 5 6) (pair 1 2))))") }
  assertResult (0) { runWithLib("(@ eq (pair 1 (pair 3 4)) (pair (pair 1 3) 4))") }
  assertResult (1) { runWithLib("(@ eq (pair 1 (pair 2 (pair 3 (pair 4 (pair 5 6))))) (pair 1 (pair 2 (pair 3 (pair 4 (pair 5 6))))))")
  }
  assertResult (0) { runWithLib("(@ eq (pair 1 (pair 2 (pair 3 (pair 4 (pair 5 8))))) (pair 1 (pair 2 (pair 3 (pair 4 (pair 5 6))))))")
  }
 }
}

