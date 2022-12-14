import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class DayFourteen {
  /**
   * @param args
   * @throws Exception
   */
  public static ArrayList<int[]> rockPos = new ArrayList<int[]>();
  public static int max = 0;
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    while(scan.hasNextLine()){
      parseLine(scan.nextLine());
    }
    int curr = rockPos.size();
    for(int i = 0; i<rockPos.size(); i++){
      if(rockPos.get(i)[1] > max){
        max = rockPos.get(i)[1];
      }
    }
    // int count = 0;
    while(addSand()){
      // count++;
    }
    for(int i = 0; i<170; i++){
      for(int j = 460; j<550; j++){
        if(isIn(j, i)){
          System.out.print("X");
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
    System.out.println(rockPos.size() - curr);
  }

  public static Boolean addSand(){
    if(isIn(500, 0)){
      return false;
    }
    rockPos.add(new int[2]);
    rockPos.get(rockPos.size()-1)[0] = 500;
    rockPos.get(rockPos.size()-1)[1] = 0;
    Boolean canMove = true;
    while(canMove){
      int[] sand = rockPos.get(rockPos.size()-1);
      if(!isIn(sand[0], sand[1]+1)){
        sand[1]++;
        continue; 
      }
      if(!isIn(sand[0]-1, sand[1]+1)){
        sand[0]--;
        sand[1]++;
        continue; 
      }
      if(!isIn(sand[0]+1, sand[1]+1)){
        sand[0]++;
        sand[1]++;
        continue; 
      }
      canMove = false;
    }
    return true;
  }

  public static Boolean isIn(int x, int y){
    if(y == max+2){
      return true;
    }
    for(int i = 0; i<rockPos.size(); i++){
      if(rockPos.get(i)[0] == x && rockPos.get(i)[1] == y){
        return true;
      }
    }
    return false;
  }

  public static void parseLine(String s){
    String[] points = s.split(" -> ");
    for(int i = 0; i<points.length-1; i++){
      String[] firString = points[i].split(",");
      int[] fir = new int[2];
      fir[0] = Integer.parseInt(firString[0]);
      fir[1] = Integer.parseInt(firString[1]);

      String[] secString = points[i+1].split(",");
      int[] sec = new int[2];
      sec[0] = Integer.parseInt(secString[0]);
      sec[1] = Integer.parseInt(secString[1]);

      for(int j = Math.min(fir[0], sec[0]); j<=Math.max(fir[0], sec[0]); j++){
        int[] currPos = {j, fir[1]};
        rockPos.add(currPos);
      }
      for(int j = Math.min(fir[1], sec[1]); j<=Math.max(fir[1], sec[1]); j++){
        int[] currPos = {fir[0], j};
        rockPos.add(currPos);
      }
    }
  }
}