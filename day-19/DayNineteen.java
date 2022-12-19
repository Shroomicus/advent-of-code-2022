import java.io.File;
import java.util.ArrayList;
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
    RobotGroup(String s){
      Pattern p = Pattern.compile("\\d+");
      Matcher m = p.matcher(s);
      getNext(m);
      robotTypes[0] = new Robot(getNext(m), 0, 0, 0);
      robotTypes[1] = new Robot(getNext(m), 0, 0, 1);
      robotTypes[2] = new Robot(getNext(m), getNext(m), 0, 2);
      robotTypes[3] = new Robot(getNext(m), 0, getNext(m), 3);
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
    }
    Boolean isValid(){
      for(int i = 0; i<productCount.length; i++){
        if(productCount[i] < 0){
          return false;
        }
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
      // System.out.println(remainingCost[0] + ", " + remainingCost[1] + ", " + remainingCost[2]);

      int max = 0;
      for(int i = 0; i<remainingCost.length; i++){
        if(robotNums[i] == 0){
          continue;
        }
        int result;
        if(makingRobot && i == factory){
          remainingCost[i] -= robotNums[i] - 1;
          if(remainingCost[i] <= 0){
            result = 1;
          } else {
            result = (remainingCost[i] + robotNums[i]-1) / robotNums[i] - 1;
          }
        } else {
          result = (remainingCost[i] + robotNums[i]-1) / robotNums[i];
        }
        if(result > max){
          max = result;
        }
      }
      // System.out.println(max);
      produce(max);
      for(int i = 0; i<3; i++){
        productCount[i] -= costs.robotTypes[target].costs[i];
      }
      factory = target;
      makingRobot = true;
    }

    void produce(int time){
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
      return productCount[3] + (totalTime - time) * robotNums[3] - ((makingRobot && factory == 3 ? 1 : 0));
    }

    RobotCount splitOff(int newTarget){
      RobotCount newCount = new RobotCount(this);
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
        return result;
    }
  }
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input3.txt"));
    RobotGroup startGroup = new RobotGroup(scan.nextLine());
    RobotCount startCount = new RobotCount(startGroup);
    LinkedList<RobotCount> queue = new LinkedList<RobotCount>();

    queue.add(startCount);
    int max = 0;
    RobotCount maxCount = new RobotCount(startCount);
    while(queue.size() > 0){
      // System.out.println(queue.size());
      RobotCount checkCount = queue.removeFirst();
      // System.out.println(queue.size() + " : " + checkCount.time);
      for(int i = 0; i<4; i++){
        if(checkCount.canMake(i)){
          RobotCount newCount = checkCount.splitOff(i);
          newCount.advance();
          if(!newCount.isValid()){
            continue;
          }
          if(newCount.time > 24){
            continue;
          }
          int finished = newCount.finish(24);
          if(finished >= max){
            max = finished;
            maxCount = newCount;
          }
          if(finished <= max/2){
            continue;
          }
          if(newCount.time >= 8){
            // if(newCount.robotNums[1] == 0){
            //   continue;
            // }
            if(newCount.time >= 16){
              // if(newCount.robotNums[2] == 0){
              //   continue;
              // }
            }
            if(newCount.time >= 18){
              // if(newCount.robotNums[3] == 0){
              //   continue;
              // }
              // int finished = newCount.finish(24);
              // if(finished >= max){
              //   max = finished;
              //   maxCount = newCount;
              // }
              // if(finished <= max/2){
              //   continue;
              // }
            }
          }
          queue.add(newCount);
        }
      }
    }
    System.out.println(max);
    System.out.println(maxCount);
  }

  public static int getNext(Matcher m){
    m.find();
    return Integer.parseInt(m.group());
  }
}