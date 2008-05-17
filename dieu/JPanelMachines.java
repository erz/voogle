package dieu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import libWarThreads.InterfaceReseau;

@SuppressWarnings("serial")
public class JPanelMachines extends JPanel implements Observer {
	
	private static int nbMachines = 0;
	private ArrayList<JPanelConfigMachine> jPanelsConfigMachine;
	private JToggleButton scannerReseau;
	private JButton attendreMachines;
	private JButton implanterGraphe = null;
	private JButton demarrerJeu;
	private JPanel centre;
	
	private JFrameDieu jFrameDieu;
	
	private class JPanelConfigMachine extends JPanel {
		private JComboBox nbNoeuds;
		public JPanelConfigMachine(int numeroMachine, String ip, int port) {
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JPanel noeuds = new JPanel(new FlowLayout());
			nbNoeuds = new JComboBox();
			for (int i=1; i<101; i++)
				nbNoeuds.addItem(new Integer(i));
			noeuds.add(new JLabel("Nombre de noeuds : "));
			noeuds.add(nbNoeuds);
			
			
			add(new JLabel("Machine n° " + numeroMachine));
			add(new JLabel("ip : port " + ip + " : " + String.valueOf(port)));
			add(noeuds);
		}
		
		public int getNombreNoeuds() {
			return nbNoeuds.getSelectedIndex() + 1;
		}
	}
	
	public JPanelMachines(JFrameDieu jFrameDieu) {
		this.jFrameDieu = jFrameDieu;
		jFrameDieu.getDieu().addObserver(this);
		setLayout(new BorderLayout());
		jPanelsConfigMachine = new ArrayList<JPanelConfigMachine>();		
		
		JPanel jpNord = new JPanel(new FlowLayout());
		jpNord.add(scannerReseau = new JToggleButton("Scanner réseau"));
		jpNord.add(attendreMachines = new JButton("Attendre machines"));
		jpNord.add(implanterGraphe = new JButton("Implanter graphe"));
		jpNord.add(demarrerJeu = new JButton("Démarrer jeu"));
		
		add(jpNord, BorderLayout.NORTH);
		
		centre = new JPanel();
		centre.setPreferredSize(new Dimension(300, 500));
		add(new JScrollPane(centre), BorderLayout.CENTER);
		
		scannerReseau.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (scannerReseau.isSelected()) {
					scannerReseau.setText("Scan en cours ...");
					JPanelMachines.this.jFrameDieu.getDieu().rechercherDesFideles(true);
				}
				else
				{
					scannerReseau.setText("Scanner réseau");
					JPanelMachines.this.jFrameDieu.getDieu().rechercherDesFideles(false);
				}
			}
			
		});
		
		attendreMachines.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JPanelMachines.this.jFrameDieu.getDieu().attendreFideles();
			}
			
		});
		
		implanterGraphe.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JPanelMachines.this.jFrameDieu.getDieu().implanterGraphe();
			}

		});
		
		demarrerJeu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JPanelMachines.this.jFrameDieu.getDieu().signalerDebutJeu();
			}
			
		});
	}

	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof InterfaceReseau) {
			InterfaceReseau interfaceDieu = (InterfaceReseau)arg1;
			JPanelConfigMachine jpcm = new JPanelConfigMachine(++nbMachines, interfaceDieu.getIP(), interfaceDieu.getPort());
			jPanelsConfigMachine.add(jpcm);
			centre.add(jpcm);
			jFrameDieu.update(jFrameDieu.getGraphics());
		}
	}

}
