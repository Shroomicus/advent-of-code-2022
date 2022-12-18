import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DaySixteenpt2Solution {
  /**
   * @param args
   * @throws Exception
   */

   public static class Valve {
    String name;
    String[] pathStrings;
    int[] times;
    int[] valveNums;

    int[] redir;

    private String[] connectStrings;
    private int[] connectNums;
    int flow;

    Valve(String v, int flow){
      name = v;
      this.flow = flow;
    }

    void setPaths(String[] paths){
      connectStrings = new String[paths.length];
      connectNums = new int[paths.length];
      for(int i = 0; i<connectStrings.length; i++){
        connectStrings[i] = paths[i];
      }
    }

    void finishPaths(Valve[] valveList){
      int point = 0;
      for(int i = 0; i<valveList.length; i++){
        if(valveList[i].flow != 0){
          point++;
        }
      }

      pathStrings = new String[point];
      times = new int[point];
      valveNums = new int[point];
      redir = new int[valveList.length];

      point = 0;
      for(int i = 0; i<valveList.length; i++){
        if(valveList[i].flow != 0){
          pathStrings[point] = valveList[i].name;
          times[point] = timeToValve(valveList, valveList[i].name);
          valveNums[point] = valveNum(valveList, valveList[i].name);
          redir[i] = point;
          point++;
        }
      }
    }

    public int timeToValve(Valve[] valveList, String s){
      LinkedList<String> queue = new LinkedList<String>();
      LinkedList<Integer> currMov = new LinkedList<Integer>();
      LinkedList<ArrayList<String>> visited = new LinkedList<ArrayList<String>>();
      for(int i = 0; i<connectStrings.length; i++){
        queue.add(connectStrings[i]);
        currMov.add(1);
        ArrayList<String> visits = new ArrayList<String>();
        visits.add(this.name);
        visited.add(visits);
      }
      while(queue.size() > 0){
        String curr = queue.removeFirst();
        int mov = currMov.removeFirst();
        ArrayList<String> visits = visited.removeFirst();
        if(curr.equals(s)){
          return mov;
        }
        Valve found = findValve(valveList, curr);
        for(int i = 0; i<found.connectStrings.length; i++){
          Boolean exists = false;
          for(int j = 0; j<visits.size(); j++){
            if(found.connectStrings[i].equals(visits.get(j))){
              exists = true;
            }
          }
          if(exists){
            continue;
          }
          queue.addLast(found.connectStrings[i]);
          // System.out.println(found.name + ":" + found.leadsTo[i]);
          currMov.addLast(mov + 1);
          visits.add(found.name);
          visited.add(visits);
        }
      }
      return -1;
    }

    void setNums(Valve[] valves){
      for(int i = 0; i<connectNums.length; i++){
        connectNums[i] = valveNum(valves, connectStrings[i]);
      }
    }

    @Override
    public String toString() {
      String result = "";
      result += "---------------------------------------\n";
      result += name + "\n";
      for(int i = 0; i<pathStrings.length; i++){
        result += times[i] + "\t" + pathStrings[i] + "\t" + valveNums[i] + "\n";
      }
      for(int i = 0; i<redir.length; i++){
        result += redir[i] + ", ";
      }
      result += "\n";
      return result;
    }
  }

  public static String[] ValveNames;
  public static void main(String[] args) throws Exception {
    Valve[] valves = parseValves("data/input.txt");
    for(int i = 0; i<valves.length; i++){
      System.out.println(valves[i]);
    }

    Boolean[][] hasVals = parseInstructions("data/secOuput.txt");
    int[] pressVals = parsePressure("data/secOuput.txt");
    int maxPress = 0;
    int tempi = 0; int tempj = 0;
    for(int i = 0; i<hasVals.length; i++){
      for(int j = i+1; j<hasVals.length; j++){
        Boolean has = false;
        for(int k = 0; k<hasVals[i].length; k++){
          if(hasVals[i][k] && hasVals[j][k]){
            has = true;
            break;
          }
        }
        if(has){
          continue;
        }
        if(maxPress < pressVals[i] + pressVals[j]){
          maxPress = pressVals[i] + pressVals[j];
          System.out.println(pressVals[i]);
          System.out.println(pressVals[j]);
          for(int k = 0; k<hasVals[i].length; k++){
            if(hasVals[i][k]){System.out.print(1);}
            else{System.out.print(0);}
          }
          System.out.println();
          for(int k = 0; k<hasVals[j].length; k++){
            if(hasVals[j][k]){System.out.print(1);}
            else{System.out.print(0);}
          }
          System.out.println();
          System.out.println();
          tempi = i;
          tempj = j;
        }
      }
      // System.out.println(i);
    }
    System.out.println(maxPress);
    System.out.println(tempi);
    System.out.println(tempj);
  }

  public static Boolean has(int[] list, int val){
    for(int i = 0; i<list.length; i++){
      if(list[i] == val){
        return true;
      }
    }
    return false;
  }

  public static int valveNum(Valve[] valves, String s){
    for(int i = 0; i<valves.length; i++){
      if(valves[i].name.equals(s)){
        return i;
      }
    }
    return -1;
  }

  public static Valve findValve(Valve[] valves, String s){
    for(int i = 0; i<valves.length; i++){
      if(valves[i].name.equals(s)){
        return valves[i];
      }
    }
    return null;
  }

  public static Valve[] parseValves(String s) throws Exception{
    Scanner scan = new Scanner(new File(s));
    ArrayList<Valve> parsedValves = new ArrayList<Valve>();
    while(scan.hasNextLine()){
      String[] inp = scan.nextLine().split("Valve | has flow rate=|;|valve");
      inp[4] = inp[4].substring(inp[4].startsWith("s") ? 2 : 1);
      Valve newValve = new Valve(inp[1], Integer.parseInt(inp[2]));
      newValve.setPaths(inp[4].split(", "));
      parsedValves.add(newValve);
    }
    Valve[] parsingValves = new Valve[parsedValves.size()];
    for(int i = 0; i<parsingValves.length; i++){
      parsingValves[i] = parsedValves.get(i);
    }
    for(int i = 0; i<parsingValves.length; i++){
      parsingValves[i].setNums(parsingValves);
      parsingValves[i].finishPaths(parsingValves);
    }
    return parsingValves;
  }

  public static Boolean[][] parseInstructions(String f) throws Exception{
    Scanner scan = new Scanner(new File(f));
    int count = 0;
    while(scan.hasNextLine()){
      scan.nextLine();
      count++;
    }

    Boolean[][] has = new Boolean[count][];
    scan.close();

    scan = new Scanner(new File(f));
    count = 0;
    while(scan.hasNextLine()){
      String[] result = scan.nextLine().split(" PRESSURE:");
      has[count] = new Boolean[result[0].length()];
      for(int i = 0; i<result[0].length(); i++){
        if(result[0].charAt(i) == '1'){
          has[count][i] = true;
        } else {
          has[count][i] = false;
        }
      }
      count++;
    }

    return has;
  }

  public static int[] parsePressure(String f) throws Exception{
    Scanner scan = new Scanner(new File(f));
    int count = 0;
    while(scan.hasNextLine()){
      scan.nextLine();
      count++;
    }

    int[] has = new int[count];
    scan.close();

    scan = new Scanner(new File(f));
    count = 0;
    while(scan.hasNextLine()){
      String[] result = scan.nextLine().split(" PRESSURE:");
      has[count] = Integer.parseInt(result[1]);
      count++;
    }

    return has;
  }
}