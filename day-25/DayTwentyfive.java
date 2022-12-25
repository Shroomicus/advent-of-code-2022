import java.io.File;
import java.util.Scanner;
public class DayTwentyfive {
  /**
   * @param args
   * @throws Exception
   */

  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    int[] digits = new int[50];
    for(int i = 0; i<digits.length; i++){
      digits[i] = 0;
    }
    while(scan.hasNextLine()){
      int[] num = snafuToNum(scan.nextLine());
      for(int i = 0; i<digits.length; i++){
        digits[i] += num[i];
      }
    }

    for(int i = 0; i<digits.length - 1; i++){
      int addDigit = digits[i] / 5;
      digits[i] %= 5;
      digits[i+1] += addDigit;
      if(digits[i] > 2){
        digits[i] -= 5;
        digits[i+1]++;
      }
    }

    int total = 0;
    for(int i = 0; i<digits.length; i++){
      total += digits[i] * Math.pow(5, i);
      System.out.print(digits[i] + ", ");
    }
    System.out.println();
    System.out.println(total);
    Boolean hasNum = false;
    for(int i = digits.length-1; i>=0; i--){
      if(!hasNum && digits[i] != 0){
        hasNum = true;
      }
      if(hasNum){
        System.out.print(setVal(digits[i]));
      }
    }
  }

  public static int[] snafuToNum(String snafu){
    int[] num = new int[50];
    for(int i = 0; i < snafu.length(); i++){
      num[snafu.length() - 1 - i] = getVal(snafu.charAt(i));
    }
    return num;
  }

  public static char setVal(int i){
    if(i == -2){
      return '=';
    }
    if(i == -1){
      return '-';
    }
    return (char) (i + '0');
  }

  public static int getVal(char c){
    if(c == '-'){
      return -1;
    }
    if(c == '='){
      return -2;
    }
    return c - '0';
  }
}