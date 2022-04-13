package project1;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author Hermann
 * @since 1-10-2017 
 * DemoBigO.java
 *
 * Description: Methods to test Big0
 */
public class DemoBigO {

    public TreeMap<Integer, Integer> tree = new TreeMap();

    public boolean throwDivideException() throws ArithmeticException {
        double one = 1.0, two = 2.0, zero = 0.0;
        double result = one / two;
        /**
         * **********************************************************
         * Write the single line of code that will cause a Divide by Zero
         * Exception to be thrown
***********************************************************
         */
        result = one / zero;

        return true;
    }

    public void throwAnException() {
        /**
         * **********************************************************
         * Write the single line of code that will throw an ArithmeticException
         * and produce the given test output
***********************************************************
         */
        throw new ArithmeticException("This is an exception test");

    }


    /*
     * An obvious O(1)
     * divide n by 4 and assign the value to varible m
     */
    public void demoBigO1(long n) {
        double m = n / 4;
    }

    /*
     * A O( Log(n) ) method studied in class
     **** GIVEN
     */
    public void demoBigOLogN(long n) {
        int nInt = (int) n;
        //int[] dummy= new int[nInt];
        for (int i = 0; i < n; i++) {
            while (n > 1) {
                n = n / 2;
            }
        }
        return;
    }

    /*
     * An obvious O(n)
     * increase variable m by 1 n times
     */
    public void demoBigOn(long n) {
        long m = 0;
        for (long i = 0; i < n; i++) {
            m = +1;
        }
    }

    /*
    * Fill a tree of size n with random integers
    **** GIVEN
     */
    public void demoBigONLogNInit(long n) {
        int randomInt;
        long i = 0;

        //System.out.println("n =  "+n );
        Random r = new Random();
        while (i < n) {
            randomInt = (int) r.nextInt((int) ((long) 2 * n));
            //System.out.println("i= "+i+"  randomInt =  "+randomInt );
            tree.put(randomInt, randomInt);
            i++;
        }
    }

    /*
    * A traversal of a tree of size n that we will learn about in 302
     **** GIVEN
     */
    public void demoBigONLogN(long n) {
        int i;
        for (Map.Entry<Integer, Integer> entry : tree.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
        };
    }

    /*
    * An obvious O(n*n)
    * Assign the sum of i and j to variable m n*n times
     */
    public void demoBigOnn(long n) {
        long m = 0;
        for (long i = 0; i <= n; i++) {
            for (long j = 0; j <= n; j++) {
                m = i + j;
            }
        }
    }

}
