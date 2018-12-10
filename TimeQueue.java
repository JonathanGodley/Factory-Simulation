// TimeQueue.java
// Priority Queue for our discrete event timings
// Implements java generic interface for Priorityqueues
// Uses generics
// 
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
import java.util.PriorityQueue;
public class TimeQueue<Double>
{
  //instance variables
  private PriorityQueue<Double> queue;

  // Constructor
  // Precondition : an int is passed as a parameter
  // Postcondition: our priority queue is intialised with a maximum size limit
  public TimeQueue(int qMax){
    queue = new PriorityQueue<Double>(qMax);
  }

  // add item to queue
  // precondition: double provided as parameter
  // postcondition: if under max size, returns true on success
  public boolean enqueue(Double dbl)
  {
      //offer the input to the queue, will return false if full.
      return queue.offer(dbl);
  }

  // GET
  // Precondition : queue is populated
  // Postcondition: head of the queue is removed and returned
  public Double dequeue()
  {
    return queue.poll();
  }

  // Precondition : queue is populated
  // Postcondition: head of the queue is returned without removing
  public Double check()
  {
    return queue.peek();
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
}
