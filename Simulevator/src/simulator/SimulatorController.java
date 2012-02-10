package simulator;

import java.io.File;
import java.util.ArrayList;

import statistics.DistanceTraveledChart;
import statistics.FloorRequestsChart;
import statistics.PassengerWaitChart;

import clock.Clock;

import algorithm.*;

/**
 * Interface for scheduler objects
 * 
 * @author Jimmy, Jonathan, Matthew
 * 
 */
public class SimulatorController extends Thread {

	private ArrayList<Algorithm> AL;
	private ArrayList<ElevatorController> ECL;
	private ArrayList<String> connectedUsers;
	private RequestSimulator RS;
	private Scheduler SCH;
	private int numElevators;
	private int numFloors;
	private int simulationStatus;
	private int oosThreshold = -1;
	private File externalRequestFile = null;
	private static int maxElevatorWeight = 1000;
	private Clock sysClock;
	private int mWeight;
	private int mSpeed;

	// Special Events Variable
	// 1: Fire/Earthquake 2: LockDown
	// 3: Zombie Attacking 4: Evacuation
	public int eventStatus = 0;

	public SimulatorController(ArrayList<ElevatorController> pELC,
			RequestSimulator pRS, Scheduler pSCH, int floornum, int eleNum, int mw, int ms) {
		ECL = pELC;
		RS = pRS;
		SCH = pSCH;
		numFloors = floornum;
		numElevators = eleNum;
		mWeight = mw;
		mSpeed = ms;
	}

	public SimulatorController(int floornum, int eleNum,
			int outOfServiceThreshhold,
			File extRequstFile, Clock clk, int mw, int ms) {
		oosThreshold = outOfServiceThreshhold;
		externalRequestFile = extRequstFile;
		numFloors = floornum;
		numElevators = eleNum;
		sysClock = clk;
		mWeight = mw;
		mSpeed = ms;

	}

	public SimulatorController(int floornum, int eleNum) {
		numFloors = floornum;
		numElevators = eleNum;
	}

	public void lockOutFloor(int lock_f) {
		RS.lockFloor(lock_f);
	}

	public void unlockOutFloor(int lock_f) {
		RS.unlockFloor(lock_f);
	}

	public boolean isLockedOut(int lock_f) {
		return RS.isLockedOut(lock_f);
	}

	// ========== Time Scale . Simulation Setting ==========

	public void changeFloorElevator(int floor, int elevator) {
		if (elevator > 0 && elevator < 11)
			elevator = numElevators;
		numFloors = floor;
	}

	public void setTimeScale(double t) {
		RS.setTimeScale(t);
		sysClock.setTimeScale(t);
		for (ElevatorController el : ECL) {
			el.setTimeScale(t);
		}
	}

	// ========== Scheduler =========

	public void changeAlgorithm(String alg) {
		SCH.changeAlgorithm(alg);
	}

	public String getCurrentAlgorithm() {
		return SCH.getCurrentAlgorithm();
	}

	public String[] getavailableAlgorithm() {
		return SCH.availableAlgorithm();
	}

	public String getElevatorUpTime(int elevatorNumber) {
		String format = String.format("%%0%dd", 2);
		long elapsedTime = ECL.get(elevatorNumber).getUptime() / 1000;
		String seconds = String.format(format, elapsedTime % 60);
		String minutes = String.format(format, (elapsedTime % 3600) / 60);
		String hours = String.format(format, elapsedTime / 3600);
		String time = hours + ":" + minutes + ":" + seconds;
		return time;
	}

	public int getElevatorWeight(int elevatorNumber) {
		return ECL.get(elevatorNumber).elevator.weightSensor.getValue();
	}

	public int getElevatorPosition(int elevatorNumber) {
		return ECL.get(elevatorNumber).getPosition();
	}

	public double getElevatorFloor(int elevatorNumber) {
		return ECL.get(elevatorNumber).getFloor();
	}

	public String getElevatorDirection(int elevatorNumber) {
		return ECL.get(elevatorNumber).getDirection();
	}

