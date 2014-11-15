package taojava.util;

public class SkipListExptSuite
{

  public static void main(String[] args)
  {
    SkipList <Integer> list = new SkipList<Integer>();
    list.add(6);
    list.add(6);
    list.remove(6);
    System.out.println("After removing 6, list.contains(6) is " + list.contains(6));
    list.add(7);
    list.add(2);
    list.add(10);
    list.add(16);
    list.remove(10);
    list.remove(11);
    list.remove(17);
  list.add(null);

    System.out.println(list);
  }

}
