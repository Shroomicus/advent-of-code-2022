import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
public class DayFive {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    LinkedList<LinkedList<Character>> towers = new LinkedList<LinkedList<Character>>();
    for(int i = 0; i < 9; i++){
      LinkedList<Character> tow = new LinkedList<Character>();
      towers.addFirst(tow);
    }

    for(int i = 0; i < 8; i++){
      String towLine = scan.nextLine();
      for(int towNum = 0; towNum < 9; towNum++){
        char c = towLine.charAt(towNum * 4 + 1);
        // System.out.print(c);
        LinkedList<Character> currTower = towers.get(towNum);
        if(c != ' '){
          currTower.addFirst(c);
        }
      }
      // System.out.println();
    }

    scan.nextLine();
    scan.nextLine();

    for(int i = 0; i < 9; i++){
      for(int j = 0; j<towers.get(i).size(); j++){
        System.out.print(towers.get(i).get(j) + " ");
      }
      System.out.println();
    }

    // while(scan.hasNextLine()){
    //   int[] inst = parseInst(scan.nextLine());
    //   for(int i = 0; i<inst[0]; i++){
    //     char c = towers.get(inst[1]).remove(towers.get(inst[1]).size() - 1);
    //     towers.get(inst[2]).add(c);
    //   }
    // }

    while(scan.hasNextLine()){
      int[] inst = parseInst(scan.nextLine());
      int point = towers.get(inst[2]).size();
      if(point < 0){
        point++;
      }
      for(int i = 0; i<inst[0]; i++){
        char c = towers.get(inst[1]).remove(towers.get(inst[1]).size() - 1);
        towers.get(inst[2]).add(point, c);
      }
    }

    System.out.println();
    for(int i = 0; i < 9; i++){
      for(int j = 0; j<towers.get(i).size(); j++){
        System.out.print(towers.get(i).get(j) + " ");
      }
      System.out.println();
    }

    System.out.println();
    for(int i = 0; i < 9; i++){
      System.out.println(towers.get(i).get(towers.get(i).size() - 1));
    }
  }

  public static int[] parseInst(String inst){
    int[] parsed = new int[3];
    String[] tokenized = inst.split(" ");
    parsed[0] = Integer.parseInt(tokenized[1]);
    parsed[1] = Integer.parseInt(tokenized[3])-1;
    parsed[2] = Integer.parseInt(tokenized[5])-1;

    return parsed;
  }
}
