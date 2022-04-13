//   This shows the implicit reference of an inner class to 
// outer members.
public class    InClass {  
private   int x,y;

   public InClass() {
        x = 10;
        y = 6;

// inner object
        In  innerob = new In();

      }

// outer  method
   private void reset( )  {   
     x =0;
     y =0;
    }

// inner class 
   class In {
      private int z;
      public In() {
         z = 200; 
// Either statement refers to outer members, in spite of being private.
         reset();
         x = 100;
         }
    }

  public static void main (String[] argv){ 
     InClass val= new InClass();
     System.out.println( val.toString());
   } 

// outer  method
 public  String toString( ) {
  return x+" "+y;
 } 

}
