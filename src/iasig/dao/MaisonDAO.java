package iasig.dao;

import iasig.dao.user.Conteneur_objet;
import iasig.dao.user.Lampadaire;
import iasig.dao.user.Maison;
import iasig.dao.user.Objet_Postgre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;





import org.postgis.PGgeometry;

/**
 * Classe permettant un acces specifique aux objets maisons contenus dans la BDD
 * OPERATIONS de type: Maintenance/creation/mse a jour
 */
public class MaisonDAO extends ObjectDao<Maison> {

	
	
	@Override
	public Maison find(long id) {
		Maison home = new Maison();
		try {

	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.CONCUR_UPDATABLE
	                                         ).executeQuery(
	                                            "SELECT * FROM maison2 WHERE maison_id = " + id
	                                         );
	        if(result.first())
	        		home = new Maison(
	        					result.getInt("maison_id"),
	        					result.getString("maison_X"),
	        					result.getString("maison_Y"),
	        					result.getString("maison_Z"),
	        					result.getString("maison_nom"),
	        					(PGgeometry)result.getObject("centroid"),
	        					result.getInt("niveau")
	        				 	);
	        
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }
		   return home;

	}
	
	@Override
	public Maison create(Maison obj) {
		try {
			
				PreparedStatement prepare = this	.connect
	                                                .prepareStatement(
	                                    	"INSERT INTO maison2 (maison_nom, maison_x, maison_y, maison_z, centroid, niveau, i, j) VALUES(?, ?, ?, ?, ?, ?, ?, ?);"
	                                                );
				prepare.setString(1, obj.getNom());
//				System.out.println(obj.getNom());
				
				prepare.setString(2, obj.getX());
				prepare.setString(3, obj.getY());
				prepare.setString(4, obj.getZ());
//				System.out.print(obj.getX());System.out.print(" "+obj.getY());System.out.println(" "+obj.getZ());
				
				prepare.setObject(5, obj.getCentroid());
//				System.out.println(obj.getCentroid());
				prepare.setObject(6, obj.getNiveau());
				prepare.setObject(7, obj.getMaille_i());
				prepare.setObject(8, obj.getMaille_j());
//				System.out.println(prepare.toString());

				prepare.executeUpdate();
				//this.connect.commit();
				prepare.close();

			
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	    return obj;
	}
	
	@Override
	public Maison update(Maison obj) {
		try {

	            this .connect	
	                 .createStatement(
	                	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                    ResultSet.CONCUR_UPDATABLE
	                 ).executeUpdate(
	                	"UPDATE maison SET maison_nom = '" + obj.getNom() + "'"+
	                	" WHERE maison_id = " + obj.getId()
	                 );
	            
			
			obj = this.find(obj.getId());
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	    
	    return obj;
	}

	@Override
	public void delete(Maison obj) {
		try {
			
	            this    .connect
	                	.createStatement(
	                         ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                         ResultSet.CONCUR_UPDATABLE
	                    ).executeUpdate(
	                         "DELETE FROM maison WHERE maison_id = " + obj.getId()
	                    );
			
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	}
	
	@Override
	public void selection_geographique(Conteneur_objet obj, Float Xobs, Float Yobs, int interval_de_maille){
		
		//recuperation de la maille observateur
		//TODO ajouter False!!
		int maille_observateur_i = (int)(Xobs/interval_de_maille);
		int maille_observateur_j = (int)(Yobs/interval_de_maille);
		//inscription de la maille observateur dans L'objet en memoire
		obj.set_Maille_Observateur(maille_observateur_i, maille_observateur_j);
		//ecriture du Polygone de requête selon paramètre de generation
		//1-recuperation des mailles extremes de l'espace à mettre en memoire
		int mailleMax_i = maille_observateur_i + obj.demi_espace_memoire_maille();
		int mailleMin_i = maille_observateur_i - obj.demi_espace_memoire_maille();
		int mailleMax_j = maille_observateur_j + obj.demi_espace_memoire_maille();
		int mailleMin_j = maille_observateur_j - obj.demi_espace_memoire_maille();
		//2-passage en coordonnees geographiques
		int Xmin = mailleMin_i * interval_de_maille;
		int Ymin = mailleMin_j * interval_de_maille;
		int Xmax = (mailleMax_i + 1) * interval_de_maille; 
		int Ymax = (mailleMax_j + 1) * interval_de_maille; 
		
//		float Xmin = Xobs - ( interval_de_maille*obj.get_paramètre_maille() )/2;
//		float Ymin =Yobs - ( interval_de_maille*obj.get_paramètre_maille() )/2;
//		float Xmax =Xobs + ( interval_de_maille*obj.get_paramètre_maille() )/2;
//		float Ymax =Yobs + ( interval_de_maille*obj.get_paramètre_maille() )/2;
		
		String Polygone = "SRID=" + "4326" + ";" + "POLYGON(("+Xmin+" "+ Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+ Xmin+" "+ Ymin+"))";
		PGgeometry polygone;
		try {
			polygone = new PGgeometry(Polygone);			
	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
	      " SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
	                                        		 );
			Maison home1 = new Maison();

			while(result.next()){
				if(result.absolute(result.getRow()))
					
	        		home1 = new Maison(
	        					result.getInt("maison_id"),
	        					result.getString("maison_x"),
	        					result.getString("maison_y"),
	        					result.getString("maison_z"),
	        					result.getString("maison_nom"),
	        					(PGgeometry)result.getObject("centroid"),
	        					result.getInt("niveau"),
	        					result.getInt("i"),
	        					result.getInt("j")
	        				 	);
	        		//retourne adresses objets	
					//System.out.print("Maison_id "+home1.getId()+" ");System.out.println("@ "+home1.toString());
					//on passe en paramètre la maille contenant l'observateur
			

					obj.AjoutObjet(home1);
	        					}
	        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }

		
	}

	@Override
	public void selection_geographique_par_polygone(
			Objet_Postgre<Lampadaire> obj, PGgeometry polygone) {
		// TODO Auto-generated method stub
		
	}
	
}












