import java.util.*; 
import java.lang.*; 

public class    ExofIt {  

static ArrayList<String>  aa = new ArrayList<String>();

  public static void main (String[] argv){ 
       String str;
       int len;
        aa.add(" This ");
        aa.add(" is ");
        aa.add(" Data ");
        aa.add(" Structures ");

       len = aa.size();
 
//  The reason we can get away with the following loop
//  is that get(int x) is a method in ArrayList<E>
// 
//  get(int x) is not part of the Collection interface!

      for (int i=0; i < len; ++i)
         System.out.print( aa.get(i) );

      System.out.println( );


//  Here is a more general way to do the same traversal.
//  Since ArrayList is a collection, we have the iterator() method.
//
       Iterator<String> it =  aa.iterator();
       while ( it.hasNext() )  {
             str = it.next();
             System.out.print( str );
        }
        System.out.println(); 

//  or even using the enhanced loop form
       for( String  w : aa)
             System.out.print( w );
        System.out.println(); 

	} 
 } 
