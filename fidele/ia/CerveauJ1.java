package fidele.ia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import libWarThreads.InfoNoeud;
import libWarThreads.Util;

import fidele.CarteWarrior;
import fidele.Dijkstra;
import fidele.TactiqueWarrior;
import fidele.entite.Warrior;

public class CerveauJ1 extends Cerveau
{
	int idNoeudCible;
	
	private int compteurExploration=0;
	
	float aggressivite = (float)0.51;

	TreeMap <Integer,Integer> risquesActuels ;	
	
	ArrayList<Integer> chemin;
	
	TactiqueWarrior tactique;
	
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
				if(compteurExploration<15)
				{
					tactiqueid = TactiqueWarrior.TACTIQUE_EXPLORATION;
					compteurExploration++;
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
			System.out.println("Je n'ai aucune information sur ma carte");
			idNoeudCible = guerrier.getNoeud().getNumeroVoisinAleatoire();
			System.out.println("J'entreprend d'explorer un de mes voisins");
			
		}
		
		
	}
	
	
	
	public void agir(Warrior guerrier) 
	{
		
		guerrier.attaquer();
		guerrier.demanderAutorisationMigration(idNoeudCible);
	}
}
