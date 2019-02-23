package sk.upjs.ics;

public class Vrchol {
	private int id;
	private int comp;
	private int parent;
	
	public Vrchol(int id) {
		this.id = id;
		this.comp = id;
		this.parent = Integer.MAX_VALUE;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComp() {
		return comp;
	}

	public void setComp(int comp) {
		this.comp = comp;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
}
