package simulator;

import java.util.Calendar;
import java.util.Random;

/**
 * Passengers
 * @author Jimmy, Jonathan, Matthew
 *
 */
public class Passenger {
	
	private int initFloor;
	private int destFloor;
	private int weight;					//In Kilograms (20 - 150)
	private long timeOfCreation;

	public Passenger(int setInitialFloor, int setDestFloor, int setWeight, long time){
		initFloor = setInitialFloor;
		destFloor = setDestFloor;
		weight = setWeight;
		timeOfCreation = time;
	}
	
	/**
	 * Weight logarithmically distributed 
	 * @param setInitialFloor
	 * @param destFloor
	 */
	public Passenger(int setInitialFloor, int setDestFloor, long time){
		initFloor = setInitialFloor;
		destFloor = setDestFloor;
		weight = (int)(150 - 120*Math.log10((new Random()).nextInt(9)+1));
		timeOfCreation = time;
	}
	
	public String getDirection()
	{
		if (initFloor > destFloor)
			return "DOWN";
		else
			return "UP";
	}
	
	public int getInitFloor(){
		return initFloor;
	}
	

	public int getDestFloor(){
		return destFloor;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public long getTimeOfCreation(){
		return timeOfCreation;
	}
	
	public String toString()
	{
		return ("(" +initFloor + "," + destFloor +"),\t" + weight + ",\t" + timeOfCreation + ",\t" + Calendar.getInstance().getTime());
	}
}
