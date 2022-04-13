//test: TestBooleanExprs

import org.scalatest.FunSuite
import SExprLibrary._
import Parser._

class TestBooleanExprs extends FunSuite {
  
  // test parser

  test ("parse T") {
    assertResult (Bool(true)) {parse("T")}
  }

  test ("parse F") {
    assertResult (Bool(false)) {parse("F")}
  }

  test ("parse not") {
    assertResult (Not(Bool(true))) {parse("(~ T)")}
  }

  test("parse and") {
    assertResult(And(Bool(true), Bool(false))) {parse("(& T F)")}
  }
  
  test("parse xor") {
    assertResult(Xor(Bool(false),Bool(true))) {parse("(^ F T)")}
  }

  test("parse or") {
    assertResult(Or(Bool(false),Bool(true))) {parse("(| F T)")}
  }

  test("parse imp") {
    assertResult(Imp(Bool(false),Bool(true))) {parse("(-> F T)")}
  }

  test("parse complex expression") {
    assertResult(Xor(And(Bool(true),Bool(false)),Not(And(Bool(false),Xor(Bool(true),Bool(true)))))) {parse("(^ (& T F) (~ (& F (^ T T))))")}
  }
  
  // ParseException for string consisting of invalid arguments (3 args, 2 args, 1 arg, no args, empty)
  test("parse exception for & with 3 arguments") {
    intercept[ParseException] {
      (parse("(& T F F)"))
    }
  }
  
  test("parse exception for ~ with 2 argument") {
    intercept[ParseException] {
      (parse("(~ T F)"))
    }
  }

  test("parse exception for & with 1 argument") {
    intercept[ParseException] {
      (parse("(& T)"))
    }
  }
  
  test("parse exception for & with no argument") {
    intercept[ParseException] {
      (parse("(&)"))
    }
  }
  
  test("parse exception for empty s-expression") {
    intercept[ParseException] {
      (parse("()"))
    }
  }
	
  // test pretty printer

  test ("print T") {
    assertResult ("T") {Bool(true).toString}
  }

  test ("print F") {
    assertResult ("F") {Bool(false).toString}
  }

  test ("print not") {
    assertResult ("(~ T)") {Not(Bool(true)).toString}
  }

  test("print and") {
    assertResult ("(& T F)") {And(Bool(true), Bool(false)).toString}
  }

  test("print xor") {
    assertResult ("(^ F T)") {Xor(Bool(false),Bool(true)).toString}
  }

test("print or") {
    assertResult ("(| F T)") {Or(Bool(false),Bool(true)).toString}
  }

  test("print imp") {
    assertResult ("(-> F T)") {Imp(Bool(false),Bool(true)).toString}
  }

  test("print complex expression") {
    assertResult ("(^ (& T F) (~ (& F (^ T T))))") {Xor(And(Bool(true),Bool(false)),Not(And(Bool(false),Xor(Bool(true),Bool(true))))).toString} 
  }


  // test reader, parser and pretty printer working together
  test("parse(print(parse(s)))) {parse(s)) holds for a complex s") {
    val expr = parse("(^ (& (& T F) (~ F)) (~ (& F F)))")
    assert(parse(expr.toString) == expr)
  }

  // test interpreter
  test ("correctly interpret true") {
    assertResult (true) {Process.process("T")}
  }

  test ("correctly interpret false") {
    assertResult (false) {Process.process("F")}
  }

  test ("correctly interpret not") {
    assertResult (true) {Process.process("(~ F)")}
    assertResult (false) {Process.process("(~ T)")}
  }

  test ("correctly interpret and") {
    assertResult (true) {Process.process("(& T T)")}
    assertResult (false) {Process.process("(& T F)")}
    assertResult (false) {Process.process("(& F T)")}
    assertResult (false) {Process.process("(& F F)")}
  }

  test ("correctly interpret xor") {
    assertResult (false) {Process.process("(^ T T)")}
    assertResult (true) {Process.process("(^ T F)")}
    assertResult (true) {Process.process("(^ F T)")}
    assertResult (false) {Process.process("(^ F F)")}
  }

    test ("correctly interpret or") {
    assertResult (true) {Process.process("(| T T)")}
    assertResult (true) {Process.process("(| T F)")}
    assertResult (true) {Process.process("(| F T)")}
    assertResult (false) {Process.process("(| F F)")}
  }

  test ("correctly interpret imp") {
    assertResult (false) {Process.process("(-> T F)")}
    assertResult (true) {Process.process("(-> T T)")}
    assertResult (true) {Process.process("(-> F F)")}
    assertResult (true) {Process.process("(-> F T)")}
  }

  
  test("correctly interpret a complex expression") {
    assertResult(false) {Process.process("(& (& (~ (^ T F)) F) (~ F))")}
  }
	
}





