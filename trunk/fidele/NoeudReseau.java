package fidele;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import libWarThreads.DTDemandeInfo;
import libWarThreads.DTDemandeInfoVoisin;
import libWarThreads.DTDialogueMigration;
import libWarThreads.DTInfosNoeud;
import libWarThreads.DTThreadMigrant;
import libWarThreads.DTPanneNoeud;
import libWarThreads.DTReponseInfo;
import libWarThreads.DTReponseInfoVoisin;
import libWarThreads.InfoNoeud;
import libWarThreads.InterfaceReseau;
import libWarThreads.Trame;



/**
 * Implantation physique d'un noeud
 *
 */
public class NoeudReseau 
{
	/*
	 * Compteur pour les accus�s de reception 
	 */
	private class CompteurACK extends Thread
	{
		private int destinataire;
		private boolean ackRecu; 
		
		public CompteurACK(NoeudReseau e)
		{
			System.out.println("Le compteur du noeud " + e.noeud.getId() + " a �t� cr��.");
			ackRecu = false;
		}
		
		public void setDestinataire(int dest)
		{
			destinataire = dest;
		}
		
		public void setRecu (boolean etat)
		{
			ackRecu = etat;
		}
		
		@Override
		public void run()
		{
			//Attente avant de considerer que le message est mort
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (ackRecu == false)
			{
				//Le message est mort, on traite la panne du noeud destinataire
				resolPanne(destinataire);
			}
			ackRecu = false;
		}
	}
	
	/*
	 * Le compteur servant � la reception des ACK
	 */
	private CompteurACK compteur;
	
	/*
	 * Renouvelle le compteur
	 */
	private void renewCompteur()
	{
		//Si il y'a un compteur, on attend qu'il finisse
		if (compteur != null)
			while(compteur.isAlive() == true);
		compteur = new CompteurACK(this);
	}
	
	/**
	 * L'ip de la machine locale
	 */
	private String ip;
	
	/**
	 * Le port d'coute de la machine
	 */
	private static int portLocal = 4002;
	// non non, un entier ne s'initialise pas  null :) private int port = null;
	
	private ServerSocket socketAttenteVoisins;
	
	/**
	 * La liste des interfaces de communication avec les noeuds voisins
	 */
	private ArrayList<InterfaceReseau> interfacesNoeudsReseau;
	// merci de ne pas modifier un code existant qui est juste, par un code faux !!!! private ArrayList<Noeud> listeNoeuds = null;
	
	private boolean connect;
	
	/**
	 * Couche suprieure
	 */
	private Noeud noeud;
	
