import java.util.*;
//  Derive class. Note the use of super()  to initialize the base class.

public class    Account {  
 private double balance;
   public  Account( double b) {
      balance=b;
     }
 } 


class  SavAccount extends Account  {  
private double rate;
 SavAccount(double rt, double bal){
// an explicit  constructor  is needed, as super does not have a default
       super(bal);
       rate=rt;
    }
}
