package sk.upjs.ics;

public class Hrana {
	private Vrchol v1;
	private Vrchol v2;
	
	public Hrana(Vrchol v1, Vrchol v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public Vrchol getV1() {
		return v1;
	}

	public void setV1(Vrchol v1) {
		this.v1 = v1;
	}

	public Vrchol getV2() {
		return v2;
	}

	public void setV2(Vrchol v2) {
		this.v2 = v2;
	}	
}
