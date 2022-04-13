
import java.util.Date;
import java.text.SimpleDateFormat;

public class Prompt {
	// a class to quickly ask for inputs of various data types
	
	public static Integer promptInt(String prompt) {
		System.out.println(prompt);
		while (true) {
			System.out.print("? ");
			String inputString = "";
			try {
				inputString = Application.inputReader.readLine();
				Integer outInt = Integer.parseInt(inputString);
				return outInt;
			}
			catch (Exception e){
				if (inputString.strip().equalsIgnoreCase("") || inputString.strip().equalsIgnoreCase("null")) {  // escape to enter a null value
					return null;
				}
				else {
					System.out.println("Invalid input, try again!");
				}
			}
		}
	}
	
	public static String promptString(String prompt) {
		System.out.println(prompt);
		while (true) {
			System.out.print("? ");
			String inputString = "";
			try {
				inputString = Application.inputReader.readLine();
				String outString = inputString;
				if (inputString.strip().equalsIgnoreCase("") || inputString.strip().equalsIgnoreCase("null")) {
					return null;
				}
				else {
					return outString;
				}
			}
			catch (Exception e){
				System.out.println("Invalid input, try again!");
			}
		}
	}
	
	public static Date promptDate(String prompt) {
		System.out.println(prompt);
		while (true) {
			System.out.print("? ");
			String inputString = "";
			try {
				inputString = Application.inputReader.readLine();
				Date outDate= new SimpleDateFormat("dd/MM/yyyy").parse(inputString);
				return outDate;
			}
			catch (Exception e){
				if (inputString.strip().equalsIgnoreCase("") || inputString.strip().equalsIgnoreCase("null")) {
					return null;
				}
				else {
					System.out.println("Invalid input, try again!");
				}
			}
		}
	}
	
	public static String promptEnum(String prompt, String ... validStrings) {
		System.out.println(prompt);
		while (true) {
			System.out.print("? ");
			String inputString = "";
			try {
				inputString = Application.inputReader.readLine();
				String outString = inputString;
				if (inputString.strip().equalsIgnoreCase("") || inputString.strip().equalsIgnoreCase("null")) {
					return null;
				}
				else {
					for (int i = 0; i < validStrings.length; i++) {
						if (validStrings[i].equalsIgnoreCase(inputString.strip())) {
							return validStrings[i];
						}
					}
					throw new Exception();
				}
			}
			catch (Exception e){
				System.out.println("Invalid input, try again!");
			}
		}
	}
	
}
