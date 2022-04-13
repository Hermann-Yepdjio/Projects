/*---------------------------------------------------------------------
  Drill-and-Practice Program that generates random drill-and-practice 
  addition problems. Problems that are answered incorrectly are 
  queued and asked again until all are answered correctly or maximum
  number of tries is reached.

  Input:  Number of problems to generate, student's answers to
          problems
  Output: Messages, problems, correct answers, number of problems
          answered correctly

  -------------------------------------------------------------------*/

import java.util.*;

public class  MathDrill  {
protected   int numProblems,              // number of problems asked
                maxAddend;                // maximum addend in a problem
protected final int  MAX_ROUNDS = 3;     // maximum number of rounds in
                                 //   which to try the problems

private Scanner keyboard = new Scanner(System.in);




public static void  main( String[]  argv ) {

     MathDrill d1 = new MathDrill();
     d1.rundrill();


}

private  void rundrill() {

  // Generate numProblems problems and store them in a queue.
  Queue<Problem> problemQueue =  new LinkedList<Problem>();
  Problem problem;

   // initialize();   

   System.out.println( "How many problems would you like? " );
   numProblems = keyboard.nextInt() ;

   System.out.println(" What's the largest addend you would like? ");
   maxAddend = keyboard.nextInt() ;




   // Generate numProblems problems and store them in a queue.
   for (int i = 1; i <= numProblems; i++)
   {
     problem = new  Problem(maxAddend);
     problemQueue.add(problem);
   }

   // Conduct the practice rounds
    problem = new Problem(maxAddend);      // next addition problem
   int userAnswer=0,               // user's answer to a problem
       numberMissed =0;             // number of problems missed
   for (int round = 1; round <= MAX_ROUNDS; round++)
   {
      // One round of problems
      numberMissed = 0;
      for (int count = 1; count <= numProblems; count++)
      {
         problem = problemQueue.element();
         problemQueue.remove();
         System.out.print(problem + "\n Answer: ");
         userAnswer = keyboard.nextInt();
         if (userAnswer == problem.answer())
            System.out.print("Correct!\n\n");
         else
         {
            System.out.print("Sorry -- Try again later\n\n");
            problemQueue.add(problem);
            numberMissed++;
         }
      }

      if (numberMissed == 0)
      {
         System.out.println("Congratulations! You correctly answered all the problems in Round #"
       + round );
         break;
      }
      else
      {
         System.out.println("\nYou missed " + numberMissed + " problems in Round #" +round )  ;
         if (round < MAX_ROUNDS)
            System.out.print("You may now try them again.  Good luck!\n");
         numProblems = numberMissed;
      }
   }

   // Wrapup
   if (numberMissed == 0)
     System.out.println("You have finished the quiz and have successfully\n" +
             "answered all the problems.  Good job!") ;
   else
   {
      System.out.print("\nYou have reached the limit on the number of tries " +
              "allowed.\n Here are the problems you missed:\n\n");
      while (!problemQueue.isEmpty())
      {
         problem = problemQueue.element();
         problemQueue.remove();
         System.out.print( problem + " Answer: " + problem.answer() + "\n\n");
      }
      System.out.print( "Perhaps it would be a good idea to practice some more.\n");
   }
}

}
