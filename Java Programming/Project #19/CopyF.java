
// Copies one file into another. The names of both files must
// be specified on the command line.

import java.io.*;

public class CopyF {


  //  As the potential Exceptions of the methods are not handle,
  //  we declare teh main to thrrow the most general expected exception.
  //

  public static void main(String[] args) throws IOException {

    // Terminate program if number of command-line arguments
    // is wrong
    if (args.length != 2) {
      System.out.println("Usage: java CopyFile2 source dest");
      System.exit(-1);
    }

    // Open source file for input and destination file for output
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
  }
}
