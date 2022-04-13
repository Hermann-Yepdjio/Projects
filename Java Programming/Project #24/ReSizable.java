import java.util.*;
public class    ReSizable{  
private static final int  SIZE=4;

      public static void main (String[] argv){ 
          int j;
          double []  darray = new double[4];
          Random r = new Random();

/*
         try {
           j = 0;
           while  ( j <= 100) {
             darray[j] = r.nextDouble();
             ++j;
            }
          }
        catch ( RuntimeException k) {
            System.out.println("Index out of bounds");
            System.exit(-1);
         } 
 */         
         try {
           j = 0;
           while  ( j <= 100) {
             if  (j == darray.length) 
                    darray = resize(darray);
             darray[j] = r.nextDouble();
             ++j;
            }
          }
        catch ( RuntimeException k) {
            System.out.println("Index out of bounds");
            System.exit(-1);
         } 
          
        System.out.println("The new array length is :"+darray.length); 
        display(darray);

	} 

   
   private static void display(double [] a1) { 
     for( int i=0 ; i < a1.length; ++i)
        System.out.print(a1[i]+" "); 

   }

   private static double [] resize (double [] a1) { 
     int oldSize = a1.length;
     double [] a2 = new double[2 * oldSize];

     for( int i=0 ; i < oldSize ; ++i)
         a2[i] = a1[i];
    return a2;
   } 

}
