package algorithm;

import java.util.ArrayList;

import simulator.ElevatorController;
import simulator.Request;

/**
 * Description: External Request: Check if an elevator is already going to a
 * certain floor if so do nothing. Only add request to elevator queue if request
 * direction is opposite of elevator. Internal Request: If not already going to
 * the specified floor. Add internal request to the end of elevator queue.
 * 
 * @author Jimmy, Jonathan, Matthew
 * 
 */
public class ContextDependent extends Algorithm {


	private Stochastic stoch;
	private SmallestQueueFirst queue;
	private ClosestFirst closest;
	public ContextDependent(String algorithm_name) {
		super(algorithm_name);
		stoch = new Stochastic(algorithm_name);
		queue = new SmallestQueueFirst(algorithm_name);
		closest = new ClosestFirst(algorithm_name);
	}

	@Override
	public int onEventInternalRequest(ElevatorController ECL,
			ArrayList<Integer> internalRequests) {
		boolean exists = false;
		for (Integer intRequest : internalRequests) {
			int request = intRequest.intValue();
			exists = false;
			for (Integer eleQueue : ECL.getQueue()) {
				if (eleQueue.intValue() == request) {
					exists = true;
					break;
				}
			}
			if (exists)
				continue;
			else
				ECL.getQueue().add(request);
		}
		return 0;
	}

	@Override
	synchronized public int onEventExternalRequest(ArrayList<ElevatorController> elevators,
			Request extRequest) {
		//count total number of elevator requests
		int sum = 0;
		for (ElevatorController e : elevators)
			sum += e.getExternal().size();
				
		if (sum < 10)
			return stoch.onEventExternalRequest(elevators, extRequest);
		else if (sum < 30)
			return closest.onEventExternalRequest(elevators, extRequest);

		return queue.onEventExternalRequest(elevators, extRequest);

	}

	@Override
	public int onEventError() {
		// TODO Auto-generated method stub
		return 0;
	}
}