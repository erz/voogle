package libWarThreads;

@SuppressWarnings("serial")
public class DTDemandeInfo extends DonneeTrame {
	
	private String nomDemandeur;
	
	public DTDemandeInfo(String nomDemandeur) {
		this.nomDemandeur = nomDemandeur;
	}
	
	public String getNomDemandeur() {
		return nomDemandeur;
	}

}
