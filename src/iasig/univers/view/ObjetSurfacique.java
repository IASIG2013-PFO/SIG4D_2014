package iasig.univers.view;

import org.postgis.PGgeometry;

/**
 * @author emilie
 *
 */
public class ObjetSurfacique {

	/**
	 * attributs
	 * 
	 */
	Integer id; //identifiant de l'objet
	private PGgeometry coord; //coordonn�es terrain L93 au format BDD
	private double Zmax ; //altitude maximale de l'objet
	private String type; //type de l'objet (ex: batiment, v�g�tation, lac...)
	
	
	
	/**constructeur
	 * 
	 * @param coord	==coordonn�es de l'objet
	 * @param rotz ==param�tre d'orientation de l'objet
	 * @param f ==facteur d'�chelle
	 * @param type ==type de l'objet
	 * 
	 */
	protected ObjetSurfacique (Integer id, PGgeometry coord, double zmax, String type){
		this.coord=coord;
		this.Zmax=zmax;
		this.type=type;
	}

	//méthodes publique Accesseur
	public PGgeometry getCentroid()
	{
		return coord;
	}
	public double getZmax(){return this.Zmax;}
	public String getType(){return this.type;}
	
}
