//test: Test

/*
We expect the first line in every test editor to be of the form
//test: <ClassName> 

where <ClassName> is the name of the test class. Look now to the first line
of this editor. Notice that the name there is not the same as the class name
in this file, on line 25. In the case of the current test suite the name
specified on the first line should be Test. So go ahead and correct this. When
you're done the first line of this file should look like:

//test: Test

Good.This line is quite fragile so don't add any more spaces or it will break. 
Go ahead and fix this first line. After your tests pass, make 
sure that all the specification tests succeed and then [Submit] 
your solution.
*/

import org.scalatest.FunSuite

import Solution._

class Test extends FunSuite {

  test ("f returns 1") {
    assertResult (1) { f(0) }
	 }

}




