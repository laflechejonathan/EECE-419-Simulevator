package simulator;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Elevator
 * @author Jimmy, Jonathan, Matthew
 *
 */
public class Elevator {
	int elevatorID;
	public boolean hasPower = true;
	public int oosCounter;	//OutOfServiceCounter
	public int oosThreshold;	// -1 means never out of service
	public boolean needsMaintenance = false;

	//sensors
	public Sensor speedSensor;
	public Sensor weightSensor;
	public Sensor positionSensor;
	public booleanSensor doorSensor;
	
	// Passengers of the Elevator
	public LinkedList<Passenger> passengerList;
	
	public Elevator(int EID, int speed, int mSpeed, int weight, int mWeight, int outOfServiceThreshold) {
		elevatorID = EID;
		passengerList = new LinkedList<Passenger>();
		speedSensor = new Sensor(speed,mSpeed,"Speed Sensor");
		weightSensor = new Sensor(weight, mWeight, "Weight Sensor");
		doorSensor = new booleanSensor(false, "Door Sensor");
		positionSensor = new Sensor(0,1000000000, "Position Sensor");
		oosCounter = 0;
		oosThreshold = outOfServiceThreshold;
	}
	
	/*
	 * ========== Passenger Methods ==========
	 */
	
	public Passenger getPassenger(int index){
		return passengerList.get(index);
	}
	
	public void addPassenger(Passenger p) throws CasualFaultException{
		passengerList.add(p);
		weightSensor.incrementValue(p.getWeight());
	}
	
	public int addPassengers(LinkedList<Passenger> LP) throws CasualFaultException{
		int count = 0;
		if(LP != null)
			for(Passenger p: LP){
				passengerList.add(p);
				weightSensor.incrementValue(p.getWeight());
				count++;
			}
		return count;
	}
	
	public void removePassenger(int index) throws CasualFaultException{
		weightSensor.incrementValue(-passengerList.get(index).getWeight());
		passengerList.remove(index);
	}
	
	public ArrayList<Passenger> exitPassenger(int floor) throws CasualFaultException{
		ArrayList<Passenger> passengersLeaving = new ArrayList<Passenger>();
		for(int i=0; i<passengerList.size();)
			if(passengerList.get(i).getDestFloor() == floor){
				weightSensor.incrementValue( - passengerList.get(i).getWeight());
				passengersLeaving.add(passengerList.get(i));
				passengerList.remove(passengerList.get(i));
				System.gc();
			}
			else
				i++;
		
		return passengersLeaving;
	}
	
	/**
	 * Removes all passengers in the passenger list.
	 */
	public void clearAllPassengers(){
		passengerList.removeAll(passengerList);
	}
	
	public ArrayList<Integer> getInternalRequest(){
		ArrayList<Integer> IR = new ArrayList<Integer>();
		for(Passenger p: passengerList){
			if(IR.indexOf(p.getDestFloor()) < 0){
				IR.add(p.getDestFloor());
			}
		}
		return IR;
	}
		
	public String toString(){
		StringBuffer bs = new StringBuffer("");
		for(Passenger p: passengerList)
			bs.append("(" + p.getInitFloor() + "," + p.getDestFloor() + ") ");
		return bs.toString();
	}
	
	public void move(boolean up) throws CriticalFaultException, CasualFaultException
	{
		doorSensor.check();
		weightSensor.check();
		speedSensor.check();
		positionSensor.check();
		int beforeMove = positionSensor.getValue();
		if (doorSensor.getValue() == true)
			throw new CriticalFaultException("Door Open in Transit");
		if (!hasPower)
			throw new CriticalFaultException("Lost Power in Transit");
		
		if (up)
		{	
			positionSensor.incrementValue(1);
			if (positionSensor.getValue() - beforeMove !=1)
				throw new CriticalFaultException("Position not changing in movement");
		}else
		{
			positionSensor.incrementValue(-1);
			if (beforeMove - positionSensor.getValue() != 1)
				throw new CriticalFaultException("Position not changing in movement");

		}
		oosCounter++;
		if(!needsMaintenance && oosCounter > oosThreshold) needsMaintenance = true;
		if(oosCounter > oosThreshold*3)
			throw new CriticalFaultException("Reached Critical Maintenance Threshold");

	}
	
	public void maintain(){
		oosCounter = 0;
		needsMaintenance = false;
	}
	
	public double getFractionUntilMaintenance(){
		if(oosThreshold <= 0){ return -1; }
		else{ return ((double)oosCounter/(double)oosThreshold); }
	}
	
	public void clearAllSensors()
	{
		doorSensor.clearRandom();
		doorSensor.clearStuck();
		weightSensor.clearConstantBias();
		weightSensor.clearRandomBias();
		weightSensor.clearStuck();
		speedSensor.clearConstantBias();
		speedSensor.clearRandomBias();
		speedSensor.clearStuck();
		positionSensor.clearConstantBias();
		positionSensor.clearRandomBias();
		positionSensor.clearStuck();
	}
}
