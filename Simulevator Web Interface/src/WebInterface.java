//new
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WebInterface.java
 *
 * Created on Nov 13, 2011, 7:24:08 PM
 */
/**
 *
 * @author Peter
 */
public class WebInterface extends javax.swing.JApplet {

//panel variables    
    //command and statistics panel variables
    private boolean firstConnect = false;
    private String simStatus;
    public String time;
    public String algorithm;
    public String passengerWaitTimeAverage;
    //Table Arrays    
    public String elevatorList[];
    public ArrayList<String> elevatorDistance;
    public ArrayList<String> elevatorUptime;
    public ArrayList<String> elevatorStatus;
    public ArrayList<String> elevatorPassenger;
    public String elevator[][];
    public String floorList[];
    public ArrayList<String> floorStatus;
    public ArrayList<String> floorPassenger;
    public ArrayList<String> floorWaitTime;
    public String lockElevatorFloorList[];
    public String floor[][];
    //command panel variables    
    public String faultList[];
    public String algorithmList[];
    //connection pannel variables;
    public String connectionStatus = "NOT CONNECTED";
    public int connectionStatusColor[] = {255, 0, 0};
//username passwords
    private HashMap administrators;
    private HashMap viewers;
    private String username = "ADMIN";
    private String password;
    private String adminLevel = "ADMIN";
//access type:
    //2=access denied
    //1=viewer
    //0=administrator
    private int access = 2;
//Networking variables
    private boolean connection = false;
    private String sentence;
    private String reSentence;
    private Socket outSocket;
    private int connected = 0;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private int port = 1723;
    private String hostname = "localhost";
//GUI Timer
    private javax.swing.Timer guiTimer;
    private ActionListener guiUpdateListener;
    private int guiTimerDelay = 200;
    //Server 
    private ServerSocket listenSocket;
    private int listenPort = 6789;
    private Thread serverThread;
    private DataServer server;

