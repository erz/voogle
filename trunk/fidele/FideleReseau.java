package fidele;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.TreeMap;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

import libWarThreads.DTDebut;
import libWarThreads.DTInfosNoeud;
import libWarThreads.DTInfosThread;
import libWarThreads.DTNouveauLien;
import libWarThreads.DTNouveauNoeud;
import libWarThreads.DTPanneFidele;
import libWarThreads.DonneeTrame;
import libWarThreads.InfoNoeud;
import libWarThreads.InfoThread;
import libWarThreads.InterfaceReseau;
import libWarThreads.Trame;

public class FideleReseau {
	
	private final int portDistant = 4000;
	private final int portLocal = 4001;
	private ServerSocket serveur;
	private static InterfaceReseau interfaceDieu;
	
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
					libWarThreads.Util.attendre(1000);
				}
			}
		}).start();
	}
	
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
	
	public void rejoindreDieu(String ip, int port) {
		interfaceDieu.rejoindreServeur(ip, port);
		decoderTrames();
	}
	
	public void envoyerControl(int n){
		interfaceDieu.envoyer(new Trame(Trame.ACK, new DTPanneFidele(n)));
	}
	
	public void envoyerInfoNoeud(InfoNoeud info) {
		interfaceDieu.envoyer(new Trame(Trame.INFO_NOEUD, new DTInfosNoeud(info)));
	}
	
	public void envoyerInfoThread(InfoThread info) {
		interfaceDieu.envoyer(new Trame(Trame.INFO_THREAD, new DTInfosThread(info)));
	}
	
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
	
	public Fidele getFidele() {
		return fidele;
	}
}
