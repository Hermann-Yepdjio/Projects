import java.util.*;
/**
   This program demonstrates the order in which
   superclass and subclass constructors are called. 
   
*/
public class ConstDemo
{
   public static void main(String[] args)
   {
      SubClass1 obj = new SubClass1();
   }
}

class SuperClass1
{
   /**
      Constructor
   */

   public SuperClass1()
   {
      System.out.println("This is the " +
               "superclass constructor.");
   }
}

class SubClass1 extends SuperClass1
{
   /**
      Constructor
   */

   public SubClass1()
   {
   // implicitly calls no-arg superclass constructor here
      System.out.println("This is the " +
                 "subclass constructor.");
   }
}

