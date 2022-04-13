import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BoxClient 
{
	static ArrayList<Double> al;
	double height;
	double width;
	double length;
	
	public static void main(String[] args) throws IOException
	{
		File file= new File ("boxes.txt");
		Scanner scan = new Scanner(file);
		while (scan.hasNextDouble())
		{
			al.add(scan.nextDouble());
		}
		scan.close();
		
		Box box1=new Box(al.get(0), al.get(1), al.get(2));
		Box box2=new Box(al.get(3), al.get(4), al.get(5));
		Box box3=new Box(al.get(6), al.get(7), al.get(8));
		Box box4=new Box(al.get(9), al.get(10), al.get(11));
		
		System.out.println(box1.compareTo(box2));
		System.out.println(box2.compareTo(box3));
		System.out.println(box3.compareTo(box4));
		System.out.println(box4.compareTo(box1));
		

	}

}
