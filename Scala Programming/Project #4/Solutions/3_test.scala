//test: TestLists
import org.scalatest.FunSuite
import SExprLibrary._
import Process._
import scala.util.Random

class TestLists extends FunSuite {
  

  // suitable for running a program that writes output but doesn't produce a useful result
  def runWithLog(s:String, allowSetOps:Boolean=true): String = {
    var result = 0
    val logger = new java.io.ByteArrayOutputStream(10240)
    Console.withOut(logger) { result = Process.process(s,allowSetOps=allowSetOps) }
    logger.flush()
    logger.toString() 
  }


  def wrapResult (s:String) = "(block (write " + s + ") 0)" 
  def useMyDefinitions(s: String) = "(() (" + MyListDefinitions.definitions + ") " + wrapResult(s) + ")"
  def runMyDefinitions(s: String) = runWithLog(useMyDefinitions(s))
  def runMyDefinitionsWithSetOpsBanned(s:String) = runWithLog(useMyDefinitions(s), allowSetOps = false)


  test("check that the set-op detecting machinery works") {
    intercept[InterpException] { runMyDefinitionsWithSetOpsBanned("(let x (pair 1 2) (setFst x 10))") }
    intercept[InterpException] { runMyDefinitionsWithSetOpsBanned("(let x (pair 1 2) (setSnd x 20))") }
  }
  // Real tests

  test("Definitions parse and interpretable with trivial body") {
    assertResult ("0\n") { runMyDefinitions("0") }
  }

  test("flatten example") {
    assertResult("(1.(2.(3.0)))\n") { runMyDefinitions("(@ flatten (@ list3 (@ list2 1 2) (@ nil) (@ list1 3)))") }
  }

  test("flatten of empty list is equal to empty list") {
    assertResult("0\n") { runMyDefinitions("(@ flatten (@ nil))") }
  }
 
  test("flatten several empty lists") {
    assertResult("0\n") { runMyDefinitions("(@ flatten (@ list3 (@ nil) (@ nil) (@ nil)))") }
  }

  test("flatten several empty lists and a non-empty one") {
    assertResult("(42.0)\n") { runMyDefinitions("(@ flatten (@ list3 (@ nil) (@ nil) (@ list1 42)))") }
  }

  test("flatten single singleton list") {
    assertResult("(42.0)\n") { runMyDefinitions("(@ flatten (@ list1 (@ list1 42)))") }
  }

  test("flatten singleton longer list") {
    assertResult("(1.(2.(3.0)))\n") { runMyDefinitions("(@ flatten (@ list1 (@ list3 1 2 3)))") }
  }

  test("flatten longer singleton lists") {
    assertResult("(1.(2.(3.0)))\n") { runMyDefinitions("(@ flatten (@ list3 (@ list1 1) (@ list1 2) (@ list1 3)))") }
  }

  test("flatten longer longer lists") {
    assertResult("(1.(2.(3.(4.(5.(6.(7.(8.(9.0)))))))))\n") {
      runMyDefinitions("(@ flatten (@ list3 (@ list3 1 2 3) (@ list3 4 5 6) (@ list3 7 8 9)))") }
  }

 test("flatten does not use `set`") {
   assertResult("(1.(2.(4.(5.(6.0)))))\n") { runMyDefinitionsWithSetOpsBanned("(@ flatten (@ list2 (@ list2 1 2) (@ list3 4 5 6)))") }
 }

  test("unzip example") {
    assertResult("((1.(3.(5.0))).(2.(4.(6.0))))\n") { runMyDefinitions("(@ unzip (@ list3 (pair 1 2) (pair 3 4) (pair 5 6)))") }
  }


  test("unzip empty list") {
    assertResult("(0.0)\n") { runMyDefinitions("(@ unzip (@ nil))") }
  }

  test("unzip singleton list") {
    assertResult("((1.0).(2.0))\n") { runMyDefinitions("(@ unzip (@ list1 (pair 1 2)))") }
  }

  test("unzip zero confusion") {
    assertResult("((0.(0.0)).(0.(0.0)))\n") { runMyDefinitions("(@ unzip (@ list2 (pair 0 0) (pair 0 0)))") }
  }


  test("unzip does not use `set`") {
    assertResult("((1.(3.0)).(2.(4.0)))\n") { runMyDefinitionsWithSetOpsBanned(
                              "(@ unzip (@ cons (pair 1 2) (@ cons (pair 3 4) (@ nil))))") } 
  }

  test("nreverse example") {
    assertResult("(3.(2.(1.0)))\n") { runMyDefinitions("(let l (@ list3 1 2 3) (@ nreverse l))") }
  }

  test("nreverse empty list") {
    assertResult("0\n") { runMyDefinitions("(@ nreverse (@ nil))") }
  }

  test("nreverse a singleton list") {
   assertResult("(1.0)\n") { runMyDefinitions("(@ nreverse (@ cons 1 (@ nil)))") }
  }

  test("nreverse a length 2 list") {
   assertResult("(2.(1.0))\n") { runMyDefinitions("(@ nreverse (@ cons 1 (@ cons 2 (@ nil))))") }
  }

  test("nreverse a longer list") {
    assertResult("(7.(6.(5.(4.(3.(2.(1.0)))))))\n") {
      runMyDefinitions("(@ nreverse (@ cons 1 (@ cons 2 (@ cons 3 (@ cons 4 (@ cons 5 (@ cons 6 (@ cons 7 (@ nil)))))))))") }
  }

}
    

