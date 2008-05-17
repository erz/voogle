package libWarThreads;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Console extends JPanel {
	
	private static JTextArea sortie;
	
	public Console() {
		sortie = new JTextArea();
		setLayout(new GridLayout(1,1));
		add(new JScrollPane(sortie));
		setPreferredSize(new Dimension(300, 75));
	}
	
	public static void ecrire(String s) {
		sortie.append(s + "\n");
	}
	
	public static void effacer() {
		sortie.setText("");
	}
	
	public static JTextArea getSortie() {
		return sortie;
	}

}
