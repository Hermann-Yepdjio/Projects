
// Make sure to start an Eclipse workspace in a folder outside of the InternationalStudentProgram folder created by your git clone
// Also make sure to add the external JAR file for mysql-connector from the Project>Properties>Libraries>Classpath menu

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

@SuppressWarnings("unused")
public class Application {

	protected static Connection conn;
	protected static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));  // application global in-stream; access with Application.inputReader for readline() calls.  Do not close.
	
	public static void mainMenu() {
		/**
		 * Primary menu function
		 */
		
		// TODO: ask for selected option number; store to variable "selection"; use Scanner or Buffered Reader (see Menu for a working Buffered Reader)
		//int selection;
		
    	System.out.print("Please enter your option number: ");
    	int selection= Integer.parseInt(inputReader.readLine());
		
		// TODO: ask for username and password for DB, store to username and password variables
		String username = "", password = "";
        String url = "jdbc:mysql://localhost:3306/International_Student_Program?serverTimezone=UTC&useSSL=TRUE";

    	System.out.print("Please enter your username and password for DB: ");
    	username = inputReader.readLine();
        password = inputReader.readLine();
        conn = DriverManager.getConnection(url, username, password);
		
		
		// initialize DB connection   
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Make sure your schema name matches with the one below, or your code will not execute.
			String schemaName = "InternationalStudentProgram";
			 url = "jdbc:mysql://localhost:3306/" + schemaName + "?serverTimezone=UTC&useSSL=TRUE";
			conn = DriverManager.getConnection(url, username, password);
			
			// TODO: insert a switch statement based on "selection" integer to navigate to a new menu.  nested switches with multiple
			
			/* Use Menu objects with appropriate option input strings for the various menu's and submenus needed, 
			 * since that will show a good use of OO techniques.  Use the integer output of the menu object to activate
			 * if - else cases or a switch statement to navigate to new menu objects or call methods in other classes.
			 * You should probably compartmentalize these switches into their own functions to allow for the nesting of switch
			 * Statements (see example idea)
			 *
			 * The menus we need are as follows:
			 * 
			 * Main Menu
			 * 
			 * Update Menu: Scenario 1, option 4 in main menu
			 *   Insert Sub-Menu include a switch with all four sub-option methods defined in Updates class
			 *   
			 *  Current Student Menu: Scenario 2, option 1 in main menu
			 *    Academic Information Sub-Menu; include a switch with the appropriate two methods defined in CurrentStudents class
			 *    Contact Information Sub-Menu; include a switch with the appropriate three methods defined in CurrentStudents class
			 *    Visa Status Sub-Menu; include a switch with the appropriate two methods defined in CurrentStudents class
			 *    
			 *  International Rules & Laws Menu: Scenario 3, option 2 in main menu
			 *  
			 *  All other menu's display something like "work in progress" and include a single valid option to go back to the previous menu.
			 *  
			 *  All menu's have the option to go back to the previous menu.
			 */
			public static void exampleMenu() {
				/**
				 * An example menu function to show a good way to handle switch statements for each menu and sub menu
				 * Delete this before final submission
				 */
				String testhead = "Welcome to the\n" + 
						"International Student Information System";
				String testoption1 = "Another Menu";
				String testoption2 = "Example Function";
				String testoption3 = "Back to Main Menu";
				Menu exMenu = new Menu(testhead, testoption1, testoption2, testoption3);  // initialize menu as an object
			    selection = exMenu.activateMenu();  // display menu and get input
				
				 boolean done, done1 = false;
			     do {
				  switch (selection) {
					case 4: 
						System.out.print("Enter User Id: "+ readEntry("userid : "));
						System.out.print("Enter password: "+ readEntry("password: "));
						
						while(((readEntry("userid : ")).compareTo("Admin")!=0)||((readEntry("password : ")).compareTo("padmin")!=0) )
						{ 
							if(((readEntry("userid : ")).compareTo("back")==0)) {
								exampleMenu();
							}
							System.out.print("Your admin username or password is wrong plese try again! or enter back to go back to the main screen");	
							System.out.print("Enter User Id: "+ readEntry("userid : "));
							System.out.print("Enter password: "+ readEntry("password: "));
						
						}
						do {
						        System.out.println("(a)  Insert new information");
						        System.out.println("(b)  Delete some information ");
						        System.out.println("(c)  Update current information");
						        
						        System.out.print("Please enter your option: ");
		                        String ch = readLine();
						 
						        switch(ch.charAt(0))
						       {
						 
						            case 'a': 
						            	    System.out.println("Add new student information");
									        System.out.println("Add new course information ");
									        System.out.println("Add new department information");
									        System.out.print("Register a student in a course");
						            	
						            	
                                      break;
                                    case 'b':
                                    	    System.out.println("Delete specific course information");
									        System.out.println("Delete department information ");
									        System.out.println("Withdraw a student from a course");
                                    	    System.out.println("operation in progress");
								            
                                      break;
                                    case 'c':
                                    	    System.out.println("Update specific student information");
									        System.out.println("Update specific course information ");
									        System.out.println("Update department information");
                                 	        System.out.println("Update a student Course registration");
                                    	    System.out.println("operation in progress");
                                     break;
                      
                                    case 'q': done = true;
                                      break;
                                    default:
                                     System.out.println(" Not a valid option ");
						 
						        } 
						      }while(!done1);
				  
						                                                       // return AnotherMenu(); unimplemented menu function; returns void
						break;
				       
					case 1:
						// printResultSet(ExampleClass.ExampleFunction(conn)); unimplemented object method to execute SQL, returning a ResultSet
						// return
						
					            	System.out.println("(1)  Academic information");
						            System.out.println("(2)  Contact information");
					             	System.out.println("(3)  Visa Status");
						
					              	System.out.print("Please enter your option: ");
                                    int choice = sc.nextInt();
                                    switch(choice) {
                                                 
                                    case 1:
                                    	System.out.print("Educational history for a student: ");
                                    	System.out.print("Collage Students information: ");
                                    	System.out.print("Please enter your option: ");
                                    	  String choice2 = readLine();
                                    	if(choice2.charAt(0)== 'A')
                                    	{
                                    		System.out.print("your student ID is: ");
                                    		
                                    	}else if(choice2.charAt(0)== 'B')
                                    	{
                                    		System.out.print("your college name is: ");
                                    	}
                                    	break;
                                    case 2:
                                    	System.out.print("(A)  All student contact details ");
                                    	System.out.print("(B)  Student from a Country ");
                                    	System.out.print("(C)  Working students contact information ");
                                    	
                                    	System.out.print("Please enter your option: ");
                                    	String choice3 = readLine()
                                    	
                                    	if(choice3.charAt(0)== 'A')
                                    	{
                                    		System.out.print("social security number, name, address, major department, and college: ");
                                    	}else  if(choice3.charAt(0)== 'B')
                                    	{
                                    		System.out.print("name of a country and returns a list consisting of the social security numbers, names and addresses  ");
                                    	} else if(choice3.charAt(0)== 'c')
                                    	{
                                    		
                                    	}
                                    	break;
                                    case 3:
                                    	System.out.print("(A)  Visa Status Report ");
                                    	System.out.print("(B)  F-1 visa ");
                                    	System.out.print("Please enter your option: ");
                                    	String choice4 = readLine();
                                    	if(choice4.charAt(0)== 'A')
                                    	{
                                    		System.out.print("It should Report the following information about the visa\n" + 
                                    				"status of each international studen ");
                                    	}else  if(choice4.charAt(0)== 'B')
                                    	{
                                    		System.out.print("List the social security numbers, names, and nationalities of the\n" + 
                                    				"undergraduate students holding a F-1 visa ");
                                    	}
                                      
                                    break;
                                    }
                                    
                                    
					            	break;
					case 3:
						return;  // called from main menu, so it will return there after exiting
				}
			  }	while (!done);
		   }   
			
			
			conn.close();  // close the connection at the end of the program
			inputReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void exampleMenu() {
		/**
		 * An example menu function to show a good way to handle switch statements for each menu and sub menu
		 * Delete this before final submission
		 */
		String testhead = "*** Cool Menu Heading ***";
		String testoption1 = "Another Menu";
		String testoption2 = "Example Function";
		String testoption3 = "Back to Main Menu";
		Menu exMenu = new Menu(testhead, testoption1, testoption2, testoption3);  // initialize menu as an object
		int selection = exMenu.activateMenu();  // display menu and get input
		switch (selection) {
			case 1: 
				// return AnotherMenu(); unimplemented menu function; returns void
				break;
			case 2:
				// printResultSet(ExampleClass.ExampleFunction(conn)); unimplemented object method to execute SQL, returning a ResultSet
				// return
				break;
			case 3:
				return;  // called from main menu, so it will return there after exiting
		}
	}
	
	public static void printResultSet(ResultSet res) {
		/**
		 * Helper method that prints each row in a given ResultSet
		 */
		try {
			int columnCount = res.getMetaData().getColumnCount();
			while(res.next()) {
				StringBuilder rowStringBuilder = new StringBuilder();
				for (int i = 1; i <= columnCount; i++) {
					rowStringBuilder.append(res.getString(i));
					rowStringBuilder.append(" ");
				}
				System.out.println(rowStringBuilder.toString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void debug() {
		// TODO: Delete this method before submitting assignment
		// A method used for testing stuff.  Feel free to modify and comment into main method to test your own stuff without using Junit or other test files.
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String schemaName = "International_Student_Program";
			String url = "jdbc:mysql://localhost:3306/" + schemaName + "?serverTimezone=UTC&useSSL=TRUE";
			Updates.InsertStudent(DriverManager.getConnection(url, "student", "password"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    static String readEntry(String prompt) {
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while(c != '\n' && c != -1) {
                buffer.append((char)c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

   
	
    
    
   
	public static void main(String[] args) {
		// mainMenu();
		debug();
	}
	
}
