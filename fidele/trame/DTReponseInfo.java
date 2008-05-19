package fidele.trame;

import libWarThreads.InfoNoeud;
import libWarThreads.trame.DonneeTrame;

@SuppressWarnings("serial")
public class DTReponseInfo extends DonneeTrame {
	
	private String nomDemandeur;
	private InfoNoeud infoReponse;
	
	public DTReponseInfo(String nomDemandeur, InfoNoeud infoReponse) {
		this.nomDemandeur = nomDemandeur;
		this.infoReponse = infoReponse;
	}
	
	public String getNomDemandeur() {
		return nomDemandeur;
	}
	
	public InfoNoeud getInfoReponse() {
		return infoReponse;
	}

}
