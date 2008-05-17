package libWarThreads;

import fidele.NoeudReseau;

public class DTPanneNoeud extends DonneeTrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int emetteur;
	private int enPanne;
	
	public DTPanneNoeud(int emet, int dest) {
		emetteur = emet;
		enPanne = dest;
	}
	
	public int getNoeudEmetteur() {
		return emetteur;
	}
	
	public int getIdNoeudPanne() {
		return enPanne;
	}
}
