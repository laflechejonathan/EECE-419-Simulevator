package display;


import javax.swing.JOptionPane;


import driver.Simulevator;

@SuppressWarnings("serial")
public class Request extends javax.swing.JFrame {

	public Request() {
		initComponents();
		this.setTitle("Request");
		this.setLocation(200, 100);
		MakeRequestPane.setVisible(true);
	}

	private void initComponents() {
		MakeRequestPane = new javax.swing.JDesktopPane();
		CancelRequestButton = new javax.swing.JButton();
		ConfirmRequestButton = new javax.swing.JButton();
		jLabel18 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		TotalPassengerWeightField = new javax.swing.JTextField();
		InitialFloorField = new javax.swing.JTextField();
		DestinationFloorField = new javax.swing.JTextField();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel18.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel18.setForeground(new java.awt.Color(255, 255, 255));
		jLabel18.setText("Destination Floor(s):");
		jLabel18.setBounds(30, 240, 210, 30);
		MakeRequestPane.add(jLabel18, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel19.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel19.setForeground(new java.awt.Color(255, 255, 255));
		jLabel19.setText("Total Passenger Weight (in kg):");
		jLabel19.setBounds(30, 100, 210, 30);
		MakeRequestPane.add(jLabel19, javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel20.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel20.setForeground(new java.awt.Color(255, 255, 255));
		jLabel20.setText("Initial Floor:");
		jLabel20.setBounds(30, 170, 210, 30);
		MakeRequestPane.add(jLabel20, javax.swing.JLayeredPane.DEFAULT_LAYER);
		TotalPassengerWeightField.setBounds(270, 100, 80, 30);
		MakeRequestPane.add(TotalPassengerWeightField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		InitialFloorField.setBounds(270, 170, 80, 30);
		MakeRequestPane.add(InitialFloorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		DestinationFloorField.setBounds(270, 240, 80, 30);
		MakeRequestPane.add(DestinationFloorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		CancelRequestButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelRequestButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		CancelRequestButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelRequestButton.setText("Cancel Request");
		CancelRequestButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelRequestButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CancelRequestButtonMouseClicked(evt);
			}
		});
		CancelRequestButton.setBounds(380, 320, 130, 25);
		MakeRequestPane.add(CancelRequestButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmRequestButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmRequestButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmRequestButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmRequestButton.setText("Confirm Request");
		ConfirmRequestButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmRequestButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ConfirmRequestButtonMouseClicked(evt);
					}
				});
		ConfirmRequestButton.setBounds(380, 280, 130, 25);
		MakeRequestPane.add(ConfirmRequestButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout MakeRequestFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(MakeRequestFrameLayout);
		MakeRequestFrameLayout.setHorizontalGroup(MakeRequestFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(MakeRequestPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 535,
						Short.MAX_VALUE));
		MakeRequestFrameLayout.setVerticalGroup(MakeRequestFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(MakeRequestPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}
	
	private void ConfirmRequestButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (!TotalPassengerWeightField.getText().matches("^[0-9]*$")
				|| "".equals(TotalPassengerWeightField.getText())) {
			JOptionPane.showMessageDialog(null,
					"Total Passenger Weight has to be an Integer!");
			TotalPassengerWeightField.setText("");
			TotalPassengerWeightField.requestFocus();
			return;
		}

		else if (!InitialFloorField.getText().matches("^[0-9]*$")
				|| "".equals(InitialFloorField.getText())) {
			JOptionPane.showMessageDialog(null,
					"Initial Floor has to be an Integer!");
			InitialFloorField.setText("");
			InitialFloorField.requestFocus();
			return;
		}

		else if (!DestinationFloorField.getText().matches("^[0-9]*$")
				|| "".equals(DestinationFloorField.getText())) {
			JOptionPane.showMessageDialog(null,
					"Destination Floor has to be an Integer!");
			DestinationFloorField.setText("");
			DestinationFloorField.requestFocus();
			return;
		} else {
			if("".equals(TotalPassengerWeightField.getText()))
				Simulevator.SC.addPassenger(
						Integer.valueOf(InitialFloorField.getText()),
						Integer.valueOf(DestinationFloorField.getText()));
			else
				Simulevator.SC.addPassenger(
						Integer.valueOf(InitialFloorField.getText()),
						Integer.valueOf(DestinationFloorField.getText()),
						Integer.valueOf(TotalPassengerWeightField.getText()));

			this.dispose();
		}
	}
	
	private void CancelRequestButtonMouseClicked(java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	
	private javax.swing.JTextField TotalPassengerWeightField;
	private javax.swing.JTextField InitialFloorField;
	private javax.swing.JTextField DestinationFloorField;
	private javax.swing.JButton CancelRequestButton;
	private javax.swing.JButton ConfirmRequestButton;
	private javax.swing.JDesktopPane MakeRequestPane;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel20;

}
