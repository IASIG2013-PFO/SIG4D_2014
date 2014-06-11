package iasig.dao.user;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class Raster_Postgre {
		
	private Vector<BufferedImage> tuile = new Vector<BufferedImage>();
	private Vector<Double[]> origine = new Vector<Double[]>();
	 
	
	@SuppressWarnings("null")
	public int nbreElements(){
		if (this.origine.size() == this.tuile.size())
			return this.origine.size();
		else return (Integer) null;
	}
	
	/**
	 * Permet de récupérer un le vecteur Origine complet
	 * @return Vector<Double[]>
	 */
	public Vector<Double[]> getVecteurOrigine()
	{
		return this.origine;
	}

	
	/**
	 * Permet de récupérer un le vecteur tuile complet
	 * @return Vector<BufferedImage>
	 */
	public Vector<BufferedImage> getVecteurTuile()
	{
		return this.tuile;
	}
	
	/**
	 * Permet de récupérer une tuile 
	 * @param  index
	 * @return BufferedImage Retourne la tuile
	 */
	public BufferedImage getTuile(int index)
	{
		return tuile.elementAt(index);
	}
	
	/**
	 * Permet de récupérer les coordonnées XY d'origine de la tuile 
	 * @param index index de la tuile dans le vecteur
	 * @return Double[] retourne un Tableau de double [X, Y]
	 */
	public Double[] getXYOrigine(int index)
	{
		return origine.elementAt(index);
	}
	
	/**
	 * Permet de récupérer les coordonnées X d'origine de la tuile 
	 * @param index index de la tuile dans le vecteur
	 * @return Double	Retourne la coordonnée X sous form d'un double
	 */
	public Double getXorigine(int index)
	{
		return origine.elementAt(index)[0];
	}
	
	/**
	 * Permet de récupérer les coordonnées Y d'origine de la tuile 
	 * @param  index
	 * @return Double
	 */
	public Double getYorigine(int index)
	{
		return origine.elementAt(index)[1];
	}
	
	/**
	 * Setter tuile  
	 * @param img	Passage d'une tuile sous forme d'un objet BufferedImage
	 */
	public void ajoutTuile(BufferedImage img)
	{
		 tuile.add(img);
	}
	
	/**
	 * Setter coordonnées XY d'origine de la tuile indexée selon le vecteur tuile
	 * @param coords	Tableau de double [X, Y]
	 */
	public void addXYOrigine(Double[] coords)
	{
		 origine.add(coords);
	}
	
	
	/**
	 * Vide le vecteur pour par l'objet;
	 * Pas de paramètre
	 */
	public void videRaster(){
		tuile.clear();
		origine.clear();
	}
	
	
}
