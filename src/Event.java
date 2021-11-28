
public class Event implements Comparable<Event>{
	String type;
	Player player;
	double eventTime;
	double duration;
	int orderNumber;
	
	
	public Event(String type, Player player, double eventTime, double duration) {
		this.type = type;
		this.player = player;
		this.eventTime = eventTime;
		this.duration = duration;
		
		if (type.equals("pe"))
			this.orderNumber = 0;
		else if (type.equals("tp"))
			this.orderNumber = 1;
		else if (type.equals("me"))
			this.orderNumber = 2;
		else if (type.equals("t"))
			this.orderNumber = 3;
		else if (type.equals("m"))
			this.orderNumber = 4;
	}
	
	
	
	public Event(String type, Player player, double eventTime) {
		this.type = type;
		this.player = player;
		this.eventTime = eventTime;
		if (type.equals("pe"))
			this.orderNumber = 0;
		else if (type.equals("tp"))
			this.orderNumber = 1;
		else if (type.equals("me"))
			this.orderNumber = 2;
		else if (type.equals("t"))
			this.orderNumber = 3;
		else if (type.equals("m"))
			this.orderNumber = 4;
	}

	public boolean isValid() { // checks whether a player can enter a queue. if not, it increments the invalid or cancelled attempts.
		if (type.equals("m") && player.numOfMassage == 3) {
			player.numOfInvalid++;
			return false;
		}
		else if (player.currentQueue != null && (type.equals("t") || type.equals("m"))) {
			player.numOfCancelled ++;
			return false;
		}
		else
			return true;
	}
	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		if (!(Math.abs(eventTime - o.eventTime) < 0.00000001)) {
			if (this.eventTime > o.eventTime)
				return 1;
			else
				return -1;
		}
		else if (this.orderNumber - o.orderNumber != 0)
			return this.orderNumber - o.orderNumber;
		
		else if (this.type.equals("t")) {
			return this.player.id - o.player.id;
		}
		else if (this.type.equals("tp")) {
			if (!(Math.abs(this.player.trainingTime - o.player.trainingTime) < 0.0000000001)) {
				if (o.player.trainingTime > this.player.trainingTime)
					return 1;
				else
					return -1;
			}
			else
				return this.player.id - o.player.id;
		}
		else if (this.type.equals("m")) {
			if (this.player.skillLevel != o.player.skillLevel)
				return o.player.skillLevel - this.player.skillLevel;
			else
				return this.player.id - o.player.id;
		}
		else
			return this.player.currentStaff.id - o.player.currentStaff.id;
		
	}
	@Override
	public String toString() {
		return "Event [type=" + type + ", orderNumber=" + orderNumber + ", player=" + player + ", eventTime="
				+ eventTime + ", duration=" + duration + "]";
	}


	
	
	
	
}
