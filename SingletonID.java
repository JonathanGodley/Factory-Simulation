// SingletonID.java
// Singleton Design Pattern used to generate Unique IDs for Items
// 
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  28/05/2017
public class SingletonID
{
  // instance Variables
  private int nextID = 0;
  private static SingletonID instance = new SingletonID();
  // private constructor
  private SingletonID(){}

  //GET
  // preconditon: N/a
  // Postcondition: return the only instance/object available
  public static SingletonID getInstance()
  {
      return instance;
   }
  //precondition: N/A
  //Postcondition unique ID returned as int
  public int getID()
  {
    nextID++;
    return (nextID * 10);
  }
}
