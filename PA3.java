// PA3.java
// Simulates a production line using discrete event simulation.
// outputs statistical information on the simulated production line.
//
// Programmer:  Jonathan Godley - c3188072
// Course: SENG2200
// Last modified:  29/05/2017
public class PA3
{
	public static void main (String[] args)
	{
    try
    {
        PA3 obj = new PA3 ();
        obj.run (args);
    }
    catch (Exception e)
    {
        e.printStackTrace (); //so we can actually see when stuff goes wrong
    }
	}

	public void run (String[] args) throws Exception
	{
		// Variables
		int qMax = Integer.parseInt(args[2]);
		double mean = Double.parseDouble(args[0]);
		double range = Double.parseDouble(args[1]);
		double timeLimit = 10000000.0;
		double[] queueFillAvg = {0.0,0.0,0.0,0.0,0.0,0.0};
		int queueFillCounts = 0;
		int numberOfStages = 10;

		// Check inputted qMax to make sure it's >1
		//qMax MUST be >1
		if (!(qMax>1)){
			qMax = 2;
			System.out.println("qMax was below minimum, setting to 2");
		}

		// Class Init
		// timeQueue max = number of production stages
		TimeQueue<Double> timeQueue = new TimeQueue<Double>(numberOfStages);
		SingletonTime simTimer = SingletonTime.getInstance();
		// Stages
		// Stage Pointer to make manipulating a bit easier
		Stage stagePTR;
		// second pointer to use in the blocking/unblocking loop
		Stage stagePTRB;
		// stage 0
		Stage0 s0a = new Stage0((mean*2),(range*2),0);
		Stage0 s0b = new Stage0(mean,range,1);
		// Stage 1 & 2
		Stage s1 = new Stage(mean,range);
		Stage s2 = new Stage(mean,range);
		// Stage 3
		Stage s3a = new Stage((mean*2),(range*2));
		Stage s3b = new Stage((mean*2),(range*2));
		// Stage 4
		Stage s4 = new Stage(mean,range);
		// Stage 5
		Stage s5a = new Stage((mean*2),(range*2));
		Stage s5b = new Stage((mean*2),(range*2));
		// Stage 6
		Stage6 s6 = new Stage6(mean,range);
		// Queues
		IS_Storage q01 = new IS_Storage(qMax);
		IS_Storage q12 = new IS_Storage(qMax);
		IS_Storage q23 = new IS_Storage(qMax);
		IS_Storage q34 = new IS_Storage(qMax);
		IS_Storage q45 = new IS_Storage(qMax);
		IS_Storage q56 = new IS_Storage(qMax);

		// Now we setup the links in our factory,
		// we link each stage to the queue before and after it.

		// Stage 0 -> Queue 01
		// Queue 01 <- Stage 1 -> Queue 12
		// Queue 12 <- Stage 2 -> Queue 23
		// Queue 23 <- Stage 3 -> Queue 34
		// Queue 34 <- Stage 4 -> Queue 45
		// Queue 45 <- Stage 5 -> Queue 56
		// Queue 56 <- Stage 6

		// Stage 0 -> Queue 01
		s0a.setNextQueue(q01);
		s0b.setNextQueue(q01);
		// Queue 01 <- Stage 1 -> Queue 12
		s1.setPrevQueue(q01);
		s1.setNextQueue(q12);
		// Queue 12 <- Stage 2 -> Queue 23
		s2.setPrevQueue(q12);
		s2.setNextQueue(q23);
		// Queue 23 <- Stage 3 -> Queue 34
		s3a.setPrevQueue(q23);
		s3b.setPrevQueue(q23);
		s3a.setNextQueue(q34);
		s3b.setNextQueue(q34);
		// Queue 34 <- Stage 4 -> Queue 45
		s4.setPrevQueue(q34);
		s4.setNextQueue(q45);
		// Queue 45 <- Stage 5 -> Queue 56
		s5a.setPrevQueue(q45);
		s5b.setPrevQueue(q45);
		s5a.setNextQueue(q56);
		s5b.setNextQueue(q56);
		// Queue 56 <- Stage 6
		s6.setPrevQueue(q56);

		// We've now initialsed our entire factory,
		// All the Stages are linked and initialised

		// now we need to generate the first 2 objects,
		// and log their completion times in our priority queue
		timeQueue.enqueue(s0a.createItem());
		timeQueue.enqueue(s0b.createItem());

		// We're also going to intialise our two pointers to them, just to avoid
		// a compiler error relating to our if-chains.
		stagePTR = s0a;
		stagePTRB = s0b;

		// loop while we have items in our queue, which should be always,
		// and while we're below the simulation time.
		while((timeQueue.hasItems() == true)&&(timeQueue.check()<timeLimit))
		{
			// update our global clock to the latest event time.
			double simTime = timeQueue.dequeue();
			simTimer.setTime(simTime);

			// first we work out which station is finished
			// and set our pointer to it.
			if (simTime == s0a.getFinishTime()){stagePTR = s0a;}
			else if (simTime == s0b.getFinishTime()){stagePTR = s0b;}
			else if (simTime == s1.getFinishTime()){stagePTR = s1;}
			else if (simTime == s2.getFinishTime()){stagePTR = s2;}
			else if (simTime == s3a.getFinishTime()){stagePTR = s3a;}
			else if (simTime == s3b.getFinishTime()){stagePTR = s3b;}
			else if (simTime == s4.getFinishTime()){stagePTR = s4;}
			else if (simTime == s5a.getFinishTime()){stagePTR = s5a;}
			else if (simTime == s5b.getFinishTime()){stagePTR = s5b;}
			else if (simTime == s6.getFinishTime()){stagePTR = s6;}

			// so we've found which station is finished it's production.
			// so we check it's output queue for space
			if (stagePTR.doesDestinationHaveSpace()==true)
			{
				//we pass our item on
				stagePTR.passItem();
				// we check previous queue for items
				if (stagePTR.doesSourceHaveItems()==true)
				{
					// now we take a new item into our stagePTR
					stagePTR.takeItem();
					// and we add it's finish time to our priority queue
					timeQueue.enqueue(stagePTR.getFinishTime());
				}
				else
				{
					// if no items in queue, we're starving
					stagePTR.setStarving();
				}
			}
			else
			{
				// if no space in next queue, we're blocked
				stagePTR.setBlocked();
			}

			// now we'll check all stations for starvation excl Stage0 which can't starving
			// we'll do this by using a loop with a pointer that changes to the next stage every loop
			for (int i = 0;i<8;i++)
			{
				//set our pointer
				if (i == 7){stagePTR = s1;}
				else if (i == 6){stagePTR = s2;}
				else if (i == 5){stagePTR = s3a;}
				else if (i == 4){stagePTR = s3b;}
				else if (i == 3){stagePTR = s4;}
				else if (i == 2){stagePTR = s5a;}
				else if (i == 1){stagePTR = s5b;}
				else if (i == 0){stagePTR = s6;}

				// check for starvation
				if (stagePTR.checkStarve()==true){
					// are we still starved?
					if (stagePTR.doesSourceHaveItems()==true)
					{
						// if not starving, take an item and queue it's due date.
						stagePTR.takeItem();
						stagePTR.setNotStarving();
						timeQueue.enqueue(stagePTR.getFinishTime());
					}
				}
			}

			// now check all stations for blockages excl S6 which can't be blocked
			// we go by "blocked time" as blocked first = first unblocked
			// we'll use our pointer again, but we'll using a second pointer as well.
			for (int i = 0;i<6;i++)
			{
				//set our pointer
				if (i == 5){stagePTR = s0a;stagePTRB = s0b;}//S0
				else if (i == 4){stagePTR = s1;}
				else if (i == 3){stagePTR = s2;}
				else if (i == 2){stagePTR = s3a;stagePTRB = s3b;}//S3
				else if (i == 1){stagePTR = s4;}
				else if (i == 0){stagePTR = s5a;stagePTRB = s5b;}//S5

				// now our s4,s2 and S1 are very simple, theres no split in them.
				if (i==1||i==3||i==4)
				{
					// is this stage blocked?
					if (stagePTR.checkBlock()==true){
						// is this stage STILL blocked?
						if(stagePTR.doesDestinationHaveSpace()==true)
						{
							// if not blocked, pass an item and check if we can take a new one
							stagePTR.passItem();
							stagePTR.setNotBlocked();
							if (stagePTR.doesSourceHaveItems()==true)
							{
								// if there are items in prev queue, we can take one
								stagePTR.takeItem();
								timeQueue.enqueue(stagePTR.getFinishTime());
							}
							else
							{
									// if no items in queue, we're starving
									stagePTR.setStarving();
							}
						}
					}
				}

				// our s0, s3, and s5 are a bit more complicated, as they're split into
				// SXa and SXb, and we need to determine which one to unblock first.
				else if(i==0||i==2||i==5)
				{
					// if both parts of the stage are blocked
					if ((stagePTRB.checkBlock()==true)&&(stagePTR.checkBlock()==true))
					{
						// determine which stage was blocked first, and unblock it first
						// check if SXa was first
						if (stagePTR.getBlockTime()<stagePTRB.getBlockTime())
						{
							// are we still blocked?
							if(stagePTR.doesDestinationHaveSpace()==true)
							{
								// if not blocked, send our item on and see if we can get a new one.
								stagePTR.passItem();
								stagePTR.setNotBlocked();
								if (stagePTR.doesSourceHaveItems()==true)
								{
									// if not starving, take item
									stagePTR.takeItem();
									timeQueue.enqueue(stagePTR.getFinishTime());
								}
								else
								{
										// if no items in queue, we're starving
										stagePTR.setStarving();
								}
							}
						}
						else // otherwise unblock SXb first, exactly the same as SXa, just a different pointer.
						{
							if(stagePTRB.doesDestinationHaveSpace()==true)
							{
								stagePTRB.passItem();
								stagePTRB.setNotBlocked();
								if (stagePTRB.doesSourceHaveItems()==true){
									stagePTRB.takeItem();
									timeQueue.enqueue(stagePTRB.getFinishTime());
								}
								else
								{
										stagePTRB.setStarving();
								}
							}
						}
					}

					// now check if only one is blocked, (which should ALSO be true after
					// 		we determine both are blocked, as the first code block only unblocks
					// 		the sub-stage that was blocked first)

					// check if stage is blocked
					if (stagePTRB.checkBlock()==true)
					{
						if(stagePTRB.doesDestinationHaveSpace()==true)
						{
							// if not blocked, pass item
							stagePTRB.passItem();
							stagePTRB.setNotBlocked();
							// check if stage starved
							if (stagePTRB.doesSourceHaveItems()==true){
								// if not starved take new item.
								stagePTRB.takeItem();
								timeQueue.enqueue(stagePTRB.getFinishTime());
							}
							else
							{
								stagePTRB.setStarving();
							}
						}
					}
					// check if stage is blocked
					if (stagePTR.checkBlock()==true)
					{
						//check if stage is STILL blocked
						if(stagePTR.doesDestinationHaveSpace()==true)
						{
							// if not blocked pass an item
							stagePTR.passItem();
							stagePTR.setNotBlocked();
							// check if starved
							if (stagePTR.doesSourceHaveItems()==true)
							{
								// if not starved take an item
								stagePTR.takeItem();
								timeQueue.enqueue(stagePTR.getFinishTime());
							}
							else
							{
									stagePTR.setStarving();
							}
						}
					}
				}
			}

			//At every time event, take an average of how many items are in each queue.
			// Not a perfect situation, due to the un-even nature of these snap-shots
			// but should be pretty close.
			queueFillAvg[0]+=q01.size();
			queueFillAvg[1]+=q12.size();
			queueFillAvg[2]+=q23.size();
			queueFillAvg[3]+=q34.size();
			queueFillAvg[4]+=q45.size();
			queueFillAvg[5]+=q56.size();
			queueFillCounts++;

			// now we restart the loop and go to the next discrete time event
		}

		// results
		// a) actual production (as a percentage),
		// b) how much time is spent starving, and
		// c) how much time is spent blocked.
		// so we're going to display this
		System.out.println();
		System.out.println("Results:");
		System.out.println();
		System.out.println("Stage  Producing  Starving  Blocked");
		System.out.println(" S0a" + s0a);
		System.out.println(" S0b" + s0b);
		System.out.println(" S1 " + s1);
		System.out.println(" S2 " + s2);
		System.out.println(" S3a" + s3a);
		System.out.println(" S3b" + s3b);
		System.out.println(" S4 " + s4);
		System.out.println(" S5a" + s5a);
		System.out.println(" S5b" + s5b);
		System.out.println(" S6 " + s6);
		System.out.println();

		// For the inter-stage storages, calculate
		// d) the average time an item spends in each queue, and
		s6.displayAverages();

		// e) the average number of items in the queue at any time
		System.out.println("Average number of items in the queue at any time");
    System.out.println();
    System.out.println("Queue 01 = "+String.format("%3.2f", queueFillAvg[0]/queueFillCounts)+" items");
    System.out.println("Queue 12 = "+String.format("%3.2f", queueFillAvg[1]/queueFillCounts)+" items");
    System.out.println("Queue 23 = "+String.format("%3.2f", queueFillAvg[2]/queueFillCounts)+" items");
    System.out.println("Queue 34 = "+String.format("%3.2f", queueFillAvg[3]/queueFillCounts)+" items");
    System.out.println("Queue 45 = "+String.format("%3.2f", queueFillAvg[4]/queueFillCounts)+" items");
    System.out.println("Queue 56 = "+String.format("%3.2f", queueFillAvg[5]/queueFillCounts)+" items");
    System.out.println();

		// finally
		// f) tabulate the number of items created by S0a and S0b
		System.out.println("Total items produced:  "+ s6.getItemsCreated());
		System.out.println("Produced by S0a:       "+ s6.totalA());
		System.out.println("Produced by S0b:       "+ s6.totalB());
		System.out.println();

		// exit properly
		System.exit ( 0 );
	}
}
