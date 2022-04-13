import java.util.*;
import java.io.File;
import javax.swing.*;

public class BaseToDecimal 
{ 
	public static void main(String[] args) 
	{
		
		System.out.println("The equivalent number in base 10 format is: " + converter());
	}
	
	public static int converter()
	{
		int BaseNum;
		int Base;
		int Result;
		
		Scanner scan =new Scanner (System.in);
		System.out.print("Please enter a base from 2-9: ");
		Base = scan.nextInt();
		while (Base<2 || Base>9)
		{
			System.out.print("Incorrect base system. Please enter a base from 2-9: ");
			Base = scan.nextInt();
		}
		System.out.println();
		System.out.print("Enter a base"+ Base+ " number: ");
		BaseNum= scan.nextInt();
		char[] myNum= (String.valueOf(BaseNum)).toCharArray();
		Result=0;
		for (int Index=0; Index < myNum.length; Index++)
		{
			if (Character.getNumericValue(myNum[Index])>=Base)
			{
				System.out.println("Sorry what you typed is not a base "+ Base+" number");
				System.exit(1);
			}
			else if (Character.getNumericValue(myNum[Index])<0)
			{
				System.out.println("Sorry what you typed is not a positive number");
				System.exit(1);
			}
			
			else
			{
				Result =Character.getNumericValue(myNum[Index])*(int)Math.pow(Base,(myNum.length-1-Index)) + Result;
			}
		}
		return Result;
		
	}
	
}
