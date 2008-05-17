package fidele;

import java.util.ArrayList;
import java.util.Vector;

public class Dijkstra {

	public static final int NIL = -1;
	 public static final int MAX = 214748364;

	 private int[][] poids ;
	 private int[] P ;
	 private float[] distance ;
	 private Vector<Integer> S = new Vector<Integer>();
	 private Vector<Integer> T = new Vector<Integer>();
	 private int depart,arrive;
	 
	public Dijkstra(int[][] poids,int depart){
		 this.poids = new int[poids.length][poids.length];
		 for(int i=0;i<poids.length;i++)
		 {
			 for(int j=0;j<poids.length;j++)
			 {
				 if(poids[i][j]!=-1) 
					 this.poids[i][j] = poids[i][j];
				 else 
					 this.poids[i][j] = MAX;
			 }
		 }
		 
		 this.depart = depart;
		 initialisation();
		 algoDijkstra();
	}
	
	private void initialisation()
	{
		 P = new int[poids.length];
		 distance = new float[poids.length];
		 
		 for(int i=0;i<distance.length;i++) 
			 distance[i] = poids[depart][i];
		 
		 for(int i=0;i<poids.length;i++) 
			 P[i] = NIL;
		 
		 for(int i=0;i<poids.length;i++) 
			 if(poids[depart][i]<MAX) 
				 P[i] = depart;
		 
		 for(int i=0;i<poids.length;i++) 
			 if(i!=depart) 
				 T.add(i);
		 
		 S.add(depart);
	}
	
	private void algoDijkstra()
	{
		 while(T.size()!=0) 
		 {
			 int s = chercherIndexDistanceMinimale();
			 T.remove(new Integer(s));
			 S.add(s);
			 Vector<Integer> v = voisin(s);
			 
			 for(int i=0;i<v.size();i++) 
				 maj_distances(s,v.get(i).intValue());
		 }
	}
	
	private int chercherIndexDistanceMinimale()
	{
		 float min = distance[T.get(0).intValue()];
		 int index = T.get(0).intValue();
		 
		 for(int i=0;i<T.size();i++) 
		 {
			 if(min>distance[T.get(i).intValue()])
			 {
				 min = distance[T.get(i).intValue()];
				 index = T.get(i).intValue();
			 }
		 }
		 return index;
	}
	
	private Vector<Integer> voisin(int s) 
	{
		 Vector<Integer> v = new Vector<Integer>();
		 for(int i=0;i<poids[s].length;i++)
			 if(poids[s][i]<MAX && T.contains(i)) 
				 v.add(i);
		 
		 return v;
	}
	
	private void maj_distances(int s1,int s2) 
	{
		 if(distance[s2] > distance[s1] + poids[s1][s2]) 
		 {
			 distance[s2] = distance[s1] + poids[s1][s2];
			 P[s2] = s1;
		 }
	}
	
	public ArrayList<Integer> chemin(int noeudCible) 
	{
		 ArrayList<Integer> V = new ArrayList<Integer>();
		 arrive = noeudCible;
		 Vector<Integer> W = new Vector<Integer>();
		 int s = noeudCible;
		 W.add(s);
		 
		 while(s!=this.depart)
		 {
			 s = P[s];
			 W.add(s);
		 }
		 
		 for(int i=0;i<W.size();i++) 
			 V.add(W.get(W.size()-1-i));
		 return V;
	}
	
}
