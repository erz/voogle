package libWarThreads;

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
