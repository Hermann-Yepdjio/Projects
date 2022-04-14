#ifndef PRODUCT
#define PRODUCT

#include<string>
#include <iostream>
using namespace std;

class Product{
    private:
        int id;
	unsigned long long int    upc14, upc12;
       	string brand, name;


    public:
	//default constructor
	Product();

        //constructs a product object
        Product(int, unsigned long long int, unsigned long long int, string, string);

	//getter for upc14
	unsigned long long int get_upc14();
	
	//getter for upc12
	unsigned long long int get_upc12();

	//getter for name;
	string get_name();

};

#endif
