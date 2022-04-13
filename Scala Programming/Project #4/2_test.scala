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
 
}

