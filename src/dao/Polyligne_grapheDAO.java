/**
 * 
 */
package dao;

import dao.DAO;
import dao.user.Buffer;
import dao.user.Graphe;
import dao.user.Lampadaire;
import dao.user.Maison;
import dao.user.Objet_Maison;
import dao.user.Objet_Postgre;
import dao.user.Polyligne_graphe;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.postgis.PGgeometry;

import iasig.mobile.view.MNT;

/**
 * @author Francois
 *
 */
public class Polyligne_grapheDAO extends ObjectDao<Polyligne_graphe> {


	public void selection_geographique(Objet_Postgre<Polyligne_graphe> obj, PGgeometry polygone){
		
		try {
			
			//vidage préalable du vecteur
			obj.VideObjets();
	
	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
	      " SELECT * FROM tunnel;" //WHERE ST_WITHIN(geom, ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"
	                                        		 );
	        Polyligne_graphe home1 = new Polyligne_graphe();

			while(result.next()){
				if(result.absolute(result.getRow()))
				//System.out.println(result.getInt("maison_id"));
					
	        		home1 = new Polyligne_graphe(
	        					result.getInt("gid"),
	        					(PGgeometry)result.getObject("geom"),
	        					result.getString("nature"),
	        					result.getString("sens"),
	        					"tunnel"
	        				 	);
	        		//retourne adresses objets	
					obj.AjoutObjet(home1);
	        		}
	        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }
		
	}

	@Override
	public Polyligne_graphe find(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polyligne_graphe create(Polyligne_graphe obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polyligne_graphe update(Polyligne_graphe obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Polyligne_graphe obj) {
		// TODO Auto-generated method stub
		
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
	
	public static PGgeometry getTunnelFromBDD() throws SQLException{
		Graphe obj = new Graphe();
		String Polygone = "SRID=" + "2154" + ";" + "POLYGON((946921 6538365,953538 6537334, 952459 6533450, 949654 6530406, 946921 6538365))";
		PGgeometry polygone = new PGgeometry(Polygone);
		Polyligne_grapheDAO Polyligne_grapheDAO=new Polyligne_grapheDAO();
		Polyligne_grapheDAO.selection_geographique(obj,polygone);
		
		
		return obj.getElement(0).get_coordonnees();
	}
	
	public static void main(String[] args) throws SQLException{
	
	}
	
}
