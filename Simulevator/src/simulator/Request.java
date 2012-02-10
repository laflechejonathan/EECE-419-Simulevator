package simulator;

/**
 * Primarily used by scheduler.
 * Signal on which floor a request is made.
 * Direction of this request.
 * @author Jimmy, Jonathan, Matthew
 *
 */
public class Request {
	public int requestFloor;
	public String direction; //UP; DOWN;

	public Request(int currentFloor, String direct) {
		requestFloor = currentFloor;
		direction = direct;
	}
	
	public String toString(){
		return("("+requestFloor+","+direction+")");
	}
}
