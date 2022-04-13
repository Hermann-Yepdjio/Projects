import java.util.*;

public class  TriangleDS {  

// we could have use   a two-dimensional ragged array, but we want to 
// emphasize the implementation of a more complex  iterator. 
 private  int[]  a1  = new int[1];
 private  int[]  a2  = new int[2];
 private  int[]  a3  = new int[3];
 private  int[]  a4  = new int[4];

 public Id currIx; // current index

//  constructors
//
 public TriangleDS(){
 currIx = new Id(1,0);
}


// the triangle of numbers is given as array
 public TriangleDS(int [] v){

 int i ;
 int ix =0;

 for( i=0 ; i < 1; ++ i) { a1[i] = v[ix]; ++ix;};
 for( i=0 ; i < 2; ++ i) { a2[i] = v[ix]; ++ix;};
 for( i=0 ; i < 3; ++ i) { a3[i] = v[ix]; ++ix;};
 for( i=0 ; i < 4; ++ i) { a4[i] = v[ix]; ++ix;};

 currIx = new Id(1,0);
    
 }




// main to instantiate an run
public static void main (String[] argv){ 

     int[] initarray = {1,2,2,3,3,3,4,4,4,4};
     TriangleDS di2 =  new TriangleDS(initarray);
     di2.run();

} 


// The storage organization not very simple, displaying is  more or less
// complicated
//

// auxiliary method to display it
public void displayElement(Id x) {

   int j = x.entry;  
   int c = x.array;

   switch (c){ 
     case 1 : System.out.print (a1[j]+" "); break;
     case 2 : System.out.print (a2[j]+" "); break;
     case 3 : System.out.print (a3[j]+" "); break;
     case 4 : System.out.print (a4[j]+" ");
    }
}

// traverse the data structure, after setting the current Id.
//
public void run() {

       // displays from beginning
       Forward it = new Forward();
        while (it.hasNext()) {
              displayElement(it.next());
           }

       System.out.println();

       // From arbitrary element
       currIx = new Id(2,1);  // current index is changed
        while (it.hasNext()) {
              displayElement(it.next());
           }

       System.out.println();

}

/* The iterator. Note that, as an inner class, it has an implicit reference
 * to fields in the Outer class 
*/

public class Forward implements Iterator<Id> {

   protected Id ind;

// auxiliary to display current index
   public void dispc() {
      System.out.println( "Current index " + currIx.array+ " "+ currIx.entry);
     }

//  next()
   public Id next() {
      ind = new Id(currIx); 
      if (ind.array == ind.entry+1)
          currIx =  new Id(ind.array+1, 0);
      else
          currIx = new  Id(ind.array, ind.entry +1);
      return ind;

    };

// hasNext() . resets the index.
 public boolean hasNext() {
      if ( currIx.array == 5 && currIx.entry == 0  ) {
          currIx.array = 1; currIx.entry = 0; //resets
          return false;}
      return true; };

// not needed 
 public void remove() {};

}

// To implement in Lab03
public  class Backward implements Iterator<Id> {
  public Id next() { return new Id();};
  public boolean  hasNext() { return false; };
  public void remove() {};
  }

}

// indices to the data
class Id{
  public int array, entry;
  public Id(Id x){array=x.array; entry=x.entry;}
  public Id(){array=1; entry=0;}
  public Id(int array , int entry){ this.array=array; this.entry=entry;}
}
