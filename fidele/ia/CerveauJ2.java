package fidele.ia;


import java.util.ArrayList;
import java.util.Vector;
import fidele.*;
import fidele.entite.Noeud;
import fidele.entite.Warrior;

public class CerveauJ2 extends Cerveau
{
	//noeuds voisins
	private Vector <Noeud> vectNoeudsVoisins = new Vector<Noeud>();
	
	//nb de thread amis sur chaque noeud voisin
	private Vector <Integer> vectNbThreadsA = new Vector<Integer>();

	//nb de thread ennemis sur chaque noeud voisin
	private Vector <Integer> vectNbThreadsE = new Vector<Integer>();
	
	//coeff de chaque noeud voisin
	private Vector <Integer> vectCoeffVoisins = new Vector<Integer>();
	
	//nb de thread amis sur notre noeud
	private int nbThreadsA;
	
	//nb de thread ennemis sur notre noeud
	private int nbThreadsE;
	
	//coeff de notre noeud
	private int coeff;
	
	public CerveauJ2 ()
	{}
	
	@Override
	public void reflechir(Warrior guerrier) 
	{
		//On récupère toutes les informations
		
		//noeuds voisins
		vectNoeudsVoisins.setSize(guerrier.getNoeud().getNombreVoisins());
		//
		
		ArrayList<Warrior> listeWarriors = new ArrayList<Warrior>();
		
		
		//////////////////////////////::
		
		//nombre de threads amis et ennemis sur notre noeud
		nbThreadsA = 0;
		nbThreadsE = 0;
		for (int i = 0; i < listeWarriors.size(); ++i)
		{
			if (listeWarriors.get(i).getProprietaire() == guerrier.getProprietaire())
				nbThreadsA = nbThreadsA + 1;
			else
				nbThreadsE = nbThreadsE + 1;
		}
		
		//coeff de notre noeud
		coeff = guerrier.getNoeud().getCoefficient();
	}

	@Override
	public void agir(Warrior guerrier) 
	{
		//On regarde les informations qu'on a et on agit en conséquence 
		
		//On attaque notre noeud ou pas ?
		
		//Le noeud est neutre : on essaye de le prendre
		if (guerrier.getNoeud().getProprietaire() == -1)
		{
			guerrier.combattre();
			return;
		}
		
		//Le noeud n'est pas neutre : on regarde si on a une chance
		if (nbThreadsA >= (nbThreadsE + coeff))
		{
			guerrier.combattre();
			return;
		}
		
		//On s'enfuit
		//On cherche un noeud voisin plus accueillant
		//Dans l'ordre :
		//Un noeud neutre
		//Un noeud ou on a l'avantage
		//un noeud avec le rapport amis/ennemi+coeff le plus grand
	}
}
