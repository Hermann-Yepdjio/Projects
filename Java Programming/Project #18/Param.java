import java.util.*;
//   Among other advantages including the use of parameter (class name)
//   parametrization allows compile time type checking.

public class Param {
	 public static void main (String[] argv){ 

//     The ArrayList class is provided with Type parameter
         ArrayList<Double>  alist = new ArrayList<Double>();

//     These compile  without problem
         alist.add( new Double (3.0));
         alist.add( new Double (5.0));

//    but type checking causes compilation error.
         alist.add( new Integer (3));

         System.out.println( alist.get(0) );
         System.out.println( alist.get(1) );

	} 


}
