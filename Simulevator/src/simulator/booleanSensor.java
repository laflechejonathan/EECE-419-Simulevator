package simulator;

import java.util.Random;


public class booleanSensor{
	private boolean value;
	public boolean isErratic = false;
	public boolean isStuck = false;
	public boolean stuckValue = false;
	private String sensorString;
	Random mrand;
	public booleanSensor(boolean v, String ss)
	{
		value = v;
		mrand = new Random();
		sensorString = ss;
		
	}
	public Boolean getValue()
	{
		if (isErratic)
			return mrand.nextBoolean();
		else if (isStuck)
			return stuckValue;
		return value;
	}
	public void setValue(boolean v) throws CriticalFaultException
	{
		if (isStuck)
			throw new CriticalFaultException(sensorString + "is stuck");
		value = v;
	}
	public void check() throws CasualFaultException, CriticalFaultException
	{
			if (!getValue().equals(getValue()))
				throw new CriticalFaultException("Erratic" + sensorString);
			return;
	}
	public void setRandom()
	{
		isErratic = true;
	}
	public void clearRandom()
	{
		isErratic = false;
	}
	public void setStuck(boolean s)
	{
		isStuck = true;
		stuckValue = s;
	}
	public void clearStuck()
	{
		isStuck = false;
	}

}
