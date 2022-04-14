#include "Queue.h"

//constructor
template<class T>
Queue<T>::Queue(int cap)
{
	capacity = cap;
	size = 0;
	queue = new T[cap];
	head = &queue[0];
}

//Destructor
template<class T>
Queue<T>::~Queue()
{
	delete [] queue;
}

//Add something to the queue
template<class T>
int Queue<T>::enqueue(T item)
{
	if(size < capacity)
	{
		queue[size] = item;
		tail = &queue[size];
		size++;
		return 0;
	}
	else
		return -1;

}

//Remove something from the queue and return the element that was removed
template<class T>
T Queue<T>::dequeue()
{
	T temp = queue[0];
	for(int i = 1; i < size; i++)
	       queue[i-1] = queue[i];
	size--;
	tail = &queue[size-1];

	return temp;	
}

//get the size of the queue
template<class T>
int Queue<T>::get_size()
{
	return size;
}

//get the capacity of the queue
template<class T>
int Queue<T>::get_capacity()
{
	return capacity;
}

