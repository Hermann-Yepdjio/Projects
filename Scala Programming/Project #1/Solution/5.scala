/*Writing software can be frustrating because errors can come up anywhere. It’s not because the assignments are hard, it’s just that we forget what the behavior should be in special cases. To make our collective lives easier (and programming less frustrating) we write tests. There are different types of tests:

Unit tests
Integration tests
By ‘unit’ we mean the smallest testable piece of functionality. You can imagine each unit as a building block in the software. With unit testing we test each building block independently against a variety of inputs. For example if we were making a pocket calculator program we would have units tests that exercised addition, subtraction, multiplication, and division separately.

Let’s say we want to write a function that returns the remainder after division of two integers. We’ve made a skeleton for this the “Solution” editor. Before you start implementing it, just go to the “Test” editor and write some tests for our Calc.rem(Int,Int) method. Look through the example there. The first test there reads: “When I call rem(2,2) I expect the result to be 1”. The second test checks the same thing in a more complicated way: it illustrates that we can test for arbitrary Boolean conditions. The third test reads: “When I call rem(2,0) I expect an ArithmeticException to be thrown”.

Your assignment is to write tests for normal and special cases and create an implementation of Calc.rem(Int,Int) that passes those tests. You’re done when all of our Specification tests pass. Don’t forget to [Submit] your solution when you are done.*/

// the solution 

object Calc { 

  def rem(div : Int, by: Int) : Int = div % by
  
}

