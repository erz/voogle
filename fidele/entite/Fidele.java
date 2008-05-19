package fidele.entite;

import java.util.TreeMap;

import libWarThreads.Console;
import libWarThreads.InfoNoeud;
import libWarThreads.InfoThread;

import fidele.FideleReseau;
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
	 * Construit un nouveau noeud sur ce fidèle
	 * @param info les infos servant à construire le noeud
	 */
	public void instancierNoeud(InfoNoeud info) {
		if (info.isBase())
			noeudsHebergs.put(info.getIdentifiantNoeud(), new NoeudBase(info, this));
		else
			noeudsHebergs.put(info.getIdentifiantNoeud(), new Noeud(info, this));
	}
	
	/**
	 * Crée un lien entre deux noeuds
	 * @param infoNoeud1 le noeud qui va se connecter en tant que client
	 * @param infoNoeud2 le noeud qui va recevoir une demande de connexion du noeud 1
	 * @param distance la distance entre les deux noeuds
	 */
	public void creerUnLien(InfoNoeud infoNoeud1, InfoNoeud infoNoeud2, int distance) {
		noeudsHebergs.get(infoNoeud1.getIdentifiantNoeud()).connecterAUnNoeud(infoNoeud2, distance);
	}
	
	/**
	 * Envoie à Dieu les infos passées en paramètres
	 * @param info l'info du noeud
	 */
	public void envoyerInfoNoeudADieu(InfoNoeud info) {
		fideleReseau.envoyerInfoNoeud(info);
	}
	
	/**
	 * Envoie à Dieu les infos concernant un Thread (et non le thread lui même !)
	 * @param info l'info du thread
	 */
	public void envoyerInfoThreadADieu(InfoThread info) {
		fideleReseau.envoyerInfoThread(info);
	}
	
	public void notifierPanneNoeud(int idNoeudEnPanne) {
		fideleReseau.envoyerNotificationPanneADieu(idNoeudEnPanne);
	}
	
	/**
	 * Mets tous les noeuds du fidèle en action (pondaison pour les bases)
	 * Fonction appelée une fois que le graphe a été implanté
	 */
	public void mettreLesNoeudsEnAction() {
		for (int i=0; i<noeudsHebergs.size(); i++)
			noeudsHebergs.get(i).mettreEnAction();
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
	 * @return la couche réseau
	 */
	public FideleReseau getFideleReseau() {
		return fideleReseau;
	}	

	/**
	 * @return la liste des noeuds instanciés sur ce fidèle sous la forme (idNoeud, Noeud)
	 */
	public TreeMap<Integer, Noeud> getNoeudsHebergs() {
		return noeudsHebergs;
	}
}
