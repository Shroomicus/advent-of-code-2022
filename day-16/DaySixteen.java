import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DaySixteen {
  /**
   * @param args
   * @throws Exception
   */

  public static class Valve {
    Boolean isOpen = false;

    String name;
    int flowRate;
    String[] leadsTo;
    ArrayList<Valve> valvePaths;
    ArrayList<Integer> moveTimes;

    Valve(Valve v){
      isOpen = v.isOpen;
      name = v.name;
      flowRate = v.flowRate;
      valvePaths = v.valvePaths;
      moveTimes = v.moveTimes;
    }

    Valve(String name, int flowRate, String[] leadsTo){
      this.name = name;
      this.flowRate = flowRate;
      this.leadsTo = leadsTo;
    }

    public void setPaths(ArrayList<Valve> otherValves){
      moveTimes = new ArrayList<Integer>(otherValves.size()-1);
      valvePaths = new ArrayList<Valve>(otherValves.size()-1);

      for(int i = 0; i<otherValves.size(); i++){
        if(otherValves.get(i).name.equals(this.name)){
          continue;
        }
        if(otherValves.get(i).flowRate == 0){
          continue;
        }
        valvePaths.add(otherValves.get(i));
        moveTimes.add(timeToValve(otherValves, otherValves.get(i).name));
      }
    }

    public int timeToValve(ArrayList<Valve> valveList, String s){
      LinkedList<String> queue = new LinkedList<String>();
      LinkedList<Integer> currMov = new LinkedList<Integer>();
      LinkedList<ArrayList<String>> visited = new LinkedList<ArrayList<String>>();
      for(int i = 0; i<leadsTo.length; i++){
        queue.add(leadsTo[i]);
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
        for(int i = 0; i<found.leadsTo.length; i++){
          Boolean exists = false;
          for(int j = 0; j<visits.size(); j++){
            if(found.leadsTo[i].equals(visits.get(j))){
              exists = true;
            }
          }
          if(exists){
            continue;
          }
          queue.addLast(found.leadsTo[i]);
          // System.out.println(found.name + ":" + found.leadsTo[i]);
          currMov.addLast(mov + 1);
          visits.add(found.name);
          visited.add(visits);
        }
      }
      return -1;
    }
  
    @Override
    public String toString() {
      String result = "";
      result += "NAME :\t" + name + "\n";
      result += "RATE :\t" + flowRate + "\n";
      result += "TIMES :\n";
      for(int i = 0; i<moveTimes.size(); i++){
        result += valvePaths.get(i).name + " : " + moveTimes.get(i) + "\n";
      }
      return result;
    }
  }

  public static void main(String[] args) throws Exception {
    ArrayList<Valve> valves = parseValves();
    Valve startValve = findValve(valves, "AA");
    // System.out.println(newValve);

    LinkedList<Valve> queue = new LinkedList<Valve>();
    LinkedList<Integer> time = new LinkedList<Integer>();
    LinkedList<ArrayList<Valve>> visited = new LinkedList<ArrayList<Valve>>();
    LinkedList<Integer> pressure = new LinkedList<Integer>();

    queue.add(startValve);
    time.add(0);
    ArrayList<Valve> startVisited = new ArrayList<Valve>();
    startVisited.add(findValve(valves, "AA"));
    visited.add(startVisited);
    pressure.add(0);
    int maxFlow = 0;
    ArrayList<Valve> visitPath = startVisited;

    int empty = 0;
    for(int i = 0; i<valves.size(); i++){
      if(valves.get(i).flowRate==0){
        empty++;
      }
    }
    
    while(queue.size()>0){
      Valve currValve = new Valve(queue.removeFirst());
      int currTime = time.removeFirst();
      int currPressure = pressure.removeFirst();
      ArrayList<Valve> currVisited = visited.removeFirst();

      int offset = currValve.flowRate;

      if(currTime > 30){
        continue;
      }
      if(currPressure + (30 - currTime) * (totalFlow(currVisited) - offset) > maxFlow){
        maxFlow = currPressure + (30 - currTime) * (totalFlow(currVisited) - offset);
        System.out.println(totalFlow(currVisited) - offset);
        visitPath = currVisited;
      }

      if(currValve.flowRate != 0 && !currValve.isOpen){
        currPressure += totalFlow(currVisited) - currValve.flowRate;
        currTime++;
        currValve.isOpen = true;
        // if(currValve.name.equals("BB")){
        //   System.out.println(totalFlow(currVisited));
        // }
      }

      for(int i = 0; i<currValve.valvePaths.size(); i++){
        Valve newValve = currValve.valvePaths.get(i);
        int moveTime = currValve.moveTimes.get(i);
        int newTime = currTime;
        int newPressure = currPressure;
        ArrayList<Valve> newVisited = new ArrayList<Valve>();
        Boolean exists = false;
        for(int v = 0; v < currVisited.size(); v++){
          if(currVisited.get(v).name.equals(newValve.name)){
            exists = true;
          }
          newVisited.add(currVisited.get(v));
        }
        if(exists){
          continue;
        }

        for(int m = 0; m<moveTime; m++){
          newTime++;
          newPressure += totalFlow(newVisited);
        }
        if(newTime > 30){
          continue;
        }

        queue.addLast(newValve);
        time.addLast(newTime);
        pressure.addLast(newPressure);
        newVisited.add(newValve);
        visited.addLast(newVisited);

        // Testing Stuff
        // Thread.sleep(300);
        // if(newTime != 10){
        //   continue;
        // }
        // if(!(currValve.name.equals("BB"))){
        //   continue;
        // }
        // if(!(newValve.name.equals("JJ"))){
        //   continue;
        // }
        // System.out.println(currValve.name + " -> " + newValve.name + " : T=" + newTime + " : P=" + newPressure);
      }
    }
    System.out.println(maxFlow);
    for(int i = 0; i < visitPath.size(); i++){
      System.out.println(visitPath.get(i).name);
    }
  }

  public static int totalFlow(ArrayList<Valve> valves){
    int total = 0;
    for(int i = 0; i<valves.size(); i++){
      total += valves.get(i).flowRate;
    }
    return total;
  }

  public static Valve findValve(ArrayList<Valve> valves, String s){
    for(int i = 0; i<valves.size(); i++){
      if(valves.get(i).name.equals(s)){
        return valves.get(i);
      }
    }
    return null;
  }

  public static ArrayList<Valve> parseValves() throws Exception{
    Scanner scan = new Scanner(new File("data/input.txt"));
    ArrayList<Valve> parsedValves = new ArrayList<Valve>();
    while(scan.hasNextLine()){
      String[] inp = scan.nextLine().split("Valve | has flow rate=|;|valve");
      inp[4] = inp[4].substring(inp[4].startsWith("s") ? 2 : 1);
      // for(int i = 0; i<inp.length; i++){
      //   System.out.println(i + " : " + inp[i]);
      // }
      Valve newValve = new Valve(inp[1], Integer.parseInt(inp[2]), inp[4].split(", "));
      parsedValves.add(newValve);
    }
    for(int i = 0; i<parsedValves.size(); i++){
      parsedValves.get(i).setPaths(parsedValves);
    }
    return parsedValves;
  }
}