// Stage0.java
// Class that mimics a production station,
//  simulating the time it takes to process an item,
//  and recording statistical information on it's activities
// This stage additionally creates new items for the production line.
//
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
public class Stage0 extends Stage
{
  // instance variables
  private SingletonID idGenerator = SingletonID.getInstance();
  private int itemsCreated = 0;
  private int itemSuffix;

  // constructor
  // preconditon: mean and range values passed, a suffix to append as an ID also passed
  // postcondition: a new stage0 has been initialised.
  public Stage0(double m, double n, int itmSfx)
  {
    super(m, n);
    starving = false;
    blocking = false;
    itemSuffix = itmSfx;
  }

  //Get
  //Preconditon: N/A
  //Postcondition: Returns the itemsCreated variable.
  public int getItemsCreated()
  {
    return itemsCreated;
  }

  //Preconditon: N/A
  //Postcondition: as our input is infinate, as we're creating items as needed
  // always return true.
  public boolean doesSourceHaveItems()
  {
    return true;
  }

  // Create a new item
  // Precondition : Stage0 initialised correctly.
  // Postcondition: a new FactoryItem is initialised and set as our current item
  // it's finish time is calculated, set and returned.
  public double createItem()
  {
    int newID = (idGenerator.getID()+itemSuffix);
    FactoryItem tempItem = new FactoryItem(newID);
    itemsCreated++;
    currentItem = tempItem;
    // calculate our finish time for this stage
    double tempTime = 0.0;
    double d = r.nextDouble();
    tempTime = mean + (range * (d - 0.5));
    tempTime += simTimer.getTime();
    finishTime = tempTime;
    return getFinishTime();
  }

  // Take an Item from the previous queue
  // Precondition : N/A
  // Postcondition: Since Stage0 doesn't use a previousQueue, and instead
  //               creates new items, a new item is created and initialised.
  public void takeItem()
  {
    producingAtTime = simTimer.getTime();
    createItem();
    currentItem.setTEntStage(simTimer.getTime());
  }
}
