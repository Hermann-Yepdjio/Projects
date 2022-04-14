#include "Product.h"

//default constructor
Product::Product()
{
}

//constructs a product object
Product::Product(int _id, unsigned long long int _upc14, unsigned long long int _upc12, string _brand, string _name)
{ 
    	id = _id;
    	upc14 = _upc14;
    	upc12 = _upc12;
	brand = _brand;
	name = _name;
}

//getter for upc14
unsigned long long int Product::get_upc14()
{
	return upc14;
}

//getter for upc12
unsigned long long int Product::get_upc12()
{
	return upc12;
}

//getter for name;
string Product::get_name()
{
	return name;
}
