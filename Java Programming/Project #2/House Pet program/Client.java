
public class Client {

	public static void main(String[] args) 
	{
		HousePet dog1= new Dog ("Spot", "John", "Meat");
		HousePet cat1= new Cat ("Boris", "Joe", "cockies");
		HousePet chinchilla1= new Chinchilla ("Mickey", "Pat", "fishs");
		System.out.println (dog1.toString());
		System.out.println (cat1.toString());
		System.out.println (chinchilla1.toString());

	}

}
