package fidele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import libWarThreads.InfoNoeud;
import libWarThreads.InfoThread;
import libWarThreads.ParametresGeneraux;
import libWarThreads.Util;

public class Noeud 
{	
	/**
	 * Un entier reprsentant le noeud de faon unique
	 */
	int idNoeud;
	
	/**
	 * Le propritaire de ce noeud
	 */
	int proprietaire;
	
	/*
	 * Nombre de threads necessaires pour capturer le noeud,
	 * et servant de bonus pour le camp propritaire pour les combat
	 */
	int coefficient;
	/**
	 * Le fidele sur lequel est instanci ce noeud
	 */
	private Fidele fidele;
	
	/**
	 *  Les voisins de ce noeud
	 */
	private ArrayList <NoeudVoisin> noeudsVoisins;

	/**
	 *  Les threads presents sur ce noeud
	 */
	private Vector<Warrior> vectWarriors;
	
	private ArrayList<DemandeurDasile> demandeursDAsile;
	
	private int frequencePondaison;
	
	/**
	 * Sous-couche rseau
	 */
	private NoeudReseau noeudReseau;
	
	boolean dejacapture;
	
	private InfoNoeud infoNoeud;
	
	public class NoeudVoisin {
		private int idNoeud;
		private int distance;
		
		public NoeudVoisin(int idVoisin, int distanceVoisin) {
			idNoeud = idVoisin;
			distance = distanceVoisin;
		}
		
		public int getDistance() {
			return distance;
		}
		
		public int getIdentifiantNoeud() {
			return idNoeud;
		}
		
		public String toString() {
			return "("+idNoeud+", "+distance+") ";
		}
	}
	
	public class DemandeurDasile {
		private String nomDemandeur;
		private int numeroNoeudProvenance;
		
		public DemandeurDasile(String nomDemandeur, int numeroNoeudProvenance) {
			this.nomDemandeur = nomDemandeur;
			this.numeroNoeudProvenance = numeroNoeudProvenance;
		}
		
		public String getNomDemandeur() {
			return nomDemandeur;
		}
		
		public int getNumeroNoeudProvenance() {
			return numeroNoeudProvenance;
		}
	}
	
