package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import simulator.ElevatorController;
import simulator.Request;

/**
 * Algorithm Interface
 * 
 * @author Jimmy, Jonathan, Matthew
 * 
 */
public abstract class Algorithm {

	/*
	 * WRITING ALGORITHM HEALTHY REMINDER:
	 * 
	 * 1. Yep 2. Check for out of service: isActive(): boolean 3. Remember if it
	 * is an external request, whether or not you assign a new destination to
	 * any elevator, the elevator you decide that will be responsible for a
	 * particular external request must be put on the externalRequests list.
	 * This is because if an elevator is taken out of service, the
	 * externalRequest list will be put back into the pool of queue to be
	 * handled. Failure to do so will result in external requests not being
	 * handled during elevator error. To add a request to externalRequests,
	 * create a request object then add it to the queue (order does not matter):
	 * getExternal(): ArrayList<Request> 4. You may or may not choose to
	 * overwrite onEventOutOfService 5. The rest is up to you
	 * 
	 * NOTE:
	 * 
	 * 1. Elevator Queue is an ArrayList of integers. 0th index in queue will be
	 * processed first. Each integer represents a floor that the elevator will
	 * go. By modifying this list, the elevator will have different
	 * destinations.
	 * 
	 * METHOD: ArrayList<Integer> - ElevatorController.get(i).getQueue();
	 * 
	 * 2. External Queue is an ArrayList of requests. These requests are
	 * classified as external requests. You must add a request to this queue
	 * every time you handle an external request. When elevator reach a floor,
	 * this list will be checked to see if there is an external request made on
	 * this floor that will be handled by this elevator
	 * 
	 * METHOD: ArrayList<Request> - ElevatorController.get(i).getExternal
	 * 
	 * 3. Request consists of two component. Floor and Direction. e.g. Floor
	 * Five going Up.
	 * 
	 * 4. Use ElevatorController.get(i).getNumFloors() to get max number of
	 * floors.
	 */

	String name;
	int oosThreshold;

	public Algorithm(String algorithm_name) {
		name = algorithm_name;
	}

	public String name() {
		return name;
	}

	public abstract int onEventInternalRequest(ElevatorController ECL,
			ArrayList<Integer> internalRequests);

	public abstract int onEventExternalRequest(
			ArrayList<ElevatorController> elevators, Request extRequest);

	public int onEventOutOfService(ElevatorController EC, boolean critical) {
		EC.getQueue().removeAll(EC.getQueue());
		if (!critical)
			EC.getQueue().add(
					(int) Math.round(EC.positionToFloor(EC.getPosition())));
		return 0;
	}

	public int onEventMaintenanceRequest(
			ArrayList<ElevatorController> elevatorList, ElevatorController EC) {
		int counter = 0;
		for (int i = 0; i < elevatorList.size(); i++) {
			if (!elevatorList.get(i).isActive())
				counter++;
		}
		if (counter < 1)
			EC.goOutOfOrder();
		return counter;
	}

	public void setThreshold(int thr) {
		oosThreshold = thr;
	}

	public int onEventBuildingFire(ArrayList<ElevatorController> elevators,
			boolean activate) {
		if (activate)
			for (ElevatorController ec : elevators)
				ec.goOutOfOrder();
		else
			for (ElevatorController ec : elevators)
				ec.goBackInOrder();
		return 0;
	}

	public int onEventBuildingLockDown(ArrayList<ElevatorController> elevators,
			boolean activate) {
		if (activate)
			for (ElevatorController ec : elevators)
				ec.pause(true);
		else
			for (ElevatorController ec : elevators)
				ec.pause(false);
		return 0;
	}

	public int onEventZombieAttack(ArrayList<ElevatorController> elevators) {
		for (ElevatorController ec : elevators) {
			while (ec.getQueue().contains(new Integer(0)))
				ec.getQueue().remove(new Integer(0));
			for (int i = 0; i < ec.getExternal().size(); i++) {
				if (ec.getExternal().get(i).requestFloor == 0)
					ec.getExternal().remove(i);
			}
		}
		return 0;
	}

	protected void sortElevatorQueue(ElevatorController chosen)
	{
		if (chosen.getQueue().size() == 0)
			return;
		int nextFloor = chosen.getQueue().get(0);
		int currentFloor = (int) chosen.getFloor();
		try{
			Collections.sort(chosen.getQueue());
		}
		catch (NoSuchElementException e){
			System.out.println("Concurrent Sort Screw Up");
			return;
		}
		if ( nextFloor < currentFloor)
			Collections.reverse(chosen.getQueue());
		// 1 2 3 4 5 7 8 9 becomes:
		// 4 5 6 7 8 3 2 1
		
		// 8 5 4 2 1 becomes:
		// 5 4 2 1
		int i = 0;
		while(i > chosen.getQueue().size() && chosen.getQueue().get(i) != nextFloor) i++;
		for(int j = i-1;j>=0;j--)
		{
			chosen.getQueue().add(chosen.getQueue().get(j));
			chosen.getQueue().remove(j);
		}

	}
	public int onEventEvacuation() {
		return 0;
	}

	public abstract int onEventError();
}
