import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
public class DayTwentyfour {
  /**
   * @param args
   * @throws Exception
   */
  public static class BlizzardMap{
    ArrayList<int[]> walls;
    ArrayList<Blizzard> blizzards = new ArrayList<Blizzard>();

    char[][] visualMap;
    Boolean[][] moveMap;

    BlizzardMap(int width, int height){
      walls = new ArrayList<int[]>();
      moveMap = new Boolean[height][width];
      visualMap = new char[height][width];
    }
    
    void parseItems(){
      for(int i = 0; i<moveMap.length; i++){
        for(int j = 0; j<moveMap[0].length; j++){
          moveMap[i][j] = false;
          visualMap[i][j] = '.';
        }
      }
      for(int i = 0; i<walls.size(); i++){
        int[] pos = walls.get(i);
        moveMap[pos[1]][pos[0]] = true;
        visualMap[pos[1]][pos[0]] = '#';
      }
      for(int i = 0; i<blizzards.size(); i++){
        int[] pos = blizzards.get(i).pos;
        moveMap[pos[1]][pos[0]] = true;
        char charAt = visualMap[pos[1]][pos[0]];
        if(charAt != '.'){
          if(charAt == '>' || charAt == '<' || charAt == '^' || charAt == 'v'){
            visualMap[pos[1]][pos[0]] = '2';
          } else {
            visualMap[pos[1]][pos[0]]++;
          }
          continue;
        }
        visualMap[pos[1]][pos[0]] = blizzards.get(i).visDir;
      }
    }

    void advanceBlizzards(){
      for(int i = 0; i<blizzards.size(); i++){
        blizzards.get(i).advance(moveMap[0].length, moveMap.length);
      }
    }

    void printMap(){
      for(int i = 0; i<visualMap.length; i++){
        for(int j = 0; j<visualMap[0].length; j++){
          System.out.print(visualMap[i][j]);
        }
        System.out.println();
      }
    }
    void printMap(MapPos p){
      for(int i = 0; i<visualMap.length; i++){
        for(int j = 0; j<visualMap[0].length; j++){
          if(j == p.pos[0] && i == p.pos[1]){
            System.out.print('E');
            continue;
          }
          System.out.print(visualMap[i][j]);
        }
        System.out.println();
      }
    }
  }

  // 0 : Right
  // 1 : Down
  // 2 : Left
  // 3 : Up 
  public static class Blizzard{
    int[] pos = new int[2];
    char visDir;
    int[] move = new int[2];
    Blizzard(int x, int y, char dir){
      pos[0] = x;
      pos[1] = y;
      visDir = dir;
      move[0] = 0;
      move[1] = 0;
      switch(dir){
        case '>':
          move[0] = 1;
          break;
        case 'v':
          move[1] = 1;
          break;
        case '<':
          move[0] = -1;
          break;
        case '^':
          move[1] = -1;
          break;
      }
    }

    void advance(int maxX, int maxY){
      pos[0] += move[0];
      pos[1] += move[1];
      if(pos[0] == 0){
        pos[0] = maxX - 2;
        return;
      }
      if(pos[1] == 0){
        pos[1] = maxY - 2;
        return;
      }
      if(pos[0] == maxX - 1){
        pos[0] = 1;
        return;
      }
      if(pos[1] == maxY - 1){
        pos[1] = 1;
        return;
      }
    }
  }

  public static class MapPos{
    int[] pos = new int[2];
    int time = 0;
    int cp = 0;
    MapPos(int x, int y, int time, int cp){
      pos[0] = x;
      pos[1] = y;
      this.time = time;
      this.cp = cp;
    }

    @Override
    public boolean equals(Object obj) {
      if(obj instanceof MapPos){
        MapPos pos = (MapPos) obj;
        return equals(pos);
      }
      return super.equals(obj);
    }
    Boolean equals(MapPos o){
      if(pos[0] == o.pos[0] && pos[1] == o.pos[1] && time == o.time && cp == o.cp){
        // System.out.println("????");
        return true;
      }
      return false;
    }
  }

