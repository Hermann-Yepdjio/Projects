import java.util.*;
import java.util.Scanner;
import java.io.*;
public class Country_City_P2
{
	public String countryName;
	protected ArrayList<City_P2> largeCities=new ArrayList<City_P2>(); // > 500K
	protected ArrayList<City_P2> mediumCities=new ArrayList<City_P2>(); // > 100K
	protected ArrayList<City_P2> smallCities=new ArrayList<City_P2>(); // > 20K
	protected ArrayList<City_P2> ignoredCities= new ArrayList<City_P2>();//<20K
	protected City_P2 largestCity= new City_P2("","","",0);
	public static void main (String[] args)
	{
	}
	public Country_City_P2(String name) //Constructor for Country
	{
		countryName=name;
	}
	public void setCountryName(String name) // sets the country name
	{
		countryName=name;
	}
	public String getCountryName() //return the country name
	{
		return countryName;
	}
	public void addCity(City_P2 city)	//to add a new city in country
	{
		if ( city.getPopulation ()>=0 && city.getPopulation() <=20000)
			ignoredCities.add(city); //add city in ignored cities if condition is true
		else if (city.getPopulation ()>20000 && city.getPopulation() <=100000)
			smallCities.add(city);	//add city in small cities if condition is true
		else if (city.getPopulation() >100000 && city.getPopulation() <=500000)
			mediumCities.add(city); //add city in medium cities if condition is true
		else if (city.getPopulation() >500000)
			largeCities.add(city);  // add city in large cities if condition is true
	}
	public ArrayList<City_P2> getLargeCities() //return array list of large cities
	{
		return largeCities;
	}
	public ArrayList<City_P2> getMediumCities() //return array list of medium cities
	{
		return mediumCities;
	}
	public ArrayList<City_P2> getSmallCities() //return array list of small cities
	{
		return smallCities;
	}
	public City_P2 getLargestCity() //finds a return the largest city

	{
		int max=0;  //check in large cities first
		for(City_P2 city:largeCities)
		{
			if (city.getPopulation()>max)
			{
				max=city.getPopulation();
				largestCity=city;
			}
		}
		if (max==0)  //check in medium cities if there is no large city
		{
			for(City_P2 city:mediumCities)
			{
				if (city.getPopulation()>max)
				{
					max=city.getPopulation();
					largestCity=city;
				}
			}
		}
		if (max==0) //check in small cities if there is no medium and large city
		{
			for(City_P2 city:smallCities)
			{
				if (city.getPopulation()>max)
				{
					max=city.getPopulation();
					largestCity=city;
				}
			}
		}
		if (max==0) //check in ignored cities if there is no small, medium and large city
		{
			for(City_P2 city:ignoredCities)
			{
				if (city.getPopulation()>max)
				{
					max=city.getPopulation();
					largestCity=city;
				}
			}
		}
		return largestCity;
	}



}