	/**
	 * Constructeur
	 * @param noeud le noeud de la couche suprieure
	 */
	public NoeudReseau(Noeud noeud) {
		this.noeud = noeud;
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		connect = true;
		//noeud.envoyerInfoAuFidele();
		interfacesNoeudsReseau = new ArrayList<InterfaceReseau>();
		try {
			socketAttenteVoisins = new ServerSocket(portLocal ++);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scannerInterfacesReseau();
	}
	
	/**
	 * Attente que des voisins se connectent, et les ajoute dans la liste
	 */
	public void attendreConnexionsVoisins() {
		
		new Thread() {
			@Override
			public void run() {
				while (true) {
					InterfaceReseau nouvelleInterface = new InterfaceReseau();
					nouvelleInterface.attendreClient(socketAttenteVoisins);
					ajouterInterface(nouvelleInterface);
					nouvelleInterface.envoyer(new Trame(Trame.DEMANDE_INFO_VOISIN, new DTDemandeInfoVoisin(noeud.getId())));
				}
			}
		}.start();
	}
	
	public void envoyerThread(Warrior thread, int numInterface) 
	{
		//Gestion des pannes
		//Au moment d'envoyer le thread, on lance le compteur pour attendre le ACK
		renewCompteur();
		compteur.setDestinataire(numInterface);
		compteur.start();
		
		interfacesNoeudsReseau.get(numInterface).envoyer(new Trame(Trame.MIGRATION, new DTThreadMigrant(thread)));
	}
	
	public void envoyerDemandesInfos(String nomDemandeur) {
		for (int i=0; i<interfacesNoeudsReseau.size(); i++)
			interfacesNoeudsReseau.get(i).envoyer(new Trame(Trame.DEMANDE_INFO, new DTDemandeInfo(nomDemandeur)));
	}
	
	public void envoyerDemandeMigration(int numeroInterface, String nomDemandeur) {
		interfacesNoeudsReseau.get(numeroInterface).envoyer(new Trame(Trame.DEMANDE_MIGRATION, new DTDialogueMigration(nomDemandeur)));
	}
	
	public void envoyerAutorisationMigration(int numeroInterface, String nomThreadAutorise) {
		interfacesNoeudsReseau.get(numeroInterface).envoyer(new Trame(Trame.AUTORISATION_MIGRATION, new DTDialogueMigration(nomThreadAutorise)));
	}
	
	/**
	 * Mthode qui scanne en continu successivement chacun des voisins et regarde pour chacun s'il possde
	 * une donne dans sa file d'attente d'objets recus.
	 *
	 */
	private void scannerInterfacesReseau() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					for (int i=0; i<interfacesNoeudsReseau.size(); i++) {
						Trame trameRecue = interfacesNoeudsReseau.get(i).lireTrame();
						if (trameRecue != null)
							switch (trameRecue.getCode()) {
							case Trame.MIGRATION :
								//Gestion des panne : on a bien recu le thread
								//On envoie une trame ACK au noeud qui l'a envoy�
								interfacesNoeudsReseau.get(i).envoyer(new Trame(Trame.ACK, null));
								noeud.recevoirThread(((DTThreadMigrant)trameRecue.getDonnee()).getWarrior());
							
								break;
							case Trame.DEMANDE_INFO :
								interfacesNoeudsReseau.get(i).envoyer(
										new Trame(Trame.REPONSE_INFO, new DTReponseInfo(
												((DTDemandeInfo)trameRecue.getDonnee()).getNomDemandeur(), new InfoNoeud(noeud))));
								break;
							case Trame.REPONSE_INFO :
								DTReponseInfo trameReponse = (DTReponseInfo)trameRecue.getDonnee();
								noeud.donnerReponseInfo(trameReponse.getNomDemandeur(), trameReponse.getInfoReponse());
								break;
							case Trame.DEMANDE_INFO_VOISIN :
								int idDemandeur = ((DTDemandeInfoVoisin)trameRecue.getDonnee()).getNomDemandeur();
								interfacesNoeudsReseau.get(i).envoyer(
										new Trame(Trame.REPONSE_INFO_VOISIN, new DTReponseInfoVoisin(
												noeud.getId(), noeud.getDistanceVoisinParId(idDemandeur))));
								break;
							case Trame.PANNE_NOEUD :
								//le noeud est en panne, election du fidele qui l'hebergera 
								int idPanne = ((DTPanneNoeud)trameRecue.getDonnee()).getIdNoeudPanne();
								NoeudReseau nr = ((DTPanneNoeud)trameRecue.getDonnee()).getNoeudEmetteur();
								int idReponse = nr.noeud.getId();
								interfacesNoeudsReseau.get(idReponse).envoyer(new Trame(Trame.ACK,null));
								break;
								
							case Trame.ACK :
								//le thread envoy� est bien arriv�
								//On arrete le compteur de ACK
								System.out.println("On a bien recu un ACK de " + compteur.destinataire);
								compteur.setRecu(true);
								break;

							case Trame.REPONSE_INFO_VOISIN :
								int idVoisin = ((DTReponseInfoVoisin)trameRecue.getDonnee()).getNomVoisin();
								int distanceVoisin = ((DTReponseInfoVoisin)trameRecue.getDonnee()).getDistanceAuVoisin();
								noeud.ajouterVoisin(idVoisin, distanceVoisin, i);
								break;
							case Trame.DEMANDE_MIGRATION :
								String nomDemandeur = ((DTDialogueMigration)trameRecue.getDonnee()).getNomDemandeur();
								noeud.ajouterUnDemandeurDAsile(nomDemandeur, i);
								break;
							case Trame.AUTORISATION_MIGRATION :
								String nomThreadAutorise = ((DTDialogueMigration)trameRecue.getDonnee()).getNomDemandeur();
								noeud.autoriserThreadAMigrer(nomThreadAutorise);
								
							}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	/**
	 * Connexion en mode client  un noeud distant
	 * @param ip ip de la machine qui heberge le noeud distant
	 * @param portDistant le port sur lequel coute la machine distante
	 */
	public void rejoindreNoeudReseau(String ip, int port) {
		InterfaceReseau nouvelleInterface = new InterfaceReseau();
		nouvelleInterface.rejoindreServeur(ip, port);
		ajouterInterface(nouvelleInterface);
	}
	
	private void ajouterInterface(InterfaceReseau nouvelleInterface) {
		interfacesNoeudsReseau.add(nouvelleInterface);
	}
	
	public int getNombreInterfacesNoeudsReseau() {
		return interfacesNoeudsReseau.size();
	}
	
	public boolean isConnect() {
		return connect;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPortLocal() {
		return portLocal - 1;
	}
	
	public String toString() {
		return ip + "/" + portLocal;
	}

	/*
	 * Fonction de r�solution de panne
	 * 	@param destinataire
	 * 		Noeud en panne
	 */
	void resolPanne(int destinataire)
	{
		/*Iterator<InterfaceReseau> i = interfacesNoeudsReseau.iterator();
		while(i.hasNext()){
			InterfaceReseau ir = i.next();
			ir.envoyer(new Trame(Trame.PANNE_NOEUD, new DTPanneNoeud(this,destinataire)));
		}
		//on prend les identifiants de tous les noeuds voisins ayant d�tect� la panne
		ArrayList<Integer> idsNoeudsVoisins = new ArrayList<Integer>();
		for(int j=0; j<noeud.getNombreVoisins();j++)
			idsNoeudsVoisins.add(new Integer(noeud.getNoeudVoisin(j).getIdentifiantNoeud()));
		//on recupere le plus grand identifiant
		int idMax = noeud.getId();
		Iterator<Integer> itMax = idsNoeudsVoisins.iterator();
		while(itMax.hasNext()){
			int cm = itMax.next().intValue();
			if(idMax < cm)
				idMax = cm;
		}*/
		//il devient initiateur(i)
		//envoyer � Dieu
		//envoyer une trame initiateur
		//il envoie une trame au identifiant de plus grand d'identifiant plus grand que celui de i
		
		//et celui qui a le plus grand id devient createur
		//ce cr�ateur demande a dieu les donn�es du noeud en panne
	
	
		//Envoyer rapport de panne � Dieu
		
		
		/////////////////
		///
		/// Un Noeud n'est pas envoyable sur le r�seau !!! ( = java.io.NotSerializableException )
		/// Donc pas de "this" dans FideleReseau.getInterfaceDieu().envoyer(new Trame(Trame.PANNE_NOEUD,new DTPanneNoeud(this,1)));
		/// A la place, cr�ez vous une classe qui impl�mente Serializable et qui contient les infos
		/// que vous souhaitez envoyer si besoin.
		///
		/////////////////
		
		
		FideleReseau.getInterfaceDieu().envoyer(new Trame(Trame.PANNE_NOEUD,new DTPanneNoeud(this,1)));
		//Dieu pr�vient tous les noeuds voisins du noeud en panne
		//Dieu leur dit qui va recr�er
		//Dieu envoie au noeud voisin ayant le plus grand id une trame avec toutes les infos
		//n�cessaires � la recr�ation(infoNoeudenPanne, liste voisins)
	
	}
	public void initiateur(){
		
	}

}