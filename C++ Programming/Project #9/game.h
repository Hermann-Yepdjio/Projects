#include "players.h"
using namespace std;

namespace GameState
{
	enum GameState{TurnP1, TurnP2, P1Won, P2Won, Draw};
};
namespace BoardField
{
	enum BoardField{Empty, Player1, Player2};
};
class Game
{
	public:
		Game(Player &p1, Player &p2); //constructor
		GameState getState(); //retuns the state of the game
		bool isRunning(); //return "yes" if the game is still running and "no" if not
		BoardField operator()(int x, int y) const; //return the state of the board at the given coordinates
		void nextTurn();//perform next turn and record changes

		
		const static int BoardWidth = 7;
		const static int  BoardHeight= 6;
		
	private:
		BoardField table[BoardHeight][BoardWidth]; //to keep track of the state of each boardfield
		int update[BoardWidth]; //too keep track of how much space is used in each column
		Player c_player; //too keep track of the current player
		Player p1 , p2;
		
};