	public int getElevatorNextDestinations(int elevatorNumber) {
		return ECL.get(elevatorNumber).getNextDestination();
	}

	public String getPassengerString(int elevatorNumber) {
		return ECL.get(elevatorNumber).getElevatorPassengerString();
	}

	public int getDoorSensor(int elevatorNumber) {
		return ECL.get(elevatorNumber).getDoorOpenValue();
	}

	// =========================== Elevator Sensor Faults
	// ====================================

	public void backToNormal()
	{
		untriggerEvent();
		for (ElevatorController e : ECL)
		{
			e.elevator.clearAllSensors();
			e.goBackInOrder();
		}
	}
	public void lockOutElevator(int i) {
		ECL.get(i).goOutOfOrder();
	}

	public void unlockElevator(int i) {
		ECL.get(i).elevator.clearAllSensors();
		ECL.get(i).goBackInOrder();
	}

	public boolean isOutOfOrder(int i) {
		return ECL.get(i).isOutOfOrder();
	}

	public int getElevatorState(int i) {
		// 0: Normal 1: OutOfOrder
		// 2: needs Maintenance 3: OutOfOrder Due To Maintenance
		return ECL.get(i).getElevatorState();
	}

	public void setElevatorSensorStuck(int elevatorNumber, int value,
			String Sensor) {
		// Sensor is either "speed", "weight", "position, "door"
		if (Sensor.equals("speed"))
			ECL.get(elevatorNumber).elevator.speedSensor.setStuck(value);
		else if (Sensor.equals("weight"))
			ECL.get(elevatorNumber).elevator.weightSensor.setStuck(value);
		else if (Sensor.equals("position"))
			ECL.get(elevatorNumber).elevator.positionSensor.setStuck(value);
	}

	public void setElevatorSensorCBias(int elevatorNumber, int value,
			String Sensor) {
		// Sensor is either "speed", "weight", "position, "door"
		if (Sensor.equals("speed"))
			ECL.get(elevatorNumber).elevator.speedSensor.setConstantBias(value);
		else if (Sensor.equals("weight"))
			ECL.get(elevatorNumber).elevator.weightSensor
					.setConstantBias(value);
		else if (Sensor.equals("position"))
			ECL.get(elevatorNumber).elevator.positionSensor
					.setConstantBias(value);
	}

	public void setElevatorSensorRBias(int elevatorNumber, int value,
			String Sensor) {
		// Sensor is either "speed", "weight", "position, "door"
		if (Sensor.equals("speed"))
			ECL.get(elevatorNumber).elevator.speedSensor.setRandomBias(value);
		else if (Sensor.equals("weight"))
			ECL.get(elevatorNumber).elevator.weightSensor.setRandomBias(value);
		else if (Sensor.equals("position"))
			ECL.get(elevatorNumber).elevator.positionSensor
					.setRandomBias(value);
	}

	public void setElevatorDoorSensorStuck(int elevatorNumber, boolean value) {
		ECL.get(elevatorNumber).elevator.doorSensor.setStuck(value);
	}

	public void setelevatorDoorSensorRand(int elevatorNumber) {
		ECL.get(elevatorNumber).elevator.doorSensor.setRandom();
	}

	public void clearElevatorSensorRBias(int elevatorNumber, String Sensor) {
		// Sensor is either "speed", "weight", "position, "door"
		if (Sensor.equals("speed"))
			ECL.get(elevatorNumber).elevator.speedSensor.clearRandomBias();
		else if (Sensor.equals("weight"))
			ECL.get(elevatorNumber).elevator.weightSensor.clearRandomBias();
		else if (Sensor.equals("position"))
			ECL.get(elevatorNumber).elevator.positionSensor.clearRandomBias();
	}

