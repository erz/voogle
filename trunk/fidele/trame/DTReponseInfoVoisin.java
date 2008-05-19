package fidele.trame;

import libWarThreads.trame.DonneeTrame;

@SuppressWarnings("serial")
public class DTReponseInfoVoisin extends DonneeTrame {
	
	private int idVoisin;
	private int distanceAuVoisin;
	
	public DTReponseInfoVoisin(int idV, int distance) {
		idVoisin = idV;
		distanceAuVoisin = distance;
	}
	
	public int getNomVoisin() {
		return idVoisin;
	}
	
	public int getDistanceAuVoisin() {
		return distanceAuVoisin;
	}

}
