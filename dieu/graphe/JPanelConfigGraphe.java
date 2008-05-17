package dieu.graphe;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import libWarThreads.ParametresGeneraux;

import dieu.JFrameDieu;

@SuppressWarnings("serial")
public class JPanelConfigGraphe extends JPanel 
{
	private JLabel jLabelNbrNoeuds = null;
	private JLabel jLabelNbrLignes  = null;
	private JLabel jLabelNbrColonnes = null;
	private JLabel jLabelNbrArretesMin = null;
	private JLabel jLabelNbrArretesMax = null;
	private JLabel jLabelModePuit = null;
	
	private JComboBox jComboBoxNbrNoeuds = null;
	private JComboBox jComboBoxNbrLignes = null;
	private JComboBox jComboBoxNbrColonnes = null;
	private JComboBox jComboBoxNbrArretesMin = null;
	private JComboBox jComboBoxNbrArretesMax = null;
	private JComboBox jComboBoxModePuit = null;

	private JButton creerGraphe = null;
	
	private JFrameDieu jFrameDieu = null ; 
	
	public JPanelConfigGraphe(JFrameDieu jFrameDieu)
	{
		this.jFrameDieu = jFrameDieu ;
		jLabelNbrNoeuds = new JLabel ("Nombre de noeuds : ");
		jLabelNbrLignes = new JLabel ("Nombre de lignes : ");
		jLabelNbrColonnes = new JLabel ("Nombre de colonnes : ");
		jLabelNbrArretesMin = new JLabel ("Nombre d'arrêtes minimum :");
		jLabelNbrArretesMax = new JLabel ("Nombre d'arrêtes maximum :");
		jLabelModePuit = new JLabel ("Mode puit :");
		
		jComboBoxNbrNoeuds = new JComboBox();
		jComboBoxNbrColonnes = new JComboBox();
		jComboBoxNbrLignes = new JComboBox();
		jComboBoxNbrArretesMin = new JComboBox();
		jComboBoxNbrArretesMax = new JComboBox();
		jComboBoxModePuit = new JComboBox();
		
		for (int i=2; i<=200; i++) jComboBoxNbrNoeuds.addItem(new Integer(i));
		for (int i=1; i<=40; i++) jComboBoxNbrColonnes.addItem(new Integer(i));		
		for (int i=1; i<=20; i++) jComboBoxNbrLignes.addItem(new Integer(i));
		for (int i=1; i<=4; i++) jComboBoxNbrArretesMin.addItem(new Integer(i));		
		for (int i=4; i<=8; i++) jComboBoxNbrArretesMax.addItem(new Integer(i));
		jComboBoxModePuit.addItem("Activé");
		jComboBoxModePuit.addItem("Désactivé");
	
		jComboBoxNbrNoeuds.setSelectedIndex(ParametresGeneraux.nbrNoeuds - 2);
		jComboBoxNbrColonnes.setSelectedIndex(ParametresGeneraux.nbrColonnes - 1);
		jComboBoxNbrLignes.setSelectedIndex(ParametresGeneraux.nbrLignes - 1);
		jComboBoxNbrArretesMin.setSelectedIndex(ParametresGeneraux.nbrArretesMin - 1);
		jComboBoxNbrArretesMax.setSelectedIndex(ParametresGeneraux.nbrArretesMax - 4);
		
		setLayout(new GridLayout(7,2));
		add(jLabelNbrNoeuds);
		add(jComboBoxNbrNoeuds);
		add(jLabelNbrLignes);
		add(jComboBoxNbrLignes);
		add(jLabelNbrColonnes);
		add(jComboBoxNbrColonnes);
		add(jLabelNbrArretesMin);
		add(jComboBoxNbrArretesMin);
		add(jLabelNbrArretesMax);		
		add(jComboBoxNbrArretesMax);
		add(jLabelModePuit);
		add(jComboBoxModePuit);

		add(creerGraphe = new JButton("Créer graphe"));

		creerGraphe.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent e) 
			{
				int nbrNoeuds = (Integer)jComboBoxNbrNoeuds.getSelectedItem() ;
				int nbrLignes   = (Integer)jComboBoxNbrLignes.getSelectedItem() ;
				int nbrColonnes = (Integer)jComboBoxNbrColonnes.getSelectedItem() ;
				int nbrArretesMin = (Integer)jComboBoxNbrArretesMin.getSelectedItem();
				int nbrArretesMax = (Integer)jComboBoxNbrArretesMax.getSelectedItem();
				String modePuit = (String)jComboBoxModePuit.getSelectedItem();
				Boolean estModePuit = false ;
				if (modePuit.equals("Activé"))
					estModePuit = true ;
				
				ParametresGeneraux.setParam(nbrNoeuds,
															   nbrLignes,
															   nbrColonnes,
															   nbrArretesMin,
															   nbrArretesMax,
															   45,
															   estModePuit) ;
				Graphe graphe = JPanelConfigGraphe.this.jFrameDieu.getDieu().getGraphe();
				graphe.initialiser();
				JPanelConfigGraphe.this.jFrameDieu.getJPanelVueJeu().initialiser(graphe);
			}
		});
	}
}