	public void clearElevatorSensorStuck(int elevatorNumber, String Sensor) {
		// Sensor is either "speed", "weight", "position, "door"
		if (Sensor.equals("speed"))
			ECL.get(elevatorNumber).elevator.speedSensor.clearStuck();
		else if (Sensor.equals("weight"))
			ECL.get(elevatorNumber).elevator.weightSensor.clearStuck();
		else if (Sensor.equals("position"))
			ECL.get(elevatorNumber).elevator.positionSensor.clearStuck();
	}

	public void clearElevatorSensorCBias(int elevatorNumber, String Sensor) {
		// Sensor is either "speed", "weight", "position, "door"
		if (Sensor.equals("speed"))
			ECL.get(elevatorNumber).elevator.speedSensor.clearConstantBias();
		else if (Sensor.equals("weight"))
			ECL.get(elevatorNumber).elevator.weightSensor.clearConstantBias();
		else if (Sensor.equals("position"))
			ECL.get(elevatorNumber).elevator.positionSensor.clearConstantBias();
	}

	public void clearElevatorDoorSensorStuck(int elevatorNumber) {
		ECL.get(elevatorNumber).elevator.doorSensor.clearStuck();
	}

	public void clearelevatorDoorSensorRand(int elevatorNumber) {
		ECL.get(elevatorNumber).elevator.doorSensor.clearRandom();
	}

	public void powerOff() {
		for (ElevatorController e : ECL) {
			e.elevator.hasPower = false;
		}
	}

	public void powerOn() {
		for (ElevatorController e : ECL) {
			e.elevator.hasPower = true;
			e.goBackInOrder();
		}
	}

	public void setElevatorSpeed(int elev, int speed) {
		ECL.get(elev).setElevatorSpeed(speed);
	}

	public void setAllElevatorSpeed(int speed) {
		for (int i = 0; i < numElevators; i++)
			ECL.get(i).setElevatorSpeed(speed);
	}

	public int getElevatorSpeed() {
		return ECL.get(0).getElevatorSpeed();
	}

	public void setElevatorDoorDelay(int delayPeriod) {
		for (int i = 0; i < numElevators; i++)
			ECL.get(i).setElevatorDoorDelay(delayPeriod);
	}

	public double getFractionUntilNextMaintenance(int elevatorNumber) {
		return ECL.get(elevatorNumber).getFractionUntilNextMaintenance();
	}
	
	public double getAveragePassengerWaitTime(int elevatorNumber){
		return ECL.get(elevatorNumber).getAverageWaitTime();
	}

	// ========== Request Simulator ==========
	/**
	 * NONE; UP; DOWN; BOTH
	 * 
	 * @param floorNumber
	 * @return
	 */
	public String getFloorRequest(int floorNumber) {
		return RS.getFloorRequest(floorNumber);
	}

	public int getNumPassengerGoingUP(int floorNumber) {
		return RS.getPassengerUPList(floorNumber).size();
	}

	public int getNumPassengerGoingDOWN(int floorNumber) {
		return RS.getPassengerDOWNList(floorNumber).size();
	}

	public void addPassenger(Passenger P) {
		RS.addPassenger(P);
	}

	public void addPassenger(int iFloor, int dFloor) {
		RS.addPassenger(iFloor, dFloor);
	}

	public void addPassenger(int iFloor, int dFloor, int weight) {
		RS.addPassenger(iFloor, dFloor, weight);
	}

	/**
	 * Use an external file to generate requests Note: Once file has finish
	 * reading, request generation is disabled Must use
	 * "enableRequestSimulation(true);" to start again.
	 * 
	 * @param F
	 */
	public void setAndUseExternalRequestFile(File F) {
		RS.setExternalFile(F);
		RS.useExternalFile(true);
	}

	public void setExternalRequestFile(File F) {
		RS.setExternalFile(F);
	}

	public void useExternalRequestFile(boolean use) {
		RS.useExternalFile(use);
	}

	public void setRandomRequestDelay(int min, int max) {
		RS.setRequestDelay(min, max);
	}

	public void setMaxExternalRequest(int maxRequests) {
		// Will not generate if over max limit. -1 means no limit.
		// Only for random generation.
		RS.setMaxExternalRequest(maxRequests);
	}
	
