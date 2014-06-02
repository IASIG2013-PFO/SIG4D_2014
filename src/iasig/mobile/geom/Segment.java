package iasig.mobile.geom;

/**
 * 
 * @author iasig.mobile
 * Classe représentant une géométrie segment
 */
public class Segment extends Geometry {

	//Attributs d'objets
	private double length;
	private org.postgis.Point startPoint;
	private org.postgis.Point endPoint;
	private Extent extent;
	
	/**
	 * 
	 * @param id identifiant du segment
	 * @param p1 premier point du segment
	 * @param p2 dernier point du segment
	 */
	public Segment(int id, org.postgis.Point p1, org.postgis.Point p2){
		this.id = id;
		this.startPoint = p1;
		this.endPoint = p2;
		this.length = p1.distance(p2);
		this.extent = new Extent(p1);
		this.extent.updateExtent(p2);
	}
	
	/**
	 * 
	 * @return longeur du segment
	 */
	public double getLength() {
		return length;
	}

	/**
	 * 
	 * @param length longeur du segment
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * 
	 * @return premier point du segment
	 */
	public org.postgis.Point getStartPoint() {
		return startPoint;
	}

	/**
	 * 
	 * @param startPoint premier point du segment
	 */
	public void setStartPoint(org.postgis.Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * 
	 * @return dernier point du segment
	 */
	public org.postgis.Point getEndPoint() {
		return endPoint;
	}

	/**
	 * 
	 * @param endPoint dernier point du segment
	 */
	public void setEndPoint(org.postgis.Point endPoint) {
		this.endPoint = endPoint;
	}


	@Override
	public String getInfo() {
		
		String info = "";
		info += "***SEGMENT***" + "\n";
		info += "LENGTH: " + this.length + "\n";
 		return info;
	}

}
