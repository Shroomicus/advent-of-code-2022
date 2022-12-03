import java.io.File;
import java.util.Scanner;
public class DayThree {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    int total = 0;
    while(scan.hasNextLine()){
      char c = getThreeCommon(scan.nextLine(), scan.nextLine(), scan.nextLine());
      total += convertChar(c);
      System.out.println(c + " : " + convertChar(c));
    }
    System.out.println(total);
  }

  public static char getThreeCommon(String val1, String val2, String val3){
    for(int i = 0; i < val1.length(); i++){
      for(int j = 0; j < val2.length(); j++){
        if(val1.charAt(i) == val2.charAt(j)){
          for(int k = 0; k < val3.length(); k++){
            if(val1.charAt(i) == val2.charAt(j) && val1.charAt(i) == val3.charAt(k)){
              return val1.charAt(i);
            }
          }
        }
      }
    }
    return 0;
  }

  public static char getCommon(String val){
    int strlen = val.length() / 2;
    for(int i = 0; i<strlen; i++){
      for(int j = 0; j<strlen; j++){
        if(val.charAt(i) == val.charAt(strlen + j)){
          return val.charAt(i);
        }
      }
    }
    return '0';
  }

  public static int convertChar(char c){
    int total = 0;
    if(c < 91){
      total += 26;
    }
    total += Character.toLowerCase(c) - 96;
    return total;
  }
}
