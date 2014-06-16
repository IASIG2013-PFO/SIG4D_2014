/**
 * 
 */
package iasig.univers.view;

import org.postgis.PGgeometry;
import org.postgis.Point;
/**
 * @author emilie
 *
 */
public class ObjetPonctuel {

	/**
	 * attributs
	 * 
	 */
	Integer id; //identifiant de l'objet
	private PGgeometry coord; //coordonn�es terrain L93 au format BDD
	private double rotZ ; //param�tre d'orientation de l'objet
	private float f ; //facteur d'�chelle
	private String type; //type de l'objet (ex: maison1, maison2...)
	
	/**constructeur vide
	 */
	protected ObjetPonctuel (){}
	
	
	/**constructeur
	 * 
	 * @param coord	==coordonn�es de l'objet
	 * @param rotz ==param�tre d'orientation de l'objet
	 * @param f ==facteur d'�chelle
	 * @param type ==type de l'objet
	 * 
	 */
	protected ObjetPonctuel (Integer id, PGgeometry coord, double rotz, float f, String type){
		this.coord=coord;
		this.rotZ=rotz;
		this.f=f;
		this.type=type;
	}

	//méthodes publique Accesseur
	public PGgeometry getCentroid()
	{
		return coord;
	}
	public double getX(){
		Point pt = (Point)coord.getGeometry();
		return pt.x;
	}
	public double getY(){
		Point pt = (Point)coord.getGeometry();
		return pt.y;
	}
	public double getZ(){
		Point pt = (Point)coord.getGeometry();
		return pt.z;
	}
	public double getf(){return this.f;}
	public double getRotZ(){return this.rotZ;}
	public String getType(){return this.type;}
	
	//setter
	public void setX(double x,double y,double z ){
	}
	public void setY(double y){
		Point pt = (Point)coord.getGeometry();
		pt.y=y;
	}
	public void setZ(double z){
		Point pt = (Point)coord.getGeometry();
		pt.z=z;
	}
	
}
