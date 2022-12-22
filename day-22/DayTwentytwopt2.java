import java.io.File;
import java.util.Map;
import java.util.Scanner;
public class DayTwentytwopt2 {
  /**
   * @param args
   * @throws Exception
   */

  public static class MapFace{
    
  }

  public static class MapPosition{
    Boolean isWall = false;
    MapPosition[] horiz = new MapPosition[2];
    MapPosition[] vert = new MapPosition[2];
    MapPosition(char c){
      if(c == '#'){
        isWall = true;
      }
    }
  }
  
  public static int[] movePos = {0, 0};
  public static void main(String[] args) throws Exception {
    String inpFile = "data/input.txt";
    MapPosition[][] map = read_map(inpFile);

    MapPosition realStart = getPos(map, 0, 0);

    String[] directions = read_directions(inpFile);
    int moveDir = 0;
    for(int i = 0; i<directions.length; i++){
      // System.out.println(directions[i]);
      if(directions[i].charAt(0) == 'R'){
        moveDir = (moveDir + 1) % 4;
        continue;
      }
      if(directions[i].charAt(0) == 'L'){
        moveDir = moveDir - 1;
        if(moveDir < 0){
          moveDir = 3;
        }
        continue;
      }
      realStart = move(realStart, moveDir, Integer.parseInt(directions[i]));
    }

    printMap(map, realStart);
    // printHoriz(map);

    System.out.println(movePos[0] + ", " + movePos[1]);
    System.out.println(1000 * movePos[1] + 4 * movePos[0] + moveDir);
  }

  public static String[] read_directions(String f) throws Exception{
    Scanner scan = new Scanner(new File(f));
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      if(inp.equals("")){
        break;
      }
    }
    String total = scan.nextLine();
    String[] moveNums = total.split("R|L");
    String[] totalInst = new String[moveNums.length + moveNums.length - 1];
    for(int i = 0; i<moveNums.length; i++){
      // System.out.println(moveNums[i]);
    }
    for(int i = 0; i<totalInst.length; i+=2){
      totalInst[i] = moveNums[i/2];
    }
    int point = 0;
    int addPoint = 1;
    while(point < total.length()){
      if(total.charAt(point) == 'L' || total.charAt(point) == 'R'){
        totalInst[addPoint] = "" + total.charAt(point);
        addPoint+=2;
      }
      point++;
    }

    return totalInst;
  }

  public static MapPosition[][] read_map(String f) throws Exception{
    Scanner scan = new Scanner(new File(f));
    int max = 0;
    int len = 0;
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      if(inp.equals("")){
        break;
      }
      max = Math.max(max, inp.length());
      // System.out.println(inp);
      len++;
    }

    scan = new Scanner(new File(f));
    MapPosition[][] map = new MapPosition[len][max];
    len = 0;
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      if(inp.equals("")){
        break;
      }
      // System.out.println(inp);
      for(int i = 0; i<max; i++){
        if(inp.length() <= i || inp.charAt(i) == ' '){
          map[len][i] = null;
          continue;
        }
        map[len][i] = new MapPosition(inp.charAt(i));
      }
      len++;
    }

    for(int i = 0; i<len; i++){
      int p = 0;
      MapPosition currPos = getPos(map, 0, i);
      MapPosition nextPos = getPos(map, 1, i);
      // System.out.println(currPos);
      while(currPos != null){
        if(nextPos == null){
          currPos.horiz[1] = getPos(map, 0, i);
          currPos.horiz[1].horiz[0] = currPos;
        } else {
          currPos.horiz[1] = nextPos;
          currPos.horiz[1].horiz[0] = currPos;
        }
        p++;
        currPos = getPos(map, p, i);
        nextPos = getPos(map, p+1, i);
      }
    }

    for(int i = 0; i<len; i++){
      int p = 0;
      MapPosition currPos = getYPos(map, i, 0);
      MapPosition nextPos = getYPos(map, i, p+1);
      
      // if(nextPos == null){
      //   System.out.println(i + " ? " + p);
      // }
      // System.out.println(currPos);
      while(currPos != null){
        if(nextPos == null){
          currPos.vert[1] = getYPos(map, i, 0);
          currPos.vert[1].vert[0] = currPos;
        } else {
          currPos.vert[1] = nextPos;
          currPos.vert[1].vert[0] = currPos;
        }
        p++;
        currPos = getYPos(map, i, p);
        nextPos = getYPos(map, i, p+1);
      }
    }

    return map;
  }

  public static void printMap(MapPosition[][] map, MapPosition curr){
    for(int i = 0; i<map.length; i++){
      for(int j = 0; j<map[0].length; j++){
        if(map[i][j] == null){
          System.out.print(' ');
          continue;
        }
        if(map[i][j] == curr){
          System.out.print('X');
          movePos[0] = j + 1;
          movePos[1] = i + 1;
          continue;
        }
        if(map[i][j].isWall){
          System.out.print('#');
          continue;
        } 
        System.out.print('.');
      }
      System.out.println();
    }
  }

  public static void printHoriz(MapPosition[][] map){
    for(int i = 0; i<map.length; i++){
      MapPosition start = getPos(map, 0, i);
      MapPosition curr = start;
      while(curr != null){
        if(curr.isWall){
          System.out.print('#');
        } else {
          System.out.print('.');
        }
        curr = curr.horiz[1];
        if(curr == start){
          break;
        }
      }
      System.out.println();
    }
  }

  public static MapPosition getPos(MapPosition[][] map, int x, int y){
    if(y >= map.length){
      return null;
    }
    MapPosition[] layer = map[y];
    int p = 0;
    while(layer[p] == null){
      p++;
    }
    if(p + x >= layer.length){
      return null;
    }
    return layer[p + x];
  }

  public static MapPosition getYPos(MapPosition[][] map, int x, int y){
    if(y >= map.length){
      return null;
    }
    if(x >= map[0].length){
      return null;
    }
    int pos = 0;
    while(map[pos][x] == null){
      pos++;
    }
    if(pos + y >= map.length){
      return null;
    }
    return map[pos + y][x];
  }

  public static MapPosition move(MapPosition startPos, int dir, int dist){
    // 0 : Right
    // 1 : Down
    // 2 : Left
    // 3 : Up
    switch(dir){
      case 0:
        for(int i = 0; i<dist; i++){
          if(startPos.horiz[1].isWall){
            break;
          }
          startPos = startPos.horiz[1];
        }
        break;
      case 1:
        for(int i = 0; i<dist; i++){
          if(startPos.vert[1].isWall){
            break;
          }
          startPos = startPos.vert[1];
        }
        break;
      case 2:
        for(int i = 0; i<dist; i++){
          if(startPos.horiz[0].isWall){
            break;
          }
          startPos = startPos.horiz[0];
        }
        break;
      case 3:
        for(int i = 0; i<dist; i++){
          if(startPos.vert[0].isWall){
            break;
          }
          startPos = startPos.vert[0];
        }
        break;
    }
    return startPos;
  }
}