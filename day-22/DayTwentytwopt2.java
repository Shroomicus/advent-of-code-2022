import java.io.File;
import java.util.Scanner;
public class DayTwentytwopt2 {
  /**
   * @param args
   * @throws Exception
   */

  public static class MapFace{
    int faceNum = -1;
    MapPosition[][] facePos;
    MapPosition[][] sides = new MapPosition[4][];
    int sideLen;
    MapFace(int fNum, int sideLen){
      faceNum = fNum;
      facePos = new MapPosition[sideLen][sideLen];
      for(int i = 0; i<4; i++){
        sides[i] = new MapPosition[sideLen];
      }
      this.sideLen = sideLen;
    }
    void setSides(){
      for(int i = 0; i<sideLen; i++){
        sides[0][i] = facePos[i][sideLen-1];
        sides[1][i] = facePos[sideLen-1][i];
        sides[2][i] = facePos[i][0];
        sides[3][i] = facePos[0][i];
      }
    }
  }

  public static class MapPosition{
    // 0 : Right
    // 1 : Down
    // 2 : Left
    // 3 : Up
    Boolean isWall = false;
    MapPosition[] moves = new MapPosition[4];
    int[] mDirs = new int[4];
    int[] realpos = new int[2];

    MapPosition(char c, int x, int y){
      if(c == '#'){
        isWall = true;
      }
      realpos[0] = x;
      realpos[1] = y;
      for(int i = 0; i<4; i++){
        mDirs[i] = i;
      }
    }
  }

  public static class MoveData{
    MapPosition p;
    int newDir;
    MoveData(MapPosition p, int newDir){
      this.p = p;
      this.newDir = newDir;
    }
  }
  
  public static int[] movePos = {0, 0};
  public static void main(String[] args) throws Exception {
    String inpFile = "data/input.txt";
    MapPosition[][] map = read_map(inpFile);
    
    int sideLen = Math.max(map.length, map[0].length);
    sideLen /= 4;

    MapFace[] cubedMap = getMapShape(map, sideLen);

    MapPosition realStart = getPos(map, 0, 0);
    linkSides(cubedMap[0], cubedMap[3], 2, 2, true);
    linkSides(cubedMap[0], cubedMap[5], 3, 2, false);
    linkSides(cubedMap[1], cubedMap[5], 3, 1, false);
    linkSides(cubedMap[1], cubedMap[2], 1, 0, false);
    linkSides(cubedMap[1], cubedMap[4], 0, 0, true);
    linkSides(cubedMap[2], cubedMap[3], 2, 3, false);
    linkSides(cubedMap[4], cubedMap[5], 1, 0, false);
    // linkSides(cubedMap[4], cubedMap[1], 1, 1, true);

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
      MoveData data = move(realStart, moveDir, Integer.parseInt(directions[i]));
      realStart = data.p;
      moveDir = data.newDir;
    }

    printMap(map, realStart);
    System.out.println(moveDir);
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
        map[len][i] = new MapPosition(inp.charAt(i), i, len);
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
          currPos.moves[0] = getPos(map, 0, i);
          currPos.moves[0].moves[2] = currPos;
        } else {
          currPos.moves[0] = nextPos;
          currPos.moves[0].moves[2] = currPos;
        }
        p++;
        currPos = getPos(map, p, i);
        nextPos = getPos(map, p+1, i);
      }
    }

    for(int i = 0; i<max; i++){
      int p = 0;
      MapPosition currPos = getYPos(map, i, 0);
      MapPosition nextPos = getYPos(map, i, p+1);
      
      // if(nextPos == null){
      //   System.out.println(i + " ? " + p);
      // }
      // System.out.println(currPos);
      while(currPos != null){
        if(nextPos == null){
          currPos.moves[1] = getYPos(map, i, 0);
          currPos.moves[1].moves[3] = currPos;
        } else {
          currPos.moves[1] = nextPos;
          currPos.moves[1].moves[3] = currPos;
        }
        p++;
        currPos = getYPos(map, i, p);
        nextPos = getYPos(map, i, p+1);
      }
    }

    return map;
  }

  public static MapFace[] getMapShape(MapPosition[][] unformatted, int sideLen) throws Exception{
    MapFace[][] shape = new MapFace[unformatted.length / sideLen][unformatted[0].length / sideLen];
    char[][] mapShape = new char[unformatted.length / sideLen][unformatted[0].length / sideLen];
    for(int i = 0; i<mapShape.length; i++){
      for(int j = 0; j<mapShape[0].length; j++){
        if(unformatted[i * sideLen][j * sideLen] == null){
          mapShape[i][j] = '-';
        } else {
          mapShape[i][j] = 'X';
        }
        shape[i][j] = null;
      }
    }

    int count = 0;
    MapFace[] faceList = new MapFace[6];
    for(int i = 0; i<mapShape.length; i++){
      for(int j = 0; j<mapShape[0].length; j++){
        if(mapShape[i][j] == 'X'){
          faceList[count] = new MapFace(count, sideLen);
          shape[i][j] = faceList[count];
          for(int k = 0; k<sideLen; k++){
            for(int l = 0; l<sideLen; l++){
              shape[i][j].facePos[k][l] = unformatted[(i * sideLen)+k][(j * sideLen)+l];
            }
          }
          faceList[count].setSides();
          count++;
        }
      }
    }
    for(int i = 0; i<shape.length; i++){
      for(int j = 0; j<shape[0].length; j++){
        if(shape[i][j] != null){
          System.out.print(shape[i][j].faceNum);
        } else {
          System.out.print('-');
        }
      }
      System.out.println();
    }

    return faceList;
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

  public static MoveData move(MapPosition startPos, int dir, int dist){
    // 0 : Right
    // 1 : Down
    // 2 : Left
    // 3 : Up
    for(int i = 0; i<dist; i++){
      // System.out.println("old:" + dir);
      // System.out.println(startPos.realpos[0] + ", " + startPos.realpos[1]);
      // System.out.println(startPos.moves[1]);

      if(startPos.moves[dir].isWall){
        continue;
      }
      int newDir = startPos.mDirs[dir];
      startPos = startPos.moves[dir];
      dir = newDir;
      // System.out.println("new:" + dir);
      // System.out.println(startPos.realpos[0] + ", " + startPos.realpos[1]);
      // System.out.println("move: " + dir);
    }

    MoveData newInfo = new MoveData(startPos, dir);
    return newInfo;
  }

  public static void linkSides(MapFace from, MapFace to, int fromDir, int toDir, Boolean reverse){
    if(!reverse){
      for(int i = 0; i<from.sideLen; i++){
        from.sides[fromDir][i].moves[fromDir] = to.sides[toDir][i];
        to.sides[toDir][i].moves[toDir] = from.sides[fromDir][i];
        from.sides[fromDir][i].mDirs[fromDir] = ((toDir + 2) % 4);
        to.sides[toDir][i].mDirs[toDir] = ((fromDir + 2) % 4);
      }
    } else {
      for(int i = 0; i<from.sideLen; i++){
        from.sides[fromDir][i].moves[fromDir] = to.sides[toDir][from.sideLen - i - 1];
        // System.out.println(from.sides[fromDir][i].moves[fromDir]);
        to.sides[toDir][from.sideLen - i - 1].moves[toDir] = from.sides[fromDir][i];
        // System.out.println(to.sides[toDir][from.sideLen - i - 1].moves[toDir]);
        from.sides[fromDir][i].mDirs[fromDir] = ((toDir + 2) % 4);
        to.sides[toDir][i].mDirs[toDir] = ((fromDir + 2) % 4);
      }
    }
  }
}