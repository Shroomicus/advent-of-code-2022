import java.io.File;
import java.util.Scanner;
public class DaySeven {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    int[][] trees = new int[99][99];
    for(int j = 0; j<99; j++){
      String treeLine = scan.nextLine();
      for(int i = 0; i<99;i++){
        trees[i][j] = treeLine.charAt(i);
      }
    }
    // int total = 0;
    // for(int i = 0; i<99; i++){
    //   // System.out.println();
    //   for(int j = 0; j<99; j++){
    //     if(isVisible(trees, j, i)){
    //       // System.out.print(1);
    //       total++;
    //     } else {
    //       // System.out.print(0);
    //     }
    //   }
    // }
    // System.out.println(total);

    // System.out.println(scenicScore(trees, 1, 2));
    int max = 0;
    for(int i = 0; i<99; i++){
      for(int j = 0; j<99; j++){
        if(scenicScore(trees, i, j) > max){
          max = scenicScore(trees, i, j);
        }
      }
    }
    System.out.println(max);
  }

  public static int scenicScore(int[][] trees, int x, int y){
    if(x == 0 || y == 0 || x == trees.length-1 || y == trees[0].length-1){
      return 0;
    }
    int[] vals = new int[4];
    int treeVal = trees[x][y];
    for(int i = 0; i<4; i++){
      vals[i] = 0;
    }
    for(int i = x+1; i < trees.length; i++){
      vals[0]++;
      if(trees[i][y] >= treeVal){
        break;
      }
    }
    for(int i = x-1; i >= 0; i--){
      vals[1]++;
      if(trees[i][y] >= treeVal){
        break;
      }
    }
    for(int i = y+1; i < trees[0].length; i++){
      vals[2]++;
      if(trees[x][i] >= treeVal){
        break;
      }
    }
    for(int i = y-1; i >= 0; i--){
      vals[3]++;
      if(trees[x][i] >= treeVal){
        break;
      }
    }
    return vals[0] * vals[1] * vals[2] * vals[3];
  }
    
  public static Boolean isVisible(int[][] trees, int x, int y){
    if(x == 0 || y == 0 || x == trees.length-1 || y == trees[0].length-1){
      return true;
    }
    int treeVal = trees[x][y];
    for(int i = 0; i < x; i++){
      if(treeVal <= trees[i][y]){
        break;
      }
      if(i == x-1){
        return true;
      }
    }
    for(int i = trees.length-1; i > x; i--){
      if(treeVal <= trees[i][y]){
        break;
      }
      if(i == x+1){
        return true;
      }
    }
    for(int i = 0; i < y; i++){
      if(treeVal <= trees[x][i]){
        break;
      }
      if(i == y-1){
        return true;
      }
    }
    for(int i = trees.length-1; i > y; i--){
      if(treeVal <= trees[x][i]){
        break;
      }
      if(i == y+1){
        return true;
      }
    }
    return false;
  }
}
