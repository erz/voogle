package dieu.graphe;

public class LienGraphe {
	
	private NoeudGraphe noeud1;
	private NoeudGraphe noeud2;
	private int distance;
	
	public LienGraphe(NoeudGraphe noeud1, NoeudGraphe noeud2, int distance) {
		this.noeud1 = noeud1;
		this.noeud2 = noeud2;
		this.distance = distance;
	}
	
	public NoeudGraphe getNoeud1() {
		return noeud1;
	}
	
	public NoeudGraphe getNoeud2() {
		return noeud2;
	}
	
	public int getDistance() {
		return distance;
	}

}
