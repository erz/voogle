package fidele;

import java.util.TreeMap;

import libWarThreads.Console;
import libWarThreads.InfoNoeud;
import libWarThreads.InfoThread;

import fidele.ia.*;

public class Fidele {
	
	private TreeMap<Integer, Noeud> noeudsHebergs;
	private FideleReseau fideleReseau;
	
	public Fidele() {
		fideleReseau = new FideleReseau(this);
		noeudsHebergs = new TreeMap<Integer, Noeud>();
		
		//Initialisation IA/Joueur
		initIA();
	}
	
	public void instancierNoeud(InfoNoeud info) {
		if (info.isBase())
			noeudsHebergs.put(info.getIdentifiantNoeud(), new NoeudBase(info, this));
		else
			noeudsHebergs.put(info.getIdentifiantNoeud(), new Noeud(info, this));
	}
	
	public void creerUnLien(InfoNoeud infoNoeud1, InfoNoeud infoNoeud2, int distance) {
		noeudsHebergs.get(infoNoeud1.getIdentifiantNoeud()).connecterAUnNoeud(infoNoeud2, distance);
	}
	
	public void envoyerInfoNoeudADieu(InfoNoeud info) {
		fideleReseau.envoyerInfoNoeud(info);
	}
	
	public void envoyerInfoThreadADieu(InfoThread info) {
		fideleReseau.envoyerInfoThread(info);
	}
	
	public void mettreLesNoeudsEnAction() {
		for (int i=0; i<noeudsHebergs.size(); i++)
			noeudsHebergs.get(i).mettreEnAction();
	}
	
	public FideleReseau getFideleReseau() {
		return fideleReseau;
	}
	
	
	public void initIA ()
	{
		//Temporaire : pour chaque joueur on lui attribue une IA
		Cerveau.setCerveau(0, new CerveauJ1((float) 0.25));
		Cerveau.setCerveau(1, new CerveauJ1((float) 0.75));
	}

	public TreeMap<Integer, Noeud> getNoeudsHebergs() {
		return noeudsHebergs;
	}
}
