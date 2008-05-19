package dieu.graphe;

import java.util.Vector;

import libWarThreads.general.ParametresGeneraux;
import libWarThreads.util.Util;



public class GrapheAlgoSylv
{
	/**
	 * Matrice des noeuds 
	 */
	private NoeudGraphe [][] matriceNoeuds ;
	
	/**
	 * Vecteur des cases libres de la matrice des noeuds
	 */
	private Vector <Coordonnees> vectCasesLibres = new Vector <Coordonnees>();

	/**
	 * Vecteur des noeuds du graphe
	 */
	private Vector <NoeudGraphe> vectNoeuds = null ; 

	/**
	 * Matrice d'adjacence 
	 */
	private int [][] matriceAdjacence ;
	
	private Graphe graphe;
	
	
	
	public GrapheAlgoSylv (Graphe graphe)
	{
		this.graphe = graphe;
		vectNoeuds = new Vector <NoeudGraphe> ();
		vectCasesLibres = new Vector <Coordonnees> ();
		matriceNoeuds = new NoeudGraphe [ParametresGeneraux.nbrLignes]
			                      [ParametresGeneraux.nbrColonnes];
		
		matriceAdjacence = new int [ParametresGeneraux.nbrNoeuds]
		                            [ParametresGeneraux.nbrNoeuds];
	
		construireGraphe();
		
	}

	private void construireGraphe()
	{
		initialiserVectCasesLibres();
		initialiserMatriceAdjacence();
		disposerNoeuds();
		lierNoeuds();
		assurerConnexit();
		matriceVersGraphe();
		initialiserListeLiens();
		choisirBase();
	}

	private Boolean estConnexe (int [][] matriceChemins)
	{
		int i;
		for(i=0;i<ParametresGeneraux.nbrNoeuds &&
				  matriceChemins[0][i] == 1 ;++i);
		if (i == ParametresGeneraux.nbrNoeuds) return new Boolean(true) ;
		return false ;
	}
	
	void assurerConnexit ()
	{
		int [][] matriceChemins = Util.fermetureRW(matriceAdjacence);
		while (false == estConnexe(matriceChemins))
		{
				Vector <NoeudGraphe> graphe1 = new Vector <NoeudGraphe> ();
				Vector <NoeudGraphe> graphe2 = new Vector <NoeudGraphe> ();
				for(int i=0;i<ParametresGeneraux.nbrNoeuds;++i)
				{
					if (matriceChemins[0][i] == 1) 
						graphe1.add(vectNoeuds.get(i));
					else
						graphe2.add(vectNoeuds.get(i));
				}
				connecterGraphes(graphe1,graphe2);
				matriceChemins = Util.fermetureRW(matriceAdjacence);
		}
	}
	
	void connecterGraphes(Vector <NoeudGraphe> graphe1,Vector <NoeudGraphe> graphe2)
	{
		/**
		 * Cration d'un tableau des distances
		 * entre chaque couples de noeuds
		 */
		double [][] tabDistances = new double [graphe1.size()]
		                                      [graphe2.size()];
		
		double longueurMin = ParametresGeneraux.nbrNoeuds ;
		int xMin = 0 ;
		int yMin = 0 ;

		for(int i=0;i<graphe1.size();++i)
		{
			for(int j=0;j<graphe2.size();++j)
			{
				tabDistances[i][j] = longueurArrete(graphe1.get(i),
													graphe2.get(j));
				if (tabDistances[i][j] < longueurMin)
				{
					xMin = i ;
					yMin = j ;
					longueurMin = tabDistances[i][j];
				}
			}
		}
		
		lierVoisin(graphe1.get(xMin),graphe2.get(yMin),true);
	}
	
	private double longueurArrete (NoeudGraphe noeud1,NoeudGraphe noeud2)
	{
		int x1 = noeud1.getXPos();
		int x2 = noeud2.getXPos();
		int y1 = noeud1.getYPos();
		int y2 = noeud2.getYPos();
	
		return Math.sqrt(Math.pow(x2-x1,2)+
						 Math.pow(y2-y1,2));
	}
	

	
	private void initialiserMatriceAdjacence()
	{
		for(int i=0;i<ParametresGeneraux.nbrNoeuds;++i)
		{
			for(int j=0;j<ParametresGeneraux.nbrNoeuds;++j)
			{
				matriceAdjacence[i][j] = 0;
			}
		}		
	}
	
