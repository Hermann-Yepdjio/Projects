import java.util.ArrayList;

public class Part1 
{
	public static void main (String[] args)
	{
		ArrayList<String> AL1=new ArrayList<String>();
		ArrayList<Integer> AL2=new ArrayList<Integer>();
		AL1.add("Neymar");
		AL1.add("messi");
		AL1.add("suarez");
		AL2.add(11);
		AL2.add(10);
		AL2.add(9);
		AL1.remove(0);
		AL2.remove(0);
		System.out.println(AL1);
		System.out.println(AL2);
	}
}
