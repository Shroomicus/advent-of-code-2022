import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class DayTen {
  /**
   * @param args
   * @throws Exception
   */
  public static ArrayList<Integer> signals = new ArrayList<Integer>();
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    int x = 1;
    addtoCRT(x);
    while(scan.hasNextLine()){
      String inp[] = scan.nextLine().split(" ");
      if(inp[0].equals("noop")){
        addtoCRT(x);
      } else {
        addtoCRT(x);
        x += Integer.parseInt(inp[1]);
        addtoCRT(x);
      }
    }
    int total = 0;
    for(int i = 19; i<220; i += 40){
      total += signals.get(i) * (i + 1);
      // System.out.println(signals.get(i));
    }
    System.out.println(total);
  }

  public static void addtoCRT(int x){
    if(signals.size() % 40 == 0){
      System.out.println();
    }
    if(Math.abs(x - (signals.size() % 40)) > 1){
      System.out.print('.');
    } else {
      System.out.print('#');
    }
    signals.add(x);
    // System.out.println((signals.size()) + ": SIG STRENGTH : " + x);
  }
}