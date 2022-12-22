import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
public class DayTwentyone {
  /**
   * @param args
   * @throws Exception
   */
  public static class Monkey{
    String name;
    String[] monkeyNames = new String[2];
    Monkey[] monkeyPaths = new Monkey[2];
    char operand;
    BigInteger monkeyNum = new BigInteger("0");
    Boolean numReady = false;

    Monkey(String monkeyInfo){
      String[] data = monkeyInfo.split(" |:");
      name = data[0];
      if(data.length == 3){
        numReady = true;
        monkeyNum = new BigInteger(data[2]);
      } else {
        monkeyNames[0] = data[2];
        operand = data[3].charAt(0);
        monkeyNames[1] = data[4];
      }
      // System.out.println(data.length);
    }

    void setPaths(ArrayList<Monkey> monkeyList){
      monkeyPaths[0] = findMonkey(monkeyList, monkeyNames[0]);
      monkeyPaths[1] = findMonkey(monkeyList, monkeyNames[1]);
    }

    BigInteger solveMonkey() throws Exception{
      if(numReady){
        return monkeyNum;
      }
      monkeyPaths[0].solveMonkey();
      monkeyPaths[1].solveMonkey();
      switch(operand){
        case '+':
          monkeyNum = monkeyPaths[0].monkeyNum.add(monkeyPaths[1].monkeyNum);
          break;
        case '-':
          monkeyNum = monkeyPaths[0].monkeyNum.subtract(monkeyPaths[1].monkeyNum);
          break;
        case '*':
          monkeyNum = monkeyPaths[0].monkeyNum.multiply(monkeyPaths[1].monkeyNum);
          break;
        case '/':
          monkeyNum = monkeyPaths[0].monkeyNum.divide(monkeyPaths[1].monkeyNum);
          break;
      }
      return monkeyNum;
    }

    @Override
    public String toString() {
      String result = "";
      if(numReady){
        result = name + ": " + monkeyNum;
      } else {
        result = name + ": " + monkeyNames[0] + " " + operand + " " + monkeyNames[1];
      }
      return result;  
    }
  }

  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();
    while(scan.hasNextLine()){
      Monkey newMonkey = new Monkey(scan.nextLine());
      monkeyList.add(newMonkey);
    }
    for(int i = 0; i<monkeyList.size(); i++){
      monkeyList.get(i).setPaths(monkeyList);
      // System.out.println(monkeyList.get(i));
    }
    System.out.println(findMonkey(monkeyList, "root").solveMonkey());
  }

  public static Monkey findMonkey(ArrayList<Monkey> monkeyList, String monkeyName){
    for(int i = 0; i<monkeyList.size(); i++){
      if(monkeyList.get(i).name.equals(monkeyName)){
        return monkeyList.get(i);
      }
    }
    return null;
  }
}