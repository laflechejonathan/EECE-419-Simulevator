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
public class Selfish extends Algorithm {


	public Selfish(String algorithm_name ) {
		super(algorithm_name);
		// TODO Auto-generated constructor stub
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

		@SuppressWarnings("unused")
		int direction;
		if (extRequest.direction.equals("UP"))
			direction = 1;
		else
			direction = -1;
		int chosenOne = -1;
		double closest = 1000;
		for (int i = 0; i < elevators.size(); i++) {
			if (elevators.get(i).getQueue().isEmpty() && elevators.get(i).getFloor()- extRequest.requestFloor < closest)
			{
				closest = elevators.get(i).getFloor()- extRequest.requestFloor;
				chosenOne = i;
			}
		}
		addExternal(elevators.get(chosenOne), extRequest);
		return -1;
	}
		
	private void addExternal(ElevatorController chosen, Request exReq)
	{
		chosen.getQueue().add(exReq.requestFloor);
		chosen.getExternal().add(exReq);
	}

	@Override
	public int onEventError() {
		// TODO Auto-generated method stub
		return 0;
	}

}
