package fidele.trame;

import libWarThreads.trame.DonneeTrame;
import fidele.entite.Warrior;

@SuppressWarnings("serial")
public class DTThreadMigrant extends DonneeTrame {
	
	private Warrior warrior;
	
	public DTThreadMigrant(Warrior warrior) {
		this.warrior = warrior;
	}
	
	public Warrior getWarrior() {
		return warrior;
	}

}
