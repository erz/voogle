package libWarThreads;

@SuppressWarnings("serial")
public class DTNouveauNoeud extends DonneeTrame {
	
	private InfoNoeud info;
	
	public DTNouveauNoeud(InfoNoeud info) {
		this.info = info;
	}
	
	public InfoNoeud getInfo() {
		return info;
	}

}
