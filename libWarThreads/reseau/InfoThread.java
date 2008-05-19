package libWarThreads.reseau;

import fidele.entite.Warrior;

@SuppressWarnings("serial")
public class InfoThread extends InfoModification {
	
	private String nom;
	private int proprietaire;
	private int etat;
	private int idNoeudDepart;
	private int idNoeudArrivee;
	private int distanceInterNoeuds;
	private int nombreDeplacements;
	
	public InfoThread(Warrior w) {
		nom = w.getNom();
		etat = w.getEtat();
		proprietaire = w.getProprietaire();
		idNoeudDepart = w.getIdNoeudCourant();
		idNoeudArrivee = w.getIdNoeudDestination();
		distanceInterNoeuds = w.getDistanceAParcourir();
		nombreDeplacements = w.getNombreDeplacements();
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
