// IS_Storage.java
// Queue class for holding items between stages
// Implements java generic interface for queues
// Is Generic
//
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
import java.util.LinkedList;
import java.util.Queue;
public class IS_Storage
{
  // Instance Variables
  private Queue<FactoryItem> queue = new LinkedList<FactoryItem>();
  private int qMax;

  // Constructor
  // Precondition : an int is passed as a parameter
  // Postcondition:  A new IS_Storage is set with a item limit qMax
  public IS_Storage(int qm)
  {
    qMax = qm;
  }

  // GET
  // Precondition : queue is populated
  // Postcondition: head of the queue is removed and returned
  public FactoryItem dequeue()
  {
    return queue.poll();
  }

  // Precondition : N/A
  // Postcondition: returns true if queue not empty, false otherwise.
  public boolean hasItems()
  {
    return !queue.isEmpty();
  }

  // Precondition : N/A
  // Postcondition: returns int containing number of items in queue
  public int size()
  {
    return queue.size();
  }

  // Precondition : queue initialised with size limit set.
  // Postcondition: returns true if queue full, false otherwise.
  public boolean isFull()
  {
    if (queue.size()<qMax)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  // Add an item to the queue
  // Precondition : a FactoryItem is passed to the queue, the queue is not
  //                at or over it's max size.
  // Postcondition: added item to queue, only if queue max not reached,
  //                returned boolean, to tell of success or failure
  public boolean enqueue(FactoryItem item)
  {
    if (size() < qMax)
    {
      queue.add(item);
      return true;
    }
    else
    {
      return false;
    }
  }

}
