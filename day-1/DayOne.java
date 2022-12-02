
import java.io.File;
import java.util.Scanner;
public class DayOne {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    // System.out.println(url.getPath());
    Scanner numberScan = new Scanner(new File("data/input.txt"));
    String calString;
    int calNum;
    int[] maxCals = new int[3];
    int currCals = 0;
    while(numberScan.hasNextLine()){
      calString = numberScan.nextLine();
      if(!calString.equals("")){
        calNum = Integer.parseInt(calString);
        currCals += calNum;
      } else {
        for(int i = 0; i<3; i++){
          if(maxCals[i] <= currCals){
            for(int j = 2; j>i; j--){
              maxCals[j] = maxCals[j-1];
            }
            maxCals[i] = currCals;
            break;
          }
        }
        currCals = 0;
      }
    }
    for(int i = 0; i<3; i++){
      if(maxCals[i] <= currCals){
        maxCals[i] = currCals;
        for(int j = i; j<2; j++){
          maxCals[j] = maxCals[j+1];
        }
        break;
      }
    }

    int totalCals = 0;
    for(int i = 0; i<3; i++){
      totalCals += maxCals[i];
      System.out.println(i + " : " + maxCals[i]);
    }

    System.out.println(totalCals);
  }
}
