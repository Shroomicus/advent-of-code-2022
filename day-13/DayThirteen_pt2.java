import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class DayThirteen_pt2 {
  /**
   * @param args
   * @throws Exception
   */
  public static class List{
    ArrayList<List> subLists = new ArrayList<List>();
    String s = "";
    int val = -1;

    List(String s){
      this.s = s;
      if(s.length() == 2 && s.charAt(1) == ']'){
        
      } else if(s.length() > 0 && s.charAt(0) == '['){
        ArrayList<String> sList = parseList(s);
        // System.out.println();
        for(int i = 0; i<sList.size(); i++){
          List newList = new List(sList.get(i));
          subLists.add(newList);
        }
      } else {
        val = Integer.parseInt(s);
      }
    }

    @Override
    public String toString() {
      if(val != -1){
        return Integer.toString(val);
      } else {
        String comb = "[";
        for(int i = 0; i<subLists.size(); i++){
          comb += subLists.get(i).toString();
          if(i!= subLists.size()-1){
            comb+= ",";
          }
        }
        comb += "]";
        return comb;
      }
    }
  }
  static int index = 1;
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/inputpt2.txt"));
    ArrayList<List> listList = new ArrayList<List>();
    while(scan.hasNextLine()){
      String inp = scan.nextLine();
      if(inp.equals("")){
        continue;
      }
      List newList = new List(inp);
      listList.add(newList);
    }
    for(int i = 0; i< listList.size()-1; i++){
      for(int j = 0; j< listList.size()-1; j++){
        if(!listOrder(listList.get(j), listList.get(j+1))){
          List l = listList.remove(j);
          listList.add(j+1, l);
        }
      }
    }
    int[] pos = new int[2];
    // This doesn't actually work. I could just find the answer from the sorted list.
    for(int i = 0; i< listList.size(); i++){
      System.out.println((i+1) + " : " + listList.get(i));
      if(listList.get(i).toString().equals("[[2]]")){
        pos[0] = i+1;
      }
      if(listList.get(i).toString().equals("[[6]]")){
        pos[1] = i+1;
      }
    }
    System.out.println(pos[0] * pos[1]);
    // System.out.println(r.subLists.size());
  }

  public static Boolean listOrder(List lefL, List rigL){
    ArrayList<List> lefList = lefL.subLists;
    ArrayList<List> rigList = rigL.subLists;
    int val = 0;
    for(int i = 0; i<Math.min(lefList.size(), rigList.size()); i++){
      List lef = lefList.get(i);
      List rig = rigList.get(i);
      if(lef.toString().equals(rig.toString())){
        continue;
      }
      if(lef.val > -1 && rig.val > -1){
        // System.out.println(lef.val);
        if(lef.val == rig.val){
          continue;
        }
        val = lef.val < rig.val ? 1 : 0;
        return val == 1;
      } else if(lef.val == -1 && rig.val == -1){
        return listOrder(lef, rig);
      } else {
        List newList = new List("[" + (lef.val == -1 ? rig.val : lef.val) +"]");
        if(lef.val == -1){
          return (listOrder(lef, newList));
        } else {
          return (listOrder(newList, rig));
        }
      }
    }
    val = lefList.size() < rigList.size() ? 1 : 0;
    // if(lefList.size() == rigList.size()){
    //   System.out.println(lefList + " : " + rigList);
    //   System.out.println(index * 3 - 2);
    //   System.out.println(val);
    // }
    return val == 1;
  }

  public static ArrayList<String> parseList(String l){
    ArrayList<String> subLists = new ArrayList<String>();
    int openCount = 0;
    int start = 1;
    for(int i = 1; i<l.length()-1; i++){
      if(l.charAt(i) == '['){
        openCount++;
      }
      if(l.charAt(i) == ']'){
        openCount--;
      }
      if(openCount==0){
        while(l.charAt(i+1) != ',' && l.charAt(i+1) != ']'){
          i++;
        }
        // System.out.print(l.charAt(i+1) + ":\t");
        subLists.add(l.substring(start, i+1));
        // System.out.println(l.substring(start, i+1));
        start = i+2;
        i+=1;
      }
    }
    return subLists;
  }
}