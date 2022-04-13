// This class has a number of syntax errors. After fixing them by modifying
// the body of the procedure proceed to the implementation. 
import java.util.*;
public class UtilQueue 
{ 
	public static int a;
	public static Queue<Problem> q;
	public static Queue<Problem> extraCredit;
	public UtilQueue()
	{
		q= new LinkedList<Problem>();
		extraCredit= new LinkedList<Problem>();
	}
	// remove the first  half of the element of  q1, and returns them as another queue.
public static  Queue<Problem>  split (  Queue<Problem>  q1  ) 
{ 
	Queue<Problem> q0= new LinkedList<Problem>();
	int num=q1.size()/2;
	for(int i=0; i<num; i++)
		q0.add(q1.remove());
            return q0;
}

// returns the problem in the middle
public  static Problem  peekMid ( Queue<Problem> q2 ) 
{
		Queue<Problem> q1= split(q2);
		return  q2.peek();
}

// returns the  last Problem in.
public  static Problem  peekLast ( Queue<Problem> q2 ) 
{
	Queue<Problem> q0=q2;
	int num=q2.size();
        for(int i=0; i<num; i++)
        {
        	if(i==num-1)
        		return q0.poll();
        	q0.poll();
        	
        }
        return  null;
  }

// makes  the  last  n problems part of an  extrac credit queue.
public  static Queue<Problem>  extraCredit (int n,  Queue<Problem> q1 )  
{
	Queue<Problem> q0=  new LinkedList<Problem>() ;
	int num= q1.size();
	for(int i=0; i<num; i++)
	{
		if(i>=num-n)
			extraCredit.add(q1.poll());
		else
			q1.poll();
		
	}
        return  extraCredit;
}

}
      

