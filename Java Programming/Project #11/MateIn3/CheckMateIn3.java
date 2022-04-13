import java.util.*;


public class CheckMateIn3 implements Application
{
	protected static String Board;
	protected static final char[] columns = {'a','b','c','d','e','f','g','h'};
	public CheckMateIn3(String Board) //constructor
	{
		this.Board=Board;
	}
	public boolean isOK(Position pos)
	{
		return false;
	}
	public static boolean isOK(Position pos,ArrayList<Position> positions ) //determine if a position is valid or not
	{
		if (pos.getRow()<1 || pos.getRow()>8 || pos.getColumn()<'a' || pos.getColumn()>'h') //check if row and column are valid choices
		{	
			return false;
		}
		for ( Position Pos: positions)
		{
			if(Character.isLowerCase(pos.getPiece()) && Character.isLowerCase(Pos.getPiece()))//check if black 
										//pieces do not overlap
			{
				if( pos.getRow()==Pos.getRow() && pos.getColumn() == Pos.getColumn())
					return false;
			}
			if(Character.isUpperCase(pos.getPiece()) && Character.isUpperCase(Pos.getPiece()))//check if white 
										//pieces do not overlap
			{
				if( pos.getRow()==Pos.getRow() && pos.getColumn() == Pos.getColumn())
					return false;
			}
			if( Pos.getPiece()=='k' || Pos.getPiece()=='K')  //check if not capturing a king
			{
				if( pos.getRow()==Pos.getRow() && pos.getColumn() == Pos.getColumn())
					return false;
			}
			
		}

		return true;
	}
	public static boolean isOK1(Position pos,ArrayList<Position> positions ) //determine if a position is valid or not
	//this function is used only to check if b is mated as it virtually allows white to move to k position
	{
		if (pos.getRow()<1 || pos.getRow()>8 || pos.getColumn()<'a' || pos.getColumn()>'h') //check if row and column are valid choices
		{	
			return false;
		}
		for ( Position Pos: positions)
		{
			if(Character.isLowerCase(pos.getPiece()) && Character.isLowerCase(Pos.getPiece()))//check if black 
										//pieces do not overlap
			{
				if( pos.getRow()==Pos.getRow() && pos.getColumn() == Pos.getColumn())
					return false;
			}
			if(Character.isUpperCase(pos.getPiece()) && Character.isUpperCase(Pos.getPiece()))//check if white 
										//pieces do not overlap
			{
				if( pos.getRow()==Pos.getRow() && pos.getColumn() == Pos.getColumn())
					return false;
			}
			
		}

		return true;
	}


