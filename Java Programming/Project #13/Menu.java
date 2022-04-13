
import java.util.ArrayList;

public class Menu {
	private String heading;
	private ArrayList<String> options;
	
	public Menu() {
		this.heading = "";
		this.options = new ArrayList<String>();
	}
	
	public Menu(String heading, String ... options) {
		this.heading = heading;
		this.options = new ArrayList<String>();
		for (int i = 0; i < options.length; i++) {
			this.options.add(options[i]);
		}
	}
	
	public void setHeading(String heading) {
		this.heading = heading;
	}
	
	public void addOption(String option) {
		this.options.add(option); 
	}
	
	public int activateMenu() {
		System.out.println(this.heading);
		for (int i = 0; i < this.options.size(); i++) {
			System.out.println(Integer.toString(i + 1) + " - " + options.get(i));
		}
		while(true) {
			System.out.print("? ");  // input prompt
			try {
				int response = Integer.parseInt(Application.inputReader.readLine());
				if (response > 0 && response <= options.size()) {
					return response;
				}
				else {
					throw new Exception();
				}
			}
			catch (Exception e) {
				System.out.println("Invalid input, try again!");
			}
		}
	}

}
