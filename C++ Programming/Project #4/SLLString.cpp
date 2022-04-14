#include <string>
#include "SLLString.h"


//using namespace std;

//default constructor
SLLString::SLLString()
{
	head = new Node;
}

//copy constructor taking a C++ string as parameter.
SLLString::SLLString(const string& other)
{

	head = new Node;
	if (other.length() > 0)
	{
		head->value = other.at(0);
		Node *tmp = head;
		for (int i = 1; i < other.length(); i++)
		{
			Node *node = new Node;
			node->value = other.at(i);
		       	tmp->next = node;
			tmp = node;	
		}
	}


}

// destructor
SLLString::~SLLString()
{

	Node *tmp;
	if(head) //if head is not null
	{	
		while(1)
		{
			if (!head->next) //break the loop if we reach the end of the list
				break;
			else //until we reach the end of the list, delete the current node and move head to the next node
			{
				tmp = head; 
				head = head->next;
				delete tmp;
			}
		}

		delete head; //delete the last node as it is not deleted in the while loop
	}
}

//copy constructor taking another SLLString
SLLString::SLLString(const SLLString& other)
{
	head = new Node;
	Node *tmp_1 = head;
	Node *tmp_2;
	if( other.head)
	{
		head->value = other.head->value; //copy the head's value
		tmp_2 = other.head;
		//copy the remaining nodes
		while(tmp_2->next)
		{
			Node *node = new Node;
			node->value = tmp_2->next->value;
			tmp_1->next = node;
			tmp_1 = node;
			tmp_2 = tmp_2->next;
		}
	}
}

// assignment constructor
SLLString& SLLString::operator=(const SLLString& other)
{
	head = other.head;
	return *this;
}

// get length of this string.
int SLLString::length()
{
	int count = 0;
	Node *tmp = head;
	while (tmp && tmp->value) //loop through every node of "this" increase count each time
	{
		count++;
		tmp = tmp->next;
	}

	return count;
}

// concatenation
SLLString& SLLString::operator+= (const SLLString& other)
{

	SLLString *str = new SLLString(other);
	if (!head->value) //if head is null then just assign other to this
		head = str->head;
	else
	{
		Node *tmp = head;
		while(tmp->next) //find the last node of this and change its next member to point to head other.
			tmp = tmp->next;
		
		tmp->next = str->head;
	}

	return *this;
}

//get character at index n.
char& SLLString::operator[](const int n)
{
	Node *tmp = head;
	for(int i = 0; i < n; i++)
	{
		tmp = tmp->next;
	}

	return tmp->value;
}


// find the index of the first occurrence of substring in the current string. Returns -1 if not found
int SLLString::findSubstring(const SLLString& substring) 
{
	

	//SLLString str(substring);
	//cout << str.length() << endl;
	Node *tmp = head;
	int index = 0;
	while(tmp->next) //Loop through all the nodes of the list
	{
	
		//if a match for the first character in substring is found, 
		if (tmp->value == substring.head->value) 		
		{
			Node *tmp_2 = tmp;
			Node *tmp_3 = substring.head;
			while(tmp_3->next) //try to match the values of the following nodes of the list to the remaining characters in substring
			{
				

				tmp_3 = tmp_3->next;
				if(!tmp_2->next)
					return -1;
				tmp_2 = tmp_2->next;

				if(tmp_2->value != tmp_3->value)
				{
					//cout << tmp_2->value << "  " << tmp_3->value << " here_3\n";
					break;
				}
				
			}

			if (tmp_2->value == tmp_3->value)
				return index;
		}
		
		tmp = tmp->next;
		index++;
	}

	return -1;
}

//erase all occurrences of character c from the current string
void SLLString::erase(char c) 
{
	Node *prev = head;
	Node *tmp = head;

	while(head && head->value == c) //delete all occurences of c at the beginning of the list
	{
	      head = head->next;
	      delete tmp; //release the space occupied by the node that is being deleted from the list
	      tmp = head;
	      prev = head;
	}
	
	while(tmp) //delete all node at index > 0 which value equals to c
	{
		tmp = prev->next;
		if (tmp && tmp->value == c)
		{
			prev->next = tmp->next;
			delete tmp; //release the space occupied by the node that is being deleted from the list
		}
		else
			prev = prev->next;
	}
}

ostream& operator<<(ostream& os, const SLLString& str)
{
	Node *tmp = str.head;

	while(tmp) //add elements of the list to osstream
	{
		os << tmp->value;
	       	tmp = tmp->next;
	}	
	
	return os;
}

int main()
{
	SLLString str("Hello world!");
	SLLString newStr; //= new SLLString;

	newStr = str;
	
	SLLString str_2(" CS@BC");
	newStr += str_2; //SLLString(" CS@BC");

	newStr[6] = 'W';

	cout << newStr << endl;

	cout << newStr.length() << endl;

	SLLString str_3("World");


	int loc = newStr.findSubstring(str_3);
	cout << loc << endl;

	newStr.erase('l'); //erase the letter l.
	cout << newStr << endl;

	newStr.erase('C');
	cout << newStr << endl;

	return 0;
}
