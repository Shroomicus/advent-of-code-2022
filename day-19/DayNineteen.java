import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class DayNineteen {
  /**
   * @param args
   * @throws Exception
   */
  public static class Robot{
    int[] costs = {0, 0, 0};

    /*
     * Ore : 0
     * Clay : 1
     * Obsidian : 2
     * Geode : 3
     */
    int product = -1;

    Robot(int oreCost, int clayCost, int obsCost, int product){
      costs[0] = oreCost;
      costs[1] = clayCost;
      costs[2] = obsCost;
      this.product = product;
    }
  }

  public static class RobotGroup{
    Robot[] robotTypes = new Robot[4];
    int[] maxNeeded = {0, 0, 0, 0};

    RobotGroup(String s){
      Pattern p = Pattern.compile("\\d+");
      Matcher m = p.matcher(s);
      getNext(m);
      robotTypes[0] = new Robot(getNext(m), 0, 0, 0);
      robotTypes[1] = new Robot(getNext(m), 0, 0, 1);
      robotTypes[2] = new Robot(getNext(m), getNext(m), 0, 2);
      robotTypes[3] = new Robot(getNext(m), 0, getNext(m), 3);
      for(int j = 0; j<3; j++){
        for(int i = 0; i<4; i++){
          maxNeeded[j] = Math.max(maxNeeded[j], robotTypes[i].costs[j]);
        }
      }
    }

    Boolean canMake(int rNum, int[] producing){
      for(int i = 0; i<3; i++){
        if(robotTypes[rNum].costs[i] > 0 && producing[0] == 0){
          return false;
        }
      }
      return true;
    }

    @Override
    public String toString() {
      String result = "";
      for(int i = 0; i<robotTypes.length; i++){
        result += "Robot : " + i + "\n";
        result += "Ore Cost : \t" + robotTypes[i].costs[0] + "\n";
        result += "Clay Cost : \t" + robotTypes[i].costs[1] + "\n";
        result += "Obs Cost : \t" + robotTypes[i].costs[2] + "\n";
      }
      return result;
    }
  }

  public static class RobotCount{
    RobotGroup costs;
    int[] robotNums = {1, 0, 0, 0};
    int[] productCount = {0, 0, 0, 0};

    String order = "";

    int factory = -1;
    Boolean makingRobot = false;

    int time = 0;
    int target = -1;

    RobotCount(RobotGroup costs){
      this.costs = costs;
    }

    RobotCount(RobotCount count){
      this.costs = count.costs;
      for(int i = 0; i<4; i++){
        this.robotNums[i] = count.robotNums[i];
        this.productCount[i] = count.productCount[i];
      }
      this.factory = count.factory;
      this.makingRobot = count.makingRobot;
      this.time = count.time;
      this.target = count.target;
      this.order = count.order;
    }

    Boolean isValid(){
      for(int i = 0; i<productCount.length; i++){
        if(productCount[i] < 0){
          return false;
        }
      }
      for(int i = 0; i<3; i++){
        robotNums[factory]++;
        if(robotNums[i] > costs.maxNeeded[i]){
          return false;
        }
        robotNums[factory]--;
      }
      if(productCount[2] >= costs.robotTypes[3].costs[2] && productCount[0] >= costs.robotTypes[3].costs[0] && target != 3){
        return false;
      }
      return true;
    }

    Boolean canMake(int t){
      if(factory == t){
        return true;
      }
      return costs.canMake(t, robotNums);
    }

    void advance(){
      int[] remainingCost = new int[3];
      for(int i = 0; i<3; i++){
        remainingCost[i] = costs.robotTypes[target].costs[i] - productCount[i];
      }
      // System.out.println(productCount[0] + ", " + productCount[1] + ", " + productCount[2]);
      // System.out.println(remainingCost[0] + ", " + remainingCost[1] + ", " + remainingCost[2]);
      // System.out.println(robotNums[0] + ", " + robotNums[1] + ", " + robotNums[2] + ", " + robotNums[3]);
      // System.out.println();

      int max = 0;
      for(int i = 0; i<remainingCost.length; i++){
        if(remainingCost[i] <= 0){
          continue;
        }
        if(robotNums[i] + (i == factory ? 1 : 0) == 0){
          continue;
        }
        int result = 0;
        if(!(makingRobot && i == factory)){
          result = (remainingCost[i] + robotNums[i]-1) / robotNums[i];
        } else {
          remainingCost[i] -= (robotNums[i]);
          if(remainingCost[i] <= 0){
            result = 1;
          } else {
            result = ((remainingCost[i] + (robotNums[i])) / (robotNums[i] + 1)) + 1;
          }
        }
        if(result > max){
          max = result;
        }
      }
      // System.out.println(max);
      produce(max);
      // System.out.println(productCount[0] + ", " + productCount[1] + ", " + productCount[2]);
      for(int i = 0; i<3; i++){
        productCount[i] -= costs.robotTypes[target].costs[i];
      }
      // System.out.println(productCount[0] + ", " + productCount[1] + ", " + productCount[2]);
      // System.out.println();
      factory = target;
      makingRobot = true;
    }

    void produce(int time){
      if(time == 0){
        return;
      }
      if(makingRobot){
        robotNums[factory]++;
      }
      for(int i = 0; i<productCount.length; i++){
        productCount[i] += robotNums[i] * time - ((makingRobot && i == factory) ? 1 : 0);
      }
      makingRobot = false;
      this.time += time;
    }

    int finish(int totalTime){
      if(makingRobot && (factory == 3)){
        return productCount[3] + (totalTime - time) * (robotNums[3] + 1) - 1;
      }
      return productCount[3] + (totalTime - time) * robotNums[3];
    }

    RobotCount splitOff(int newTarget){
      RobotCount newCount = new RobotCount(this);
      newCount.order += newTarget + "->";
      newCount.target = newTarget;
      return newCount;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Time: " + time + "\n";
        result += "Ore:\t" + "Robots: " + robotNums[0] + "\tResources:" + productCount[0] + "\n";
        result += "Clay:\t" + "Robots: " + robotNums[1] + "\tResources:" + productCount[1] + "\n";
        result += "Obs:\t" + "Robots: " + robotNums[2] + "\tResources:" + productCount[2] + "\n";
        result += "Geode:\t" + "Robots: " + robotNums[3] + "\tResources:" + productCount[3] + "\n";
        result += "Making: " + makingRobot + " : " + factory + "\n";
        result += "Order:\t" + order;
        return result;
    }
  }
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    int realTotal = 1;
    int count = 1;
    while(scan.hasNextLine()){
      RobotGroup startGroup = new RobotGroup(scan.nextLine());
      RobotCount startCount = new RobotCount(startGroup);
      LinkedList<RobotCount> queue = new LinkedList<RobotCount>();
      queue.add(startCount);
      int max = 0;
      int[] maxTime = new int[33];
      for(int i = 0; i<maxTime.length; i++){
        maxTime[i] = 0;
      }
      while(queue.size() > 0){
        // System.out.println(queue.size());
        RobotCount checkCount = queue.removeFirst();
        for(int i = 0; i<4; i++){
          if(checkCount.canMake(i)){
            RobotCount newCount = checkCount.splitOff(i);
            newCount.advance();
            if(!newCount.isValid()){
              continue;
            }
            if(newCount.time > 32){
              continue;
            }

            if(newCount.productCount[3] >= maxTime[newCount.time]){
              maxTime[newCount.time] = newCount.productCount[3];
            } else {
              continue;
            }
            queue.add(newCount);
          }
        }
      }
      for(int i = 0; i<maxTime.length; i++){
        max = Math.max(max, maxTime[i]);
      }
      realTotal *= max;
      System.out.println(max);
      count++;
      // System.out.println(maxCount);
    }
    System.out.println(realTotal);
  }

  public static int getNext(Matcher m){
    m.find();
    return Integer.parseInt(m.group());
  }
}