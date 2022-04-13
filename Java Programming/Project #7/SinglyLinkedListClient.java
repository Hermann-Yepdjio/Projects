public class SinglyLinkedListClient
{
	public static	SinglyLinkedList<String> sll= new SinglyLinkedList<String>();

	public static void main ( String[] argv)
	{
		sll.addToFront("SA");
		sll.addToFront("SB");
		sll.addToFront("SD");
		sll.addToFront("SE");
		sll.add(2, "SC");
		sll.add(0,"SF");
		sll.add(6,"S2");
		sll.add("S1");
		sll.add("S0");
		for (String str: sll)
			System.out.println(str);

	}
}
