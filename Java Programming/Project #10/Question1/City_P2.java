
public class City_P2

{
	protected String cityName;
	protected String provinceName;
	protected String countryName;
       	protected int population;	
	public City_P2(	String city, String province, String country,int pop) //Constructor for Cities
	{
		cityName=city;
		provinceName=province;
		countryName=country;
		population=pop;
	}
	public void setCityName(String City) //Set the name of the city
	{
		cityName=City;
	}
	public void setPopulation(int pop) //Set the population
	{
		population=pop;
	}
	public String getCityName() // return the city's name
	{
		return cityName;
	}
	public int getPopulation() // return the population
	{
		return population;
	}	
	public String getProvinceName() //return the province's name
	{
		return provinceName;
	}
	
	public void setProvinceName(String province) //set the province's name
	{
		provinceName=province;
	}	
	public void setCountryName(String country) //set the country's name
	{
		countryName=country;
	}
	public String  getCountryName() //return the country's name
	{
		return countryName;
	}	


}
