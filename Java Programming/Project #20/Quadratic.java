import java.util.*;
public class Quadratic {
/** All these method work with arrays  and are  O(n^2)
 *
 *  Notice that the arrays are sorting in place, that is,
 *  no additional large structure are used, and no return type
 *
 *   -- insertionSort 
 *   -- bubbleSort 
 *   -- median 
 *
*/

// Insertion Sort 
public static void insertionSort( double [] x ) {
  int n = x.length;
  for (int i = 1; i < n; i++)
            for (int k = i; k > 0 && x [k -1] > x [k]; k--)
        swap (x, k, k -1);
}

// Bubble Sort 
public static void bubbleSort (double[ ] x) {
   int n = x.length;
   int finalSwapPos = n - 1, swapPos;

   while (finalSwapPos > 0) {
      swapPos = 0;
      for (int i = 0; i < finalSwapPos; i++)
             if (x [i] > x [i + 1]) {
                         swap (x, i, i + 1);
                         swapPos = i;
               } 
               finalSwapPos = swapPos;
       } 
} 


// return  median
    public static double  median (double[] x) {
    int n = x.length;

    insertionSort (x);

    // if n is odd both indices quantities ar equal
    return  (x[n/2] + x [ (n-1)/2 ])/2.0 ; //

}

//  Auxiliary method
   private  static void swap (double [ ] x, int a, int b) {
      double t = x[a];
      x[a] = x[b];
      x[b] = t;
    } 

}
