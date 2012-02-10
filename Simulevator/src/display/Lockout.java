package display;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import driver.Simulevator;

@SuppressWarnings("serial")
public class Lockout extends javax.swing.JFrame {

	public Lockout() {
		initComponents();
		this.setTitle("Lockout");
		this.setLocation(200, 100);
		LockoutPane.setVisible(true);
	}

	private void initComponents() {

		LockoutPane = new javax.swing.JDesktopPane();
		jLabel29 = new javax.swing.JLabel();
		CancelLockoutButton = new javax.swing.JButton();
		ConfirmLockoutButton = new javax.swing.JButton();
		jLabel30 = new javax.swing.JLabel();
		LockoutElevatorField = new javax.swing.JSpinner();
		LockoutFloorField = new javax.swing.JTextField();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel29.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel29.setForeground(new java.awt.Color(255, 255, 255));
		jLabel29.setText("Floor(s):");
		jLabel29.setBounds(30, 160, 130, 30);
		LockoutPane.add(jLabel29, javax.swing.JLayeredPane.DEFAULT_LAYER);

		CancelLockoutButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelLockoutButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		CancelLockoutButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelLockoutButton.setText("Cancel Lockout");
		CancelLockoutButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelLockoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CancelLockoutButtonMouseClicked(evt);
			}
		});
		CancelLockoutButton.setBounds(380, 320, 130, 25);
		LockoutPane.add(CancelLockoutButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmLockoutButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmLockoutButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmLockoutButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmLockoutButton.setText("Confirm Lockout");
		ConfirmLockoutButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmLockoutButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						ConfirmLockoutButtonMouseClicked(evt);
					}
				});
		ConfirmLockoutButton.setBounds(380, 280, 130, 25);
		LockoutPane.add(ConfirmLockoutButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		jLabel30.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel30.setForeground(new java.awt.Color(255, 255, 255));
		jLabel30.setText("Elevator #:");
		jLabel30.setBounds(30, 100, 130, 30);
		LockoutPane.add(jLabel30, javax.swing.JLayeredPane.DEFAULT_LAYER);

		LockoutElevatorField.setModel(new javax.swing.SpinnerNumberModel(1, 1,
				10, 1));
		LockoutElevatorField.setBounds(190, 100, 90, 30);
		JFormattedTextField LockoutElevatorFieldUnEditable = ((JSpinner.DefaultEditor) LockoutElevatorField
				.getEditor()).getTextField();
		LockoutElevatorFieldUnEditable.setEditable(false);
		LockoutPane.add(LockoutElevatorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);
		LockoutFloorField.setBounds(190, 160, 90, 30);
		LockoutPane.add(LockoutFloorField,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout LockoutFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(LockoutFrameLayout);
		LockoutFrameLayout.setHorizontalGroup(LockoutFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(LockoutPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 535,
						Short.MAX_VALUE));
		LockoutFrameLayout.setVerticalGroup(LockoutFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(LockoutPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}

	private void ConfirmLockoutButtonMouseClicked(java.awt.event.MouseEvent evt) {
		if (!LockoutFloorField.getText().matches("^[0-9]*$")
				|| "".equals(LockoutFloorField.getText())) {
			JOptionPane.showMessageDialog(null, "Floor has to be an Integer!");
			LockoutFloorField.setText("");
			LockoutFloorField.requestFocus();
			return;
		}

		else {
			Simulevator.SC.lockOutFloor(
					Integer.valueOf(LockoutFloorField.getText()));
			this.dispose();
		}
	}

	private void CancelLockoutButtonMouseClicked(java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JButton ConfirmLockoutButton;
	private javax.swing.JButton CancelLockoutButton;
	private javax.swing.JLabel jLabel29;
	private javax.swing.JLabel jLabel30;
	private javax.swing.JDesktopPane LockoutPane;
	private javax.swing.JSpinner LockoutElevatorField;
	private javax.swing.JTextField LockoutFloorField;
}
