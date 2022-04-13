import java.util.*;

public class DecimalToBinaryUser
{
  public static void main (String[] args)
  {
    new DecimalToBinaryUser().run();
  } // method main

  public static void run()
  {  
    final int SENTINEL = -1;
         
    final String INPUT_PROMPT =
      "\nPlease enter a non-negative base-10 integer (or " +
                  SENTINEL + " to quit): ";

    final String RESULT_MESSAGE = "The binary equivalent is ";

    Scanner sc = new Scanner (System.in);

    int n;
         
    while (true)
    {
      try
      {
        System.out.print (INPUT_PROMPT);
        n = sc.nextInt();               
        if (n == SENTINEL)
          break;            
        System.out.println (RESULT_MESSAGE + getBinary (n));              
      } // try
      catch (Exception e)
      {    
        System.out.println (e);
        sc.nextLine();
      } // catch Exception
    } // while
  } // method run
    
  public static String getBinary (int n) 
  {
      // throw new UnsupportedOperationException();
      if (n < 0)
         throw new IllegalArgumentException();        
      return getBin (n);
  } // method getBinary
 
  public static String getBin (int n) 
  {  
        if (n <= 1)
            return Integer.toString (n);
        return getBin (n / 2) + Integer.toString (n % 2);
  } // method getBin

} // class DecimalToBinaryUser