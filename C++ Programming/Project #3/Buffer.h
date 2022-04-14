#include "Packet.h"
#include "Queue.h"


class Buffer
{

	private:		
		int capacity;

	public:
		Queue<Packet> *queue;
		//Contructor
		Buffer(int b_c);

		//Destructor
		~Buffer();

		//check if buffer is full
		bool is_full();
};
