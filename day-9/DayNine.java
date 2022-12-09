import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;
public class DayNine {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    int[][] knots = new int[10][2];
    for(int i = 0; i<knots.length; i++){
      knots[i][0] = 0;
      knots[i][1] = 0;
    }

    // int[] head = {0, 0};
    // int[] tail = {0, 0};
    // LinkedList<int[]> visited = new LinkedList<int[]>();
    ArrayList<int[]> visited = new ArrayList<int[]>();
    int[] startpos = {0, 0};
    visited.add(startpos);

    int count = 0;

    while(scan.hasNextLine()){
      String[] inp = scan.nextLine().split(" ");
      int[] hMove = move(inp[0], Integer.parseInt(inp[1]));
      for(int i = 0; i<Integer.parseInt(inp[1]); i++){

        knots[0][0] += hMove[0];
        knots[0][1] += hMove[1];
        for(int j = 1; j<knots.length; j++){
          knots[j] = updatedPos(knots[j-1], knots[j]);
        }

        // int[] lastHead = {head[0], head[1]};
        // head[0] += hMove[0];
        // head[1] += hMove[1]; 
        // if(Math.abs(head[0] - tail[0]) > 1 || Math.abs(head[1] - tail[1]) > 1){
        //   tail[0] = lastHead[0];
        //   tail[1] = lastHead[1];
        // }
        int[] pos = {knots[9][0], knots[9][1]};
        Boolean has = false;
        for(int j = 0; j < visited.size(); j++){
          if(visited.get(j)[0] == pos[0] && visited.get(j)[1] == pos[1]){
            has = true;
          }
        }
        if(!has){
          visited.add(pos);
        }
      }
      // if(count < 9 && count >= 7){
        // System.out.println(inp[0] + " " + inp[1]);
        // display(knots, visited);
        // System.out.println();
      // }
    }
    System.out.println(visited.size());
  }

  public static int[] move(String s, int m){
    int[] movement = {0, 0};
    switch(s.charAt(0)){
      case 'L':
        movement[0] -= 1;
        break;
      case 'R':
        movement[0] += 1;
        break;
      case 'D':
        movement[1] -= 1;
        break;
      case 'U':
        movement[1] += 1;
        break;
    }
    return movement;
  }

  public static int[] updatedPos(int[] head, int[] tail){
    int xdiff = (head[0] - tail[0]);
    int ydiff = (head[1] - tail[1]);

    if(Math.abs(xdiff) > 1 && Math.abs(ydiff) > 1){
      if(xdiff > 1){
        tail[0] += 1;
      } else {
        tail[0] -= 1;
      }

      if(ydiff > 1){
        tail[1] += 1;
      } else {
        tail[1] -= 1;
      }
      return tail;
    }
    if(Math.abs(xdiff) > 1){
      if(xdiff > 1){
        tail[0] += 1;
      } else {
        tail[0] -= 1;
      }
      tail[1] = head[1];
    }
    
    if(Math.abs(ydiff) > 1){
      if(ydiff > 1){
        tail[1] += 1;
      } else {
        tail[1] -= 1;
      }
      tail[0] = head[0];
    }
    return tail;
  }

  public static void display(int[][] positions, ArrayList<int[]> visited){
    int max = 10;
    for(int i = -max; i < max; i++){
      for(int j = -max; j < max; j++){
        int[] point = {j, -i};
        if(hasPoint(positions, j, i) > -1){
          System.out.print(hasPoint(positions, j, i));
          continue;
        } else if(i == 0 && j == 0){
          System.out.print('=');
          continue;
        }
        Boolean has = false;
        for(int k = 0; k < visited.size(); k++){
          if(visited.get(k)[0] == point[0] && visited.get(k)[1] == point[1]){
            System.out.print('?');
            has = true;
            break;
          }
        }
        if(has){
          continue;
        }
        System.out.print('-');
      }
      System.out.println();
    }
  }

  public static int hasPoint(int[][] positions, int x, int y){
    y *= -1;
    for(int i = 0; i<positions.length; i++){
      if(positions[i][0] == x && positions[i][1] == y){
        return i;
      }
    }
    return -1;
  }
}