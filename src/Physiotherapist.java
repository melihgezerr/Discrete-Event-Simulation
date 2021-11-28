
public class Physiotherapist extends Staff implements Comparable<Physiotherapist>{
	double serviceTime;
	
	public Physiotherapist(int id, double serviceTime) {
		super.id = id;
		this.serviceTime = serviceTime;
	}

	@Override
	public int compareTo(Physiotherapist o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}

	@Override
	public String toString() {
		return "Physiotherapist [id=" + id + ", serviceTime=" + serviceTime + "]";
	}
	
	
	

}
