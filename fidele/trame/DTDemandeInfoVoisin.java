package fidele.trame;

import libWarThreads.trame.DonneeTrame;

@SuppressWarnings("serial")
public class DTDemandeInfoVoisin extends DonneeTrame {
	
	private int idDemandeur;
	
	public DTDemandeInfoVoisin(int id) {
		idDemandeur = id;
	}
	
	public int getNomDemandeur() {
		return idDemandeur;
	}

}
