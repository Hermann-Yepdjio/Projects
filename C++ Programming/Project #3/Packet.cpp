#include "Packet.h"

//default constructor
Packet::Packet(){}

//constructor
Packet::Packet(int a_t, int p_t)
{
	arrival_time = a_t;
	processing_time = p_t;
}

//get the arrival time
int Packet::get_arrival_time()
{
	return arrival_time;
}

//get the processing time
int Packet::get_processing_time()
{
	return processing_time;
}

//set the processing time
void Packet::set_processing_time(int t)
{
	processing_time = t;
}
