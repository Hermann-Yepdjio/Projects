import java.util.Scanner;


public class DecimalToBinary {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		System.out.println("The equivalent number in base 2 format is: " + converter());

	}
	public static int converter()
	{
		int quotient;
		int Remainders;
		String result;
		
		Scanner scan =new Scanner (System.in);
		System.out.print("Please enter a base 10 format number: ");
		quotient = scan.nextInt();
		if (quotient<0)
		{
			System.out.println("Sorry what you typed is not a positive number");
			System.exit(1);
		}
		else if (quotient==0)
			return 0;
		result="";
		Remainders =0;
		while (quotient !=0)
		{
			Remainders=quotient % 2;
			result = result + Remainders;
			quotient = quotient/2;
		}
		String newResult= new StringBuffer(result).reverse().toString();
		return Integer.parseInt(newResult);
		
		
	}

}
