package display;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;

import driver.Simulevator;


@SuppressWarnings("serial")
public class TakeOutOfService extends javax.swing.JFrame {

	public TakeOutOfService() {

		initComponents();
		this.setTitle("Take Out of Service");
		this.setLocation(200, 100);
		TakeOutOfServicePane.setVisible(true);

	}

	private void initComponents() {

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		TakeOutOfServicePane = new javax.swing.JDesktopPane();
		CancelTakeOutOfServiceButton = new javax.swing.JButton();
		ConfirmTakeOutOfServiceButton = new javax.swing.JButton();
		jLabel37 = new javax.swing.JLabel();
		TakeOutOfServiceElevatorField = new javax.swing.JSpinner();

		CancelTakeOutOfServiceButton
				.setBackground(new java.awt.Color(102, 0, 0));
		CancelTakeOutOfServiceButton.setFont(new java.awt.Font(
				"Palatino Linotype", 0, 14)); // NOI18N
		CancelTakeOutOfServiceButton.setForeground(new java.awt.Color(204, 204,
				0));
		CancelTakeOutOfServiceButton.setText("Cancel");
		CancelTakeOutOfServiceButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelTakeOutOfServiceButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						CancelTakeOutOfServiceButtonMouseClicked(evt);
					}
				});
		CancelTakeOutOfServiceButton.setBounds(380, 320, 130, 25);
		TakeOutOfServicePane.add(CancelTakeOutOfServiceButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmTakeOutOfServiceButton.setBackground(new java.awt.Color(102, 0,
				0));
		ConfirmTakeOutOfServiceButton.setFont(new java.awt.Font(
				"Palatino Linotype", 0, 14)); // NOI18N
		ConfirmTakeOutOfServiceButton.setForeground(new java.awt.Color(204,
				204, 0));
		ConfirmTakeOutOfServiceButton.setText("Confirm");
		ConfirmTakeOutOfServiceButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmTakeOutOfServiceButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ConfirmTakeOutOfServiceButtonMouseClicked(evt);
					}
				});
		ConfirmTakeOutOfServiceButton.setBounds(380, 280, 130, 25);
		TakeOutOfServicePane.add(ConfirmTakeOutOfServiceButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel37.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel37.setForeground(new java.awt.Color(255, 255, 255));
		jLabel37.setText("Elevator #:");
		jLabel37.setBounds(30, 100, 130, 30);
		TakeOutOfServicePane.add(jLabel37,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		TakeOutOfServiceElevatorField
				.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
		TakeOutOfServiceElevatorField.setBounds(190, 100, 90, 30);
		JFormattedTextField TakeOutOfServiceElevatorFieldUnEditable = ((JSpinner.DefaultEditor) TakeOutOfServiceElevatorField
				.getEditor()).getTextField();
		TakeOutOfServiceElevatorFieldUnEditable.setEditable(false);
		TakeOutOfServicePane.add(TakeOutOfServiceElevatorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		javax.swing.GroupLayout TakeOutOfServiceFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(
				TakeOutOfServiceFrameLayout);
		TakeOutOfServiceFrameLayout
				.setHorizontalGroup(TakeOutOfServiceFrameLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(TakeOutOfServicePane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 535,
								Short.MAX_VALUE));
		TakeOutOfServiceFrameLayout
				.setVerticalGroup(TakeOutOfServiceFrameLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(TakeOutOfServicePane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 363,
								Short.MAX_VALUE));
	}
	

	private void ConfirmTakeOutOfServiceButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		Simulevator.SC.lockOutElevator((Integer) TakeOutOfServiceElevatorField
				.getValue() - 1);
		this.dispose();
	}
	

	private void CancelTakeOutOfServiceButtonMouseClicked(
			java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JDesktopPane TakeOutOfServicePane;
	private javax.swing.JButton CancelTakeOutOfServiceButton;
	private javax.swing.JButton ConfirmTakeOutOfServiceButton;
	private javax.swing.JSpinner TakeOutOfServiceElevatorField;
	private javax.swing.JLabel jLabel37;

}
