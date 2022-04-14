#ifndef UPC_ENTRY
#define UPC_ENTRY
#include <string>
#include<iostream>
#include<vector>
#include<sstream>
#include <cmath>

using namespace std;

struct UPCEntry
{
	long long upc = -1;
	string desc = "";
	UPCEntry(){}
	UPCEntry(string entry)
	{
		vector<string> result;
		stringstream s_stream(entry); //create string stream from the string
		while(s_stream.good()) 
		{
		      	string substr;
      			getline(s_stream, substr, ','); //get first string delimited by comma
      			result.push_back(substr);
   		}
		upc = stoll(result.at(0));
		desc = result.at(1);
	}
	int hash1(int tableSize)
	{
		return upc % tableSize;
	}

	int hash2(int tableSize)
	{
		int result = abs(desc[0] + 27 * desc[1] + 729 * desc[2]) % tableSize;
		return result;
	}
};
#endif
