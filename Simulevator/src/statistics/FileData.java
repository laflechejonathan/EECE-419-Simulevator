package statistics;

import java.io.*;

import org.jfree.data.xy.XYSeries;

public class FileData {
	private BufferedWriter fileOut;
	private XYSeries Data;
	public FileData(String title, String fPath) throws IOException
	{
		Data = new XYSeries(title, true, false);
		fileOut = new BufferedWriter(new FileWriter(fPath));
	}
	public void addData(double x, double y) throws IOException
	{
		if (Data.getItemCount() > 100)
		{
			Number oldestX = Data.getX(0);
			Number oldestY = Data.getY(0);
			Data.remove(0);
			fileOut.write(oldestX.toString() + "," + oldestY.toString());
		}
		Data.addOrUpdate(x,y);
	}
}
