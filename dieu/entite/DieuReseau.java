package dieu.entite;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


import libWarThreads.reseau.InfoNoeud;
import libWarThreads.reseau.InterfaceReseau;
import libWarThreads.trame.DTDebut;
import libWarThreads.trame.DTInfosNoeud;
import libWarThreads.trame.DTInfosThread;
import libWarThreads.trame.DTNouveauLien;
import libWarThreads.trame.DTNouveauNoeud;
import libWarThreads.trame.DTPanneFidele;
import libWarThreads.trame.DTPanneNoeud;
import libWarThreads.trame.Trame;
import libWarThreads.util.Console;

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
	
	public void scannerReseau() {
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
							case Trame.PANNE_NOEUD :
								//gerer une requete de panne
								System.out.println("PAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANNNNNNNE");
								gestionPanne(((DTPanneNoeud)trameRecue.getDonnee()).getIdNoeudPanne());
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
	private void gestionPanne(int idPanne){
		Vector<Integer> voisins = dieu.getGraphe().getNoeud(idPanne).getVectVoisin();
		Iterator<Integer> i = voisins.iterator();
		int max=0;
		while(i.hasNext()){
			//On insere le code ici pour chaque voisin
			int k = i.next().intValue(); //On met dans k la valeur en cours
			if(max < k) 
				 max = k;
		}
		System.out.println("Noeud "+idPanne+" en panne");
	}
}
