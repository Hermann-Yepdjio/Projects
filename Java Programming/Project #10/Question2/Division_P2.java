import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

public class Division_P2
{
	protected static boolean condition=false;  //to decide if the user should be prompted again for file name
	protected static long divident;  // number to be divided
	protected static int divisor;    // number that devides
	protected static long quotient=0;  //answer
	protected static long remainder=0;  //remainder from division
	protected static long result=0;  //temporary variable to hold the answer
	protected static int choice;  //to hold choice between loop method and recursive method
	public static void main (String[] args)
	{
		client();
	}
	
	public static void input()  //get the input from the user
	{  
	     while (condition==false)  //keep prompting the user if wrong file is provided
	     {
		String fileName=JOptionPane.showInputDialog("Please insert the name of the file containing the long integer " 
							+"to be devided(Please include the extension and make sure"
						       +" the file is the directory Documents)"); //display Welcome message and
								//get fileName from the user
		try //try for exceptions
		{
			File file= new File ("C:\\Users\\Hermann\\Documents\\"+fileName);
			Scanner scan=new Scanner(file);
		
			if (scan.hasNextLong())
			{
			   divident=scan.nextLong(); // check if first token is a long integer
			   boolean condition2=false; // condition to check if the user entered a valid integer
			   while (condition2==false)  // keep prompting the user if a non integer is typed
			   {	   
				try   // try for exception
				{
				  divisor =Integer.parseInt(JOptionPane.showInputDialog("Please type an integer (the divisor)"));

				  if(divisor==0)
	  			  {
		    			JOptionPane.showMessageDialog(null, "Sorry the division by 0 can't be performed. Please "
							+"try again");
	   			  }
				  else
				  	condition2=true;
				}
				catch(IllegalArgumentException iae)
				{
				  JOptionPane.showMessageDialog(null, "Sorry wrong input. Please make sure you enter an integer");
				}
			   }
			   boolean condition3=false; //condition to check if the user entered a valid choice 
			   while (condition3==false)//keep prompting the user if non valid input is provided
			   {
				try
				{
			   	  choice = Integer.parseInt(JOptionPane.showInputDialog("What technique do u want to use for this"
						   				+" divison? \n"
										+" type '1' for loop and '2' for recursion"));
				  if (choice==1 || choice==2)
					condition3=true;
				  else
					JOptionPane.showMessageDialog(null, "Sorry!! Wrong inout try again. Please make a "
									+"a valid choice");
				}
				catch (IllegalArgumentException iae)
				{
				  JOptionPane.showMessageDialog(null, "Sorry!! Wrong input. Please try again");
				}
				
			   }


			}
			else
				JOptionPane.showMessageDialog(null, "Sorry the file you specified is either empty or contains a"
					       		   + " wrong data. please try again and make sure the file is not empty"
		        			       + " and that the first element in the file is a long integer");
				condition=true;
		}
		catch(FileNotFoundException fnfe)
		{
			JOptionPane.showMessageDialog(null, "Sorry the file you specified does not exist. Please try again");
		}
	
	     }
	}
	public static void whileDivision() //uses a while loop to perform the division(Only uses negative numbers)
	{
		while (result+divisor >=divident && result+divisor<0)
		{
			result=result+divisor;
			quotient++;
		}
		remainder = divident-result;
		
	}
	public static void recDivision() // uses recursion to perform the division(Only uses negative numbers)
	{
		if (result+divisor>=divident && result+divisor<0)
		{
			result=result+divisor;
			quotient++;
			recDivision();
		}
		
		else
			remainder = divident-result;
	}
	public static void client ()  //display results base on inputs
	{
	   input();
	    
	    if (choice==1) // uses loop technic if user choice was 1
	    {	    
		if (divident>0 && divisor<0) //handle division for positive dividents and negative divisors
		{
			divident=-1*divident;
			whileDivision();
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ divisor + " = " + -1*quotient + "   "
				       			+"Remainder = "+ -1* remainder);
		}
		else if (divident<0 && divisor>0)  //handle division for negative dividents and positive divisors
		{
			divisor=-1*divisor;
			whileDivision();
			JOptionPane.showMessageDialog(null, divident +" : "+ -1*divisor + " = " + -1*quotient + "    "
							+"Remainder = "+ remainder);
		}
		else if (divident>0 && divisor>0)  //handle division for positive dividents and positive divisors
		{
			divisor=-1*divisor;
			divident=-1*divident;
			whileDivision();
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ -1*
					divisor + " = " + quotient + "    "
							+"Remainder = "+ -1* remainder);
		}
		else if (divident<0 && divisor<0) //handle division for negative dividents and negative divisors
		{
			whileDivision();
			JOptionPane.showMessageDialog(null, divident +" : "+ divisor + " = " + quotient + "    "
						+"Remainder = "+ remainder);
		}
	    }
		

	    else  //uses recursive technic if user choice was 2
	    {
	      try
	      {
 
		if (divident>0 && divisor<0) //handle division for positive dividents and negative divisors
		{
			divident=-1*divident;
			recDivision();
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ divisor + " = " + -1*quotient + "   "
				       			+"Remainder = "+ -1*remainder);
		}
		else if (divident<0 && divisor>0) //handle division for negative dividents and positives divisors
		{
			divisor=-1*divisor;
			recDivision();
			JOptionPane.showMessageDialog(null, divident +" : "+ -1*divisor + " = " + -1*quotient + "    "
							+"Remainder = "+ remainder);
		}
		else if (divident>0 && divisor>0) //handle division for positive dividents and positive divisors
		{
			divisor=-1*divisor;
			divident=-1*divident;
			recDivision();
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ -1*divisor + " = " + quotient + "    "
							+"Remainder = "+ -1*remainder);
		}
		else if (divident<0 && divisor<0)  //handle division for negative dividents and negative divisors
		{
			recDivision();
			JOptionPane.showMessageDialog(null, divident +" : "+ divisor + " = " + quotient + "    "
							+"Remainder = "+ remainder);
		}
	      }
	      catch (StackOverflowError soe)
	      {
		      JOptionPane.showMessageDialog(null, "Sorry you can't use recursion for this operation because the "
				      + "divident and divisor you provided are too far appart from each other.  \n"
				   + "Using this method, the program will run into a stack overflow error. \n"
			+"  Please try again and either use the loop method or provide different numbers that are closer"
			+" to each other. ");
	      }
	    }
	}

}
