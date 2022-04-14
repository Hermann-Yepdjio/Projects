#include "Buffer.h"

//Constructor
Buffer::Buffer(int b_c)
{
	queue = new Queue<Packet>(b_c);
	capacity = b_c;
}


//Deconstructor
Buffer::~Buffer()
{
	queue->~Queue();
}

//check if buffer is full
bool Buffer::is_full()
{
	if (queue->get_size() == queue->get_capacity())
		return true;
	return false;
}

