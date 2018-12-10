// FactoryItem.java
// Just a class that can be passed through different stations,
// containing data about it's journey for later analysis
//
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
public class FactoryItem
{
    // Instance Variables
    private int uniqueID;
    private double[] timeEnteredStage = new double[7];
    private double[] timeLeftStage = new double[7];
    private int curStage = 0;

    // Constructor
    // Precondition : an int is passed as a parameter
    // Postcondition:  A new Item is created with a unique ID
    public FactoryItem(int newID)
    {
        uniqueID = newID;
      }

    // SET
    // Precondition : a double containing the current system's simTime is passed
    // Postcondition: entTime is entered in to the appropriate position in the timeEnteredStage array
    public void setTEntStage(double entTime)
    {
        timeEnteredStage[curStage] = entTime;
    }

    // Precondition : a double containing the current system's simTime is passed
    // Postcondition: leftTime is entered in to the appropriate position
    //                in the timeLeftStage array, and we increment the current stage
    public void setTLeaveStage(double leftTime)
    {
        timeLeftStage[curStage] = leftTime;
        curStage++;
    }

    // GET
    // Precondition : This FactoryItem object has been correctly initialsed
    // Postcondition: unique ID integer returned
    public int getID()
    {
        return uniqueID;
    }

    // Precondition : timeEnteredStage and timeLeftStage must both be propagated
    // Postcondition: the average time in each queue for this item is calculated and returned as an array
    public double[] getAverageQueueWait()
    {
      double[] avgQ = new double[6];
      avgQ[0] = timeEnteredStage[1] - timeLeftStage[0];
      avgQ[1] = timeEnteredStage[2] - timeLeftStage[1];
      avgQ[2] = timeEnteredStage[3] - timeLeftStage[2];
      avgQ[3] = timeEnteredStage[4] - timeLeftStage[3];
      avgQ[4] = timeEnteredStage[5] - timeLeftStage[4];
      avgQ[5] = timeEnteredStage[6] - timeLeftStage[5];
      return avgQ;
    }
}
