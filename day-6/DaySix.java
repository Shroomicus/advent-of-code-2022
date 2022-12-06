import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
public class DaySix {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    String inp = scan.nextLine();
    LinkedList<Character> last = new LinkedList<Character>();
    for(int i = 0; i<inp.length(); i++){
      if(last.size() == 14){
        last.pop();
      } else {
        last.add(inp.charAt(i));
        continue;
      }
      last.add(inp.charAt(i));

      Boolean has = false;
      for(int j = 0; j<14; j++){
        for(int k = 0; k<14; k++){
          if(j == k){
            continue;
          }
          if(last.get(j) == last.get(k)){
            has = true;
            break;
          }
        }
      }
      if(!has){
        // for(int j = 0; j<4; j++){
        //   System.out.println(last.get(j));
        // }
        System.out.println(i + 1);
        return;
      }
    }
  }
}
