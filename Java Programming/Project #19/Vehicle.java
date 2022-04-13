import java.util.*;
/**
 *  A class is defined with twor derive classes that override the
 *  start() method
*/

public class    Vehicle {  

  public void start() {
    System.out.println( "Dont know how" );
    }

   public void accelerate () {
    }

   public void stop() {
    }

 } 


class   Bike  extends  Vehicle  {  
   public  void start() {
    System.out.println( "Step hard on the pedal" );
    }

  }

class   Car  extends  Vehicle  {  
    public  void start() {
    System.out.println( "With key, turn ignition on");
    }

}
