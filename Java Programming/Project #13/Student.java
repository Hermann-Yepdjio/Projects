import java.util.Date;

public class Student {
	// class used for storing data involved with inserting a student.  Combination of Student and Person relations.
	public String name;
	public String address;
	public String phoneNumber;
	public Integer ssn;
	public Date birthday;
	public String gender;
	public String nationality;
	public Date enrollDate;
	public Integer collegeAttendingId;
	public Integer majorDepartmentId;
	public String studentType;
	public String degreePursuing;
	
	Student() {
		this.name = null;
		this.address = null;
		this.phoneNumber = null;
		this.ssn = null;
		this.birthday = null;
		this.gender = null;
		this.nationality = null;
		this.enrollDate = null;
		this.collegeAttendingId = null;
		this.majorDepartmentId = null;
		this.studentType = null;
		this.degreePursuing = null;
	}
	
	public void buildFromPrompts() {
		// uses Prompt.java to retrieve user input of a certain data type.
		System.out.println("To enter a null value for the following prompts, input nothing or \"null\".");
		this.name = Prompt.promptString("Enter student name:");
		this.address = Prompt.promptString("Enter student address:");
		this.phoneNumber = Prompt.promptString("Enter student phone number:");
		this.ssn = Prompt.promptInt("Enter student SSN:");  // should be unique
		this.birthday = Prompt.promptDate("Enter student birthdate:");
		this.gender = Prompt.promptEnum("Enter student gender (M/F/O):", "M", "F", "O");
		this.nationality = Prompt.promptString("Enter student nationality:");
		this.enrollDate = Prompt.promptDate("Enter student enroll date:");
		this.collegeAttendingId = Prompt.promptInt("Enter valid ID of college student is attending:");  // must match an existing college id
		this.majorDepartmentId = Prompt.promptInt("Enter valid ID of department of student's major:");  // must match an existing department id
		this.studentType = Prompt.promptEnum("Enter student type (graduate/undergraduate):","graduate","undergraduate");
		this.degreePursuing = Prompt.promptString("Enter valid name of Degree the student is pursuing:");
	}
	
}
