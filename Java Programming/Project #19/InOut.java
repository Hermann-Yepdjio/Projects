
// Provides prompt and readLine methods for console input


import java.io.*;
import java.util.*;

public class InOut {
//  private static File  streamIn = new File (System.in);

 private static InputStreamReader streamIn =
    new InputStreamReader(System.in);
  private static BufferedReader in =
//    new BufferedReader(streamIn, 1);
    new BufferedReader(streamIn);



  // Displays the string s without terminating the current
  // line
  public static void prompt(String s) {
    System.out.print(s);
    System.out.flush();
  }

  // Reads and returns a single line of input entered by the
  // user; terminates the program if an exception is thrown
  public static String readLine() {
    String line = null;
    try {
      line = in.readLine();
    } catch (IOException e) {
      System.out.println("Error in InOut.readLine: " +
                         e.getMessage());
      System.exit(-1);
    }
    return line;
  }
}
