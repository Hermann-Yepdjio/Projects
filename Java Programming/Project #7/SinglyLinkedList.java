import java.util.*;

public  class SinglyLinkedList<E>   extends AbstractList<E> 
{
  protected Entry<E> head;  // reference to the first entry in the list                     
  
  
  /**
   * Initializes this SinglyLinkedList object to be empty, with elements to be of 
   * type E.
   */
  public SinglyLinkedList()
  {
    head = null;
  } // constructor
  
  
  /**
   *  Determines if this SinglyLinkedList object has no elements.
   *
   *  @return true -  if this SinglyLinkedList object has no elements; otherwise,
   *                          false.  
   */
  public boolean isEmpty ()  
  {
    return head == null;
  } // method isEmpty
  
  
  /**
   *  Adds a specified element to the front of this SinglyLinkedList object.
   *
   *  @param element - the element to be appended.   
   *
   */
  public void addToFront (E element) 
  {
    Entry<E> newEntry = new Entry<E>();
    newEntry.element = element;
    newEntry.next = head;
    head = newEntry;     
  } // method addToFront
   public void add (int ix, E element)
   {
	   int count=0;
	   if (ix> size() || ix<0)
		   throw new IllegalArgumentException();
	   if (ix==0)
		   addToFront(element);
	   for (Entry<E> current= head; current != null; current= current.next)
	   {
		count++;
		if (count==ix)
		{
			Entry<E> newEntry= new Entry<E>();
			newEntry.element= element;
			newEntry.next=current.next;
			current.next= newEntry;
		}
	   }
   }
   public boolean add(E value)
   {
	   Entry<E> current= head;
	   Entry<E> newEntry= new Entry<E>();
	   newEntry.element=value;
	   if (newEntry==null)
		   addToFront(value);
	   else
	   {
	  	 for (int i=0; i< size(); i++)
	 	  {
			 if (current.next==null)
				 current.next=newEntry;
			 else
				 current= current.next;


	 	  }
	   }
	  return true;
   }
  
  
  /**
   *  Returns a SinglyLinkedListIterator object to iterate over this
   *  SinglyLinkedList object.
   *  
   *  It must be implemented as it is abstract in superclass AbstractCollection<E> 
   */  
  public Iterator<E> iterator()
  {
    return new SinglyLinkedListIterator();
  } // method iterator
  
  
  /**  
   *  Determines the number of elements in this SinglyLinkedList object.
   *  The worstTime(n) is O(n).
   *
   *  @return the number of elements.
   *  It must be implemented as it is abstract in superclass AbstractCollection<E> 
   */
  public int size() 
  {
    int count = 0;
    
    for (Entry<E> current = head; current != null; current = current.next)
      count++;
    return count;
  } // method size
  
  
  /** 
   *  Determines if this SinglyLinkedList object contains a specified element.
   *  The worstTime(n) is O(n).
   *
   *  @param obj - the specified element being sought.
   *
   *  @return true - if this SinglyLinkedList object contains element; otherwise,
   *                false. 
   *
   */
  public boolean contains (Object obj) 
  {
    if (obj == null)
    {
      for (Entry<E> current = head; current != null; current = current.next)
        if (obj == current.element)
          return true;
    } // if obj == null
    else   
      for (Entry<E> current = head; current != null; current = current.next)
        if (obj.equals (current.element))
          return true;
    return false;
  } // method contains    

  //  get is abstract in ArrayList<E> so it must be implemented 
 public E get(int index)
  {
    throw new UnsupportedOperationException( );
  }


  protected class SinglyLinkedListIterator implements Iterator<E> 
  {
    protected Entry<E> current;
    
    /**
     *  The iterator has been initialized.
     */
    protected SinglyLinkedListIterator() 
    {
       current = head;
    } // constructor
    
    
    /** 
     *  Returns the element this Iterator object was (before this call) 
     *  positioned at, and advances this Iterator object.
     *                    
     *  @return - the element this Iterator object was positioned at.
     *
     *  @throws NoSuchElementException  if this Iterator object was
     *                 not postioned at an element before this call.
     */                            
    public E next() 
    {
        E theElement = current.element;
        current = current.next;
      return theElement;
    } // method next
    
    
    /**
     *  Determines if this Iterator object is positioned at an element in this
     *  Collection.
     *
     *  @return true - if this Iterator object is positioned at an element; 
     *                otherwise, false.                        
     */                   
    public boolean hasNext() 
    {       
      return current != null;
    } // method hasNext
    
    /**
     *  Removes the element returned by the most recent call to next().
     *  The behavior of this Iterator object is unspecified if the underlying 
     *  collection is modified ( while this iteration is in progress) other than 
     *   by calling this remove() method.
     *
     *  @throws IllegalStateException - if next() had not been called before
     *                 this call to remove(), or if there had been an intervening call 
     *                 to remove() between the most recent call to next() and this 
     *                 call.
     **/
    public void remove() 
    { 
      throw new UnsupportedOperationException( ); 
    }
    
  } // class SinglyLinkedListIterator
  
  
  protected class Entry<E>
  {
    protected E element;
    protected Entry<E> next;
    
  } // class Entry
  
} // class SinglyLinkedList
