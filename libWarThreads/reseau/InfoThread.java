package libWarThreads.reseau;

@SuppressWarnings("serial")
public class InfoThread extends InfoModification {
	
	private String nom;
	private int proprietaire;
	private int etat;
	private int idNoeudDepart;
	private int idNoeudArrivee;
	private int distanceInterNoeuds;
	private int nombreDeplacements;
	private boolean attendreRenfort;
	
	public InfoThread(String n, int e, int p, int idND, int idNA, int d, int nD,boolean ar) {
		nom = n;
		etat = e;
		proprietaire = p;
		idNoeudDepart = idND;
		idNoeudArrivee = idNA;
		distanceInterNoeuds = d;
		nombreDeplacements = nD;
		attendreRenfort = ar;
	}
	
	public String getNom() {
		return nom;
	}
	
	public int getProprietaire() {
		return proprietaire;
	}
	
	public int getEtat() {
		return etat;
	}
	
	public int getIdNoeudDepart() {
		return idNoeudDepart;
	}
	
	public int getIdNoeudArrivee() {
		return idNoeudArrivee;
	}
	
	public int getDistance() {
		return distanceInterNoeuds;
	}
	
	public String toString() {
		return nom+"("+etat+") : "+idNoeudDepart+" -> "+idNoeudArrivee+", d = "+distanceInterNoeuds+" nb = "+nombreDeplacements;
	}

}
