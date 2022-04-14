#include <iostream>
#include "Response.h"
#include "Buffer.h"
#include <vector>
#include <string>
#include <sstream>
using namespace std;

//read the packets
vector<Packet> readPackets()
{
	vector<Packet> requests;
	int arr_time, proc_time;
	string str;
	while(getline(cin, str))
	{
		if(str.empty())
			break;
		istringstream ss(str);
		ss >> arr_time >> proc_time;
		Packet p(arr_time, proc_time);
		requests.push_back(p);
	}

	return requests;
}

//process the packets
vector<Response> processPackets(vector<Packet> requests, Buffer *buffer)
{
	int time_elapsed = 0;
	int index = 0; //the index of the package currently being analyzed
	vector<Response> responses;
	
	//The processor is idle
	if(requests.size() == 0)
		cout << -2 << endl;


	//iterate through the requests
	for(int i = 0; i < requests.size(); i++)
	{
		//add requests to the buffer if their arrival time = actual time
		while(i < requests.size() && requests.at(i).get_arrival_time() <= time_elapsed && !buffer->is_full())
		{
			buffer->queue->enqueue(requests.at(i));
			Response r(-2, i);
                        responses.push_back(r);
			i++;
		}
		
		//if buffer is full drop all the packets that have already arrived
		while(i < requests.size() && requests.at(i).get_arrival_time() <= time_elapsed && buffer->is_full())
		{
			Response r(-1, i);
			responses.push_back(r);	
			i++;
		}

		//if the arrival time of the next packet in the list is not actual
		if(i < requests.size() && requests.at(i).get_arrival_time() > time_elapsed)
		{

			//start processing the packet at the head of the buffer until another packet arrives and needs to be added to the buffer
			while(requests.at(i).get_arrival_time() > time_elapsed)
			{
				//if there are packets in the buffer, start processing the one at the head of the queue
				if(buffer->queue->get_size()>0)
				{	
					buffer->queue->head->set_processing_time(buffer->queue->head->get_processing_time() - 1);
					time_elapsed++;

					if(buffer->queue->head->get_processing_time() == 0)
					{
						buffer->queue->dequeue();
						for(int j = 0; j < responses.size(); j++)
						{
							if(responses.at(j).get_start_time() == -2)
							{
								responses.at(j).set_start_time(time_elapsed - requests.at(responses.at(j).get_packet_num()).get_processing_time() );
								break;
							}
						}

					}

				}
				else //if the buffer is empty just increment the timer
					time_elapsed++;
			}

			i--; //otherwise packet at index i will be skipped next time the for loop runs
		}
		else //if all the requests are already in the queue
		{
			while(buffer->queue->get_size() > 0)
			{
				if(buffer->queue->head->get_processing_time() > 0)
				{
					buffer->queue->head->set_processing_time(buffer->queue->head->get_processing_time() - 1);
					time_elapsed++;
				}

				if(buffer->queue->head->get_processing_time() == 0)
				{
					buffer->queue->dequeue();
					for(int j = 0; j < responses.size(); j++)
					{
						if(responses.at(j).get_start_time() == -2)
						{
							responses.at(j).set_start_time(time_elapsed - requests.at(responses.at(j).get_packet_num()).get_processing_time() );
							break;
						}
					}

				}

					
			}
		}
	}

	return responses;

}

void printResponses(vector<Response> responses)
{

	for(int i = 1; i < responses.size(); i++)
	{
		for(int j = i; j >0; j--)
		{
			if(responses.at(j).get_packet_num() >= responses.at(j-1).get_packet_num())
				break;
			else
			{
				Response tmp = responses.at(j);
				responses.at(j) = responses.at(j-1);
				responses.at(j-1) = tmp;
			}
		}
	}

	//print the responses
	for(int i = 0; i<responses.size(); i++)
		cout << responses.at(i).get_start_time() << endl; 
}

int main() 
{
	int bufferSize, num_packets;
	cin >> bufferSize >> num_packets;
	cin.ignore();
	vector<Packet> requests = readPackets(); //read packets from user

	//create buffer with the given size
	Buffer buffer(bufferSize);
	//process the packets
	vector<Response> responses = processPackets(requests, &buffer);
	//print responses
	printResponses(responses);
	return 0;
	

}	
