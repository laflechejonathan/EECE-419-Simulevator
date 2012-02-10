package driver;

import gui.GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.UIManager;

import network.CommandServer;

import clock.Clock;
import algorithm.*;
import display.*;
import simulator.SimulatorController;
import statistics.*;

// BY JACKIE CHEN
// DATE: OCT. 26, 2011

@SuppressWarnings("serial")
public class Simulevator extends javax.swing.JFrame {

	public Simulevator() {
		initComponents();
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Simulevator by EECE 419 - POD 3");
		this.setLocation(170, 100);
		SimulationSettingsPane.setVisible(false);
		SimulationPane.setVisible(false);

		/* MEOW */// ALGORITHMs ARE ADDED HERE
		AL = new ArrayList<Algorithm>();
		AL.add(new ClosestFirst("Closest First"));
		AL.add(new Stochastic("Stochastic"));
		AL.add(new SmallestQueueFirst("Smallest Queue First"));
		AL.add(new ContextDependent("Context Dependent"));
		AL.add(new ShortestPath("Shortest Path First"));
		AL.add(new SmallestDistanceFirst("Smallest Dist First"));
		AL.add(new SmallestWeightFirst("Smallest Weight First"));
		commandServer.initAlgorithm(AL);
		commandServer.start();
		myIP = commandServer.getIP();
	}

	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		PlotStatisticsFileChooser = new javax.swing.JFileChooser();
		MakeRequestButton = new javax.swing.JButton();
		FileInputFileChooser = new javax.swing.JFileChooser();
		FileOutputFileChooser = new javax.swing.JFileChooser();

		MainMenuPane = new javax.swing.JDesktopPane();
		jLabel1 = new javax.swing.JLabel();
		ExitButton = new javax.swing.JButton();
		PlotStatisticsButton = new javax.swing.JButton();
		StartSimulationButton = new javax.swing.JButton();
		MainMenuLayeredPane = new javax.swing.JLayeredPane();
		SimulationSettingsPane = new javax.swing.JDesktopPane();
		ContinueButton = new javax.swing.JButton();
		ReturnToMenuButton = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		AlgorithmButton = new javax.swing.JButton();
		FileOutputButton = new javax.swing.JButton();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();

