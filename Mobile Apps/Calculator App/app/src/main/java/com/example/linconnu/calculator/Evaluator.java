package com.example.linconnu.calculator;

/**
 * Created by linconnu on 25/10/17.
 */

public class Evaluator
{


    public static double eval(final String str)
    {

        return new Object()
        {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    //checks the validity of an arithmetic expression provided as a string
    public static boolean isValid(String exp)
    {
        int counter1 =0, counter2=0;
        for(int i=0; i<exp.length(); i++)
        {
            if(counter2>counter1)
                return false;
            if (exp.charAt(i) == '+' || exp.charAt(i) == '-' || exp.charAt(i) == '*' || exp.charAt(i) == '/' || exp.charAt(i) == '^') //checks if two operators are following each other
            {
                if(i<exp.length()-1 && (exp.charAt(i+1) == '+' || exp.charAt(i+1) == '-' || exp.charAt(i+1) == '*'
                                                             || exp.charAt(i+1) == '/' || exp.charAt(i+1) == '^'))
                    return false;
            }
            if(i==exp.length()-1 && (exp.charAt(i) == '+' || exp.charAt(i) == '-' || exp.charAt(i) == '*' //checks if expression ends with an operator
                    || exp.charAt(i) == '/' || exp.charAt(i) == '^'))
                return false;
            if(exp.charAt(i) == '(')
            {
                if(i<exp.length()-1 && (exp.charAt(i+1)=='*' || exp.charAt(i+1)=='/'||exp.charAt(i+1)=='^' || exp.charAt(i+1)==')')) //checks if first element in the paranthes is * or / or ^
                    return false;
                counter1 += 1;
            }
            if(exp.charAt(i) == ')') //check if last element in parantheses is a * or / or ^
            {
                if(i>0 && (exp.charAt(i-1) == '+' || exp.charAt(i-1) == '-'
                                                  || exp.charAt(i-1) == '*' || exp.charAt(i-1) == '/' || exp.charAt(i-1) == '^'))
                    return false;
                counter2 += 1;
            }
            if(i==0 && (exp.charAt(i)=='*' || exp.charAt(i)=='/'||exp.charAt(i)=='^')) //hecks if expression starts with a * or / or ^
                return false;
        }
        if(counter1 != counter2) //check if the number of left and right parantheses matches
            return false;

        return true;
    }

}
