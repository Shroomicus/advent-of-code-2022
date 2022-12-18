import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DaySixteenpt2Parse {
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
    FileWriter writer = new FileWriter("data/secOuput.txt");
    Valve[] valves = parseValves("data/input.txt");
    for(int i = 0; i<valves.length; i++){
      System.out.println(valves[i]);
    }
    
    int[][] inst = parseInstructions("data/output.txt");

    int[] pressures = new int[inst.length];

    // for(int i = 0; i<inst[0].length; i++){
    //   System.out.println(inst[0][i]);
    // }

    Valve firValve = findValve(valves, "AA");

    int maxPressure = 0;
    for(int j = 0; j<inst.length; j++){
      pressures[j] = 0;
      int time = 0;
      time += firValve.times[firValve.redir[inst[j][0]]] + 1;
      pressures[j] += (26 - time) * valves[inst[j][0]].flow;

      if(j == 77440 || j == 159258){
        System.out.println(firValve.name + "->" + firValve.pathStrings[firValve.redir[inst[j][0]]]);
        System.out.println("AddedTime: " + (firValve.times[firValve.redir[inst[j][0]]] + 1));
        System.out.println("AddedPres: " + (26 - time) * valves[inst[j][0]].flow);
      }

      for(int i = 1; i<inst[j].length; i++){
      // for(int i = 1; i<2; i++){
        time += valves[inst[j][i-1]].times[valves[inst[j][i-1]].redir[inst[j][i]]] + 1;
        if(j == 77440 || j == 159258){
          System.out.println("AddedTime: " + (valves[inst[j][i-1]].times[valves[inst[j][i-1]].redir[inst[j][i]]] + 1));
        }
        if(time > 26){
          break;
        }
        pressures[j] += (26 - time) * valves[inst[j][i]].flow;
        if(j == 77440 || j == 159258){
          System.out.println("AddedPres: " + (26 - time) * valves[inst[j][i]].flow);
        }
      }
      if(maxPressure < pressures[j]){
        maxPressure = pressures[j];
      }
      if(j == 77440 || j == 159258){
        System.out.println();
      }
    }
  
    int count = 0;
    for(int i = 0; i<inst.length; i++){
      // if(pressures[i] < maxPressure/2){
      if(pressures[i] < maxPressure/2){
        continue;
      }
      if(count == 17122 || count == 59088){
        for(int k = 0; k<inst[i].length; k++){
          System.out.print(valves[inst[i][k]].name + "->");
        }
        System.out.println();
        System.out.println(i);
        System.out.println(pressures[i]);
      }
      count++;
      for(int j = 0; j<valves.length; j++){
        if(has(inst[i], j)){
          writer.write("1");
        } else {
          writer.write("0");
        }
      }
      writer.write(" PRESSURE:" + Integer.toString(pressures[i]));
      writer.write("\n");
    }
    writer.close();
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

  public static int[][] parseInstructions(String f) throws Exception{
    Scanner scan = new Scanner(new File(f));
    int count = 0;
    while(scan.hasNextLine()){
      scan.nextLine();
      count++;
    }

    int[][] moves = new int[count][];
    scan.close();

    scan = new Scanner(new File(f));
    count = 0;
    while(scan.hasNextLine()){
      String dir[] = scan.nextLine().split("->");
      moves[count] = new int[dir.length-1];
      for(int i = 0; i<moves[count].length; i++){
        moves[count][i] = Integer.parseInt(dir[i]);
      }
      count++;
    }

    return moves;
  }
}