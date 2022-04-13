import java.util.*;
class Problem {

private Random r = new Random();
private int addend1, addend2, answer;

Problem (int maxAddend) {
   addend1 = r.nextInt(maxAddend);
   addend2 = r.nextInt(maxAddend);
   answer = addend1 + addend2;
}

public String toString() {
   return addend1 + "+" + addend2;
  }

public int answer() {
   return answer;
  }

}

