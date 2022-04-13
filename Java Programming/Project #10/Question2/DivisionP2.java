import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

public class DivisionP2
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
					JOptionPane.showMessageDialog(null, "Sorry!! Wrong inout try again. Please make "
									+"a valid choice");
				}
				catch (IllegalArgumentException iae)
				{
				  JOptionPane.showMessageDialog(null, "Sorry!! Wrong input. Please try again");
				}
				
			   }
			   condition=true;


			}
			else
				JOptionPane.showMessageDialog(null, "Sorry the file you specified is either empty or contains a"
					       		   + " wrong data. please try again and make sure the file is not empty"
		        			       + " and that the first element in the file is a long integer");
				
		}
		catch(FileNotFoundException fnfe)
		{
			JOptionPane.showMessageDialog(null, "Sorry the file you specified does not exist. Please try again");
		}
	
	     }
	}
	public static void whileDivision() //uses a for and while loop to perform the division(assume numbers are negative)
	{
		String[] digits = String.valueOf(divident).split("(?<=.)");
		String tempD="";		//to hold the temporary dividend
		String tempQ="";		//to hold the temporary quotient
		long tempDiv=Math.abs((long)divisor);  //absolute value of divisor
		if (digits.length>String.valueOf(tempDiv).length()) //check if digits has at least the same number of digits 
								   //(first digit excluded which is the sign (-)) than tempDiv
		{
			for (int i=1;i<String.valueOf(tempDiv).length(); i++) //prepare the first temp dividend
				tempD=tempD+digits[i];
			for (int i=String.valueOf(tempDiv).length(); i<digits.length; i++) //append the divident one bit at the
								//time and perform the divison using substraction
			{
				int count=0;	//count how many tempDiv are in tempD(Value used to append temp quotient)
				tempD=tempD+digits[i];
				while (Long.parseLong(tempD)>=tempDiv) //perform the division using substraction
				{
				  tempD= String.valueOf(Long.parseLong(tempD)-tempDiv);
				  count++;
				}
				tempQ=tempQ+ count;
				remainder=Long.parseLong(tempD);
			}
			quotient=Long.parseLong('-'+tempQ);

		}
		else //check if digit(which represent the divident) has less digit than tempDiv
		{
			remainder = -divident;
		}


	}
							// uses recursion to perform the division(Only uses negative numbers)
	
	public static void recDivision(String[] digits, String tempD, String tempQ, int count,int index, long tempDiv) 
			
			//check if digit has at least the number of digit than tempDiv and prepare the first temp divident 
	{	
		if(digits.length>String.valueOf(tempDiv).length() && index<= String.valueOf(tempDiv).length())
		{
			recDivision(digits, tempD+digits[index], tempQ, count, index+1, tempDiv);
			
		}
			
			//check for index out of bound
		else if (digits.length>String.valueOf(tempDiv).length() && index<digits.length)
		{

			if (Long.parseLong(tempD)>=tempDiv) //check to decide if the division should operated and operate it
								//using substraction

			    recDivision(digits, String.valueOf(Long.parseLong(tempD)-tempDiv), tempQ, count+1, index, tempDiv);

			else //check if division is done ,increase index and append tempD for the next division 
			    recDivision(digits, tempD+digits[index], tempQ+count, 0, index+1, tempDiv);


		}
		else if (digits.length>String.valueOf(tempDiv).length() && index==digits.length) //check if last index is reached
				    
		{
			if (Long.parseLong(tempD)>=tempDiv) //check and perform the last division

		      	   recDivision(digits, String.valueOf(Long.parseLong(tempD)-tempDiv), tempQ, count+1, index, tempDiv);
			else //check if last division is over and update values if quotient and remainder
			{

				quotient=Long.parseLong('-'+tempQ+count);
				remainder= Long.parseLong(tempD);
			}
		}
		else //check if divident has less digits than divisor
			remainder = -divident;
				
	
			



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
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ divisor + " = " + quotient + "   "
				       			+"Remainder = "+ remainder);
		}
		else if (divident<0 && divisor>0)  //handle division for negative dividents and positive divisors
		{
			divisor=-1*divisor;
			whileDivision();
			JOptionPane.showMessageDialog(null, divident +" : "+ -1*divisor + " = " + quotient + "    "
							+"Remainder = "+ -1*remainder);
		}
		else if (divident>0 && divisor>0)  //handle division for positive dividents and positive divisors
		{
			divisor=-1*divisor;
			divident=-1*divident;
			whileDivision();
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ -1*divisor + " = " + -1*quotient + "    "
							+"Remainder = "+ remainder);
		}
		else if (divident<0 && divisor<0) //handle division for negative dividents and negative divisors
		{
			whileDivision();
			JOptionPane.showMessageDialog(null, divident +" : "+ divisor + " = " + -1*quotient + "    "
						+"Remainder = "+ -1*remainder);
		}
	    }
		

	   else  //uses recursive technic if user choice was 2
	    {
	      if (divident>0 && divisor<0) //handle division for positive dividents and negative divisors
		{
			divident=-1*divident;
			recDivision(String.valueOf(divident).split("(?<=.)"),"", "", 0, 1, Math.abs((long)divisor));
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ divisor + " = " + quotient + "   "
				       			+"Remainder = "+ remainder);
		}
		else if (divident<0 && divisor>0)  //handle division for negative dividents and positive divisors
		{
			divisor=-1*divisor;
			recDivision(String.valueOf(divident).split("(?<=.)"),"", "", 0, 1, Math.abs((long)divisor));
			JOptionPane.showMessageDialog(null, divident +" : "+ -1*divisor + " = " + quotient + "    "
							+"Remainder = "+ -1*remainder);
		}
		else if (divident>0 && divisor>0)  //handle division for positive dividents and positive divisors
		{
			divisor=-1*divisor;
			divident=-1*divident;
			recDivision(String.valueOf(divident).split("(?<=.)"),"", "", 0, 1, Math.abs((long)divisor));
			JOptionPane.showMessageDialog(null, -1*divident +" : "+ -1*divisor + " = " + -1*quotient + "    "
							+"Remainder = "+ remainder);
		}
		else if (divident<0 && divisor<0) //handle division for negative dividents and negative divisors
		{
			recDivision(String.valueOf(divident).split("(?<=.)"),"", "", 0, 1, Math.abs((long)divisor));
			JOptionPane.showMessageDialog(null, divident +" : "+ divisor + " = " + -1*quotient + "    "
						+"Remainder = "+ -1*remainder);
		}
	    }
	}

}

