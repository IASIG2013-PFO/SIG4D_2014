/**
 * 
 */
package iasig.mobile.view;

import org.postgis.PGgeometry;

/**
 * @author emilie
 *
 */
public class ObjetLineaire {

	/**
	 * attributs
	 * 
	 */
	Integer id; //identifiant de l'objet
	private PGgeometry coord; //coordonnées terrain L93 au format BDD
	private String type; //type de l'objet (ex: batiment, végétation, lac...)
	
	
	
	/**constructeur
	 * 
	 * @param coord	==coordonnées de l'objet
	 * @param type ==type de l'objet
	 * 
	 */
	protected ObjetLineaire(Integer id, PGgeometry coord, String type){
		this.coord=coord;
		this.type=type;
	}

	//mÃ©thodes publique Accesseur
	public PGgeometry getCentroid(){return coord;}
	public String getType(){return this.type;}
	
}
