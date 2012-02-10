package simulator;

import java.util.ArrayList;
import algorithm.Algorithm;

/**
 * Scheduler
 * @author Jimmy, Jonathan, Matthew
 *
 */
public class Scheduler extends Thread {
	private Algorithm currentAlgorithm;
	private RequestSimulator theRequestSimulator;
	private ArrayList<Algorithm> availableAlgorithms;
	private ArrayList<ElevatorController> elevatorList;
	private boolean endThread = false;
	private boolean isSpecialEvent = false;

	public Scheduler(RequestSimulator rSim, ArrayList<Algorithm> aList,
			ArrayList<ElevatorController> eList, int ca) {
		theRequestSimulator = rSim;
		availableAlgorithms = aList;
		elevatorList = eList;
		currentAlgorithm = availableAlgorithms.get(0);
		endThread = false;
	}

	public void run() {
		while (true && !endThread) {
			try {
				while (!theRequestSimulator.isEmpty())
					handleExternalRequest(elevatorList, theRequestSimulator.poll());
				sleep(600000);
			} catch (InterruptedException e) {
				// Do Nothing. 
				// If interrupted that means wake the scheduler up.
			}
		}
		System.out.println("Scheduler is Completed");
	}

	public void changeAlgorithm(String newAlgorithm) {
		for(Algorithm al: availableAlgorithms)
			if(al.name().equals(newAlgorithm)){
				currentAlgorithm = al;
				break;
			}
	}
	
	public String getCurrentAlgorithm() {
		return currentAlgorithm.name();
	}
	
	public String[] availableAlgorithm(){
		String[] algorithms = new String[availableAlgorithms.size()];
		for(int i=0; i<availableAlgorithms.size(); i++)
			algorithms[i] = availableAlgorithms.get(i).name();
		return algorithms;
	}
	
	public void takeBackRequests(ElevatorController dying)
	{
		for(int i = 0;i<dying.getExternal().size();i++)
			handleExternalRequest(elevatorList, dying.getExternal().get(i));
	}

	
	synchronized public int handleInternalRequest(ElevatorController pECL, ArrayList<Integer> internalRequests){
		return currentAlgorithm.onEventInternalRequest(pECL, internalRequests);
	}
	synchronized public int handleExternalRequest(ArrayList<ElevatorController> elevators, Request extRequest){
		return currentAlgorithm.onEventExternalRequest(elevators, extRequest);
	}
	synchronized public int handleOutOfService(ElevatorController EC,boolean critical){
		if(!isSpecialEvent)takeBackRequests(EC);
		return currentAlgorithm.onEventOutOfService(EC,critical);

	}
	synchronized public int handleMaintenanceRequest(ElevatorController EC){
		return currentAlgorithm.onEventMaintenanceRequest(elevatorList, EC);
	}
	synchronized public int handleBuildingFire(boolean active){
		if(active)isSpecialEvent = true;else isSpecialEvent = false;
		return currentAlgorithm.onEventBuildingFire(elevatorList, active);
	}
	synchronized public int handleBuildingLockDown(boolean active){
		if(active)isSpecialEvent = true;else isSpecialEvent = false;
		return currentAlgorithm.onEventBuildingLockDown(elevatorList, active);
	}
	synchronized public int handleZombieAttack(){
		return currentAlgorithm.onEventZombieAttack(elevatorList);
	}
	synchronized public int handleEvacuation(){
		return currentAlgorithm.onEventEvacuation();
	}
	synchronized public int onEventError(){
		return currentAlgorithm.onEventError();
	}

	// Ending The Thread
	public void endThread(){
		endThread = true;
	}
	
}
