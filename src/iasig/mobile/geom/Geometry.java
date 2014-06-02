package iasig.mobile.geom;
/**
 * 
 * @author iasig.mobile
 * Classe abstraite représentant les géométries.
 *
 */
public abstract class Geometry {
	//Variables de la classe
	protected int id;
	protected Extent extent;
	@SuppressWarnings("unused")
	private String info;
	
	/**
	 * 
	 * @return identifiant de la géométrie
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * 
	 * @param id identifiant de la géométrie
	 */
	public void setId(int id){
		this.id = id;
	}
	
	/**
	 * 
	 * @return étendue de la géométrie
	 */
	public Extent getExtent(){
		return this.extent;
	}
	
	/**
	 * 
	 * @param extent étendue de la géométrie
	 */
	public void setExtent(Extent extent){
		this.extent = extent;
	}
	
	/**
	 * 
	 * @return informations sur la géométrie
	 */
	public abstract String getInfo();
	
	/**
	 * 
	 * @param info information sur la géométrie
	 */
	public void setInfo(String info) {
		this.info = info;
	}
}
