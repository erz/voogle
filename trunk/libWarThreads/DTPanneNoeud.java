package libWarThreads;

import fidele.NoeudReseau;

public class DTPanneNoeud extends DonneeTrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NoeudReseau emetteur;
	private int enPanne;
	
	public DTPanneNoeud(NoeudReseau emet, int dest) {
		emetteur = emet;
		enPanne = dest;
	}
	
	public NoeudReseau getNoeudEmetteur() {
		return emetteur;
	}
	
	public int getIdNoeudPanne() {
		return enPanne;
	}
}
