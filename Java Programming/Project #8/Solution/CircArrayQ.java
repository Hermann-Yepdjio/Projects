import java.util.*;
class CircArrayQ   {

  protected final int CAPACITY=9; 
  protected int [] array; 

  protected int front=0; 
  protected int back=0; 

CircArrayQ()
{
   array = new int[CAPACITY];
}
CircArrayQ(int capacity)
{
	array=new int[capacity+1];
}

// parameter constructor for lab 

public  boolean isEmpty() {
   return ( front == back) ;
 }

void  enqueue ( int e) {
   int  newBack = (back + 1) % CAPACITY;

   if (newBack != front) {    // queue isn't full
      array[back] = e;
      back = newBack;
   }
   else {
      System.out.print(" *** Queue full ***   Terminating.\n");
      System.exit(1);
   }

}



// front   to be implemented in Lab
public   int front() 
{
 return array[front];
}


// dequeue to be implemented in Lab
public void dequeue() 
{ 
	if(front!= back)
		front++;
	else
		System.out.println("The list is empty");
 }

public int size() 
{
  return back-front;
}


}