	private void initialiserListeLiens() {
		for(int i=0;i<ParametresGeneraux.nbrNoeuds;i++)
		{
			for(int j=0;j<ParametresGeneraux.nbrNoeuds-i;j++)
			{
				if(matriceAdjacence[i][j+i]== 1)
				{
					Cercle cercle = new Cercle (new Coordonnees(vectNoeuds.get(i).getXPos(),vectNoeuds.get(i).getYPos()));
					cercle.setCoordonneesPoint(new Coordonnees (vectNoeuds.get(j+i).getXPos(),vectNoeuds.get(j+i).getYPos()));
					
					graphe.ajouterLien(new Lien(vectNoeuds.get(i), vectNoeuds.get(j+i), (int) cercle.getRayon()));
				}
			}
		}
		
	}
	
	private void choisirBase(){
		int minX=1000,
			minY=1000,
			maxX=0,
			maxY=0;
		int base1=0,
			base2=0;
		
		for(int i=0; i<vectNoeuds.size();i++)
		{
			//on cherche le noeud le plus en haut  gauche
			if(vectNoeuds.get(i).getXPos()<=minX)
			{
				if(vectNoeuds.get(i).getYPos()<=minY)
				{
					minX= vectNoeuds.get(i).getXPos();
					minY= vectNoeuds.get(i).getYPos();
					base1=i;
				}
			}
			
			//on cherche le noeud le plus en bas  droite
			if(vectNoeuds.get(i).getYPos()>=maxY)
			{
				if(vectNoeuds.get(i).getXPos()>=maxX)
				{
					maxX= vectNoeuds.get(i).getXPos();
					maxY= vectNoeuds.get(i).getYPos();
					base2=i;
				}
			}
		}
		
		vectNoeuds.get(base1).getInfo().setBase(true);
		vectNoeuds.get(base1).getInfo().setProprietaire(0);
		vectNoeuds.get(base1).getInfo().setFrequencePondaison(10);
		vectNoeuds.get(base2).getInfo().setBase(true);
		vectNoeuds.get(base2).getInfo().setProprietaire(1);
		vectNoeuds.get(base2).getInfo().setFrequencePondaison(10);
	}
	
	private void initialiserVectCasesLibres ()
	{
		for(int i=0;i<ParametresGeneraux.nbrLignes;++i)
		{
			for(int j=0;j<ParametresGeneraux.nbrColonnes;++j)
			{
				vectCasesLibres.add(new Coordonnees(i,j));
			}
		}
	}

	private void disposerNoeuds()
	{
		/**
		 * On remplit la matrice
		 */
		int n ;
		for (n=0;n<ParametresGeneraux.nbrNoeuds;++n)
		{
			/**
			 * Indice x et y du futur noeud dans la matrice
			 */
			int xPos ;
			int yPos ;
		
			/**
			 * On cherche dans le vecteur des cases libres une case libre ...
			 */
			int indVectCasesLibres = (int)(Math.random()*vectCasesLibres.size()-1);
			
			/**
			 * On rcupre les Coordonnees d'un lment libre de la matrice
			 */
			Coordonnees coordNoeud ;
			coordNoeud = vectCasesLibres.get(indVectCasesLibres) ;
			xPos = coordNoeud.X ;
			yPos = coordNoeud.Y ;

			/**
			 * La case n'est plus libre, on l'enlve du vecteur
			 */
			vectCasesLibres.remove(indVectCasesLibres);

			/**
			 * On insre le noeud dans la matrice
			 */
			NoeudGraphe nouveauNoeud = new NoeudGraphe (n) ; // pensez  creer la classe NoeudGraphe ;-)
			nouveauNoeud.setXPos(xPos);
			nouveauNoeud.setYPos(yPos);
			matriceNoeuds[xPos][yPos] = nouveauNoeud ;

			/**
			 * On insre le noeud dans le vecteur des noeuds
			 */
			vectNoeuds.add(nouveauNoeud);
		}
	}

