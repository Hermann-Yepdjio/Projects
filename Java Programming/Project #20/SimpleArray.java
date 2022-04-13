import java.util.*;

public class    SimpleArray  {  
/**  
 *
 *   The methods  
 *
 *        sumArray   - maxArray  - secondMaxArray 
 *
 *   are all have  O(n) worst running time, with n the size of the array. 
 *   addition, comparisons or assigment may be used as units.
 *    
*/

public static  double  sumArray   ( double []  x ) {

  int n = x.length;

  double sum=0;

  for ( int i = 0; i < n ; ++i )
      sum +=  x[i] ;

   return sum;
}

public static double   maxArray    ( double []  x ) {

  int n = x.length;

   double max= x[0];

   for ( int i = 1; i < n ; ++i )
       if  ( x[i] > max )
          max = x[i]   ;

   return  max;

}

public static  double secondMax    ( double []  x ) {

     int n = x.length;
     double first, second;

     first = second = x[0];

     for ( int i = 1; i < n ; ++i )
         if (  x[i] > first) {
               second = first; 
               first = x[i];
               }
         else if (x[i] > second)
                second = x[i];

    return second;

  }

 } 
