#include "UPCEntry.h"


typedef struct Node
{
	UPCEntry value;
	struct Node* next;
}Node;

struct List
{
	Node* head;
	int size;

	List()
	{
		head = new Node();
		size = 0;
	}
	List(UPCEntry val)
	{
		head = new Node();
		head->value = val;
		size = 1;
	}

	~List()
	{
		while(head->next)
		{
			Node* tmp = head->next;
			head->next = tmp->next;
			delete tmp;
		}

		delete head;
	}

	void insert(UPCEntry val)
	{
		Node* node = new Node();
		node->value = val;
		if(size == 0)
		{
			head = node;
			size = 1;
			return;
		}
		Node* tmp = head;
		head = node;
		head->next = tmp;
		size++;	
	}

	int contains(UPCEntry val)
	{
		Node* tmp = head;
		int counter = 0;
		while(tmp)
		{
			if(tmp->value.upc == val.upc && (tmp->value.desc.compare(val.desc) || tmp->value.desc == val.desc))
				return counter;

			tmp = tmp->next;
			counter++;
		}

		return -1;
	}

};
