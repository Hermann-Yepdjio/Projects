
using namespace std;

class Response
{
	private:
		int start_time, packet_num;

	public:
		//default constructor
		Response();

		//constructor
		Response(int starting_time, int p_num);

		//get starting time()
		int get_start_time();

		//set starting_time
		void set_start_time(int t);

		//get p_num()
		int get_packet_num();
	
};
