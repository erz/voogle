/**
 * warthreads : ThreadVerifierConnectRun.java
 */
package fidele.trame;

import java.util.TreeMap;

import libWarThreads.InfoNoeud;

/**
 * @author Jean-Marc MUZI
 *
 */
class ThreadVerifierConnectRun implements Runnable {

	int timelimit = 5; //limite de temps de r�ponse en secondes
	
	private TreeMap <Integer, InfoNoeud> infosVoisins = null;
	
	public ThreadVerifierConnectRun(TreeMap<Integer, InfoNoeud> infosVoisins) {
		// TODO Auto-generated constructor stub
		this.infosVoisins = infosVoisins;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	//@Override
	public void run() {
		// TODO Auto-generated method stub
		//Il nous faut ABSOLUMENT la liste des voisins de ce noeud
		while (infosVoisins == null){
			Thread.currentThread().interrupt();
		}
		//R�server un socket de connexion sur un port � d�finir (ce sera le m�me pour toutes les machines)
		//Parcourir la liste des voisins et les pinger chacun � leur tour
		//Si un ping est hors limite de temps, traiter la machine en panne
		//Sinon, continuer avec la machine suivante
		//Si on arrive en bout de liste, recommencer
	}
	
}
