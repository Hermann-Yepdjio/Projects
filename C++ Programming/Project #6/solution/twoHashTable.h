#include "position.h"
//#include "UPCEntry.h"
#include "list.h"
//#include <cmath>
#include <iostream>
#include <sstream>
#include <fstream>

using namespace std;

class TwoHashTable
{ 
	public:
	List *table;
	int t_size;
	public:
	TwoHashTable(string filename, int tableSize)
	{
		t_size = tableSize;
		table = new List[t_size]();
		
		parse_csv(filename);
	
	}

	//parse the csv file content and store the containt in the twoHashTable
	void parse_csv(string filename)
	{

		// Open an existing file 
		ifstream fin(filename); 

		if (fin.fail())
		{
			cout << "The file does not exists" <<endl;
			exit(-1);
		}
		// Read the Data from the file 
		string line; 

		getline(fin, line); //to read and skip the first line of the csv file that contains the column names

		//read each line of the file and store it in a string variable called line
		while (getline(fin, line)) 
		{ 
			//cout << line << endl;
			UPCEntry entry(line);
			//cout << "UPC: " << entry.upc << "     Description: " << entry.desc << endl; 
			insert(entry);	
		}


	}
	bool insert(UPCEntry &item) // returns true if successful, false otherwise.
	{
		int hash_1 = item.hash1(t_size);
		int hash_2 = item.hash2(t_size);
		//cout << hash_1 << "  " << hash_2 << endl;
		if(table[hash_1].contains(item) != -1 || table[hash_2].contains(item) != -1)
			return false;
		
		if(table[hash_1].size > table[hash_2].size)
			table[hash_2].insert(item);
		else
			table[hash_1].insert(item);
		
		return true;
	
	}
	Position search(UPCEntry &item) // if not found, return the default position with both indices set as -1
	{
		Position p;
		int hash_1 = item.hash1(t_size);
		int hash_2 = item.hash2(t_size);
		//cout << hash_1 << "   " << hash_2 << endl;
		int pos = table[hash_1].contains(item);
		if (pos == -1)
		{
			pos = table[hash_2].contains(item);
			if (pos == -1)
				return p;
			else
			{
				p.indexInTable = hash_2;
				p.indexInBin = pos;
				return p;
			}
		}
		p.indexInTable = hash_1;
		p.indexInBin = pos;
		return p;
	}

	float getStdDev()
	{
		int *bin_lengths = new int[t_size];
		for(int i = 0; i < t_size; i++)
			bin_lengths[i] = table[i].size;
		return stddev(bin_lengths, t_size);
	}

	private:
	float stddev(int *binLengths, int tableSize)
	{
		float sum = 0;
		for (int i = 0; i < tableSize; i++)
			sum += binLengths[i];
		float avarage = sum / tableSize;
		float dev_sum = 0;
		for (int i = 0; i < tableSize; i++)
		{
			dev_sum = dev_sum + (binLengths[i] - avarage) * (binLengths[i] - avarage);
		}
		float variance = dev_sum / tableSize;
		return sqrt(variance);
	}
};
