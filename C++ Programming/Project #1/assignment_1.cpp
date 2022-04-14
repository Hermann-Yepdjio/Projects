#include <iostream>
#include <fstream>
#include <string>

using namespace std;

#define size 300000  //constant for the size of the array of contact

//Contact Object
class Contact
{ 
	// Access specifier 
	private: 

	// Data Members 
	string name; //assuming that each name has a single part
       	long int number; 

	public:

	//Default Constructor
	Contact()
	{
	}

	//Contructor
	Contact(string n, long int num)
	{
	    name = n;
	    number = num;
	}
	
	//getter for the name
	string get_name()
	{
		return name;
	}
	
	//getter for the phone number
	long int get_num()
	{
		return number;
	}

	// Member Functions() 
	
	string toString()
	{
		return name + " " +  to_string(number);
	}
};


//Read the input text file, create contacts using the info contained in the file and load the contacts into an array
int read_file(Contact* contacts)
{
	string first_name, last_name;
	long int number;
	int count = 0; //To know how many entries are in the array.
	ifstream file;
	file.open("phonebook_2.txt"); //open the file
	while(file)
	{
		file>>first_name>>last_name>>number;
		contacts[count] = Contact(first_name + " " + last_name, number); 
		if (file)
		{
			count++;
			cout << count << endl;
		}
	}
	file.close(); //close the file
	return count;
}


//List all the contacts in the array
void list(Contact* contacts, int count)
{
	for (int i = 0; i < count; i++)
	{
		cout << contacts[i].toString() << endl;
	}
}

//add a new contact to the array
int add(Contact* contacts, int count)
{
	string name, first_name, last_name;
	long int number;
	cout << "Enter name: ";
	cin.ignore(); //needed to prevent getline to stop reading after cout
	getline(cin, name); //read all the tokens that are entered (cin >> only reads the first token)
	cout << "Enter Phone: ";
	cin >> number;
	contacts[count] = Contact(name, number);


	return count + 1;
	
}

// remove a contact from the array
int remove(Contact* contacts, int count)
{
	string name;
	int new_count = count; // to keep track of how many contact are in the array in case the same name exits multiple times and we need to delete all the instances
        cout << "Enter name: ";
        cin.ignore(); //needed to prevent getline to stop reading after cout
        getline(cin,  name); //read all the tokens that are entered (cin >> only reads the first token)
	int i = 0;
	while (i < new_count)
	{
		if (contacts[i].get_name() == name)
		{
			for (int j = i; j < new_count - 1; j++)
                        	contacts[j] = contacts[j + 1];
			new_count--;

		}
		else
			i++;
	}

	return  new_count;
}


//Search for a contact in the array
void search(Contact* contacts, int count)
{
	string name;
	bool found = false;
	cout << "Enter name: ";
	cin.ignore(); //needed to prevent getline to stop reading after cout
        getline(cin, name); //read all the tokens that are entered (cin >> only reads the first token
	for (int i = 0; i < count; i++)
	{
		if ( contacts[i].get_name() == name)
		{
			found = true;
			cout << "Phone Number: " << contacts[i].get_num() << endl;
		}
	}
	if (!found)
		cout << "No contact found for this name" << endl;
}


//To handle the communication with the user. Can also be inserted in the main function ( I just didn't want the main function to be too long)
int main()
{
	Contact* contacts = new Contact[size];
        int count = read_file(contacts);
	cout << "\n\n ***MY PHONE BOOK APPLICATION***\n Please choose an operation:" << endl;
	char ans;
	while(1)
	{
		cout << "A(Add) | S (Search) | D(Delete) |L(List) |Q(Quit): ";
		cin >> ans;
		if (ans == 'A')
			count = add(contacts, count);
		else if (ans == 'S')
			search(contacts, count);
		else if (ans == 'L')
			list(contacts, count);
		else if (ans == 'D')
			count = remove(contacts, count);
		else if (ans == 'Q')
			break;

	}

	delete[] contacts;

	return 0;
}

