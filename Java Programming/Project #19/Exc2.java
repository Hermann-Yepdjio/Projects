//  This  program shows that  floating division by 0.0 
//  does not throw  an exception.
//
public class Exc2{

  public static void main(String[] args) {

    double n1,n2, res, res2; 

// input buffer
    String userInput ;

    System.out.print("Enter Number 1: \n");
    userInput = InOut.readLine();
    n1 = Double.parseDouble(userInput);


    System.out.print("Enter Number 2: \n");
    userInput = InOut.readLine();
    n2 = Double.parseDouble(userInput);


    res = n1/n2;

// Try dividing floating by 0.0. Java does not throw an exceptions
// when producing IEEE 754 Infinite or NaN.  A special trick is needed.
//
    
    System.out.println("The Ratio is : " + res);

    System.out.println("We are happy with res: " + res);
    System.out.println("Multiply into  res " );
    res2 = -2 * res;
    res = res + res2; 
    System.out.println("Now res is  " + res);
    res /= 3.14;
    System.out.println("Now res is  " + res);

  }
}
