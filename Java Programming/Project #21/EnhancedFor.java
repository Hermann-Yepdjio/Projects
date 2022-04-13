import java.util.*;

// A very simple program that shows that use of an enhanced for.

public class    EnhancedFor {  

     public static void main (String[] argv){ 
            new EnhancedFor().run();

	   } 

    public void run(){
      final double MIN_GPA=0.0;
      final double MAX_GPA=4.0;
      final int SENT =-1;
      final String  INP_PROMPT = "Enter please GPA, "+ SENT +" to quit";


    // Automatic variables must be initialized
     double oneGPA=0.0, sum=0.0;
     ArrayList <Double> gpa= new ArrayList<Double>();
     Scanner sc = new Scanner(System.in);

     while (true) {
	 try {
	   System.out.println( INP_PROMPT);
	   oneGPA = sc.nextDouble();
	   if (  (int) oneGPA ==  SENT ) 
		  break;
	   if (  MIN_GPA >=  oneGPA  &&  oneGPA <= MAX_GPA )
		throw new ArithmeticException("Range Error");
	   gpa.add(oneGPA);
         }
        catch( Exception e) {
           System.out.println(e + "\n");
           sc.nextLine();
         }
      } 

//  The existence of iterator allows the enhanced for.
     for ( Double g : gpa)  
         sum += g;
     System.out.println( "Average GPA: " + sum/gpa.size());

     } 

}
