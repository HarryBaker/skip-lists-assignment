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
 * @author Your Name Here
 */
public class SkipList<T extends Comparable<T>>
    implements SortedList<T>
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+
  int maxLevel = 20;
  int size;
  Random rand;
  Node header;
  Node dummy;
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
    T val;
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

    for (int i = 0; i <= this.maxLevel; i++)
      {
        this.header.nextArray[i] = this.dummy;
        this.dummy.nextArray[i] = null;
      }// for

  }// SkipList()

  // +-------------------------+-----------------------------------------
  // | Internal Helper Methods |
  // +-------------------------+

  public int randomHeight(double p)
  {
    this.rand = new Random();
    int level = 0;
    while (this.rand.nextDouble() < p)
      {
        level++;
      }
    return Math.min(level, this.maxLevel);
  }

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
          return cursor.nextArray[0] != null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next()
        {
          if (!hasNext())
            {
              throw new NoSuchElementException(
                                               "There is no element after this node");
            }
          else
            {
              cursor = cursor.nextArray[0];
            }
          return cursor.val;
        }
      };

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
  public void add(T val)
  {
    int counter = this.maxLevel;
    Node temp = this.header;
    int height = randomHeight(this.p);
    Node insert = new Node(val, height);
    Node[] tracker = new SkipList.Node[this.maxLevel];
    
   /* while (this.header.nextArray[counter].val.compareTo(val) >= 0
        || this.header.nextArray[counter].val == null){
     counter--;
     if (this.header.nextArray[counter].val != null)
       {
         
       }
    }
    */
    while (temp.nextArray[counter].val.compareTo(val) >= 0
        || temp.nextArray[counter] == null)
      {
       tracker[counter] = temp.nextArray[counter];
       
        if (temp.nextArray[counter] != null)
          {
            temp = temp.nextArray[counter];
          }
        counter--;
      }
    
    int countToZero = counter;
    for (int i = countToZero; i >= 0; i--)
      {
        tracker[i] = temp.nextArray[i];
      }
    
    for (int i = 0; i < height; i ++)
      {
        temp.nextArray[i] = insert;
      }
    
  } // add(T val)

  /**
   * Determine if the set contains a particular value.
   */
  public boolean contains(T val)
  {
    // STUB
    return false;
  } // contains(T)

  /**
   * Remove an element from the set.
   *
   * @post !contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to remove, contains(lav) continues to hold.
   */
  public void remove(T val)
  {
    // STUB
  } // remove(T)

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
