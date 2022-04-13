import java.util.*;

public class   Starting  {  

  public static void main (String[] argv){ 
        Starting  dr = new Starting();
        dr.run();

   } 

public void run( ) {

   Vehicle  unknown = new Vehicle();
   Vehicle  ferrari = new Car();
   Vehicle  penny = new Bike();

   Vehicle  myVehicle;  // to be initialized later

    unknown.start();
    ferrari.start();
    penny.start();

   for( int i = 0 ; i < 10 ; ++ i) {
       myVehicle = getVehicle();
       myVehicle.start(); // depends on how is it initialized. 
    } 

  }

public Vehicle getVehicle( ) {

// randomly return either a Car of a Bike. 

    Random rand = new Random();
    if ( 0 == rand.nextInt(2))
       return new Car();
    else  
       return new Bike();
  }


}
