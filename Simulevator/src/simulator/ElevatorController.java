package simulator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import statistics.*;

import clock.Clock;

/**
 * Move the elevator. Contact scheduler if there are new internal requests.
 * 
 * @author Jimmy, Jonathan, Matthew
 * 
 */
public class ElevatorController extends Thread {

	Semaphore ECSemaphore;
	private int elevatorID;

	// Properties
	public Elevator elevator;
	private int elevatorFloorRatio; // Position distance between floor
	private boolean pause = false;
	private int elevatorDoorDelay = 1000; // PerPassenger

	// Core Parts
	private RequestSimulator theRequestSimulator;
	private List<Integer> nextDestination;
	private ArrayList<Request> externalRequests;
	private Scheduler myScheduler;
	
	// Time Scale
	public double timeScale = 1;

	// Fault Injection
	private boolean outOfOrder = false;

	// Ending a Thread
	private boolean endThread = false;

	// Clock
	private Clock sysClock;
	private long creationTime;
	// Statistics
	private PassengerWaitChart passWaits;
	private DistanceTraveledChart distTrav;
	private FloorRequestsChart floorReqs;
	private int EID;
	private long distanceTravelled = 0;
	// Special Event Variable
	private String currentEvent = "None";
	
	// Door value for Jackie; Range from 0 to 100
	private int doorOpenValue = 0;
	private int doorOpenSpeedTime = 10; // in ms*100
	
	// Constructor
	public ElevatorController(int eid, int maxEWeight,
			int outOfServiceThreshold, RequestSimulator reqSim, Clock clk,
			PassengerWaitChart pw, DistanceTraveledChart dt, FloorRequestsChart fr) {
		sysClock = clk;
		elevator = new Elevator(elevatorID, 0, 250, 0, maxEWeight,
				outOfServiceThreshold);
		elevatorFloorRatio = 60;
		theRequestSimulator = reqSim;
		ECSemaphore = new Semaphore(1);
		nextDestination = Collections.synchronizedList(new ArrayList<Integer>());
		externalRequests = new ArrayList<Request>();
		endThread = false;
		passWaits = pw;
		distTrav = dt;
		floorReqs = fr;
		EID = eid;
	}

	/**
	 * run() Thread. Constantly move the elevator to destination.
	 */
	public void run() {
		creationTime = sysClock.getTimeMillis();
		while (true && !endThread) {
			try {
				synchronized (this) {
					while ((outOfOrder && nextDestination.isEmpty()) || pause)
						sleep(500);
				}
				sleep(Math.abs(Math.round(1000 / (timeScale * elevator.speedSensor.getValue())))); // Chill/Relax Time
				

				ECSemaphore.acquire();
				// There is no next destination, check if it needs maintenance
				if (nextDestination.isEmpty() )
				{
					if (elevator.needsMaintenance)
						tryGoingIntoMaintenance();
				}
				// Next destination is above
				else if (nextDestination.get(0) > positionToFloor(elevator.positionSensor
						.getValue()))
				{
					elevator.move(true);// elevator.setPosition(elevator.getPosition()
					distTrav.incrementDistanceTraveled(EID);
					distanceTravelled++;
				}						// + 1);
				// Next destination is below
				else if (nextDestination.get(0) < positionToFloor(elevator.positionSensor
						.getValue()))
				{
					elevator.move(false);
					distTrav.incrementDistanceTraveled(EID);
					distanceTravelled++;
				}	
				// elevator.setPosition(elevator.getPosition()
											// - 1);
				// You are at the floor, flow the passengers
				else {
					for (int i = 0; i < externalRequests.size(); i++) {
						if (externalRequests.get(i).requestFloor == nextDestination
								.get(0))
							externalRequests.remove(externalRequests.get(i));
					}
					floorReqs.updateFloorRequestsChart(EID, nextDestination.get(0));
					nextDestination.remove(0);
					passengerFlow((int) Math
							.round(positionToFloor(elevator.positionSensor
									.getValue())));
					
				}
			} catch (InterruptedException e) {
				System.out.println("Elevator Controller Interrupted");
			} catch (CriticalFaultException e) {
				System.out.println(e.toString());
				goStalled();
			} catch (CasualFaultException e) {
				System.out.println(e.toString());
				if (outOfOrder)
					goStalled();
				else
					goOutOfOrder();
			} catch (NullPointerException e){
							
			}finally {
				ECSemaphore.release();
			}
		}
		System.out.println("Elevator Controller Is Finished");
	}
	
	//Clock
	public long getUptime()
	{
		return sysClock.getTimeMillis()-creationTime;
	}

	// =========== Time Scale ======================
	public void setElevatorSpeed(int s) {
		elevator.speedSensor.setValue(s);
	}

	public int getElevatorSpeed() {
		return elevator.speedSensor.getValue();
	}

	public void setTimeScale(double t) {
		timeScale = t;
	}

	// ========= Fault Injection ===========

	public void goOutOfOrder() {
		myScheduler.handleOutOfService(this, false);
		outOfOrder = true;
		distTrav.resetDistanceTraveled(EID);
	}

	public boolean isOutOfOrder() {
		return outOfOrder;
	}

	public void goStalled() {
		myScheduler.handleOutOfService(this, true);
		outOfOrder = true;
		nextDestination.removeAll(nextDestination);
	}
	
	public void tryGoingIntoMaintenance(){
		myScheduler.handleMaintenanceRequest(this);
	}

	public void goBackInOrder() {
		distTrav.resetDistanceTraveled(EID);
		distanceTravelled = 0;
		outOfOrder = false;
		creationTime = sysClock.getTimeMillis();
		elevator.maintain();
	}

