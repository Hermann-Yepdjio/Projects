#include "BookstoreManager.h"
#include <cmath>


//Constructor
BookstoreManager::BookstoreManager()
{
	capacity = 2;
	size = 0;
	book_list = new Book[capacity];
}

//Destructor
BookstoreManager::~BookstoreManager()
{
	delete[] book_list;
}

//returns true if the array is empty, otherwise false	
void BookstoreManager::isEmpty()
{
	if (size == 0)
		cout << "true" << endl;
	else
		cout << "false" << endl;	
}

//returns true if the array is full, otherwise false
void BookstoreManager::isFull()
{
	if (capacity == size)
		cout << "true" << endl;
	else
		cout << "false" << endl;
}

//prints the number of books in the array
void BookstoreManager::listSize()
{
	cout << size << endl;
}

//prints the content of the array
void BookstoreManager::print()
{
	if(size==0)
		cout << "The bookstore array is empty! Try to add some books." << endl;
	else
	{
		for (int i = 0; i < size; i++)
		{
			cout << book_list[i].get_title() << endl;
			cout << book_list[i].get_authors() << endl;
			cout << book_list[i].get_isbn() << endl;
			cout << book_list[i].get_publisher() << endl;
			cout << endl; // just to make some space so the output is easier to read
		}
	}
}

//insert a new book
void BookstoreManager::insert(Book b)
{
	if (size == capacity)
	{
		capacity = capacity * 2;
		Book* bigger_book_list =  new Book[capacity]; //create a bigger array (twice the capacity of the previous array

		//Copy over all the element of the previous array into the new array
		for (int i = 0; i < size; i++)
			bigger_book_list[i] = book_list[i];

		delete[] book_list; //delete the previous array to release the memory
		book_list = bigger_book_list;
		bigger_book_list = nullptr;

		cout << "The array's size is being doubled due to lack of space. Please wait a moment.....";
	}

	if (size == 0)
	{
		book_list[0] = b;
		size++;
	}
	else
	{

		bool was_inserted = false;
		for (int i = 0; i < size; i++)
		{
			if (book_list[i].get_isbn() >= b.get_isbn())
			{
				//shift by 1 to the right every book that has an isbn > or =. this is to keep the array sorted 
				for (int j = i; j < size; j++) 
					book_list[j + 1] = book_list[j];

				book_list[i] = b; //insert the new book at index i
				size++; //increment size
				was_inserted = true;

				break; //break the loop as there's no need to go any further
			}
		}

		//check if the new book was inserted. if not, insert it at the end of the list
		if (was_inserted == false)
		{
			book_list[size] = b;	
			size++; //increament size
		}

	}
}

//remove a book
void BookstoreManager::remove(Book b)
{
	bool was_deleted = false;
	for (int i = 0; i < size; i++)
	{
		if (book_list[i].get_isbn() == b.get_isbn())
		{
			//shift left by 1 all the books that are to the right of b
			for (int j = i + 1; j < size; j++)
				book_list[j - 1] = book_list[j];
			size--; //decrement the size of the array
			was_deleted = true; //record that the book was deleted
			break; //break the loop as there is no need to go any further if the book was found
		}
	}

	//check if the book was deleted
	if (!was_deleted)
		cout << "Not Found" << endl;

}
	

//remove all books with a given publisher
void BookstoreManager::removePublisher(string p)
{
	bool was_deleted = false;
	for (int i = 0; i < size; i++)
	{
		if (book_list[i].get_publisher() == p)
		{
			remove(book_list[i]); //use the function remove to delete the book
			was_deleted = true; //record that the book was deleted
			i--; // to make sure we do not miss anything since everything to the right of book_list[i] will be shifted left by one
		}
	}

	//check if the book was deleted
	if (!was_deleted)
		cout << "Not Found" << endl;


}

//search for a book using binary search algorithm
void BookstoreManager::search(Book b)
{
	int low_index = 0;
	int high_index = size;
	bool was_found = false;
	int mid_index;
	while (low_index <= high_index)
	{
		mid_index = floor((low_index + high_index) / 2);
		if (book_list[mid_index].get_isbn() == b.get_isbn())
		{
			cout << book_list[mid_index].get_title()<<endl;
			cout << book_list[mid_index].get_authors() << endl;
			cout << book_list[mid_index].get_isbn() << endl;
			cout <<book_list[mid_index].get_publisher() << endl;

			was_found = true;

			break; // break the loop as there's no need to go any further once the book is found
		}

		else if (book_list[mid_index].get_isbn() < b.get_isbn())
			low_index = mid_index + 1;
		else 
			high_index = mid_index - 1;
	}

	//check if the book was found
	if (!was_found)
		cout << "Not Found" << endl;

}


