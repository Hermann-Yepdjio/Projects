/**  Illustrates how the try-catch block prevent us getting 
     an Exception message at run time, for bad input.
     
     NumberFormatException is a RunTimeException and thus is unchecked. 
     Notice difference in the way the exception is dealt.

     

*/

public class Exc1{
  public static void main(String[] args) {

// try  to enter  some characters here
    InOut.prompt("Enter Number: ");
    String userInput ;
    userInput = InOut.readLine();
    double n1 = Double.parseDouble(userInput);   // no catching 

// try  to enter  some characters here
    InOut.prompt("Enter Number: ");
    userInput = InOut.readLine();
    double n2 = Convert.toDouble(userInput);  // catching
    System.out.println("The Ratio is : " + n1/n2);
  }
}
