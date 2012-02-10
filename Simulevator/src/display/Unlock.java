package display;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import driver.Simulevator;

@SuppressWarnings("serial")
public class Unlock extends javax.swing.JFrame {

	public Unlock() {

		initComponents();
		this.setTitle("Unlock");
		this.setLocation(200, 100);
		UnlockPane.setVisible(true);

	}

	private void initComponents() {

		jLabel32 = new javax.swing.JLabel();
		CancelUnlockButton = new javax.swing.JButton();
		ConfirmUnlockButton = new javax.swing.JButton();
		jLabel33 = new javax.swing.JLabel();
		UnlockElevatorField = new javax.swing.JSpinner();
		UnlockFloorField = new javax.swing.JTextField();
		UnlockPane = new javax.swing.JDesktopPane();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel32.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel32.setForeground(new java.awt.Color(255, 255, 255));
		jLabel32.setText("Floor(s):");
		jLabel32.setBounds(30, 160, 130, 30);
		UnlockPane.add(jLabel32, javax.swing.JLayeredPane.DEFAULT_LAYER);

		CancelUnlockButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelUnlockButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		CancelUnlockButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelUnlockButton.setText("Cancel Unlock");
		CancelUnlockButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelUnlockButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CancelUnlockButtonMouseClicked(evt);
			}
		});
		CancelUnlockButton.setBounds(380, 320, 130, 25);
		UnlockPane.add(CancelUnlockButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmUnlockButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmUnlockButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmUnlockButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmUnlockButton.setText("Confirm Unlock");
		ConfirmUnlockButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmUnlockButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ConfirmUnlockButtonMouseClicked(evt);
			}
		});
		ConfirmUnlockButton.setBounds(380, 280, 130, 25);
		UnlockPane.add(ConfirmUnlockButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel33.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel33.setForeground(new java.awt.Color(255, 255, 255));
		jLabel33.setText("Elevator #:");
		jLabel33.setBounds(30, 100, 130, 30);
		UnlockPane.add(jLabel33, javax.swing.JLayeredPane.DEFAULT_LAYER);

		UnlockElevatorField.setModel(new javax.swing.SpinnerNumberModel(1, 1,
				10, 1));
		UnlockElevatorField.setBounds(190, 100, 90, 30);
		JFormattedTextField UnlockElevatorFieldUnEditable = ((JSpinner.DefaultEditor) UnlockElevatorField
				.getEditor()).getTextField();
		UnlockElevatorFieldUnEditable.setEditable(false);
		UnlockPane.add(UnlockElevatorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		UnlockFloorField.setBounds(190, 160, 90, 30);
		UnlockPane
				.add(UnlockFloorField, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout UnlockFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(UnlockFrameLayout);
		UnlockFrameLayout.setHorizontalGroup(UnlockFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(UnlockPane, javax.swing.GroupLayout.DEFAULT_SIZE,
						535, Short.MAX_VALUE));
		UnlockFrameLayout.setVerticalGroup(UnlockFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(UnlockPane, javax.swing.GroupLayout.DEFAULT_SIZE,
						363, Short.MAX_VALUE));

	}

	private void ConfirmUnlockButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (!UnlockFloorField.getText().matches("^[0-9]*$")
				|| "".equals(UnlockFloorField.getText())) {
			JOptionPane.showMessageDialog(null, "Floor has to be an Integer!");
			UnlockFloorField.setText("");
			UnlockFloorField.requestFocus();
			return;
		}

		else {
			Simulevator.SC.unlockOutFloor(
					Integer.valueOf(UnlockFloorField.getText()));
			this.dispose();
		}
	}

	private void CancelUnlockButtonMouseClicked(java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JDesktopPane UnlockPane;
	private javax.swing.JButton CancelUnlockButton;
	private javax.swing.JButton ConfirmUnlockButton;
	private javax.swing.JSpinner UnlockElevatorField;
	private javax.swing.JTextField UnlockFloorField;
	private javax.swing.JLabel jLabel32;
	private javax.swing.JLabel jLabel33;

}
