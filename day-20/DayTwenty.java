import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
public class DayTwenty {
  /**
   * @param args
   * @throws Exception
   */

  public static class Number{
    int originalNum;
    int move;
    int total;
    Number next, prev;
    Number(int move, int total){
      originalNum = move;
      move %= (total - 1);
      if(move < 0){
        this.move = total + move - 1;
        if(this.move < 0){
          System.out.println("AAAAA");
        }
      } else {
        this.move = move;
      }
      this.total = total;
    }

    void advance(){
      int n = move;
      Number store = this.prev;
      store.next = this.next;
      this.next.prev = store;
      for(int i = 0; i<n; i++){
        store = store.next;
      }
      // System.out.println(store);
      store.next.prev = this;
      this.next = store.next;
      store.next = this;
      this.prev = store;
    }
    
    @Override
    public String toString() {
      String result = "";

      result += "Original Movement Value : " + originalNum;

      return result;
    }
  }

  public static Number start;
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    ArrayList<String> numStrings = new ArrayList<String>();
    Number[] numArray;
    Number[] posArray;
    int count = 0;
    while(scan.hasNextLine()){
      numStrings.add(scan.nextLine());
      count++;
    }
    numArray = new Number[count];
    for(int i = 0; i<count; i++){
      numArray[i] = new Number(Integer.parseInt(numStrings.get(i)), count);
    }
    for(int i = 1; i<count; i++){
      numArray[i].next = numArray[(i+1)%count];
      numArray[i].prev = numArray[(i-1)%count];
    }
    numArray[0].next = numArray[1];
    numArray[0].prev = numArray[count-1];

    // printNums(numArray[0], count);

    for(int i = 0; i<count; i++){
      // System.out.println(i);
      numArray[i].advance();
    }

    int total = 0;
    System.out.println(numAfter(numArray[0], 1000));
    total += numAfter(numArray[0], 1000).originalNum;
    System.out.println(numAfter(numArray[0], 2000));
    total += numAfter(numArray[0], 2000).originalNum;
    System.out.println(numAfter(numArray[0], 3000));
    total += numAfter(numArray[0], 3000).originalNum;
    System.out.print(total);
  }

  public static Number numAfter(Number s, int n){
    Number curr = s;
    while(curr.originalNum != 0){
      curr = curr.next;
    }
    for(int i = 0; i<n; i++){
      curr = curr.next;
    }
    return curr;
  }

  public static void printNums(Number s, int n){
    Number curr = s;
    for(int i = 0; i<n; i++){
      System.out.println(curr.originalNum + " : " + curr.move);
      curr = curr.next;
    }
  }
}