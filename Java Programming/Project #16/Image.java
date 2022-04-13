import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.File;
//import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Image
{
	int height, width;
	int[][] raw_values;
	int blueMask = 0xFF0000, greenMask = 0xFF00, redMask = 0xFF;

	public Image() //default constructor
	{
	}
	public void read_input(String file_name)
	{
		BufferedReader reader;
		try 
		{
			reader = new BufferedReader(new FileReader(file_name));
			String line = reader.readLine();
			int count = 0;
			int row_count = 0;
			int col_count = 0;
			while (line != null) 
			{
				if (count == 0) //read first line in image.dat and extract width and height info
				{
					String[] splitted = line.split("\\s+");//split line at space characters
					height = Integer.parseInt(splitted[0]);
					width = Integer.parseInt(splitted[1]);

					raw_values = new int[height][width * 3]; //create a 2d array of pixel values in file
					count+=1;

				}
				else //fill the 2d array (i.e raw values) with pixel values from image.dat
				{
					String[] splitted = line.split( "[\\s,]+" ); //split line at space and comma characters
					col_count = 0; //reset column count to zero as we are moving to a new row
					for(String data: splitted) // read value in file and insert it in raw_values at the corresponding (row, column) position
					{
						raw_values[row_count][col_count++] = Integer.parseInt(data); 
					}
					row_count++;
				}
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

	//create the PNG image
	public void create_image()
	{
		int r, g, b, rgb_value;
		BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 

		//compute single rgb values for every set of (red, green, blue) values in raw_values
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				r = raw_values[i][j * 3];
				g = raw_values[i][j * 3 + 1];
				b = raw_values[i][j + 3 + 2];
				rgb_value = (r << 16) + (g << 8) + b;
				canvas.setRGB(j, i, rgb_value);		
			}
		}

		try 
		{
			ImageIO.write(canvas,"PNG",new File("image.png"));
		}	
		catch (IOException e) 
		{
              		e.printStackTrace();
        	}
	}

	public static void main(String[] args)
	{
		Image im = new Image();
		im.read_input("image.dat");
		//System.out.println(Arrays.deepToString(im.raw_values));
		im.create_image();
	}
}
