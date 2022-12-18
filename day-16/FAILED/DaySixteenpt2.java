import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DaySixteenpt2 {
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

    public Boolean equals(String s){
      return name.equals(s);
    }

    public Boolean equals(Valve s){
      return name.equals(s.name);
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

    public int valveDist(String s){
      for(int i = 0; i<valvePaths.size(); i++){
        if(valvePaths.get(i).equals(s)){
          return moveTimes.get(i);
        }
      }
      return -1;
    }

    public int valveDist(Valve s){
      for(int i = 0; i<valvePaths.size(); i++){
        if(valvePaths.get(i).equals(s)){
          return moveTimes.get(i);
        }
      }
      return -1;
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

  public static class ValveMovement{
    ArrayList<Valve> visited = new ArrayList<Valve>();
    ArrayList<Valve> notVisited = new ArrayList<Valve>();
    Valve[] first = new Valve[2];
    int firDistLeft;
    Valve[] second = new Valve[2];
    int secDistLeft;

    int lNum = 0;
    int rNum = 0;

    int pressure = 0;
    int addedPressure = 0;
    int time = 0;

    ValveMovement(Valve firfrom, Valve secfrom, Valve firto, Valve secto){
      first[0] = new Valve(firfrom);
      first[1] = new Valve(firto);
      firDistLeft = first[0].valveDist(first[1]);
      second[0] = new Valve(secfrom);
      second[1] = new Valve(secto);
      secDistLeft = second[0].valveDist(second[1]);
    }

    ValveMovement(Valve firfrom, Valve secfrom, Valve firto, Valve secto, ArrayList<Valve> visited, ArrayList<Valve> notVisited, int pressure, int time, int addedPressure, int firDistLeft, int secDistLeft, int lNum, int rNum){
      this(firfrom, secfrom, firto, secto);
      this.first[0].isOpen = firfrom.isOpen;
      this.second[0].isOpen = secfrom.isOpen;
      this.firDistLeft = firDistLeft;
      this.secDistLeft = secDistLeft;
      for(int i = 0; i<visited.size(); i++){
        this.visited.add(visited.get(i));
      }
      for(int i = 0; i<notVisited.size(); i++){
        this.notVisited.add(notVisited.get(i));
      }

      this.pressure = pressure;
      this.time = time;
      this.addedPressure = addedPressure;
      this.lNum = lNum;
      this.rNum = rNum;
    }

    void updateFirst(Valve newFirst){
      first[0] = first[1];
      first[1] = new Valve(newFirst);
      firDistLeft = first[0].valveDist(first[1]);
      addVisited(new Valve(first[1]));
      lNum++;
    }

    void updateSecond(Valve newSecond){
      second[0] = second[1];
      second[1] = new Valve(newSecond);
      secDistLeft = second[0].valveDist(second[1]);
      addVisited(new Valve(second[1]));
      rNum++;
    }

    void addVisited(Valve added){
      if(findValve(visited, added) != null){
        return;
      }
      visited.add(new Valve(added));
      for(int i = 0; i<notVisited.size(); i++){
        if(notVisited.get(i).equals(added)){
          notVisited.remove(i);
        }
      }
    }
  
    void setNotVisited(ArrayList<Valve> valveList){
      for(int i = 0; i<valveList.size(); i++){
        if(valveList.get(i).flowRate == 0){
          continue;
        }
        notVisited.add(new Valve(valveList.get(i)));
      }
      for(int i = 0; i<2; i++){
        addVisited(new Valve(first[i]));
        addVisited(new Valve(second[i]));
      }
    }

    void incMoves(){
      if(!first[0].isOpen && first[0].flowRate != 0 && first[0].valveDist(first[1]) == firDistLeft){
        addedPressure += first[0].flowRate;
        pressure -= first[0].flowRate;
        firDistLeft++;
        first[0].isOpen = true;
      }
      if(!second[0].isOpen && first[0].flowRate != 0 && second[0].valveDist(second[1]) == secDistLeft){
        addedPressure += second[0].flowRate;
        pressure -= second[0].flowRate;
        secDistLeft++;
        second[0].isOpen = true;
      }
      int min = Math.min(firDistLeft, secDistLeft);
      if(notVisited.size() == 0){
        // System.out.println(pressure + " : " + addedPressure + " : " + time);

        for(int i = 0; i<min; i++){
          time++;
          pressure += addedPressure;
          firDistLeft -= 1;
          secDistLeft -= 1;
        }
        if(firDistLeft == 0 && secDistLeft == 0){
          pressure += addedPressure;
          addedPressure += first[1].flowRate + second[1].flowRate;
          pressure -= addedPressure;
          for(int i = time; i<26; i++){
            time++;
            pressure += addedPressure;
          }
        } else if(firDistLeft == 0){
          pressure += addedPressure;
          addedPressure += first[1].flowRate;
          pressure -= addedPressure;
          for(int i = 0; i<secDistLeft; i++){
            pressure += addedPressure;
            time++;
          }
          pressure += addedPressure;
          addedPressure += second[1].flowRate;
          pressure -= addedPressure;
          for(int i = time; i<26; i++){
            time++;
            pressure += addedPressure;
          }
        } else if(secDistLeft == 0){
          pressure += addedPressure;
          addedPressure += second[1].flowRate;
          pressure -= addedPressure;
          for(int i = 0; i<firDistLeft; i++){
            pressure += addedPressure;
            time++;
          }
          pressure += addedPressure;
          addedPressure += first[1].flowRate;
          pressure -= addedPressure;
          for(int i = time; i<26; i++){
            time++;
            pressure += addedPressure;
          }
        }
        
        return;
      }
      time += min;
      pressure += min * addedPressure;
      firDistLeft -= min;
      secDistLeft -= min;
    }

    LinkedList<ValveMovement> updateMoves(){
      incMoves();
      LinkedList<ValveMovement> updatedMoves = new LinkedList<ValveMovement>();
      // System.out.println(toString());
      // time++;
      // pressure += addedPressure;
      if(notVisited.size() == 0 && !(time >= 26)){
        ValveMovement newValve = new ValveMovement(first[0], second[0], first[1], second[1], visited, notVisited, pressure, time, addedPressure, firDistLeft, secDistLeft, lNum, rNum);
        updatedMoves.add(newValve);
      }
      if(secDistLeft != 0){
        for(int i = 0; i<notVisited.size(); i++){
          ValveMovement newValve = new ValveMovement(first[0], second[0], first[1], second[1], visited, notVisited, pressure, time, addedPressure, firDistLeft, secDistLeft, lNum, rNum);
          newValve.updateFirst(notVisited.get(i));
          updatedMoves.add(newValve);
        }
      }
      if(firDistLeft != 0){
        for(int i = 0; i<notVisited.size(); i++){
          ValveMovement newValve = new ValveMovement(first[0], second[0], first[1], second[1], visited, notVisited, pressure, time, addedPressure, firDistLeft, secDistLeft, lNum, rNum);
          newValve.updateSecond(notVisited.get(i));
          updatedMoves.add(newValve);
        }
      }
      if(firDistLeft == 0 && secDistLeft == 0){
        for(int i = 0; i<notVisited.size(); i++){
          for(int j = i+1; j<notVisited.size(); j++){
            ValveMovement newValve = new ValveMovement(first[0], second[0], first[1], second[1], visited, notVisited, pressure, time, addedPressure, firDistLeft, secDistLeft, lNum, rNum);
            newValve.updateFirst(notVisited.get(i));
            newValve.updateSecond(notVisited.get(j));
            updatedMoves.add(newValve);
            ValveMovement secNewValve = new ValveMovement(first[0], second[0], first[1], second[1], visited, notVisited, pressure, time, addedPressure, firDistLeft, secDistLeft, lNum, rNum);
            secNewValve.updateFirst(notVisited.get(j));
            secNewValve.updateSecond(notVisited.get(i));
            updatedMoves.add(secNewValve);
          }
        }
      }
      return updatedMoves;
    }

    int getPressure(int totalTime){
      return pressure + addedPressure * (totalTime-time);
    }

    @Override
    public String toString() {
      String result = "";
      result += "-----------------------------\n";
      result += first[0].name + " -> " + first[1].name + " : " + firDistLeft + "\n";
      result += second[0].name + " -> " + second[1].name + " : " + secDistLeft + "\n";
      for(int i = 0; i<visited.size(); i++){
        result += visited.get(i).name;
        if(i != visited.size()-1){
          result += ", ";
        }
      }
      result += "\n";
      for(int i = 0; i<notVisited.size(); i++){
        result += notVisited.get(i).name;
        if(i != notVisited.size()-1){
          result += ", ";
        }
      }
      result += "\n";
      result += "Time : " + time + "\n";
      result += "Pressure : " + pressure + "\n";
      result += "Adding Pressure : " + addedPressure + "\n";
      result += "-----------------------------\n";
      return result;
    }
  }

  public static void main(String[] args) throws Exception {
    ArrayList<Valve> valves = parseValves();
    Valve startValve = findValve(valves, "AA");

    LinkedList<ValveMovement> moves = addValves(valves, startValve);

    int max = 0;
    while(moves.size() > 0){
      moves.addAll(moves.get(0).updateMoves());
      ValveMovement currMov = moves.removeFirst();
      if(currMov.time > 26){
        continue;
      }
      if(currMov.lNum > 4 || currMov.rNum > 4){
        continue;
      }
      System.out.println(currMov.lNum);
      // currMov.incMoves();

      // AA -> JJ -> BB -> CC
      // AA -> DD -> HH -> EE
      // if(checkMove(currMov, "AA", "JJ", "AA", "DD")){
      //   System.out.println(count + "\n" + currMov);
      // }
      // if(checkMove(currMov, "DD", "HH", "AA", "JJ")){
      //   System.out.println(count + "\n" + currMov);
      // }
      // if(checkMove(currMov, "JJ", "BB", "DD", "HH")){
      //   System.out.println(count + "\n" + currMov);
      // }
      // if(count != 0){
      //   count++;
      //   continue;
      // }
      // System.out.println(count + "\n" + currMov);

      if(currMov.getPressure(26) > max){
        max = currMov.getPressure(26);
      }
    }
    System.out.println(max);

    // for(int i = 0; i<moves.size(); i++){
    //   System.out.println(moves.get(i));
    // }
  }

  public static Boolean checkMove(ValveMovement curr, String firfrom, String firto, String secfrom, String secto){
    if(curr.first[0].equals(firfrom) && curr.first[1].equals(firto) && curr.second[0].equals(secfrom) && curr.second[1].equals(secto)){
      return true;
    }
    if(curr.first[0].equals(secfrom) && curr.first[1].equals(secto) && curr.second[0].equals(firfrom) && curr.second[1].equals(firto)){
      return true;
    }
    return false;
  }

  public static LinkedList<ValveMovement> addValves(ArrayList<Valve> valves, Valve startValve){
    LinkedList<ValveMovement> moves = new LinkedList<ValveMovement>();
    for(int i = 0; i<valves.size(); i++){
      if(valves.get(i).equals(startValve)){continue;}
      if(valves.get(i).flowRate == 0){continue;}
      for(int j = i+1; j<valves.size(); j++){
        if(valves.get(j).flowRate == 0){continue;}
        ValveMovement newMove = new ValveMovement(startValve, startValve, valves.get(i), valves.get(j));
        newMove.setNotVisited(valves);
        moves.addLast(newMove);
      }
    }
    return moves;
  }

  public static int totalFlow(ArrayList<Valve> valves){
    int total = 0;
    for(int i = 0; i<valves.size(); i++){
      if(!valves.get(i).isOpen){
        continue;
      }
      total += valves.get(i).flowRate;
    }
    return total;
  }

  public static Valve findValve(ArrayList<Valve> valves, String s){
    for(int i = 0; i<valves.size(); i++){
      if(valves.get(i).equals(s)){
        return valves.get(i);
      }
    }
    return null;
  }

  public static Valve findValve(ArrayList<Valve> valves, Valve s){
    for(int i = 0; i<valves.size(); i++){
      if(valves.get(i).equals(s)){
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
      Valve newValve = new Valve(inp[1], Integer.parseInt(inp[2]), inp[4].split(", "));
      parsedValves.add(newValve);
    }
    for(int i = 0; i<parsedValves.size(); i++){
      parsedValves.get(i).setPaths(parsedValves);
    }
    return parsedValves;
  }
}