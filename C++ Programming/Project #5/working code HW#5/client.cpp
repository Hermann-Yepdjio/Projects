#include <iostream>
#include <sstream>
#include <fstream>
#include "BST.h"
#include <vector>
#include <time.h>
#include <limits>

using namespace std;

BST tree;
unsigned long long int upc; 
string output;

//parse the csv file content and store it as product objects in a binary search tree
void parse_csv()
{
// File pointer 
	//fstream fin; 

	// Open an existing file 
	ifstream fin("sample.csv"); 

	if (fin.fail())
	{
		cout << "The file does not exists" <<endl;
		exit(-1);
	}
	// Read the Data from the file 
	// as String Vector 
	vector<string> row; 
	string line, word, temp; 

	getline(fin, line); //to read and skip the first line of the csv file that contains the column names

	//read each line of the file and store it in a string variable called line
	while (getline(fin, line)) 
	{ 

		row.clear(); 

		// create a stream of strings from line 
		istringstream s(line); 

		//split the line at comma characters and store each portion in word which is store in vector row
		while (getline(s, word, ','))
		{ 
			row.push_back(word);
		} 
		
		Product p(stoi(row.at(0)), stoll(row.at(1)), stoll(row.at(2)), row.at(3), row.at(4));
		tree.insert(p);
		//cout << stoi(row.at(0)) << " " << stoll(row.at(1)) << " " << stoll(row.at(2)) << " " << row.at(3) << " " << row.at(4) << endl;
		
	}


}

int main()
{
	parse_csv();

	while(1)
	{

		cout << "UPC Code: ";
		cin >> upc;
		while (!cin.good())
		{
    			cin.clear();
    			cin.ignore(numeric_limits<streamsize>::max(), '\n');
			cout << "Wrong type of data provided!!! Please try again. \n\nUPC Code: ";
			cin >> upc;
		}
		clock_t t;
		t = clock();
		output = tree.find(upc);
		t = clock() - t;
		cout << output << endl;
		cout << "Lookup time: " << t <<  " milliseconds\n\n";

	}

    return 0;
}
