// SingletonTime.java
// Singleton Design Pattern hold simTime variable, making it accessible to
// the rest of the program
// 
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
public class SingletonTime
{
  // instance Variables
  private double simTime = 0.0;
  private static SingletonTime instance = new SingletonTime();
  // private constructor
  private SingletonTime(){}

  //GET
  // Precondition: N/A
  // Postcondition: return the only instance/object available
  public static SingletonTime getInstance()
  {
      return instance;
   }

  // precondition: N/A
  // Postcondition: simTime variable is returned.
  public double getTime()
  {
    return (simTime);
  }

  //SET
  // precondition: double is passed as parameter
  // Postcondition: simTime variable is set to new value.
  public void setTime(double newTime)
  {
    simTime = newTime;
  }
}
