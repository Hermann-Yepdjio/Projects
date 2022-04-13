
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		UtilQueue q= new UtilQueue();
		for(int i=1; i<31; i++)
			q.q.add(new Problem(i));
		System.out.println(q.q);
		System.out.println(q.extraCredit(5,q.q));

	}

}
