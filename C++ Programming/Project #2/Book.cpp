#include "Book.h"
//#include <iostream>

//using namespace std;

//default constructor
Book::Book() {}

//constructor
Book::Book(string t, int i, string a, string p)
{
	title =t;
	authors = a;
	publisher = p;
	isbn = i;
}
//another constructor
Book::Book(int i)
{
	isbn = i;
}
//Definition for Destructor 
Book::~Book(){} 

//return the title
string Book::get_title()
{
	return title; 
}

//edit the title
void Book::set_title(string t)
{
	title = t;
}

//print the title
void Book::show_title()
{
	cout << title << endl;
}

//return the authors
string Book::get_authors()
{
	return authors;
}

//edit the authors
void Book::set_authors(string a)
{
	authors = a;
}

//print the authors
void Book::show_authors()
{
	cout << authors << endl;
}

//return the publisher
string Book::get_publisher()
{
	return publisher;
}

//edit the publisher
void Book::set_publisher(string p)
{
	publisher = p;
}

//print the publisher
void Book::show_publisher()
{
	cout << publisher << endl;
}

//return the isbn
int Book::get_isbn()
{
	return isbn;
}

//edit the title
void Book::set_isbn(int i)
{
	isbn = i;
}

//print the isbn
void Book::show_isbn()
{
	cout << isbn << endl;
}


 
