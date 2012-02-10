package statistics;

import gui.GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import java.awt.event.AdjustmentEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

@SuppressWarnings("serial")
public class FloorRequestsChart extends JFrame implements Runnable {

	static Thread thread1;
	public static SlidingCategoryDataset dataset;
	public static int abc;
	public static DefaultCategoryDataset Data;
	private static JScrollBar jscrollbar;
	private static ValueAxis Rangeaxis;
	private static int[][] floor_req;
	private static boolean threadstarted = false;
	public JPanel jpanel;
	private double maxFloor = 0;

	public FloorRequestsChart(String title) {
		super(title);
		jpanel = createPanel();
		jpanel.setPreferredSize(new Dimension(700, 415));
		setContentPane(jpanel);
		getContentPane().add(FloorRequestsChart.jscrollbar, BorderLayout.SOUTH);

		for (int i = 0; i < GUI.numFloors; i++)
			for (int j = 1; j <= GUI.numElevators; j++)
				Data.addValue(0, (Comparable<?>) j, (Comparable<?>) i);

		jscrollbar.setMaximum(GUI.numFloors - 10);

		floor_req = new int[GUI.numElevators][GUI.numFloors];
		thread1 = new Thread(this);
	}

	public FloorRequestsChart(String title, String path) {
		super(title);
		jpanel = createPanel();
		jpanel.setPreferredSize(new Dimension(700, 415));
		setContentPane(jpanel);
		getContentPane().add(FloorRequestsChart.jscrollbar, BorderLayout.SOUTH);

		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line;
			while ((line = in.readLine()) != null
					&& !line.contains("FLOOR REQUESTS"))
				;
			while ((line = in.readLine()) != null && line.contains(",")) {
				String[] line_split = new String[1];
				line_split = line.split(",");
				Data.addValue((Number) (Double.parseDouble((line_split[2]))),
						(Comparable<?>) line_split[0],
						(Comparable<?>) line_split[1]);
				maxFloor = Double.parseDouble(line_split[1]) + 1;
			}
			jscrollbar.setMaximum((int) maxFloor - 10);
			in.close();
			thread1 = new Thread(this);
			this.startThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startThread() {

		if (threadstarted == false) {
			thread1.start();
			threadstarted = true;
		}
	}

	@SuppressWarnings("deprecation")
	public void stopThread() {
		thread1.stop();
		Data.clear();
	}

	public void cleardata() {
		Data.clear();
	}

	public void run() {

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while (true) {

			jscrollbar.setBounds(0, 385, 260, 20);
			jscrollbar.setDoubleBuffered(true);

			try {

				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		}
	}

	public void updateFloorRequestsChart(int elev, int floor) {
		if (floor_req[elev][floor] < Integer.MAX_VALUE) {
			floor_req[elev][floor]++;
			Data.addValue(floor_req[elev][floor], (Comparable<?>) (elev + 1),
					(Comparable<?>) floor);
		}
	}

	private static JFreeChart createChart() {

		String title = "Floor Requests";
		String CategoryAxisLabel = "Floor";
		String rangeAxisLabel = "Requests";

		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH));
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

		TextTitle aa = new TextTitle(year + "." + month + "." + day);
		aa.setHorizontalAlignment(HorizontalAlignment.CENTER);
		aa.setPaint(Color.white);
		Data = new DefaultCategoryDataset();
		dataset = new SlidingCategoryDataset(Data, 0, 10);

		JFreeChart chart = ChartFactory.createBarChart3D(title,
				CategoryAxisLabel, rangeAxisLabel, dataset,
				PlotOrientation.VERTICAL, true, // legend
				true,// tooltips,
				false // urls
				);

		chart.setBackgroundPaint(new Color(55, 55, 55));
		chart.addSubtitle(aa);
		LegendTitle LegendTitle = chart.getLegend();
		LegendTitle.setPosition(RectangleEdge.RIGHT);
		LegendTitle.setBackgroundPaint(Color.darkGray);
		LegendTitle.setItemPaint(Color.white);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.getRenderer().setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		plot.setBackgroundPaint(Color.black);
		plot.setOutlinePaint(Color.white);
		CategoryAxis Categoryaxis = plot.getDomainAxis();
		jscrollbar = getScrollBar(Categoryaxis);
		Rangeaxis = plot.getRangeAxis();
		Rangeaxis.setLabelPaint(Color.white);
		Rangeaxis.setAxisLinePaint(Color.white);
		Rangeaxis.setTickLabelPaint(Color.white);
		Categoryaxis.setLabelPaint(Color.white);
		Categoryaxis.setAxisLinePaint(Color.white);
		Categoryaxis.setTickLabelPaint(Color.white);
		TextTitle LocalTitle = chart.getTitle();
		LocalTitle.setPaint(Color.white);

		return chart;
	}

	private static JScrollBar getScrollBar(final CategoryAxis CategoryAxis) {
		JScrollBar jscrollbar = new JScrollBar(JScrollBar.HORIZONTAL);
		jscrollbar.setBounds(0, 380, 260, 20);
		jscrollbar.setModel(new DefaultBoundedRangeModel());
		jscrollbar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				dataset.setFirstCategoryIndex(e.getValue());
				dataset.setMaximumCategoryCount(10);
			}
		});
		return jscrollbar;
	}

	public JPanel createPanel() {
		JFreeChart jfreechart = createChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public void writeToFile(String path) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true));
			out.write("FLOOR REQUESTS\n");
			for (int i = 1; i < Data.getRowCount(); i++) {
				for (int j = 0; j < Data.getColumnCount(); j++) {
					Number a = Data.getValue((Comparable<?>) i,
							(Comparable<?>) j);
					out.write(i + "," + j + "," + a.toString() + "\n");
				}
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
