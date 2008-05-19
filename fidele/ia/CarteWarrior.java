package fidele.ia;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

import fidele.entite.Noeud;

import libWarThreads.ParametresGeneraux;
import libWarThreads.Util;

public class CarteWarrior implements Serializable 
{

	private static final long serialVersionUID = 8860904217894494417L;

	public class InfosCollectees implements Serializable
	{
		private static final long serialVersionUID = 2033358053004626524L;

		private long dateCollecte ;
		
		private int proprietaire;
		private boolean base;
		
		private int nombreThreadsJ1;
		private int nombreThreadsJ2 ;
		
		public InfosCollectees (Noeud noeud)
		{
			this.proprietaire = noeud.getProprietaire();
			this.base = noeud.isBase() ;
			nombreThreadsJ1=0;
			nombreThreadsJ2=0;
			
			for(int i=0;i<noeud.getListeWarrior().size();++i)
			{
				if (noeud.getListeWarrior().get(i).getProprietaire() == 0 ) nombreThreadsJ1++ ;
				else nombreThreadsJ2++ ;
			}
			
			dateCollecte = new Date().getTime() ; 
		}
		
		public void afficher()
		{
			System.out.println("	Date de collecte = " + dateCollecte );
			System.out.println("	Proprietaire noeud = " + proprietaire );
			System.out.println("	Est ce une base = " + base );
			System.out.println("	Nombre de thread du J1 = " + nombreThreadsJ1 );
			System.out.println("	Nombre de thread du J2 = " + nombreThreadsJ2 );
		}
		
		public long getDate()
		{
			return dateCollecte ;
		}
		
		public boolean estBase ()
		{
			return base ;
		}
		
		public int getProprietaire()
		{
			return proprietaire ;
		}
		
		public int getNbrThreadsJ1 ()
		{
			return nombreThreadsJ1;
		}
		
		public int getNbrThreadsJ2 ()
		{
			return nombreThreadsJ2;
		}
		
		public int[][] getMatriceDistance (){
			return matriceDistances;
		}
		
	}
	
	/**
	 * Distance infinie 
	 */
	public static final int MAX_INT = 214748364 ;
	
	/**
	 * Matrice des distances
	 */
	private int [][] matriceDistances ;
	
	public TreeMap <Integer,InfosCollectees> mapInfoCollectees ;
	
	private int proprietaireThread ;
	
	public CarteWarrior(int proprietaireThread)
	{
		this.proprietaireThread = proprietaireThread ;
		matriceDistances = new int [ParametresGeneraux.nbrNoeuds]
		                           [ParametresGeneraux.nbrNoeuds];
		
		for(int i=0;i<ParametresGeneraux.nbrNoeuds;++i)
			for(int j=0;j<ParametresGeneraux.nbrNoeuds;++j)
				matriceDistances[i][j] = MAX_INT;
		
		mapInfoCollectees = new TreeMap<Integer,InfosCollectees> ();
	}

	
	public void afficher()
	{
		System.out.println("Matrice des distances");
		Util.afficherMatrice(matriceDistances,
							 ParametresGeneraux.nbrNoeuds,
							 ParametresGeneraux.nbrNoeuds);
		
		for (Iterator <Integer> i = mapInfoCollectees.keySet().iterator() ; i.hasNext() ;)
		{
			Integer idNoeud = i.next();
			System.out.println("Informations sur le noeud " + idNoeud);
			mapInfoCollectees.get(idNoeud).afficher();
		}
	}
	
	public TreeMap <Integer,Integer> getRisques (int noeudCourant)
	{
		TreeMap <Integer,Integer> tabRisque = new TreeMap <Integer,Integer>  ();
		int maxDistance = 0;
		for(int i=0;i<ParametresGeneraux.nbrNoeuds;++i)
			for(int j=0;j<ParametresGeneraux.nbrNoeuds;++j)
				maxDistance = Math.max(maxDistance, matriceDistances[i][j]);
		
		for (Iterator <Integer> i = mapInfoCollectees.keySet().iterator() ; i.hasNext() ;)
		{
			Integer idNoeud = i.next();
			int nbrThreadsEnnemis = 0 ;
			if (this.proprietaireThread == 0)
				nbrThreadsEnnemis = mapInfoCollectees.get(idNoeud).getNbrThreadsJ2() - 
									mapInfoCollectees.get(idNoeud).getNbrThreadsJ1() ;
			else
				nbrThreadsEnnemis = mapInfoCollectees.get(idNoeud).getNbrThreadsJ1() - 
									mapInfoCollectees.get(idNoeud).getNbrThreadsJ2() ;
			
			tabRisque.put(idNoeud,new Integer(matriceDistances[noeudCourant][idNoeud] + 100 * nbrThreadsEnnemis)) ;
		}
		
		return tabRisque ;
	}
	
	public void ajouterLien (int idNoeud1,int idNoeud2,int distance)
	{
		if (matriceDistances[idNoeud1][idNoeud2] == MAX_INT)
		{
			matriceDistances[idNoeud1][idNoeud2]=distance;
			matriceDistances[idNoeud2][idNoeud1]=distance;
		}
	}
	
	public void collecterInfos (Noeud noeud)
	{
		Integer idNoeud = noeud.getId() ;
		InfosCollectees infosCollectees = new InfosCollectees (noeud);
		mapInfoCollectees.put(idNoeud, infosCollectees);
	}
	
	public void fusionner (CarteWarrior carte)
	{
		for(int i=0;i<ParametresGeneraux.nbrNoeuds;++i)
			for(int j=0;j<ParametresGeneraux.nbrNoeuds;++j)
				matriceDistances[i][j] = Math.min(carte.getDistance(i,j),matriceDistances[i][j]);
	
		for (Iterator <Integer>i = mapInfoCollectees.keySet().iterator() ; i.hasNext() ;)
		{
			Integer idNoeud = i.next();
			if (carte.mapInfoCollectees.containsKey(idNoeud))
			{
				if (carte.mapInfoCollectees.get(idNoeud).getDate() >  mapInfoCollectees.get(idNoeud).getDate())
				{
					mapInfoCollectees.put(idNoeud,carte.mapInfoCollectees.get(idNoeud));
				}
			}
		}
		
		for (Iterator <Integer>i = carte.mapInfoCollectees.keySet().iterator() ; i.hasNext() ;)
		{
			Integer idNoeud = i.next();
			if (! mapInfoCollectees.containsKey(idNoeud))
			{
				mapInfoCollectees.put(idNoeud,carte.mapInfoCollectees.get(idNoeud));
			}
		}
	}
	
	public int getDistance (int idNoeud1,int idNoeud2)
	{
		return matriceDistances[idNoeud1][idNoeud2];
	}
	
	public void calculerDistancesMin ()
	{
		matriceDistances = Util.distancesRW(matriceDistances);
	}
	
	public int[][] getMatriceDistance (){
		return matriceDistances;
	}
}
