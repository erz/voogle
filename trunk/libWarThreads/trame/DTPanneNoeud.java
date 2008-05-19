package libWarThreads.trame;

@SuppressWarnings("serial")
public class DTPanneNoeud extends DonneeTrame{	
	private int enPanne;
	
	public DTPanneNoeud(int idNoeudEnPanne) {
		enPanne = idNoeudEnPanne;
	}
	
	public int getIdNoeudPanne() {
		return enPanne;
	}
}
