import java.awt.*;
public abstract  class   AbstractPolygon extends Polygon {  

  protected String name;

  public AbstractPolygon(int xp[], int yp[], int sides)
   {
      super(xp, yp, sides);
   }

  public abstract void paint(Graphics g);

   public String toString() {
      return name;
  } 
 } 
