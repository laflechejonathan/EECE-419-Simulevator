package display;

import gui.GUI;
import driver.Simulevator;

@SuppressWarnings("serial")
public class Monitor extends javax.swing.JFrame {

	public Monitor() {

		initComponents();
		this.setTitle("Monitor");
		this.setLocation(200, 100);
		MonitorPane.setVisible(true);

	}

	private void initComponents() {

		MonitorPane = new javax.swing.JDesktopPane();
		CancelMonitorButton = new javax.swing.JButton();
		ConfirmMonitorButton = new javax.swing.JButton();
		FollowElevatorField = new javax.swing.JSpinner();
		MonitorFloorField = new javax.swing.JTextField();
		jLabel41 = new javax.swing.JLabel();
		jLabel42 = new javax.swing.JLabel();
		jLabel43 = new javax.swing.JLabel();
		jLabel44 = new javax.swing.JLabel();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel41.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel41.setForeground(new java.awt.Color(255, 255, 255));
		jLabel41.setText("Floor:");
		jLabel41.setBounds(30, 200, 130, 30);
		MonitorPane.add(jLabel41, javax.swing.JLayeredPane.DEFAULT_LAYER);

		CancelMonitorButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelMonitorButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		CancelMonitorButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelMonitorButton.setText("Cancel");
		CancelMonitorButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelMonitorButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CancelMonitorButtonButtonMouseClicked(evt);
			}
		});
		CancelMonitorButton.setBounds(380, 320, 130, 25);
		MonitorPane.add(CancelMonitorButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmMonitorButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmMonitorButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmMonitorButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmMonitorButton.setText("Confirm");
		ConfirmMonitorButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmMonitorButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ConfirmMonitorButtonButtonMouseClicked(evt);
					}
				});
		ConfirmMonitorButton.setBounds(380, 280, 130, 25);
		MonitorPane.add(ConfirmMonitorButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel42.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel42.setForeground(new java.awt.Color(255, 255, 255));
		jLabel42.setText("OR");
		jLabel42.setBounds(290, 150, 130, 30);
		MonitorPane.add(jLabel42, javax.swing.JLayeredPane.DEFAULT_LAYER);

		FollowElevatorField.setModel(new javax.swing.SpinnerNumberModel(0, 0,
				10, 1));
		FollowElevatorField.setBounds(260, 100, 90, 30);
		MonitorPane.add(FollowElevatorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		MonitorFloorField.setBounds(260, 200, 90, 30);
		MonitorPane.add(MonitorFloorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel43.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel43.setForeground(new java.awt.Color(255, 255, 255));
		jLabel43.setText("Elevator #:");
		jLabel43.setBounds(30, 100, 130, 30);
		MonitorPane.add(jLabel43, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel44.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel44.setForeground(new java.awt.Color(255, 255, 255));
		jLabel44.setText("(0 for None)");
		jLabel44.setBounds(30, 120, 130, 30);
		MonitorPane.add(jLabel44, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout MonitorFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(MonitorFrameLayout);
		MonitorFrameLayout.setHorizontalGroup(MonitorFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(MonitorPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 535,
						Short.MAX_VALUE));
		MonitorFrameLayout.setVerticalGroup(MonitorFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(MonitorPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}

	private void ConfirmMonitorButtonButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		if ((Integer) FollowElevatorField.getValue() == 0) {
			for (int i = 1; i < GUI.numElevators; i++)
				GUI.follow_elev[i] = false;
			if (!"".equals(MonitorFloorField.getText()))
				Simulevator.SimulationScrollBar
						.setValue((50 + (60 * GUI.numFloors))
								- (60 * Integer.valueOf(MonitorFloorField
										.getText())) - 300);
		} else if ((Integer) FollowElevatorField.getValue() != 0) {
			for (int i = 1; i < GUI.numElevators; i++)
				GUI.follow_elev[i] = false;
			GUI.follow_elev[(Integer) FollowElevatorField.getValue()] = true;
		}

		this.dispose();
	}

	private void CancelMonitorButtonButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JTextField MonitorFloorField;
	private javax.swing.JDesktopPane MonitorPane;
	private javax.swing.JButton CancelMonitorButton;
	private javax.swing.JButton ConfirmMonitorButton;
	private javax.swing.JSpinner FollowElevatorField;
	private javax.swing.JLabel jLabel41;
	private javax.swing.JLabel jLabel42;
	private javax.swing.JLabel jLabel43;
	private javax.swing.JLabel jLabel44;

}
