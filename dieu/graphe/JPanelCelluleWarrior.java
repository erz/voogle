package dieu.graphe;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class JPanelCelluleWarrior extends JPanel
{
	private static final long serialVersionUID = 3817107907135042902L;
	
	public JPanelCelluleWarrior(Color color)
	{
		super();
		setVisible(true);
		this.setFocusable(false);
		setBackground(color);
		setPreferredSize(new Dimension(2,10));
		setFocusable(false);
	}
}
