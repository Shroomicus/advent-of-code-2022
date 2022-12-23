import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class DayTwentythree {
  /**
   * @param args
   * @throws Exception
   */

  public static class Elf{
    int x;
    int y;
    int propX;
    int propY;
    Boolean willMove = false;
    Elf(int x, int y){
      this.x = x;
      this.y = y;
      propX = x;
      propY = y;
    }

    Boolean checkDir(ArrayList<Elf> elves, int dir){
      // System.out.println(dir);
      switch(dir){
        case 3:
          if(hasPos(elves, x+1, y) || hasPos(elves, x+1, y+1) || hasPos(elves, x+1, y-1)){
            return false;
          }
          break;
        case 1:
          if(hasPos(elves, x, y+1) || hasPos(elves, x+1, y+1) || hasPos(elves, x-1, y+1)){
            return false;
          }
          break;
        case 2:
          if(hasPos(elves, x-1, y) || hasPos(elves, x-1, y+1) || hasPos(elves, x-1, y-1)){
            return false;
          }
          break;
        case 0:
          if(hasPos(elves, x, y-1) || hasPos(elves, x+1, y-1) || hasPos(elves, x-1, y-1)){
            return false;
          }
          break;
      }
      return true;
    }
  
    void proposeDir(ArrayList<Elf> elves, int startDir){
      if(!(hasPos(elves, x, y+1) || hasPos(elves, x+1, y+1) || hasPos(elves, x-1, y+1) || hasPos(elves, x, y-1) || hasPos(elves, x+1, y-1)||hasPos(elves, x-1, y-1)|| hasPos(elves, x-1, y)|| hasPos(elves, x+1, y))){
        propX = x;
        propY = y;
        return;
      }

      for(int i = 0; i<4; i++){
        if(checkDir(elves, startDir)){
          switch(startDir){
            case 3:
              propX = x + 1;
              propY = y;
              break;
            case 1:
              propX = x;
              propY = y + 1;
              break;
            case 2:
              propX = x - 1;
              propY = y;
              break;
            case 0:
              propX = x;
              propY = y - 1;
              break;
          }
          return;
        }
        startDir = (startDir + 1) % 4;
      }
      propX = x;
      propY = y;
    }
  
    void canMove(ArrayList<Elf> elves){
      if(x == propX && y == propY){
        willMove = false;
        return;
      }
      if(hasProp(elves, propX, propY)){
        willMove = false;
        return;
      }
      willMove = true;
    }
  }
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    ArrayList<Elf> elfList = new ArrayList<Elf>();
    int count = 0;
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      for(int i = 0; i<inp.length(); i++){
        if(inp.charAt(i) == '#'){
          Elf nElf = new Elf(i, count);
          elfList.add(nElf);
        }
      }
      count++;
    }

    int move = 0;
    while(moveElves(elfList, move % 4)){
      move++;
    }
    System.out.println(move+1);
    printElves(elfList);
  }

  public static Boolean moveElves(ArrayList<Elf> elfList, int dir){
    for(int i = 0; i<elfList.size(); i++){
      elfList.get(i).proposeDir(elfList, dir);
    }
    Boolean willMove = false;
    for(int i = 0; i<elfList.size(); i++){
      elfList.get(i).canMove(elfList);
      if(elfList.get(i).willMove){
        willMove = true;
      }
    }
    if(!willMove){
      System.out.println("TEST");
      return false;
    }
    for(int i = 0; i<elfList.size(); i++){
      if(elfList.get(i).willMove){
        elfList.get(i).x = elfList.get(i).propX;
        elfList.get(i).y = elfList.get(i).propY; 
      }
    }
    return true;
  }

  public static void printElves(ArrayList<Elf> elfList){
    int minX = elfList.get(0).x;
    int maxX = elfList.get(0).x;
    int minY = elfList.get(0).x;
    int maxY = elfList.get(0).x;
    for(int i = 0; i<elfList.size(); i++){
      if(elfList.get(i).x < minX){
        minX = elfList.get(i).x;
      }
      if(elfList.get(i).x > maxX){
        maxX = elfList.get(i).x;
      }
      if(elfList.get(i).y < minY){
        minY = elfList.get(i).y;
      }
      if(elfList.get(i).y > maxY){
        maxY = elfList.get(i).y;
      }
    }

    int empty = 0;
    for(int j = minY; j<=maxY; j++){
      for(int i = minX; i<=maxX; i++){
        if(hasPos(elfList, i, j)){
          System.out.print("#");
        } else {
          System.out.print(".");
          empty++;
        }
      }
      System.out.println();
    }
    System.out.println(empty);
  }

  public static Boolean hasPos(ArrayList<Elf> elfList, int x, int y){
    for(int i = 0; i<elfList.size(); i++){
      if(elfList.get(i).x == x && elfList.get(i).y == y){
        return true;
      }
    }
    return false;
  }
  public static Boolean hasProp(ArrayList<Elf> elfList, int x, int y){
    int count = 0;
    for(int i = 0; i<elfList.size(); i++){
      if(elfList.get(i).propX == x && elfList.get(i).propY == y){
        count++;
      }
    }
    if(count > 1){
      return true;
    }
    return false;
  }
}