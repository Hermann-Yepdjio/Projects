import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RulesAndLaws {

	// object containing methods used in Scenario 3
	
	/* 
	 * See Updates.java for how Prompt, Utility, and other objects are used for an input-based sql query.
	 * Try to structure your queries somewhat similarly in terms of using a class for storing table data,
	 * like how Student was used in Updates, and using the Utility and Prompt methods for input
	 * and null input processing when applicable.  If you have any questions definitely ask.
	 * 
	 */
	
	
	
	 private static void InterStuReg(Connection conn) throws SQLException, IOException {
		 
          Statement stmt = conn.createStatement();
          
         // STEP 2 DEFINE A STRING THAT IS = TO YOUR query SQL Statement
          
         String query = "  SELECT* \n" + 
         		"	    FROM Regulation \n" + 
         		"        ";
         
         		
       
          // Step 3: Declare a variable with ResultSet type
        
         ResultSet rset;
       
         
          //Execute your Query and store the return in the declared variable from step 3

          rset= stmt.executeQuery(query);
        
          
          System.out.println("   (International Rules & Laws ");
          System.out.println("--------------------------------------------------\n");

          // Write a loop to read all the returned rows from the query execution
           
          while(rset.next()) {
              int RegulationID = rset.getInt(1);
              String RegulationName = rset.getString(2);
              String RegulationDescription = rset.getString(3);
              
              System.out.println("RegulationID is: " + RegulationID + "   RegulationName is: " + RegulationName + "   RegulationDescription is: " + RegulationDescription  );
               
          }
        
          stmt.close();  
           
           
          
          
      }
	
	
}
