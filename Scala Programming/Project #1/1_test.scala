//test: Test

/*
Good. This is the editor where you can write tests for your own code.
The test below is currently incorrect. Correct the expected value to the one
the Speak.greet() function actually returns, then click [Save] and then 
[Your test]. Your test should now pass.

Whenever you think your solution is complete you can [Spec-test].
This will run your solution against our tests. Go ahead. You can now see that
your code passed all tests. 

After [Spec-test] you can [Submit] your solution for us to review.
Go ahead and [Submit] your solution if all the tests have succeeded and we'll
see you at the next assignment.
*/

import org.scalatest.FunSuite

import Speak._

class Test extends FunSuite {

  test("greet returns proper greeting")
  {
    assertResult ("Hello world") { greet() }
  }

}

