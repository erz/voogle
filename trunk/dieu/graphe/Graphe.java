package dieu.graphe;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

import libWarThreads.InfoModification;
import libWarThreads.InfoNoeud;
import libWarThreads.InfoThread;
import libWarThreads.ParametresGeneraux;

import fidele.entite.Noeud;

import dieu.ihm.JFrameDieu;
import dieu.ihm.JPanelVueJeu;

public class Graphe extends Observable
{
	private Vector <NoeudGraphe> vectNoeuds;
	
	private ArrayList<Lien> liens;
	
	public Graphe() {
		vectNoeuds = new Vector<NoeudGraphe>();
		liens = new ArrayList<Lien>();
	}
	
	public void initialiser() {
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
	
	public void ajouterLien(Lien lien) {
		liens.add(lien);
	}
	
	public NoeudGraphe getNoeud(int i)
	{	
		return vectNoeuds.get(i);
	}
	
	public Lien getLien(int i) {
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