    /** Initializes the applet WebInterface */
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WebInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WebInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WebInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WebInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //initialize panel
        try {
            listenSocket = new ServerSocket(listenPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initCommandPanel();
        initTableArrays();

        //initTimer();
            /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    initComponents();
                    commandPanel.setVisible(false);
                    connectionPanel.setVisible(false);
                    actionLogScrollPane.setVisible(false);
                    statisticsPanel.setVisible(false);


                    //authenticate();
                    //hideCommandPanel();


                    //initConnection();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

    private void startDataServer() {
        server = new DataServer(6789, listenSocket);
        serverThread = new Thread(server);
        serverThread.start();


    }

    private void initConnection() { 
        System.out.println("in");
JFrame frame = new JFrame("Connection");
System.out.println("out");
        try {
             

             hostname = JOptionPane.showInputDialog("Enter hostname(IP)");
             //port = Integer.parseInt(JOptionPane.showInputDialog("Enter port"));
            //hostname = "5.74.115.147";
            //hostname = "128.189.85.34";
            port = 1723;

            //create connection and send init message
            try {
                outSocket = new Socket(hostname, port);
                outToServer = new DataOutputStream(outSocket.getOutputStream());
                outToServer.writeBytes("I/" + username + "/" + access + "\n");
                inFromServer = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
                String reply = inFromServer.readLine();

                outSocket.close();
                //check reply for algorithm
                if (reply.contains("I/A")) {
                    connection = true;
                    String[] replySplit = reply.split("/");
                    algorithmList = new String[replySplit.length - 2];
                    for (int i = 0; i < replySplit.length - 2; i++) {
                        algorithmList[i] = replySplit[i + 2];
                    }

                    modifyAlgorithmComboBox.setModel(new javax.swing.DefaultComboBoxModel(algorithmList));

                    //JOptionPane.showMessageDialog(frame, "Connected to Server", "Connection", JOptionPane.WARNING_MESSAGE);
                    actionLog.append("Connection Available Waiting For Incoming Data.\n");
                    firstConnect = true;
                    
                } else {
                    connection = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Cannot Connect.", "Connection", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Invalid hostname or port was inputed.", "Connection", JOptionPane.ERROR_MESSAGE);
            connection = false;
        }
        if (connection) {
            startDataServer();
            startGuiTimer();
            if(server.connected){
            actionLog.append("Connected to server.");
        }
        }
        
        



    }

    private void sendCommand(String cmd) {
        try {
            outSocket = new Socket(hostname, port);
            outToServer = new DataOutputStream(outSocket.getOutputStream());
            outToServer.writeBytes(cmd);
            inFromServer = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
            String reply = inFromServer.readLine();

            outSocket.close();

            if (reply.contains("C/OK")) {
                connection = true;
                actionLog.append("C:sent.\n");
            } else {
                actionLog.append("C:NO ADMIN AUTH,\n"
                        + "ONE ADMIN ALREADY ON");
            }
        } catch (Exception e) {
            System.out.println("Error Sending");
            actionLog.append("C:not sent.\n");
            connection = false;
        }


    }

    private void authenticate() {
        //initializing hashmaps username and password combos
        administrators = new HashMap();
        viewers = new HashMap();
        //setting usernames and passwords
        administrators.put(new String("admin"), "admin");
        administrators.put(new String("kobe"), "beef");
        administrators.put(new String("bob"), "builder");
        viewers.put(new String("view"), "view");



        JFrame frame = new JFrame("Authentication");
        int tries = 1;

        while (access != 0 || access != 1) {
            if (tries > 3) {
                break;
            }
            access = 2;
            //prompt authentication
            username = JOptionPane.showInputDialog("Enter username");
            password = JOptionPane.showInputDialog("Enter password");
            //checking access
            if (administrators.containsKey(username)) {
                if ((password.compareTo((String) administrators.get(username)) == 0)) {
                    access = 0;
                }
            } else {
                if (viewers.containsKey(username)) {
                    if ((password.compareTo((String) viewers.get(username)) == 0)) {
                        access = 1;
                    }
                }
            }
           
            switch (access) {

                case 0:
                    JOptionPane.showMessageDialog(frame, "Access Granted: Administrator.", "Authenticated", JOptionPane.WARNING_MESSAGE);
                    actionLog.append("AUTH: Logged as " + username + ".\n");
                    break;
                case 1:
                    JOptionPane.showMessageDialog(frame, "Access Granted: Viewer.", "Authenticated", JOptionPane.WARNING_MESSAGE);
                    actionLog.append("AUTH: Logged as " + username + ".\n");
                    break;
                case 2:
                    String deny = "Access Denied: Incorrect Username Or Password";
                    JOptionPane.showMessageDialog(frame, deny, "Authentication Error", JOptionPane.ERROR_MESSAGE);

                default:
            }

            if (access != 2) {
                break;
            }


            tries++;

        }
        if (access == 0) {
            commandPanel.setVisible(true);
            connectionPanel.setVisible(true);
            actionLogScrollPane.setVisible(true);
            statisticsPanel.setVisible(true);
        } else {
            if (access == 1) {
                commandPanel.setVisible(false);
                connectionPanel.setVisible(true);
                actionLogScrollPane.setVisible(true);
                statisticsPanel.setVisible(true);

            } else {
                commandPanel.setVisible(false);
                connectionPanel.setVisible(false);
                actionLogScrollPane.setVisible(false);
                statisticsPanel.setVisible(false);
            }
        }

    }

    private void initCommandPanel() {
        faultList = new String[]{"Earthquake", "Fire", "Power Outage",
            "Weight Error - Bias", "Weight Error - Random", "Weight Error - Constant",
            "Speed Error - Bias", "Speed Error - Random", "Speed Error - Constant",
            "Position Error - Bias", "Position Error - Random", "Position Error - Constant",
            "Door Stuck", "Door Erratic", "Zombie Attack", "Emergency Evacuation"};

        algorithmList = new String[]{};
        elevatorList = new String[0];
//        for (int i = 0; i < elevatorList.length; i++) {
//            int j = i + 1;
//            elevatorList[i] = "" + j;
//        }
        floorList = new String[0];
//        for (int i = 0; i < floorList.length; i++) {
//            int j = i;
//            floorList[i] = "" + j;
//        }



    }

    private void initGlobalVariable() {
            if(elevatorList== null||floorList==null){
                elevatorList= new String[]{};
                floorList= new String[]{};
            }
                    
        if (!firstConnect) {
            faultList = new String[]{"Earthquake", "Fire", "Power Outage",
                "Weight Error - Bias", "Weight Error - Random", "Weight Error - Constant",
                "Speed Error - Bias", "Speed Error - Random", "Speed Error - Constant",
                "Position Error - Bias", "Position Error - Random", "Position Error - Constant",
                "Door Stuck", "Door Erratic", "Zombie Attack", "Emergency Evacuation"};

            
            elevatorList = new String[Integer.parseInt(server.elevatorNumber)];
            for (int i = 0; i < elevatorList.length; i++) {
                int j = i + 1;
                elevatorList[i] = "" + j;
            }
            floorList = new String[Integer.parseInt(server.floorNumber)];
            for (int i = 0; i < floorList.length; i++) {
                int j = i;
                floorList[i] = "" + j;
            }

            requestSrcComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));
            requestDestComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));
            lockUnlockFloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));
            resumeElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
                    faultInjectionElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
            firstConnect = true;
        }
        if(server !=null && server.elevatorNumber != null && server.floorNumber != null){
            if ( (elevatorList.length != Integer.parseInt(server.elevatorNumber)) || (floorList.length != Integer.parseInt(server.floorNumber))) {
                            System.out.println("Reinit");
            faultList = new String[]{"Earthquake", "Fire", "Power Outage",
                "Weight Error - Bias", "Weight Error - Random", "Weight Error - Constant",
                "Speed Error - Bias", "Speed Error - Random", "Speed Error - Constant",
                "Position Error - Bias", "Position Error - Random", "Position Error - Constant",
                "Door Stuck", "Door Erratic", "Zombie Attack", "Emergency Evacuation"};
            elevatorList = new String[Integer.parseInt(server.elevatorNumber)];
            for (int i = 0; i < elevatorList.length; i++) {
                int j = i + 1;
                elevatorList[i] = "" + j;
            }
            floorList = new String[Integer.parseInt(server.floorNumber)];
            for (int i = 0; i < floorList.length; i++) {
                int j = i;
                floorList[i] = "" + j;
            }

            requestSrcComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));
            requestDestComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));
            lockUnlockFloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));
            resumeElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
        faultInjectionElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
            firstConnect = true;
                
                }
        elevatorList = new String[Integer.parseInt(server.elevatorNumber)];
        for (int i = 0; i < elevatorList.length; i++) {
            int j = i + 1;
            elevatorList[i] = "" + j;
        }
        floorList = new String[Integer.parseInt(server.floorNumber)];
        for (int i = 0; i < floorList.length; i++) {
            int j = i;
            floorList[i] = "" + j;
        }
        }
        this.simStatus = server.status;
        this.time = server.time;
        this.algorithm = server.algorithm;
        this.passengerWaitTimeAverage = server.passengerWaitTimeAverage;



        this.elevatorDistance = server.elevatorDistance;
        this.elevatorUptime = server.elevatorUptime;
        this.elevatorStatus = server.elevatorStatus;
        this.elevatorPassenger = server.elevatorPassenger;


        this.floorStatus = server.floorStatus;
        this.floorPassenger = server.floorPassenger;
        this.floorWaitTime = server.floorWaitTime;

        // System.out.println(server.floorPassenger.toString());

    }

    private void initTableArrays() {
        elevator = new String[elevatorList.length][5];

        for (int i = 0; i < elevatorList.length; i++) {
            for (int j = 0; j < 5; j++) {
                switch (j) {
                    case 0:
                        elevator[i][j] = elevatorList[i];
                        break;
                    case 1:
                        elevator[i][j] = elevatorStatus.get(i);
                        break;
                    case 2:
                        elevator[i][j] = elevatorPassenger.get(i);
                        break;
                    case 3:
                        elevator[i][j] = elevatorUptime.get(i);
                        break;
                    case 4:
                        elevator[i][j] = elevatorDistance.get(i);
                        break;
                }
                //System.out.println(elevator[j][i]);
            }
        }

        floor = new String[floorList.length][4];

        for (int i = 0; i < floorList.length; i++) {
            for (int j = 0; j < 4; j++) {
                switch (j) {
                    case 0:
                        floor[i][j] = floorList[i];
                        break;
                    case 1:
                        floor[i][j] = floorStatus.get(i);
                        break;
                    case 2:
                        floor[i][j] = floorPassenger.get(i);
                        break;
                    case 3:
                        floor[i][j] = floorWaitTime.get(i);
                        break;
                }
                //System.out.println(elevator[j][i]);
            }
        }



    }

    /** Periodically update the display of object heap size.  */
    
    private void startGuiTimer() {
        guiUpdateListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!server.connected) {
                    connection = false;
                } else {
                    connection = true;
                }
                updateGui();


            }
        };
        guiTimer = new javax.swing.Timer(guiTimerDelay, guiUpdateListener);
        guiTimer.start();
 
    }

    /**
     * Must be called when the About Box is closed - otherwise the timer will continue 
     * to operate.
     */
    private void stopGuiTimer() {
        System.err.append("Stopping timer...");
        guiTimer.stop(); //stops notifying registered listeners
        guiTimer.removeActionListener(guiUpdateListener); //removes the one registered listener
        guiUpdateListener = null;
        guiTimer = null;
    }

    private void updateGui() {
        // System.out.println("updating");

        updateConnectionStatus();
        try {
            if (connection) {
                //System.out.println("updating tables");
                initGlobalVariable();
                initTableArrays();
            } else {

                initCommandPanel();
                initTableArrays();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        elevatorStatisticsTable.setModel(new javax.swing.table.DefaultTableModel(elevator,
                new String[]{"Elevator", "Floor", "Passenger", "Uptime", "Distance"}));
        floorStatisticsTable.setModel(new javax.swing.table.DefaultTableModel(floor,
                new String[]{"Floor", "Status", "Passenger", "Wait Time"}));


        simulationStatusLabel.setText("Simulation Status: " + simStatus);



        algorithmLabel.setText("Algorithm: " + algorithm);
        timeLabel.setText("Time: " + time);
        passengerWaitTimeAvgLabel.setText("Passenger Wait Time Avg: " + passengerWaitTimeAverage);


    }

    public void updateConnectionStatus() {
        // System.out.println("yay");
        if (connection) {
            connectionStatus = "CONNECTED";
            connectionStatusColor = new int[]{0, 204, 0};
            // actionLog.append("CONNECTION: Connected to " + hostname + ".\n");


        } else {
            connectionStatus = "NOT CONNECTED";
            connectionStatusColor = new int[]{255, 0, 0};
            // actionLog.append("CONNECTION: Did not connect to: " + hostname + ".\n");
        }
        connectionLabel.setText(connectionStatus);
        connectionLabel.setForeground(new java.awt.Color(connectionStatusColor[0],
                connectionStatusColor[1],
                connectionStatusColor[2]));
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        commandPanel = new javax.swing.JPanel();
        faultInjectionLabel = new javax.swing.JLabel();
        faultInjectionComboBox = new javax.swing.JComboBox();
        faultInjectionButton = new javax.swing.JButton();
        modifyAlgorithmLabel = new javax.swing.JLabel();
        modifyAlgorithmComboBox = new javax.swing.JComboBox();
        requestGeneratorLabel = new javax.swing.JLabel();
        modifyAlgorithmButton = new javax.swing.JButton();
        requestSrcComboBox = new javax.swing.JComboBox();
        lockUnlockFloorLabel = new javax.swing.JLabel();
        lockUnlockFloorComboBox = new javax.swing.JComboBox();
        requestGenButton = new javax.swing.JButton();
        unlockFloorButton = new javax.swing.JButton();
        faultInjectionElevatorComboBox = new javax.swing.JComboBox();
        requestDestComboBox = new javax.swing.JComboBox();
        resumeSimButton = new javax.swing.JButton();
        simulationControlLabel = new javax.swing.JLabel();
        faultInjectionValueTextField = new javax.swing.JTextField();
        lockFloorButton = new javax.swing.JButton();
        resumeElevatorLabel = new javax.swing.JLabel();
        resumeElevatorComboBox = new javax.swing.JComboBox();
        resumeElevatorButton = new javax.swing.JButton();
        pauseSimButton = new javax.swing.JButton();
        faultInjectionValueLabel = new javax.swing.JLabel();
        requestGenValueLabel = new javax.swing.JLabel();
        connectionPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        hostnameLabel = new javax.swing.JLabel();
        connectionStatusLabel = new javax.swing.JLabel();
        connectionButton = new javax.swing.JButton();
        connectionLabel = new javax.swing.JLabel();
        statisticsPanel = new javax.swing.JPanel();
        elevatorStatisticsScrollPane = new javax.swing.JScrollPane();
        elevatorStatisticsTable = new javax.swing.JTable();
        simulationStatusLabel = new javax.swing.JLabel();
        floorStatisticsScrollPane = new javax.swing.JScrollPane();
        floorStatisticsTable = new javax.swing.JTable();
        algorithmLabel = new javax.swing.JLabel();
        elevatorStatisticsLabel = new javax.swing.JLabel();
        floorStatisticsLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        passengerWaitTimeAvgLabel = new javax.swing.JLabel();
        actionLogScrollPane = new javax.swing.JScrollPane();
        actionLog = new javax.swing.JTextArea();
        authorLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        authenticationMenuItem = new javax.swing.JMenuItem();
        logOutMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        Help = new javax.swing.JMenu();
        userManual = new javax.swing.JMenuItem();

        setBackground(new java.awt.Color(51, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 1024, 600));
        setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        setMaximumSize(new java.awt.Dimension(1024, 600));
        setMinimumSize(new java.awt.Dimension(1024, 600));
        setPreferredSize(new java.awt.Dimension(1024, 600));

        mainPanel.setBackground(new java.awt.Color(51, 51, 51));
        mainPanel.setForeground(new java.awt.Color(51, 51, 51));
        mainPanel.setMaximumSize(new java.awt.Dimension(1024, 600));
        mainPanel.setMinimumSize(new java.awt.Dimension(1024, 600));
        mainPanel.setPreferredSize(new java.awt.Dimension(1024, 600));

        titleLabel.setFont(new java.awt.Font("Arial Black", 0, 52)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("SIMULEVATOR");

        commandPanel.setBackground(new java.awt.Color(51, 51, 51));
        commandPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "COMMAND", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 20), new java.awt.Color(255, 102, 102))); // NOI18N
        commandPanel.setMaximumSize(new java.awt.Dimension(650, 32767));
        commandPanel.setMinimumSize(new java.awt.Dimension(650, 375));

        faultInjectionLabel.setForeground(new java.awt.Color(255, 255, 255));
        faultInjectionLabel.setText("FAULT INJECTION:");

        faultInjectionComboBox.setBackground(new java.awt.Color(255, 255, 255));
        faultInjectionComboBox.setModel(new javax.swing.DefaultComboBoxModel(faultList));
        faultInjectionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                faultInjectionComboBoxActionPerformed(evt);
            }
        });

        faultInjectionButton.setText("Inject");
        faultInjectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                faultInjectionButtonActionPerformed(evt);
            }
        });

        modifyAlgorithmLabel.setForeground(new java.awt.Color(255, 255, 255));
        modifyAlgorithmLabel.setText("MODIFY ALGORITHM:");

        modifyAlgorithmComboBox.setModel(new javax.swing.DefaultComboBoxModel(algorithmList));

        requestGeneratorLabel.setForeground(new java.awt.Color(255, 255, 255));
        requestGeneratorLabel.setText("REQUEST GENERATOR:");

        modifyAlgorithmButton.setText("Modify");
        modifyAlgorithmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyAlgorithmButtonActionPerformed(evt);
            }
        });

        requestSrcComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
        requestSrcComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestSrcComboBoxActionPerformed(evt);
            }
        });

        lockUnlockFloorLabel.setForeground(new java.awt.Color(255, 255, 255));
        lockUnlockFloorLabel.setText("LOCK/UNLOCK FLOOR:");

        lockUnlockFloorComboBox.setModel(new javax.swing.DefaultComboBoxModel(floorList));

        requestGenButton.setText("Generate");
        requestGenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestGenButtonActionPerformed(evt);
            }
        });

        unlockFloorButton.setText("Unlock");
        unlockFloorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unlockFloorButtonActionPerformed(evt);
            }
        });

        faultInjectionElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
        faultInjectionElevatorComboBox.setVisible(false);

        requestDestComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));

        resumeSimButton.setText("Resume");
        resumeSimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeSimButtonActionPerformed(evt);
            }
        });

        simulationControlLabel.setForeground(new java.awt.Color(255, 255, 255));
        simulationControlLabel.setText("SIMULATION CONTROL:");

        faultInjectionValueTextField.setText("write value...");
        faultInjectionValueTextField.setMaximumSize(new java.awt.Dimension(100, 28));
        faultInjectionValueTextField.setMinimumSize(new java.awt.Dimension(100, 28));
        faultInjectionValueTextField.setPreferredSize(new java.awt.Dimension(100, 28));
        faultInjectionValueTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                faultInjectionValueTextFieldActionPerformed(evt);
            }
        });
        faultInjectionValueTextField.setVisible(false);

        lockFloorButton.setText("Lock");
        lockFloorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockFloorButtonActionPerformed(evt);
            }
        });

        resumeElevatorLabel.setForeground(new java.awt.Color(255, 255, 255));
        resumeElevatorLabel.setText("RESUME ELEVATOR OPERATION:");

        resumeElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
        resumeElevatorComboBox.setMaximumSize(new java.awt.Dimension(100, 27));
        resumeElevatorComboBox.setMinimumSize(new java.awt.Dimension(100, 27));
        resumeElevatorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeElevatorComboBoxActionPerformed(evt);
            }
        });

        resumeElevatorButton.setText("Resume");
        resumeElevatorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resumeElevatorButtonMouseClicked(evt);
            }
        });

        pauseSimButton.setText("Pause");
        pauseSimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseSimButtonActionPerformed(evt);
            }
        });

        faultInjectionValueLabel.setForeground(new java.awt.Color(102, 204, 255));
        faultInjectionValueLabel.setText("(fault, value, elevator)");

        requestGenValueLabel.setForeground(new java.awt.Color(102, 204, 255));
        requestGenValueLabel.setText("(Src floor, Dest Floor)");

        org.jdesktop.layout.GroupLayout commandPanelLayout = new org.jdesktop.layout.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, commandPanelLayout.createSequentialGroup()
                        .add(simulationControlLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 273, Short.MAX_VALUE)
                        .add(pauseSimButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(resumeSimButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, commandPanelLayout.createSequentialGroup()
                        .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(modifyAlgorithmLabel)
                            .add(commandPanelLayout.createSequentialGroup()
                                .add(lockUnlockFloorLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 182, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, commandPanelLayout.createSequentialGroup()
                                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(requestSrcComboBox, 0, 177, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, lockUnlockFloorComboBox, 0, 177, Short.MAX_VALUE)
                                    .add(modifyAlgorithmComboBox, 0, 177, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(requestDestComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(88, 88, 88)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(modifyAlgorithmButton)
                            .add(commandPanelLayout.createSequentialGroup()
                                .add(lockFloorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(unlockFloorButton))
                            .add(requestGenButton)))
                    .add(commandPanelLayout.createSequentialGroup()
                        .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(commandPanelLayout.createSequentialGroup()
                                .add(3, 3, 3)
                                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(commandPanelLayout.createSequentialGroup()
                                        .add(faultInjectionLabel)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(faultInjectionValueLabel))
                                    .add(commandPanelLayout.createSequentialGroup()
                                        .add(faultInjectionComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 230, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, resumeElevatorComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, faultInjectionValueTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(18, 18, 18)
                                        .add(faultInjectionElevatorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                            .add(resumeElevatorLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 61, Short.MAX_VALUE)
                        .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(resumeElevatorButton)
                            .add(faultInjectionButton)))
                    .add(commandPanelLayout.createSequentialGroup()
                        .add(requestGeneratorLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(requestGenValueLabel)))
                .addContainerGap())
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(commandPanelLayout.createSequentialGroup()
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(resumeSimButton)
                    .add(simulationControlLabel)
                    .add(pauseSimButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(faultInjectionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(faultInjectionValueLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(faultInjectionComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(faultInjectionValueTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(faultInjectionElevatorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(faultInjectionButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(resumeElevatorLabel)
                    .add(resumeElevatorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(resumeElevatorButton))
                .add(13, 13, 13)
                .add(modifyAlgorithmLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(modifyAlgorithmComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(modifyAlgorithmButton))
                .add(7, 7, 7)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(requestGeneratorLabel)
                    .add(requestGenValueLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(requestSrcComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(requestDestComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(requestGenButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lockUnlockFloorLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lockUnlockFloorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(unlockFloorButton)
                    .add(lockFloorButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        connectionPanel.setBackground(new java.awt.Color(51, 51, 51));
        connectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONNECTION", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 20), new java.awt.Color(255, 255, 51))); // NOI18N
        connectionPanel.setMaximumSize(new java.awt.Dimension(150, 150));
        connectionPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        usernameLabel.setForeground(new java.awt.Color(255, 255, 255));
        usernameLabel.setText("USERNAME: "+username);

        hostnameLabel.setForeground(new java.awt.Color(255, 255, 255));
        hostnameLabel.setText("HOST: "+hostname);

        connectionStatusLabel.setForeground(new java.awt.Color(255, 255, 255));
        connectionStatusLabel.setText("STATUS: ");

        connectionButton.setText("Connect/Reconnect");
        connectionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                connectionButtonMouseClicked(evt);
            }
        });
        connectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });

        connectionLabel.setForeground(new java.awt.Color(connectionStatusColor[0], connectionStatusColor[1], connectionStatusColor[2]));
        connectionLabel.setText(connectionStatus);

        org.jdesktop.layout.GroupLayout connectionPanelLayout = new org.jdesktop.layout.GroupLayout(connectionPanel);
        connectionPanel.setLayout(connectionPanelLayout);
        connectionPanelLayout.setHorizontalGroup(
            connectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(connectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(hostnameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(usernameLabel)
                    .add(connectionButton)
                    .add(connectionPanelLayout.createSequentialGroup()
                        .add(connectionStatusLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(connectionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        connectionPanelLayout.setVerticalGroup(
            connectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(connectionPanelLayout.createSequentialGroup()
                .add(usernameLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(hostnameLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(connectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(connectionStatusLabel)
                    .add(connectionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(connectionButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statisticsPanel.setBackground(new java.awt.Color(51, 51, 51));
        statisticsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "STATISTICS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 20), new java.awt.Color(153, 102, 255))); // NOI18N
        statisticsPanel.setMaximumSize(new java.awt.Dimension(400, 400));
        statisticsPanel.setPreferredSize(new java.awt.Dimension(228, 400));

        elevatorStatisticsTable.setModel(new javax.swing.table.DefaultTableModel(
            elevator,
            new String [] {
                "Elevator", "Status", "Passenger", "Uptime", "Distance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        elevatorStatisticsScrollPane.setViewportView(elevatorStatisticsTable);

        simulationStatusLabel.setForeground(new java.awt.Color(255, 255, 255));
        simulationStatusLabel.setText("Simulation Status:");

        floorStatisticsTable.setModel(new javax.swing.table.DefaultTableModel(
            floor,
            new String [] {
                "Floor", "Status", "Waiting","Wait Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        floorStatisticsScrollPane.setViewportView(floorStatisticsTable);

        algorithmLabel.setForeground(new java.awt.Color(255, 255, 255));
        algorithmLabel.setText("Algorithm:");

        elevatorStatisticsLabel.setForeground(new java.awt.Color(255, 255, 255));
        elevatorStatisticsLabel.setText("ELEVATOR STATISTICS:");

        floorStatisticsLabel.setForeground(new java.awt.Color(255, 255, 255));
        floorStatisticsLabel.setText("FLOOR STATISTICS:");

        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setText("Time:");

        passengerWaitTimeAvgLabel.setForeground(new java.awt.Color(255, 255, 255));
        passengerWaitTimeAvgLabel.setText("Passenger Wait Time Avg:");

        org.jdesktop.layout.GroupLayout statisticsPanelLayout = new org.jdesktop.layout.GroupLayout(statisticsPanel);
        statisticsPanel.setLayout(statisticsPanelLayout);
        statisticsPanelLayout.setHorizontalGroup(
            statisticsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statisticsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statisticsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(simulationStatusLabel)
                    .add(algorithmLabel)
                    .add(timeLabel)
                    .add(passengerWaitTimeAvgLabel)
                    .add(elevatorStatisticsLabel)
                    .add(elevatorStatisticsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .add(floorStatisticsLabel)
                    .add(floorStatisticsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                .addContainerGap())
        );
        statisticsPanelLayout.setVerticalGroup(
            statisticsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statisticsPanelLayout.createSequentialGroup()
                .add(simulationStatusLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(algorithmLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(timeLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(passengerWaitTimeAvgLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(elevatorStatisticsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(elevatorStatisticsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(floorStatisticsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(floorStatisticsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        actionLogScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        actionLog.setBackground(new java.awt.Color(51, 51, 51));
        actionLog.setColumns(20);
        actionLog.setEditable(false);
        actionLog.setForeground(new java.awt.Color(204, 204, 204));
        actionLog.setLineWrap(true);
        actionLog.setRows(5);
        actionLog.setMaximumSize(new java.awt.Dimension(200, 200));
        actionLogScrollPane.setViewportView(actionLog);

        authorLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18));
        authorLabel.setForeground(new java.awt.Color(51, 255, 204));
        authorLabel.setText("by UBC EECE POD 3");

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(connectionPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(actionLogScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 415, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(titleLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(authorLabel))
                    .add(commandPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(statisticsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mainPanelLayout.createSequentialGroup()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(authorLabel)
                            .add(titleLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(connectionPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(actionLogScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 148, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(commandPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(statisticsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE))
                .addContainerGap())
        );

        menuBar.setMaximumSize(new java.awt.Dimension(1024, 100));
        menuBar.setMinimumSize(new java.awt.Dimension(1024, 1));
        menuBar.setPreferredSize(new java.awt.Dimension(1024, 22));

        File.setText("File");

        authenticationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        authenticationMenuItem.setText("Log In/Change User");
        authenticationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authenticationMenuItemActionPerformed(evt);
            }
        });
        File.add(authenticationMenuItem);

        logOutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        logOutMenuItem.setText("Log Out");
        logOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutMenuItemActionPerformed(evt);
            }
        });
        File.add(logOutMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Quit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        File.add(exitMenuItem);

        menuBar.add(File);

        Help.setText("Help");

        userManual.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        userManual.setText("userManual");
        Help.add(userManual);

        menuBar.add(Help);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1024, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(mainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 615, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(mainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void connectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionButtonActionPerformed
// TODO add your handling code here:

}//GEN-LAST:event_connectionButtonActionPerformed

private void modifyAlgorithmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyAlgorithmButtonActionPerformed
// TODO add your handling code here:
    String tempString = "C: Algorithm changed to " + (String) modifyAlgorithmComboBox.getSelectedItem() + ".\n";
    String cmdString = "C/A/" + modifyAlgorithmComboBox.getSelectedItem() + "\n";
    actionLog.append(cmdString);
    sendCommand(cmdString);

}//GEN-LAST:event_modifyAlgorithmButtonActionPerformed

private void connectionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_connectionButtonMouseClicked
// TODO add your handling code here:
    initConnection();
}//GEN-LAST:event_connectionButtonMouseClicked

private void faultInjectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_faultInjectionButtonActionPerformed
// TODO add your handling code here:
    String tempString;
    String cmdString;

    System.out.println(faultInjectionComboBox.getSelectedIndex());
    if ((faultInjectionComboBox.getSelectedIndex() > 2) && (faultInjectionComboBox.getSelectedIndex() < 12)) {
        try {
            
            tempString = "C: " + (String) faultInjectionComboBox.getSelectedItem() + " injected.\n";
            double value = Double.parseDouble(faultInjectionValueTextField.getText());
            cmdString = "C/F/" + faultInjectionComboBox.getSelectedIndex() + "/" + faultInjectionValueTextField.getText() + "/" + (faultInjectionElevatorComboBox.getSelectedIndex() + 1) + "\n";
            actionLog.append(cmdString);
            sendCommand(cmdString);

        } catch (Exception e) {
            e.printStackTrace();
            actionLog.append("C:Invalid value for fault\n");
        }
    } else {
        if (faultInjectionComboBox.getSelectedIndex() >= 12) {
            tempString = "C: " + (String) faultInjectionComboBox.getSelectedItem() + " injected.\n";
            cmdString = "C/F/" + faultInjectionComboBox.getSelectedIndex() + "/" + faultInjectionElevatorComboBox.getSelectedItem() + "\n";
        } else {
            tempString = "C: " + (String) faultInjectionComboBox.getSelectedItem() + " injected.\n";
            cmdString = "C/F/" + faultInjectionComboBox.getSelectedIndex() + "\n";


        }
        actionLog.append(cmdString);
        sendCommand(cmdString);
    }



}//GEN-LAST:event_faultInjectionButtonActionPerformed

private void requestGenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestGenButtonActionPerformed
// TODO add your handling code here:
    String tempString = "C: R. generated for " + (String) requestSrcComboBox.getSelectedItem() + " to " + (String) requestDestComboBox.getSelectedItem()+"\n";
    String cmdString;
    cmdString = "C/R/" + (requestSrcComboBox.getSelectedIndex()) + "/" + (requestDestComboBox.getSelectedIndex()) + "\n";
    if (requestSrcComboBox.getSelectedIndex() == requestDestComboBox.getSelectedIndex()) {
        actionLog.append("C: ERROR same src, dest floor\n");
    } else {
        actionLog.append(tempString);
        sendCommand(cmdString);
    }
}//GEN-LAST:event_requestGenButtonActionPerformed

private void unlockFloorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unlockFloorButtonActionPerformed
// TODO add your handling code here:
    String tempString;
    String cmdString;
    cmdString = "C/E/" + (lockUnlockFloorComboBox.getSelectedIndex()) + "/U\n";

    tempString = "C: Floor " + lockUnlockFloorComboBox.getSelectedItem() + "unlocked.\n";


    actionLog.append(cmdString);
    sendCommand(cmdString);

}//GEN-LAST:event_unlockFloorButtonActionPerformed

private void faultInjectionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_faultInjectionComboBoxActionPerformed
// TODO add your handling code here:
    if ((faultInjectionComboBox.getSelectedIndex() > 2) && (faultInjectionComboBox.getSelectedIndex() < 12)) {
        faultInjectionElevatorComboBox.setVisible(true);
        faultInjectionElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
        faultInjectionValueTextField.setVisible(true);
        faultInjectionValueTextField.setText("");
    } else {
        if (faultInjectionComboBox.getSelectedIndex() >= 12) {
            faultInjectionElevatorComboBox.setVisible(true);
            faultInjectionValueTextField.setVisible(false);
                    faultInjectionElevatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(elevatorList));
        } else {

            faultInjectionElevatorComboBox.setVisible(false);
            faultInjectionValueTextField.setVisible(false);
        }
    }
}//GEN-LAST:event_faultInjectionComboBoxActionPerformed

private void resumeSimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeSimButtonActionPerformed
    String tempString;
    String cmdString;
    cmdString = "C/S/R\n";

    tempString = "C: Sim Resumed.\n";
    actionLog.append(cmdString);
    sendCommand(cmdString);
}//GEN-LAST:event_resumeSimButtonActionPerformed

private void authenticationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authenticationMenuItemActionPerformed
    authenticate();
// TODO add your handling code here:
}//GEN-LAST:event_authenticationMenuItemActionPerformed

private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
// TODO add your handling code here:
    System.exit(0);
}//GEN-LAST:event_exitMenuItemActionPerformed

private void faultInjectionValueTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_faultInjectionValueTextFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_faultInjectionValueTextFieldActionPerformed

private void lockFloorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockFloorButtonActionPerformed
// TODO add your handling code here:
    String tempString;
    String cmdString;
    cmdString = "C/E/" + (lockUnlockFloorComboBox.getSelectedIndex()) + "/L\n";

    tempString = "C: Floor " + lockUnlockFloorComboBox.getSelectedItem() + "locked.\n";


    actionLog.append(cmdString);
    sendCommand(cmdString);
}//GEN-LAST:event_lockFloorButtonActionPerformed

private void pauseSimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseSimButtonActionPerformed
    String tempString;
    String cmdString;
    cmdString = "C/S/P\n";

    tempString = "C: Sim Pause.\n";
    actionLog.append(cmdString);
    sendCommand(cmdString);
}//GEN-LAST:event_pauseSimButtonActionPerformed

private void resumeElevatorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeElevatorComboBoxActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_resumeElevatorComboBoxActionPerformed

private void logOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutMenuItemActionPerformed
    access = 2;
    commandPanel.setVisible(false);
    connectionPanel.setVisible(false);
    actionLogScrollPane.setVisible(false);
    statisticsPanel.setVisible(false);
    actionLog.setText("");
            JFrame frame = new JFrame("Authentication");
                    JOptionPane.showMessageDialog(frame, "Logged Out", "Authenication", JOptionPane.WARNING_MESSAGE);

}//GEN-LAST:event_logOutMenuItemActionPerformed

private void resumeElevatorButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resumeElevatorButtonMouseClicked
    String tempString;
    String cmdString;
    cmdString = "C/C/"+(resumeElevatorComboBox.getSelectedIndex()+1)+"\n";

    tempString = "C: Elevator"+ (resumeElevatorComboBox.getSelectedIndex()+1)+"Resumed.\n";
    actionLog.append(cmdString);
    sendCommand(cmdString);// TODO add your handling code here:
}//GEN-LAST:event_resumeElevatorButtonMouseClicked

private void requestSrcComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestSrcComboBoxActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_requestSrcComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu File;
    private javax.swing.JMenu Help;
    private javax.swing.JTextArea actionLog;
    private javax.swing.JScrollPane actionLogScrollPane;
    private javax.swing.JLabel algorithmLabel;
    private javax.swing.JMenuItem authenticationMenuItem;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JButton connectionButton;
    private javax.swing.JLabel connectionLabel;
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JLabel connectionStatusLabel;
    private javax.swing.JLabel elevatorStatisticsLabel;
    private javax.swing.JScrollPane elevatorStatisticsScrollPane;
    private javax.swing.JTable elevatorStatisticsTable;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JButton faultInjectionButton;
    private javax.swing.JComboBox faultInjectionComboBox;
    private javax.swing.JComboBox faultInjectionElevatorComboBox;
    private javax.swing.JLabel faultInjectionLabel;
    private javax.swing.JLabel faultInjectionValueLabel;
    private javax.swing.JTextField faultInjectionValueTextField;
    private javax.swing.JLabel floorStatisticsLabel;
    private javax.swing.JScrollPane floorStatisticsScrollPane;
    private javax.swing.JTable floorStatisticsTable;
    private javax.swing.JLabel hostnameLabel;
    private javax.swing.JButton lockFloorButton;
    private javax.swing.JComboBox lockUnlockFloorComboBox;
    private javax.swing.JLabel lockUnlockFloorLabel;
    private javax.swing.JMenuItem logOutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton modifyAlgorithmButton;
    private javax.swing.JComboBox modifyAlgorithmComboBox;
    private javax.swing.JLabel modifyAlgorithmLabel;
    private javax.swing.JLabel passengerWaitTimeAvgLabel;
    private javax.swing.JButton pauseSimButton;
    private javax.swing.JComboBox requestDestComboBox;
    private javax.swing.JButton requestGenButton;
    private javax.swing.JLabel requestGenValueLabel;
    private javax.swing.JLabel requestGeneratorLabel;
    private javax.swing.JComboBox requestSrcComboBox;
    private javax.swing.JButton resumeElevatorButton;
    private javax.swing.JComboBox resumeElevatorComboBox;
    private javax.swing.JLabel resumeElevatorLabel;
    private javax.swing.JButton resumeSimButton;
    private javax.swing.JLabel simulationControlLabel;
    private javax.swing.JLabel simulationStatusLabel;
    private javax.swing.JPanel statisticsPanel;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JButton unlockFloorButton;
    private javax.swing.JMenuItem userManual;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
