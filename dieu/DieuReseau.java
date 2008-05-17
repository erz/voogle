package dieu;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import libWarThreads.Console;
import libWarThreads.DTDebut;
import libWarThreads.DTInfosNoeud;
import libWarThreads.DTInfosThread;
import libWarThreads.DTNouveauLien;
import libWarThreads.DTNouveauNoeud;
import libWarThreads.DTPanneFidele;
import libWarThreads.InfoNoeud;
import libWarThreads.InterfaceReseau;
import libWarThreads.Trame;

public class DieuReseau {
	
	private ServerSocket socketAttenteMachines;
	private final int portDistant = 4001;
	private final int portLocal = 4000;
	private ArrayList<InterfaceReseau> interfacesMachines;
	
	private boolean scanEnCours;
	
	private Dieu dieu;
	
	public DieuReseau(Dieu dieu) {
		this.dieu = dieu;
		interfacesMachines = new ArrayList<InterfaceReseau>();
		scanEnCours = false;
		decoderTrames();
	}
	
	public void scannerMachines() {
		scanEnCours = true;
		new Thread() {
			@Override
			public void run() {
				try {
					final byte[] ipLocale = InetAddress.getLocalHost().getAddress();
					ipLocale[3] --;
					while (scanEnCours) {
						ipLocale[3]++;
						new Thread() {
							@Override
							public void run() {
								InterfaceReseau nouvelleInterface = new InterfaceReseau();
								try {
									if (nouvelleInterface.rejoindreServeur(InetAddress.getByAddress(ipLocale).toString().substring(1), portDistant)) {
										interfacesMachines.add(nouvelleInterface);
										dieu.notifierObserveurs(nouvelleInterface);
									}
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	public void attendreMachines() {
		try {
			socketAttenteMachines = new ServerSocket(portLocal);
		} catch (IOException e) {
			Console.ecrire("Le port " + portLocal + " est occup");
		}
		new Thread() {
			@Override
			public void run() {
				while (true) {
					InterfaceReseau nouvelleInterface = new InterfaceReseau();
					nouvelleInterface.attendreClient(socketAttenteMachines);
					interfacesMachines.add(nouvelleInterface);
					dieu.notifierObserveurs(nouvelleInterface);
				}
			}
		}.start();
	}
	
	public void envoyerSignalDebutATous() {
		for (int i=0; i<interfacesMachines.size(); i++)
			envoyerSignalDebut(i);
	}
	
	private void envoyerSignalDebut(int numeroFidele) {
		interfacesMachines.get(numeroFidele).envoyer(new Trame(Trame.DEBUT, new DTDebut()));
	}
	
	public void envoyerSignalPanne(int numeroFidele) {
		interfacesMachines.get(numeroFidele).envoyer(new Trame(Trame.PANNE_FIDELE, new DTPanneFidele(numeroFidele)));
	}
	
	public void envoyerUnNoeud(InfoNoeud infoNoeud, int numFidele) {
		System.out.println("Envoie d'un noeud");
		interfacesMachines.get(numFidele).envoyer(
				new Trame(Trame.NOUVEAU_NOEUD, new DTNouveauNoeud(infoNoeud)));
		
	}
	
	public void envoyerUnLien(InfoNoeud infoNoeud1, InfoNoeud infoNoeud2, int numFideleNoeud1, int distance) {
		interfacesMachines.get(numFideleNoeud1).envoyer(
				new Trame(Trame.NOUVEAU_LIEN, new DTNouveauLien(infoNoeud1, infoNoeud2, distance)));
	}

	public void decoderTrames() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					for (int i=0; i<interfacesMachines.size(); i++) {
						Trame trameRecue = interfacesMachines.get(i).lireTrame();
						if (trameRecue != null) {
							//System.out.println("Nouvelle modif");
							switch (trameRecue.getCode()) {
							case Trame.INFO_NOEUD :
								dieu.ajouterInfoModification(((DTInfosNoeud)trameRecue.getDonnee()).getInfoNoeud());
								//System.out.println("Fin nouvelle modif");
								break;
							case Trame.INFO_THREAD :
								dieu.ajouterInfoModification(((DTInfosThread)trameRecue.getDonnee()).getInfoThread());
								//System.out.println("Fin nouvelle modif");
								break;
							}
						}
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
	
	public void arreterScan() {
		scanEnCours = false;
		
	}
	
	public int getNombreInterfacesMachines() {
		return interfacesMachines.size();
	}

}
