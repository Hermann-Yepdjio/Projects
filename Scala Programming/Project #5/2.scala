/*
Consider the Quicksort program in the solution template. Systematically remove the recursion from function quicksort following the general approach described in lecture for printtree. Do not change the partition function. Your modified version of quicksort must be non-recursive, and must make the same sequence of calls to partition, with the same arguments, as the original function does.

Hints: You may find it easier to develop your solution in C (or a pseudo-Scala with labels and gotos), so that you can use goto in the intermediate stages of your program transformation. Eventually, you’ll need to use only Scala’s structured control operators.

The unmodified template solution should already pass 24/24 tests. Your modified program should continue to pass all 24 tests. For grading purposes, not all of the tests in this assignment are weighted the same. Half of the assignment points are based on the percentage of tests that your modified program passes. The other half are given only if:

your quicksort implementation makes the same sequence of calls to partition as the original, unmodified implementation (this is among the tests)
your quicksort implementation is not recursive (this is manually graded by inspection)*/

import scala.collection.mutable.Stack
object Solution {
  
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
  def qs(a:Array[Int], m:Int, n:Int) : Unit = 
  {

    // FIXME: Edit the remainder of this function body to remove recursion.
    var s = Stack[(Int, Int)]()
    s.push((m, n))
    while(!s.isEmpty)
    {
        val top = s.pop
        if(top._1 < top._2)
        {
            val j = partition(a, top._1, top._2)
            s.push((j + 1, top._2))
            s.push((top._1, j))
        }
    }
    
  }

  // Top-level sort of entire array.
  // You should not modify this function.
  def quicksort(a:Array[Int]) = qs(a,0,a.size-1)
}