  public static void main(String[] args) throws Exception {
    BlizzardMap fullMap = readMap("data/input.txt");
    MapPos start = new MapPos(1, 0, 0, 0);
    fullMap.advanceBlizzards();
    fullMap.parseItems();
    fullMap.printMap();
    
    LinkedList<MapPos> queue = new LinkedList<MapPos>();
    int lastTime = 0;
    queue.add(start);
    while(queue.size() > 0){
      MapPos currPos = queue.removeFirst();


      if(currPos.time != lastTime){
        lastTime = currPos.time;
        // System.out.println(lastTime);
        fullMap.advanceBlizzards();
        fullMap.parseItems();
      }
      // Thread.sleep(1000);
      // System.out.println(currPos.time);
      // fullMap.printMap(currPos);
      queue.addAll(getMoves(currPos, fullMap, queue));
      if(currPos.cp == 0 && currPos.pos[0] == fullMap.moveMap[0].length - 2 && currPos.pos[1] == fullMap.moveMap.length - 1){
        System.out.println(currPos.time);
        currPos.cp++;
        queue = new LinkedList<MapPos>();
        queue.add(currPos);
        // break;
      }
      if(currPos.cp == 1 && currPos.pos[0] == 1 && currPos.pos[1] == 0){
        System.out.println(currPos.time);
        currPos.cp++;
        queue = new LinkedList<MapPos>();
        queue.add(currPos);
      }
      if(currPos.cp == 2 && currPos.pos[0] == fullMap.moveMap[0].length - 2 && currPos.pos[1] == fullMap.moveMap.length - 1){
        System.out.println(currPos.time);
        break;
      }
    }
  }

  public static LinkedList<MapPos> getMoves(MapPos currPos, BlizzardMap fullMap, LinkedList<MapPos> otherMoves){
    LinkedList<MapPos> moves = new LinkedList<MapPos>();
    if(!fullMap.moveMap[currPos.pos[1]][currPos.pos[0]]){
      MapPos newMove = new MapPos(currPos.pos[0], currPos.pos[1], currPos.time+1, currPos.cp);
      if(!otherMoves.contains(newMove)){
        moves.add(newMove);
      }
    }
    if(!fullMap.moveMap[currPos.pos[1]][currPos.pos[0]+1]){
      MapPos newMove = new MapPos(currPos.pos[0]+1, currPos.pos[1], currPos.time+1, currPos.cp);
      if(!otherMoves.contains(newMove)){
        moves.add(newMove);
      }
    }
    if(!fullMap.moveMap[currPos.pos[1]][currPos.pos[0]-1]){
      MapPos newMove = new MapPos(currPos.pos[0]-1, currPos.pos[1], currPos.time+1, currPos.cp);
      if(!otherMoves.contains(newMove)){
        moves.add(newMove);
      }
    }
    if(currPos.pos[1]+1 < fullMap.moveMap.length && !fullMap.moveMap[currPos.pos[1]+1][currPos.pos[0]]){
      MapPos newMove = new MapPos(currPos.pos[0], currPos.pos[1]+1, currPos.time+1, currPos.cp);
      if(!otherMoves.contains(newMove)){
        moves.add(newMove);
      }
    }
    if(currPos.pos[1]-1 >= 0 && !fullMap.moveMap[currPos.pos[1]-1][currPos.pos[0]]){
      MapPos newMove = new MapPos(currPos.pos[0], currPos.pos[1]-1, currPos.time+1, currPos.cp);
      if(!otherMoves.contains(newMove)){
        moves.add(newMove);
      }
    }
    return moves;
  }

  public static BlizzardMap readMap(String file) throws Exception{
    Scanner scan = new Scanner(new File(file));
    int totalLines = 1;
    int mapWidth = 0;
    mapWidth = scan.nextLine().length();
    while(scan.hasNextLine()){
      totalLines++;
      scan.nextLine();
    }
    
    BlizzardMap fullMap = new BlizzardMap(mapWidth, totalLines);
    scan = new Scanner(new File(file));
    totalLines = 0;
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      for(int i = 0; i<mapWidth; i++){
        if(inp.charAt(i) == '#'){
          int[] newWall = new int[2];
          newWall[0] = i;
          newWall[1] = totalLines;
          fullMap.walls.add(newWall);
          continue;
        }
        if(inp.charAt(i) != '.'){
          Blizzard newBlizzard = new Blizzard(i, totalLines, inp.charAt(i));
          fullMap.blizzards.add(newBlizzard);
        }
      }
      totalLines++;
    }

    return fullMap;
  }
}