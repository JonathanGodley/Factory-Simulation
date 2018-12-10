// Stage6.java
// Class that mimics a production station,
//  simulating the time it takes to process an item,
//  and recording statistical information on it's activities
// This stage additionally produces statistics from each FactoryItem is receives.
//
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
public class Stage6 extends Stage
{
  // Instance Variables
  private double[] averageTimeInQueue = new double[]{0,0,0,0,0,0};
  private int itemsfrom0a = 0;
  private int itemsfrom0b = 0;

  // constructor
  // preconditon: mean and range values provided
  // postcondition: a new stage6 has been initialised.
  public Stage6(double m, double n)
  {
    super(m, n);
    setStarving();
    blocking = false;
  }

  //GET
  //Preconditon: N/A
  //Postcondition: as our output is infinate, as we're simply ripping the information
  // out of items as needed, always return true.
  public boolean doesDestinationHaveSpace()
  {
    return true;
  }

  //preconditons: N/A
  //postconditons: total number of items created is returned
  public int getItemsCreated()
  {
    return (itemsfrom0a + itemsfrom0b);
  }

  //preconditons: N/A
  //postconditons: total number of items produced in s0a is returned
  public int totalA()
  {
    return itemsfrom0a;
  }

  //preconditons: N/A
  //postconditons: total number of items produced in s0b is returned
  public int totalB()
  {
    return itemsfrom0b;
  }

  //Catch passItem() calls, instead rip statistics out of the item,
  //Preconditon: currentItem is a valid FactoryItem
  //Postcondition: statistic information is ripped from the currentItem and
  // the currentItem is set to null
  public void passItem()
  {
    currentItem.setTLeaveStage(simTimer.getTime());
    totalProductionTime += (simTimer.getTime()-producingAtTime);
    producingAtTime = 0;

    // determine which Stage0 created the currentItem
    int temp = currentItem.getID();

    // use modulus to get the last digit
    temp = temp % 10;
    if (temp == 0){
      itemsfrom0a++;
    }
    else if (temp == 1){
      itemsfrom0b++;
    }
    // extract the stored queue wait time
    double[] tempArray = currentItem.getAverageQueueWait();
    for(int i=0;i<6;i++)
    {
      // add the array to our current total
      averageTimeInQueue[i] += tempArray[i];
    }
    currentItem = null;
  }

  // Display the average time an item spends in each queue
  //Preconditons: The stage6 has processed items
  //Postconditions: The average time an item spends in each queue is printed.
  public void displayAverages()
  {
    System.out.println("Average time an item spends in each queue:");
    System.out.println();
    System.out.println("Queue 01 = "+String.format("%5.2f", averageTimeInQueue[0]/getItemsCreated())+" time units");
    System.out.println("Queue 12 = "+String.format("%5.2f", averageTimeInQueue[1]/getItemsCreated())+" time units");
    System.out.println("Queue 23 = "+String.format("%5.2f", averageTimeInQueue[2]/getItemsCreated())+" time units");
    System.out.println("Queue 34 = "+String.format("%5.2f", averageTimeInQueue[3]/getItemsCreated())+" time units");
    System.out.println("Queue 45 = "+String.format("%5.2f", averageTimeInQueue[4]/getItemsCreated())+" time units");
    System.out.println("Queue 56 = "+String.format("%5.2f", averageTimeInQueue[5]/getItemsCreated())+" time units");
    System.out.println();

  }

}