	public void setOOSThreshold(int os) {
		for (Algorithm a : AL)
		{
			a.setThreshold(os);
		}
	}

	/**
	 * Request Pattern Normal Distribution
	 * 
	 * @param initial
	 * @param destination
	 */
	public void useNormalDistribution(boolean initial, boolean destination) {
		RS.useNormalDistribution(initial, destination);
	}

	public boolean setMeanAndSDInit(int mean, double sd) {
		return RS.setMeanAndSDInitONLY(mean, sd);
	}

	public boolean setMeanAndSDDest(int mean, double sd) {
		return RS.setMeanAndSDDestONLY(mean, sd);
	}

	public void changePresent(String S) {
		// Include "morning", "afternoon", "night"
		RS.usePresent(S);
	}

	public void pauseRequestSimulator() {
		RS.pauseSimulation(true);
	}
	
	public void manualRequestSimulation(){
		RS.setManual();
	}

	public void resumeRequestSimulator() {
		RS.pauseSimulation(false);
	}
	public long getLongestPassengerWaitOnFloor(int floor){
		return RS.getWaitTimePassengerOnFloor(floor)/1000;
	}
	// ========== Special Events ==========
	public void triggerBuildingOnFire() {
		// All elevator go to nearest floor and open door
		// All external request are gone (Cancelled since they probably ran for
		// the stairs)
		// Idea is external request clear. All elevator go out of service
		if (eventStatus == 0) {
			eventStatus = 1;
			RS.setEvent("Fire");
			SCH.handleBuildingFire(true);
		}
	}
	
	public void triggerBuildingEarthquake() {
		if (eventStatus == 0)
		{
			eventStatus=5;
			RS.setEvent("Fire");
			SCH.handleBuildingFire(true);
		}
	}

	public void triggerBuildingLockDown() {
		// All elevator stay where they are and doors closed
		// All external request are gone and disabled (Canceled since they
		// probably ran to hide somewhere)
		// Idea is external request clear. All elevator pause
		if (eventStatus == 0) {
			eventStatus = 2;
			RS.setEvent("Lockdown");
			SCH.handleBuildingLockDown(true);
		}
	}

	public void triggerZombieInfestation() {
		// Lock floor 1.
		// All passenger requests being made are going to the top floor
		// Increased number of passenger requests
		// Floor 1 requests are all cancelled.
		if (eventStatus == 0) {
			eventStatus = 3;
			RS.setEvent("Zombie");
			SCH.handleZombieAttack();
		}
	}

	public void triggerBuildingEvacuation() {
		// All passenger requests being made are going to first floor
		// Increased number of passenger requests
		if (eventStatus == 0) {
			eventStatus = 4;
			RS.setEvent("Evacuation");
			SCH.handleEvacuation();
		}
	}

	public void untriggerEvent() {
		switch (eventStatus) {
		case 0:
			break;
		case 1:
			SCH.handleBuildingFire(false);
			break;
		case 2:
			SCH.handleBuildingLockDown(false);
		}
		RS.setEvent("None");
		eventStatus = 0;
	}

