package simulator;

public class CriticalFaultException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1795587357262341703L;

	public CriticalFaultException(String ex) {
		super("A sensor has failed:" + ex);
	}
}