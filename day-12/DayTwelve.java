import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
public class DayTwelve {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    LinkedList<int[]> posList = new LinkedList<int[]>();
    LinkedList<Integer> moveList = new LinkedList<Integer>();
    LinkedList<ArrayList<int[]>> dirList = new LinkedList<ArrayList<int[]>>();
    int[][] mountains = new int[41][163];
    int[] s = new int[2];
    ArrayList<int[]> lowests = new ArrayList<int[]>();
    int[] e = new int[2];

    for(int l = 0; l<41; l++){
      String inp = scan.nextLine();
      for(int w = 0; w < 163; w++){
        if(inp.charAt(w) >= 'a'){
          mountains[l][w] = inp.charAt(w) - 'a';
          int[] newPos = {l, w};
          lowests.add(newPos);
        } else {
          if(inp.charAt(w) == 'S'){
            s[0] = l;
            s[1] = w;
            mountains[l][w] = 0;
            lowests.add(s);
          } else {
            e[0] = l;
            e[1] = w;
            mountains[l][w] = 'z' - 'a';
          }
        }
      }
    }
    
    // for(int i = 0; i<lowests.size(); i++){
    //   posList.addFirst(lowests.get(i));
    //   moveList.addFirst(0);
    //   dirList.addFirst(new ArrayList<int[]>());
    //   dirList.get(0).add(lowests.get(i));
    // }

    posList.addFirst(s);
    moveList.addFirst(0);
    dirList.addFirst(new ArrayList<int[]>());
    dirList.get(0).add(s);

    int min = 41 * 163;
    while(moveList.size() > 0){
      int currMov = moveList.removeLast();
      int[] currPos = posList.removeLast();
      ArrayList<int[]> lastMovs = dirList.removeLast();
      // System.out.println(currPos[0] + "\t " + currMov);
      int currVal = mountains[currPos[0]][currPos[1]];
      int[] newPos = new int[2];

      if(newPos[0] < 0 || newPos[0] >= 41){
        continue;
      }

      // System.out.println(currMov);

      if(currVal == 0){
        currMov = 0;
      }

      if(currPos[0] == e[0] && currPos[1] == e[1]){
        // System.out.println("TEST");
        if(min > currMov){
          min = currMov;
        }
      }

      newPos[0] = currPos[0] + 1;
      newPos[1] = currPos[1];
      if(newPos[0] < 41 && isIn(lastMovs, newPos) && mountains[newPos[0]][newPos[1]] <= 1 + currVal){
        int[] addPos = {newPos[0], newPos[1]};
        posList.addFirst(addPos);
        moveList.addFirst(currMov + 1);
        lastMovs.add(addPos);
        dirList.add(lastMovs);
        // System.out.println(currMov);
      }

      newPos[0] = currPos[0] - 1;
      newPos[1] = currPos[1];
      if(newPos[0] >= 0 && isIn(lastMovs, newPos) && mountains[newPos[0]][newPos[1]] <= 1 + currVal){
        int[] addPos = {newPos[0], newPos[1]};
        posList.addFirst(addPos);
        moveList.addFirst(currMov + 1);
        lastMovs.add(addPos);
        dirList.add(lastMovs);
        // System.out.println(currMov);
      }

      newPos[0] = currPos[0];
      newPos[1] = currPos[1] + 1;
      if(newPos[1] < 163 && isIn(lastMovs, newPos) && mountains[newPos[0]][newPos[1]] <= 1 + currVal){
        int[] addPos = {newPos[0], newPos[1]};
        posList.addFirst(addPos);
        moveList.addFirst(currMov + 1);
        lastMovs.add(addPos);
        dirList.add(lastMovs);
        // System.out.println(currMov);
      }

      newPos[0] = currPos[0];
      newPos[1] = currPos[1] - 1;
      if(newPos[1] >= 0 && isIn(lastMovs, newPos) && mountains[newPos[0]][newPos[1]] <= 1 + currVal){
        int[] addPos = {newPos[0], newPos[1]};
        posList.addFirst(addPos);
        moveList.addFirst(currMov + 1);
        lastMovs.add(addPos);
        dirList.add(lastMovs);
        // System.out.println(currMov);
      }
    }

    System.out.println(min);
    System.out.println(41 * 163);
  }

  public static Boolean isIn(ArrayList<int[]> list, int[] pos){
    for(int i = 0; i<list.size(); i++){
      if(list.get(i)[0] == pos[0] && list.get(i)[1] == pos[1]){
        return false;
      }
    }
    return true;
  }
}