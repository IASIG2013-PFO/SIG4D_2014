/**
 * 
 */
package dao;


import dao.user.Buffer;
import dao.user.Lampadaire;
import dao.user.Objet_Postgre;
import dao.user.Raster_img_mnt;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import org.postgis.PGgeometry;

/**
 * @author Francois
 *
 */
public class Raster_img_mntDAO extends ObjectDao<Raster_img_mnt> {


	@Override
	public Raster_img_mnt create(Raster_img_mnt obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Raster_img_mnt update(Raster_img_mnt obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Raster_img_mnt obj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Raster_img_mnt find(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void selection_geographique(Objet_Postgre<Raster_img_mnt> obj, int i, int j){
		
	    byte[] content = null;
//	    Dataset dattaset = null;
	    //type de raster enregistr� (img ou raster)
	    String type_raster="img";
	    //conversion des valeurs de i et j vers l'�quivalent en indice
	    int id=(85-i)*99+j;
	    try {
			
			//vidage préalable du vecteur
	
	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
//	      " SELECT ID, ST_AsPNG(rast) as image, I, J, type_Raster_img_mnt, Resolution FROM route WHERE ST_WITHIN(rast, ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"// WHERE gid>="+a+" AND gid<="+b+";"//
	      " SELECT rid, ST_AsPNG(rast) as img FROM ortho_5 WHERE rid='"+ id +"'LIMIT 1;"
	                                        		 );
//	        System.out.println(result.toString());

	        Raster_img_mnt home1;

	        	result.next();
				if(result.absolute(result.getRow()))
				//System.out.println(result.getInt("maison_id"));

					
					content = result.getBytes("img");
                	ByteArrayInputStream bis = new ByteArrayInputStream(content);
                	BufferedImage image=null;
					try {
						image = ImageIO.read(bis);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(type_raster=="img"){
						long tr1 = System.currentTimeMillis();
		        		home1 = new Raster_img_mnt(
		        					result.getInt("rid"),
		        					image,i,j,"img",1
//		        					result.getInt("I"),
//		        					result.getInt("J"),
//		        					result.getString("type_Raster_img_mnt"),
//		        					result.getInt("Resolution")
		        		);
		        		
		        	}
					else{    
						long tr2 = System.currentTimeMillis();
		        		home1 = new Raster_img_mnt(
	        					result.getInt("rid"),
	        					(PGgeometry)result.getObject("Raster"),
	        					i,j,"mnt",1
//	        					result.getInt("I"),
//	        					result.getInt("J"),
//	        					result.getString("type_Raster_img_mnt"),
//	        					result.getInt("Resolution")
						);
		        	}
	        		//retourne adresses objets	
//					System.out.print("Maison_id "+home1.get_ID()+" ");System.out.println("@ "+home1.toString());
					obj.AjoutObjet(home1);
//	        	}
			
	        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }
		
	}


	@Override
	public void selection_geographique(Buffer obj, Float Xobs, Float Yobs,
			int interval_de_maille) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selection_geographique_par_polygone(
			Objet_Postgre<Lampadaire> obj, PGgeometry polygone) {
		// TODO Auto-generated method stub
		
	}

}

