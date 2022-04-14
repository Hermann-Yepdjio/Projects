#ifndef BOOK_MANAGER_H
#define BOOK_MANAGER_H

#include "Book.h"

class BookstoreManager
{
	private:
	       Book* book_list; //a dynamic array to hold the books 
	       int capacity, size; //the capacity and the size of the array

	public:

	       //Constructor
	       BookstoreManager();

	       //Destructor
	       ~BookstoreManager();

	       //returns true if the array is empty, otherwise false	
	       void isEmpty();

	       //returns true if the array is full, otherwise false
	       void isFull();

	       //prints the number of books in the array
	       void listSize();

	       //prints the content of the array
	       void print();

	       //insert a new book
	       void insert(Book b);
	
	       //remove a book
	       void remove(Book b);

	       //remove all books with a given publisher
	       void removePublisher(string p);

	       //search for a book
	       void search(Book b);

};

#endif
