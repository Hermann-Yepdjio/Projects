import java.util.*;
public class MatrixOperations {
/**  
 *
 *   Methods  for square matrices ( n x n)
 *
 *        transpose  is O(n^2)   
 *        squares  is  O(n^3)
 *
 *   addition, or multiplication   may be used as units.
 *    
*/



    // returns B = A^T
    public static double[][] transpose(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (m != n ) throw new RuntimeException("Matrix must be square.");
        double[][] b = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                b[j][i] = a[i][j];
        return b;
    }


    // returns C = A^2 = A A
    public static double[][] square(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (m != n ) throw new RuntimeException("Matrix must be square.");
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++)
                    c[i][j] += a[i][k] * a[k][j];
        return c;
    }


}
