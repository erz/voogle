package dieu.ihm;

import javax.swing.JPanel;

import dieu.graphe.JPanelConfigGraphe;

@SuppressWarnings("serial")
public class JPanelConfiguration extends JPanel
{
	public JPanelConfiguration(JFrameDieu jFrameDieu)
	{
		add (new JPanelMachines(jFrameDieu));
		add (new JPanelConfigGraphe(jFrameDieu));
	}
}

