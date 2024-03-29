package fidele.ia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import fidele.entite.Warrior;

import libWarThreads.reseau.InfoNoeud;

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
	
	private int[] actions;
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
		actions = new int[3];
		
		switch(tactique){
		case TACTIQUE_EXPLORATION:
			TactiqueExploration();
			break;
		case TACTIQUE_OFFENSIVE:
			TactiqueOffensive();
			break;
		case TACTIQUE_DEFENSIVE:
			TactiqueDefensive();
			break;
		case TACTIQUE_INDETERMINEE:
			TactiqueIndeterminee();
			break;
		}
	}
	
	public TactiqueWarrior ()
	{
		this.tactique = -1 ;
		this.cheminAParcourir = null;
		actions = new int[3];
		actions[1]=1;
		actions[2]=2;
	}
	
	private void TactiqueIndeterminee(){
		actions[1]=1;
		actions[2]=2;
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
			//System.out.println("N� voisin ="+idCible);
		}
		//Sinon on choisit un thread au hasard
		else
		{
			idCible = (int) (Math.random()*guerrier.getNoeud().getNombreVoisins());
		}
		
		actions[1]=1;
		actions[2]=2;
	}
	
	private void TactiqueDefensive(){
		
		/*for(int i=0;i<guerrier.getCarte().getMatriceDistance().length;i++)
		{
		  guerrier.getCarte().mapInfoCollectees.
		}
		cheminAParcourir = new Dijkstra(guerrier.getCarte().getMatriceDistance(),guerrier.getIdNoeudCourant()).chemin(guerrier.);
		*/
	}
	
	private void TactiqueOffensive(){
		
		risquesActuels = guerrier.getCarte().getRisques(guerrier.getNoeud().getId());
		int idtemp=0;
		
		if (risquesActuels.size() != 0)
		{
			if(cheminAParcourir.size() ==0)
			{
				
				if(guerrier.getCarte().mapInfoCollectees.size()>2)
				{
					actions[0]=3;
				}
				
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
					System.out.println("Id risque minimum = " + idtemp);
					//System.out.println("Risques :"+ new ArrayList<Integer>(risquesActuels));
					
					//Trouve le chemin le plus court pour acceder au noeud cible! 
					cheminAParcourir = new Dijkstra(guerrier.getCarte().getMatriceDistance(),guerrier.getIdNoeudCourant()).chemin(idtemp);
					System.out.println("je suis sur le noeud:"+guerrier.getIdNoeudCourant());
					System.out.println("le chemin est :"+ cheminAParcourir);
					cheminAParcourir.remove(0);
					//On recupere le premier id du noeud du chemin
					for(int i=0;i<guerrier.getNoeud().getNombreVoisins();i++)
					{
						if(cheminAParcourir.size() > 0 && cheminAParcourir.get(0) == guerrier.getNoeud().getNoeudVoisinParIndice(i).getIdentifiantNoeud())
							idCible = i; 
					
					}
					
					actions[1]=1;
					actions[2]=2;
					
					
					//et on le supprime de la liste du chemin
					if (cheminAParcourir.size() > 0)
						cheminAParcourir.remove(0);
					System.out.println("le nouveau chemin est :"+ cheminAParcourir);
			}
			
				
		}
		else
			{
				//On recupere le premier id du noeud du chemin
				for(int i=0;i<guerrier.getNoeud().getNombreVoisins();i++)
				{
					if(cheminAParcourir.get(0) == guerrier.getNoeud().getNoeudVoisinParIndice(i).getIdentifiantNoeud())
						idCible = i; 
				
				}
				
				actions[1]=1;
				actions[2]=2;
				if(cheminAParcourir.size() == 1)
				{
					actions[0]=3;
				}
				
				//et on le supprime de la liste du chemin
				cheminAParcourir.remove(0);
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
	
	public int getActions(int i){
		return actions[i];
	}
	public int getIdCible(){
		return idCible;
	}
}
