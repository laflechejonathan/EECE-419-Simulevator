package statistics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.io.*;
import java.util.Calendar;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;

@SuppressWarnings("serial")
public class PassengerWaitChart extends ApplicationFrame implements Runnable {

	static Thread thread1;
	public static XYSeries s1 = new XYSeries("Elevator", true, false);
	private static boolean threadstarted = false;
	private static int[] waits;
	private static long incWait = 0;
	private static long incWaitSize = 0;
	public JPanel jpanel;

	public PassengerWaitChart(String title) {
		super(title);
		jpanel = createPanel();
		jpanel.setPreferredSize(new Dimension(700, 415));
		setContentPane(jpanel);

		waits = new int[100];
		thread1 = new Thread(this);

	}

	public PassengerWaitChart(String title, String path) {
		super(title);
		jpanel = createPanel();
		jpanel.setPreferredSize(new Dimension(700, 415));
		setContentPane(jpanel);

		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line;
			while ((line = in.readLine()) != null
					&& !line.contains("PASSENGER WAITS"))
				;
			while ((line = in.readLine()) != null && line.contains(",")) {
				String[] line_split = new String[1];
				line_split = line.split(",");
				s1.addOrUpdate(Double.parseDouble(line_split[0]),
						Double.parseDouble(line_split[1]));
			}
			in.close();
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
		s1.clear();
	}

	public void cleardata() {
		s1.clear();
	}

	public void run() {

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while (true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		}

	}

	// UPDATE PASSENGER WAIT TIME
	synchronized public void addPassengerWait(long wait) {
		int bin = 0;
		incWait += wait / 1000;
		incWaitSize++;
		for (; bin < 99 && wait > 0; bin++, wait -= 1000) {
			if (waits[bin] < Integer.MAX_VALUE)
				waits[bin]++;
		}
		if (waits[bin] < Integer.MAX_VALUE)
			s1.addOrUpdate((double) bin, waits[bin]);

	}

	public double getWaitTimeAverage() {
		try {
			return (incWait / incWaitSize);
		} catch (ArithmeticException e) {
			return 0;
		} catch (Exception e) {
			incWait = incWait / incWaitSize;
			incWaitSize = 1;
			return incWait;
		}
	}

	public static JFreeChart CreateChart() {

		String title = "Passenger Wait";
		String xAxisLabel = "Wait Times";
		String rangeAxisLabel = "Passengers";

		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH));
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

		TextTitle aa = new TextTitle(year + "." + month + "." + day);
		aa.setHorizontalAlignment(HorizontalAlignment.CENTER);
		aa.setPaint(Color.white);

		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(s1);

		JFreeChart chart = ChartFactory.createXYBarChart(title, xAxisLabel,
				false, rangeAxisLabel, data, PlotOrientation.VERTICAL, true,
				true, false);

		chart.setBackgroundPaint(new Color(55, 55, 55));
		chart.addSubtitle(aa);
		LegendTitle LegendTitle = chart.getLegend();
		LegendTitle.setBackgroundPaint(Color.darkGray);
		LegendTitle.setItemPaint(Color.white);
		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		plot.setBackgroundPaint(Color.black);
		plot.getRenderer().setSeriesPaint(0, Color.BLUE);

		NumberAxis valueaxis = (NumberAxis) plot.getDomainAxis();
		valueaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		ValueAxis Rangeaxis = plot.getRangeAxis();
		Rangeaxis.setLabelPaint(Color.white);
		Rangeaxis.setAxisLinePaint(Color.white);
		Rangeaxis.setTickLabelPaint(Color.white);
		valueaxis.setLabelPaint(Color.white);
		valueaxis.setAxisLinePaint(Color.white);
		valueaxis.setTickLabelPaint(Color.white);
		TextTitle LocalTitle = chart.getTitle();
		LocalTitle.setPaint(Color.white);

		return chart;
	}

	public static JPanel createPanel() {
		JFreeChart jfreechart = CreateChart();
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public void writeToFile(String path) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true));
			out.write("PASSENGER WAITS\n");
			for (int i = 0; i < s1.getItemCount(); i++) {
				XYDataItem a = s1.getDataItem(i);
				out.write(a.getXValue() + "," + a.getYValue() + "\n");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
