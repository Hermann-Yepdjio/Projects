import java.util.*;
import java.io.*;
public class CheckMateIn3P3 
{
	protected static CheckMateIn3 CM;
	protected static ArrayList<String> Mate;
	protected static String board;
	public static void main(String[] args)
	{
		display();
	}
	public static void display() 
	{
		
		Mate=new ArrayList<String>();
			try  // to make sure the file exists and contains correct data
			{
				Scanner scan=new Scanner(new File("position.fen"));
				if (scan.hasNext())
					board =scan.next();
				String[] st=board.split("/");
			if (st.length!=8)
					throw new IllegalArgumentException();
				for (String str:st)
				{
					char[] ch=str.toCharArray();
					int count=0;
					for (char ch1: ch)
					{
						if (Character.isDigit(ch1) && (Character.getNumericValue(ch1)<1 || Character.getNumericValue(ch1)>8))
						{
								throw new  IllegalArgumentException();
						}
						else if (!Character.isDigit(ch1) && ch1!='k'&&ch1!='K'&&ch1!='q'&&ch1!='Q'&&ch1!='b'&&ch1!='B'&&ch1!='n'&&ch1!='N'&&ch1!='r'&&ch1!='R'&&ch1!='p'&&ch1!='P')
						{	
							throw new  IllegalArgumentException();
						}
						if (Character.isDigit(ch1))
							count+=Character.getNumericValue(ch1);
						if (!Character.isDigit(ch1))
								count++;
						
					}
					if(count!=8)
						throw new  IllegalArgumentException();
				}
			
			
			try(Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("solutions.txt"), "utf-8"))) 
			{
			String[] rows = board.split("/"); //different positions on each row
			System.out.println("{--------------");
			writer.write("{--------------\n");
			for (String row:rows)
			{
				char[] pos= row.toCharArray();
				for (char ch: pos)
				 {
					 if (Character.isDigit(ch))
					 {
					   for (int i=0; i<Character.getNumericValue(ch); i++)
					   {
						   System.out.print(". ");
						   writer.write(". ");
					   }
					 }
					 else
					 {
						 System.out.print(String.valueOf(ch)+ " ");
						 writer.write(String.valueOf(ch)+ " ");
					 }
				 }
				System.out.println();
				writer.write("\n");
			}
			System.out.println("--------------}");
			writer.write("--------------}\n");
			CM=new CheckMateIn3(board);
		    ArrayList<Position> pos1=CM.getPositions(board);
			Backtracking(CM.getPositions(board), CM.getPossibleMoves(CM.getPositions(board)), 0, "");
			if(Mate.isEmpty())
			{
				System.out.println("No 3-move mates found.");
				writer.write("No 3-move mates found.\n");
			}
			else
			{
				for (String str:Mate)
				{
					writer.write(str+ " #\n");
				}
			}
	      } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}catch (FileNotFoundException fnfe)
	{
		System.out.println("Sorry the file you have specified does not exit. Please try again");
	}
	catch (IllegalArgumentException IAE)
	{
		System.out.println("Sorry the file contains wrong data. please try again ");
	}
			
	}
	public static void Backtracking(ArrayList<Position> positions, ArrayList<Position> possibleMoves, int count, String solution)
	{
			boolean condition=false;
			ArrayList<Position> newPositions=new ArrayList<Position>();
			ArrayList<Position> newPositions1=new ArrayList<Position>();
			int count1=0;
			for (Position pos: possibleMoves) // execute possible moves
			{
				condition = false;
				if(pos.getRow()==0)
				{
					count1++;
				}
				else if( Character.isUpperCase(pos.getPiece())) //white makes a move
				{
					newPositions.clear();
					for (Position pos1: positions) //make a copy of current positions
					{
						newPositions.add(new Position(pos1.getPosition()));
					}
					newPositions.set(count1, new Position(pos.getPosition())); //set new position for a piece that moved
					Iterator<Position> itr= newPositions.iterator();
					while(itr.hasNext()) //check if a black piece was captured
					{
						Position pos2=itr.next();
						if (Character.isLowerCase( pos2.getPiece()) && pos2.getRow()==pos.getRow() && pos2.getColumn()==pos.getColumn())
							itr.remove();
					}
					ArrayList<Position> newPossibleMoves=CM.getPossibleMoves(newPositions); // compute new possible moves 
					if(CM.isBMated(newPositions)) //check if b is mated
					{
						Mate.add(solution+(count+1)+ ". " +(new Position(pos.getPosition())).getPosition());
						condition=true;
						System.out.println(solution+(count+1)+ ". " +(new Position(pos.getPosition())).getPosition()+ " #");
					}
					int count2=0;
					if(!condition)
					{			
						for(Position pos3:newPossibleMoves) //to operate a possible move
						{
							if(pos3.getRow()==0)
								count2++;
							else if(Character.isLowerCase(pos3.getPiece()) && count<2 ) //black moves
							{
								newPositions1.clear();
								for (Position pos1: newPositions) //make a copy of current positions
								{
									newPositions1.add(new Position(pos1.getPosition()));
								}
								newPositions1.set(count2,  new Position(pos3.getPosition())); //set new position for a piece that moved
								Iterator<Position> itr1= newPositions1.iterator();
								while(itr1.hasNext()) //check and eliminate a white piece if it was captured
								{
									Position pos2=itr1.next();
									if (Character.isUpperCase( pos2.getPiece()) && pos2.getRow()==pos3.getRow() && pos2.getColumn()==pos3.getColumn())
										itr1.remove();
								}
								ArrayList<Position> newPossibleMoves1=CM.getPossibleMoves(newPositions1); //compute new possible moves
								if(CM.isBMated2(newPositions1)) //check if b is mated
								{
									Mate.add(solution+" "+(count+1)+ ". " +(new Position(pos.getPosition())).getPosition()+" + " +(new Position(pos3.getPosition())).getPosition());
									System.out.println(solution+" "+(count+1)+ ". " +(new Position(pos.getPosition())).getPosition()+" + " +(new Position(pos3.getPosition())).getPosition()+ " #");
								}
								else // go to next white move(at most 3 white moves)
								{
									Backtracking(newPositions1, newPossibleMoves1, count+1, solution+ " "+(count+1)+ ". " +pos.getPosition()+" + " +pos3.getPosition()+"  ");
									
								}
								
							}
						}
					}
				}
			}

	}
}
