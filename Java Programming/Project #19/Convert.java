

public class Convert {

  // convert string to values in wrapper classes for floating points.
  // 

  //  double  conversion 
  //
  public static double toDouble(String s) {
    double d = 0.0;
    try {
      d = Double.valueOf(s).doubleValue();
    } catch (NumberFormatException e) {
      System.out.println("Error in Convert.toDouble: " +
                         e.getMessage());
      System.exit(-1);
    }
    return d;
  }

  //  float conversion 
  //
  public static float toFloat(String s) {
    float f = 0.0f;
    try {
      f = Float.valueOf(s).floatValue();
    } catch (NumberFormatException e) {
      System.out.println("Error in Convert.toFloat: " +
                         e.getMessage());
      System.exit(-1);
    }
    return f;
  }
}
