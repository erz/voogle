package fidele;

import libWarThreads.InfoNoeud;

public class NoeudBase extends Noeud {

	public NoeudBase(InfoNoeud info, Fidele fidele) {
		super(info, fidele);
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
				while (true) {
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
	public boolean isBase(){
		return true;
	}

}
