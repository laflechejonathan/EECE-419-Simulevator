package statistics;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class Statistics extends JPanel {

	public Statistics(DistanceTraveledChart dtChart,
			FloorRequestsChart frChart, PassengerWaitChart pwChart) {
		super();
		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent panel1 = frChart.jpanel;
		tabbedPane.addTab("Floor Requests", panel1);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		JComponent panel2 = dtChart.jpanel;
		tabbedPane.addTab("Distance Traveled", panel2);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		JComponent panel3 = pwChart.jpanel;
		tabbedPane.addTab("Passenger Waits", panel3);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

}
