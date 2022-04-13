//test: Test

// test of the example
import org.scalatest.FunSuite

class Test extends FunSuite {
  
  test("test of the example" ) {
    assert (BrokenVersion.Process.process(Example.example) != WorkingVersion.Process.process(Example.example))
  }
}


