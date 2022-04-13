public class TowersOfHanoi{
   public static void main(String[] args) {
      int nDisks = 5;
      hanoi(nDisks, 'A', 'B', 'C');
   }
   public static void hanoi(int topN, char from,
   char inter, char to) {
      if (topN == 1){
         System.out.println("Disk 1 from "
         + from + " to " + to);
      }else {
         hanoi(topN - 1, from, to, inter);  // Pegs exchange roles
         System.out.println("Disk "
         + topN + " from " + from + " to " + to);
         hanoi(topN - 1, inter, from, to);  // Pegs exchange roles
      }
   }
}
