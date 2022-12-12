import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
public class DayEleven {
  /**
   * @param args
   * @throws Exception
   */
  public static class Monkey{
    LinkedList<Long> items;
    // First item is what the operation is, second is number (-1 being self);
    int[] operate;
    // First item is test num, rest are the monkeys
    Long[] test;

    Long inspections = 0l;
    Monkey(LinkedList<Long> items, int[] operate, Long[] test){
      this.items = items;
      this.operate = operate;
      this.test = test;
    }

    // Returns int[], where first element is the monkey it is being thrown to, and the second the worry value.
    Long[] throwToMonkey(){
      Long[] monkeyInfo = {0l, 0l};
      Long item = items.remove();
      inspections++;
      if(operate[0] == (int)'*'){
        if(operate[1] == -1){
          item *= item;
        } else {
          item *= operate[1];
        }
      } else {
        item += operate[1];
      }

      // item /= 3;

      if(item % test[0] == 0){
        monkeyInfo[0] = test[1];
      } else {
        monkeyInfo[0] = test[2];
      }

      monkeyInfo[1] = item;

      return monkeyInfo;
    }
  }
  public static void main(String[] args) throws Exception {
    Monkey monkeys[] = parseMonkeys(8);
    for(int i = 0; i<10000; i++){
      for(int j = 0; j<8; j++){
        throwAll(monkeys, j);
        reduce(monkeys);
      }
    }

    for(int j = 0; j<8; j++){
      System.out.println(monkeys[j].inspections);
    }

    Long[] maxes = {0l, 0l};
    for(int j = 0; j<8; j++){
      if(monkeys[j].inspections > maxes[0]){
        maxes[1] = maxes[0];
        maxes[0] = monkeys[j].inspections;
      } else {
        if(monkeys[j].inspections > maxes[1]){
          maxes[1] = monkeys[j].inspections;
        }
      }
    }

    System.out.println(maxes[0] * maxes[1]);
  }

  public static void reduce(Monkey[] monkeys){
    Long mult = 1l;
    for(int i = 0; i<monkeys.length; i++){
      mult *= monkeys[i].test[0];
    }
    for(int i = 0; i<monkeys.length; i++){
      for(int j = 0; j<monkeys[i].items.size(); j++){
        if(monkeys[i].items.get(j) % mult >= 0){
          monkeys[i].items.set(j, monkeys[i].items.get(j) % mult);
        }
      }
    }
  }

  public static void throwAll(Monkey[] monkeys, int monkey){
    while(monkeys[monkey].items.size() > 0){
      Long[] throwInfo = monkeys[monkey].throwToMonkey();
      monkeys[Math.toIntExact(throwInfo[0])].items.add(throwInfo[1]);
    }
  }

  public static Monkey[] parseMonkeys(int monkeyNum) throws Exception{
    Scanner scan = new Scanner(new File("data/input.txt"));
    Monkey monkeys[] = new Monkey[monkeyNum];
    for(int i = 0; i<monkeys.length; i++){
      scan.nextLine();
      String[] inp = scan.nextLine().split(": ");
      LinkedList<Long> items = new LinkedList<Long>();
      inp = inp[1].split(", ");
      for(int j = 0; j<inp.length; j++){
        items.add(Long.parseLong(inp[j]));
      }

      inp = scan.nextLine().split("= old ");
      inp = inp[1].split(" ");
      int[] operate = new int[2];
      operate[0] = inp[0].charAt(0);
      if(inp[1].equals("old")){
        operate[1] = -1;
      } else {
        operate[1] = Integer.parseInt(inp[1]);
      }

      inp = scan.nextLine().split("by ");
      Long[] test = new Long[3];
      test[0] = Long.parseLong(inp[1]);
      inp = scan.nextLine().split("monkey ");
      test[1] = Long.parseLong(inp[1]);
      inp = scan.nextLine().split("monkey ");
      test[2] = Long.parseLong(inp[1]);

      scan.nextLine();

      monkeys[i] = new Monkey(items, operate, test);
    }
    return monkeys;
  }
}