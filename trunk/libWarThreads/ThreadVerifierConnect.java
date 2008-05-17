/**
 * warthreads : ThreadVerifierConnect.java
 */
package libWarThreads;

import java.util.TreeMap;

/**
 * @author Jean-Marc MUZI
 *
 */
public class ThreadVerifierConnect extends Thread {

	public ThreadVerifierConnect(TreeMap <Integer, InfoNoeud> infosVoisins) {
		// TODO Auto-generated constructor stub
		super(new ThreadVerifierConnectRun(infosVoisins));
	}
}
