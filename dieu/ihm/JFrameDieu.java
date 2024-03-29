package dieu.ihm;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import dieu.entite.Dieu;

import libWarThreads.util.Console;

/**
 * Fen�tre graphique du programme dieu
 * @author Tony
 *
 */
@SuppressWarnings("serial")
public class JFrameDieu extends JFrame {
	
	private Dieu dieu;
	private JTabbedPane onglets;

	JPanelConfiguration jPanelConfiguration = null ;
	public JPanelVisualisation jPanelVueJeu = null ;
	
	public JFrameDieu() {
		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout());
		contenu.add(BorderLayout.SOUTH, new Console());
		dieu = new Dieu();
		onglets = new JTabbedPane(JTabbedPane.TOP);
		onglets.addTab("Configuration",jPanelConfiguration = new JPanelConfiguration(this));
		onglets.addTab("Visualisation",jPanelVueJeu = new JPanelVisualisation());
		contenu.add(BorderLayout.CENTER, onglets);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dieu");
		setVisible(true);
		pack();
	}
	
	public Dieu getDieu() {
		return dieu;
	}

	public JPanelVisualisation getJPanelVueJeu()
	{
		return jPanelVueJeu ;
	}
	
}
