import java.util.*;

public class TimingRandom
{  
   public static void main (String[ ] args) 
   {
       new TimingRandom().run();
   } // method main
   
   public void run()
   {
       final int SENTINEL = -1;

       final String INPUT_PROMPT = "\nPlease enter the number of"+
        " integers to be sorted (or " + SENTINEL + " to quit): ";
      
       final String ANSWER_1 = "The elapsed time was ";
       final String ANSWER_2 = " seconds.";

       final double NANO_FACTOR = 1e9; // nanoseconds per second


       Scanner sc = new Scanner (System.in);
       
       long elapsedTime;
       
       while (true)
       {
           try
           {
               System.out.print (INPUT_PROMPT);  
               int n = sc.nextInt();
               if (n == SENTINEL)
                   break;
               elapsedTime = randomTimer (n);
               System.out.println (ANSWER_1 + 
                                   (elapsedTime / NANO_FACTOR) + ANSWER_2);                
           } // try
           catch (Exception e)
           {
               System.out.println (e); 
               sc.nextLine();
           } // catch 
       } // while
   } // method run
   

   /**
    *  Determines the elapsed time to sort a randomly generated array of ints.    
    *
    *  @param n  the size of the array to be generated and sorted.
    *
    *  @return the elapsed time to sort the randomly generated array.
    * 
    *  @throws NegativeArraySizeException  if n is less than 0.
    * 
    */
   public long randomTimer (int n)
   {       
       Random r = new Random(); 

       long startTime,
            finishTime,
            elapsedTime;
  
       int[ ] x = new int [n];
       for (int i = 0; i < n; i++)
           x [i] = r.nextInt();
       startTime = System.nanoTime();

       // Sort x into ascending order:
       selectionSort (x);

       // Calculate the elapsed time:
       finishTime = System.nanoTime();
       elapsedTime = finishTime - startTime;
       return elapsedTime;                         
   } // method randomTimer
   
   /**
    *  Sorts a specified array of int values into ascending order.
    *  The worstTime(n) is O(n * n).
    *
    *  @param x  the array to be sorted.
    *
    */
   public static void selectionSort (int [ ] x) 
   {
       // Make x [0  i] sorted and <= x [i + 1] x [x.length  1]:
       for (int i = 0; i < x.length - 1; i++) 
       {
           int pos = i;
           for (int j = i + 1; j < x.length; j++)
               if (x [j] < x [pos]) 
                   pos = j;
           int temp = x [i];
           x [i] = x [pos];
           x [pos] = temp;           
        } // for i
    } // method selectionSort

} // class TimingRandom 
