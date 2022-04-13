//  Shows the use of try-catch to  make sure input is correct
//  avoiding  integer division by 0. 
//  This avoids the ocurrence of the excection. 

import java.io.*;
public class Exc4{

// input buffer;
   static String userInput ;

// input method
  private static void promptRead() {
   InOut.prompt("Please Enter integer: ");
   userInput = InOut.readLine();
   return;
  }


  public static void main(String[] args) {

// note that variables  assigned inside loop must be initialized
    int n1, n2 , n3=1;   
    boolean again=true;    // loop control

    promptRead();
    n1 = Integer.parseInt(userInput);

    while (again) { 
      again =false;
      promptRead();
      n2 = Integer.parseInt(userInput);
      try {   
       n3= n1/n2;
        }
      catch (ArithmeticException e) {
       System.out.println(" Possible Division by zero.  ");
       again= true;
        }
     }

   System.out.println("The Integer division  is : " + n3);
  }
}
