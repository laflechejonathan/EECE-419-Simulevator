package network;
import java.io.*;
import java.net.*;
import java.util.*;

import algorithm.Algorithm;

import simulator.SimulatorController;

/**
 *
 * @author Peter
 */
public class CommandServer extends Thread{

    private int port = 1723;
    private ServerSocket listenSocket;
    public static ArrayList<DataSender> client_connected = new ArrayList<DataSender>();
    public static ArrayList<Command> connection_connected = new ArrayList<Command>();  
    public static ArrayList<String> ip_connect = new ArrayList<String>();
    private static String algorithmString;
    private SimulatorController sc;

	public CommandServer(){
		try {
            //establish the listen socket
			listenSocket = new ServerSocket(port);
			System.out.println(port+": Server Established");
	    } catch (IOException e) {           
	        System.out.println("Error creating listening socket");
	    }   
	}
	
	public void createNewClientConnection(int port, String ipAddress){
		// ipAddress = 5.44.170.49
		DataSender client = new DataSender(this, sc, port, ipAddress); 
		client.start();
		client_connected.add(client);
		client.sendResume();
	}
    
    public CommandServer(SimulatorController pSC, int port) {
        
        this.port = port;        
        try {
            //establish the listen socket
            listenSocket = new ServerSocket(port);            
            System.out.println(port+": Server Established");
            sc = pSC;
        } catch (IOException e) {            
            System.out.println("Error creating listening socket");
        }        
    }
    
    public void run(){
        try{            
            while (true) {                
                // Listen for a TCP connection request.
                Socket connectionSocket = this.listenSocket.accept();                                       
                Command conn;
                
                try {
                	conn = new Command(sc, algorithmString, connectionSocket, this.port);
                    conn.run();
                	if(!ip_connect.contains(connectionSocket.getInetAddress().toString().replace("/", ""))){
	                    connection_connected.add(conn);
	                    ip_connect.add(connectionSocket.getInetAddress().toString().replace("/", ""));
	                    createNewClientConnection(6789, connectionSocket.getInetAddress().toString().replace("/", ""));
                	}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }catch(Exception e){
            System.out.println("Cannot make server thread");
        }
        try {
			listenSocket.close();
			System.out.println("Connection Closed");
			simulationEnd();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void initSimulatorController(SimulatorController pSC){
    	for(Command cc: connection_connected)
    		cc.initializeSimulatorController(pSC);
    	for(DataSender ds: client_connected)
    		ds.initializeSimulatorController(pSC);
    	sc = pSC;
    }
    public void simulationEnd(){
    	for(Command cc: connection_connected)
    		cc.simulationEnd();
    	for(DataSender ds: client_connected)
    		ds.simulationEnd();
    	sc = null;
    }
    public void removeMe(DataSender pDS){
    	ip_connect.remove(pDS.getAddress());
    	client_connected.remove(pDS);
    }
    public void initAlgorithm(ArrayList<Algorithm> alg){
    	StringBuffer sb = new StringBuffer("I/A");
    	for(int i=0; i<alg.size(); i++)
    		sb.append("/" + alg.get(i).name());
    	sb.append("\n");
    	algorithmString = sb.toString();
    }
    public void sendPause(){
    	for(DataSender ds: client_connected){
    		ds.sendPause();
    	}
    }
    public void sendResume(){
    	for(DataSender ds: client_connected){
    		ds.sendResume();
    	}
    }
    public void sendStop(){
    	for(DataSender ds: client_connected){
    		ds.sendStop();
    	}
    }
    public void sendQuit(){
    	for(DataSender ds: client_connected){
    		ds.sendQuit();
    	}
    }
    public ArrayList<String> getConnectedUsers(){
    	return ip_connect;
    }
    public String getIP(){
    	try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "UnRetrievable";
		}
    }
}
