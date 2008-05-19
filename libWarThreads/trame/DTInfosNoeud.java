package libWarThreads.trame;

import libWarThreads.reseau.InfoNoeud;

@SuppressWarnings("serial")
public class DTInfosNoeud extends DonneeTrame {
	
	private InfoNoeud infoNoeud;
	
	public DTInfosNoeud(InfoNoeud info) {
		this.infoNoeud = info;
	}
	
	public InfoNoeud getInfoNoeud() {
		return infoNoeud;
	}
}
