#ifndef BOOK_H
#define BOOK_H

#include <string>
#include <iostream>

using namespace std;

class Book 
{ 
	private: 
		string title, authors, publisher;
	int isbn; 

	public:
	
	//default constructor
	Book();

	//constructor
	Book(string t, int i, string a, string p);

	//another constructor
	Book(int i);

	//return the title
	string get_title();

	//edit the title
	void set_title(string t);

	//print the title
	void show_title();

	//return the authors
        string get_authors();

        //edit the authors
        void set_authors(string a);

        //print the authors
        void show_authors();

	//return the publisher
        string get_publisher();

        //edit the publisher
        void set_publisher(string p);

        //print the publisher
        void show_publisher();

	//return the isbn
        int get_isbn();

        //edit the title
        void set_isbn(int i);

        //print the isbn
        void show_isbn();

	//Definition for Destructor 
	~Book(); 
};

#endif
