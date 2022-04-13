import java.util.*;

public class BinarySearchUser
{
  public static void main (String[ ] args) 
  {
    new BinarySearchUser ().run();
  } // method main


  public void run()
  {
          final String ARRAY_MESSAGE =
               "The array on which binary searches will be performed is:\n" +
               "Ada, Ben, Carol, Dave, Ed, Frank, Gerri, Helen, Iggy, Joan";

          final String SENTINEL = "***";

          final String INPUT_PROMPT =
               "\n\nPlease enter a name to be searched for in the array (or " +
               SENTINEL + " to quit): ";

          final String[ ] names = {"Ada", "Ben", "Carol", "Dave", "Ed", "Frank",
                   "Gerri", "Helen", "Iggy", "Joan"};

          final String FOUND = "That name was found at index ";

          final String NOT_FOUND = "That name was not found, but could be " +
                  "inserted at index ";

          String name;

          Scanner sc = new Scanner (System.in);

          int index;

          System.out.println (ARRAY_MESSAGE);
          while (true)
          {       
               System.out.print (INPUT_PROMPT);
               name = sc.next();
               if (name.equals(SENTINEL))
                   break;
               index = binarySearch (names, 0, names.length - 1, name);
               if (index >= 0)
                   System.out.println (FOUND + index);
               else
                   System.out.println (NOT_FOUND + (-index - 1)); 
          } // while
  } // method run


  public static  int binarySearch(Object[ ] a, int first, int last, Object key) 
  {
        if (first <= last) 
        {
            int mid = (first + last) >> 1;
            Comparable midVal = (Comparable)a [mid];
            int comp = midVal.compareTo (key);
            if (comp < 0)
                 return binarySearch (a, mid + 1, last, key);
            if (comp > 0)
                 return binarySearch (a, first, mid - 1, key);
            return mid;  // key found
        } // if first <= last
        return -first - 1; // key not found; belongs at a[first]
  } // method binarySearch

} // class BinarySearchUser
