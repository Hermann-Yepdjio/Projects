
import java.util.*;
public class lab02
{
 

  public static void main(String[] argv)
  { 
	long startTime= System.currentTimeMillis();
	Scanner scan =new Scanner (System.in);
		System.out.println("enter a number: ");
	      int n=scan.nextInt();
		
		System.out.println(pythagoric(n));
	long EndTime= System.currentTimeMillis();
		System.out.println("The running time was: "+( EndTime-startTime));

  }
  public static long pythagoric (long n )
  {
 	long ret=0;
	for (int x = 0 ; x < n ; ++x )
	for (int y = 0 ; y < n ; ++y )
	for (int z = 0 ; z < n ; ++z )
	ret =x*y*z + (y-1)/3 ;
	return ret;
  }
}
 
