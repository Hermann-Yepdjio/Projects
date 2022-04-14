#include <iostream> 
#include "Book.h" 
#include "BookstoreManager.h" 

using namespace std; 

int main() 
{      
	BookstoreManager bookstoreManager;      
	
	//prints true if the bookstore is empty     
	bookstoreManager.isEmpty();  

	cout << endl; // just to make some space so the output is easier to read
    	
	//insert 4 books     
	string title, authors, publisher;     
	int isbn;     
	for(int i=0;i<4;i++)
	{         
		cout<<"Enter book title:";   
	  	//cin.ignore(); //needed to prevent getline to stop reading after cout
        	getline(cin, title); //read all the tokens that are entered (cin >> only reads the first token	
		//cin>>title;         
		cout<<"Enter authors:";   
		//cin.ignore(); //needed to prevent getline to stop reading after cout 
                getline(cin, authors);
		//cin>>authors;         
		cout<<"Enter isbn:";   
		cin>>isbn;         
		cout<<"Enter publisher:";      
	        cin.ignore(); //needed to prevent getline to stop reading after cout 
                getline(cin, publisher);	
		//cin>>publisher;         
		Book aBook(title, isbn, authors, publisher);   
	      	
		bookstoreManager.insert(aBook);         

		cout<<endl;    
	}      
	
	//print bookstore     
	bookstoreManager.print();      
	
	//search for books     
	cout<<"Searching…"<<endl;     
	cout<<"ISBN:";  
	cin>>isbn;     
	Book b2(isbn);     
	bookstoreManager.search(b2);      

	cout << endl; // just to make some space so the output is easier to read
	
	//remove a book     
	cout<<"Removing…"<<endl;     
	cout<<"ISBN:"; 
	cin>>isbn;     
	Book b1(isbn);     
	bookstoreManager.remove(b1);    

	cout << endl; // just to make some space so the output is easier to read	
	
	//print bookstore     
	bookstoreManager.print();     

	cout << endl; // just to make some space so the output is easier to read	
	
	//remove books from a particular publisher     
	cout<<"Removing publisher"<<endl;     
	cout<<"Publisher:"; 
	cin.ignore(); //needed to prevent getline to stop reading after cout
        getline(cin, publisher);
	//cin>>publisher;      
	bookstoreManager.removePublisher(publisher);    

	cout << endl; // just to make some space so the output is easier to read	
	
	//print bookstore     
	bookstoreManager.print();       

	cout << endl; // just to make some space so the output is easier to read
	
	//prints the number of books     
	bookstoreManager.listSize(); 

	cout << endl; // just to make some space so the output is easier to read

}
