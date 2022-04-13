import java.io.*;
public class  Executive {  

protected String name;
protected Date dob;

public Executive ( ) { setname ("");}

public Executive ( String  name ) {
    this.name = name;
    dob = new Date();
   }   

public Executive ( String  name, String d ) {
    this.name = name;
    dob = new Date (d);
   }   


public String toString() {
    return name;
   } 

protected void setname( String str){
   name = str;
   } 

} 
