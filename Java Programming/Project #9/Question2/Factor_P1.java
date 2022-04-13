import java.io.*;
import java.util.*;
public class Factor_P1 {
private static long integer;
private static boolean positive;
private static int counter;
	public static void main(String[] args) throws IOException
	{
		ReadFile();
		Display();
	}
		
	public static void ReadFile() throws IOException //reads the file and throws an exception
													// if the file is empty or inexistent
	{	
		File file=new File("C:\\Users\\Hermann\\Documents\\integer.txt");
			Scanner scan = new Scanner(file);
	
		try
		{
			integer=Long.parseLong(scan.nextLine());
		}
		catch (NumberFormatException nbe)
		{
			scan.close();
			throw new NumberFormatException ("Please make sure that what is in the file in an integer");
		}
		catch ( NoSuchElementException nsee)
		{
			scan.close();
			throw new NoSuchElementException("Sorry the file is empty");
		}
		positive=true;
		if (integer<0) // change a negative value to positive to make the factorization easier
		{
			positive=false; //to remind that the value was initially negative
			integer=-1*integer;
		}
		scan.close();
	}
	public static long Factor(long n)
	{	
		counter=0; //initialize the counter for root square operations
		if (n%2==0 && n!=0) // check if the number is even and return 2 if it is the case
			return 2;
		double Y=0;
		long X=(long)Math.floor(Math.sqrt(n)); //performs the square root of the integer in the file
		counter++;  // counter increases by one because a square root operation was just performed
		boolean Condition=true; //initialize a condition to loop between step 2 and 3 as long as needed
		if (Math.pow(X, 2)==n) //check if X^2=n and X if it the case
			return (int)X;
		X=X+1;
		while (Condition) //loops through step 2 and 3 until we have a factor or know that 
						// the number is prime
		{
			if (X==(n+1)/2)
			{
				return n;
			}
			else
			{
				Y=Math.sqrt(Math.pow(X, 2)-n);
				counter++;
			}
			if (Y%1==0)
			{
				return (int)(X+Y);
			}
			else 
				X=X+1;
		}
		return (int)Y;
		
	}
	public static void Display () // displays the result in a nice format
	{
		if (integer==0)
			System.out.println(integer +" = ( 0 ) * ( 0 )");
		else if (integer==Factor(integer)&& integer!=1 && integer!=-1)
		{
			if (positive==false)
				integer=-1*integer; // to put the integer back to its negative form
			System.out.println(integer + " can't be factored");
		}
		else if(positive==false)
		{
			integer =-1*integer;  // to put the integer back to its negative form
			System.out.println(integer +" = ( "+Factor(-1*integer)+" ) * ( "+integer/Factor(-1*integer)+" )");
		}
		else 
			System.out.println(integer +" = ( "+Factor(integer)+" ) * ( "+integer/Factor(integer)+" )");

		System.out.println("There have been "+ counter + " square root operations performed to obtain this result");
	}
	
}
