package dieu.graphe;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

import libWarThreads.reseau.InfoModification;
import libWarThreads.reseau.InfoNoeud;
import libWarThreads.reseau.InfoThread;

public class Graphe extends Observable
{
	private Vector <NoeudGraphe> vectNoeuds;
	
	private ArrayList<LienGraphe> liens;
	
	public Graphe() {
		vectNoeuds = new Vector<NoeudGraphe>();
		liens = new ArrayList<LienGraphe>();
	}
	
	public void construire() {
		GrapheAlgoSylv grapheAlgo = new GrapheAlgoSylv (this);
		vectNoeuds = grapheAlgo.getVectNoeud();
	}

	private void verifierParametres ()
	{
		//A VERIFIER
		//Nombre de lignes * Nombre de colonnes >= Nombre de noeuds
		//Nombre d'arrètes par noeud >= 2
		//Nombre d'arrètes par noeud < Nombre de noeuds -1
	}
	

	
	public void afficherGraphe ()
	{
		for(int i=0;i<vectNoeuds.size();++i)
		{
			vectNoeuds.get(i).afficherInfos() ;
		}
	}
	
	public void actualiser(InfoModification info) {
		if (info instanceof InfoNoeud)
			vectNoeuds.get(((InfoNoeud)info).getIdentifiantNoeud()).setInfos((InfoNoeud)info);
		else
		{
			if (info instanceof InfoThread) {
				
			}
		}
		
		setChanged();
		notifyObservers(info);
		
	}
	
	public void ajouterNoeud(NoeudGraphe noeud) {
		vectNoeuds.add(noeud);
	}
	
	public void ajouterLien(LienGraphe lien) {
		liens.add(lien);
	}
	
	public NoeudGraphe getNoeud(int i)
	{	
		return vectNoeuds.get(i);
	}
	
	public LienGraphe getLien(int i) {
		return liens.get(i);
	}
	
	public int getTaille() {
		return vectNoeuds.size();
	}
	
	public int getNbLiens() {
		return liens.size();
	}
	
	public int getNbNoeudsConnectés() {
		int nbNoeudsConnectés = 0;
		for (int i=0; i<getTaille(); i++)
			if (getNoeud(i).getInfo().isConnecte())
				nbNoeudsConnectés ++;
		return nbNoeudsConnectés;
	}
	
	
}
