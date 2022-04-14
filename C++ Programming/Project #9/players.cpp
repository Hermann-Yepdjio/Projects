#include <players.h>
#include <iostream>
using namespace std;

	class Game;

	//int Player::getNextTurn(Game &game) const override; // pure virtual function because of "=0" which makes the class an abstract class


	int HumanPlayer::getNextTurn(Game &game) const  //HumanPlayer class getNextTurn() implementation
	{
		int temp = 0;
		bool condition = false;
		while(!condition) //make sure the user inputs a valid value
		{
			cout<<"Please type an integer between 1 and 7 and press enter: " << endl;
			cin>>temp;
			if(temp >1 && temp<=7)
				condition = true;
		}
		return temp;
		
	}


	int AiPlayer:: getNextTurn(Game &game) const  //aiplayer class getnextturn() implementation

	{
		int temp = 1 + rand()%7;
		cout<< rand<<"/n";
		return temp;	
	}



