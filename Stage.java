// Stage.java
// Class that mimics a production station,
//  simulating the time it takes to process an item,
//  and recording statistical information on it's activities
//
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
import java.util.Random;
public class Stage
{
  // Instance Variables
  private IS_Storage nextQueue;
  private IS_Storage prevQueue;
  protected double totalProductionTime;
  private double totalStarvingTime;
  private double totalBlockingTime;
  protected FactoryItem currentItem;
  protected double finishTime = 0.0;
  protected boolean starving;
  protected boolean blocking;
  protected double starvedAtTime;
  protected double blockedAtTime;
  protected double producingAtTime;
  protected double mean;
  protected double range;
  protected SingletonTime simTimer = SingletonTime.getInstance();
  protected Random r = new Random();


  // Constructor
  // Precondition : a mean and range value is provided
  // Postcondition:  A new Stage is created, awaiting input
  public Stage(double m, double n)
  {
      setStarving();
      blocking = false;
      mean = m;
      range = n;
  }

  // Set
  // Precondition : N/A
  // Postcondition: the stage is linked to a queue object
  public void setNextQueue(IS_Storage nxt)
  {
    nextQueue = nxt;
  }

  // Precondition : N/A
  // Postcondition: the stage is linked to a queue object
  public void setPrevQueue(IS_Storage prev)
  {
    prevQueue = prev;
  }

  // Preconditons: N/A
  // Postconditions: starving is set to true, the time starving started is recorded
  public void setStarving()
  {
    // need to set starving to true, need to save starving time
    starvedAtTime = simTimer.getTime();
    starving = true;
  }

  // Preconditons: N/A
  // Postconditions: blocked is set to true, the time blocking started is recorded
  public void setBlocked()
  {
    // need to set starving to true, need to save starving time
    blockedAtTime = simTimer.getTime();
    blocking = true;
  }

  // Preconditons: N/A
  // Postconditions: starving set to false, total starving time incremented,
  // and starving start time reset
  public void setNotStarving()
  {
    starving = false;
    totalStarvingTime += (simTimer.getTime()-starvedAtTime);
    starvedAtTime = 0;
  }

  // Preconditons: N/A
  // Postconditions: blocking set to false, total blocked time incremented,
  // and blocking start time reset
  public void setNotBlocked()
  {
    blocking = false;
    totalBlockingTime += (simTimer.getTime()-blockedAtTime);
    blockedAtTime = 0;
  }

  //GET
  // Precondition : N/A
  // Postcondition: returns finishTime variable
  public double getFinishTime()
  {
    return finishTime;
  }

  // Precondition : N/A
  // Postcondition: returns whether or not the stage is starving
  public boolean checkStarve()
  {
    return starving;
  }

  // Precondition : prevQueue has been set to a valid queue object
  // Postcondition: return true if prevQueue not empty, false otherwise.
  public boolean doesSourceHaveItems()
  {
    if (prevQueue.hasItems()==true)
    {
      return true;
    }
    else return false;
  }

  // Precondition : nextQueue has been set to a valid queue object
  // Postcondition: return true if nextQueue not full, false otherwise.
  public boolean doesDestinationHaveSpace()
  {
    if (nextQueue.isFull()==false)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  //Precondition: N/A
  //Postcondition: blocking variable returned
  public boolean checkBlock()
  {
    return blocking;
  }

  //Precondition: N/A
  //Postcondition: blocked at time variable returned
  public double getBlockTime()
  {
    return blockedAtTime;
  }

  // Pass an Item to the next queue
  // Precondition : nextQueue has been set and has Space,
  //                currentItem is a valid item,
  // Postcondition: the currentItem is placed into the next queue in the
  // production line, and cleared from this stage.
  public void passItem()
  {
    nextQueue.enqueue(currentItem);
    // set time the item left this stage
    currentItem.setTLeaveStage(simTimer.getTime());
    currentItem = null;
    totalProductionTime += (simTimer.getTime()-producingAtTime);
    producingAtTime = 0;
  }

  // Take an Item from the previous queue
  // Precondition : prevQueue has been set and has Space,
  // Postcondition: the item at the head of prevQueue is moved into
  // this stage, and is set as it's current item.
  // The stages completion time is set
  public void takeItem()
  {
      currentItem = prevQueue.dequeue();
      // set the time this item entered this stage.
      producingAtTime = simTimer.getTime();
      currentItem.setTEntStage(simTimer.getTime());
      // calculate our finish time for this stage
      double tempTime = 0.0;
      double d = r.nextDouble();
      tempTime = mean + (range * (d - 0.5));
      tempTime += simTimer.getTime();
      finishTime = tempTime;
  }

  // OVERLOADING
  // Precondition : Stage Initialised.
  // Postcondition: String is returned containing statistical information
  // on the stage's production history since creation.
  public String toString()
  {
    String stageSummary;
    double totalTime = totalProductionTime + totalStarvingTime + totalBlockingTime;

    stageSummary = "     " + String.format("%4.2f", (totalProductionTime/totalTime))
    + "%      " + String.format("%4.2f", (totalStarvingTime/totalTime)) + "%    "
    + String.format("%4.2f", (totalBlockingTime/totalTime))+"%";
    return stageSummary;
  }
}
