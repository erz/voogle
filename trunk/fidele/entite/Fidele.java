package fidele.entite;

import java.util.ArrayList;
import java.util.TreeMap;

import libWarThreads.reseau.InfoNoeud;
import libWarThreads.reseau.InfoThread;
import libWarThreads.util.Console;

import fidele.ia.*;

public class Fidele {
	
	private TreeMap<Integer, Noeud> noeudsHebergs;
	private FideleReseau fideleReseau;
	
	public Fidele() {
		fideleReseau = new FideleReseau(this);
		noeudsHebergs = new TreeMap<Integer, Noeud>();
		
		//Initialisation IA/Joueur
		initIA();
	}
	
	/**
	 * Construit un nouveau noeud sur ce fid�le
	 * @param info les infos servant � construire le noeud
	 */
	public void instancierNoeud(InfoNoeud info) {
		if (info.isBase())
			noeudsHebergs.put(info.getIdentifiantNoeud(), new NoeudBase(info, this));
		else
			noeudsHebergs.put(info.getIdentifiantNoeud(), new Noeud(info, this));
	}
	
	/**
	 * Cr�e un lien entre deux noeuds
	 * @param infoNoeud1 le noeud qui va se connecter en tant que client
	 * @param infoNoeud2 le noeud qui va recevoir une demande de connexion du noeud 1
	 * @param distance la distance entre les deux noeuds
	 */
	public void creerUnLien(InfoNoeud infoNoeud1, InfoNoeud infoNoeud2, int distance) {
		noeudsHebergs.get(infoNoeud1.getIdentifiantNoeud()).connecterAUnNoeud(infoNoeud2, distance);
	}
	
	/**
	 * Envoie � Dieu les infos pass�es en param�tres
	 * @param info l'info du noeud
	 */
	public void envoyerInfoNoeudADieu(InfoNoeud info) {
		fideleReseau.envoyerInfoNoeud(info);
	}
	
	/**
	 * Envoie � Dieu les infos concernant un Thread (et non le thread lui m�me !)
	 * @param info l'info du thread
	 */
	public void envoyerInfoThreadADieu(InfoThread info) {
		fideleReseau.envoyerInfoThread(info);
	}
	
	public void notifierPanneNoeud(int idNoeudEnPanne) {
		fideleReseau.envoyerNotificationPanneADieu(idNoeudEnPanne);
	}
	
	/**
	 * Mets tous les noeuds du fid�le en action (pondaison pour les bases)
	 * Fonction appel�e une fois que le graphe a �t� implant�
	 */
	public void mettreLesNoeudsEnAction() {
		ArrayList<Noeud> listeNoeuds = new ArrayList<Noeud>(noeudsHebergs.values());
		for (int i=0; i<listeNoeuds.size(); i++)
			listeNoeuds.get(i).mettreEnAction();
	}
	
	/**
	 * Arrete tous les threads afin de terminer le jeu.
	 */
	public void stopperTout()  
	{
		ArrayList<Noeud> listeNoeuds = new ArrayList<Noeud>(noeudsHebergs.values());
		for (int i=0; i<listeNoeuds.size(); i++)
			listeNoeuds.get(i).stopperNoeud();
	}
	
	/**
	 * Initialise l'IA
	 */
	public void initIA () {
		//Temporaire : pour chaque joueur on lui attribue une IA
		Cerveau.setCerveau(0, new CerveauJ1((float) 0.25));
		Cerveau.setCerveau(1, new CerveauJ1((float) 0.75));
	}
	
	/**
	 * @return la couche r�seau
	 */
	public FideleReseau getFideleReseau() {
		return fideleReseau;
	}	

	/**
	 * @return la liste des noeuds instanci�s sur ce fid�le sous la forme (idNoeud, Noeud)
	 */
	public TreeMap<Integer, Noeud> getNoeudsHebergs() {
		return noeudsHebergs;
	}
}
