import java.io.File;
import java.util.ArrayList;

import java.util.Scanner;
public class DayFifteen {
  /**
   * @param args
   * @throws Exception
   */
  public static int minX = 0; 
  public static int maxX = 0;
  public static int maxR = 0;
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    ArrayList<int[]> sensors = new ArrayList<int[]>();
    String min = scan.nextLine();
    sensors.add(parseString(min));
    minX = sensors.get(sensors.size()-1)[0];
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      sensors.add(parseString(inp));
    }

    // for(int i = 0; i<sensors.size(); i++){
    //   System.out.println(sensors.get(i)[2]);
    // }
    // for(int i = 0; i<22; i++){
    //   for(int j = -10; j<=35; j++){
    //     if(isObject(sensors, j, i)){
    //       if(isBeacon(sensors, j, i)){
    //         System.out.print("S");
    //       } else {
    //         System.out.print("B");
    //       }
    //     } else if(isDetected(sensors, j, i)){
    //       System.out.print("-");
    //     } else {
    //       System.out.print(".");
    //     }
    //   }
    //   System.out.println();
    // }

    // int detected = 0;
    // for(int i = minX-maxR; i<maxX+maxR; i++){
    //   if(isDetected(sensors, i, 2000000)){
    //     detected++;
    //   } else {
    //   }
    // }
    // System.out.println(detected);

    int[][] senseArray = new int[sensors.size()][3];
    for(int i = 0; i<senseArray.length; i++){
      for(int j = 0; j<3; j++){
        senseArray[i][j] = sensors.get(i)[j];
      }
    }

    System.out.println(isDetected(sensors, 3303271, 2906101));
    

    int[] currBeacon = new int[3];
    Boolean found = false;
    currBeacon = senseArray[0];
    int beacNum = 0;
    int x = 0;
    int y = 0;
    while(!found){
      if(!isIn(currBeacon, x, y)){
        while(!isIn(currBeacon, x, y)){
          beacNum = (beacNum + 1) % (sensors.size());
          currBeacon = senseArray[beacNum];
          if(!isDetected(sensors, x, y)){
            System.out.println(x + ", " + y);
            return;
          }
        }
      }
      if(isIn(currBeacon, x, y)){
        // Thread.sleep(1000);
        x = currBeacon[0];
        x += currBeacon[2] - Math.abs(y - currBeacon[1]) + 1;
        // System.out.println("Curr Pos:\t" + x + ", " + y);
        // System.out.println("Curr Beac:\t" + currBeacon[0] + ", " + currBeacon[1] + ", " + currBeacon[2]);
        if(x > 4000000){
          x = 0;
          y++;
        }
      }
    }
  }

  public static Boolean isIn(int[] beacon, int x, int y){
    if(Math.abs(beacon[0] - x) + Math.abs(beacon[1] - y) <= beacon[2]){
      return true;
    }
    return false;
  }

  public static Boolean isObject(ArrayList<int[]> sensors, int x, int y){
    for(int i = 0; i<sensors.size(); i++){
      int[] sensor = sensors.get(i);
      if((x == sensor[0] && y == sensor[1]) || (x == sensor[3] && y == sensor[4])){
        // System.out.println("????");
        return true;
      }
    }
    return false;
  }

  public static Boolean isDetected(ArrayList<int[]> sensors, int x, int y){
    if(isObject(sensors, x, y)){
      return true;
    }
    for(int i = 0; i<sensors.size(); i++){
      if(isIn(sensors.get(i), x, y)){
        return true;
      }
    }
    return false;
  }

  public static Boolean isBeacon(ArrayList<int[]> sensors, int x, int y){
    for(int i = 0; i<sensors.size(); i++){
      int[] sensor = sensors.get(i);
      if((x == sensor[0] && y == sensor[1])){
        return true;
      }
    }
    return false;
  }

  public static int[] parseString(String s){
    int[] result = new int[5];
    String[] r = s.split("x=|,|y=|:");

    int[] vals = new int[2];
    result[0] = Integer.parseInt(r[1]);
    result[1] = Integer.parseInt(r[3]);
    result[3] = Integer.parseInt(r[5]);
    result[4] = Integer.parseInt(r[7]);
    if(minX > result[0]){
      minX = result[0];
    }
    if(maxX < result[0]){
      maxX = result[0];
    }
    for(int i = 0; i<2; i++){
      vals[i] = Math.abs(result[i] - result[3+i]);
    }
    result[2] = vals[0] + vals[1];
    if(minX > result[3]){
      minX = result[3];
    }
    if(maxX < result[3]){
      maxX = result[3];
    }
    if(maxR < result[2]){
      maxR = result[2];
    }
    return result;
  }
}