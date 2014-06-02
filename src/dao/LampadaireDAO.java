package dao;


import dao.user.Lampadaire;
import dao.user.Objet_Postgre;
import dao.user.Buffer;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgis.PGgeometry;

public class LampadaireDAO extends ObjectDao<Lampadaire> {

	
	@Override
	public Lampadaire find(long id) {
		Lampadaire lamp = new Lampadaire();
		try {

	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.CONCUR_UPDATABLE
	                                         ).executeQuery(
	                                            "SELECT * FROM lampadaire WHERE id = " + id
	                                         );
	        if(result.first())
	        		lamp = new Lampadaire(
	        					result.getInt("id"),
	        					result.getInt("gid"),

	        					result.getFloat("xcoord"),
	        					result.getFloat("ycoord"),
	        					
	        					(PGgeometry)result.getObject("geom")
	        				 	);
	        
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }
		   return lamp;

	}
	
	@Override
	public Lampadaire create(Lampadaire obj) {
		try {
			
				PreparedStatement prepare = this	.connect
	                                                .prepareStatement(
	                                    	"INSERT INTO lampadaire (id, gid, xcoord, ycoord, geom) VALUES(?, ?, ?, ?, ?);"
	                                                );
				prepare.setInt(1, obj.getId());
//				System.out.println(obj.getNom());
				prepare.setInt(2, obj.getGid());
				prepare.setFloat(3, obj.getX());
				prepare.setFloat(4, obj.getY());
				
				prepare.setObject(5, obj.getCentroid());

				prepare.executeUpdate();
				this.connect.commit();
				prepare.close();

			
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	    return obj;
	}
	
	@Override
	public Lampadaire update(Lampadaire obj) {
		try {

	            this .connect	
	                 .createStatement(
	                	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                    ResultSet.CONCUR_UPDATABLE
	                 ).executeUpdate(
	                	"UPDATE lampadaire SET gid = '" + obj.getGid() + "'"+
	                	" WHERE id = " + obj.getId()
	                 );
	            
			
			obj = this.find(obj.getId());
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	    
	    return obj;
	}

	@Override
	public void delete(Lampadaire obj) {
		try {
			
	            this    .connect
	                	.createStatement(
	                         ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                         ResultSet.CONCUR_UPDATABLE
	                    ).executeUpdate(
	                         "DELETE FROM lampadaire WHERE maison_id = " + obj.getId()
	                    );
			
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	}
	
	@Override
	public void selection_geographique_par_polygone(Objet_Postgre<Lampadaire> obj, PGgeometry polygone){
		
		try {
			
			//vidage préalable du vecteur
			obj.VideObjets();
			
	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
	      " SELECT * FROM lampadaire WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
	                                        		 );
	        System.out.println(result.toString());
			Lampadaire lamp = new Lampadaire();


			while(result.next()){
				if(result.absolute(result.getRow()))
				//System.out.println(result.getInt("maison_id"));
					
					lamp = new Lampadaire(
        					result.getInt("id"),
        					result.getInt("gid"),

        					result.getFloat("xcoord"),
        					result.getFloat("ycoord"),
        					
        					(PGgeometry)result.getObject("geom")
        				 	);
				
	        		//retourne adresses objets	
					//System.out.print("Maison_id "+home1.getId()+" ");System.out.println("@ "+home1.toString());
					obj.AjoutObjet(lamp);
	        					}
	        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }

		
	}

	@Override
	public void selection_geographique(Buffer obj, Float Xobs, Float Yobs, int interval_de_maille) {
		// TODO Auto-generated method stub
		
	}



	
}












