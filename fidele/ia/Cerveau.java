package fidele.ia;

import java.util.Vector;

import fidele.Warrior;

/*
 * Cette classe est destinée à être dérivée par autant de joueurs qui veulent jouer
 * Elle sert aussi à garder la correspondance entre les joueurs et leur Cerveau
 * Ainsi selon son propriétaire, le Warrior agira selon le Cerveau de son maitre
 */
public abstract class Cerveau 
{	
	//Tableau des correspondance Joueur/Cerveau (on se limite à 100 joueurs max pour le moment,
	//On devrait pouvoir voir venir)
	private static Vector<Cerveau> vectCerveauJoueur;
	
	public Cerveau ()
	{}
	
	//Phase de réflexion, d'analyse et de recherche d'indices et élements exterieurs
	public abstract void reflechir (Warrior guerrier);
	
	//L'action décidée par le thread en fonction de ce qu'il a réfléchi avant
	//action unique a priori
	public abstract void agir (Warrior guerrier);
	
	/*
	 * Renvoie le Cerveau correspondant au joueur demandé
	 */
	public static Cerveau getCerveau (int joueur)
	{
		return vectCerveauJoueur.get(joueur);
	}
	
	/*
	 * Renvoie le Cerveau correspondant au joueur demandé
	 */
	public static Cerveau setCerveau (int joueur, Cerveau cerv)
	{
		if (vectCerveauJoueur == null)
		{
			vectCerveauJoueur = new Vector<Cerveau> ();
			vectCerveauJoueur.setSize(100);
		}
		return vectCerveauJoueur.set(joueur, cerv);
	}
	
}

