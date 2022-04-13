
public class Box implements Comparable<Box>
{
	double length;
	double width;
	double height;
	public Box(double l, double w, double h)
	{
		length=l;
		width= w;
		height= h;
	}
	public double getLength()
	{
		return length;
	}
	public double getWidth()
	{
		return width;
	}
	public double getHeight()
	{
		return height;
	}
	public int equals(Box b)
	{
		if (length==b.getLength() && width==b.getWidth() && height== b.getHeight())
			return 1;
		else
			return 0;
			
	}

	public int compareTo(Box b) 
	{
		if (this.equals(b)==1)
			return 0;
		else
		{
			if(length>b.getLength()|| width>b.getWidth()|| height> b.getHeight())
				return 1;
			else
				return -1;
		}
		
	
	}

}
