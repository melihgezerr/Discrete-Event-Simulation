

public class Masseur extends Staff implements Comparable<Masseur>{

	public Masseur(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(Masseur o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}

	@Override
	public String toString() {
		return "Masseur [id=" + id + "]";
	}
	
	
	

}
