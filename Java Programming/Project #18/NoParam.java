import java.util.*;
//  Not using parameters warns about members

public class NoParam {
	 public static void main (String[] argv){ 
         ArrayList  alist = new ArrayList();

//      These cause Warnings for the unchecked types
         alist.add( new Double (3.0));
         alist.add( new Integer (3));

//      but the program runs as expected 
         System.out.println( alist.get(0) );
         System.out.println( alist.get(1) );

	} 


}
