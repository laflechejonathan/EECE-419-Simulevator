package network;
import simulator.SimulatorController;


public class Decoder {
    public static boolean decode(SimulatorController pSC, String cmd){
    	if(pSC != null){
	    	System.out.println(cmd);
	    	String[] cmdSplit = cmd.split("/");
			if(cmdSplit[0].equals("C")){
				switch (cmdSplit[1].charAt(0)) {
				case 'F':
					if(cmdSplit.length > 5){
						return false;
					}
					switch(Integer.parseInt(cmdSplit[2])){
					case 0:
						pSC.triggerBuildingEarthquake(); break;
					case 1:
						pSC.triggerBuildingOnFire(); break;
					case 2:
						pSC.triggerBuildingLockDown(); break;
					case 3:
						pSC.setElevatorSensorStuck(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "weight"); break;
					case 4:
						pSC.setElevatorSensorRBias(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "weight"); break;
					case 5:
						pSC.setElevatorSensorCBias(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "weight"); break;
					case 6:
						pSC.setElevatorSensorStuck(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "speed"); break;
					case 7:
						pSC.setElevatorSensorRBias(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "speed"); break;
					case 8:
						pSC.setElevatorSensorCBias(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "speed"); break;
					case 9:
						pSC.setElevatorSensorStuck(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "position"); break;
					case 10:
						pSC.setElevatorSensorRBias(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "position"); break;
					case 11:
						pSC.setElevatorSensorCBias(Integer.parseInt(cmdSplit[4])-1, Integer.parseInt(cmdSplit[3]), "position"); break;
					case 12:
						pSC.setElevatorDoorSensorStuck(Integer.parseInt(cmdSplit[3])-1, true); break;
					case 13:
						pSC.setelevatorDoorSensorRand(Integer.parseInt(cmdSplit[3])-1); break;
					case 14:
						pSC.triggerZombieInfestation();
					case 15:
						pSC.triggerBuildingEvacuation();
					}

					break;
				case 'A':
					if(cmdSplit.length != 3){
						return false;
					}
					pSC.changeAlgorithm(cmdSplit[2]);
					//change algorithm
					break;
				case 'R':
					if(cmdSplit.length != 4){
						return false;
					}
					pSC.addPassenger(Integer.parseInt(cmdSplit[2]), Integer.parseInt(cmdSplit[3]));
					//request
					break;
				case 'E':
					if(cmdSplit.length != 4){
						return false;
					}
					//lock/unlcok
					if(cmdSplit[3].equals("L"))
						pSC.lockOutFloor(Integer.parseInt(cmdSplit[2]));
					else if(cmdSplit[3].equals("U"))
						pSC.unlockOutFloor(Integer.parseInt(cmdSplit[2]));
						
					break;
				case 'C':
					if(cmdSplit.length != 3){
						return false;
					}
					pSC.unlockElevator(Integer.parseInt(cmdSplit[2])-1);
					break;
				case 'S':
					if(cmdSplit[2].equals("P"))
						pSC.pauseSimulation();
					else if(cmdSplit[2].equals("R"))
						pSC.resumeSimulation();
				default:
					return false;
			}
			}else{
				return false;
			}
			return true;
	    }
        return false;
    }
}
