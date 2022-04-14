#include "Response.h"

//default constructor
Response::Response(){}

//constructor
Response::Response(int starting_time, int p_num)
{
	start_time = starting_time;
	packet_num = p_num;
}

//get starting time()
int Response:: get_start_time()
{
	return start_time;
}

//set starting time
void Response::set_start_time(int t)
{
	start_time = t;
}


//get p_num()
int Response:: get_packet_num()
{
	return packet_num;
}
