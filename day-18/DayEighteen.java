import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
public class DayEighteen {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    ArrayList<int[]> drops = new ArrayList<int[]>();

    int min = 20;
    int max = 0;

    while(scan.hasNextLine()){
      String[] dropInfo = scan.nextLine().split(",");
      int[] newDrop = new int[3];
      for(int i = 0; i<dropInfo.length; i++){
        newDrop[i] = Integer.parseInt(dropInfo[i]);
        if(min > newDrop[i]){
          min = newDrop[i];
        }
        if(max < newDrop[i]){
          max = newDrop[i];
        }
      }
      drops.add(newDrop);
    }
    max+=2;

    Boolean[][][] air = new Boolean[max][max][max];
    for(int i = 0; i<max; i++){
      for(int j = 0; j<max; j++){
        for(int k = 0; k<max; k++){
          int[] particle = {i, j, k};
          air[i][j][k] = !has(drops, particle);
        }
      }
    }

    int remaining = 0;
    for(int i = 0; i<max; i++){
      for(int j = 0; j<max; j++){
        for(int k = 0; k<max; k++){
          if(air[i][j][k]){
            remaining++;
          }
        }
      }
    }
    System.out.println(remaining);

    int[] start = {0, 0, 0};
    LinkedList<int[]> airCheck = new LinkedList<int[]>();
    airCheck.add(start);
    while(airCheck.size() > 0){
      int[] curr = airCheck.removeFirst();
      if(!air[curr[0]][curr[1]][curr[2]]){
        continue;
      }
      air[curr[0]][curr[1]][curr[2]] = false;
      for(int j = 0; j<3; j++){
        curr[j]++;
        // System.out.println(curr[0] + ","  + curr[1] + "," + curr[2]);
        if(curr[j] < max && air[curr[0]][curr[1]][curr[2]]){
          airCheck.add(curr.clone());
        }
        curr[j]-=2;
        if(curr[j] >= min && air[curr[0]][curr[1]][curr[2]]){
          airCheck.add(curr.clone());
        }
        curr[j]++;
      }
    }

    remaining = 0;
    for(int i = 0; i<max; i++){
      for(int j = 0; j<max; j++){
        for(int k = 0; k<max; k++){
          if(air[i][j][k]){
            remaining++;
          }
        }
      }
    }
    System.out.println(remaining);
    System.out.println();

    System.out.println(min);
    System.out.println(max);

    System.out.println(air[2][2][5]);

    int total = drops.size() * 6;

    for(int i = 0; i<drops.size(); i++){
      int[] newDrop = {drops.get(i)[0], drops.get(i)[1], drops.get(i)[2]};
      for(int j = 0; j<3; j++){
        newDrop[j]++;
        if(newDrop[0] != -1 && newDrop[1] != -1 && newDrop[2] != -1 && air[newDrop[0]][newDrop[1]][newDrop[2]]){
          total--;
        }
        if(has(drops, newDrop) && !air[newDrop[0]][newDrop[1]][newDrop[2]]){
          total--;
        }
        newDrop[j]-=2;
        if(newDrop[0] != -1 && newDrop[1] != -1 && newDrop[2] != -1 && air[newDrop[0]][newDrop[1]][newDrop[2]]){
          total--;
        }
        if(has(drops, newDrop) && !air[newDrop[0]][newDrop[1]][newDrop[2]]){
          total--;
        }
        newDrop[j]++;
      }
      // System.out.println(has(drops, newDrop));
    }

    System.out.println(total);
    // int[] checkdrop = {13, 9, 2};
    // System.out.println(has(drops, checkdrop));
  }

  public static Boolean has(ArrayList<int[]> drops, int[] drop){
    for(int i = 0; i<drops.size(); i++){
      Boolean has = true;
      for(int j = 0; j<drop.length; j++){
        if(drop[j] != drops.get(i)[j]){
          has = false;
        }
      }
      if(has){
        return true;
      }
    }
    return false;
  }
}