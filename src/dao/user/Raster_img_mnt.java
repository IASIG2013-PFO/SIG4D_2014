/**
 * 
 */
package dao.user;

import dao.*;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import org.postgis.PGgeometry;

/**
 * @author Francois
 *
 */
public class Raster_img_mnt {

	private int ID;
	private BufferedImage image;
	private PGgeometry rast;
	private int I,J;
	private String type_Raster;
	private int Resolution;
	/**
	 * 
	 */
	
	public Raster_img_mnt(){}
	
	public Raster_img_mnt(int ID, BufferedImage image, int I, int J, String type_Raster, int Resolution) {
		this.ID=ID;
		this.image=image;
		this.I=I;
		this.J=J;
		this.type_Raster=type_Raster;
		this.Resolution=Resolution;
	}
	
	public Raster_img_mnt(int ID, PGgeometry Rast, int I, int J, String type_Raster, int Resolution) {
		this.ID=ID;
		this.rast=rast;
		this.I=I;
		this.J=J;
		this.type_Raster=type_Raster;
		this.Resolution=Resolution;
	}
	
	public int get_ID(){
		return ID;
	}
	
	public BufferedImage get_image(){
		return image;
	}
	
	public String get_type(){
		return type_Raster;
	}
	
	public int get_Resolution(){
		return Resolution;
	}
	
	public int get_I(){
		return I;
	}
	
	public int get_J(){
		return J;
	}

}
