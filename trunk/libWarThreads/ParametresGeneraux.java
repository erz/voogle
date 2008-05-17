package libWarThreads;

/**
 * Contient tous les paramtres qui caractrisent un graphe
 * @author Trix
 *
 */
public class ParametresGeneraux
{
	/**
	 * Le nombre de joeurs contrlant les threads
	 */
	public static int nombreJoueurs = 1;
	
	/**
	 * Nombre de noeuds du graphes
	 */
	public static int nbrNoeuds = 10;
	
	/**
	 * Nombre de lignes de la matrice reprsentant le graphe
	 */
	public static int nbrLignes = 15;
	
	/**
	 * Nombre de colonnes de la matrice reprsentant le graphe
	 */
	public static int nbrColonnes = 20;
	
	/**
	 * Nombre d'arrtes minimum par noeud
	 */
	public static int nbrArretesMin = 2;
	
	/**
	 * Nombre d'arrtes maximal par noeud
	 */
	public static int nbrArretesMax = 4;
	
	/**
	 * Nombre de pixels espaant deux lements voisins de la matrice 
	 */
	public static int nbrPixels ;

	/**
	 * Si activ, favorise l'apparition de puits dans le graphe
	 * Attention, dans ce cas, la plage d'arrte n'est plus respecte !
	 */
	public static boolean favoriserPuits ;
	
	public static int nbrMaxThreadsParNoeud = 5;
	
	public static void setParam (int nombreNoeuds,
						int nombreColonnes,
						int nombreLignes,
						int nombreArretesMin,
						int nombreArretesMax,
						int nombrePixels,
						Boolean Puits)
	{
		nbrNoeuds = nombreNoeuds ;
		nbrLignes = nombreLignes ;
		nbrColonnes = nombreColonnes ;
		nbrArretesMin = nombreArretesMin ;
		nbrArretesMax = nombreArretesMax ;
		nbrPixels = nombrePixels ;
		favoriserPuits = Puits ;
	}
}