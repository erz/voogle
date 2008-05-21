package dieu.entite;

import java.util.ArrayList;
import java.util.Observable;
import java.util.TreeMap;
import java.util.Vector;

import dieu.graphe.Graphe;
import dieu.graphe.NoeudGraphe;

import libWarThreads.reseau.InfoModification;
import libWarThreads.reseau.InfoNoeud;


/**
 * Classe reprï¿½sentant Dieu
 *
 */
public class Dieu extends Observable 
{
	private class ControleFin extends Thread
	{
		long depart;
		long dureeJeu;
		boolean controle;
		// 0 = en cours, 1 = arreter a cause du temps, 2 = arreter par domination totale
		int etatJeu;
		
		ControleFin (long timeDep, long duree)
		{
			depart = timeDep;
			dureeJeu = duree;
			controle = true;
		}
		
		@Override
		public void run()
		{
			while (controle)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Recuperer les informations de tous les noeuds
				Vector <NoeudGraphe> vect = graphe.getVectNoeuds();
				Vector<Integer> NbPtsParCamp = new Vector<Integer> ();
				NbPtsParCamp.setSize(libWarThreads.general.ParametresGeneraux.nombreJoueurs);
				
				for(int i = 0; i < NbPtsParCamp.size(); ++i)
					NbPtsParCamp.set(i, 0);
				
				for (int i = 0; i < vect.size(); ++i)
				{
					if (vect.get(i).getInfo().getProprietaire() >= 0)
					{
						NbPtsParCamp.set(vect.get(i).getInfo().getProprietaire(), NbPtsParCamp.get(vect.get(i).getInfo().getProprietaire()) + 1);	
					}
				}
				
				for(int i = 0; i < NbPtsParCamp.size(); ++i)
				{
					if (NbPtsParCamp.get(i) == vect.size())
					{
						//Ce camp est gagnant, on s'arrete
						System.out.println("Le Camp " + i + " est le Gagnant !");
						etatJeu = 2;
					}
				}
				
				if (etatJeu == 0 && (depart+dureeJeu) <= System.currentTimeMillis())
				{
					//Le temps est écoulé, on arrete la partie
					System.out.println("Le temps est écoulé !");
					etatJeu = 1;
				}
				
				//Arret du jeu
				if (etatJeu != 0)
				{
					if (etatJeu == 1)
					{
						System.out.println("Au bout du temps réglementaire, les résultats sont :");
						for(int i = 0; i < NbPtsParCamp.size(); ++i)
							System.out.println("Le camp " + i + " contrôle " + NbPtsParCamp.get(i) + " noeud ! Félicitations !");
					}
					
					if (etatJeu == 2)
					{
						System.out.println("La carte a été entièrement conquise !");
					}
					
					//On arrete tous les fideles
					signalerFinJeu();
					controle = false;
				}
			}
		}
	}
	
	/**
	 * La liste des informations des noeuds non encore actualisï¿½s
	 */
	private ArrayList<InfoModification> infosModifications;
	
	/**
	 * Sous-couche rï¿½seau
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
		System.out.println("Graphe implante");
	}
	
	public void signalerDebutJeu() 
	{
		new ControleFin(System.currentTimeMillis(), 30000).start();
		dieuReseau.envoyerSignalDebutATous();
	}
	
	public void signalerFinJeu() {
		dieuReseau.envoyerSignalFinATous();
	}
	
	public void simulerPanneSurFidele(int numeroFidele) {
		dieuReseau.envoyerSignalPanne(numeroFidele);
	}
	
	/**
	 * Demande ï¿½ une machine connectï¿½e d'instancier un noeud
	 * @param numeroMachine l'id de la machine
	 * @param idNoeud l'id du noeud ï¿½ implanter
	 */
	public void implanterUnNoeud(InfoNoeud infoNoeud, int numFidele) {
		noeudsSurFideles.put(infoNoeud.getIdentifiantNoeud(), numFidele);
		dieuReseau.envoyerUnNoeud(infoNoeud, numFidele);
	}
	
	/**
	 * Demande ï¿½ deux noeuds implantï¿½s sur des machines de crï¿½er un lien entre eux
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