	private void rechercherVoisinsCote   (NoeudGraphe noeudCentral,
										  int rayon,
										  int horizontal,
										  int vertical)
	{
		NoeudGraphe noeudVoisin = null ;
		
		int xPosNoeud = noeudCentral.getXPos() ;
		int yPosNoeud = noeudCentral.getYPos() ;
		
		int xPosTmp ;
		int yPosTmp ;
		
		/**
		 * C'est une recherche horizontale
		 */
		if (vertical == 0)
		{
			yPosTmp = yPosNoeud + rayon * horizontal;
			for(xPosTmp=xPosNoeud-rayon+1 ;
				xPosTmp<=xPosNoeud+rayon-1 ;
				++xPosTmp)
			{
				noeudVoisin = getNoeudMatrice(xPosTmp,yPosTmp);
				if (noeudVoisin != null)
					lierVoisin(noeudCentral,noeudVoisin,false);
			}
		}
		/**
		 * C'est une recherche verticale
		 */
		else
		{
			xPosTmp = xPosNoeud + rayon * vertical ;
			for(yPosTmp=yPosNoeud-rayon;
				yPosTmp<=yPosNoeud+rayon;
				++yPosTmp)
			{
				noeudVoisin = getNoeudMatrice(xPosTmp,yPosTmp);
				if (noeudVoisin != null)
					lierVoisin(noeudCentral,noeudVoisin,false);
			}
		}
	}

	private void rechercherVoisins(NoeudGraphe noeudCentral,
								   int rayon)
	{
		rechercherVoisinsCote(noeudCentral,rayon, 0,-1);
		rechercherVoisinsCote(noeudCentral,rayon, 0, 1);
		rechercherVoisinsCote(noeudCentral,rayon,-1, 0);
		rechercherVoisinsCote(noeudCentral,rayon, 1, 0);
	}

	public NoeudGraphe getNoeudMatrice(int x,int y)
	{
		if (x<0 || 
			y<0 || 
			x >= ParametresGeneraux.nbrLignes ||
			y >= ParametresGeneraux.nbrColonnes) return null ;
		
		return matriceNoeuds[x][y];
	}
	
	private void lierVoisin(NoeudGraphe noeud1,
			NoeudGraphe noeud2,
							Boolean outrePasserLimites)
	{
		if ((noeud1.getNbrVoisins() < ParametresGeneraux.nbrArretesMin &&
			 noeud2.getNbrVoisins() < ParametresGeneraux.nbrArretesMax) ||
			outrePasserLimites == true || 
			ParametresGeneraux.favoriserPuits == true)
		{
			noeud1.ajouterVoisin(noeud2.getIdNoeud());
			noeud2.ajouterVoisin(noeud1.getIdNoeud());
			matriceAdjacence[noeud1.getIdNoeud()][noeud2.getIdNoeud()] = 1;
			matriceAdjacence[noeud2.getIdNoeud()][noeud1.getIdNoeud()] = 1;
		}
	}
	
	private void lierNoeuds ()
	{
		/**
		 * Pour chaque noeuds, on cre les arrtes avec les noeuds voisins
		 */
		int m;
		for(m=0;m<vectNoeuds.size();++m)
		{
			NoeudGraphe monNoeud = vectNoeuds.get(m);
			int xPosNoeud = monNoeud.getXPos();
			int yPosNoeud = monNoeud.getYPos();
		
			/**
			 * Le noeud se connecte avec autant de voisins que nécessaire
			 */
			int nbrArretesNoeud ;
			int niveau = 1 ;
			
			while(monNoeud.getNbrVoisins() < ParametresGeneraux.nbrArretesMin &&
				  niveau <= ParametresGeneraux.nbrNoeuds/2)
			{
				rechercherVoisins(monNoeud,niveau);
				++niveau;
			}
		}			
	}

	private void matriceVersGraphe()
	{
		for(int i=0;i<vectNoeuds.size();++i)
		{
			vectNoeuds.get(i).setXPos(vectNoeuds.get(i).getXPos()*ParametresGeneraux.nbrPixels);
			vectNoeuds.get(i).setYPos(vectNoeuds.get(i).getYPos()*ParametresGeneraux.nbrPixels);
		}		
	}
	
	public void afficherMatrice(int [][] matrice,
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
	
	public Vector <NoeudGraphe> getVectNoeud()
	{	
		return vectNoeuds;
	}
}