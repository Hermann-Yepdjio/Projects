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


// SOLUTION (and how to get there):
//
// Here is the original recursive function:
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   if (m < n) {
//     val j = partition(a,m,n)
//     qs(a,m,j)
//     qs(a,j+1,n)
//   }
// }
//
// ------------------
//
// First, we remove tail recursion. 
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
// top:
//   if (m < n) {
//     val j = partition(a,m,n)
//     sort (n,m,j)
//     m = j+1
//     goto top;
//   }
// }
//
// ------------------
//
// Next, simulate the calls and returns using an explicit stack.
// There is only one return point, so all we need to record on the stack
// are the values of local variables that might change over the "call" and will
// be needed after the "return," namely n and j.  It is clearest to make
// a stack of pairs of Ints, although a single stack of Ints would also work fine
// (provided we remember the order in which the two integers are pushed and
// reverse that order when popping).
// By the way, the Stack class from the Scala library is deprecated in recent
// versions of the language, but using it makes the stack-related behavior very clear.
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   val stack : Stack[(Int,Int)] = Stack()  
// top:
//   if (m0 < n0) {
//     val j = partition(a,m0,n0)
//     stack.push (j,n0)
//     n0 = j
//     goto top
// retaddr:
//     m0 = j+1
//     goto top    
//   }
//   if (stack.nonEmpty) {
//     (j,n0) = stack.pop
//     goto retaddr
//   } 
// }
//
// ---------------
//
// Rearranging a bit to get rid of the retaddr label and goto:
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   val stack : Stack[(Int,Int)] = Stack()  
// top:
//   if (m0 < n0) {
//     val j = partition(a,m0,n0)
//     stack.push (j,n0)
//     n0 = j
//     goto top
//   }
//   if (stack.nonEmpty) {
//     (j,n0) = stack.pop
//     m0 = j+1
//     goto top    
//   } 
// }
//
//
// -----------------
//
// Introducing a while loop to get rid of the top label and goto.
// Also, to make this valid Scala, we need to make mutable versions of m and n
// since all function parameters are implicitly values (not vars), and
// we need to unpack the popped pair in two steps.
//
// This is now valid Scala, which will compile and pass all the tests, so
// if you got this far, you were done.

  import collection.mutable.Stack

  // Sort the array slice a(m)...a(n)
  def qs(a:Array[Int], m:Int, n:Int) : Unit = {
    val stack : Stack[(Int,Int)] = Stack()
    var m0 = m
    var n0 = n
    var j = 0
    while (true) {
      if (m0 < n0) {
        j = partition(a,m0,n0)
        stack.push((j,n0))
        n0 = j
      } else if (stack.nonEmpty) {
        val p = stack.pop()
        j = p._1; n0 = p._2
        m0 = j+1
      } else
        return
    }
  }

//
// -----------------------
// 
// (And for completeness, here's the same version using a stack of single integers
//  rather than pairs.)
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   val stack : Stack[Int] = Stack()
//   var m0 = m
//   var n0 = n
//   var j = 0
//   while (true) {
//     if (m0 < n0) {
//       j = partition(a,m0,n0)
//       stack.push(j)
//       stack.push(n0)
//       n0 = j
//     } else if (stack.nonEmpty) {
//       n0 = stack.pop()
//       j = stack.pop()
//       m0 = j+1
//     } else
//       return
//   }
// }
//
//------------
//
// Returning to the stack-of-pairs version, we can go further, 
// to get a nicer looking program.
// First, we introduce a nested while loop.
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   val stack : Stack[(Int,Int)] = Stack()
//   var m0 = m
//   var n0 = n
//   var j = 0
//   while (true) {
//     while (m0 < n0) {
//       j = partition(a,m0,n0)
//       stack.push((j,n0))
//       n0 = j
//     }
//     if (stack.nonEmpty) {
//       val p = stack.pop()
//       j = p._1; n0 = p._2
//       m0 = j+1
//     } else
//       return
//   }
// }
//
// ---------------
//
// We can tidy up the outer while loop by initializing the
// stack appropriately. At the same time, we can revert to using the
// original variable names.
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   val stack = Stack((m,n))
//   while (stack.nonEmpty) {
//     var (m,n) = stack.pop()
//     while (m < n) {
//       val j = partition(a,m,n)
//       stack.push((j+1,n))
//       n = j                                   
//     }
//   }
// }
//
//
// ---------------
//
// Finally, we can choose to treat the two sub-problems symmetrically,
// at the expense of more stack operations.
// This version clearly shows how sub-problems are generated and processed.
//
// def qs(a:Array[Int], m:Int, n:Int) : Unit = {
//   val stack = Stack((m,n))
//   while (stack.nonEmpty) {
//     var (m,n) = stack.pop()
//     if (m < n) {
//       val j = partition(a,m,n)
//       stack.push((j+1,n))
//       stack.push((m,j))
//     }
//   }
// }
//

  // Top-level sort of entire array.
  // You should not modify this function.
  def quicksort(a:Array[Int]) = qs(a,0,a.size-1)
}

