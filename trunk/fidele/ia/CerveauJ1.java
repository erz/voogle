package fidele.ia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import libWarThreads.reseau.InfoNoeud;
import libWarThreads.util.Util;

import fidele.entite.Warrior;

public class CerveauJ1 implements Serializable
{
	int idNoeudCible;
	
	float aggressivite = (float)0.51;

	TreeMap <Integer,Integer> risquesActuels ;	
	
	ArrayList<Integer> chemin;
	
	
	
	public CerveauJ1 (float aggressivite)
	{
		this.aggressivite = aggressivite ;
		chemin = new ArrayList<Integer>();
		
	}
	
	public void echangerCarte (ArrayList <Warrior> familleThreads)
	{
		CarteWarrior carte = new CarteWarrior(familleThreads.get(0).getProprietaire());
		for(int i=0;i<familleThreads.size();++i)
			carte.fusionner(familleThreads.get(i).getCarte());
		
		carte.calculerDistancesMin();
		
		for(int i=0;i<familleThreads.size();++i)
			familleThreads.get(i).setCarte(carte);
	}
	
	public void reflechir(Warrior guerrier) 
	{
		ArrayList <Warrior> familleThreads = guerrier.reunirFamille();
		echangerCarte(familleThreads);
		System.out.println("Noeud actuel = " + guerrier.getNoeud().getId());
		risquesActuels = guerrier.getCarte().getRisques(guerrier.getNoeud().getId());
		deciderTactique(guerrier);
	}

	public void deciderTactique (Warrior guerrier)
	{
		//System.out.println("Le Thread (" + guerrier.getNom() + ") sur le noeud (" + guerrier.getNoeud().getId() + ") reflechi ...");
		
		int tactiqueid = -1 ;
		
		if (risquesActuels.size() != 0)
		{
			
			if(chemin.size()== 0)
			{
				if(guerrier.getCarte().mapInfoCollectees.size()<3)
				{
					tactiqueid = TactiqueWarrior.TACTIQUE_EXPLORATION;
				}
				else
					 tactiqueid = TactiqueWarrior.TACTIQUE_OFFENSIVE;
				
			}
			
			guerrier.setTactique(new TactiqueWarrior(guerrier,tactiqueid,chemin));
			//recuperation de l'id noeud sur lequel le warrior va migrer
			idNoeudCible=guerrier.getTactique().getIdCible();
			//recuperation de la suite du chemin
			chemin = guerrier.getTactique().getCheminsAParcourir();
			
		}
		else
		{
			//System.out.println("Je n'ai aucune information sur ma carte");
			idNoeudCible = guerrier.getNoeud().getNumeroVoisinAleatoire();
			//System.out.println("J'entreprend d'explorer un de mes voisins");
			
		}
		
		
	}
	
	
	
	public void agir(Warrior guerrier) 
	{
		
		for(int i=0;i<3;i++)
		{
			System.out.println("////////////////ACTIONS("+i+") :"+guerrier.getTactique().getActions(i));
			switch(guerrier.getTactique().getActions(i))
			{
				case 1:
					guerrier.combattre();
					break;
				case 2:
					guerrier.demanderAutorisationMigration(idNoeudCible);
					break;
				case 3:
					System.out.println("J'attend des renforts");
					try 
					{
						System.out.println("J'attend des renforts");
						guerrier.sleep(4000);
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
			}
			
		}
		
	}
	
}
