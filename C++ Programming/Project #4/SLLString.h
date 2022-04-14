#include <string.h>
#include <iostream>
using namespace std;

typedef struct Node
{
	char value;
	struct Node* next;
}Node;

class SLLString
{
	private:
	       	Node* head;

	public:

	SLLString(); //Default constructor
	SLLString(const string& other); //copy constructor taking a C++ string as parameter.
	~SLLString(); // destructor
	SLLString(const SLLString& other); //copy constructor taking another SLLString
	SLLString& operator=(const SLLString& other); // assignment constructor
	int length(); // get length of this string.
	SLLString& operator+= (const SLLString& other); // concatenation
	char& operator[](const int n); //get character at index n.
	int findSubstring(const SLLString& substring); // find the index of the first occurrence of substring in the current string. Returns -1 if not found
	void erase(char c); //erase all occurrences of character c from the current string
	friend ostream& operator<<(ostream& os, const SLLString& str);

};
