package simulator;

import java.util.Random;


public class Sensor {
	private int value;
	public int constantBias = 0;
	public int randomBias = 0;
	public int maxValue;
	private boolean isStuck = false;
	private int stuckValue = 0;
	private String sensorString;
	Random mrand;
	public Sensor(int v, int mv, String ss)
	{
		value = v;
		maxValue = mv;
		mrand = new Random();
		sensorString = ss;
		
	}
	public int getValue()
	{
		if (isStuck)
			return stuckValue;
		return value + constantBias + getRBias();
	}
	public void setMaxValue(int m)
	{
		maxValue = m;
	}
	public void setValue(int v)
	{
		value = v;
	}
	public void check() throws CasualFaultException, CriticalFaultException
	{
			if (Math.abs(getValue() - getValue()) > 10)
				throw new CasualFaultException("Erratic" + sensorString);
			else if (getValue() > maxValue)
				throw new CriticalFaultException(sensorString + "Limit Exceeded");
			return;
	}
	public void setRandomBias(int r)
	{
		randomBias = r;
	}
	public void clearRandomBias()
	{
		randomBias = 0;
	}
	public void setConstantBias(int c)
	{
		constantBias = c;
	}
	public void clearConstantBias()
	{
		constantBias = 0;
	}
	public void setStuck(int s)
	{
		isStuck = true;
		stuckValue = s;
	}
	public void clearStuck()
	{
		isStuck = false;
	}
	public void incrementValue(int inc)
	{
		value += inc;
	}
	private int getRBias()
	{
		if (randomBias > 0 )
			return mrand.nextInt(randomBias);
		return 0;
	}

}
