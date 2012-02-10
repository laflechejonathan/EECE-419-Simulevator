package clock;

public class Clock extends Thread{
	private double scale = 1;
	private long timeEl = 0;
	private long initTime = 0;
	private Boolean paused = false;
	public Clock(int s,long init)
	{
		scale = s;
		initTime = init;
	}
	
	public void run()
	{
		while(true)
		{
			try {
				synchronized(this)
				{
					while (paused)
						wait();
				}
				long time = System.currentTimeMillis();
				sleep(50);
				timeEl += (System.currentTimeMillis()-time)*scale;
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		}
	}
	public void pause()
	{
		paused = true;
	}
	public void unPause()
	{
		synchronized (this)
		{
			paused = false;
			notify();
		}
	}
	public void setTimeScale(double s)
	{
		scale = s;
	}
	public double getTimeScale()
	{
		return scale;
	}
	public long getTimeMillis()
	{
		return initTime + timeEl;
	}
}
