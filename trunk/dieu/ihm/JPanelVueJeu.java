package dieu.ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

import libWarThreads.InfoModification;
import libWarThreads.InfoNoeud;
import libWarThreads.InfoThread;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

import dieu.graphe.Cercle;
import dieu.graphe.Coordonnees;
import dieu.graphe.Graphe;
import dieu.graphe.JPanelCelluleNoeud;
import dieu.graphe.JPanelCelluleWarrior;
import fidele.entite.Warrior;

@SuppressWarnings("serial")
public class JPanelVueJeu extends JPanel implements Observer
{
	// Informations sur le graphe  reprsenter
	private Graphe graphe = null ;
	
	// Reprsentation graphique du graphe
	private JGraph jgraph = null;
	
	// Liste des cellules ( reprsentant uniquement les noeuds )
	public Vector <CelluleNoeud> vectCelluleNoeud = null;

	public Lock verrouReload = null ;
	
	public JPanelVueJeu()
	{
		super();
		setVisible(true);
	}
	
	public void initialiser(Graphe Reseau)
	{
		graphe = Reseau ;
		graphe.addObserver(this);
		
		verrouReload = new ReentrantLock();
		
		vectCelluleNoeud = new Vector <CelluleNoeud>(graphe.getTaille());
		vectCelluleNoeud.setSize(graphe.getTaille());

		jgraph = new JGraph(new DefaultGraphModel());
		jgraph.setEditable(false);
		jgraph.setDragEnabled(false);
		jgraph.setDropEnabled(false);
		jgraph.setMoveable(false);
		jgraph.setFocusable(true);
		jgraph.setSelectionEnabled(false);
		jgraph.getSelectionModel().setChildrenSelectable(false);
		
		jgraph.getGraphLayoutCache().setFactory(

		new DefaultCellViewFactory()
		{
			public CellView createView(GraphModel model, Object cell)
			{
				if 		(model.isPort(cell))	return new PortView(cell);
				else if (model.isEdge(cell))	return new EdgeView(cell);
				else
				{
					Cellule cellule = (Cellule) cell ;
					if (cellule.getType().equals("noeud")) 	return new VueNoeud(cell);
				}
				return new VertexView (cell);
			}
		});

		genererNoeuds();
		genererLiens();
		add(jgraph);
	}
	
	private void genererNoeuds()
	{
    	for(int i=0;i<graphe.getTaille();i++)
    	{		
    		//numero de case dans le tableau pour placer le sommet
    		String noeud = String.valueOf(graphe.getNoeud(i).getIdNoeud());
    		CelluleNoeud celluleNoeud = creerCelluleNoeud(noeud,
														  graphe.getNoeud(i).getXPos(),
														  graphe.getNoeud(i).getYPos());
    	}
    	jgraph.getGraphLayoutCache().toFront(vectCelluleNoeud.toArray());
	}
	
    private void genererLiens()
    {
    	int nbrNoeuds = graphe.getTaille();

    	for(int i=0;i<nbrNoeuds;i++)
    	{		
    		//Ports pour les voisins
    		Vector <DefaultEdge> vectEdges = new Vector<DefaultEdge> ();
    		
    		for(int j=0;j<graphe.getNoeud(i).getVectVoisin().size();j++)
    		{	
    			DefaultEdge nouveauLien = new DefaultEdge();
    			
    			DefaultPort portSource = new DefaultPort() ;
    			DefaultPort portDest = new DefaultPort() ;
    			
    			vectCelluleNoeud.get(i).add(portSource);
    			vectCelluleNoeud.get(graphe.getNoeud(i).getVectVoisin().get(j)).add(portDest);
    			
    			jgraph.getGraphLayoutCache().insertEdge(nouveauLien,portSource,portDest);
				vectEdges.add(nouveauLien) ;
    		}
    		vectCelluleNoeud.get(i).vectEdges = vectEdges ;
        	jgraph.getGraphLayoutCache().toBack(vectEdges.toArray());
    	}
    }
  
	public CelluleNoeud creerCelluleNoeud(String name,
										  double x,
										  double y)
	{
			// Creation d'une cellule
			CelluleNoeud cell = new CelluleNoeud(name);
			
			// Ajout de la cellule au vecteur des cellules noeuds
			vectCelluleNoeud.set(new Integer(name), cell);
			
			// Taille et position
			GraphConstants.setBounds(cell.getAttributes(),
									 new Rectangle2D.Double(x, y, 40, 40));

			GraphConstants.setOpaque(cell.getAttributes(),
									true);
			
    		jgraph.getGraphLayoutCache().insert(cell);
    		jgraph.repaint();
    		return cell;
	}

