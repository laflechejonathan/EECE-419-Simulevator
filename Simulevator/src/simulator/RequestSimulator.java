package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

import clock.Clock;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

/**
 * Elevator Request Simulator
 * - Randomly generate request and puts them onto an internal floor queue.
 * - Notify scheduler when new requests are made
 * - Upon request by elevator controller. Remove its own passenger and
 *   return them to the elevator controller.
 * @author Jimmy, Jonathan, Matthew
 *
 */
public class RequestSimulator extends Thread {
	
	private static Semaphore requestSemaphore;
	private static Semaphore floorSemaphore;
	
	private ArrayList<LinkedList<Passenger>> floorsUp;
	private ArrayList<LinkedList<Passenger>> floorsDown;
	private int numberOfFloors = 0;
	private boolean manual = false;
	
	// Floor lock-out
	private boolean[] lockedOutFloors;
	
	private Scheduler myScheduler;
	
	// List of non-handled requests
	private Queue<Request> requests;

	// Time Scale
	public double timeScale = 1;
	
	// Random Request Simulation Properties
	private int minRequestDelay = 1000;
	private int maxRequestDelay = 5000;
	private int maxExternalRequest = -1;
	private boolean generateRequest = true;
	
	// Variables for Reading requests from input file
	private File externalRequestFile; // = new File("externalRequestFile");
	private boolean useExternalFile = false;
	
	// Normal Distribution Mode
	private boolean normalDistribute_initial = false;
	private boolean normalDistribute_destination = false;
	private RandomEngine re = new DRand();
	private double[] meansd = new double[4];
	private Normal nm_init = new Normal(0.0, 10.0, re);
	private Normal nm_dest = new Normal(0.0, 10.0, re);
	
	// Ending a Thread
	private boolean endThread = false;;
	private Clock sysClock;

	// Special Event Variable
	private String currentEvent = "None";
	
