import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DaySixteenpt2try2 {
  /**
   * @param args
   * @throws Exception
   */

   public static class Valve {
    String name;
    String[] pathStrings;
    int[] times;
    int[] valveNums;

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

      point = 0;
      for(int i = 0; i<valveList.length; i++){
        if(valveList[i].flow != 0){
          pathStrings[point] = valveList[i].name;
          times[point] = timeToValve(valveList, valveList[i].name);
          valveNums[point] = valveNum(valveList, valveList[i].name);
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
      return result;
    }
  }

  public static String[] ValveNames;
  public static void main(String[] args) throws Exception {
    FileWriter writer = new FileWriter("data/output.txt");

    Valve[] valves = parseValves("data/input.txt");
    for(int i = 0; i<valves.length; i++){
      System.out.println(valves[i]);
    }
    Valve startValve = findValve(valves, "AA");
    LinkedList<int[]> visited = new LinkedList<int[]>();
    LinkedList<Valve> queue = new LinkedList<Valve>();
    LinkedList<Integer> time = new LinkedList<Integer>();
    time.add(0);
    queue.add(startValve);
    int[] startVisits = new int[0];
    visited.add(startVisits);

    int half = startValve.pathStrings.length/2;
    System.out.println(half);
    while(queue.size() > 0){
      Valve setQueue = queue.removeFirst();
      int[] setVisits = visited.removeFirst();
      int setTime = time.removeFirst();
      if(setVisits.length >= half){
        for(int i = 0; i<setVisits.length; i++){
          writer.write(Integer.toString(setVisits[i]) + "->");
          // writer.write(valves[setVisits[i]].name + "->");
        }
        writer.write(" TIME:" + Integer.toString(setTime));
        writer.write("\n");
        if(setVisits.length == half + 1){
          continue;
        }
      }
      if(setTime > 26){
        // for(int i = 0; i<setVisits.length; i++){
        //   writer.write(Integer.toString(setVisits[i]) + "->");
        //   // writer.write(valves[setVisits[i]].name + "->");
        // }
        // writer.write(" TIME:" + Integer.toString(setTime));
        // writer.write("\n");
        // System.out.println("OVERTIME");
        continue;
      }
      // for(int i = 0; i<setVisits.size(); i++){
      //   System.out.print(setVisits.get(i) + " : ");
      // }
      // System.out.println();
      for(int i = 0; i<setQueue.pathStrings.length; i++){
        if(setQueue.times[i] == -1){
          continue;
        }
        if(has(setVisits, setQueue.valveNums[i])){
          continue;
        }
        queue.add(valves[setQueue.valveNums[i]]);
        int[] newVisits = new int[setVisits.length+1];
        for(int j = 0; j<setVisits.length; j++){
          newVisits[j] = setVisits[j];
        }
        newVisits[newVisits.length-1] = setQueue.valveNums[i];
        visited.add(newVisits);
        time.add(setTime + setQueue.times[i]+1);
      }
    }
    writer.close();
    System.out.println("AAAAAAA");
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
}