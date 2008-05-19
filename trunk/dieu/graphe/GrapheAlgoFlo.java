package dieu.graphe;

import java.util.ArrayList;
import java.util.Vector;

import libWarThreads.general.ParametresGeneraux;

public class GrapheAlgoFlo 
{
	private ArrayList <Coordonnees> matcoord = null;		
	private Vector <NoeudGraphe> vectNoeuds = null ; 
	
	public GrapheAlgoFlo (ParametresGeneraux parametresGraphes)
	{
		vectNoeuds = new Vector <NoeudGraphe> ();
    	matcoord = new ArrayList<Coordonnees>();
    	
		construireGraphe(parametresGraphes.nbrNoeuds);
		decouper();
		ajoutCoordNoeud();
	}

	private void construireGraphe (int nbrNoeuds)
	{
		Vector <NoeudGraphe> vectNoeudsDeconnectes = new Vector <NoeudGraphe> (nbrNoeuds);
		int i;
		for(i=0;i<nbrNoeuds;++i)
		{
			vectNoeudsDeconnectes.add(new NoeudGraphe(i)); // remplacer par la classe NoeudGraphe la classe NoeudGraphe ;-)
		}

		vectNoeuds.add(vectNoeudsDeconnectes.get(0));
		vectNoeudsDeconnectes.remove(0);
		
		while (vectNoeuds.size() != nbrNoeuds)
		{
			/**
			 * On choisit au hasard, un noeuds dj connect et un noeud dconnect
			 */
			int indVectNoeudsDeconnectes = (int)(Math.random()*vectNoeudsDeconnectes.size()-1);
			int indVectNoeudsConnectes = (int)(Math.random()*vectNoeuds.size());
			
			/**
			 * Ajout de l'arrte entre les deux noeuds
			 */
			int idNoeudDeconnecte =	vectNoeudsDeconnectes.get(indVectNoeudsDeconnectes).getIdNoeud();
			int idNoeudConnecte = vectNoeuds.get(indVectNoeudsConnectes).getIdNoeud();
			vectNoeudsDeconnectes.get(indVectNoeudsDeconnectes).ajouterVoisin(idNoeudConnecte);
			vectNoeuds.get(indVectNoeudsConnectes).ajouterVoisin(idNoeudDeconnecte);

			/**
			 * Le noeud dconnect devient connect
			 */
			vectNoeuds.add(vectNoeudsDeconnectes.get(indVectNoeudsDeconnectes));
			vectNoeudsDeconnectes.remove(indVectNoeudsDeconnectes);
		}
	}
	private void decouper()
	{	
    	int taillemat = vectNoeuds.size();
	
    	for(int i=0;i<taillemat/2;i++)
    	{
    		for(int j=0;j<taillemat/2;j++)
    		{
    			Coordonnees coordcell = new Coordonnees((1000/(taillemat/2))*j,(1000/(taillemat/2))*i+30);
    			matcoord.add(coordcell);
    		}
    	}
    }

	private void ajoutCoordNoeud()
	{
		int nbrSommet = vectNoeuds.size();
    	int numCell = 0;
    	int espace = ((nbrSommet/2)*(nbrSommet/2))/nbrSommet;
    	
    	for(int i=0;i<nbrSommet;i++)
    	{
    		numCell += ((int)(Math.random()*(espace+1-1))+2);
    		vectNoeuds.get(i).setXPos(matcoord.get(numCell).X);
    		vectNoeuds.get(i).setYPos(matcoord.get(numCell).Y);	
    	}
	}

	public Vector <NoeudGraphe> getVectNoeud()
	{	
		return vectNoeuds;
	}
}