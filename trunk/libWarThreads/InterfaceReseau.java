package libWarThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import libWarThreads.trame.Trame;


/**
 * Interface de communication avec un point distant
 * @author Tony
 * @since 28/03/08
 *
 */
public class InterfaceReseau {
	
	public Socket socket;
	private final int delaiConnection = 500;
	
	/**
	 * Le flux d'entrée
	 */
	public ObjectOutputStream out;
	
	/**
	 * Le flux de sortie
	 */
	public ObjectInputStream in;
	
	/**
	 * File d'attente contenant les trames recues
	 */
	private ArrayList<Trame> tramesRecues;
	
	/**
	 * Constructeur
	 * 
	 */
	public InterfaceReseau() {
		tramesRecues = new ArrayList<Trame>();
	}
	
	public boolean rejoindreServeur(String ip, int port) {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), delaiConnection);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			recevoir();
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println("Aucun serveur à l'adresse " + ip + " : " + port);
			 e.printStackTrace();
			return false;
		}
	}
	
	public boolean attendreClient(ServerSocket serveur) {
		try {
			socket = serveur.accept();
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			recevoir();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Console.ecrire("Erreur lors de l'attente d'un client");
			System.out.println("erreur");
			return false;
		}
	}
	
	/**
	 * Envoi d'une trame sur le flux de sortie
	 * @param t la trame envoyée
	 */
	public synchronized void envoyer(Trame t) {
		try {
			out.writeObject(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reception d'une trame sur le flux d'entrée et mise en file d'attente de celle-ci
	 *
	 */
	public void recevoir() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						tramesRecues.add((Trame)InterfaceReseau.this.in.readObject());
					} catch (StreamCorruptedException sce) {
						sce.printStackTrace();
						System.exit(0);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	/**
	 * Retourne la 1ere trame de la liste, et retire celle-ci de la liste
	 * @return la 1ere trame de la liste
	 */
	public Trame lireTrame() {
		if (! tramesRecues.isEmpty()) {
			Trame tete = tramesRecues.get(0);
			tramesRecues.remove(0);
			return tete;
		}
		return null;
	}
	
	public String getIP() {
		return socket.getInetAddress().toString().substring(1);
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	
}
