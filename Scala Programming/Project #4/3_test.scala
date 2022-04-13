/*
This question uses a full implementation of the solution interpreter from question one (which is hidden in library code). Refer to question one to see the language features available in this toy language, including local variables and mutable pairs.

As noted in lecture, pairs can be used to encode lists within the language. The test template shows one simple way to do this, defining (ordinary) functions nil, cons, isnil, head, tail, sethead and settail in terms of the underlying pairs, together with some examples of their use.

Your task is to use this set of functions to implement and test the following list operations. You may find it easier (or just more fun!) to write your solutions in recursive functional style (like the append, countdown, or length examples) rather than in imperative style (like countupi and lengthi).

(a) (@ flatten l) takes a list of lists l and returns a fresh list containing the elements of l concatenated together. For example, (@ flatten (@ list3 (@ list2 1 2) (@ nil) (@ list1 3))) yields the list (1.(2.(3.0))). (Note that lists are printed out using the underlying pair representation since the interpreter doesn’t know they are intended to be lists.) The original list l must not be changed. You must not use setFst or setSnd operations (or any other function defined using those operations).

(b) (@ unzip l) takes a list l of pairs and returns a pair of lists, the first containing the first elements of l and the second containing the second elements of l. For example, (@ unzip (@ list3 (pair 1 2) (pair 3 4) (pair 5 6))) yields ((1.(3.(5.0))).((2.(4.(6.))))) Again, l must not be changed, and you must not use setFst or setSnd operations. Hint: let can be useful here.

(c) (@ nreverse l) should reverse list l in place, i.e., without constructing any new pairs, and return the resulting reversed list. After this call, the list pointed to by parameter l will be changed and typically no longer useful. For example, (let l (@ list3 1 2 3) (@ nreverse l)) yields (3.(2.(1.0))) and changes the list l. Hints: You’ll need to use settail but not sethead.

To test and submit these responses, save your function definitions as Scala in the solution template. We provide an example test harness in the test template, which you can use to test your solution. This harness makes it easy to see list-valued results.*/



//test: TestLists
import org.scalatest.FunSuite
import SExprLibrary._
import Process._

class TestLists extends FunSuite {
  
  // The interpreter doesn't allow the top-level program expression to return a list.
  // So we wrap the it to write out the result instead (and then return 0), and 
  // we capture the written output to use as the basis for our tests. 
  // Note that the output always has a final '\n' character.
  def wrapResult (s:String) = "(block (write " + s + ") 0)" 
  def runWithLog(s:String): String = {
    var result = 0
    val logger = new java.io.ByteArrayOutputStream(10240)
    Console.withOut(logger) { result = Process.process(s) }
    logger.flush()
    logger.toString() 
  }
  def runMyDefinitions(s: String) = runWithLog("(() (" + MyListDefinitions.definitions + ") " + wrapResult(s) + ")")

  test("Test some basic list definitions") {
    assertResult ("1\n") { runMyDefinitions("(@ not 0)") }
    assertResult ("0\n") { runMyDefinitions("(@ not 2)") }
    assertResult ("0\n") { runMyDefinitions("(@ length (@ nil))") }
    assertResult ("3\n") { runMyDefinitions("(@ length (@ list3 1 2 3))") }
    assertResult ("(1.(2.(3.0)))\n") { runMyDefinitions("(@ countup 3)") }
    assertResult ("10\n") { runMyDefinitions("(@ length (@ countup 10))") }
    assertResult ("55\n") { runMyDefinitions("(@ sum (@ countup 10))") }
    assertResult ("(1.(2.0))\n") { runMyDefinitions("(@ list2 1 2)") }
    assertResult ("(1.(2.(3.(4.0))))\n") { runMyDefinitions("(@ append (@ list2 1 2) (@ list2 3 4))") }
    assertResult ("((1.(2.0)).(0.((3.0).0)))\n") { runMyDefinitions("(@ list3 (@ list2 1 2) (@ nil) (@ list1 3))") }
    assertResult ("10\n") { runMyDefinitions("(@ lengthi (@ countup 10))") }
    assertResult ("55\n") { runMyDefinitions("(@ sum (@ countupi 10))") }
    assertResult ("(3.(2.(1.0)))\n") { runMyDefinitions("(@ countdown 3)") }
    assertResult ("20\n") { runMyDefinitions("(@ lengthi (let x (@ countdown 10) (@ append x x)))") }
  }

  
  test("flatten example") {
    assertResult("(1.(2.(3.0)))\n") { runMyDefinitions("(@ flatten (@ list3 (@ list2 1 2) (@ nil) (@ list1 3)))") }
  }

  test("unzip example") {
    assertResult("((1.(3.(5.0))).(2.(4.(6.0))))\n") { runMyDefinitions("(@ unzip (@ list3 (pair 1 2) (pair 3 4) (pair 5 6)))") }
  }

  test("nreverse example") {
    assertResult("(3.(2.(1.0)))\n") { runMyDefinitions("(let l (@ list3 1 2 3) (@ nreverse l))") }
  }

}


