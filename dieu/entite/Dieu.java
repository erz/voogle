package dieu.entite;

import java.util.ArrayList;
import java.util.Observable;
import java.util.TreeMap;

import dieu.graphe.Graphe;

import libWarThreads.reseau.InfoModification;
import libWarThreads.reseau.InfoNoeud;


/**
 * Classe repr�sentant Dieu
 * @author Tony
 *
 */
public class Dieu extends Observable {
	
	/**
	 * La liste des informations des noeuds non encore actualis�s
	 */
	private ArrayList<InfoModification> infosModifications;
	
	/**
	 * Sous-couche r�seau
	 */
	private DieuReseau dieuReseau;
	
	private TreeMap<Integer, Integer> noeudsSurFideles;
	
	/**
	 * Le graphe
	 */
	private Graphe graphe;
	
	/**
	 * Constructeur
	 *
	 */
	public Dieu() {
		infosModifications = new ArrayList<InfoModification>();
		dieuReseau = new DieuReseau(this);
		graphe = new Graphe();
		noeudsSurFideles = new TreeMap<Integer, Integer>();
		actualiserGraphe();
	}
	
	public void implanterGraphe() {
		for (int i=0; i<graphe.getTaille(); i++)
			implanterUnNoeud(
					graphe.getNoeud(i).getInfo(),
					getNumeroFideleAleatoire()
					);
		
		while (graphe.getNbNoeudsConnectes() < graphe.getTaille());
		for (int i=0; i<graphe.getNbLiens(); i++)
			implanterUnLien(graphe.getLien(i).getNoeud1().getInfo(),
					graphe.getLien(i).getNoeud2().getInfo(), graphe.getLien(i).getDistance());
		System.out.println("Graphe implant�");
	}
	
	public void signalerDebutJeu() {
		dieuReseau.envoyerSignalDebutATous();
	}
	
	public void simulerPanneSurFidele(int numeroFidele) {
		dieuReseau.envoyerSignalPanne(numeroFidele);
	}
	
	/**
	 * Demande � une machine connect�e d'instancier un noeud
	 * @param numeroMachine l'id de la machine
	 * @param idNoeud l'id du noeud � implanter
	 */
	public void implanterUnNoeud(InfoNoeud infoNoeud, int numFidele) {
		noeudsSurFideles.put(infoNoeud.getIdentifiantNoeud(), numFidele);
		dieuReseau.envoyerUnNoeud(infoNoeud, numFidele);
	}
	
	/**
	 * Demande � deux noeuds implant�s sur des machines de cr�er un lien entre eux
	 *
	 */
	public void implanterUnLien(InfoNoeud infoNoeud1, InfoNoeud infoNoeud2, int distance) {
		dieuReseau.envoyerUnLien(infoNoeud1, infoNoeud2, noeudsSurFideles.get(infoNoeud1.getIdentifiantNoeud()), distance);
	}
	
	public void rechercherDesFideles(boolean demarrage) {
		if (demarrage)
			dieuReseau.scannerReseau();
		else
			dieuReseau.arreterScan();
	}
	
	public void attendreFideles() {
		dieuReseau.attendreMachines();
	}
	
	public void ajouterInfoModification(InfoModification info) {
		infosModifications.add(info);
	}
	
	public void actualiserGraphe() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						if (! infosModifications.isEmpty()) {
							graphe.actualiser(infosModifications.get(0));
							infosModifications.remove(0);
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	
	/**
	 * Demande aux observateurs de s'actualiser
	 * @param o
	 */
	public void notifierObserveurs(Object o) {
		setChanged();
		notifyObservers(o);
	}
	
	public int getNumeroFideleAleatoire() {
		return (int)(Math.random() * dieuReseau.getNombreInterfacesMachines());
	}
	
	public Graphe getGraphe() {
		return graphe;
	}
}
