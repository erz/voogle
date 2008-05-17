package libWarThreads;

import java.lang.Math;

/**
 * Des mthodes utilitaires
 * @author Rudy,Sylvain
 *
 */
public class Util 
{
	
	public static void attendre(int millisecondes) {
		try {
			Thread.sleep(millisecondes);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void afficherMatrice(	int [][] matrice,
										int nbrLignes,
										int nbrColonnes)
	{
		for(int i=0;i<nbrLignes;++i)
		{
			for(int j=0;j<nbrColonnes;++j)
			{
				System.out.print("["+matrice[i][j]+"]");
			}
		System.out.println("");
		}
	}
	
	public static int[][] fermetureRW(int[][] g)
	{
		final int[][] res = new int[g.length][g.length];
	    for (int i = 0; i < res.length; ++i)
	      for (int j = 0; j < res.length; ++j)
	          res[i][j] = g[i][j];
	    for (int i = 0; i < res.length; ++i) res[i][i]=1;
	    for (int k = 0; k < res.length; ++k)
	      for (int i = 0; i < res.length; ++i)
	        for (int j = 0; j < res.length; ++j)
	          res[i][j] = res[i][j] | res[i][k] & res[k][j];
	    return res;
	}
	
	public static int[][] distancesRW (final int[][] g)
	{
		final int[][] res = new int[g.length][g.length];
	    for (int i = 0; i < res.length; ++i)
	      for (int j = 0; j < res.length; ++j)
	          res[i][j] = g[i][j];

	    for (int k = 0; k < res.length; ++k)
	      for (int i = 0; i < res.length; ++i)
	        for (int j = 0; j < res.length; ++j)
	          res[i][j] = Math.min(res[i][j],res[i][k] + res[k][j]);
	    return res;
	}
	
}