	public ArrayList<Position> getPositions(String board)
	{ 
		ArrayList<Position> positions=  new ArrayList<Position>();// to hold the positions on the board
	       	String[] rows = board.split("/"); //different positions on each row
		int countR=8;
		int countC=1;
		for (String row: rows)
		{
		 char[] pos= row.toCharArray();  //array of chars corresponding to a position or a number of empty spaces
		 for (char ch: pos)
		 {
			 if (Character.isDigit(ch))
			 {
			   countC +=Character.getNumericValue(ch);  //count number of empty space before next position
			 }
			 else if (ch=='p'|| ch=='P' || ch=='r'|| ch=='R' |ch=='n'|| ch=='N' |ch=='b'|| ch=='B' |ch=='q'|| ch=='Q' |ch=='k'|| ch=='K' )
			{	
				positions.add(new Position (String.valueOf(ch)+String.valueOf(columns[countC-1])+countR));
				countC++;  //increment culumn by one each time a position is found

			}
			 else
				 throw new IllegalArgumentException("Unknown piece type"); //throws new exception if piece not 
			 								 // recognized
		 }
		 countR--; //decrement row number
		 countC=1; // reset column number to 0

		}
		return positions;
	}
		
		
	public ArrayList<Position> getPossibleMoves(ArrayList<Position> positions)
	{
		
	  ArrayList<Position> possibleMoves=new ArrayList<Position>(); //to hold all the possible moves at each stage of game
		for (Position pos: positions)
		{
			Position pos1=new Position();
			//Position pos1 = new Position();
			if(pos.getPiece()=='K' || pos.getPiece()=='k') //finds possible moves for kings append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn(pos.getColumn());
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn(pos.getColumn());
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow());
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces

			}
			else if (pos.getPiece()=='p') //finds possible moves for black pawns append arrayList
			{
				
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn(pos.getColumn());
				boolean condition= false;
				for(Position pos2: positions)
				{
					if(Character.isUpperCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn() && pos2.getRow()==pos.getRow()-1)
						condition=true;
					if (Character.isUpperCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()+1 && pos2.getRow()==pos.getRow()-1)
					{
						pos1.setRow(pos.getRow()-1);
						pos1.setColumn((char) (pos.getColumn()+1));
						if (isOK(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));
					}
					if (Character.isUpperCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()-1 && pos2.getRow()==pos.getRow()-1)
					{
						pos1.setRow(pos.getRow()-1);
						pos1.setColumn((char) (pos.getColumn()-1));
						if (isOK(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));			
					}
				}	
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn(pos.getColumn());
				if(!condition)
				{
					if (isOK1(pos1, positions))
						possibleMoves.add(new Position(pos1.getPosition()));
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			else if (pos.getPiece()=='P')  //finds possible moves for white pawns append arrayList
			{
				
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				for(Position pos2: positions)
				{
					if(Character.isLowerCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn() && pos2.getRow()==pos.getRow()+1)
						condition=true;
					if (Character.isLowerCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()+1 && pos2.getRow()==pos.getRow()+1)
					{
						pos1.setRow(pos.getRow()+1);
						pos1.setColumn((char) (pos.getColumn()+1));
						if (isOK(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));

					}
					if (Character.isLowerCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()-1 && pos2.getRow()==pos.getRow()+1)
					{
						pos1.setRow(pos.getRow()+1);
						pos1.setColumn((char) (pos.getColumn()-1));
						if (isOK(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));			
					}
				}
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn(pos.getColumn());
				if(!condition)
				{
					if (isOK1(pos1, positions))
						possibleMoves.add(new Position(pos1.getPosition()));
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			else if (pos.getPiece()=='Q' || pos.getPiece()=='q') //finds possible moves for queens append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow());
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				int count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					if (isOK(pos1, positions))
					{
						
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces

			}
			else if (pos.getPiece()=='R' || pos.getPiece()=='r') //finds possible moves for rooks append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow());
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				int count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()+ count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
					while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()- count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			else if(pos.getPiece()=='N' || pos.getPiece()=='n') //finds possible moves for Knights append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()+2);
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()+2);
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-2);
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-2);
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn((char) (pos.getColumn()+2));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn((char) (pos.getColumn()+2));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn((char) (pos.getColumn()-2));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn((char) (pos.getColumn()-2));
				if (isOK(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces

			}
			else if (pos.getPiece()=='B'||pos.getPiece()=='b' )  //finds possible moves for bishops append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow());
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				int count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK(pos1, positions))
					{
						possibleMoves.add(new Position(pos1.getPosition()));
						count++;
						for(Position pos2: positions)
						{
							if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
								condition=true;
							if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
								condition=true;
						}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			
		}
		return possibleMoves;

	}
	public ArrayList<Position> getPossibleMoves1(ArrayList<Position> positions) //this function is only used to check
			//if b is mated as it is implemented such that a can move to k position
	{
		
	  ArrayList<Position> possibleMoves=new ArrayList<Position>(); //to hold all the possible moves at each stage of game
		for (Position pos: positions)
		{
			Position pos1=new Position();
			//Position pos1 = new Position();
			if(pos.getPiece()=='K' || pos.getPiece()=='k') //finds possible moves for kings append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn(pos.getColumn());
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn(pos.getColumn());
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow());
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces

			}
			else if (pos.getPiece()=='p') //finds possible moves for black pawns and append arrayList
			{
				
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn(pos.getColumn());
				boolean condition=false;
				for(Position pos2: positions)
				{
					if(Character.isUpperCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn() && pos2.getRow()==pos.getRow()-1)
						condition=true;
					if (Character.isUpperCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()+1 && pos2.getRow()==pos.getRow()-1)
					{
						pos1.setRow(pos.getRow()-1);
						pos1.setColumn((char) (pos.getColumn()+1));
						if (isOK1(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));
					}
					if (Character.isUpperCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()-1 && pos2.getRow()==pos.getRow()-1)
					{
						pos1.setRow(pos.getRow()-1);
						pos1.setColumn((char) (pos.getColumn()-1));
						if (isOK1(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));			
					}
				}	
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn(pos.getColumn());
				if(!condition)
				{
					if (isOK1(pos1, positions))
						possibleMoves.add(new Position(pos1.getPosition()));
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			else if (pos.getPiece()=='P')  //finds possible moves for white pawns append arrayList
			{
				
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn(pos.getColumn());
				boolean condition=false;
				for(Position pos2: positions)
				{   if(Character.isLowerCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn() && pos2.getRow()==pos.getRow()+1)
						condition=true;
					if (Character.isLowerCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()+1 && pos2.getRow()==pos.getRow()+1)
					{
						pos1.setRow(pos.getRow()+1);
						pos1.setColumn((char) (pos.getColumn()+1));
						if (isOK1(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));

					}
					if (Character.isLowerCase(pos2.getPiece())&& pos2.getColumn()==pos.getColumn()-1 && pos2.getRow()==pos.getRow()+1)
					{
						pos1.setRow(pos.getRow()+1);
						pos1.setColumn((char) (pos.getColumn()-1));
						if (isOK1(pos1, positions))
							possibleMoves.add(new Position(pos1.getPosition()));			
					}
				}
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn(pos.getColumn());
				if(!condition)
				{
					if (isOK1(pos1, positions))
						possibleMoves.add(new Position(pos1.getPosition()));
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			else if (pos.getPiece()=='Q' || pos.getPiece()=='q') //finds possible moves for queens append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow());
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				int count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					if (isOK1(pos1, positions))
					{
						
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces

			}
			else if (pos.getPiece()=='R' || pos.getPiece()=='r') //finds possible moves for rooks and append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow());
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				int count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()+ count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;
					while (!condition && count<8)
				{
					pos1.setRow(pos.getRow());
					pos1.setColumn((char) (pos.getColumn()- count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			else if(pos.getPiece()=='N' || pos.getPiece()=='n') //finds possible moves for Knights append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow()+2);
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()+2);
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-2);
				pos1.setColumn((char) (pos.getColumn()+1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-2);
				pos1.setColumn((char) (pos.getColumn()-1));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn((char) (pos.getColumn()+2));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn((char) (pos.getColumn()+2));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()-1);
				pos1.setColumn((char) (pos.getColumn()-2));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				pos1.setRow(pos.getRow()+1);
				pos1.setColumn((char) (pos.getColumn()-2));
				if (isOK1(pos1, positions))
					possibleMoves.add(new Position(pos1.getPosition()));
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces

			}
			else if (pos.getPiece()=='B'||pos.getPiece()=='b' )  //finds possible moves for bishops append arrayList
			{
				pos1.setPiece(pos.getPiece());
				pos1.setRow(pos.getRow());
				pos1.setColumn(pos.getColumn());
				boolean condition = false;
				int count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK1(pos1, positions))
					{
						possibleMoves.add(new Position(pos1.getPosition()));
						count++;
						for(Position pos2: positions)
						{
							if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
								condition=true;
							if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
								condition=true;
						}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()+count);
					pos1.setColumn((char) (pos.getColumn()-count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				condition = false;
				count=1;

				while (!condition && count<8)
				{
					pos1.setRow(pos.getRow()-count);
					pos1.setColumn((char) (pos.getColumn()+count));
					if (isOK1(pos1, positions))
					{
							possibleMoves.add(new Position(pos1.getPosition()));
							count++;
							for(Position pos2: positions)
							{
								if (Character.isLowerCase(pos2.getPiece()) && Character.isUpperCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
								if (Character.isUpperCase(pos2.getPiece()) && Character.isLowerCase(pos1.getPiece())&& pos2.getColumn()==pos1.getColumn()&&pos2.getRow()==pos1.getRow())
									condition=true;
							}
					}
					else
						condition=true;
				}
				possibleMoves.add(new Position()); //just to difference possibles moves for similar pieces
			}
			
		}
		return possibleMoves;

	}
	public boolean isBMated(ArrayList<Position> positions ) //determine if black is mated or not (after white played)
	{
		ArrayList<Position> possibleMoves = getPossibleMoves1(positions);
		ArrayList<Position> possibleMoves1 = getPossibleMoves(positions);
		boolean condition=true;
		Position kPosition=new Position();
		for (Position pos:positions)
		{
			if (pos.getPiece()=='k')
			{
				kPosition= new Position(pos.getPosition());
			}
		}
		ArrayList<String> kPossibleMoves=new ArrayList<String>();
		ArrayList<String> whitePossibleMoves=new ArrayList<String>();
		ArrayList<String> othersPossibleMoves=new ArrayList<String>();
		for(Position pos: possibleMoves)
		{
			if(pos.getPiece()=='k')
			{
				kPossibleMoves.add(new String(pos.getColumn()+String.valueOf(pos.getRow())));
				othersPossibleMoves.add(new String(pos.getColumn()+String.valueOf(pos.getRow())));
			}
			else if(Character.isUpperCase(pos.getPiece()))
				whitePossibleMoves.add(new String(pos.getColumn()+String.valueOf(pos.getRow())));
			else if(pos.getRow()!=0)
				othersPossibleMoves.add(new String(pos.getColumn()+String.valueOf(pos.getRow())));
		}
		for (String pos1:kPossibleMoves)
		{
			if(!whitePossibleMoves.contains(pos1))
			{
				ArrayList<Position> newPositions= new ArrayList<Position>();
				for (Position pos2:positions)
					newPositions.add(new Position(pos2.getPosition()));
				Iterator<Position> itr2= newPositions.iterator();
				while (itr2.hasNext())
				{
					Position pos=itr2.next();
					if (pos.getPiece()=='k')
						itr2.remove();
				}
				newPositions.add(new Position('k'+pos1));
				if (!isBMated2(newPositions))
					return false;
			}
		}
		if(!whitePossibleMoves.contains(String.valueOf(kPosition.getColumn())+kPosition.getRow()))
			condition=false;
		Iterator<Position> itr=positions.iterator();
		ArrayList<Position> wInDanger=new ArrayList<Position>();
		while(itr.hasNext())
		{
			Position pos=itr.next();
			if(othersPossibleMoves.contains(String.valueOf(pos.getColumn())+pos.getRow())&& Character.isUpperCase(pos.getPiece()))
			{
				wInDanger.add(new Position(pos.getPosition()));
			}
		}
		for(Position pos: wInDanger)
		{
			ArrayList<Position> positions1= new ArrayList<Position>();
			
			int count=0;
			for (Position pos1:possibleMoves1)
			{
				if (pos1.getRow()==0)
				{
					count++;
				}
				if(Character.isLowerCase(pos1.getPiece()) &&  pos.getColRow().equals(pos1.getColRow()))
				{
					for (Position pos2:positions)
					{
						positions1.add(new Position(pos2.getPosition()));
					}
					positions1.set(count, new Position(pos1.getPosition()));
					Iterator<Position> itr1= positions1.iterator();
					while (itr1.hasNext())
					{
						Position p=itr1.next();
						if (p.getPosition().equals(pos.getPosition()))
							itr1.remove();
					}
					if(!isBMated2(positions1))
						return false;
					positions1.clear();
					
				}
			}
		}
		
		return condition;
	}
	
	public boolean isBMated2(ArrayList<Position> positions ) //(find out if black made a wrong move and is therefore mated)
	{
		ArrayList<Position> possibleMoves = getPossibleMoves1(positions);
		boolean condition=true;
		Position kPosition=new Position();
		for (Position pos:positions) //gets current position of k
		{
			if (pos.getPiece()=='k')
			{
				kPosition= new Position(pos.getPosition());
			}
		}
		ArrayList<String> whitePossibleMoves=new ArrayList<String>();
		for(Position pos: possibleMoves) //gets all possible white moves
		{
			if(Character.isUpperCase(pos.getPiece()))
				whitePossibleMoves.add(new String(pos.getColumn()+String.valueOf(pos.getRow())));
		}
		if(!whitePossibleMoves.contains(String.valueOf(kPosition.getColumn())+kPosition.getRow())) //check if white can move to k position
		{
			condition=false;
		}
		return condition;
		
	}

	
	public void markAsPossible(Position pos) 
	{
		
	}

	public boolean isGoal(Position pos) 
	{
		return false;
	}

	public void markAsDeadEnd(Position pos) 
	{
		
	}

	public Iterator<Position> iterator(Position pos) 
	{
		return null;
	}
}
