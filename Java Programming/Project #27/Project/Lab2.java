import java.util.*; 
/**
* 
*@author Hermann
*@since 1-15-17
*Lab2.java
*
*Description: A Run-Time Estimate Of The Average Height Of A Binary Search Tree
*
**/
public class Lab2
{
	final static int numSample=20;
	public static void main (String[] arg)
	{
		run(); //execute the run method
	}
	public static void run()
	{
		System.out.println("Average the heights of 20 random trees\n");
		System.out.println ("  n      |   ratio of average height to log2n");
		for (int i=1000; i<=16000; i=i*2) //vary the sizes of the binary search trees
		{
			int sumHeights=0; 
			for(int j=0; j<numSample; j++)  //execute 20 trials for each size
			{
				BinarySearchTree<Double> bst= new BinarySearchTree<Double>(); //instantiate a new binary search tree
				for (int k=0; k<i; k++)  //add n random doubles in a binary search tree
				{
					double temp= Math.random()*1000000 + 1;  //pick a random number between 0 and 1000000
					while (bst.contains(temp))  //check if the number is not already present in the tree
						temp= Math.random()*1000000 + 1;

					bst.add(temp);		
				}
				sumHeights+= bst.height();
			}
			double avgHeight=sumHeights/numSample;  //compute the average height for 20 trees of size n
			double ratio = avgHeight/(Math.log(i)/Math.log(2));  //compute the ratio average height to log2(size of tree)
			if (i!=16000)
				System.out.println (i + "     |   " + ratio);
			else  
				System.out.println (i + "    |   " + ratio);
	
		}
	}
}