	private class Cellule extends DefaultGraphCell
	{
		protected String stringType;
		public Cellule (String name)
		{
			super(name);
			stringType = "gnrique";
		}
		
		public String getType ()
		{
			return stringType ;
		}
		
	}
	
	private class CelluleNoeud extends Cellule
	{
		int idCellule ;
		public JPanelCelluleNoeud jPanelCelluleNoeud ;

		// Liste des edges
		public Vector <DefaultEdge> vectEdges = null;
		
		public CelluleNoeud (String name)
		{
			super(name);
			idCellule = new Integer(name).intValue();
			stringType = "noeud";
			jPanelCelluleNoeud = new JPanelCelluleNoeud(idCellule);
		}
	}
	

	private class VueNoeud extends VertexView 
	{
		public VueNoeud(Object cell) 
		{ 
			super(cell);
		}
		
		public Component getRendererComponent(JGraph graph,
											  boolean selected,
											  boolean focus,
											  boolean preview) 
		{
			CelluleNoeud celluleNoeud = (CelluleNoeud) getCell() ;
			return celluleNoeud.jPanelCelluleNoeud;
		}
		
	}

	public void  genererThreadDeplacement (final InfoThread infoThread)
	{
		// Coordonnées du noeud de départ
		final int xDep = graphe.getNoeud(infoThread.getIdNoeudDepart()).getXPos() + 20;
		final int yDep = graphe.getNoeud(infoThread.getIdNoeudDepart()).getYPos() + 20;
		
		// Coordonnées du noeud d'arrivé
		final int xArr = graphe.getNoeud(infoThread.getIdNoeudArrivee()).getXPos() + 20;
		final int yArr = graphe.getNoeud(infoThread.getIdNoeudArrivee()).getYPos() + 20;
		
		new Thread() 
		{
			@Override
			public void run() 
			{
				Cercle cercleTrajectoire = null ;
				cercleTrajectoire = new Cercle(new Coordonnees(xDep,yDep)) ;
				
				// On calcule l'angle et le rayon par rapport au noeud de départ
				cercleTrajectoire.setCoordonneesPoint(new Coordonnees(xArr,yArr));
				
				float distanceFinale = cercleTrajectoire.getRayon() ;
			
				float distanceActuelle = 0 ;
				
				// On met le rayon ( distance parcourue ) à 0
				cercleTrajectoire.setRayon(distanceActuelle);
				
				
				int proprietaire = infoThread.getProprietaire();
				Color couleurProprietaire = null ;
				if ( proprietaire == 0) couleurProprietaire = Color.red ;
				else couleurProprietaire = Color.green ;
				
				// On récupère la position du thread
				int xCur = xDep ;
				int yCur = yDep ;
				
				Graphics g = JPanelVueJeu.this.jgraph.getGraphics();
				
				while (distanceActuelle < distanceFinale)
				{
					distanceActuelle += 1 ;
					// On fait progresser peu à peu le thread le long de l'arrête
					cercleTrajectoire.setRayon(distanceActuelle);
					
					g.setColor(couleurProprietaire);
					jgraph.repaint(xCur, yCur, 2, 9);
					xCur = cercleTrajectoire.getCoordonneesPoint().X ;
					yCur = cercleTrajectoire.getCoordonneesPoint().Y ;
					g.fillRect(xCur, yCur, 2, 9);
					
					try 
					{
						sleep(10);
					} 
					catch (InterruptedException e) 
					{
						// TODO Bloc catch auto-généré
						e.printStackTrace();
					}
				}
				jgraph.repaint(xCur, yCur, 2, 9);
			}
		}.start();
	}
	
	public void update(Observable arg0, Object o)
	{
		InfoModification info = (InfoModification)o;
		if (info instanceof InfoNoeud) 
		{
			InfoNoeud infoNoeud = (InfoNoeud)info;
			CelluleNoeud celluleNoeud = vectCelluleNoeud.get(infoNoeud.getIdentifiantNoeud());
			celluleNoeud.jPanelCelluleNoeud.setInfo(infoNoeud);
			
			int xPosCell,yPosCell ;
			xPosCell = graphe.getNoeud(infoNoeud.getIdentifiantNoeud()).getXPos();
			yPosCell = graphe.getNoeud(infoNoeud.getIdentifiantNoeud()).getYPos();
			jgraph.repaint(xPosCell,yPosCell,40,40);
		}
		else if (info instanceof InfoThread)
		{
			InfoThread infoThread = (InfoThread)info;
			if (infoThread.getEtat() == Warrior.ETAT_DEPLACEMENT )
			{
				genererThreadDeplacement(infoThread);
				//System.out.println(infoThread);
			}
		}
	}
}
