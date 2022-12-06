import java.io.File;
import java.util.Scanner;
public class DayFour {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    String[] s = new String[2];
    String[] fElf = new String[2];
    String[] sElf = new String[2];

    int[] fInt = new int[2];
    int[] sInt = new int[2];
    int total = 0;
    while(scan.hasNext()){
      String str = scan.next();
      s = str.split(",", 2);
      fElf = s[0].split("-", 2);
      sElf = s[1].split("-", 2);
      for(int i = 0; i<2; i++){
        fInt[i] = Integer.parseInt(fElf[i]);
        sInt[i] = Integer.parseInt(sElf[i]);
      }

      // PT 1 check
      // if(((fInt[0] - sInt[0]) <= 0) && ((fInt[1] - sInt[1]) >= 0)){
      //   total++;
      //   continue;
      // }
      // if((fInt[0] - sInt[0] >= 0) && ((fInt[1] - sInt[1]) <= 0)){
      //   total++;
      //   continue;
      // }

      if(isIn(fInt[0], sInt)){
        total++;
        System.out.println(str);
        continue;
      }
      if(isIn(fInt[1], sInt)){
        total++;
        System.out.println(str);
        continue;
      }
      if(isIn(sInt[0], fInt)){
        total++;
        System.out.println(str);
        continue;
      }
      if(isIn(sInt[1], fInt)){
        total++;
        System.out.println(str);
        continue;
      }
      // System.out.println(str);
    }
    System.out.println(total);
  }

  public static Boolean isIn(int val, int[] range){
    if(val >= range[0] && val <= range[1]){
      return true;
    }
    return false;
  }
}
