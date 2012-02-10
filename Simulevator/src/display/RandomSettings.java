package display;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import driver.Simulevator;

@SuppressWarnings("serial")
public class RandomSettings extends javax.swing.JFrame {

	public RandomSettings() {

		initComponents();
		this.setTitle("Request Simulator Settings");
		this.setLocation(200, 100);
		RandomSettingsPane.setVisible(true);

	}

	private void initComponents() {

		RandomSettingsPane = new javax.swing.JDesktopPane();
		jLabel24 = new javax.swing.JLabel();
		ConfirmRandomButton = new javax.swing.JButton();
		RequestType = new javax.swing.JComboBox();
		jLabel22 = new javax.swing.JLabel();
		jLabel23 = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		jLabel25 = new javax.swing.JLabel();
		jLabel26 = new javax.swing.JLabel();
		jLabel27 = new javax.swing.JLabel();
		jLabel28 = new javax.swing.JLabel();
		jLabel29 = new javax.swing.JLabel();
		jLabel30 = new javax.swing.JLabel();
		MinSpeedField = new javax.swing.JSlider();
		MaxSpeedField = new javax.swing.JSlider();
		SimultaneousRequestsField = new javax.swing.JSlider();
		MeanInitField = new javax.swing.JSpinner();
		MeanDestField = new javax.swing.JSpinner();
		SDInitField = new javax.swing.JSpinner();
		SDDestField = new javax.swing.JSpinner();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel25.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel25.setForeground(new java.awt.Color(255, 255, 255));
		jLabel25.setText("Request Type:");
		jLabel25.setBounds(30, 70, 130, 30);
		RandomSettingsPane
				.add(jLabel25, javax.swing.JLayeredPane.DEFAULT_LAYER);

		RequestType.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Random", "N. Distribution (Both)","N. Distribution (Initial Floor)", "N. Distribution (Dest. Floor)"}));
				RequestType.setBounds(300, 70, 225, 30);
		RequestType.setBounds(320, 70, 180, 30);
		RandomSettingsPane
				.addMouseMotionListener(new java.awt.event.MouseMotionListener() {
					public void mouseMoved(java.awt.event.MouseEvent evt) {
						ChooseRequestType(evt);
					}

					public void mouseDragged(java.awt.event.MouseEvent evt) {

					}
				});
		RandomSettingsPane.add(RequestType,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel27.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel27.setForeground(new java.awt.Color(255, 255, 255));
		jLabel27.setText("Min. Request Speed (1 = 10ms):");
		jLabel27.setBounds(30, 110, 240, 30);
		RandomSettingsPane
				.add(jLabel27, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLabel28.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel28.setForeground(new java.awt.Color(255, 255, 255));
		jLabel28.setText("100");
		jLabel28.setBounds(460, 110, 80, 30);
		RandomSettingsPane
				.add(jLabel28, javax.swing.JLayeredPane.DEFAULT_LAYER);
		MinSpeedField.setMaximum(1000);
		MinSpeedField.setMinimum(0);
		MinSpeedField.setMajorTickSpacing(10);
		MinSpeedField.setValue(100);
		MinSpeedField.setSnapToTicks(true);
		MinSpeedField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				jLabel28.setText(String.valueOf(MinSpeedField.getValue()));
			}
		});
		MinSpeedField.setBounds(320, 110, 130, 30);
		RandomSettingsPane.add(MinSpeedField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel26.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel26.setForeground(new java.awt.Color(255, 255, 255));
		jLabel26.setText("Max Request Speed (1 = 10ms):");
		jLabel26.setBounds(30, 150, 240, 30);
		RandomSettingsPane
				.add(jLabel26, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLabel29.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel29.setForeground(new java.awt.Color(255, 255, 255));
		jLabel29.setText("250");
		jLabel29.setBounds(460, 150, 80, 30);
		RandomSettingsPane
				.add(jLabel29, javax.swing.JLayeredPane.DEFAULT_LAYER);
		MaxSpeedField.setMaximum(1000);
		MaxSpeedField.setMinimum(0);
		MaxSpeedField.setMajorTickSpacing(10);
		MaxSpeedField.setValue(250);
		MaxSpeedField.setSnapToTicks(true);
		MaxSpeedField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				jLabel29.setText(String.valueOf(MaxSpeedField.getValue()));
			}
		});
		MaxSpeedField.setBounds(320, 150, 130, 30);
		RandomSettingsPane.add(MaxSpeedField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel22.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel22.setForeground(new java.awt.Color(255, 255, 255));
		jLabel22.setText("Max Simultaneous Requests:");
		jLabel22.setBounds(30, 190, 260, 30);
		RandomSettingsPane
				.add(jLabel22, javax.swing.JLayeredPane.DEFAULT_LAYER);
		jLabel30.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel30.setForeground(new java.awt.Color(255, 255, 255));
		jLabel30.setText("5");
		jLabel30.setBounds(460, 190, 80, 30);
		RandomSettingsPane
				.add(jLabel30, javax.swing.JLayeredPane.DEFAULT_LAYER);
		SimultaneousRequestsField.setMaximum(100);
		SimultaneousRequestsField.setMinimum(0);
		SimultaneousRequestsField.setMajorTickSpacing(1);
		SimultaneousRequestsField.setValue(5);
		SimultaneousRequestsField.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				jLabel30.setText(String.valueOf(SimultaneousRequestsField
						.getValue()));
			}
		});
		SimultaneousRequestsField.setBounds(320, 190, 130, 30);
		RandomSettingsPane.add(SimultaneousRequestsField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel24.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel24.setForeground(new java.awt.Color(255, 255, 255));
		jLabel24.setText("Mean Floor (Initial, Destination):");
		jLabel24.setBounds(30, 230, 260, 30);
		jLabel24.setVisible(false);
		RandomSettingsPane
				.add(jLabel24, javax.swing.JLayeredPane.DEFAULT_LAYER);
		MeanInitField.setBounds(320, 230, 100, 30);
		MeanInitField.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
		MeanInitField.setModel(new javax.swing.SpinnerNumberModel(0, 0,
				1000000, 1));
		RandomSettingsPane.add(MeanInitField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		MeanInitField.setVisible(false);
		MeanDestField.setBounds(430, 230, 100, 30);
		MeanDestField.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
		MeanDestField.setModel(new javax.swing.SpinnerNumberModel(1, 0,
				1000000, 1));
		MeanDestField.setVisible(false);

		RandomSettingsPane.add(MeanDestField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel23.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel23.setForeground(new java.awt.Color(255, 255, 255));
		jLabel23.setText("Standard Deviation (Initial, Destination):");
		jLabel23.setBounds(30, 270, 260, 30);
		jLabel23.setVisible(false);
		RandomSettingsPane
				.add(jLabel23, javax.swing.JLayeredPane.DEFAULT_LAYER);
		SDInitField.setBounds(320, 270, 100, 30);
		SDInitField.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
		SDInitField.setModel(new javax.swing.SpinnerNumberModel(1, 0, 1000000,
				1));
		SDInitField.setVisible(false);
		RandomSettingsPane.add(SDInitField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		SDDestField.setBounds(430, 270, 100, 30);
		SDDestField.setFont(new java.awt.Font("Palatino Linotype", 0, 12));
		SDDestField.setModel(new javax.swing.SpinnerNumberModel(1, 0, 1000000,
				1));
		SDDestField.setVisible(false);
		RandomSettingsPane.add(SDDestField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmRandomButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmRandomButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmRandomButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmRandomButton.setText("Confirm");
		ConfirmRandomButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmRandomButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ConfirmRandomButtonMouseClicked(evt);
			}
		});
		ConfirmRandomButton.setBounds(380, 320, 130, 25);
		RandomSettingsPane.add(ConfirmRandomButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout RandomSettingsFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(RandomSettingsFrameLayout);
		RandomSettingsFrameLayout.setHorizontalGroup(RandomSettingsFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(RandomSettingsPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 535,
						Short.MAX_VALUE));
		RandomSettingsFrameLayout.setVerticalGroup(RandomSettingsFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(RandomSettingsPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}

	private void ChooseRequestType(java.awt.event.MouseEvent evt) {

		if (RequestType.getSelectedItem().toString().equals("Random")) {
			MeanInitField.setVisible(false);
			MeanDestField.setVisible(false);
			SDInitField.setVisible(false);
			SDDestField.setVisible(false);
			jLabel23.setVisible(false);
			jLabel24.setVisible(false);
		}
		else if (RequestType.getSelectedItem().toString().equals("N. Distribution (Initial Floor)")) {
			jLabel23.setVisible(true);
			jLabel24.setVisible(true);
			MeanDestField.setVisible(false);
			SDDestField.setVisible(false);
			MeanInitField.setVisible(true);
			SDInitField.setVisible(true);
		}
		
		else if (RequestType.getSelectedItem().toString().equals("N. Distribution (Dest. Floor)")) {
			jLabel23.setVisible(true);
			jLabel24.setVisible(true);
			MeanDestField.setVisible(true);
			SDDestField.setVisible(true);
			MeanInitField.setVisible(false);
			SDInitField.setVisible(false);
		}
		
		else {
			MeanInitField.setVisible(true);
			MeanDestField.setVisible(true);
			SDInitField.setVisible(true);
			SDDestField.setVisible(true);
			jLabel23.setVisible(true);
			jLabel24.setVisible(true);
		}
	}

	private void ConfirmRandomButtonMouseClicked(java.awt.event.MouseEvent evt) {

		if (MaxSpeedField.getValue() < MinSpeedField.getValue()) {
			JOptionPane.showMessageDialog(null, "Invalid Input! Try Again");

		}

		else {

			Simulevator.maxrequestspeed = MaxSpeedField.getValue() * 10;
			Simulevator.minrequestspeed = MinSpeedField.getValue() * 10;
			Simulevator.maxsimultaneousrequests = SimultaneousRequestsField
					.getValue();

			Simulevator.random = true;
			Simulevator.nd_init = false;
			Simulevator.nd_dest = false;

			if (RequestType.getSelectedItem().toString()
					.equals("Normal Distribution (Initial Floor)")) {
				
				Simulevator.meaninitfloor = (Integer) MeanInitField.getValue();
				Simulevator.sdinitfloor = (Integer) SDInitField.getValue();

				Simulevator.random = false;
				Simulevator.nd_init = true;
				Simulevator.nd_dest = false;


			}
			
			else if (RequestType.getSelectedItem().toString()
					.equals("Normal Distribution (Dest. Floor)")) {

				Simulevator.sddestfloor = (Integer) SDDestField.getValue();
				Simulevator.meandestfloor = (Integer) MeanDestField.getValue();
	
				Simulevator.random = false;
				Simulevator.nd_init = false;
				Simulevator.nd_dest = true;
			}
			else if (RequestType.getSelectedItem().toString()
					.equals("Normal Distribution (Both)")) {

				Simulevator.meaninitfloor = (Integer) MeanInitField.getValue();
				Simulevator.meandestfloor = (Integer) MeanDestField.getValue();
				Simulevator.sdinitfloor = (Integer) SDInitField.getValue();
				Simulevator.sddestfloor = (Integer) SDDestField.getValue();

				Simulevator.random = false;
				Simulevator.nd_init = false;
				Simulevator.nd_dest = false;
			}
			this.dispose();
		}
	}

	private javax.swing.JButton ConfirmRandomButton;
	private javax.swing.JSlider MinSpeedField;
	private javax.swing.JSlider MaxSpeedField;
	private javax.swing.JSlider SimultaneousRequestsField;
	private javax.swing.JSpinner MeanInitField;
	private javax.swing.JSpinner MeanDestField;
	private javax.swing.JSpinner SDInitField;
	private javax.swing.JSpinner SDDestField;
	private javax.swing.JComboBox RequestType;
	private javax.swing.JDesktopPane RandomSettingsPane;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel26;
	private javax.swing.JLabel jLabel27;
	private javax.swing.JLabel jLabel28;
	private javax.swing.JLabel jLabel29;
	private javax.swing.JLabel jLabel30;

}