		FloorSettingsField = new javax.swing.JTextField();
		ElevatorSpeedSettingsField = new javax.swing.JTextField();
		MaxWeightField = new javax.swing.JTextField();
		ElevatorOutOfServiceField = new javax.swing.JTextField();
		ElevatorSettingsField = new javax.swing.JSpinner();
		ManualRadioButton = new javax.swing.JRadioButton();
		PresetRadioButton = new javax.swing.JRadioButton();
		RandomRadioButton = new javax.swing.JRadioButton();
		jLabel14 = new javax.swing.JLabel();
		SimulationPane = new javax.swing.JDesktopPane();
		jLabel15 = new javax.swing.JLabel();
		TakeOutOfServiceButton = new javax.swing.JButton();
		ResumeOperationButton = new javax.swing.JButton();
		GoFasterButton = new javax.swing.JButton();
		SlowDownButton = new javax.swing.JButton();
		LockoutButton = new javax.swing.JButton();
		UnlockButton = new javax.swing.JButton();
		InjectFaultButton = new javax.swing.JButton();
		ChangeAlgorithmButton = new javax.swing.JButton();
		MakeRequestButton = new javax.swing.JButton();
		DisplayStatisticsButton = new javax.swing.JButton();
		ResumePauseButton = new javax.swing.JButton();
		FinishSimulationButton = new javax.swing.JButton();
		SimulationLayeredPane = new javax.swing.JLayeredPane();
		SimulationScrollBar = new javax.swing.JScrollBar();
		MonitorButton = new javax.swing.JButton();
		ElevatorFasterButton = new javax.swing.JButton();
		ElevatorSlowerButton = new javax.swing.JButton();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				commandServer.sendQuit();
				commandServer.interrupt();
				System.exit(0);
			}
		});
		// setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36)); // NOI18N
		jLabel1.setForeground(new java.awt.Color(255, 237, 0));
		jLabel1.setText("SIMULEVATOR");
		jLabel1.setBorder(new javax.swing.border.MatteBorder(null));
		jLabel1.setBounds(360, 10, 330, 50);
		MainMenuPane.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

		ExitButton.setBackground(new java.awt.Color(102, 0, 0));
		ExitButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		ExitButton.setForeground(new java.awt.Color(204, 204, 0));
		ExitButton.setText("Exit");
		ExitButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		ExitButton.setFocusable(false);
		ExitButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ExitButtonMouseClicked(evt);
			}
		});
		ExitButton.setBounds(920, 650, 140, 25);
		MainMenuPane.add(ExitButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

		PlotStatisticsButton.setBackground(new java.awt.Color(102, 0, 0));
		PlotStatisticsButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14));
		PlotStatisticsButton.setForeground(new java.awt.Color(204, 204, 0));
		PlotStatisticsButton.setText("Plot Statistics");
		PlotStatisticsButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		PlotStatisticsButton.setFocusable(false);
		PlotStatisticsButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						PlotStatisticsButtonMouseClicked(evt);
					}
				});
		PlotStatisticsButton.setBounds(920, 600, 140, 25);
		MainMenuPane.add(PlotStatisticsButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		StartSimulationButton.setBackground(new java.awt.Color(102, 0, 0));
		StartSimulationButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		StartSimulationButton.setForeground(new java.awt.Color(204, 204, 0));
		StartSimulationButton.setText("Start Simulation");
		StartSimulationButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		StartSimulationButton.setFocusable(false);
		StartSimulationButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						StartSimulationButtonMouseClicked(evt);
					}
				});

		StartSimulationButton.setBounds(920, 550, 140, 25);
		MainMenuPane.add(StartSimulationButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		MainMenuLayeredPane.setBackground(new java.awt.Color(255, 255, 255));
		MainMenuLayeredPane.setBounds(10, 60, 900, 620);
		MainMenuLayeredPane.setBorder(javax.swing.BorderFactory
				.createCompoundBorder());
		MainMenuLayeredPane.setForeground(new java.awt.Color(255, 255, 255));
		MainMenuPane.add(MainMenuLayeredPane,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ContinueButton.setBackground(new java.awt.Color(102, 0, 0));
		ContinueButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
		ContinueButton.setForeground(new java.awt.Color(204, 204, 0));
		ContinueButton.setText("Continue");
		ContinueButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		ContinueButton.setFocusable(false);
		ContinueButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ContinueButtonMouseClicked(evt);
			}
		});

		ContinueButton.setBounds(920, 610, 140, 25);
		SimulationSettingsPane.add(ContinueButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ReturnToMenuButton.setBackground(new java.awt.Color(102, 0, 0));
		ReturnToMenuButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
		ReturnToMenuButton.setForeground(new java.awt.Color(204, 204, 0));
		ReturnToMenuButton.setText("Return to Menu");
		ReturnToMenuButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ReturnToMenuButton.setFocusable(false);
		ReturnToMenuButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ReturnToMenuButtonMouseClicked(evt);
			}
		});
		ReturnToMenuButton.setBounds(920, 650, 140, 25);
		SimulationSettingsPane.add(ReturnToMenuButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel2.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 24));
		jLabel2.setForeground(new java.awt.Color(255, 237, 45));
		jLabel2.setText("Settings");
		jLabel2.setBorder(new javax.swing.border.MatteBorder(null));
		jLabel2.setBounds(450, 50, 122, 50);
		SimulationSettingsPane.add(jLabel2,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		AlgorithmButton.setBackground(new java.awt.Color(102, 0, 0));
		AlgorithmButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		AlgorithmButton.setForeground(new java.awt.Color(204, 204, 0));
		AlgorithmButton.setText("Algorithm");
		AlgorithmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		AlgorithmButton.setFocusable(false);
		AlgorithmButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				AlgorithmButtonMouseClicked(evt);
			}
		});

		AlgorithmButton.setBounds(920, 530, 140, 25);
		SimulationSettingsPane.add(AlgorithmButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		FileOutputButton.setBackground(new java.awt.Color(102, 0, 0));
		FileOutputButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		FileOutputButton.setForeground(new java.awt.Color(204, 204, 0));
		FileOutputButton.setText("File Output");
		FileOutputButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		FileOutputButton.setFocusable(false);
		FileOutputButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				FileOutputButtonMouseClicked(evt);
			}
		});

		FileOutputButton.setBounds(920, 570, 140, 25);
		SimulationSettingsPane.add(FileOutputButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel4.setBackground(new java.awt.Color(255, 255, 255));
		jLabel4.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
		jLabel4.setForeground(new java.awt.Color(255, 255, 255));
		jLabel4.setText("Number of Elevators:");
		jLabel4.setBounds(250, 170, 180, 20);
		SimulationSettingsPane.add(jLabel4,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel5.setBackground(new java.awt.Color(255, 255, 255));
		jLabel5.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel5.setForeground(new java.awt.Color(255, 255, 255));
		jLabel5.setText("Number of Floors:");
		jLabel5.setBounds(250, 240, 180, 20);
		SimulationSettingsPane.add(jLabel5,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel6.setBackground(new java.awt.Color(255, 255, 255));
		jLabel6.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel6.setForeground(new java.awt.Color(255, 255, 255));
		jLabel6.setText("Max Elevator Speed:");
		jLabel6.setBounds(250, 310, 180, 20);
		SimulationSettingsPane.add(jLabel6,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel8.setBackground(new java.awt.Color(255, 255, 255));
		jLabel8.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel8.setForeground(new java.awt.Color(255, 255, 255));
		jLabel8.setText("Distance Until Elevator");
		jLabel8.setBounds(250, 440, 180, 20);
		SimulationSettingsPane.add(jLabel8,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel11.setBackground(new java.awt.Color(255, 255, 255));
		jLabel11.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel11.setForeground(new java.awt.Color(255, 255, 255));
		jLabel11.setText("Simulation Type:");
		jLabel11.setBounds(260, 600, 180, 20);
		SimulationSettingsPane.add(jLabel11,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel12.setBackground(new java.awt.Color(255, 255, 255));
		jLabel12.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel12.setForeground(new java.awt.Color(255, 255, 255));
		jLabel12.setText("Max Elevator Weight:");
		jLabel12.setBounds(250, 370, 180, 20);
		SimulationSettingsPane.add(jLabel12,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel13.setBackground(new java.awt.Color(255, 255, 255));
		jLabel13.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel13.setForeground(new java.awt.Color(255, 255, 255));
		jLabel13.setText("Out of Service (Floors):");
		jLabel13.setBounds(260, 460, 180, 20);
		SimulationSettingsPane.add(jLabel13,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		FloorSettingsField.setBounds(480, 230, 70, 30);
		SimulationSettingsPane.add(FloorSettingsField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		ElevatorSpeedSettingsField.setBounds(480, 300, 70, 30);
		SimulationSettingsPane.add(ElevatorSpeedSettingsField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		MaxWeightField.setBounds(480, 370, 70, 30);
		SimulationSettingsPane.add(MaxWeightField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		ElevatorOutOfServiceField.setBounds(480, 440, 70, 30);
		SimulationSettingsPane.add(ElevatorOutOfServiceField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ElevatorSettingsField.setFont(new java.awt.Font("Palatino Linotype", 0,
				12));
		ElevatorSettingsField.setModel(new javax.swing.SpinnerNumberModel(1, 1,
				10, 1));
		ElevatorSettingsField.setBounds(480, 170, 90, 24);
		JFormattedTextField ElevatorSettingsFieldUnEditable = ((JSpinner.DefaultEditor) ElevatorSettingsField
				.getEditor()).getTextField();
		ElevatorSettingsFieldUnEditable.setEditable(false);
		SimulationSettingsPane.add(ElevatorSettingsField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ManualRadioButton.setBackground(new java.awt.Color(0, 0, 0));
		buttonGroup1.add(ManualRadioButton);
		ManualRadioButton.setForeground(new java.awt.Color(255, 255, 255));
		ManualRadioButton.setText("Manual");

		ManualRadioButton.setBounds(480, 600, 120, 23);
		SimulationSettingsPane.add(ManualRadioButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		PresetRadioButton.setBackground(new java.awt.Color(0, 0, 0));
		buttonGroup1.add(PresetRadioButton);
		PresetRadioButton.setForeground(new java.awt.Color(255, 255, 255));
		PresetRadioButton.setText("Preset");
		PresetRadioButton.setBounds(600, 600, 90, 23);
		PresetRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JRadioButton rb = (JRadioButton) evt.getSource();
				if (rb.isSelected()) {
					int FileInputReturnValue = FileInputFileChooser
							.showOpenDialog(SimulationSettingsPane);
					/* MEOW */
					if (FileInputReturnValue == JFileChooser.APPROVE_OPTION)
						inputFile = FileInputFileChooser.getSelectedFile();
				}
			}
		});
		SimulationSettingsPane.add(PresetRadioButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		RandomRadioButton.setBackground(new java.awt.Color(0, 0, 0));
		buttonGroup1.add(RandomRadioButton);
		RandomRadioButton.setForeground(new java.awt.Color(255, 255, 255));
		RandomRadioButton.setText("Random");
		RandomRadioButton.setBounds(710, 600, 110, 23);
		RandomRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JRadioButton rb = (JRadioButton) evt.getSource();
				if (rb.isSelected()) {
					RandomSettings rs = new RandomSettings();
					rs.setVisible(true);
				}
			}
		});
		SimulationSettingsPane.add(RandomRadioButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel14.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36));
		jLabel14.setForeground(new java.awt.Color(255, 237, 45));
		jLabel14.setText("SIMULEVATOR");
		jLabel14.setBorder(new javax.swing.border.MatteBorder(null));
		jLabel14.setBounds(360, 10, 330, 50);
		SimulationSettingsPane.add(jLabel14,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel15.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36));
		jLabel15.setForeground(new java.awt.Color(255, 237, 0));
		jLabel15.setText("SIMULEVATOR");
		jLabel15.setBorder(new javax.swing.border.MatteBorder(null));
		jLabel15.setBounds(360, 10, 330, 50);
		SimulationPane.add(jLabel15, javax.swing.JLayeredPane.DEFAULT_LAYER);

		TakeOutOfServiceButton.setBackground(new java.awt.Color(102, 0, 0));
		TakeOutOfServiceButton.setFont(new java.awt.Font("Palatino Linotype",
				0, 14)); // NOI18N
		TakeOutOfServiceButton.setForeground(new java.awt.Color(204, 204, 0));
		TakeOutOfServiceButton.setText("Take Out of Service");
		TakeOutOfServiceButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		TakeOutOfServiceButton.setFocusable(false);
		TakeOutOfServiceButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						TakeOutOfServiceButtonMouseClicked(evt);
					}
				});

		TakeOutOfServiceButton.setBounds(920, 160, 140, 25);
		SimulationPane.add(TakeOutOfServiceButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ResumeOperationButton.setBackground(new java.awt.Color(102, 0, 0));
		ResumeOperationButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ResumeOperationButton.setForeground(new java.awt.Color(204, 204, 0));
		ResumeOperationButton.setText("Resume Operation");
		ResumeOperationButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ResumeOperationButton.setFocusable(false);
		ResumeOperationButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ResumeOperationButtonMouseClicked(evt);
					}
				});

		ResumeOperationButton.setBounds(920, 210, 140, 25);
		SimulationPane.add(ResumeOperationButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		LockoutButton.setBackground(new java.awt.Color(102, 0, 0));
		LockoutButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		LockoutButton.setForeground(new java.awt.Color(204, 204, 0));
		LockoutButton.setText("Lockout");
		LockoutButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		LockoutButton.setFocusable(false);
		LockoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				LockoutButtonMouseClicked(evt);
			}
		});

		LockoutButton.setBounds(920, 260, 140, 25);
		SimulationPane.add(LockoutButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		UnlockButton.setBackground(new java.awt.Color(102, 0, 0));
		UnlockButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		UnlockButton.setForeground(new java.awt.Color(204, 204, 0));
		UnlockButton.setText("Unlock");
		UnlockButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		UnlockButton.setFocusable(false);
		UnlockButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				UnlockButtonMouseClicked(evt);
			}
		});

		UnlockButton.setBounds(920, 310, 140, 25);
		SimulationPane
				.add(UnlockButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

		InjectFaultButton.setBackground(new java.awt.Color(102, 0, 0));
		InjectFaultButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		InjectFaultButton.setForeground(new java.awt.Color(204, 204, 0));
		InjectFaultButton.setText("Inject Fault");
		InjectFaultButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		InjectFaultButton.setFocusable(false);
		InjectFaultButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				InjectFaultButtonMouseClicked(evt);
			}
		});

		InjectFaultButton.setBounds(920, 360, 140, 25);
		SimulationPane.add(InjectFaultButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ChangeAlgorithmButton.setBackground(new java.awt.Color(102, 0, 0));
		ChangeAlgorithmButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ChangeAlgorithmButton.setForeground(new java.awt.Color(204, 204, 0));
		ChangeAlgorithmButton.setText("Change Algorithm");
		ChangeAlgorithmButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ChangeAlgorithmButton.setFocusable(false);
		ChangeAlgorithmButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ChangeAlgorithmButtonMouseClicked(evt);
					}
				});

		ChangeAlgorithmButton.setBounds(920, 410, 140, 25);
		SimulationPane.add(ChangeAlgorithmButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		MakeRequestButton.setBackground(new java.awt.Color(102, 0, 0));
		MakeRequestButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		MakeRequestButton.setForeground(new java.awt.Color(204, 204, 0));
		MakeRequestButton.setText("Make Request");
		MakeRequestButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		MakeRequestButton.setFocusable(false);
		MakeRequestButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				MakeRequestButtonMouseClicked(evt);
			}
		});

		MakeRequestButton.setBounds(920, 460, 140, 25);
		SimulationPane.add(MakeRequestButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		DisplayStatisticsButton.setBackground(new java.awt.Color(102, 0, 0));
		DisplayStatisticsButton.setFont(new java.awt.Font("Palatino Linotype",
				0, 14));
		DisplayStatisticsButton.setForeground(new java.awt.Color(204, 204, 0));
		DisplayStatisticsButton.setText("Display Statistics");
		DisplayStatisticsButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		DisplayStatisticsButton.setFocusable(false);
		DisplayStatisticsButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						DisplayStatisticsButtonMouseClicked(evt);
					}
				});

		DisplayStatisticsButton.setBounds(920, 510, 140, 25);
		SimulationPane.add(DisplayStatisticsButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ResumePauseButton.setBackground(new java.awt.Color(102, 0, 0));
		ResumePauseButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
		ResumePauseButton.setForeground(new java.awt.Color(204, 204, 0));
		ResumePauseButton.setText("Resume/Pause");
		ResumePauseButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ResumePauseButton.setFocusable(false);
		ResumePauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ResumePauseButtonMouseClicked(evt);
			}
		});

		ResumePauseButton.setBounds(920, 560, 140, 25);
		SimulationPane.add(ResumePauseButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		FinishSimulationButton.setBackground(new java.awt.Color(102, 0, 0));
		FinishSimulationButton.setFont(new java.awt.Font("Palatino Linotype",
				0, 14));
		FinishSimulationButton.setForeground(new java.awt.Color(204, 204, 0));
		FinishSimulationButton.setText("Finish Simulation");
		FinishSimulationButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		FinishSimulationButton.setFocusable(false);
		FinishSimulationButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						FinishSimulationButtonMouseClicked(evt);
					}
				});

		FinishSimulationButton.setBounds(920, 655, 140, 25);

		SimulationPane.add(FinishSimulationButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		GoFasterButton.setBackground(new java.awt.Color(102, 0, 0));
		GoFasterButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
		GoFasterButton.setForeground(new java.awt.Color(204, 204, 0));
		GoFasterButton.setText("Fast Forward");
		GoFasterButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		GoFasterButton.setFocusable(false);
		GoFasterButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				GoFasterButtonMouseClicked(evt);
			}
		});

		GoFasterButton.setBounds(700, 655, 170, 25);

		SimulationPane.add(GoFasterButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		SlowDownButton.setBackground(new java.awt.Color(102, 0, 0));
		SlowDownButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14));
		SlowDownButton.setForeground(new java.awt.Color(204, 204, 0));
		SlowDownButton.setText("Slow Down");
		SlowDownButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		SlowDownButton.setFocusable(false);
		SlowDownButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				SlowDownButtonMouseClicked(evt);
			}
		});

		SlowDownButton.setBounds(500, 655, 170, 25);

		SimulationPane.add(SlowDownButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ElevatorFasterButton.setBackground(new java.awt.Color(102, 0, 0));
		ElevatorFasterButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ElevatorFasterButton.setForeground(new java.awt.Color(204, 204, 0));
		ElevatorFasterButton.setText("Increase Elevator Speed");
		ElevatorFasterButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ElevatorFasterButton.setFocusable(false);
		ElevatorFasterButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ElevatorFasterButtonMouseClicked(evt);
					}
				});

		ElevatorFasterButton.setBounds(250, 655, 170, 25);
		SimulationPane.add(ElevatorFasterButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ElevatorSlowerButton.setBackground(new java.awt.Color(102, 0, 0));
		ElevatorSlowerButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ElevatorSlowerButton.setForeground(new java.awt.Color(204, 204, 0));
		ElevatorSlowerButton.setText("Decrease Elevator Speed");
		ElevatorSlowerButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ElevatorSlowerButton.setFocusable(false);
		ElevatorSlowerButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ElevatorSlowerButtonMouseClicked(evt);
					}
				});

		ElevatorSlowerButton.setBounds(50, 655, 170, 25);
		SimulationPane.add(ElevatorSlowerButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		SimulationLayeredPane.setBackground(new java.awt.Color(255, 255, 255));
		SimulationLayeredPane.setBorder(javax.swing.BorderFactory
				.createCompoundBorder());
		SimulationLayeredPane.setForeground(new java.awt.Color(255, 255, 255));
		SimulationLayeredPane.setBounds(10, 60, 900, 620);
		SimulationScrollBar.setBounds(880, 0, 17, 580);
		SimulationLayeredPane.add(SimulationScrollBar,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		SimulationScrollBar.setVisible(false);
		SimulationPane.add(SimulationLayeredPane,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		SimulationPane.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() > 0) {
					SimulationScrollBar.setValue(SimulationScrollBar.getValue() + 20);
					GUIDisplay.repaint();
				} else if (e.getWheelRotation() < 0)
					SimulationScrollBar.setValue(SimulationScrollBar.getValue() - 20);
				GUIDisplay.repaint();
			};

		});

		MonitorButton.setBackground(new java.awt.Color(102, 0, 0));
		MonitorButton.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		MonitorButton.setForeground(new java.awt.Color(204, 204, 0));
		MonitorButton.setText("Monitor");
		MonitorButton.setBorder(javax.swing.BorderFactory.createBevelBorder(
				javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray,
				java.awt.Color.black, java.awt.Color.darkGray,
				java.awt.Color.black));
		MonitorButton.setFocusable(false);
		MonitorButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				MonitorButtonButtonMouseClicked(evt);
			}
		});

		MonitorButton.setBounds(920, 110, 140, 25);
		SimulationPane.add(MonitorButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(MainMenuPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 1077,
						Short.MAX_VALUE)
				.addGroup(
						layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(SimulationSettingsPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										1077, Short.MAX_VALUE))
				.addGroup(
						layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(SimulationPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										1077, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(MainMenuPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 690,
						Short.MAX_VALUE)
				.addGroup(
						layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(SimulationSettingsPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										690, Short.MAX_VALUE))
				.addGroup(
						layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(SimulationPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										690, Short.MAX_VALUE)));

		this.setResizable(false);

		pack();
	}// </editor-fold>

	private void FinishSimulationButtonMouseClicked(
			java.awt.event.MouseEvent evt) {

		SimulationLayeredPane.removeAll();
		SimulationScrollBar.setBounds(880, 0, 17, 580);
		SimulationLayeredPane.add(SimulationScrollBar,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		SimulationPane.setVisible(false);
		GUIDisplay.stop();
		GUIDisplay.destroy();
		GUIDisplay = null;
		SC.endSimulation();
		SC = null;
		MainMenuPane.setVisible(true);
		timescale = 1;
		inputFile = null;
		Runtime.getRuntime().gc();
		StatsFrame.dispose();
		if (fileOutputPath != null) {
			BufferedWriter out;
			try {
				out = new BufferedWriter(new FileWriter(fileOutputPath));
				out.write("Simulevator\n");
				out.close();
				dtChart.writeToFile(fileOutputPath);
				frChart.writeToFile(fileOutputPath);
				pwChart.writeToFile(fileOutputPath);

			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		frChart.stopThread();
		pwChart.stopThread();
		dtChart.stopThread();
		commandServer.sendStop();
		commandServer.simulationEnd();
	}

	private void ResumePauseButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (simulationpaused == 0) {
			SC.pauseSimulation();
			commandServer.sendPause();
			simulationpaused = 1;
		} else {
			SC.resumeSimulation();
			commandServer.sendResume();
			simulationpaused = 0;
		}
	}

	private void ReturnToMenuButtonMouseClicked(java.awt.event.MouseEvent evt) {
		SimulationSettingsPane.setVisible(false);
		MainMenuPane.setVisible(true);
	}

	private void StartSimulationButtonMouseClicked(java.awt.event.MouseEvent evt) {
		MainMenuPane.setVisible(false);
		ElevatorSettingsField.setValue(10);
		FloorSettingsField.setText("20");
		ElevatorSpeedSettingsField.setText("200");
		MaxWeightField.setText("1000");
		ElevatorOutOfServiceField.setText("5000");
		ManualRadioButton.setSelected(true);
		SimulationSettingsPane.setVisible(true);

	}

	private void ExitButtonMouseClicked(java.awt.event.MouseEvent evt) {
		commandServer.sendQuit();
		commandServer.interrupt();
		System.exit(0);
	}

	private void ContinueButtonMouseClicked(java.awt.event.MouseEvent evt) {

		if (!FloorSettingsField.getText().matches("^[0-9]*$")
				|| "".equals(FloorSettingsField.getText())
				|| Integer.valueOf(FloorSettingsField.getText()) <= 0) {
			JOptionPane.showMessageDialog(null,
					"Number of Floors has to be a Positive Integer!");
			FloorSettingsField.setText("");
			FloorSettingsField.requestFocus();
			return;
		}

		else if (!ElevatorSpeedSettingsField.getText().matches("^[0-9]*$")
				|| "".equals(ElevatorSpeedSettingsField.getText())) {
			JOptionPane.showMessageDialog(null,
					"Max Elevator Speed has to be an Integer!");
			ElevatorSpeedSettingsField.setText("");
			ElevatorSpeedSettingsField.requestFocus();
			return;
		}

		else if (!MaxWeightField.getText().matches("^[0-9]*$")
				|| "".equals(MaxWeightField.getText())) {
			JOptionPane.showMessageDialog(null,
					"Time Until Elevator Door Closes has to be an Integer!");
			MaxWeightField.setText("");
			MaxWeightField.requestFocus();
			return;
		}

		else if (!ElevatorOutOfServiceField.getText().matches("^[0-9]*$")
				|| "".equals(ElevatorOutOfServiceField.getText())) {
			JOptionPane.showMessageDialog(null,
					"Time Until Elevator Out of Service has to be an Integer!");
			ElevatorOutOfServiceField.setText("");
			ElevatorOutOfServiceField.requestFocus();
			return;
		}

		else if (!ManualRadioButton.isSelected()
				&& !PresetRadioButton.isSelected()
				&& !RandomRadioButton.isSelected()) {
			JOptionPane
					.showMessageDialog(null, "Simulation Type Not Selected!");
		}

		else if (meaninitfloor > (Integer.valueOf(FloorSettingsField.getText()))
				|| meandestfloor > (Integer.valueOf(FloorSettingsField
						.getText()))
				|| sdinitfloor > (Integer.valueOf(FloorSettingsField.getText()))
				|| sddestfloor > (Integer.valueOf(FloorSettingsField.getText()))) {
			JOptionPane.showMessageDialog(null, "Invalid Mean/SD Floor!");
		}

		else {

			if (StatsFrame != null) {
				frChart.cleardata();
				pwChart.cleardata();
				dtChart.cleardata();
				StatsFrame.dispose();
			}

			maxspeed = Integer.valueOf(ElevatorSpeedSettingsField.getText());

			/* MEOW */
			Clock sysClock = new Clock(1, System.currentTimeMillis());
			SC = new SimulatorController(Integer.valueOf(FloorSettingsField
					.getText()), // Floor
					(Integer) ElevatorSettingsField.getValue(), // Elevator
					Integer.parseInt(ElevatorOutOfServiceField.getText()) * 60, // Position
																				// until
																				// out
																				// of
																				// service
					// Max elevator out of service
					inputFile, sysClock, Integer.valueOf(MaxWeightField
							.getText()),
					Integer.valueOf(ElevatorSpeedSettingsField.getText())); // external
																			// input
																			// file.
																			// null
																			// is
																			// none.

			GUIDisplay = new GUI(Integer.valueOf(FloorSettingsField.getText()),
					(Integer) ElevatorSettingsField.getValue(), SC, sysClock,
					commandServer, myIP);
			SimulationLayeredPane.add(GUIDisplay, BorderLayout.CENTER);

			if (Integer.valueOf(FloorSettingsField.getText()) > 7) {
				SimulationScrollBar.setMaximum((int) ((Integer
						.valueOf(FloorSettingsField.getText()) * 60) - 400));
				SimulationScrollBar.setValue(SimulationScrollBar.getMaximum());
				SimulationScrollBar.setVisible(true);
			}

			GUIDisplay.init();
			GUIDisplay.setVisible(true);

			SimulationSettingsPane.setVisible(false);
			FloorSettingsField.setText("");
			ElevatorSpeedSettingsField.setText("");
			MaxWeightField.setText("");
			ElevatorOutOfServiceField.setText("");
			dtChart = new DistanceTraveledChart("Distance");
			pwChart = new PassengerWaitChart("Passenger Waits");
			frChart = new FloorRequestsChart("Floor Requests");
			dtChart.startThread();
			pwChart.startThread();
			frChart.startThread();

			SC.startNewSimulation(AL, pwChart, dtChart, frChart);

			if (RandomRadioButton.isSelected()) {
				if (random) {
					SC.setRandomRequestDelay(minrequestspeed, maxrequestspeed);
					SC.setMaxExternalRequest(maxsimultaneousrequests);
				} else if (nd_init) {
					SC.useNormalDistribution(true, false);
					SC.setMeanAndSDInit(meaninitfloor, sdinitfloor);
				} else if (nd_dest) {
					SC.useNormalDistribution(false, true);
					SC.setMeanAndSDDest(meandestfloor, sddestfloor);
				} else {
					SC.useNormalDistribution(true, true);
					SC.setMeanAndSDInit(meaninitfloor, sdinitfloor);
					SC.setMeanAndSDDest(meandestfloor, sddestfloor);
				}
			}

			if (ManualRadioButton.isSelected()) {
				SC.pauseRequestSimulator();
				SC.manualRequestSimulation();
			}

			commandServer.initSimulatorController(SC);
			commandServer.sendResume();
			SimulationPane.setVisible(true);

			UIManager.put("swing.boldMetal", Boolean.FALSE);
			StatsFrame = new JFrame("Statistics");
			StatsFrame.add(new Statistics(dtChart, frChart, pwChart),
					BorderLayout.CENTER);
			StatsFrame.pack();
		}
	}

	private void DisplayStatisticsButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		StatsFrame.setVisible(true);

	}

	private void MakeRequestButtonMouseClicked(java.awt.event.MouseEvent evt) {
		Request r = new Request();
		r.setVisible(true);
	}

	private void InjectFaultButtonMouseClicked(java.awt.event.MouseEvent evt) {
		InjectFault f = new InjectFault();
		f.setVisible(true);
	}

	private void UnlockButtonMouseClicked(java.awt.event.MouseEvent evt) {
		Unlock u = new Unlock();
		u.setVisible(true);
	}

	private void LockoutButtonMouseClicked(java.awt.event.MouseEvent evt) {
		Lockout l = new Lockout();
		l.setVisible(true);
	}

	private void ResumeOperationButtonMouseClicked(java.awt.event.MouseEvent evt) {
		ResumeOperation r = new ResumeOperation();
		r.setVisible(true);
	}

	private void TakeOutOfServiceButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		TakeOutOfService s = new TakeOutOfService();
		s.setVisible(true);
	}

	private void PlotStatisticsButtonMouseClicked(java.awt.event.MouseEvent evt) {
		int PlotStatisticsFileReturnValue = PlotStatisticsFileChooser
				.showOpenDialog(this);
		if (PlotStatisticsFileReturnValue == JFileChooser.APPROVE_OPTION) {
			String filepath = PlotStatisticsFileChooser.getSelectedFile()
					.toString();

			if (filepath.substring(filepath.length() - 4, filepath.length())
					.equals(".txt")) {
				try {
					BufferedReader in = new BufferedReader(new FileReader(
							filepath));
					String line;
					if ((line = in.readLine()) != null
							&& !line.contains("Simulevator")) {
						JOptionPane.showMessageDialog(null, "Invalid File!");

					} else {
						in.close();
						dtChart = new DistanceTraveledChart(
								"Distance Traveled", filepath);
						pwChart = new PassengerWaitChart("Passenger Waits",
								filepath);
						frChart = new FloorRequestsChart("Floor Requests",
								filepath);

						UIManager.put("swing.boldMetal", Boolean.FALSE);
						StatsFrame = new JFrame("Statistics");
						StatsFrame.add(
								new Statistics(dtChart, frChart, pwChart),
								BorderLayout.CENTER);
						StatsFrame.pack();
						StatsFrame.setVisible(true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				JOptionPane.showMessageDialog(null, "Invalid File Type!");
			}
		}
	}

	private void FileOutputButtonMouseClicked(java.awt.event.MouseEvent evt) {
		int FileOutputReturnValue = FileOutputFileChooser.showOpenDialog(this);
		if (FileOutputReturnValue == JFileChooser.APPROVE_OPTION)
			fileOutputPath = FileOutputFileChooser.getSelectedFile().toString();

	}

	private void ChangeAlgorithmButtonMouseClicked(java.awt.event.MouseEvent evt) {
		ChangeAlgorithm a = new ChangeAlgorithm(AL);
		a.setVisible(true);
	}

	private void AlgorithmButtonMouseClicked(java.awt.event.MouseEvent evt) {
		ChangeAlgorithm a = new ChangeAlgorithm(AL);
		a.setVisible(true);
	}

	private void MonitorButtonButtonMouseClicked(java.awt.event.MouseEvent evt) {
		Monitor m = new Monitor();
		m.setVisible(true);
	}

	private void SlowDownButtonMouseClicked(java.awt.event.MouseEvent evt) {
		timescale = timescale <= 0.0078125 ? 0.0078125 : timescale / 2;
		SC.setTimeScale(timescale);
	}

	private void GoFasterButtonMouseClicked(java.awt.event.MouseEvent evt) {
		timescale = timescale >= 128 ? 128 : timescale * 2;
		SC.setTimeScale(timescale);

	}

	private void ElevatorFasterButtonMouseClicked(java.awt.event.MouseEvent evt) {
		int tmp;
		for (tmp = SC.getElevatorSpeed() + 1; tmp % 30 != 0; tmp++)
			;
		tmp = tmp > maxspeed ? maxspeed : tmp;
		SC.setAllElevatorSpeed(tmp);
	}

	private void ElevatorSlowerButtonMouseClicked(java.awt.event.MouseEvent evt) {
		int tmp;
		for (tmp = SC.getElevatorSpeed() - 1; tmp % 30 != 0; tmp--)
			;
		tmp = tmp <= 1 ? 1 : tmp;
		SC.setAllElevatorSpeed(tmp);
	}

	public static void main(String args[]) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Simulevator.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Simulevator.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Simulevator.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Simulevator.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new Simulevator().setVisible(true);

			}
		});
	}

	private javax.swing.JButton AlgorithmButton;
	private javax.swing.JButton ChangeAlgorithmButton;
	private javax.swing.JButton ContinueButton;
	private javax.swing.JButton DisplayStatisticsButton;
	private javax.swing.JButton ExitButton;
	private javax.swing.JFileChooser FileInputFileChooser;
	private javax.swing.JButton FileOutputButton;
	private javax.swing.JFileChooser FileOutputFileChooser;
	private javax.swing.JButton FinishSimulationButton;
	private javax.swing.JButton InjectFaultButton;
	private javax.swing.JButton LockoutButton;
	private javax.swing.JLayeredPane MainMenuLayeredPane;
	private javax.swing.JDesktopPane MainMenuPane;
	private javax.swing.JButton MakeRequestButton;
	private javax.swing.JButton PlotStatisticsButton;
	private javax.swing.JFileChooser PlotStatisticsFileChooser;
	private javax.swing.JButton ResumeOperationButton;
	private javax.swing.JButton ResumePauseButton;
	private javax.swing.JButton ReturnToMenuButton;
	private javax.swing.JLayeredPane SimulationLayeredPane;
	private javax.swing.JDesktopPane SimulationPane;
	public static javax.swing.JScrollBar SimulationScrollBar;
	private javax.swing.JDesktopPane SimulationSettingsPane;
	private javax.swing.JButton StartSimulationButton;
	private javax.swing.JButton TakeOutOfServiceButton;
	private javax.swing.JButton UnlockButton;
	private javax.swing.JButton GoFasterButton;
	private javax.swing.JButton SlowDownButton;
	private javax.swing.JButton ElevatorFasterButton;
	private javax.swing.JButton ElevatorSlowerButton;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JRadioButton ManualRadioButton;
	private javax.swing.JRadioButton PresetRadioButton;
	private javax.swing.JRadioButton RandomRadioButton;
	private javax.swing.JSpinner ElevatorSettingsField;
	private javax.swing.JTextField FloorSettingsField;
	private javax.swing.JTextField ElevatorSpeedSettingsField;
	private javax.swing.JTextField MaxWeightField;
	private javax.swing.JTextField ElevatorOutOfServiceField;
	private javax.swing.JButton MonitorButton;
	private GUI GUIDisplay;
	public static DistanceTraveledChart dtChart;
	public static FloorRequestsChart frChart;
	public static PassengerWaitChart pwChart;
	public static SimulatorController SC;
	public static int simulationpaused = 0;
	private static double timescale = 1;
	/* MEOW */private File inputFile = null;
	/* MEOW */private ArrayList<Algorithm> AL;
	private CommandServer commandServer = new CommandServer();

	private String fileOutputPath = null;
	public static Thread msThread;
	private static int maxspeed;

	public static int minrequestspeed;
	public static int maxrequestspeed;
	public static int meaninitfloor;
	public static int meandestfloor;
	public static int sddestfloor;
	public static int sdinitfloor;
	public static int maxsimultaneousrequests;
	public static boolean random;
	public static boolean nd_init;
	public static boolean nd_dest;
	public static String myIP;
	private static JFrame StatsFrame;
}
