package fidele.ihm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fidele.entite.Fidele;

import libWarThreads.Console;

@SuppressWarnings("serial")
public class JFrameFidele extends JFrame {
	
	private Fidele fidele;
	private JTextField ipDieu;
	private JTextField portDieu;
	private JButton rejoindreDieu;
	private JButton attendreDieu;
	
	public JFrameFidele() {
		fidele = new Fidele();
		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout());
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
		JPanel jpPort = new JPanel(new FlowLayout());
		jpPort.add(new JLabel("Dieu - ip : port"));
		jpPort.add(ipDieu = new JTextField("127.0.0.1", 8));
		jpPort.add(new JLabel(":"));
		jpPort.add(portDieu = new JTextField("4000", 4));
		centre.add(jpPort);
		rejoindreDieu = new JButton("Rejoindre dieu");
		centre.add(rejoindreDieu);
		centre.add(attendreDieu = new JButton("Attendre dieu"));
		contenu.add(BorderLayout.CENTER, centre);
		contenu.add(BorderLayout.SOUTH, new Console());
		setTitle("Fidele");
		setSize(300, 200);
		setVisible(true);
		
		rejoindreDieu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					fidele.getFideleReseau().rejoindreDieu(ipDieu.getText(), Integer.valueOf(portDieu.getText()));
				} catch (java.lang.NumberFormatException e) {
					Console.ecrire("Mauvais format");
				}
			}
			
		});
		
		attendreDieu.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					fidele.getFideleReseau().attendreDieu();
				} catch (java.lang.NumberFormatException e) {
					Console.ecrire("Mauvais format");
				}
			}
			
		});
	}

}
