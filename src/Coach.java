
public class Coach extends Staff implements Comparable<Coach> {

	
	public Coach(int id) {
		super.id = id;
	}


	@Override
	public int compareTo(Coach o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}


	@Override
	public String toString() {
		return "Coach [id=" + id + "]";
	}
	
	

}
