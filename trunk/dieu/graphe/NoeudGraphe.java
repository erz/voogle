package dieu.graphe;

import java.util.ArrayList;
import java.util.Vector;

import libWarThreads.InfoNoeud;

public class NoeudGraphe 
{
	/**
	 * Les informations du noeud destinées à être envoyées à dieu
	 */
	private InfoNoeud info;

	/**
	 *	Coordonnes du noeud sur la carte
	 */
	private int xPos;
	private int yPos;
	
	

	/**
	 *  Vecteur des voisins du noeud
	 */
	private Vector <Integer> vectVoisins = null ;

	
	public NoeudGraphe (int identifiant)
	{
		info = new InfoNoeud(identifiant);
		vectVoisins = new Vector <Integer> ();
		
		//temporaire
		/*if (identifiant % 5 == 0)
		{
			info.setBase(true);
			info.setProprietaire(0);
		}
		*/
	}
	
	public InfoNoeud getInfo() {
		return info;
	}
	
	public void setInfos(InfoNoeud info) {
		this.info = info;
	}
	
	public int getIdNoeud ()
	{
		return info.getIdentifiantNoeud();
	}
	
	
	public int getXPos ()
	{
		return xPos ;
	}
	
	public int getYPos ()
	{
		return yPos ;
	}
	
	public void setXPos (int x)
	{
		xPos = x ;
	}
	
	public void setYPos (int y)
	{
		yPos = y ;
	}
	
	public int getNbrVoisins ()
	{
		return vectVoisins.size();
	}
	
	
	public void ajouterVoisin (int nouveauVoisin)
	{
		/**
		 * Si le voisin existe déja, on ne l'ajoute pas
		 */
		int i;
		for(i=0;i<vectVoisins.size() && 
			nouveauVoisin != vectVoisins.get(i);++i) ;
		
		if (i == vectVoisins.size()) vectVoisins.add(nouveauVoisin);
	}
	
	public void afficherInfos ()
	{
		System.out.println("Noeud id = " + getIdNoeud() + " a pour coordonnées (" + xPos + "," + yPos + ")");
		
		if (vectVoisins.size() == 0)
		{
			System.out.println("Aucun voisins ...");
			return ;
		}
		System.out.print("Liste des voisins :");
		System.out.print("[");
		int i;
		for(i=0;i<vectVoisins.size()-1;++i)
		{
			System.out.print(vectVoisins.get(i)+",");
		}
		System.out.println(vectVoisins.get(i)+"]");
	}
	
	public Vector<Integer> getVectVoisin(){
		return vectVoisins;
	}
	
	/**
	 *  Cre un thread surveillant  intervalle rgulier les occupants du noeud
	 */
	
	//Lancer un dé avec un résultat entre 1 et 100
	public int lancerDe()
	{	
	 	return (int)(100*Math.random());
	}
	
	
	
}
