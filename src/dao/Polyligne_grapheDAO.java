/**
 * 
 */
package dao;

import dao.user.Buffer;
import dao.user.Graphe;
import dao.user.Lampadaire;
import dao.user.Objet_Postgre;
import dao.user.Polyligne_graphe;


import java.sql.ResultSet;
import java.sql.SQLException;



import org.postgis.PGgeometry;


/**
 * @author Francois
 *
 */
public class Polyligne_grapheDAO extends ObjectDao<Polyligne_graphe> {


	public void selection_geographique(Objet_Postgre<Polyligne_graphe> obj, PGgeometry polygone){
		
		try {
			
			//vidage pr�alable du vecteur
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
