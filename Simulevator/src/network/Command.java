package network;

import java.io.*;
import java.net.Socket;

import simulator.SimulatorController;

/**
 *
 * @author Peter
 */

class Command implements Runnable {

    private Socket socket;
    public String responseLine;
    private InputStream is;
    private DataOutputStream os;
    private int port;
    public String requestType;
    private SimulatorController sc;
    private String alList;
    
    // Constructor
    public Command(SimulatorController pSC, String algList, Socket socket, int port) throws Exception {
        this.socket = socket;
        this.port = port;
        this.sc = pSC;
        alList = algList;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {           
            e.printStackTrace();
        }
    }

    private void processRequest() throws Exception {
        // Get a reference to the socket's input and output streams.
        is = socket.getInputStream();
        os = new DataOutputStream(socket.getOutputStream());

        // Set up input stream filters.
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();

        // Display the request line.
        System.out.println(port+ " : "+requestLine);
        
        //Check type of request.
        if(requestLine.contains("I/")){          
            connectionRequest();
        }else{
            commandRequest();
        }

        // Construct the response message.
        responseLine = "";

        // Close streams and socket.
        os.close();
        br.close();
        socket.close();
        
        Decoder.decode(sc, requestLine);
    }

    private void commandRequest(){
        //implement function according to command
 //call function   
        responseLine = "C/OK\n";
        try{
            os.writeBytes(responseLine);
            os.flush();
        }catch(Exception e){
            System.out.println("Error Sending.\n");
        }
        requestType = "C";
    }
    
    private void connectionRequest(){
        responseLine = alList;
        //add connection 
        try{
            os.writeBytes(responseLine);
            os.flush();
        }catch(Exception e){
            System.out.println("Error Sending.\n");
        }
        requestType = "I";
    }
    
    public void simulationEnd(){
    	sc = null;
    }
    public void initializeSimulatorController(SimulatorController pSC){
    	sc = pSC;
    }
}