	// ========== Simulator Control ==========
	public void startNewSimulation(ArrayList<Algorithm> pAL,
			PassengerWaitChart passWaits, DistanceTraveledChart distTrav,
			FloorRequestsChart floorReq) {
		if (simulationStatus == 0) {
			// ALGORITHM
			if (pAL != null)
				AL = pAL;
			setOOSThreshold(oosThreshold);
			// REQUEST SIMULATOR
			RS = new RequestSimulator(numFloors, sysClock);
			if (externalRequestFile != null)
				setAndUseExternalRequestFile(externalRequestFile);

			// ELEVATOR CONTROLLER
			ECL = new ArrayList<ElevatorController>();
			for (int eCount = 0; eCount < numElevators; eCount++) {
				ECL.add(new ElevatorController(eCount, maxElevatorWeight,
						oosThreshold, RS, sysClock, passWaits, distTrav,
						floorReq));
				ECL.get(eCount).elevator.speedSensor.setValue(mSpeed/2);
				ECL.get(eCount).elevator.positionSensor.setMaxValue(numFloors*60);
				ECL.get(eCount).elevator.speedSensor.setMaxValue(mSpeed);
				ECL.get(eCount).elevator.weightSensor.setMaxValue(mWeight);

			}
			// SCHEDULER + Initialize RS and EC Schedulers
			SCH = new Scheduler(RS, AL, ECL, 0);
			for (int eCount = 0; eCount < numElevators; eCount++)
				ECL.get(eCount).initScheduler(SCH);
			RS.initScheduler(SCH);

			sysClock.start();
			SCH.start();
			RS.start();
			for (ElevatorController ec : ECL)
				ec.start();

			simulationStatus = 1;
		}
	}

	public void pauseSimulation() {
		if (simulationStatus == 1) {
			sysClock.pause();
			for (int i = 0; i < ECL.size(); i++)
				ECL.get(i).pause(true);
			RS.pauseSimulation(true);
			simulationStatus = 2;
		}
	}

	public void resumeSimulation() {
		if (simulationStatus == 2) {
			sysClock.unPause();
			for (int i = 0; i < ECL.size(); i++) {
				ECL.get(i).pause(false);
			}
			RS.pauseSimulation(false);
			simulationStatus = 1;
		}
	}

	public void endSimulation() {
		if (simulationStatus != 0) {

			RS.endThread();
			RS.interrupt();
			RS = null;
			while (ECL.size() != 0) {
				ECL.get(0).endThread();
				ECL.get(0).interrupt();
				ECL.remove(0);
			}
			ECL = null;
			SCH.endThread();
			SCH.interrupt();
			SCH = null;
			AL.remove(AL);
			AL = null;
			Runtime.getRuntime().gc();
			simulationStatus = 0;
		}
	}

	/**
	 * 0: No simulation Running 1: Simulation Running 2: Simulation Paused
	 * 
	 * @return
	 */
	public int getSimulationStatus() {
		return simulationStatus;
	}
	
	/* MEOW2 */ 
	// Implemented
	public long getSimulationTime(){
		return sysClock.getTimeMillis();
	}
	public int getNumFloor(){
		return numFloors;
	}
	public int getNumElevator(){
		return numElevators;
	}
	public int getElevatorNumPassenger(int elevatorNumber){
		return ECL.get(elevatorNumber).elevator.passengerList.size();
	}
	public int getElevatorDistanceTravelled(int elevatorNumber){
		return ECL.get(elevatorNumber).elevator.oosCounter;
	}
	public String[] getConnectedUsers(){
		return (String[]) connectedUsers.toArray();
	}
	public void addConnectedUser(String user){
		connectedUsers.add(user);
	}
	public void removeConnectedUser(String user){
		connectedUsers.remove(user);
	}
	/*
	 * // THIS WILL NOT BE HERE AFTER INTEGRATION // JUST FOR TESTING IO public
	 * void run() { while (true) { try { // FLOOR REQUEST STATUS sleep(100);
	 * 
	 * StringBuffer SB = new StringBuffer(); for (int i = 0; i < numFloors; i++)
	 * { if (getFloorRequest(i).equals("NONE")) SB.append("_\t"); else if
	 * (getFloorRequest(i).equals("UP")) SB.append("U\t"); else if
	 * (getFloorRequest(i).equals("DOWN")) SB.append("D\t"); else
	 * SB.append("B\t"); } SB.append("\n"); for (int i = 0; i < numFloors; i++)
	 * SB.append(getNumPassengerGoingUP(i) + "\t"); SB.append("\n"); for (int i
	 * = 0; i < numFloors; i++) SB.append(getNumPassengerGoingDOWN(i) + "\t"); }
	 * catch (InterruptedException e) { e.printStackTrace(); } } }
	 */
}
