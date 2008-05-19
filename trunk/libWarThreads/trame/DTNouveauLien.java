package libWarThreads.trame;

import libWarThreads.reseau.InfoNoeud;

@SuppressWarnings("serial")
public class DTNouveauLien extends DonneeTrame {

	private InfoNoeud infoNoeud1;
	private InfoNoeud infoNoeud2;
	private int distance;
	
	public DTNouveauLien(InfoNoeud noeud1, InfoNoeud noeud2, int distance) {
		infoNoeud1 = noeud1;
		infoNoeud2 = noeud2;
		this.distance = distance;
	}
	
	public InfoNoeud getInfoNoeud1() {
		return infoNoeud1;
	}
	
	public InfoNoeud getInfoNoeud2() {
		return infoNoeud2;
	}
	
	public int getDistance() {
		return distance;
	}

}
