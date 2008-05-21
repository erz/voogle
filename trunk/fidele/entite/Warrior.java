package fidele.entite;

import java.io.Serializable;
import java.util.ArrayList;

import libWarThreads.reseau.InfoNoeud;
import libWarThreads.reseau.InfoThread;
import libWarThreads.util.Util;

import fidele.ia.*;

@SuppressWarnings("serial")
public class Warrior extends Thread implements Serializable
{
	
	/**
	 * Le nom rfrence le thread de facon unique sur tout le rseau
	 */
	private String nom;
	
	/**
	 *  Identifiant du thread
	 */
	private int idWarrior;
	
	private int idNoeudCourant;
	
	/**
	 * Numero du joueur qui dtient ce thread
	 */
	private int proprietaire;
	
	/**
	 * Noeud sur lequel le thread se trouve (un noeud n'est pas serializable !)
	 */
	private transient Noeud noeud;
	
	/**
	 *  Compteur global des threads
	 */
	private static int nbThreads = 0;
	
	private int numeroNoeudDestination;
	private int idNoeudDestination;
	private int distanceAParcourir;
	
	private int nombreDeplacements;
	
	/**
	 * Contient la carte des noeuds parcourus
	 * et les informations correspondants
	 */
	private CarteWarrior carte ;
	
	/**
	 * Tactique actuelle du thread
	 */
	private TactiqueWarrior tactique ;
	
	/**
	 * La liste des informations des noeuds voisins du noeud
	 * sur lequel se trouve le thread
	 */
	private ArrayList<InfoNoeud> infosNoeudsVoisins;
	
	/**
	 * Un ptit verrou :p
	 */
	//private Object verrou;
	
	private boolean attendreRenfort;
	private CerveauJ1 cerveau;
	
	public static final int ETAT_DEFAUT = 0;
	public static final int ETAT_DEPLACEMENT = 1;
	public static final int ETAT_CAPTURE = 2;
	public static final int ETAT_DEMANDE_INFO = 3;
	public static final int ETAT_MORT = 4;
	public static final int ATTENTE_MIGRATION = 5;
	
	private int etat;
	private  boolean stop;
	
	/**
	 *  Constructeur
	 */
	public Warrior (Noeud noeud)
	{
		this.noeud = noeud;
		idNoeudCourant = noeud.getId();
		idWarrior = nbThreads++;
		etat = Warrior.ETAT_DEFAUT;
		stop = false;
		proprietaire = noeud.getProprietaire();
		infosNoeudsVoisins = new ArrayList<InfoNoeud>();
		nom = proprietaire + "." + idWarrior;
		carte = new CarteWarrior(this.getProprietaire());
		tactique = new TactiqueWarrior();
		cerveau = new CerveauJ1((float)0.5);
		//verrou = new Object();
	}
	
	public InfoThread creerInfoThread() {
		return new InfoThread(
				nom,
				proprietaire,
				etat,
				idNoeudCourant,
				idNoeudDestination,
				distanceAParcourir,
				nombreDeplacements,
				attendreRenfort
				);
	}
	
	public ArrayList<InfoNoeud> getInfoNoeudVoisins(){
		return infosNoeudsVoisins;
	}
	
	public void requerirInfosVoisins() {
		etat = Warrior.ETAT_DEMANDE_INFO;
		infosNoeudsVoisins.clear();
		noeud.interrogerVoisins(nom);
		while (infosNoeudsVoisins.size() < noeud.getNombreVoisins())
			Util.attendre(50);
	}
	
	public void migrer() {
		etat = Warrior.ETAT_DEPLACEMENT;
		nombreDeplacements ++;
		noeud.migrerThread(this);
		etat = Warrior.ETAT_DEFAUT;
		Util.attendre(500);
	}
	
	public void mourrir() {
		etat = Warrior.ETAT_MORT;
		stop = true;
	}
	
