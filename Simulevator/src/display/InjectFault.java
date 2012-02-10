package display;

import javax.swing.JOptionPane;

import driver.Simulevator;

@SuppressWarnings("serial")
public class InjectFault extends javax.swing.JFrame {

	public InjectFault() {

		initComponents();
		this.setTitle("Fault");
		this.setLocation(200, 100);
		InjectFaultPane.setVisible(true);

	}

	private void initComponents() {

		InjectFaultPane = new javax.swing.JDesktopPane();
		jLabel24 = new javax.swing.JLabel();
		CancelInjectionButton = new javax.swing.JButton();
		ConfirmInjectionButton = new javax.swing.JButton();
		FaultType = new javax.swing.JComboBox();
		jLabel25 = new javax.swing.JLabel();
		jLabel26 = new javax.swing.JLabel();
		jLabel27 = new javax.swing.JLabel();
		FaultSensorType = new javax.swing.JComboBox();
		FaultValueField = new javax.swing.JTextField();
		FaultElevatorField = new javax.swing.JTextField();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel24.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel24.setForeground(new java.awt.Color(255, 255, 255));
		jLabel24.setText("Sensor:");
		jLabel24.setBounds(30, 280, 130, 30);
		InjectFaultPane.add(jLabel24, javax.swing.JLayeredPane.DEFAULT_LAYER);

		CancelInjectionButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelInjectionButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		CancelInjectionButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelInjectionButton.setText("Cancel Injection");
		CancelInjectionButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelInjectionButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						CancelInjectionButtonMouseClicked(evt);
					}
				});
		CancelInjectionButton.setBounds(380, 320, 130, 25);
		InjectFaultPane.add(CancelInjectionButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmInjectionButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmInjectionButton.setFont(new java.awt.Font("Palatino Linotype",
				0, 14)); // NOI18N
		ConfirmInjectionButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmInjectionButton.setText("Confirm Injection");
		ConfirmInjectionButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmInjectionButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ConfirmInjectionButtonMouseClicked(evt);
					}
				});
		ConfirmInjectionButton.setBounds(380, 280, 130, 25);
		InjectFaultPane.add(ConfirmInjectionButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		FaultType.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Stuck Sensor", "Constant Bias", "Random Bias", "Door Stuck",
				"Door Erratic", "Zombie Infestation", "Earthquake", "Fire", "Lockdown", "Evacuation" }));
		FaultType.setBounds(210, 100, 130, 30);
		InjectFaultPane
				.addMouseMotionListener(new java.awt.event.MouseMotionListener() {
					public void mouseMoved(java.awt.event.MouseEvent evt) {
						ChooseFaultType(evt);
					}

					public void mouseDragged(java.awt.event.MouseEvent evt) {

					}
				});
		InjectFaultPane.add(FaultType, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel25.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel25.setForeground(new java.awt.Color(255, 255, 255));
		jLabel25.setText("Fault Type:");
		jLabel25.setBounds(30, 100, 130, 30);
		InjectFaultPane.add(jLabel25, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel26.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel26.setForeground(new java.awt.Color(255, 255, 255));
		jLabel26.setText("Value:");
		jLabel26.setBounds(30, 220, 130, 30);
		InjectFaultPane.add(jLabel26, javax.swing.JLayeredPane.DEFAULT_LAYER);

		FaultSensorType.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "position", "speed", "weight" }));
		FaultSensorType.setBounds(210, 280, 80, 30);
		InjectFaultPane.add(FaultSensorType,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		FaultValueField.setBounds(210, 220, 80, 30);
		InjectFaultPane.add(FaultValueField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		FaultElevatorField.setBounds(210, 160, 80, 30);
		InjectFaultPane.add(FaultElevatorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel27.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel27.setForeground(new java.awt.Color(255, 255, 255));
		jLabel27.setText("Elevator #:");
		jLabel27.setBounds(30, 160, 130, 30);
		InjectFaultPane.add(jLabel27, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout InjectFaultFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(InjectFaultFrameLayout);
		InjectFaultFrameLayout.setHorizontalGroup(InjectFaultFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(InjectFaultPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 535,
						Short.MAX_VALUE));
		InjectFaultFrameLayout.setVerticalGroup(InjectFaultFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(InjectFaultPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}

	private void ChooseFaultType(java.awt.event.MouseEvent evt) {
		// "Stuck Sensor", "Constant Bias", "Random Bias", "Door Stuck",
		// "Door Erratic" }));

		if (FaultType.getSelectedItem().toString().equals("Stuck Sensor")
				|| FaultType.getSelectedItem().toString().equals("Random Bias")
				|| FaultType.getSelectedItem().toString()
						.equals("Constant Bias")) {
			jLabel26.setVisible(true);
			jLabel24.setVisible(true);
			FaultSensorType.setVisible(true);
			FaultValueField.setVisible(true);
		} else if (FaultType.getSelectedItem().toString().equals("Door Stuck")
				|| FaultType.getSelectedItem().toString()
						.equals("Door Erratic")) {
			jLabel26.setVisible(false);
			jLabel24.setVisible(false);
			FaultSensorType.setVisible(false);
			FaultValueField.setVisible(false);
		} else {
			jLabel26.setVisible(false);
			jLabel24.setVisible(false);
			FaultSensorType.setVisible(false);
			FaultValueField.setVisible(false);
			jLabel27.setVisible(false);
			FaultElevatorField.setVisible(false);

		}

	}

	private void ConfirmInjectionButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		/*
		 * if (!FaultWeightField.getText().matches("^[0-9]*$") ||
		 * "".equals(FaultWeightField.getText())) {
		 * JOptionPane.showMessageDialog(null, "Weight has to be an Integer!");
		 * FaultWeightField.setText(""); FaultWeightField.requestFocus();
		 * return; }
		 * 
		 * else if (!FaultElevatorField.getText().matches("^[0-9]*$") ||
		 * "".equals(FaultElevatorField.getText())) {
		 * JOptionPane.showMessageDialog(null,
		 * "Elevator # has to be an Integer!"); FaultElevatorField.setText("");
		 * FaultElevatorField.requestFocus(); return; } /* else if
		 * (FaultType.getSelectedItem().equals("Erratic Weight"))
		 * Driver.SC.elevatorErraticWeightFault(Integer
		 * .parseInt(FaultElevatorField.getText())-1); else if
		 * (FaultType.getSelectedItem().equals("Bias Weight"))
		 * Driver.SC.elevatorBiasWeightFault(
		 * Integer.parseInt(FaultElevatorField.getText())-1,
		 * Integer.parseInt(FaultWeightField.getText()));
		 */

		/*
		 * * else if (!FaultFloorField.getText().matches("^[0-9]*$") ||
		 * "".equals(FaultFloorField.getText())) {
		 * JOptionPane.showMessageDialog(null, "Floor has to be an Integer!");
		 * FaultFloorField.setText(""); FaultFloorField.requestFocus(); return;
		 * } else {
		 */

		// We need to have different Inputs for different faults.. Not cool
		// enough for that
		// But. look at my try catch block for dealing with bad input.
		// 2cool4school
		try {
			// "Stuck Sensor", "Constant Bias", "Random Bias", "Door Stuck",
			// "Door  Erratic" }));

			if (FaultType.getSelectedItem().equals("Stuck Sensor")) {

				Simulevator.SC.setElevatorSensorStuck(
						(Integer.parseInt(FaultElevatorField.getText()) - 1),
						Integer.parseInt(FaultValueField.getText()),
						(String) FaultSensorType.getSelectedItem());
			} else if (FaultType.getSelectedItem().equals("Constant Bias")) {
				Simulevator.SC.setElevatorSensorCBias(
						(Integer.parseInt(FaultElevatorField.getText()) - 1),
						Integer.parseInt(FaultValueField.getText()),
						(String) FaultSensorType.getSelectedItem());
			} else if (FaultType.getSelectedItem().equals("Random Bias")) {
				Simulevator.SC.setElevatorSensorRBias(
						(Integer.parseInt(FaultElevatorField.getText()) - 1),
						Integer.parseInt(FaultValueField.getText()),
						(String) FaultSensorType.getSelectedItem());
			} else if (FaultType.getSelectedItem().equals("Door Stuck")) {
				Simulevator.SC.setElevatorDoorSensorStuck(
						Integer.parseInt(FaultElevatorField.getText()) - 1,
						Boolean.parseBoolean(FaultValueField.getText()));
			} else if (FaultType.getSelectedItem().equals("Door Erratic")) {
				Simulevator.SC.setelevatorDoorSensorRand(Integer
						.parseInt(FaultElevatorField.getText()) - 1);
			} else if (FaultType.getSelectedItem().equals("Zombie Infestation")) {
				Simulevator.SC.triggerZombieInfestation();
			} else if (FaultType.getSelectedItem().equals("Earthquake")) {
				Simulevator.SC.triggerBuildingEarthquake();
			} else if (FaultType.getSelectedItem().equals("Fire")) {
				Simulevator.SC.triggerBuildingOnFire();
			} else if (FaultType.getSelectedItem().equals("Lockdown")) {
				Simulevator.SC.triggerBuildingLockDown();
			} else if (FaultType.getSelectedItem().equals("Evacuation")) {
				Simulevator.SC.triggerBuildingEvacuation();
			}



		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Invalid Input!  Try Again");
			FaultElevatorField.setText("");
			FaultElevatorField.requestFocus();
			return;
		}
		this.dispose();
		// }
	}

	private void CancelInjectionButtonMouseClicked(java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JButton CancelInjectionButton;
	private javax.swing.JButton ConfirmInjectionButton;
	private javax.swing.JTextField FaultElevatorField;
	private javax.swing.JComboBox FaultType;
	private javax.swing.JComboBox FaultSensorType;
	private javax.swing.JTextField FaultValueField;
	private javax.swing.JDesktopPane InjectFaultPane;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel26;
	private javax.swing.JLabel jLabel27;

}
