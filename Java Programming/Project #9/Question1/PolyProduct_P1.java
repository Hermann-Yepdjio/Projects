import java.io.*;
import java.util.Scanner;
import java.util.*;
public class PolyProduct_P1 
{
	private static ArrayList<String> Polynomials;
	
	public static void main(String[] args) throws IOException
	{
		Polynomials = new ArrayList<String>();
		ReadFile();
		Display();
		
	}
	public static void ReadFile() throws IOException
	{
		
		File file=new File ("C:\\Users\\Hermann\\Documents\\binomials.txt");  //instantiate the 
														//file that contains the polynomials
		Scanner scan= new Scanner(file);  //instantiate a scanner to scan through the file
		while (scan.hasNextLine())
		{
			Polynomials.add(scan.nextLine()); //copy each line of the file in the arraylist 
		}
		scan.close();
	}
	public static ArrayList<Integer> PolyProduct() //opera the product and return an arraylist 
												//containing the coefficients of the final polynomial
	{
		ArrayList<Integer> Result = new ArrayList<Integer>(); //to stock finals results
		if (Polynomials.size()==0)
			return Result;			// return an empty arraylist if the file is empty
		Result.add(0);		//instantiate an first imaginary function 0X+1 that will multiply
		Result.add(1);		//the first real function in the in file without changing its values
		for (int i = 0; i<Polynomials.size(); i++) //operates the functions product
		{
			ArrayList<Integer> tempResult=new ArrayList<Integer>(); //to contains results after 
																	//a number is multiplied by another
			ArrayList<Integer> tempResult2=new ArrayList<Integer>();// to contains results after
														//corresponding coefficients are added together
			int [] PolyNums = new int[2]; //to contain values of the next function in the file;
			String[] temp= Polynomials.get(i).split("x");
			try
			{
				PolyNums[0]=Integer.valueOf(temp[0].replaceAll("\\s",""));
				PolyNums[1]=Integer.valueOf(temp[1].replaceAll("\\s",""));
			}
			catch (NumberFormatException nfe)
			{
				throw new NumberFormatException("Sorry one or more function(s) in the file is not of the correct format");
			}
			
			for (int value:Result)
			{
				tempResult.add(value*PolyNums[0]);
				tempResult.add(value*PolyNums[1]);
			}
			tempResult2.add(tempResult.get(0));
			for (int j=1; j<tempResult.size()-1; j+=2 )
			{
				tempResult2.add(tempResult.get(j)+tempResult.get(j+1));
			}
			tempResult2.add(tempResult.get(tempResult.size()-1));
		
			Result=tempResult2; // update Result before tempResult2 gets initialize again in the loop
		}
		
			return Result;
			
	}
	public static void Display() //to display the resulting function on a nice format
	{
		if (PolyProduct().isEmpty())
			System.out.println("Sorry the file does not contain any function");
		else
		{
			int size=PolyProduct().size(); //this represent the size of the arraylist 
										   //returned by the PolyProduct function
			String FinalFunction="";
			for (int i=1; i<PolyProduct().size()-2; i++)
			{
				if (PolyProduct().get(i)<0)
					FinalFunction= FinalFunction+(PolyProduct().get(i) + "X^" + (size-i-1)+" ");
				else if(PolyProduct().get(i)>0 && i>1)
					FinalFunction= FinalFunction+ "+"+(PolyProduct().get(i) + "X^" + (size-i-1)+ " ");
				else if (PolyProduct().get(i)>0 && i==1)
					FinalFunction= FinalFunction+(PolyProduct().get(i) + "X^" + (size-i-1)+" ");
			}
			if (PolyProduct().get(size-2)<0)
				FinalFunction= FinalFunction+ (PolyProduct().get(size-2)+"X"+" ");
			else if(PolyProduct().get(size-2)>0 && size==3)
				FinalFunction= FinalFunction+(PolyProduct().get(size-2)+"X"+" ");
			else if(PolyProduct().get(size-2)>0)
				FinalFunction= FinalFunction+"+" +(PolyProduct().get(size-2)+"X"+" ");
			if (PolyProduct().get(size-1)<0)
				FinalFunction= FinalFunction+ (PolyProduct().get(size-1));
			else if(PolyProduct().get(size-1)>0)
				FinalFunction= FinalFunction+ "+" +(PolyProduct().get(size-1));
				
			System.out.println(FinalFunction);
		}
		
		
	}

}
