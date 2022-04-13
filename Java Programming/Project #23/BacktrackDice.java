import java.util.*;

/* Program that show all possible ways to add to some value with some number of
 * dice. 
 *
 *  Illustrates some advanced tecniques but the basic iteration is reamains.
 *  For example, the search is exhaustive, will find all cases.
*/
 
public class    BacktrackDice {  


    public static void main (String[] argv){ 
       diceSum(3, 15);

     } 
//  Searches for  all:
//      desired:   sum of dice
//      dice:      number of dice involved 
//
public static void diceSum( int dice, int desired) {
    List<Integer> chosen = new ArrayList<Integer>(); // chosen is empty.
    btSearch (dice, desired, chosen, 0);
   }

//    Backtracking: a number of parameters 
//
//      dice: remaining dice
//      desired :  desired sum 
//      chosen : current choice  c0, c1, c2 ..
//      sofar  : current sum

private static void btSearch ( int dice , int desired, List<Integer> chosen, int soFar){
  if (dice == 0) {
         if (soFar == desired) 
             System.out.println( chosen);
         }
// notice the heuristic of not attempting any choice which is not bound
// to succeed (soFar too low). This is call prunning. 
//
  else if (soFar <= desired && soFar + 6*dice >= desired) 
          for ( int i= 1; i <= 6; ++i) {
             chosen.add(i);
             btSearch(dice -1, desired, chosen, soFar+i);
             chosen.remove(chosen.size() - 1) ; //upon return makes another attempt.
            }
  }
}
