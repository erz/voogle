package fidele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import fidele.entite.Warrior;

import libWarThreads.InfoNoeud;

/**
 * Tactique actuelle du thread
 * 
 */
public class TactiqueWarrior implements Serializable
{
	private static final long serialVersionUID = -1165846305731503942L;
	
	public static final int TACTIQUE_INDETERMINEE = -1;
	public static final int TACTIQUE_OFFENSIVE = 0;
	public static final int TACTIQUE_DEFENSIVE = 1;
	public static final int TACTIQUE_EXPLORATION = 2;
	
	
	private int tactique ;
	private ArrayList <Integer> cheminAParcourir ;
	private int idCible;
	private Warrior guerrier;
	private TreeMap <Integer,Integer> risquesActuels ;

	public TactiqueWarrior (Warrior warrior,int tactique,ArrayList <Integer> cheminAParcourir)
	{
		this.tactique = tactique ;
		this.cheminAParcourir = cheminAParcourir;
		this.guerrier = warrior;
		if(tactique == TACTIQUE_EXPLORATION)
			TactiqueExploration();
		if(tactique == TACTIQUE_OFFENSIVE)
			TactiqueOffensive();
		if(tactique == TACTIQUE_DEFENSIVE)
			TactiqueDefensive();
		
	}
	
	public TactiqueWarrior ()
	{
		this.tactique = -1 ;
		this.cheminAParcourir = null;
	}
	
	private void TactiqueExploration()
	{
		//guerrier.requerirInfosVoisins();
		ArrayList<Integer> listeidcible= new ArrayList<Integer>();
		int[][] matrice = guerrier.getCarte().getMatriceDistance();
		int idNoeudCourant = guerrier.getIdNoeudCourant();
		
		//On cherche les noeuds pas encore parcouru parmi les voisins
		for(int k=0; k<guerrier.getNoeud().getNombreVoisins();k++)
		{
			if(matrice[guerrier.getNoeud().getNoeudVoisinParIndice(k).getIdentifiantNoeud()][idNoeudCourant] == 214748364){
				listeidcible.add(guerrier.getNoeud().getNoeudVoisinParIndice(k).getIdentifiantNoeud());
			}
		}
		System.out.println(listeidcible);
		
		//Si la liste n'est pas vide on choisi un noeud au hasard
		if(listeidcible.size() !=0)
		{
			int temp = listeidcible.get((int)(Math.random()*(listeidcible.size())));
			
			for(int i=0;i<guerrier.getNoeud().getNombreVoisins();i++)
			{
				if(temp == guerrier.getNoeud().getNoeudVoisinParIndice(i).getIdentifiantNoeud())
					idCible = i; 
			
			}
			//System.out.println("idcible ="+temp);
			//System.out.println("N° voisin ="+idCible);
		}
		//Sinon on choisit un thread au hasard
		else
		{
			idCible = (int) (Math.random()*guerrier.getNoeud().getNombreVoisins());
		}
		
	}
	
	private void TactiqueDefensive(){
		
	}
	
	private void TactiqueOffensive(){
		
		risquesActuels = guerrier.getCarte().getRisques(guerrier.getNoeud().getId());
		int idtemp=0;
		
		if (risquesActuels.size() != 0)
		{
			if(cheminAParcourir.size() ==0 )
			{
				int minRisque = CarteWarrior.MAX_INT ;
				for (Iterator <Integer> i = risquesActuels.keySet().iterator() ; i.hasNext() ;)
				{
					Integer idNoeud = i.next();
					
					//on choisit un noeud dont le risque est minimal mais >0 ( on evite les threads trop facile)
					if (Math.min(minRisque,risquesActuels.get(idNoeud)) != minRisque && risquesActuels.get(idNoeud)>=0)
					{
						minRisque = risquesActuels.get(idNoeud) ;
						idtemp=idNoeud;
						
					}
				}
				
				//Trouve le chemin le plus court pour acceder au noeud cible! 
				cheminAParcourir = new Dijkstra(guerrier.getCarte().getMatriceDistance(),guerrier.getIdNoeudCourant()).chemin(idtemp);
				System.out.println("le chemin est :"+ cheminAParcourir);
				//On recupere le premier id du noeud du chemin
				for(int i=0;i<guerrier.getNoeud().getNombreVoisins();i++)
				{
					if(cheminAParcourir.get(0) == guerrier.getNoeud().getNoeudVoisinParIndice(i).getIdentifiantNoeud())
						idCible = i; 
				
				}
				
				//et on le supprime de la liste du chemin
				cheminAParcourir.remove(0);
				System.out.println("le nouveau chemin est :"+ cheminAParcourir);
			}
			else
			{
				//On recupere le premier id du noeud du chemin
				for(int i=0;i<guerrier.getNoeud().getNombreVoisins();i++)
				{
					if(cheminAParcourir.get(0) == guerrier.getNoeud().getNoeudVoisinParIndice(i).getIdentifiantNoeud())
						idCible = i; 
				
				}
				//et on le supprime de la liste du chemin
				cheminAParcourir.remove(0);
			}
		}
	}
	
	public ArrayList <Integer> getCheminsAParcourir ()
	{
		return cheminAParcourir ;
	}
	
	public void majChemin (Integer idNoeud)
	{
		cheminAParcourir.remove(idNoeud);
	}
	
	public int getIdCible(){
		return idCible;
	}
}
