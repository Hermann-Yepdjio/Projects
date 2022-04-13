
// Copies one file into another. The names of both files must
// be specified on the command line.

import java.io.*;

public class CopyF2 {

   // no need for throws clause, as we are using try-catch.

  public static void main(String[] args) {

    // Checking number of arguments
    if (args.length != 2) {
      System.out.println("Usage: java CopyFile source dest");
      System.exit(-1);
    }

      // All I/O operations are enclosed in try catch blocks
    try {

      FileInputStream source = new FileInputStream(args[0]);
      FileOutputStream dest = new FileOutputStream(args[1]);

      // Set up a 512-byte buffer
      byte[] buffer = new byte[512];

      // Copy bytes from the source file to the destination
      // file, 512 bytes at a time
      while (true) {
        int count = source.read(buffer);
        if (count == -1)
          break;
        dest.write(buffer, 0, count);
      }

      // Close source and destination files
      source.close();
      dest.close();

    } catch (FileNotFoundException e) {  // most likely problem 
      System.out.println("File cannot be opened");

    } catch (IOException e) {    //most general last 
      System.out.println("I/O error during copy");
    }

  }
}
