package libWarThreads.trame;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Trame implements Serializable {
	
	public static final int NOUVEAU_NOEUD = 1;
	public static final int NOUVEAU_LIEN = 2;
	public static final int INFO_NOEUD = 3;
	public static final int MIGRATION = 4;
	public static final int DEBUT = 5;
	public static final int PANNE_FIDELE = 6;
	public static final int DEMANDE_INFO = 7;
	public static final int REPONSE_INFO = 8;
	public static final int DEMANDE_INFO_VOISIN = 9;
	public static final int REPONSE_INFO_VOISIN = 10;
	public static final int INFO_THREAD = 11;
	public static final int ACK = 12;
	public static final int PANNE_NOEUD = 13;
	public static final int DEMANDE_MIGRATION = 14;
	public static final int AUTORISATION_MIGRATION = 15;
	public static final int SIGNAL_VIE = 16;
	
	private int code;
	private DonneeTrame donnee;
	
	public Trame(int code, DonneeTrame donnee) {
		this.code = code;
		this.donnee = donnee;
	}
	
	public int getCode() {
		return code;
	}
	
	public DonneeTrame getDonnee() {
		return donnee;
	}

}