	public void maintain() {
		elevator.maintain();
		goBackInOrder();
	}

	public boolean isActive() {
		return !outOfOrder;
	}

	public int getElevatorState(){
		if(outOfOrder)
			return 1;
		else if(!outOfOrder && elevator.needsMaintenance)
			return 2;
		else
			return 0;
	}

	/**
	 * Floor passenger in and out of elevator when elevator arrive at "floor"
	 * 
	 * @param floor
	 * @throws InterruptedException
	 * @throws CriticalFaultException
	 * @throws CasualFaultException
	 */
	private void passengerFlow(int floor) throws InterruptedException,
			CriticalFaultException, CasualFaultException {
		// NOTE: Since this is only called from run()
		// Semaphore is already handled
		// Open elevator door
		while(doorOpenValue<100){doorOpenValue++; sleep(Math.round(doorOpenSpeedTime/timeScale));}
		elevator.doorSensor.setValue(true);	
		// Passenger Exit Elevator
		ArrayList<Passenger> deadPassengers = elevator.exitPassenger(floor);
		//update statistics
		for (Passenger p :deadPassengers)
			passWaits.addPassengerWait(sysClock.getTimeMillis()-p.getTimeOfCreation());
		// Passenger flow into elevator
		if (outOfOrder) {
			theRequestSimulator.takeBack(elevator.passengerList,floor);
			elevator.clearAllPassengers();
		}
		else {
			String nextDirection = null;
			boolean upAndDown = false;
			// Check if this stop has an external request attached
			for(int n=0; n<externalRequests.size();){
				if(externalRequests.get(n).requestFloor == floor){
					if(nextDirection != null && !nextDirection.equals(externalRequests.get(n).direction)) upAndDown = true;
					nextDirection = externalRequests.get(n).direction;
					externalRequests.remove(n);
				}
				else
					n++;
			}
			// If not then just use next destination as a reference
			if(nextDirection == null) nextDirection = getNextDirection(floor);
			// Get passengers
			sleep(Math.round(elevatorDoorDelay*elevator.addPassengers(theRequestSimulator
					.extractFloorPassengers(floor, elevator.weightSensor.getValue(),
							elevator.weightSensor.maxValue, nextDirection))/timeScale));
			// If external request shows we handled this floor for both up and down
			// Then we need to get passengers again.
			if(upAndDown){
				nextDirection = (nextDirection.equals("UP")? "DOWN": "UP");
				sleep(Math.round(elevatorDoorDelay*elevator.addPassengers(theRequestSimulator
						.extractFloorPassengers(floor, elevator.weightSensor.getValue(),
								elevator.weightSensor.maxValue, nextDirection))/timeScale));
			}
			// Signal Scheduler to update its queue
			myScheduler.handleInternalRequest(this,
					elevator.getInternalRequest());
		}
		// Close the elevator doors
		while(doorOpenValue>0){doorOpenValue--; sleep(Math.round(doorOpenSpeedTime/timeScale));}
		elevator.doorSensor.setValue(false);
	}

	// ========== Getters ==========

	public String getDirection() {
		return getNextDirection(positionToFloor(elevator.positionSensor
				.getValue()));
	}

	public int getPosition() {
		return elevator.positionSensor.getValue();
	}

	public double getFloor() {
		return positionToFloor(elevator.positionSensor.getValue());
	}

	public boolean hasRoom() {
		return true;
	}

	public int getNextDestination() {
		if (!nextDestination.isEmpty())
			return nextDestination.get(0);
		else
			return -1;
	}

	public String getElevatorPassengerString() {
		return elevator.toString();
	}

	public boolean isFree() {
		if (nextDestination.isEmpty())
			return true;
		return false;
	}

	public List<Integer> getQueue() {
		return nextDestination;
	}

	public ArrayList<Request> getExternal() {
		return externalRequests;
	}

	public double getFractionUntilNextMaintenance() {
		return elevator.getFractionUntilMaintenance();
	}
	
	public int getDoorOpenValue(){
		return doorOpenValue;
	}
	
	public int getNumFloors(){
		return theRequestSimulator.getNumFloors();
	}
	
	public long getDistancedTravelled(){
		return distanceTravelled;
	}
	
	public double getAverageWaitTime(){
		return passWaits.getWaitTimeAverage();
	}
	
	// ========== Setter =========

	public void pause(boolean p) {
		pause = p;
	}

	public void setElevatorDoorDelay(int delay) {
		elevatorDoorDelay = delay;
	}
	
	public void setEvent(String event){
		if(!currentEvent.equals("None")){
			if(event.equals("Fire")){
				currentEvent = "Fire";
			}
			else if(event.equals("Lockdown")){
				currentEvent = "Lockdown";
			}
			else if(event.equals("Zombie")){
				
			}
			else if(event.equals("Evacuation))")){
				
			}
			else if(event.equals("None")){
				
			}
		}
	}

	// ========== End The Thread =========

	public void endThread() {
		endThread = true;
	}

	// ========== Scheduler Initializer =========

	/**
	 * Initialize internal scheduler
	 */
	public void initScheduler(Scheduler s) {
		myScheduler = s;
	}

	// ========== Functions ==========

	/**
	 * Return fraction of floor reached to position
	 * 
	 * @param position
	 * @return
	 */
	public double positionToFloor(int position) {
		return (double) position / (double) elevatorFloorRatio;
	}

	private String getNextDirection(double currentFloor) {
		if (nextDestination.isEmpty())
			return "BOTH";
		else if (nextDestination.get(0) > currentFloor)
			return "UP";
		else
			return "DOWN";
	}

}
