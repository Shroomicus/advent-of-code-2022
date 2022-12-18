import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import java.util.Scanner;
public class DaySeventeen {
  /**
   * @param args
   * @throws Exception
   */
  public static int otherCount = 10000;
  public static BigInteger total = new BigInteger("0");
  public static Boolean repeating = false;
  public static int storedVal = 0;
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    String moves = scan.nextLine();
    ArrayList<int[]> cave = new ArrayList<int[]>();

    for(int i = 0; i<3; i++){
      int[] empty = {0, 0, 0, 0, 0, 0, 0};
      cave.add(empty);
    }
    int place = 0;
    int count = 0;
    
    while(otherCount != 0){
      place = dropRock(cave, count+1, moves, place);
      if(!repeating){checkSeal(cave, moves, place, rockHeight(cave), count, 10000-otherCount);}
      otherCount--;
      count++;
      count %= 5;
      // place %= moves.length();
    }
    // place = dropRock(cave, 1, moves, place);
    // place = dropRock(cave, 2, moves, place);
    // place = dropRock(cave, 3, moves, place);
    // place = dropRock(cave, 4, moves, place);
    // place = dropRock(cave, 5, moves, place);
    System.out.println(total.add(new BigInteger(Integer.toString(rockHeight(cave) - storedVal))));
    // System.out.println(moves.length());
    // printCave(cave);
    // place = dropRock(cave, 1, moves, place);
    // place = dropRock(cave, 2, moves, place);
    // place = dropRock(cave, 3, moves, place);
    // place = dropRock(cave, 4, moves, place);
    // System.out.println(cave.size());
    // System.out.println(rockHeight(cave));
  }

  public static ArrayList<String> barrs = new ArrayList<String>();
  public static ArrayList<Integer> heights = new ArrayList<Integer>();
  public static ArrayList<Integer> counts = new ArrayList<Integer>();

  public static Boolean checkSeal(ArrayList<int[]> cave, String windPaths, int wind, int height, int piece, int count){
    int h = rockHeight(cave) - 1;
    if(h < 2){
      return false;
    }
    for(int j = h; j > 2; j--){
      Boolean found = true;
  
      for(int i = 0; i<7; i++){
        if(cave.get(j)[i] == 0 && cave.get(j-1)[i] == 0){
          found = false;
        }
      }
      if(found){
        String barrier = "";
        for(int i = 0; i<7; i++){
          barrier += ('0' + cave.get(j)[i]);
          // System.out.print(cave.get(j)[i]);
        }
        // System.out.println();
        for(int i = 0; i<7; i++){
          barrier += ('0' + cave.get(j-1)[i]);
          // System.out.print(cave.get(j-1)[i]);
        }
        int low;
        if(barrs.size() == 0 || wind >= windPaths.length()){
          low = 0;
        } else {
          low = Integer.parseInt(barrs.get(0).split(" ")[1]);
        }
        if(low > wind){
          low = 0;
        }
        barrier += " " + wind + " " + windPaths.substring(low, wind) + " " + piece;
        // System.out.println(barrier);
        if(barrs.contains(barrier)){
          BigInteger full = new BigInteger("1000000000000");
          full = full.subtract(new BigInteger(Integer.toString(counts.get(barrs.indexOf(barrier)))));
          System.out.println(full.toString());
          BigInteger remainder = full.remainder(new BigInteger(Integer.toString(count - counts.get(barrs.indexOf(barrier)))));
          System.out.println(remainder.toString());
          otherCount = remainder.intValue();
          storedVal = height - heights.get(barrs.indexOf(barrier));

          full = full.divide(new BigInteger(Integer.toString(count - counts.get(barrs.indexOf(barrier)))));
          full = full.multiply(new BigInteger(Integer.toString(height - heights.get(barrs.indexOf(barrier)))));
          total = full;
          System.out.println(full);

          System.out.println(barrier);
          System.out.println(height);
          System.out.println(count);
          System.out.println(barrs.get(barrs.indexOf(barrier)));
          System.out.println(heights.get(barrs.indexOf(barrier)));
          System.out.println(counts.get(barrs.indexOf(barrier)));
          repeating = true;
          // System.exit(0);
        }
        barrs.add(0, barrier);
        heights.add(0, height);
        counts.add(0, count);
        return true;
      }
    }
    return true;
  }

  public static int dropRock(ArrayList<int[]> cave, int type, String winds, int place){
    while(cave.size() > rockHeight(cave)){
      cave.remove(cave.size()-1);
    }
    for(int i = cave.size(); i<rockHeight(cave) + 3; i++){
      int[] empty = {0, 0, 0, 0, 0, 0, 0};
      cave.add(empty);
    }
    // printCave(cave);
    // System.out.println(cave.size() + " : " + rockHeight(cave));

    int[][] pieceMap = pieceMap(type);
    int[][] addMap = new int[pieceMap.length][7];
    for(int i = 0; i<pieceMap.length; i++){
      for(int j = 0; j<addMap[i].length; j++){
        addMap[i][j] = 0;
      }
      for(int j = 0; j<pieceMap[i].length; j++){
        addMap[i][j+2] = pieceMap[i][j];
      }
    }
    addToTop(cave, addMap);

    Boolean canDrop = true;
    int[] pieceStart = {2, rockHeight(cave)-1, 0};
    // cave.get(pieceStart[1]-3)[4] = 5;
    while(canDrop){
      // printCave(cave);
      // System.out.println(rockHeight(cave));
      // System.out.println();
      pieceStart = movePiece(cave, type, pieceStart, winds.charAt(place) == '>');
      place++;
      place%=winds.length();
      canDrop = pieceStart[2] == 1;
    }
    return place;
  }

  public static int[] movePiece(ArrayList<int[]> cave, int type, int[] pos, Boolean right){
    int[] result = new int[3];

    // System.out.println(pos[0] + ", " + pos[1]);
    int[][] pieceMap = pieceMap(type);
    if(canMove(cave, type, pos, right)){
      if(right){
        for(int i = 0; i<pieceMap.length; i++){
          for(int j = pos[0] + lastCount(pieceMap[i])+1; j > pos[0] + firstCount(pieceMap[i]); j--){
            cave.get(pos[1]-i)[j] = cave.get(pos[1]-i)[j-1];
          }
          cave.get(pos[1]-i)[pos[0] + firstCount(pieceMap[i])] = 0;
        }
        pos[0]++;
      } else {
        for(int i = 0; i<pieceMap.length; i++){
          for(int j = pos[0] + firstCount(pieceMap[i])-1; j < pos[0] + lastCount(pieceMap[i]); j++){
            cave.get(pos[1]-i)[j] = cave.get(pos[1]-i)[j+1];
          }
          cave.get(pos[1]-i)[pos[0] + lastCount(pieceMap[i])] = 0;
        }
        pos[0]--;
      }
    }
    // System.out.println(canMoveDown(cave, type, pos));
    if(canMoveDown(cave, type, pos)){
      for(int i = 0; i<pieceMap[0].length; i++){
        for(int j = pieceMap.length - downCount(pieceMap, i); j>upCount(pieceMap, i); j--){
          cave.get(pos[1]-j)[pos[0] + i] = cave.get(pos[1]-j+1)[pos[0] + i];
        }
        cave.get(pos[1]-upCount(pieceMap, i))[pos[0] + i] = 0;
        // System.out.println();
      }
      result[2] = 1;
      pos[1]--;
    } else {
      result[2] = 0;
    }

    result[0] = pos[0];
    result[1] = pos[1];
    return result;
  }

  public static Boolean canMove(ArrayList<int[]> cave, int type, int[] pos, Boolean right){
    int[][] pieceMap = pieceMap(type);
    if(right){
      if(pos[0]+pieceMap[0].length >= 7){
        return false;
      }
      for(int i = 0; i<pieceMap.length; i++){
        // System.out.println("Offset: " + (pos[0] + firstCount(pieceMap[i]) - 1));
        if(cave.get(pos[1] - i)[pos[0] + lastCount(pieceMap[i]) + 1] > 0){
          return false;
        }
      }
    } else {
      if(pos[0]-1 < 0){
        return false;
      }
      for(int i = 0; i<pieceMap.length; i++){
        // System.out.println("Offset: " + (pos[0] + firstCount(pieceMap[i]) - 1));
        if(cave.get(pos[1] - i)[pos[0] + firstCount(pieceMap[i]) - 1] > 0){
          return false;
        }
      }
    }
    return true;
  }

  public static Boolean canMoveDown(ArrayList<int[]> cave, int type, int[] pos){
    int[][] pieceMap = pieceMap(type);
    // System.out.println(downCount(pieceMap, 1));
    if(pos[1] - pieceMap.length < 0){
      return false;
    }
    // System.out.println(":" + downCount(pieceMap, 0));
    for(int i = 0; i<pieceMap[0].length; i++){
      // System.out.println(pos[1] - pieceMap.length + downCount(pieceMap, i));
      if(cave.get(pos[1] - pieceMap.length + downCount(pieceMap, i))[pos[0] + i] > 0){
        return false;
      }
    }
    
    return true;
  }

  public static int upCount(int[][] map, int column){
    for(int i = 0; i < map[0].length; i++){
      if(map[i][column] > 0){
        return i;
      }
    }
    return 0;
  }

  public static int downCount(int[][] map, int column){
    for(int i = map.length-1; i >= 0; i--){
      if(map[i][column] > 0){
        return map.length-1-i;
      }
    }
    return 0;
  }

  public static int lastCount(int[] row){
    for(int i = row.length-1; i>=0; i--){
      if(row[i] > 0){
        return i;
      }
    }
    return 0;
  }
  public static int firstCount(int[] row){
    for(int i = 0; i<row.length; i++){
      if(row[i] > 0){
        return i;
      }
    }
    return 0;
  }

  public static int[][] pieceMap(int type){
    switch(type){
      case 1:
        int[][] linePiece = {
          {1, 1, 1, 1}
        };
        return linePiece;
      case 2:
        int[][] crossPiece = {
          {0, 2, 0},
          {2, 2, 2},
          {0, 2, 0}
        };
        return crossPiece;
      case 3:
        int[][] lPiece = {
          {0, 0, 3},
          {0, 0, 3},
          {3, 3, 3}
        };
        return lPiece;
      case 4:
        int[][] barPiece = {
          {4},
          {4},
          {4},
          {4}
        };
        return barPiece;
      case 5:
        int[][] squarePiece = {
          {5, 5},
          {5, 5}
        };
        return squarePiece;
    }
    return null;
  }

  public static void addToTop(ArrayList<int[]> cave, int[][] piece){
    for(int i = piece.length-1; i>=0; i--){
      cave.add(piece[i]);
    }
  }

  public static void printCave(ArrayList<int[]> cave){
    for(int i = cave.size()-1; i>=0; i--){
      for(int j = 0; j<7; j++){
        if(cave.get(i)[j] == 0){
          System.out.print('.');
          continue;
        }
        System.out.print(cave.get(i)[j]);
      }
      System.out.println();
    }
  }

  public static int rockHeight(ArrayList<int[]> cave){
    for(int i = cave.size()-1; i>=0; i--){
      for(int j = 0; j<7; j++){
        if(cave.get(i)[j] != 0){
          return i+1;
        }
      }
    }
    return 0;
  }
}