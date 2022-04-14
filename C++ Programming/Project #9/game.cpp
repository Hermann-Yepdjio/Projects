#include <stdio>
#include <game.h>

	class Player;
	Game::Game(Player &p1, Player &p2) //constructor
	{
		this.p1 = p1;
		this.p2 = p2;   
		this.c_player = p1;  //assign player1 as the current player
		for(int i =0; i<BoardWidth; i++)
		{
			update[i]=0; //fill the update table with 0s
			for(int j=0; j<BoardHeight; j++) //fill the game table with Emptys
				table[j][i]=Empty;
		}                     
	}
	GameState Game::getState() //retuns the state of the game
	{
		int count_p1 = 0;
		int count_p2 = 0; 
		for(int i = 0; i< BoardWidth; i++) //check if a player has aligned 4  pieces in one column
		{
			for (int j = 0; j< BoardHeight; j++)
			{
				if (table[j][i]==Player1)
				{
					count_p1++;
					count_p2 = 0;
				}
				else if ( table[j][i]=Player2)
				{
					count_p1=0;
					count_p2++;
				}
				else
				{
					count_p1=0;
					count_p2=0;
				}
				if (count_p1==4)
					return P1Won;
				if (count_p2==4)
					return P2Won;
			}
		}
		for(int i = 0; i< BoardHeight; i++) //check if a player has aligned 4 pieces in one row
		{
			for (int j = 0; j< Boardwidth; j++)
			{
				if (table[i][j]==Player1)
				{
					count_p1++;
					count_p2 = 0;
				}
				else if(table[i][j]==Player2)
				{
					count_p1=0;
					count_p2++;
				}
				else
				{
					count_p1=0;
					count_p2=0;
				}
				if (count_p1==4)
					return P1Won;
				if (count_p2==4)
					return P2Won;
			}
		}
		for(int i = 0; i< BoardWidth; i++) //check if a player has aligned 4 pieces in the first diagonal
		{
			for (int j = 0; j< BoardHeight; j++)
			{
				int temp = i;
				for(int k=j+1; k<BoardHeight; k++)
				{
					temp++;
					if (temp<BoardWidth)
					{
						if(table[k][temp]==Player1)
						{
							count_p1++;
							count_p2 = 0;
						}
						else if (table[k][temp]=Player2)
						{
							count_p1=0;
							count_p2++;
						}
						else
						{
							count_p1=0;
							count_p2=0;
						}
						if (count_p1==4)
							return P1Won;
						if (count_p2==4)
							return P2Won;
					}	
				}			
			}
		}

		for(int i = BoardWidth-1; i>=0; i--) //check if a player has aligned 4 pieces in the second diagonal
		{
			for (int j = 0; j< BoardHeight; j++)
			{
				int temp = i;
				for(int k=j+1; k<BoardHeight; k++)
				{
					temp--;
					if (temp>=0)
					{
						if(table[k][temp]==Player1)
						{
							count_p1++;
							count_p2 = 0;
						}
						else if (table[k][temp]=Player2)
						{
							count_p1=0;
							count_p2++;
						}
						else
						{
							count_p1=0;
							count_p2=0;
						}
						if (count_p1==4)
							return P1Won;
						if (count_p2==4)
							return P2Won;
					}	
				}			

			}
		}
		bool condition = true;
		for(int i = 0; i<BoardWidth; i++
		{
			for(int j =0; j<BoardHeight; j++)
			{
				if (table[j][i]==Empty)
					condtion = false;
			}
		}
		if (condition == true)
			return Draw;
		if(c_player==p1)
			return TurnP;
		else
			return TurnP2;

		return Draw;


	}
	bool Game::isRunning() //return "true" if the game is still running and "false" if not
	{
		if(getState()=="Draw" || getState()=="P1Won" || getState()=="P2Won")
			return true;
		return false;
	}	
	BoardField Game::operator()(int x, int y) const //return the state of the board at the given coordinate
	{
		if (x<0 || x>BoardHeight || y<0 ||  y>BoardWidth)
			return Empty;
		return table[x][y];
	}
	void Game::nextTurn()//perform next turn and record changes
	{
		int temp = c_player.getNextTurn(this);
		if(update[temp-1]<6 && c_player = p1)
		{
			table[update[temp-1]][temp-1] = Player1;
			c_player = p2;
			update[temp-1]++;  
		}
		else if(update[temp-1]<6 && c_player = p2)
		{
			table[update[temp-1]][temp-1] =   Player2;
			c_player=p1;
			update[temp-1]++;
		}

		
	}


