
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.LinkedList;

@SuppressWarnings("unused")
public class Updates {
	
	// object containing methods used in Scenario 1
	
	public static void InsertStudent(Connection conn) {
		// method used to insert a student based on input.
		// on the large side since the Student relation contains many fields of different types
		boolean newPersonInserted = false;
		Student studentInserting = new Student();
		studentInserting.buildFromPrompts();
		int studentPID = generateNewPID(conn);
		try {
			// insert new person table
			PreparedStatement personInsertStatement = conn.prepareStatement("INSERT INTO Person (PersonID, FullName, Address, PhoneNumber) "
					+ "VALUE (?, ?, ?, ?)");
			Utility.setInt(personInsertStatement, 1, studentPID);  // generates new valid PID via simple query
			Utility.setString(personInsertStatement, 2, studentInserting.name);  // uses Utility.set to process null values for this field.
			Utility.setString(personInsertStatement, 3, studentInserting.address);
			Utility.setString(personInsertStatement, 4, studentInserting.phoneNumber);
			personInsertStatement.executeUpdate();
			newPersonInserted = true;
			personInsertStatement.close();
			// insert new student table
			PreparedStatement studentInsertStatement = conn.prepareStatement("INSERT INTO Student"
					+ "(PersonID, SSN, Birthday, Gender, Nationality, StudentType, EnrollDate, CollegeAttendingID, MajorDepartmentID, DegreePursuing) "
					+ "VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			Utility.setInt(studentInsertStatement, 1, studentPID);
			Utility.setInt(studentInsertStatement, 2, studentInserting.ssn);
			Utility.setDate(studentInsertStatement, 3, studentInserting.birthday);
			Utility.setString(studentInsertStatement, 4, studentInserting.gender);
			Utility.setString(studentInsertStatement, 5, studentInserting.nationality);
			Utility.setString(studentInsertStatement, 6, studentInserting.studentType);
			Utility.setDate(studentInsertStatement, 7, studentInserting.enrollDate);
			Utility.setInt(studentInsertStatement, 8, studentInserting.collegeAttendingId);
			Utility.setInt(studentInsertStatement, 9, studentInserting.majorDepartmentId);
			Utility.setString(studentInsertStatement, 10, studentInserting.degreePursuing);
			studentInsertStatement.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.toString());
			if (newPersonInserted) {  // new table added to person without corresponding student; we want to delete this
				try {
					PreparedStatement personRemoveStatement = conn.prepareStatement("DELETE FROM Person WHERE PersonID = ?");
					personRemoveStatement.setInt(1, studentPID);
					personRemoveStatement.executeUpdate();
				}
				catch(Exception e2) {  // unable to remove the inserted person table; an unlikely situation for this assignment
					e2.printStackTrace();;
				}
			}
		}
	}
	
	private static int generateNewPID(Connection conn) {
		// utility function used to generate a PID so the user does not need to input one.
		try {
			PreparedStatement personQuery = conn.prepareStatement("SELECT * FROM (SELECT PersonID FROM Person ORDER BY PersonID DESC) AS N LIMIT 1");
			ResultSet personRset = personQuery.executeQuery();
			LinkedList<Integer> pidList = new LinkedList<Integer>();
			personRset.next();
			int outPid = personRset.getInt(1) + 1;
			return outPid;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return -1;  // will generate an error on the insert
		}
		
	}
	
	
}
