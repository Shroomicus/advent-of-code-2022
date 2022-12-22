import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class DayTwentyonept2 {
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

    Boolean unknown = false;
    int unknownSide = -1;
    Boolean found = false;

    Monkey(String monkeyInfo){
      String[] data = monkeyInfo.split(" |:");
      name = data[0];
      if(data.length == 3){
        numReady = true;
        if(data[2].equals("?")){
          unknown = true;
          found = true;
          return;
        }
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
  
    Boolean hasUnknown(){
      if(unknown){
        return true;
      }
      if(monkeyPaths[0] != null && monkeyPaths[0].hasUnknown()){
        unknown = true;
        unknownSide = 0;
        return true;
      }
      if(monkeyPaths[1] != null && monkeyPaths[1].hasUnknown()){
        unknown = true;
        unknownSide = 1;
        return true;
      }
      return false;
    }
  
    BigInteger findAnswer(BigInteger expectNum) throws Exception{
      int unknown = -1;
      BigInteger known = this.monkeyNum;
      if(monkeyPaths[0] != null && !monkeyPaths[0].unknown){
        monkeyPaths[0].solveMonkey();
        known = monkeyPaths[0].monkeyNum;
        unknown = 1;
      }
      if(monkeyPaths[1] != null && !monkeyPaths[1].unknown){
        monkeyPaths[1].solveMonkey();
        known = monkeyPaths[1].monkeyNum;
        unknown = 0;
      }
      if(operand == '='){
        expectNum = known;
      }
      BigInteger expected = findExpected(unknown, known, expectNum, operand);
      System.out.println(expected);
      if(monkeyPaths[unknown].found){
        return expected;
      }
      BigInteger testExpected = monkeyPaths[unknown].findAnswer(expected);
      // System.out.println(testExpected);

      return testExpected;
    }
  }

  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/inputpt2.txt"));
    ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();
    while(scan.hasNextLine()){
      Monkey newMonkey = new Monkey(scan.nextLine());
      monkeyList.add(newMonkey);
    }
    for(int i = 0; i<monkeyList.size(); i++){
      monkeyList.get(i).setPaths(monkeyList);
      // System.out.println(monkeyList.get(i));
    }
   System.out.println(findMonkey(monkeyList, "root").hasUnknown());
   System.out.println(findMonkey(monkeyList, "root").monkeyPaths[0].hasUnknown());
   System.out.println(findMonkey(monkeyList, "root").monkeyPaths[1].hasUnknown());

   System.out.println(findMonkey(monkeyList, "root").findAnswer(new BigInteger("0")));
  }

  public static Monkey findMonkey(ArrayList<Monkey> monkeyList, String monkeyName){
    for(int i = 0; i<monkeyList.size(); i++){
      if(monkeyList.get(i).name.equals(monkeyName)){
        return monkeyList.get(i);
      }
    }
    return null;
  }

  public static BigInteger findExpected(int unknownSide, BigInteger known, BigInteger expectedResult, char operand){
    if(operand == '='){
      return known;
    }
    if(unknownSide == 0){
      System.out.println("x" + " " + operand + " " + known  + " = " + expectedResult);
      switch(operand){
        case '+':
          return expectedResult.subtract(known);
        case '-':
          return expectedResult.add(known);
        case '*':
          return expectedResult.divide(known);
        case '/':
          return expectedResult.multiply(known);
      }
    }
    if(unknownSide == 1){
      System.out.println(known + " " + operand + " " + "x"  + " = " + expectedResult);
      switch(operand){
        case '+':
          return expectedResult.subtract(known);
        case '-':
        // a - b = c
        // b = a - c
          return known.subtract(expectedResult);
        case '*':
        // a * b = c
        // b = c / a
          return expectedResult.divide(known);
        case '/':
        // a / b = c
        // b = a / c
          return known.divide(expectedResult);
      }
    }
    return null;
  }
}