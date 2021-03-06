import java.util.*;
import java.io.*;
public class CitiesDisplayP2
{
	protected static Country_City_P2[] array = new Country_City_P2[20]; //array of countries(size 20)
	protected static int indexLast=0; //to keep track of last element in the array
	protected static boolean condition= false;	//condition for looping when user enters wrong info
	protected static Scanner scan1 = new Scanner(System.in);		//instantiate a scanner to read 
																	//inputs from the user
	public static void main (String[] args)
	{
		readFile();
		countriesList();
		userRequest();
	}
	public static void readFile() //read the file, creates new objects and store values and objects
	
	{
		while (condition==false) //to keep the program running after a wrong input
		{
			try //try and catch exceptions
			{
				System.out.println("Please type the name of the file containing the cities "
						+ "(Please do not forget to include the extension)"); //Print a message to
																			  //the user
				String fileName=scan1.next();		//read the file name typed by the user
				File file= new File("C:\\Users\\Hermann\\Documents\\"+fileName); //instantiate 
																				 //a new file
				Scanner scan= new Scanner (file); //instantiate the scanner for the file
				if (!scan.hasNextLine()) //check if the file is empty
					System.out.println("Sorry the file you specified is empty. Please try again.");
				while (scan.hasNextLine())//if file is not empty
				{
					String[] line= scan.nextLine().split("[ ]+"); // split the line using 
																  //spaces as delimiter
					if (line.length !=4) //throw new exception if a line does not have 4 tokens
					{
						
						throw new Exception ("One or more lines in your file contains less "
								+ "or more than 4 fields"); 
					}
					else
					{	
																//splits each line in the file
						City_P2 city=new City_P2(line[0],line[1], line[2], Integer.parseInt(line[3]));
						boolean exist= false;
						for (int i=0; i<indexLast; i++) //check if a country already exist in the array
						{
							if (array[i].getCountryName().equals(line[2]))
							{
								exist=true;
								array[i].addCity(city); //add a new city to the country
							}
						}
						
						if (exist==false) //Check if country does not exist in the array
						{
							array[indexLast]=new Country_City_P2(line[2]); //create a new country
							array[indexLast].addCity(city);  //add a new city
							indexLast++; //increment index for next country to be created
							
						}

					}
					
				}
				condition=true; //condition to get out of the loop
				scan.close(); //closes the scanner scan
			}
			catch (FileNotFoundException fnfe)
			{
				System.out.println ("Sorry the file you specified does not exist. Please try "
						+ "again and make sure the file you specify is in the directory " + ""
								+ "Documents");
			}
			catch (IllegalArgumentException tme)
			{
				System.out.println("Sorry the file you specified contains wrong information."
						+ " Please try again an make sure each data in the file is of the "
						+ "correct type");
			}
			catch (Exception e)
			{
				System.out.println ("Sorry the file you specified contains wrong information."
					+ " Please try again and make sure each line in the file contains, the"
					+ " city name, Province or department, the country and the popualtion"
					+ " of the city. Please sure each line contains all these fields and "
					+ "only these fields and excactly in the same orther separated with white"
					+ " space(s)");
			}
			

			
		}
		//scan1.close();

				 
	}
	public static void countriesList() //print out the list of countries along side with their 
					//largest city and populations, number of small, medium and 
										//large cities
	{
		System.out.println("The following is the list of the different countries(with their information)"
				+ " in the file specified earlier ");
		System.out.println(); // Prints empty line to make the output look better
		for (int i=0; i<indexLast;i++)
		{
			Country_City_P2 country=array[i];
			
		        System.out.println("Country: "+ country.getCountryName() + "\nLargest City: "
					+ country.getLargestCity().getCityName() + "; Pop: "
							+ country.getLargestCity().getPopulation());
			System.out.println( "Small cities: "+country.getSmallCities().size()+ "\nMedium cities: "
					+ country.getMediumCities().size() 
					+"\nLarge cities: " + country.getLargeCities().size());
			System.out.println(); // Prints empty line to make the output look better
		}

	}
	public static void userRequest() //display information desired by the user
	{
		boolean found = false; //condition to keep looping if a wrong input in entered
		String country;
		String type;
		while (found==false)
		{   
			System.out.println ("Type a country name excatly as it appears in the file");
			country = scan1.next(); //get a country name from a user
			System.out.println("type small, medium or large and press enter for the list of "
					+ "small, medium or large cities");
			type=scan1.next(); //get a city type from the user
			System.out.println(); // Prints empty line to make the output look better
				
			
			for (int i=0; i<indexLast; i++)  // loops through the array of countries
			{
				Country_City_P2 country1=array[i];
				if (country1.getCountryName().equals(country)) //check if the country specified
										//by the user exits in the array
				{
					if (type.equals("small")) //check the user input for city type and print 
											//the list of small cities
					{
					     System.out.println("These are the small cities in "+ country + " followed"
								+ " by their populaion:");
					     System.out.println(); // Prints empty line to make the output look better
					     for (City_P2 city: country1.getSmallCities())
					     System.out.println(city.getCityName() + ": " + city.getPopulation());
					     found=true;
					}
					if (type.equals("medium")) //check the user input for city type and print 
											//the list of medium cities 
					{
					    System.out.println("These are the medium cities in "+ country + " followed "
								+ "by their population:");
					    System.out.println(); // Prints empty line to make the output look better
					    for (City_P2 city: country1.getMediumCities())
					    System.out.println(city.getCityName() + ": " + city.getPopulation());
					    found=true;
					}
					if (type.equals("large"))  //check the user input for city type and print 
											//the list of large cities 
					{
					    System.out.println("These are the large cities in "+ country + " followed "
								+ "by their populations:");
					    System.out.println(); // Prints empty line to make the output look better
				            for (City_P2 city: country1.getLargeCities())
					    System.out.println(city.getCityName() + ": " + city.getPopulation());
					    found=true;
					}
				}
			
			}
			if (found==false)
				System.out.println("Sorry you entered either a wrong country name or city type."
						+ " Please try again");
		}
		scan1.close();
	}


}
