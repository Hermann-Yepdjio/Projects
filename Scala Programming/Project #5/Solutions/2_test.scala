//test: Test

// test of the solution
import org.scalatest.FunSuite
import scala.collection.mutable.MutableList
import scala.util.Random
import Solution._

object OriginalRef {

  // Control tracing of partition calls.
  // You should not modify this variable.
  var trace: Boolean = false

  // Partition the slice a(l)..a(h) using a(l) as the pivot
  // You should not modify this function.
  def partition(a:Array[Int], l:Int, h:Int) : Int = {
    // Trace the call
    if (trace)
      println("partition " + l + " " + h)

    // Swap a(k) and a(l) 
    def swap(k:Int,l:Int) = { val t = a(k); a(k) = a(l); a(l) = t }

    val x = a(l)
    var i = l - 1
    var j = h + 1
    while (true) {
      j = j - 1
      while (a(j) > x)
        j = j - 1
      i = i + 1
      while (a(i) < x)
        i = i + 1
      if (i < j)
        swap(i,j)
      else {
        return j
      }
    }

    // We can never get here, but the compiler still complains about a missing Int return 
    throw new Error("impossible!")
  }

  // Sort the array slice a(m)...a(n)
  def qs(a:Array[Int], m:Int, n:Int) : Unit = {

    // FIXME: Edit the remainder of this function body to remove recursion.
    if (m < n) {
      val j = partition(a,m,n)
      qs(a,m,j)
      qs(a,j+1,n)
    }
  }

  // Top-level sort of entire array.
  // You should not modify this function.
  def quicksort(a:Array[Int]) = qs(a,0,a.size-1)

  def runQuicksort(a:Array[Int], tracearg:Boolean = false) = { trace = tracearg; quicksort(a); a }
}



class Test extends FunSuite {
  
  val example_array = Array(10,32,567,-1,789,3,18,0,-51)
  
  val rng = Random
  def zeroArray(size:Int) : Array[Int] = new Array[Int](size)
  def expectDeep(expected:Array[Int])(actual:Array[Int]) = assert(actual sameElements expected) 
  
  def randomArray = randomArrayBounded(Int.MinValue/2, Int.MaxValue/2)_
  
  def randomArrayBounded(low:Int,hi:Int)(size:Int) : Array[Int] = {
    val r = new Array[Int](size)
    for(i <- 0 until r.length) {
      r(i) = low + (rng.nextInt % (hi - low))
    }
    r
  }
  
  def rangeArrayUp(size:Int) : Array[Int]   = (0 until size).toArray 
  def rangeArrayDown(size:Int) : Array[Int] = (size until 0).toArray 
  
  def runQuicksort(a:Array[Int],trace:Boolean = false) = { Solution.trace = trace; Solution.quicksort(a); a }

  def testArray(sizes:Range, arr: => (Int => Array[Int])) =
    for(i <- sizes) {
      val orig = arr(i)
      val sort = orig.clone
      expectDeep(OriginalRef.runQuicksort(orig)) { runQuicksort(sort) }
    }
  
  val SMALL = 4 until 100
  val LARGE = 100 until 500 by 50
  
  // SPECIAL CASES
  test("Quicksort works on an empty array") {
    val a = Array[Int]()
    expectDeep(Array[Int]()) { runQuicksort(a) }
  }
  
  test("Quicksort works on the example array") {
    testArray(0 until 1, Function.const(example_array))
  }

  test("Quicksort sorts 1 2 3") {
    testArray(0 until 1, Function.const(Array(1, 2, 3)))
  }

  test("Quicksort sorts 3 2 1") {
    testArray(0 until 1, Function.const(Array(3, 2, 1)))
  }

  test("Quicksort sorts 1 1 3") {
    testArray(0 until 1, Function.const(Array(1, 1, 3)))
  }

  test("Quicksort sorts 1 3 3") {
    testArray(0 until 1, Function.const(Array(1, 3, 3)))
  }
  
  test("Quicksort sorts 1 3 1") {
    testArray(0 until 1, Function.const(Array(1, 3, 1)))
  }

  test("Quicksort sorts 3 1 1") {
    testArray(0 until 1, Function.const(Array(3, 1, 1)))
  }
  
  test("Quicksort works on an array of size 2 of all zeros") {
    testArray(2 until 3, zeroArray)
  }

  test("Quicksort works on an array of size 3 of all zeros") {
    testArray(3 until 4, zeroArray)
  }
  
  // RANDOM
  test("Quicksort works on an array of size 1 of random inputs") {
    testArray(SMALL, Function.const(randomArray(1)))
  }

  test("Quicksort works on an array of size 2 of random inputs") {
    testArray(SMALL, Function.const(randomArray(2)))
  }
  
  
  test("Quicksort works on an array of size 3 of random inputs") {
    testArray(SMALL, Function.const(randomArray(3)))
  }


// ALL ZEROES


  test("Quicksort sorts the array (for small all-zero inputs)") {
    testArray(SMALL, zeroArray)
  }

  test("Quicksort sorts the array (for a few large all-zero inputs)") {
    testArray(LARGE, zeroArray)
  }
 
  // RANDOM
  test("Quicksort sorts the array (for many small random inputs)") {
    testArray(SMALL, randomArray)
  }


  test("Quicksort sorts the array (for a few large random inputs)") {
    testArray(LARGE, randomArray)
  }
  

  // ALREADY SORTED
  test("Quicksort sorts a small already-sorted array") {
    testArray(SMALL, rangeArrayUp)
  }

  test("Quicksort sorts a large already-sorted array)") {
    testArray(LARGE, rangeArrayUp)
  }

//  REVERSE SORTED
  test("Quicksort sorts a small reverse-sorted array") {
    testArray(SMALL, rangeArrayDown)
  }
  
  test("Quicksort sorts large reverse-sorted array)") {
    testArray(LARGE, rangeArrayDown)
  }

  
// RANDOM WITH LIKELY REPETITION
  test("Quicksort sorts the array (for small inputs with likely repetition)") {
    testArray(SMALL, randomArrayBounded(0,50))
  }
  
  test("Quicksort sorts the array (for a few large random inputs with likely repetition)") {
    testArray(LARGE, randomArrayBounded(0,100))
  }
  
// Assignment requirements

  test("Quicksort makes the same sequence of calls to partition as the original function") {
    def run(a: Array[Int]) = {

      def captureConsoleOutput(s: => Unit) = {
        val output = new java.io.ByteArrayOutputStream(128*1024)
        def testOutput = {s; output.flush(); output.toString() }
        Console.withOut(output)(testOutput)
      }

      val copy1 = a.clone
      val copy2 = a.clone
      
      // check that the sequence of calls is the same
      val log1 = captureConsoleOutput (OriginalRef.runQuicksort(copy1,true))
      val log2 = captureConsoleOutput (runQuicksort(copy2,true))
      assert (log1 == log2) 
    }

    val a1 = randomArrayBounded(0, 100)(200)
    val a2 = randomArrayBounded(0, 100)(500)
    val a3 = rangeArrayDown(200)
    val a4 = rangeArrayUp(500)
    run(a1)
    run(a2)
    run(a3)
    run(a4)
    
    ()

  }
  
  
}


