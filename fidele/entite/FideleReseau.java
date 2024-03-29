package fidele.entite;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.TreeMap;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;


import libWarThreads.reseau.InfoNoeud;
import libWarThreads.reseau.InfoThread;
import libWarThreads.reseau.InterfaceReseau;
import libWarThreads.trame.DTDebut;
import libWarThreads.trame.DTInfosNoeud;
import libWarThreads.trame.DTInfosThread;
import libWarThreads.trame.DTNouveauLien;
import libWarThreads.trame.DTNouveauNoeud;
import libWarThreads.trame.DTPanneFidele;
import libWarThreads.trame.DTPanneNoeud;
import libWarThreads.trame.DonneeTrame;
import libWarThreads.trame.Trame;

public class FideleReseau {
	
	/**
	 * Le port sur lequel on �coute les voisins qui souhaitent se connecter
	 */
	private final int portLocal = 4001;
	private ServerSocket serveur;
	
	/**
	 * L'interface de communication avec Dieu
	 */
	private static InterfaceReseau interfaceDieu;
	
	/**
	 * La couche sup�rieure
	 */
	private Fidele fidele;
	
	public FideleReseau(Fidele fidele) {
		this.fidele = fidele;
		interfaceDieu = new InterfaceReseau();
		new Thread(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					TreeMap<Integer, Noeud> tmNoeuds = getFidele().getNoeudsHebergs();
					//Parcourir le treemap tmNoeuds
					//et faire un envoyerControl(<id du noeud>)
					//A chaque Noeud
					
					//Attendre 1 seconde
					libWarThreads.util.Util.attendre(1000);
				}
			}
		}).start();
	}
	
	/**
	 * Attente que Dieu initie une connection vers ce noeud
	 */
	public void attendreDieu() {
		try {
			serveur = new ServerSocket(portLocal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread() {
			@Override
			public void run() {
				interfaceDieu.attendreClient(serveur);
				decoderTrames();
			}
		}.start();
	}
	
	/**
	 * Initie une connexion en mode client vers Dieu
	 * @param ip l'ip de la machine de Dieu
	 * @param port le port d'�coute de Dieu
	 */
	public void rejoindreDieu(String ip, int port) {
		interfaceDieu.rejoindreServeur(ip, port);
		decoderTrames();
	}
	
	/**
	 * Envoie une trame d'acquittement
	 * @param n
	 */
	public void envoyerControl(int n){
		interfaceDieu.envoyer(new Trame(Trame.ACK, new DTPanneFidele(n)));
	}
	
	/**
	 * Envoie l'information du noeud � Dieu (et non le noeud lui meme !)
	 * @param info
	 */
	public void envoyerInfoNoeud(InfoNoeud info) {
		interfaceDieu.envoyer(new Trame(Trame.INFO_NOEUD, new DTInfosNoeud(info)));
	}
	
	/**
	 * Envoyer l'information du Thread � Dieu (et non le thread lui meme !)
	 * @param info
	 */
	public void envoyerInfoThread(InfoThread info) {
		interfaceDieu.envoyer(new Trame(Trame.INFO_THREAD, new DTInfosThread(info)));
	}
	
	public void envoyerNotificationPanneADieu(int idNoeudEnPanne) {
		interfaceDieu.envoyer(new Trame(Trame.PANNE_NOEUD, new DTPanneNoeud(idNoeudEnPanne)));
	}
	
	/**
	 * D�codage des trames recues
	 * Effectue les actions necessaires en fonction du type de trame
	 */
	public void decoderTrames() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					Trame trameRecue = interfaceDieu.lireTrame();
					if (trameRecue != null)
						switch (trameRecue.getCode()) {
						case Trame.NOUVEAU_NOEUD :
							DTNouveauNoeud infosNouveauNoeud = (DTNouveauNoeud)trameRecue.getDonnee();
							fidele.instancierNoeud(infosNouveauNoeud.getInfo());
							break;
						case Trame.NOUVEAU_LIEN :
							DTNouveauLien nouveauLien = (DTNouveauLien)trameRecue.getDonnee();
							fidele.creerUnLien(nouveauLien.getInfoNoeud1(), nouveauLien.getInfoNoeud2(), nouveauLien.getDistance());
							break;
						case Trame.DEBUT :
							DTDebut debutJeu = (DTDebut)trameRecue.getDonnee();
							fidele.mettreLesNoeudsEnAction();
							break;
						case Trame.FIN :
							fidele.stopperTout();
							break;
						case Trame.PANNE_FIDELE :
							DTPanneFidele panneSurFidele = (DTPanneFidele)trameRecue.getDonnee();
							break;
						}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	/**
	 * Accesseur pour l'interface vers Dieu
	 * @return
	 */
	public static InterfaceReseau getInterfaceDieu() {
		return interfaceDieu;
	}
	
	/**
	 * 
	 * @return la couche sup�rieure
	 */
	public Fidele getFidele() {
		return fidele;
	}
}
