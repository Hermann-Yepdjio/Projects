//  Shows how Java throws an arithmetic exception when 
//  doing integer division by 0. Contrast this with the 
//  floating point cases 

public class Exc3{

// input buffer;
  static String userInput ;

// input method
  private static void promptRead() {
   InOut.prompt("Please Enter integer: ");
   userInput = InOut.readLine();
   return;
  }


  public static void main(String[] args) {
    int n1, n2 ;   

    promptRead();
    n1 = Integer.parseInt(userInput);
     
    promptRead();
    n2 = Integer.parseInt(userInput);

    System.out.println("The Integer division  is : " + n1/n2);
  }
}