	/**
	 * Constructor
	 * @param numOfFloors
	 * @param numOfElevators
	 */
	public RequestSimulator(int numOfFloors, Clock clk) {
		
		sysClock = clk;
		requestSemaphore = new Semaphore(1);
		floorSemaphore = new Semaphore(1);
		
		floorsUp = new ArrayList<LinkedList<Passenger>>();
		floorsDown = new ArrayList<LinkedList<Passenger>>();
		
		numberOfFloors = numOfFloors;
		
		for(int i=0; i<numOfFloors; i++)
		{
			floorsUp.add(new LinkedList<Passenger>());
			floorsDown.add(new LinkedList<Passenger>());
		}
		lockedOutFloors = new boolean[numOfFloors];
		for(int i=0; i<numOfFloors; i++) lockedOutFloors[i] = false;
		
		requests = new LinkedList<Request>();
		
		endThread = false;
	}

	
	/**
	 * Run() Thread.
	 * Randomly wakes up to a randomly generate a passenger on random floor
	 */
	public void run() {
		Random rand = new Random();
		int initialFloor, destinationFloor;
		int sleepTime;
		while (true && !endThread && !manual) {
			try
			{
				sleep(30);
				if(!generateRequest || maxExternalRequest==0)
				{
					sleep(500);
				}
				else if(currentEvent.equals("Zombie") || currentEvent.equals("Evacuation")){
					sleepTime = rand.nextInt(((maxRequestDelay-minRequestDelay) + minRequestDelay)/2);
					sleep(Math.abs(Math.round(sleepTime / timeScale)));
					if(!generateRequest) continue;
					// Random generate passenger initial floor and destination floor
					do{
						initialFloor = rand.nextInt(floorsUp.size()-2)+1;
						if(currentEvent.equals("Zombie")) destinationFloor = floorsUp.size()-1;
						else destinationFloor = 0;
					}while(initialFloor == destinationFloor);
					
					// Create Passenger and add to queue
					addPassenger(initialFloor, destinationFloor);
				}
				else if(!useExternalFile)
				{
					int counter = 0;
					if(maxExternalRequest > 0){
						for(int i=0; i<floorsUp.size(); i++){
							if(floorsUp.get(i).size() > 0) counter++;
							if(floorsDown.get(i).size() > 0) counter++;
							if(counter > maxExternalRequest){ counter = -1; break;}
						}
					}
					if(counter != -1){
						sleepTime = rand.nextInt(maxRequestDelay-minRequestDelay) + minRequestDelay;
						sleep(Math.abs(Math.round(sleepTime / timeScale)));
						if(!generateRequest) continue;
						// Random generate passenger initial floor and destination floor
						do{
							if(normalDistribute_initial) 
								initialFloor = getNormalizedExternalRequest_init();
							else
								initialFloor = rand.nextInt(floorsUp.size()-1);
							if(normalDistribute_destination) 
								destinationFloor = getNormalizedExternalRequest_dest();
							else
								destinationFloor = rand.nextInt(floorsUp.size()-1);
							
						}while(initialFloor == destinationFloor);
						
						// Create Passenger and add to queue
						addPassenger(initialFloor, destinationFloor);
					}
				}
				else
				{
					BufferedReader br = new BufferedReader(new FileReader(externalRequestFile));
					String line = null;
					while((line = br.readLine()) != null && useExternalFile){
						while(!generateRequest)sleep(500);
						if(line.contains(",")){
							try{
								initialFloor = Integer.parseInt(line.split(",")[0].trim());
								destinationFloor = Integer.parseInt(line.split(",")[1].trim());
								if(!(initialFloor<0 || initialFloor>(numberOfFloors-1) || destinationFloor<0 || destinationFloor>(numberOfFloors-1))){
									sleepTime = Integer.parseInt(line.split(",")[2].trim());
									addPassenger(initialFloor, destinationFloor);
									sleep(Math.abs(Math.round(sleepTime / timeScale)));
								}
							}catch(Exception e){
								//Something went wrong, let's just not read this file anymore.
								break;
							}
						}
					}
					br.close();
					useExternalFile = false;
					generateRequest = false;	// Turn off the simulator once file is finished reading until resumed.
				}
				
			}catch(InterruptedException e){
				System.out.println("Request Simulator Interrupted");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Request Simulator Is Finished");
	}	
	
/*
 * Parameters for setting the time scale and
 * how frequently to generate requests
 * 
 */
	public void setTimeScale(double t)
	{
		timeScale = t;
	}
	public void setRequestDelay(int min, int max)
	{
		minRequestDelay = min;
		maxRequestDelay = max;
	}
	// -1: No limit, 0: No more generation.
	public void setMaxExternalRequest(int max){
		maxExternalRequest = max;
	}
	public void pauseSimulation(boolean b){
		generateRequest = !b;
	}

	
/*
 * Parameters for setting input file to support
 * simulation request from external file
 */
	public void setExternalFile(File f)
	{
		externalRequestFile = f;
	}
	public void useExternalFile(boolean use)
	{
		useExternalFile = use;
	}
	
	/**
	 * Check if there is any elevator requests
	 * @return boolean
	 */
	 public boolean isEmpty(){
		boolean empty = true;
		try {
			requestSemaphore.acquire();	
			empty = requests.isEmpty();
		} catch (InterruptedException e) {
			
		}finally{
			requestSemaphore.release();
		}
		return empty;
	}
	
	/**
	 * Take a look at the first queue
	 * @return
	 */
	public Request peek(){
		return requests.peek();
	}
	
	/**
	 * Take a look at the first queue and remove it
	 * @return
	 */
	public Request poll(){
		return requests.poll();
	}
	
	/**
	 * Take a look at the first queue and remove it
	 * @return
	 */
	public Request remove(){
		return requests.remove();
	}

	/**
	 * Put all passenger onto a given floor
	 * @param strandedP
	 * @param floor
	 */
	public void takeBack(LinkedList<Passenger> strandedP,int floor)
	{
		if(!(currentEvent.equals("Fire") || currentEvent.equals("Lockdown")))
			for(Passenger p : strandedP)
				addPassenger(floor, p.getDestFloor());
	}
	
	/**
	 * Remove passenger from floor queue and return them
	 * @param floorNumber
	 * @param maxWeight
	 * @param direction
	 * @return
	 */
	public LinkedList<Passenger> extractFloorPassengers(int floorNumber, int currentWeight, int maxWeight, String direction){
		
		LinkedList<Passenger> passengerList = new LinkedList<Passenger>();
		LinkedList<Passenger> floorPassengerList;
		try {
			floorSemaphore.acquire();
			
			if (direction.equals("BOTH")){
				if(floorsUp.get(floorNumber).size() > floorsDown.get(floorNumber).size())
					direction = "UP";
				else 
					direction = "DOWN";
			}
			if (direction.equals("UP"))
				floorPassengerList = floorsUp.get(floorNumber);
			else
				floorPassengerList = floorsDown.get(floorNumber);
			
			
			while(floorPassengerList.size()>0){
				if((currentWeight + floorPassengerList.getFirst().getWeight()) < maxWeight){
					passengerList.add(floorPassengerList.getFirst());
					currentWeight += floorPassengerList.getFirst().getWeight();
					floorPassengerList.removeFirst();
				}
				else
					break;
			}
			if (floorPassengerList.size() > 0)
			{
				requestSemaphore.acquire();
				requests.add(new Request(floorNumber, direction));
				requestSemaphore.release();
			}
			floorSemaphore.release();			
		} catch (InterruptedException e) {
			e.printStackTrace(); releaseSemaphore();
		}		
		return passengerList;
	}
	
	/**
	 * Remove the first request queue
	 */
	public void requestHandled()
	{
		try {
			requestSemaphore.acquire();	
			requests.remove();
			requestSemaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace(); releaseSemaphore();
		}
	}
	
	/**
	 * Add a passenger to floor queue
	 * @param initFloor
	 * @param destFloor
	 */
	public boolean addPassenger(int initFloor, int destFloor){
		return addPassenger(new Passenger(initFloor, destFloor,sysClock.getTimeMillis()));
	}
	public boolean addPassenger(int initFloor, int destFloor, int weight){
		return addPassenger(new Passenger(initFloor, destFloor, weight, sysClock.getTimeMillis()));
	}
	/**
	 * Add a passenger to floor queue
	 * @param Passenger
	 */
	public boolean addPassenger(Passenger p)
	{
		// If the destination is locked. Do not create.
		if(lockedOutFloors[p.getDestFloor()] || lockedOutFloors[p.getInitFloor()])
			return false;
		
		ArrayList<LinkedList<Passenger>> floors;
		int theFloor = p.getInitFloor();
		if (p.getDirection().equals("UP"))
			floors = floorsUp;
		else
			floors = floorsDown;

		try {
			requestSemaphore.acquire();
			floorSemaphore.acquire();
			if (floors.get(theFloor).isEmpty()){
				requests.add(new Request(theFloor, p.getDirection()));
				try{
					if(!myScheduler.isInterrupted()){
						myScheduler.interrupt(); 
					//	System.out.print("Notify Scheduler;\t");
					}
				}catch(Exception e){System.out.println("Notify Failed");}
			}
			floors.get(theFloor).add(p);
			floorSemaphore.release();			
			requestSemaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace(); releaseSemaphore();
		}
		return true;
	}
	
	public int presentSetting(String setting){
		return 0;
	}
	
	/**
	 * Return whether a floor current has a UP/DOWN/BOTH/NONE request
	 * @param floorNumber
	 * @return
	 */
	public String getFloorRequest(int floorNumber){
		if(floorsUp.get(floorNumber).isEmpty() && floorsDown.get(floorNumber).isEmpty())
			return "NONE";
		else if(!floorsUp.get(floorNumber).isEmpty() && floorsDown.get(floorNumber).isEmpty())
			return "UP";
		else if(floorsUp.get(floorNumber).isEmpty() && !floorsDown.get(floorNumber).isEmpty())
			return "DOWN";
		else
			return "BOTH";
	}
	
	/**
	 * Get passengers of a certain floor going up
	 * @param floorNumber
	 * @return
	 */
	public LinkedList<Passenger> getPassengerUPList(int floorNumber){
		return floorsUp.get(floorNumber);
	}
	
	/**
	 * Get passengers of a certain floor going up
	 * @param floorNumber
	 * @return
	 */
	public LinkedList<Passenger> getPassengerDOWNList(int floorNumber){
		return floorsDown.get(floorNumber);
	}
	
	public void lockFloor(int f){
		lockedOutFloors[f] = true;
	}
	
	public void unlockFloor(int f){
		lockedOutFloors[f] = false;
	}
	public boolean isLockedOut(int f){
		return lockedOutFloors[f];
	}
	
	public int getNumFloors(){
		return floorsUp.size();
	}
	/**
	 * ================ End Thread ================
	 */
	
	public void endThread(){
		endThread = true;
	}
	
	/**
	 * ================ Initialize internal scheduler ================
	 */
	public void initScheduler(Scheduler s){
		myScheduler = s;
	}
	
	/**
	 * ================ Release all semaphore ================
	 */
	private void releaseSemaphore(){
		requestSemaphore.release();
		floorSemaphore.release();
	}
	
	/**
	 * ================ probability Function ================
	 */
	public void useNormalDistribution(boolean initial, boolean destination){
		normalDistribute_initial = initial;
		normalDistribute_destination = destination;
	}
	public boolean setMeanAndSDInitONLY(double mean, double sd){
		if(mean >= 0 && mean < floorsUp.size()-1 && !(mean==meansd[2] && (int)(sd)==0)){
			nm_init = new Normal(mean, sd, re);  meansd[0] = mean; meansd[1] = sd;
			return true;
		}
		else return false;
	}
	public boolean setMeanAndSDDestONLY(double mean, double sd){
		if(mean >= 0 && mean < floorsUp.size()-1 && !(mean==meansd[0] && (int)(sd)==0)){
			nm_dest = new Normal(mean, sd, re); meansd[2] = mean; meansd[3] = sd;
			return true;
		}
		else return false;
	}
	public void usePresent(String present){
		if(present.equals("morning")){
			normalDistribute_initial = true;
			setMeanAndSDInitONLY(0.0, 0.0);
			normalDistribute_destination = false;
		}
		else if(present.equals("afternoon")){
			normalDistribute_initial = false;
			normalDistribute_destination = false;
		}
		else if(present.equals("night")){
			normalDistribute_initial = false;
			normalDistribute_destination = true;
			setMeanAndSDDestONLY(0.0, 0.0);
		}
	}
	public void setEvent(String event){
		
		try {
			requestSemaphore.acquire();
			floorSemaphore.acquire();
			System.out.println(event);
			if(currentEvent.equals("None")){
				if(event.equals("Fire")){
					currentEvent = "Fire";
					requests.removeAll(requests);
					for(int i=0; i<floorsUp.size(); i++){
						floorsUp.get(i).removeAll(floorsUp.get(i));
						floorsDown.get(i).removeAll(floorsDown.get(i));
					}
					generateRequest = false;
				}
				else if(event.equals("Lockdown")){
					currentEvent = "Lockdown";
					requests.removeAll(requests);
					for(int i=0; i<floorsUp.size(); i++){
						floorsUp.get(i).removeAll(floorsUp.get(i));
						floorsDown.get(i).removeAll(floorsDown.get(i));
					}
					generateRequest = false;
				}
				else if(event.equals("Zombie")){
					currentEvent = "Zombie";
					floorsUp.get(0).removeAll(floorsUp.get(0));
					floorsDown.get(0).removeAll(floorsDown.get(0));
				}
				else if(event.equals("Evacuation")){
					currentEvent = "Evacuation";
				}
			}	
			else{
				if(event.equals("None")){
					currentEvent = "None";
					generateRequest = true;
				}
			}
			System.out.println(currentEvent);
			requestSemaphore.release();
			floorSemaphore.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
			requestSemaphore.release();
			floorSemaphore.release();
		}
	}
	public String getCurrentEvent(){
		return currentEvent;
	}
	private int getNormalizedExternalRequest_init(){
		int returnValue = -1;
		while(returnValue < 0 || returnValue > floorsUp.size()-1){
			returnValue = (int)Math.round(nm_init.nextDouble());
		}
		return returnValue;
	}
	private int getNormalizedExternalRequest_dest(){
		int returnValue = -1;
		while(returnValue < 0 || returnValue > floorsUp.size()-1){
			returnValue = (int)Math.round(nm_dest.nextDouble());
		}
		return returnValue;
	}
	
	public void setManual(){
		manual = true;
	}
	
	public long getWaitTimePassengerOnFloor(int floor){
		long Up = 0;
		long Down = 0;
		if(floorsUp.get(floor).size() != 0){
			Up = sysClock.getTimeMillis()-floorsUp.get(floor).get(0).getTimeOfCreation();
		}
		if(floorsDown.get(floor).size() != 0){
			Down = sysClock.getTimeMillis()-floorsDown.get(floor).get(0).getTimeOfCreation();
		}
		return (Up>Down?Up:Down);
	}
}
