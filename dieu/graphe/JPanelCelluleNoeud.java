package dieu.graphe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import libWarThreads.reseau.InfoNoeud;

public class JPanelCelluleNoeud extends JPanel
{
	private static final long serialVersionUID = -274269934592320120L;

	Object cell;
	public InfoNoeud infoNoeud;
	
	public JPanelCelluleNoeud(Object c)
	{
		super();
		cell=c;
		initialiser();
		setVisible(true);
	}
	
	public void setInfo(InfoNoeud i)
	{
		infoNoeud = i;
		actualiserConnection();
		actualiserCamp();
	}
	
	public void initialiser()
	{
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new ContourNoeud(Color.BLACK,10,10));
		setBackground(Color.GRAY);
		setFocusable(false);
		setSize(40,40);
	}
	
	private void actualiserCamp()
	{
		if (infoNoeud.getProprietaire()==-1) setBackground(Color.white);
		if (infoNoeud.getProprietaire()== 0) setBackground(Color.red);
		if (infoNoeud.getProprietaire()== 1) setBackground(Color.green);
	}
	
	private void actualiserConnection()
	{
		if(infoNoeud.isConnecte())setBackground(Color.white);
		else setBackground(Color.gray);
	}
	
	public JLabel getJlabel(){
		
		JLabel myLabelOrdonnancement = new JLabel(cell.toString());
		myLabelOrdonnancement.setHorizontalAlignment(SwingConstants.CENTER);
		myLabelOrdonnancement.setAlignmentX(Component.CENTER_ALIGNMENT);
		return myLabelOrdonnancement;
	}
	
	public JLabel getJlabel1(){
		JLabel nombreThreads = new JLabel();
		return nombreThreads;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawString(cell.toString(), 15, 15);
	
	
		int nbrThreadsJ1 = 0;
		int nbrThreadsJ2 = 0;
		
		for(int i=0;i<infoNoeud.getProprietairesThreads().length;++i)
		{
			if (infoNoeud.getProprietairesThreads()[i] == 0 ) nbrThreadsJ1++ ;
			else nbrThreadsJ2++;	
		}
		
		for(int n=0;n<nbrThreadsJ1;++n)
		{
			g.setColor(Color.RED);
			g.fillRect(n*4+5,14,2, 9);
		}
		
		for(int n=0;n<nbrThreadsJ2;++n)
		{
			g.setColor(Color.GREEN);
			g.fillRect(n*4+5,26,2, 9);
		}
	}
}
