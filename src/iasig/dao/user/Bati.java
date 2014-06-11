package iasig.dao.user;

import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;


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


