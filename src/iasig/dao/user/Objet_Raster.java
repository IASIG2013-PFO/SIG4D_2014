package iasig.dao.user;

import java.util.Vector;

/**
 * Classe "legacy" - Classe permettant une mise en mémoire des objets Raster contenus dans la BDD sous forme 
 * de vecteur de vecteur - Obsolete
 */
public class Objet_Raster extends Objet_Postgre<Raster_img_mnt> {

	private int ID;
	private Vector <Raster_img_mnt> vec_Raster=new Vector <Raster_img_mnt>();
	private String type_raster;
	
	/**
	 * 
	 */
	public int get_ID(){
		return ID;
	}
	
	public Vector <Raster_img_mnt> get_vec_raster(){
		return vec_Raster;
	}
	
	public String get_type_raster(){
		return type_raster;
	}
	
	@Override
	public void AjoutObjet(Raster_img_mnt Raster){
		vec_Raster.addElement(Raster);
	}
	
	@Override
	public void getObjet_par_niveau(int niveau) {

	}
	
	@Override
	public void VideObjets() {
		vec_Raster.clear();
	}

	@Override
	public int NbreObjets() {
		return vec_Raster.size();
		
	}

	@Override
	public Raster_img_mnt getElement(int index) {
		return vec_Raster.elementAt(index);
	}


}
