#include <iostream>
#include <fstream>
#include <algorithm>
using namespace std;
int *arr; //instante an array of length 10000
int percentile (double p); //function prototype
int main()
{
	ifstream dataFile("data.txt"); // intantiate an ifstream object to read a file of numbers
	ofstream dataFile2;  //instantiate an ofstream object to write to a file 
	dataFile2.open ("dataSorted.txt"); //open the file
	int index = 1,  //index of array where the number read from the file should be copied to
		number;
	if (!dataFile.eof()) //to read the first line of the file which is number of integers in the file
	{
		dataFile >> number;
		arr = new int[number]();
		arr [0] = number;
	}
	while (!dataFile.eof())  //read the rest of the file and copy the values in the array
	{
		dataFile >> number;
		arr[index] = number;
		index++;
	}
	
	sort (arr + 1, arr + arr[0]); //sort the array
	for (int i=0; i<=arr[0]; i++) //copy the values from the array into a new text file
	{
			dataFile2 << arr[i] << " ";
	}
	dataFile2.close(); //close the file
	cout << "25% percentile: " << percentile (.25) << ".\n" ;  //print 25% percentile to the user
	cout << "50% percentile: " << percentile (.5) << ".\n" ;   //print 50% percentile to the user
	cout << "75% percentile: " << percentile (.75) << ".\n" ;  //print 75% percentile to the user
	system ("PAUSE");
	return 0;
}
int percentile (double p) //takes a pourcentage and compute the percentile
{
	int index = (int)ceil(arr[0]*p);
	return arr[index];
}