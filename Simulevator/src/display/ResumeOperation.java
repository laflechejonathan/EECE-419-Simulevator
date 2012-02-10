package display;

import driver.Simulevator;

@SuppressWarnings("serial")
public class ResumeOperation extends javax.swing.JFrame {

	public ResumeOperation() {

		initComponents();
		this.setTitle("Resume Operation");
		this.setLocation(200, 100);
		ResumeOperationPane.setVisible(true);

	}

	private void initComponents() {

		ResumeOperationPane = new javax.swing.JDesktopPane();
		CancelResumeButton = new javax.swing.JButton();
		ConfirmResumeButton = new javax.swing.JButton();
		jLabel36 = new javax.swing.JLabel();
		ResumeOperationElevatorField = new javax.swing.JComboBox();
		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		CancelResumeButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelResumeButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		CancelResumeButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelResumeButton.setText("Cancel Resume");
		CancelResumeButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelResumeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CancelResumeButtonMouseClicked(evt);
			}
		});
		CancelResumeButton.setBounds(380, 320, 130, 25);
		ResumeOperationPane.add(CancelResumeButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmResumeButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmResumeButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmResumeButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmResumeButton.setText("Confirm Resume");
		ConfirmResumeButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmResumeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ConfirmResumeButtonMouseClicked(evt);
			}
		});
		ConfirmResumeButton.setBounds(380, 280, 130, 25);
		ResumeOperationPane.add(ConfirmResumeButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel36.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel36.setForeground(new java.awt.Color(255, 255, 255));
		jLabel36.setText("Elevator #:");
		jLabel36.setBounds(30, 100, 130, 30);
		ResumeOperationPane.add(jLabel36,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ResumeOperationElevatorField
				.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
						"All", "1", "2", "3", "4", "5", "6", "7", "8", "9",
						"10" }));
		ResumeOperationElevatorField.setBounds(190, 100, 90, 30);
		ResumeOperationPane.add(ResumeOperationElevatorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout ResumeOperationFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(ResumeOperationFrameLayout);
		ResumeOperationFrameLayout
				.setHorizontalGroup(ResumeOperationFrameLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(ResumeOperationPane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 535,
								Short.MAX_VALUE));
		ResumeOperationFrameLayout.setVerticalGroup(ResumeOperationFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(ResumeOperationPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}

	private void ConfirmResumeButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (ResumeOperationElevatorField.getSelectedItem().equals("All"))
			Simulevator.SC.backToNormal();
		else
			Simulevator.SC.unlockElevator(Integer.parseInt(ResumeOperationElevatorField.getSelectedItem().toString())-1);
		this.dispose();
	}

	private void CancelResumeButtonMouseClicked(java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JLabel jLabel36;
	private javax.swing.JComboBox ResumeOperationElevatorField;
	private javax.swing.JDesktopPane ResumeOperationPane;
	private javax.swing.JButton CancelResumeButton;
	private javax.swing.JButton ConfirmResumeButton;

}
