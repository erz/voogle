package fidele.trame;

import libWarThreads.DonneeTrame;

@SuppressWarnings("serial")
public class DTDialogueMigration extends DonneeTrame {
	
	private String nomDemandeur;
	
	public DTDialogueMigration(String nomDemandeur) {
		this.nomDemandeur = nomDemandeur;
	}
	
	public String getNomDemandeur() {
		return nomDemandeur;
	}

}
