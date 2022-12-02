
import java.io.File;
import java.util.Scanner;
public class DayTwo {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Scanner numberScan = new Scanner(new File("data/input.txt"));
    int opponent;
    int player;
    int totalScore = 0;
    while(numberScan.hasNext()){
      opponent = numberScan.next().charAt(0) - 65;
      player = numberScan.next().charAt(0) - 88;
      totalScore += outcome(opponent, player);
    }
    System.out.println(totalScore);
  }

  // Returns amount you recieve from a round given opponent answer and your answer
  public static int winner(int oppAns, int playAns){
    if(playAns == (oppAns + 1) % 3){
      return 7 + playAns;
    }
    if(playAns == (oppAns + 2) % 3){
      return 1 + playAns;
    }
    if(playAns == oppAns){
      return 4 + playAns;
    }
    return -1;
  }

  // Returns amount you recieve from a round given opponent answer and the result of the match
  public static int outcome(int oppAns, int result){
    switch(result){
      case 0:
        return (oppAns + 2) % 3 + 1;
      case 1:
        return 4 + oppAns;
      case 2:
        return 7 + (oppAns + 1) % 3;
    }
    return -1;
  }
}
