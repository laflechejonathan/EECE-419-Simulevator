package network;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import simulator.SimulatorController;

/**
 *
 * @author Sameer
 */
public class DataSender extends Thread{

    private Socket socket;
    private String address;
    private int port;
    private DataOutputStream os;   
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    String[] String_data = new String[10];
    private SimulatorController sc;
    private CommandServer myCommander;
    int retryLimit = 5;
    int retryCount = 0;
    String numLines = "0";
    String status = "D/S/S";

    public DataSender(){  
    }

    public DataSender(CommandServer pCS, SimulatorController pSC, int port, String address){
    	this.sc = pSC;
        this.port = port;
        this.address = address;
        this.myCommander = pCS;
    }

    private void send_data() throws Exception{
    	String[] toSend = new String[12];
    	sdf.format(new Date(sc.getSimulationTime()));
        // Time
        toSend[0] = "D/D/"+sdf.format(new Date(sc.getSimulationTime()));
        // Algorithm
        toSend[1] = "D/A/" + sc.getCurrentAlgorithm();
        // Initialize Value
        toSend[2] = "D/I/E/"+sc.getNumElevator()+"/F/"+sc.getNumFloor();
        // Passenger Wait Time Average
        toSend[3] = "D/P/"+sc.getAveragePassengerWaitTime(0);
        // Elevator Data
        StringBuffer cmdb5 = new StringBuffer("D/E/S");
        StringBuffer cmdb6 = new StringBuffer("D/E/P");
        StringBuffer cmdb7 = new StringBuffer("D/E/U");
        StringBuffer cmdb8 = new StringBuffer("D/E/D");
        for(int i=0; i<sc.getNumElevator(); i++){
	        cmdb5.append("/" + Math.round(sc.getElevatorFloor(i)));
	        cmdb6.append("/" + sc.getElevatorNumPassenger(i));
	        cmdb7.append("/" + sc.getElevatorUpTime(i));
	        cmdb8.append("/" + sc.getElevatorDistanceTravelled(i));
        }
        toSend[4] = cmdb5.toString();
        toSend[5] = cmdb6.toString();
        toSend[6] = cmdb7.toString();
        toSend[7] =  cmdb8.toString();
        
        StringBuffer cmdb9 = new StringBuffer("D/F/S");
        StringBuffer cmdb10 = new StringBuffer("D/F/W");
        StringBuffer cmdb11 = new StringBuffer("D/F/P");
        for(int i=0; i<sc.getNumFloor(); i++){
        	if(sc.isLockedOut(i))
        		cmdb9.append("/" + 1);
        	else
        		cmdb9.append("/" + 0);
        	cmdb10.append("/" + sc.getLongestPassengerWaitOnFloor(i));
        	cmdb11.append("/" + (sc.getNumPassengerGoingUP(i)+sc.getNumPassengerGoingDOWN(i)));
        }
        toSend[8] = cmdb9.toString();
        toSend[9] = cmdb10.toString();
        toSend[10] =  cmdb11.toString();
        toSend[11] = status;
	        
	    send(toSend);
    }

    private void encoder(){
    }
    
    public void run() {
      
	  while(retryCount < retryLimit){
		  try {
    		  if(sc!=null){
    			  try{
	    			  encoder();
	    			  send_data();
    			  }
    			  catch (Exception e){
    				  e.printStackTrace();
    			  }
    		  }
    		  sleep(250);
    	  }catch (Exception e) {
        	  e.printStackTrace();
          } 
	  }  
      
      myCommander.removeMe(this);
      // This connection is no longer found
    }
    
    public void initializeSimulatorController(SimulatorController pSC){
    	sc = pSC;
    }
    
    public void simulationEnd(){
    	sc = null;
    }
    
    public String getAddress(){
    	return address;
    }

    synchronized public void sendPause(){
    	send("D/S/P");
    	status = "D/S/P";
    }
	synchronized public void sendResume(){
		send("D/S/R");
		status = "D/S/R";
	}
	synchronized public void sendStop(){
		send("D/S/S");
		status = "D/S/S";
	}
	synchronized public void sendQuit(){
		send("D/S/Q");
		status = "D/S/Q";
	}
	private void send(String S){
	    String[] toSend = new String[1];
	    toSend[0] = S;
	    send(toSend);
	}
	private void send(String[] S){
	    try{     
	        socket = new Socket(address,port);
	        os = new DataOutputStream(socket.getOutputStream());
	        // BufferedReader inFromServer = new BufferedReader (new InputStreamReader(socket.getInputStream()));    
	        
	        // Number of lines sent
	        numLines = S.length + "\n";
	        os.writeBytes(numLines);
	        for(int i=0; i< S.length; i++)
	        	os.writeBytes(S[i]+"\n");
	        os.flush();
	        os.close();
	        socket.close();   
	        retryCount = 0;
        }catch(Exception e){
        	retryCount++;
            System.out.println("Error Sending.\n");
        }
    }
    
//    public static void main(String[] args) throws IOException{
//        
//           DataSender client1 = new DataSender(6789,"5.44.170.49");       
//           Thread thread = new Thread(client1);
//           thread.start();        
//    }
    
}
