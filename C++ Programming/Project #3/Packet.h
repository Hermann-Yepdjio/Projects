using namespace std;

class Packet
{
	private:
		int arrival_time, processing_time;

	public:
		//default constructor
		Packet();

		//constructor
		Packet(int a_t, int p_t);

		//get the arrival time
		int get_arrival_time();

		//get the processing time
		int get_processing_time();

		//set the processing time
		void set_processing_time(int t);
};
