public class Position 
{
    	protected int row;
        protected char column;
        protected char piece;
            

	/**
         * Initializes this Position object to (0, 0).
         */    	
        public Position () 
	{
        	row = 0;
        	column = 'x';
        	piece ='c';
    } // default constructor


	/**
         * Initializes this Position object to (row, column).
         *
         * @param row the row this Position object has been initialized to.
         * @param column the column this Position object has been initialized to.
         */
    	public Position (String pos) 
    	{
    		char[] Pos=pos.toCharArray();
    		piece= Pos[0];
    		column= Pos[1];
    		row= Character.getNumericValue(Pos[2]);
    	} // constructor


	/**
         * Determines the row of this Position object.
         *
         * @return the row of this Position object.
         */
    	public int getRow () 
    	{
        	return row;
    	} // method getRow\
    	public void setRow(int Row)
    	{
    		this.row= Row;
    	}
    	public String getColRow()
    	{
    		return String.valueOf(column)+String.valueOf(row);
    	}//method setPosition
    	public String getPosition()
    	{
    		return String.valueOf(piece)+String.valueOf(column)+String.valueOf(row);
    	}


	/**
         * Determines the column of this Position object.
         *
         * @return the column of this Position object.
         */    	
        public char getColumn () 
        {
        	return column;
    	} // method getColumn
        public void setColumn(char Column)
        {
        	this.column=Column;
        }
        public char getPiece () 
        {
        	return piece;
    	} // method getPiece
        public void setPiece(char Piece)
        {
        	this.piece=Piece;
        }

} // class Position