	/**
	 * Met le thread en attente.
	 * La méthode deBloquer débloque le thread.
	 */
	public void bloquer() {
		suspend();
	}
	
	/**
	 * Reveille le thread.
	 */
	public void deBloquer() {
		resume();
	}
	
	/**
	 * Demande au noeud voisin l'autorisation de migrer.
	 * En attente d'une réponse positive, le thread est bloqué.
	 * @param numeroVoisin
	 */
	public void demanderAutorisationMigration(int numeroVoisin) {
		etat = Warrior.ATTENTE_MIGRATION;
		numeroNoeudDestination = numeroVoisin;
		idNoeudDestination = noeud.getNoeudVoisinParIndice(numeroVoisin).getIdentifiantNoeud();
		distanceAParcourir = noeud.getNoeudVoisinParIndice(numeroVoisin).getDistance();
		noeud.demanderAutorisationMigration(numeroVoisin, nom);
		// attente autorisation
		bloquer();
		// autorisation recue, on migre
		migrer();
	}
	
	/**
	 *  Tentative de capture du noeud 
	 */
	public void combattre() {
		//Attaque du noeud courant
		etat = Warrior.ETAT_CAPTURE;
		Util.attendre(1000);
	}
	
	public void ajouterInfoNoeudVoisin(InfoNoeud info) {
		infosNoeudsVoisins.add(info);
	}
	
	public int getProprietaire() {
		return proprietaire;
	}
	
	public Noeud getNoeud(){
		return noeud;
	}
	
	public int getIdNoeudDestination() {
		return idNoeudDestination;
	}
	
	public int getDistanceAParcourir() {
		return distanceAParcourir;
	}
	
	public int getNombreDeplacements() {
		return nombreDeplacements;
	}
	
	/**
	 *  Renvoi si le thread est dans un etat de capture
	 */
	public int getEtat(){ return etat ;}
	
	public void setNoeud(Noeud n) {
		noeud = n;
		idNoeudCourant = noeud.getId();
		idNoeudDestination = -1;
	}
	
	public void setEtat(int e) { etat = e; }
	
	public void stopper()
	{
		stop = true;
	}
	
	@Override
	public void run()
	{
		//Tant qu'il est vivant
		while(!stop)
		{
			/*
			 * Ces trois actions ci dessus dpendent de l'IA du warrior, et sont concentrs
			 * dans les fonction "reflechir" et "agir" de son Cerveau.
			 */
			cerveau.reflechir(this);
			cerveau.agir(this);
		}
	}
	
	
	
	public ArrayList <Warrior> reunirFamille () {
		Noeud noeudCourant = this.getNoeud();
		ArrayList <Warrior> familleThread = new ArrayList <Warrior> ();
		
		for(int i=0;i<noeudCourant.getListeWarrior().size();++i)
		{
			if (noeudCourant.getListeWarrior().get(i).getProprietaire() == this.getProprietaire())
				familleThread.add(noeudCourant.getListeWarrior().get(i));
		}
		return familleThread ;
	}
	
	public ArrayList<InfoNoeud> getInfosNoeudsVoisins() {
		return infosNoeudsVoisins;
	}
	
	public String getNom() {
		return nom;
	}
	
	public int getIdNoeudCourant() {
		return idNoeudCourant;
	}
	
	public int getNumeroNoeudDestination() {
		return numeroNoeudDestination;
	}
	
	public CarteWarrior getCarte() {
		return carte;
	}
	
	public void setCarte(CarteWarrior carte) {
		this.carte = carte ;
	}
	
	public void setTactique (TactiqueWarrior tactique){
		this.tactique = tactique ;
	}
	
	public TactiqueWarrior getTactique(){
		return tactique;
	}
	
	public String toString() {
		return "(" + idWarrior + ", " + noeud.getId() + ")";
	}

	
	public void setAttendreRenfort(boolean bool){
		attendreRenfort = bool;
	}
	
	public boolean getAttendreRenfort(){
		return attendreRenfort;
	}
}
