package simulator;

public class CasualFaultException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1795587357292341703L;

	public CasualFaultException(String ex) {
		super("A sensor has failed:" + ex);
	}
}
