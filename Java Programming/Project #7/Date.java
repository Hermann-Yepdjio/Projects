import java.util.*;
import java.io.*;

public class  Date  {  

private int  day;
private int  month ;
private int  year ;

   public Date ( )  {  day=0; month=0; year=0;}

   public Date (String d )  {
     validate ( d);
     convert(d);
   }

   protected void validate( String str) {

   }

   protected void convert( String str){
     StringTokenizer st= new StringTokenizer(str,"/");
     month =  Integer.parseInt( st.nextToken());
     day =  Integer.parseInt( st.nextToken());
     year =  Integer.parseInt( st.nextToken());

   } 

} 
