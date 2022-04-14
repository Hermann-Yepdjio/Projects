#include "game.h"
class Player
{
	//Player() = default; //default constructor;
	//~virtual Player() = default ; // virtual destructor;
	virtual int getNextTurn(Game&) const = 0; // pure virtual function because of "=0" which makes the class an abstract class
};

class HumanPlayer: public Player
{
	//HumanPlayer() = default; //default constructor;
	//~virtual HumanPlayer() = default ; // virtual destructor;
	virtual int getNextTurn(Game&) const override;
};
class AiPlayer: public Player
{
	//AiPlayer() = default; //default constructor;
	//~virtual AiPlater() = default ; // virtual destructor;
	virtual int getNextTurn(Game&) const override;
};



