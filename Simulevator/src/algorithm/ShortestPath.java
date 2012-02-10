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
public class ShortestPath extends Algorithm {


	public ShortestPath(String algorithm_name) {
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
		sortElevatorQueue(ECL);
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
		
		// Count the path
		ElevatorController chosenOne = null;
		long minPath = 100000;
		for (ElevatorController e : elevators)
		{
			if (e.isActive())
			{
				int pathLen = 0;
				int curr = (int)e.getFloor();
				for (int i = 0; i < e.getQueue().size();i++)
				{
					int x = e.getQueue().get(i);
					pathLen+= Math.abs(curr-x);
					curr = x;
				}
				pathLen+= Math.abs(curr-extRequest.requestFloor);
				if (pathLen < minPath)
				{
					chosenOne = e;
					minPath = pathLen;
				}
			}
		}
		
		if (chosenOne !=null) {
			addExternal(chosenOne, extRequest);
			return -1;
		}
		return -1;

	}
	private void addExternal(ElevatorController chosen, Request exReq)
	{
		
		// When adding External Requests,sort according to current direction
		chosen.getExternal().add(exReq);
		chosen.getQueue().add(exReq.requestFloor);
		sortElevatorQueue(chosen);
	}

	@Override
	public int onEventError() {
		// TODO Auto-generated method stub
		return 0;
	}
}
