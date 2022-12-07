import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class DaySeven {
  /**
   * @param args
   * @throws Exception
   */
  public static class Directory{
    String name;
    int size = 0;
    Directory parent = null;
    ArrayList<Directory> subDirectories;
    ArrayList<FileSize> files;
    Directory(String s){
      name = s;
      subDirectories = new ArrayList<Directory>();
      files = new ArrayList<FileSize>();
    }
    Directory(String s, Directory p){
      name = s;
      parent = p;
      subDirectories = new ArrayList<Directory>();
      files = new ArrayList<FileSize>();
    }

    void addDirectory(Directory dir){
      subDirectories.add(dir);
    }

    void addFile(FileSize file){
      files.add(file);
    }

    Directory getChildDir(String s){
      if(s.equals("..")){
        return parent;
      }
      for(int i = 0; i<subDirectories.size(); i++){
        if(subDirectories.get(i).name.equals(s)){
          return subDirectories.get(i);
        }
      }
      return null;
    }

    void printInsides(){
      for(int i = 0; i<subDirectories.size(); i++){
        subDirectories.get(i).calculateSize();
        System.out.println("DIR: \t" + subDirectories.get(i).name + "\t : \t" + subDirectories.get(i).size);
      }
      for(int i = 0; i<files.size(); i++){
        System.out.println("FILE: \t" + files.get(i).name + "\t : \t" + files.get(i).size);
      }
    }

    void calculateSize(){
      size = 0;
      for(int i = 0; i<subDirectories.size(); i++){
        subDirectories.get(i).calculateSize();
        size += subDirectories.get(i).size;
      }
      for(int i = 0; i<files.size(); i++){
        size += files.get(i).size;
      }
    }

    int getBelow(int maxSize){
      int total = 0;
      for(int i = 0; i<subDirectories.size(); i++){
        if(subDirectories.get(i).size < maxSize){
          total += subDirectories.get(i).size;
        }
        total += subDirectories.get(i).getBelow(maxSize);
      }
      return total;
    }

    int findDelete(int maxSize){
      int closest = size;
      for(int i = 0; i<subDirectories.size(); i++){
        if(subDirectories.get(i).size - maxSize > 0 && subDirectories.get(i).size < closest){
          closest = subDirectories.get(i).size;
          // System.out.println("??");
        }
        if(subDirectories.get(i).findDelete(maxSize) - maxSize > 0 && subDirectories.get(i).findDelete(maxSize) < closest){
          closest = subDirectories.get(i).findDelete(maxSize);
        }
      }
      return closest;
    }
  }

  public static class FileSize{
    String name;
    int size;
    FileSize(String n, int s){
      name = n;
      size = s;
    }
  }
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(new File("data/input.txt"));
    Directory root = new Directory(scan.nextLine().split(" ")[2]);
    // System.out.println(root.name);
    Directory activeDir = root;
    while(scan.hasNextLine()){
      String[] inp = scan.nextLine().split(" ");
      if(inp[0].equals("$")){
        if(inp[1].equals("ls")){
          continue;
        }
        activeDir = activeDir.getChildDir(inp[2]);
        continue;
      }
      if(inp[0].equals("dir")){
        Directory newDir = new Directory(inp[1], activeDir);
        activeDir.addDirectory(newDir);
      } else {
        FileSize newFile = new FileSize(inp[1], Integer.valueOf(inp[0]));
        activeDir.addFile(newFile);
      }
    }
    root.printInsides();
    root.calculateSize();
    System.out.println(root.size);
    System.out.println(30000000 - (70000000 - root.size));
    System.out.println(root.findDelete(30000000 - (70000000 - root.size)));

    // System.out.println(root.getBelow(100000));
  }
}
