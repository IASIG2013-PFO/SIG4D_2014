/**
 * 
 */
package dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import iasig.mobile.view.Tuile;

/**
 * @author Jean-François
 */
public class OrthoDAO extends DAO {
	
	public OrthoDAO(){
		//Appel au constructeur de la classe mère
		super();
	}
	
	public BufferedImage load_ortho(int i, int j, int r){
		
	    byte[] content = null;
	    BufferedImage image=null;
	    
	    //conversion des valeurs de i et j vers l'équivalent en indice
	    int id=i+j*99+1;
	    
	    try {
	
	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	        
	       "SELECT ST_AsPNG(rast) as img FROM ortho_"+Tuile.resolution[r-1]+" WHERE rid='"+id+"'LIMIT 1;");
	       
	        result.next();
				
			content = result.getBytes("img");
		  
        	ByteArrayInputStream bis = new ByteArrayInputStream(content);
        	
			try {
				image = ImageIO.read(bis);
			}
			catch (IOException e) {
				e.printStackTrace();
			}	
	    }
    	catch (SQLException e) {
	            e.printStackTrace();
	    }
	    
	    return image;
	}
}
