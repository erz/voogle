package fidele.entite;

import libWarThreads.reseau.InfoNoeud;

public class NoeudBase extends Noeud 
{
	boolean pondre;

	public NoeudBase(InfoNoeud info, Fidele fidele) {
		super(info, fidele);
		pondre = true;
	}
	
	public void pondreUnThread() {
		Warrior w = new Warrior(this);
		ajouterThread(w);
		w.start();
	}
	
	public void pondreEnContinu() {
		new Thread() {
			@Override
			public void run() {
				while (pondre) {
					pondreUnThread();
					try {
						sleep(60000 / getFrequencePondaison());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start();
	}
	
	@Override
	public void mettreEnAction() {
		super.mettreEnAction();
		pondreUnThread();
	}
	
	@Override
	public void stopperNoeud() 
	{
		super.stopperNoeud();
		pondre = false;
	}
	
	@Override
	public boolean isBase(){
		return true;
	}

}
