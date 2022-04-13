  object Example {
      
// The problem that leads to incorrect results is the ability to have a reference 
// to a stack-allocated variable that outlives the stack frame in which the variable 
// was allocated.

// This language, in combination with the incomplete implementation, 
// gives us an easy way to let a stack reference "escape" in this manner 
// by building a closure containing a local variable, so that the function 
// body will need to refer to the local variable when it is eventually called. 
// If we can build a closure, then call it in a scope other than the one in 
// which it was defined, the stack location corresponding to the variables in 
// the closure environment won't be correct. That's why, in part (b), we need 
// to improve the implementation to copy variables that might be used in
// closures into the heap.

// The trick to "escape" is by returning a function. For example, in the following 
// program we introduce the local `z` and the parameter `x` here, which lines 
// up the stack so that `f` returns an incorrect integer result, 1.

  val example = """
(let f 
    (let z 
        0 
        (let y 
            37 
            (fun (x) y)))
    (@ f 1)
) 
"""

// This program should produce the following result: 37

// There are many variations that will similarly obtain incorrect results.
// For example, the following program ought to return 1 (since `(@ f 1)` 
// should be a function which accepts any argument and then returns 1) 
// but instead returns 2.

// (let f 
//    (fun (x)
//        (fun (y) x))
//    (@ (@ f 1) 2)
// ) 

// Similarly, it is possible to get an `InterpException` instead of an 
// integer result in the template, when the solution would have produced a result. 
// (Giving a program with this behavior is not worth full credit as a response 
// to this problem, but it's worth considering). 
// The example on slide 4 of lecture 6b behaves like this.  Another one is below:
// it is very similar to the first example,  but it doesn't declare the unused variable `z`. 
// As a result, the stack-based implementation doesn't place this value on the 
// stack which means that when we look for the value of `y` on the stack, 
// instead of finding 37, or the replacement value 1, we actually find 
// the closure value representing `f`! So `f` applied to an argument returns 
// a reference to _itself_, and the overall value of the expression 
// is the closure for `f`. This results in an InterpException because 
// the overall program is supposed to evaluate to a number, not a function.

// (let f 
//        (let y 
//            37 
//            (fun (x) y))
//    (@ f 1)
// ) 

}

