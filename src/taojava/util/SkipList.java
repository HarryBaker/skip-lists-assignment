package taojava.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * A randomized implementation of sorted lists.  
 * 
 * Consulted code from Patrick Slough's repository
 * 
 * @author Samuel A. Rebelsky
 * @author Harry Baker and Larry Boateng
 */
public class SkipList<T extends Comparable<T>>
    implements SortedList<T>
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+
  
  //Maximum possible level of any node in the list
  int maxLevel = 20;
  
  //Height of the biggest node in the list
  int maxHeight = 0;
  
  //Size of the entire list. 
  int size;
  
  //Random integer
  Random rand;
  
  //Marks beginning of list
  Node header;
  
  //Marks end of list
  Node dummy;
  
  //Determines random distribution of height of nodes
  double p = 0.5;

  // +------------------+------------------------------------------------
  // | Internal Classes |
  // +------------------+

  /**
   * Nodes for skip lists.
   */
  public class Node
  {
    // +--------+--------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * The value stored in the node.
     */
    
    //Value stored in node
    T val;
    
    //Height of the node
    int level;
    
    Node[] nextArray; // storing all pointers to the different levels 

    @SuppressWarnings("unchecked")
    public Node(T val, int level)
    {
      this.val = val;
      this.level = level;
      this.nextArray = new SkipList.Node[level];
    }// Node(T, int)
  } // class Node

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+

  public SkipList()
  {
    this.header = new Node(null, maxLevel);
    this.dummy = new Node(null, maxLevel);
    this.size = 0;

    for (int i = 0; i < this.maxLevel; i++)
      {
        this.header.nextArray[i] = this.dummy;
        this.dummy.nextArray[i] = null;
      }// for
  }// SkipList()

  // +-------------------------+-----------------------------------------
  // | Internal Helper Methods |
  // +-------------------------+

  //Creates a randomized height for a node based on p
  public int randomHeight(double p)
  {
    this.rand = new Random();
    int level = 1;
    while (this.rand.nextDouble() < p)
      {
        level++;
      }//while
    return Math.min(level, this.maxLevel);
  }//randomHeight(double)

  // +-----------------------+-------------------------------------------
  // | Methods from Iterable |
  // +-----------------------+

  /**
   * Return a read-only iterator (one that does not implement the remove
   * method) that iterates the values of the list from smallest to
   * largest.
   */
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
      {
        Node cursor = SkipList.this.header;

        @Override
        public boolean hasNext()
        {
          return cursor.nextArray[0].val != null;
        }//hasNext()

        @SuppressWarnings("unchecked")
        @Override
        public T next()
        {
          if (!hasNext())
            {
              throw new NoSuchElementException(
                                               "There is no element after this node");
            }//if
          else
            {
              cursor = cursor.nextArray[0];
            }//else
          return cursor.val;
        }//next()
      };//Iterator<T>()

  } // iterator()

  // +------------------------+------------------------------------------
  // | Methods from SimpleSet |
  // +------------------------+

  /**
   * Add a value to the set.
   *
   * @post contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to add, contains(lav) continues to hold.
   */
  @SuppressWarnings("unchecked")
  public void add(T val)
  {
    //Sanity check to determine that the list does not already
    //have val stored, and that val is not null
    if (this.contains(val))
      return;
    if (val != null)
      {
        Node temp = this.header;
        int height = randomHeight(this.p);

        //set height of tallest non-dummy node
        if (height > this.maxHeight)
          {
            this.maxHeight = height;
          }//if

        //new node
        Node insert = new Node(val, height);

        //array that keeps track of all levels of pointers
        Node[] tracker = new SkipList.Node[this.maxLevel];

        //Iterates through list until it reaches appropriate level
        for (int i = this.maxHeight; i >= 0; i--)
          {
            while (temp.nextArray[i].val != null
                   && temp.nextArray[i].val.compareTo(val) < 0)
              {
                temp = temp.nextArray[i];
              }//while
            tracker[i] = temp;
          }//for

        //Adds new node to the list and resets pointers 
        for (int i = 0; i < height; i++)
          {
            insert.nextArray[i] = tracker[i].nextArray[i];
            tracker[i].nextArray[i] = insert;
          }//for
        this.size++;
      }//if
  } // add(T val)

  /**
   * Determine if the set contains a particular value.
   */
  public boolean contains(T val)
  {
    Node cursor = this.header;

    if (val != null)
      {
        //Iterates through list to appropriate spot
        for (int i = this.maxHeight; i >= 0; i--)
          {
            while (cursor.nextArray[i].val != null
                   && cursor.nextArray[i].val.compareTo(val) < 0)
              {
                cursor = cursor.nextArray[i];
              }//while
          }//for
        cursor = cursor.nextArray[0];

        //Checks if cursor is equal to val
        if (cursor.val == null)
          {
            return false;
          }//if
        else if (cursor.val.compareTo(val) == 0)
          {
            return true;
          } // if
        else
          {
            return false;
          }// else
      }
    //if val is null, return true
    else
      {
        return true;
      }//else
  } // contains(T)

  /**
   * Remove an element from the set.
   *
   * @post !contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to remove, contains(lav) continues to hold.
   */
  @SuppressWarnings("unchecked")
  public void remove(T val)
  {
    Node cursor = this.header;
    Node[] tracker = new SkipList.Node[this.maxLevel];

    //Iterates through list until it reaches the first node lower than val

    if (val != null)
      {

        for (int i = this.maxHeight; i >= 0; i--)
          {
            while (cursor.nextArray[i].val != null
                   && cursor.nextArray[i].val.compareTo(val) < 0)
              {
                cursor = cursor.nextArray[i];
              }//while
            tracker[i] = cursor;
          }//for
        cursor = cursor.nextArray[0];

        //if the next node is equal to val, removes it
        if (cursor.val == null)
          {
            System.err.println("List is Empty");
          }//if
        else if (cursor.val.compareTo(val) == 0)
          {
            int height = cursor.level;
            this.size--;
            for (int i = 0; i < height; i++)
              {
                tracker[i].nextArray[i] = cursor.nextArray[i];
              }//for
          } // if
      }//if
  } // remove(T)

  
  //return size of list
  public int size()
  {
    return this.size;
  }//size()

  // +--------------------------+----------------------------------------
  // | Methods from SemiIndexed |
  // +--------------------------+

  /**
   * Get the element at index i.
   *
   * @throws IndexOutOfBoundsException
   *   if the index is out of range (index < 0 || index >= length)
   */
  public T get(int i)
  {
    // STUB
    return null;
  } // get(int)

  /**
   * Determine the number of elements in the collection.
   */
  public int length()
  {
    // STUB
    return 0;
  } // length()

} // class SkipList<T>
