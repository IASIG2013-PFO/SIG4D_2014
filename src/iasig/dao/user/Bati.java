package iasig.dao.user;

import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;

/**
 * Classe prototype définissant un élement de bâti indifférencié
 * Obsolète - le constructeur des instances du bati est déplacé vers iasig.mobile.view
 * Les attributs sont un extrait avec les champs de la BDD de la table du bâti
 */
public class Bati {

	private String id;
	//attribut types Postgis
	private PGgeometry geom;	
	
	public Bati(String id, PGgeometry geom) {
		this.id = id;
		this.geom = geom;
	}

	
	/**
	 * Getter sur le multipolygone l'objet
	 * retourne un multipolygone type Postgis
	 */
	public MultiPolygon getMultipolygon(){
		MultiPolygon poly = (MultiPolygon)geom.getGeometry();
		return poly;
	}
	
	/**
	 * Getter sur l'id l'objet
	 * retourne un String
	 */
	public String getID(){
		return id;
	}
	
	
}


