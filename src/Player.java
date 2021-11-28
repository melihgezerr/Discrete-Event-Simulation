import java.util.PriorityQueue;
import java.util.Queue;

public class Player {
	int id;
	int skillLevel;
	
	int numOfInvalid = 0;
	int numOfCancelled = 0;
	
	int numOfMassage = 0;
	double trainingTime;
	
	double enterQueueTime;
	double exitQueueTime;
	
	double spentTimePhysiotherapyQueue = 0;  //// FOR OUTPUT 11
	double spentTimeMassageQueue = 0;  //// FOR OUTPUT 12
	
	PriorityQueue<Event> currentQueue = null;
	Staff currentStaff = null;
	
//	public boolean isValid() { // checks whether a player can enter a queue. if not, it increments the invalid or cancelled attempts.
//		if (numOfMassage == 3) {
//			numOfInvalid++;
//			return false;
//		}
//		else if (currentQueue != null) {
//			numOfCancelled ++;
//			return false;
//		}
//		else
//			return true;
//	}
	
	
	public Player(int id, int skillLevel) {
		this.id = id;
		this.skillLevel = skillLevel;
	}


	@Override
	public String toString() {
		return "Player [id=" + id + ", skillLevel=" + skillLevel +"]";
	}
	
	
	

}
