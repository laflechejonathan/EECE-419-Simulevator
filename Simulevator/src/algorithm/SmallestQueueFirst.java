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
public class SmallestQueueFirst extends Algorithm {


	public SmallestQueueFirst(String algorithm_name) {
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

		int direction;
		if (extRequest.direction.equals("UP"))
			direction = 1;
		else
			direction = -1;

		// Is there an elevator going there already?
		for (int i = 0; i < elevators.size(); i++)
			if (elevators.get(i).isActive()) {
				for (int j = 0; j < elevators.get(i).getQueue().size(); j++)
					if (elevators.get(i).getQueue().get(j).intValue() == extRequest.requestFloor
							&& j < (elevators.get(i).getQueue().size() - 1))
						if ((elevators.get(i).getQueue().get(j + 1).intValue() - elevators
								.get(i).getQueue().get(j).intValue())
								* direction > 0){
							elevators.get(i).getExternal().add(extRequest);
							return i;
						}
			}
		// Is there a bored elevator?
		int chosenOne = -1;
		int minQueueSize = 1000;
		for (int i = 0; i < elevators.size(); i++) {
			if (elevators.get(i).getQueue().size() < minQueueSize && elevators.get(i).isActive()) {
				minQueueSize = elevators.get(i).getQueue().size();
				chosenOne = i;
			}
		}
		
		if (chosenOne >= 0) {
			addExternal(elevators.get(chosenOne), extRequest);
			return -1;
		}

/*		for (int i = 0; i < elevators.size(); i++)
			if (!elevators.get(i).getDirection().equals(extRequest.direction) && elevators.get(i).isActive()) {
				addExternal(elevators.get(i), extRequest);
				break;
			}
*/	
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
