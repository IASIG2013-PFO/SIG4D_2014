package iasig.mobile.geom;
/**
 * 
 * @author iasig.mobile
 * Classe représentant l'étendue d'une géométrie
 */
public class Extent extends Geometry {

	//Attributs d'objets
	private int id;
	private double xmin, xmax, ymin, ymax;
	
	/**
	 * Constructeur par défaut. Initialise l'étendue à 0
	 */
	public Extent(){
		this.id = 0;
		this.xmin = 0;
		this.xmax = 0;
		this.ymin = 0;
		this.ymax = 0;
	}
	/**
	 * 
	 * @param xmin xmin de l'étendue
	 * @param xmax xmax de l'étendue
	 * @param ymin ymin de l'étendue
	 * @param ymax ymax de l'étendue
	 */
	public Extent(double xmin, double xmax, double ymin, double ymax){
		this();
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
	public Extent(org.postgis.Point point){
		this.xmin = point.x;
		this.xmax = point.x;
		this.ymin = point.y;
		this.ymax = point.y;
	}
	
	/**
	 * Constructeur de création d'une étendue à partir d'une autre étendue
	 */
	public Extent(Extent extent){
		this();
		setExtent(extent);
	}
	
	/**
	 * 
	 * @return xmin de l'étendue
	 */
	public double getXmin() {
		return xmin;
	}
	
	/**
	 * 
	 * @param xmin xmin de l'étendue
	 */
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	
	/**
	 * 
	 * @return xmax de l'étendue
	 */
	public double getXmax() {
		return xmax;
	}

	/**
	 * 
	 * @param xmax xmax de l'étendue
	 */
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	
	/**
	 * 
	 * @return ymin de l'étendue
	 */
	public double getYmin() {
		return ymin;
	}
	
	/**
	 * 
	 * @param ymin ymin de l'étendue
	 */
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	
	/**
	 * 
	 * @return ymax de l'étendue
	 */
	public double getYmax() {
		return ymax;
	}

	/**
	 * 
	 * @param ymax ymax de l'étendue
	 */
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
	/**
	 * 
	 * @param geom objet de type géométrie
	 * Mise à jour de l'étendue grâce à la nouvelle géométrie
	 */
	public void updateExtent(Geometry geom){
		Extent ext = geom.getExtent();
		if(ext.getXmin() < this.xmin){
			this.xmin = ext.getXmin();
		}
		if(ext.getXmax() > this.xmax){
			this.xmax = ext.getXmax();
		}
		if(ext.getYmin() < this.ymin){
			this.ymin = ext.getYmin();
		}
		if(ext.getYmax() > this.ymax){
			this.ymax = ext.getYmax();
		}
	}
	
	
	public void updateExtent(org.postgis.Point geom){
		if(geom.x < this.xmin){
			this.xmin = geom.x;
		}
		if(geom.x > this.xmax){
			this.xmax = geom.x;
		}
		if(geom.y < this.ymin){
			this.ymin = geom.y;
		}
		if(geom.y > this.ymax){
			this.ymax = geom.y;
		}
	}
	
	/**
	 * 
	 * @param ext objet étendue
	 * @return indique si l'étendue en entrée intersecte l'étendue
	 */
	public boolean intersect(Extent ext){
		if(this.xmax < ext.getXmin()){
			return false;
		}
		if(this.xmin > ext.getXmax()){
			return false;
		}
		if(this.ymax < ext.getYmin()){
			return false;
		}
		if(this.ymin > ext.getYmax()){
			return false;
		}
		return true;
	}


	@Override
	public Extent getExtent() {
		return this;
	}

	@Override
	public void setExtent(Extent ext) {
		this.xmin = ext.xmin;
		this.xmax = ext.xmax;
		this.ymin = ext.ymin;
		this.ymax = ext.ymax;
		this.id = ext.id; 

	}

	@Override
	public String getInfo() {
		String info = "";
		info += "***ETENDUE***" + "\n";
		info += "ID: " + this.id + "\n";
		info += "XMIN: " + this.xmin + "\n";
		info += "XMAX: " + this.xmax + "\n";
		info += "YMIN: " + this.ymin + "\n";
		info += "YMAX: " + this.ymax + "\n";
 		return info;
	}

}
