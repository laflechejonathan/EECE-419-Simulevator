package display;

import java.util.ArrayList;
import algorithm.Algorithm;
import driver.Simulevator;

@SuppressWarnings("serial")
public class ChangeAlgorithm extends javax.swing.JFrame {

	/* MEOW */ ArrayList<Algorithm> AL;
	
	/* MEOW */
	public ChangeAlgorithm(ArrayList<Algorithm> pAL) {
		AL = pAL;
		initComponents();
		this.setTitle("Algorithm");
		this.setLocation(200, 100);
		AlgorithmPane.setVisible(true);
	}

	private void initComponents() {
		AlgorithmPane = new javax.swing.JDesktopPane();
		jLabel23 = new javax.swing.JLabel();
		CancelChangeButton = new javax.swing.JButton();
		ConfirmChangeButton = new javax.swing.JButton();
		AlgorithmComboBox = new javax.swing.JComboBox();

		this.setBackground(new java.awt.Color(0, 0, 0));
		this.setMinimumSize(new java.awt.Dimension(535, 403));

		jLabel23.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		jLabel23.setForeground(new java.awt.Color(255, 255, 255));
		jLabel23.setText("Select Algorithm:");
		jLabel23.setBounds(30, 100, 130, 30);
		AlgorithmPane.add(jLabel23, javax.swing.JLayeredPane.DEFAULT_LAYER);

		CancelChangeButton.setBackground(new java.awt.Color(102, 0, 0));
		CancelChangeButton
				.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
		CancelChangeButton.setForeground(new java.awt.Color(204, 204, 0));
		CancelChangeButton.setText("Cancel");
		CancelChangeButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		CancelChangeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CancelChangeButtonMouseClicked(evt);
			}
		});
		CancelChangeButton.setBounds(380, 320, 130, 25);
		AlgorithmPane.add(CancelChangeButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		ConfirmChangeButton.setBackground(new java.awt.Color(102, 0, 0));
		ConfirmChangeButton.setFont(new java.awt.Font("Palatino Linotype", 0,
				14)); // NOI18N
		ConfirmChangeButton.setForeground(new java.awt.Color(204, 204, 0));
		ConfirmChangeButton.setText("Confirm");
		ConfirmChangeButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
						java.awt.Color.darkGray, java.awt.Color.black,
						java.awt.Color.darkGray, java.awt.Color.black));
		ConfirmChangeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ConfirmChangeButtonMouseClicked(evt);
			}
		});
		ConfirmChangeButton.setBounds(380, 280, 130, 25);
		AlgorithmPane.add(ConfirmChangeButton,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		String s[] = new String[AL.size()];
		for (int i = 0; i < s.length; i++)
			s[i] = AL.get(i).name();

		AlgorithmComboBox.setModel(new javax.swing.DefaultComboBoxModel(s));
		AlgorithmComboBox.setBounds(210, 100, 130, 30);
		AlgorithmPane.add(AlgorithmComboBox,
				javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout AlgorithmFrameLayout = new javax.swing.GroupLayout(
				this.getContentPane());
		this.getContentPane().setLayout(AlgorithmFrameLayout);
		AlgorithmFrameLayout.setHorizontalGroup(AlgorithmFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(AlgorithmPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 535,
						Short.MAX_VALUE));
		AlgorithmFrameLayout.setVerticalGroup(AlgorithmFrameLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(AlgorithmPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 363,
						Short.MAX_VALUE));

	}

	/* MEOW */
	// Re arrange so that selected algorithm is on top
	// Since simulevator always runs the first algorithm
	private void ConfirmChangeButtonMouseClicked(java.awt.event.MouseEvent evt) {
		Algorithm tempA = null;
		if(Simulevator.SC != null){
			Simulevator.SC.changeAlgorithm(AlgorithmComboBox.getSelectedItem()
					.toString());
		}
		for(int i=0; i<AL.size(); i++){
			if(AL.get(i).name().equals(AlgorithmComboBox.getSelectedItem().toString())){
				tempA = AL.get(i);
				AL.remove(i);
				AL.add(0, tempA);
			}
		}
		this.dispose();
	}

	private void CancelChangeButtonMouseClicked(java.awt.event.MouseEvent evt) {
		this.dispose();
	}

	private javax.swing.JComboBox AlgorithmComboBox;
	private javax.swing.JDesktopPane AlgorithmPane;
	private javax.swing.JButton CancelChangeButton;
	private javax.swing.JButton ConfirmChangeButton;
	private javax.swing.JLabel jLabel23;

}
