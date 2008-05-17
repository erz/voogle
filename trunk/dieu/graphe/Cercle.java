package dieu.graphe;

public class Cercle 
{
	public Coordonnees coordonneesCentre = null ;
	public Coordonnees coordonneesPoint = null ;
	
	private float angle;
	private float rayon;
	
	public void calculerCoordPoint ()
	{
		coordonneesPoint.X = (int) (coordonneesCentre.X + (rayon * Math.cos(angle)));
		coordonneesPoint.Y = (int) (coordonneesCentre.Y + (rayon * Math.sin(angle)));
	}
	
	private void calculerAngleEtRayon() 
	{
		if(coordonneesCentre.X != coordonneesPoint.X)
		{
			rayon = (float) Math.sqrt(Math.pow((coordonneesPoint.X - coordonneesCentre.X),2)+Math.pow((coordonneesPoint.Y-coordonneesCentre.Y),2));
			if((coordonneesCentre.Y < coordonneesPoint.Y))
				angle = (float) Math.acos(((coordonneesPoint.X - coordonneesCentre.X)/rayon));
			else
				angle = -(float) Math.acos(((coordonneesPoint.X - coordonneesCentre.X)/rayon));

		}
		else
		{
			if(coordonneesCentre.Y > coordonneesPoint.Y)
			{
				angle = (float) ((3*Math.PI)/2);
				rayon = Math.abs(coordonneesPoint.Y-coordonneesCentre.Y);
			}
			else
			{
				angle = (float) ((Math.PI)/2);
				rayon = Math.abs(coordonneesCentre.Y-coordonneesPoint.Y);
			}
		}
	}
	
	public Cercle(Coordonnees coordonneesCentre)
	{
		this.coordonneesCentre = coordonneesCentre ;
		this.angle = 0 ;
		this.rayon = 0 ;
	}
	
	public void setAngle ( float nouvelAngle )
	{
		angle = nouvelAngle ;
		calculerCoordPoint();
	}
	
	public void setRayon (float nouveauRayon)
	{
		rayon = nouveauRayon;
		calculerCoordPoint();
	}
	
	public void setCoordonneesPoint ( Coordonnees coordonneesPoint )
	{
		this.coordonneesPoint = coordonneesPoint;
		calculerAngleEtRayon();
	}

	public Coordonnees getCoordonneesPoint ()
	{
		return coordonneesPoint ;
	}
	
	public float getAngle ()
	{
		return angle ;
	}
	
	public float getRayon ()
	{
		return rayon ;
	}
}
