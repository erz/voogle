package libWarThreads.reseau;

@SuppressWarnings("serial")
public class InfoNoeud extends InfoModification {

	private String ip;
	private int port;
	private int identifiantFidele;
	private int identifiantNoeud;
	
	private boolean connecte;
	
	/**
	 *  A qui appartient le noeud ? 0 = neutre, 1..n = joueur i
	 */
	private int proprietaire;
	
	private boolean base;
	
	private int[] proprietairesThreads;
	
	private int frequencePondaison = 10;
	
	public InfoNoeud(int id, boolean connect, int proprio, int nbThreads, int[] propriosThreads, String IP, int por) {
		identifiantNoeud = id;
		connecte = connect;
		proprietaire = proprio;
		proprietairesThreads = new int[nbThreads];
		System.arraycopy(propriosThreads, 0, proprietairesThreads, 0, nbThreads);
		ip = IP;
		port = por;
	}
	
	public InfoNoeud(int identifiant) {
		this.identifiantNoeud = identifiant;
		connecte = false;
		proprietaire = -1;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public int getIdentifiantFidele() {
		return identifiantFidele;
	}
	
	public int getIdentifiantNoeud() {
		return identifiantNoeud;
	}
	
	public int getProprietaire() {
		return proprietaire;
	}
	
	public boolean isBase() {
		return base;
	}
	
	public int getFrequencePondaison() {
		return frequencePondaison;
	}
	
	public int[] getProprietairesThreads() {
		return proprietairesThreads;
	}
	
	
	
	public boolean isConnecte() {
		return connecte;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setIdentifiantFidele(int identifiant) {
		identifiantFidele = identifiant;
	}
	
	public void setProprietaire(int appartenance) {
		this.proprietaire = appartenance;
	}
	
	public void setConnecte(boolean c) {
		connecte = c;
	}
	
	public void setBase(boolean b) {
		base = b;
	}
	
	public void setFrequencePondaison(int fp) {
		frequencePondaison = fp;
	}
	
	public String toString() {
		return ip + "/" + port;
	}
}