	public Noeud (InfoNoeud info, Fidele f)
	{
		fidele = f;
		infoNoeud=info;
		dejacapture = false;
		idNoeud = info.getIdentifiantNoeud();
		proprietaire = info.getProprietaire();
		coefficient = (int)(5*(Math.random())+1);
		frequencePondaison = info.getFrequencePondaison();
		noeudsVoisins = new ArrayList <NoeudVoisin> (ParametresGeneraux.nbrArretesMax);
		vectWarriors = new Vector<Warrior>();
		noeudReseau = new NoeudReseau(this);
		demandeursDAsile = new ArrayList<DemandeurDasile>();
		
		
		envoyerInfoNoeudAuFidele();
		noeudReseau.attendreConnexionsVoisins();
		
		//On lance la surveillance en arrire-plan des batailles du noeud
		initSurveillance(this);
		
		scruterDemandeursDasile();
	}
	
	
	/**
	 * Scrute en permanence la liste des threads souhaitant migrer sur ce noeud.
	 * Si la liste n'est pas vide et qu'il reste de la place sur ce noeud,
	 * alors une autorisation de migration est envoyée.
	 */
	public void scruterDemandeursDasile() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					if (vectWarriors.size() < ParametresGeneraux.nbrMaxThreadsParNoeud && ! demandeursDAsile.isEmpty()) {
						DemandeurDasile demandeurDasile = demandeursDAsile.get(0);
						Noeud.this.envoyerAutorisationMigration(
								demandeurDasile.getNomDemandeur(), demandeurDasile.getNumeroNoeudProvenance());
						demandeursDAsile.remove(0);
					}
					Util.attendre(50);
				}
			}
		}.start();
	}
	
	public void setCoefficient(int coeff)
	{
		coefficient = coeff;
	}
	
	public boolean isbase(){
		return false;
	}
	public void connecterAUnNoeud(InfoNoeud infoVoisin, int distance) {
		noeudReseau.rejoindreNoeudReseau(infoVoisin.getIp(), infoVoisin.getPort());
		this.ajouterVoisin(infoVoisin.getIdentifiantNoeud(), distance);
	}
	
	public void envoyerInfoNoeudAuFidele() {
		fidele.envoyerInfoNoeudADieu(new InfoNoeud(this));
	}
	
	public void envoyerInfoThreadAuFidele(Warrior w) {
		fidele.envoyerInfoThreadADieu(new InfoThread(w));
	}
	
	public void demanderAutorisationMigration(int numeroVoisin, String nomDemandeur) {
		noeudReseau.envoyerDemandeMigration(numeroVoisin, nomDemandeur);
	}
	
	public void envoyerAutorisationMigration(String nomThreadAutorise, int numeroNoeudProvenance) {
		noeudReseau.envoyerAutorisationMigration(numeroNoeudProvenance, nomThreadAutorise);
	}
	
	public void autoriserThreadAMigrer(String nomMigreur) {
		this.getWarriorParNom(nomMigreur).deBloquer();
	}
	
	public void ajouterThread(Warrior w) {
		vectWarriors.add(w);
	}
	
	public synchronized void migrerThread(Warrior thread) {
		thread.getCarte().collecterInfos(this);
		noeudReseau.envoyerThread(thread, thread.getNumeroNoeudDestination());
		envoyerInfoThreadAuFidele(thread);
		int temp =0;
		for(int i=0;i<vectWarriors.size();i++)
		{
			if(vectWarriors.get(i).getName()==thread.getNom())
				temp=i;	
		}
		vectWarriors.remove(temp);
		envoyerInfoNoeudAuFidele();
		thread.tuer();
	
		
	}
	
	public void recevoirThread(Warrior w) {
		vectWarriors.add(w);
		w.getCarte().ajouterLien(w.getIdNoeudCourant(),
								 w.getIdNoeudDestination(),
								 w.getDistanceAParcourir());
		w.setNoeud(this);
		w.start();
		try 
		{
			w.sleep(w.getDistanceAParcourir()*10);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		envoyerInfoNoeudAuFidele();
	}
	
	public void interrogerVoisins(String nomDemandeur) {
		noeudReseau.envoyerDemandesInfos(nomDemandeur);
	}
	
	public void donnerReponseInfo(String nomDemandeur, InfoNoeud reponse) {
		
		int temp =0;
		for(int i=0;i<vectWarriors.size();i++)
		{
			if(vectWarriors.get(i).getName().equals(nomDemandeur)) temp=i;	
		}
		vectWarriors.get(temp).ajouterInfoNoeudVoisin(reponse);
	}
	
	/**
	 * Dmarrer les actions relatives  ce noeud
	 * cf. NoeudBase.mettreEnAction()
	 */
	public void mettreEnAction() {
		
	}
	
	public int getId ()
	{
		return idNoeud;
	}
	
	public int getNombreVoisins ()
	{
		return noeudsVoisins.size();
	}
	
	
	public void ajouterVoisin(int idVoisin, int distanceVoisin) {
		noeudsVoisins.add(new NoeudVoisin(idVoisin, distanceVoisin));
	}
	
    public void ajouterVoisin(int idVoisin, int distanceVoisin, int emplacement) {
        noeudsVoisins.add(emplacement, new NoeudVoisin(idVoisin, distanceVoisin));
    }
    
    public void ajouterUnDemandeurDAsile(String demandeur, int numeroNoeudProvenance) {
    	demandeursDAsile.add(new DemandeurDasile(demandeur, numeroNoeudProvenance));
    }
	
	public int getNumeroVoisinAleatoire() {
		return (int)(Math.random() * getNombreVoisins());
	}
	
	/**
	 *  Cree un thread surveillant  intervalle rgulier les occupants du noeud
	 */
	private void initSurveillance (final Noeud noeud)
	{
        new Thread() {
            public void run() {
                //Bouclage infini
            	while (true)
            	{
            		
            		//On parcourt les threads qui se balade sur le noeud, des qu'il s'agit de threads
            		//de camps diffrent, on cherche  resoudre la bataille.
            		if (vectWarriors != null && !vectWarriors.isEmpty())
            		{

    					//Liste a partir du treemap
    					//ArrayList<Warrior> liste = getListeWarrior();
    					
            			//On prend le camp du premier warrior, pour comparer.
            			
            			boolean combat = false;
            			int camp1 = vectWarriors.get(0).getProprietaire();
            			
            			//On parcourt tous les warriors
            			for (int i = 0; i < vectWarriors.size(); i++)
            			{
            				//System.out.println("Warrior " + liste.get(i).getNom() + " du joueur " + liste.get(i).getProprietaire() + " sur le noeud " + liste.get(i).getNoeud().getId() + " et d'tat" + liste.get(i).getEtat());
            				if (camp1 != vectWarriors.get(i).getProprietaire())
            				{
            					//BASTON!
            					resolBataille();
            					combat = true;
            				}
            			}
            			if (combat == false && proprietaire != camp1)
            			{
            				//Il n'y a pas eu de combats
            				//Si le noeud est neutre, on voit si les threads actuels peuvent le capturer
            				
            				//Le thread est capturable (neutre ou d'un camp ennemi)
        					//System.out.println("Tentative de capture!");
        					
        					//Nombre de threads voulant le capturer
        					int nbWC = 0;
        					for(int i = 0; i < vectWarriors.size(); i++)
                			{
        						//System.out.println("Etat du warrior = "+liste.get(i).getEtat());
        						if (vectWarriors.get(i).getEtat() == 2)
        							nbWC = nbWC + 1;
        						//System.out.println("nombre threads="+nbWC);
                			}
        					
        					//Il y a assez de warriors pour le capturer
        					if (nbWC >= coefficient)
        					{
        						//on le capture
        						proprietaire=camp1;
        						if(dejacapture==false)
        							noeud.setCoefficient(noeud.coefficient+1);
        						noeud.dejacapture =true;
        						System.out.println("Capture!");
        						
        					}
            			}
            		}
            		
            		//On attend un temps donne
            		try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        }.start();
	}
	
	//Lancer un d avec un rsultat entre 1 et 100
	public int lancerDe()
	{	
	 	return (int)(100*Math.random());
	}
	
	/**
	 *  Resout le combat present sur le noeud (si y'en a un)
	 */
	public void resolBataille ()
	{

		System.out.println("Combat engage !");
		
		//Liste de warriors a partir du treemap
		//ArrayList<Warrior> liste = getListeWarrior();
		
		//Premierement, faut recuperer le nombre de warrior de chaque camp prsent, et pour 
		//faire ca on va commencer par dtecter l'id joueur le plus gros.
		int idplusgros = 0;
		for (int i = 0; i < vectWarriors.size(); ++i)
		{
			if (vectWarriors.get(i).getProprietaire() > idplusgros)
				idplusgros = vectWarriors.get(i).getProprietaire();
		}
		//Ok, maintenant on cre un vecteur de int dont la taille est suffisante pour
		//y mettre tous les id, et qui contiendra le nombre de warrior de chaque id.
		Vector<Integer> vectNbParId = new Vector<Integer> ();
		vectNbParId.setSize(idplusgros + 1);
		
		//On initialise le vecteur
		for (int i = 0; i < vectNbParId.size(); i++)
		{
			vectNbParId.set(i,0);
		}
		
		//Et maintenant, on le remplit
		for (int i = 0; i < vectWarriors.size(); ++i)
		{
			if(vectWarriors.get(i).getEtat() == 2)
			{
				int idmaitre = vectWarriors.get(i).getProprietaire();
				vectNbParId.set(idmaitre, vectNbParId.get(idmaitre)+1 );
			}
		}
		
		//Ensuite, il faut faire lancer autant de des que de warrior dans chaque camp, et
		//comparer les resultats, et supprimer le camp perdant, et changer l'appartenance du noeud
		
		//Tableau des rsultat de chaque camp
		Vector<Integer> vectResultatDes = new Vector<Integer>();
		vectResultatDes.setSize(idplusgros + 1);
		
		//On initialise le vecteur
		for (int i = 0; i < vectResultatDes.size(); i++)
		{
			vectResultatDes.set(i, 0);
		}
		
		//On lance autant de ds que de Warrior
		for (int i = 0; i < vectNbParId.size(); ++i)
		{
			int res = 0;
			for (int j = 0; j < vectNbParId.get(i); ++j)
			{
				res = res + lancerDe();
			}
			vectResultatDes.set(i, res);
		}
		
		//On rajoute comme bonus autant de lancer de des que le coeff du noeud
		if (proprietaire >= 0 && proprietaire < vectNbParId.size() && vectNbParId.get(proprietaire) > 0)
		{
			for (int i = 0; i < coefficient; ++i)
			{
				vectResultatDes.set(proprietaire, vectResultatDes.get(proprietaire)+lancerDe());
			}
		}
		
		//On cherche le plus petit rsultat
		int plusPetitRes = vectResultatDes.get(0);
		
		for (int i = 1; i < vectResultatDes.size(); ++i)
			if (vectResultatDes.get(i) < plusPetitRes)
				plusPetitRes = vectResultatDes.get(i);
		
		//On cherche tous les camps ayant le plus petit rsultat
		//IL FAUT TOUS LES ELIMINER
		for (int i = 0; i < vectResultatDes.size(); ++i)
		{
			if (vectResultatDes.get(i) == plusPetitRes)
			{
				for(int j = 0; j < vectWarriors.size(); ++j)
				{
					if (vectWarriors.get(j).getProprietaire() == i /*&& vectWarriors.get(j).getEtat() == 2*/)
					{
						//TUONS LE
						
						//D'abord on le DETRUIT
						vectWarriors.get(j).stopper();
						
						//Puis on l'enleve du vecteur du noeud
						vectWarriors.remove(j);
						System.out.println("Detruit!");
						
						j = j - 1;
					}
				}
			}
		}
		
		//Dcidons qui a remport le noeud
		
		int idGagnant = 0;
		int plusGrande = vectResultatDes.get(0);
		
		//On cherche le premier qui a la plus GRANDE (somme de ds)
		for (int i = 0; i < vectResultatDes.size(); ++i)
		{
			if (vectResultatDes.get(i) > plusGrande)
			{
				plusGrande = vectResultatDes.get(i);
				idGagnant = i;
			}
		}
		
		//Le noeud prend la couleur du gagnant
		
		infoNoeud.setProprietaire(idGagnant);
		proprietaire = idGagnant;
		
		//Si il y'a toujours plus d'un camp prsents sur le noeud,
		//cette fonction sera automatiquement rappele par le thread 
		//surveillant donc pas d'inquitude.
	}
	
	public int[] getProprietairesThreads() {
		int[] etats = new int[vectWarriors.size()];
		//ArrayList<Warrior> listeWarriors= new ArrayList<Warrior> (vectWarriors.values());
		for (int i=0; i<vectWarriors.size(); i++)
			etats[i] = vectWarriors.get(i).getProprietaire();
		return etats;
	}
	
	public NoeudReseau getNoeudReseau() {
		return noeudReseau;
	}
	
	public int getProprietaire() {
		return proprietaire;
	}
	
	public int getFrequencePondaison() {
		return frequencePondaison;
	}
	
	public int getNombreThreads() {
		return vectWarriors.size();
	}
	
	public int getDistanceVoisin(int i) {
		return noeudsVoisins.get(i).getDistance();
	}
	
	public int getDistanceVoisinParId(int idVoisin) {
		return getNoeudVoisinParId(idVoisin).getDistance();
	}
	
	public NoeudVoisin getNoeudVoisin(int i) {
		return noeudsVoisins.get(i);
	}
	
	public Warrior getWarriorParNom(String sonNom) {
		for (int i=0; i<vectWarriors.size(); i++)
			if (vectWarriors.get(i).getNom().equals(sonNom))
				return vectWarriors.get(i);
		assert(false);
		return null;
	}
	
	public NoeudVoisin getNoeudVoisinParId(int idVoisin) {
		for (int i=0; i<noeudsVoisins.size(); i++)
			if (noeudsVoisins.get(i).getIdentifiantNoeud() == idVoisin)
				return noeudsVoisins.get(i);
		assert(false);
		return null;
	}
	
	public Vector<Warrior> getListeWarrior() {
		   return vectWarriors;
		}
	
	public int getCoefficient ()
	{
		return coefficient;
	}
	
	public InfoNoeud getInfoNoeud ()
	{
		return infoNoeud ;
	}
}
