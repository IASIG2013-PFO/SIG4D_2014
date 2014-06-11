package iasig.dao;

import org.postgis.PGgeometry;

import iasig.dao.DAO;
import iasig.dao.user.Arbre;
import iasig.dao.user.Conteneur_objet;
import iasig.dao.user.Lampadaire;
import iasig.dao.user.Maison;
import iasig.dao.user.ObjetPonctuel;
import iasig.dao.user.Objet_Postgre;
import iasig.dao.user.Raster_img_mnt;
import iasig.mobile.view.Batiment;
import iasig.mobile.view.Tuile;
import iasig.mobile.view.World;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;



public class GenericDAO extends DAO {

 
	
	//constructeur vide
	public GenericDAO(){
		
	}
	
	/**
	 * Methode Statique pour l'initialisation du Buffer
	 */
	public static void selection_geographique_init(Conteneur_objet obj, Float Xobs, Float Yobs, int interval_de_maille){
		
		//recuperation de la maille observateur
		//TODO ajouter False!!
//		int maille_observateur_i = (int)(Xobs/interval_de_maille);
//		int maille_observateur_j = (int)(Yobs/interval_de_maille);
	
		
		int maille_observateur_i = (int)((Xobs-Tuile.Xmin)/Tuile.DX);
		int maille_observateur_j = (int)((Yobs-Tuile.Ymin)/Tuile.DY);
		
		//inscription de la maille observateur dans L'objet en memoire
		obj.set_Maille_Observateur(maille_observateur_i, maille_observateur_j);
		
		//ecriture du Polygone de requête selon paramètre de generation
		//1-recuperation des mailles extremes de l'espace à mettre en memoire
		int mailleMax_i = maille_observateur_i + obj.demi_espace_memoire_maille() ;
		int mailleMin_i = maille_observateur_i - obj.demi_espace_memoire_maille() ;
		int mailleMax_j = maille_observateur_j + obj.demi_espace_memoire_maille() ;
		int mailleMin_j = maille_observateur_j - obj.demi_espace_memoire_maille() ;
		//2-passage en coordonnees geographiques
		int Xmin =  ( Tuile.Xmin + mailleMin_i * Tuile.DX );
		int Ymin =  ( Tuile.Ymin + mailleMin_j * Tuile.DY );
		int Xmax =  ( Tuile.Xmin + (mailleMax_i + 1) * Tuile.DX ); 
		int Ymax =  ( Tuile.Ymin + (mailleMax_j + 1) * Tuile.DY ); 
		
		
		
		
		String Polygone = "SRID=" + "2154" + ";" + "POLYGON(("+Xmin+" "+ Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+ Xmin+" "+ Ymin+"))";
		PGgeometry polygone;
		try {
			polygone = new PGgeometry(Polygone);			
	        ResultSet result = connection_statique
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
	     // " SELECT *, ST_CENTROID(geom) FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
	      //Multifoncteur - Selectionne l'integralite des champs de la table de objets generiques inclus dans le polygone passe en argument
	      " SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"

	                               );System.out.println(polygone.getValue());
	        
	        //Mise a disposition des conteneurs
        	Maison home1 = new Maison();
        	Lampadaire lamp = new Lampadaire();
        	Arbre arbre = new Arbre();
        	ObjetPonctuel objet_ponctuel = new ObjetPonctuel();
        	
     while(result.next()){
 		if(result.absolute(result.getRow()))

 			switch (result.getString("type")) {
 				case "maison":
 					//Instanciation d'une maison					
 					home1 = new Maison(
 							result.getInt("gid"),
 							result.getString("nom"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),

 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 						 	);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(home1);
 					break;
 				case "lampadaire":
 					//Instanciation d'un lampadaire				
 					lamp = new Lampadaire(
 							result.getInt("gid"),
 							//result.getString("nom"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),
 							result.getString("type"),

 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 						 	);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(lamp);
 					
 				case "arbre":
 					//Instanciation d'un qrbre				
 					arbre = new Arbre(
 							result.getInt("gid"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),
 							result.getString("type"),
 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 						 	);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(arbre);
 					
 					break;
 				default:
 					//throw new IllegalArgumentException("Invalid type: " + result.getString("type"));
 					//instanciation d'un nouvel objet ponctuel
 					objet_ponctuel = new ObjetPonctuel(
 							result.getInt("gid"),
 							result.getString("type"),
 							result.getString("nom"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),
 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 							);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(objet_ponctuel);
 			}
     }

	                                        		 
	                                        		 
	                                        		 
	                                        		 
	                                        		 
	                       		 
	                                        		 
	                                        		 
	       
	        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }

		
	}
	/**
	 * Methode Statique pour la MAJ globale du Buffer
	 * le Buffer ne recharge pas les objets swappes dans le buffer des objets visibles
	 */
	public static void selection_geographique(Conteneur_objet obj, Float Xobs, Float Yobs, int interval_de_maille){
			
		
			//Vidage du buffer
			obj.vide_Objet_en_memoire();
		
			//recuperation de la maille observateur
			//TODO ajouter False!!
//			int maille_observateur_i = (int)(Xobs/interval_de_maille);
//			int maille_observateur_j = (int)(Yobs/interval_de_maille);
			int maille_observateur_i = (int)((Xobs-Tuile.Xmin)/Tuile.DX);
			int maille_observateur_j = (int)((Yobs-Tuile.Ymin)/Tuile.DY);
			
			//inscription de la maille observateur dans L'objet en memoire
			obj.set_Maille_Observateur(maille_observateur_i, maille_observateur_j);
			
			//ecriture du Polygone de requête selon paramètre de generation
			//1-recuperation des mailles extremes de l'espace à mettre en memoire
			int mailleMax_i = maille_observateur_i + obj.demi_espace_memoire_maille();
			int mailleMin_i = maille_observateur_i - obj.demi_espace_memoire_maille();
			int mailleMax_j = maille_observateur_j + obj.demi_espace_memoire_maille();
			int mailleMin_j = maille_observateur_j - obj.demi_espace_memoire_maille();
			
			//Decommenter si tu veux le swap
//			//2-Calcul de l'espace de swap ( zone du buffer correspondant à l'espace de "SWAP" du visible)
//			int mailleSwapMax_i = maille_observateur_i + (int)obj.dimension_espace_visible/2;
//			int mailleSwapMin_i = maille_observateur_i - (int)obj.dimension_espace_visible/2;
//			int mailleSwapMax_j = maille_observateur_j + (int)obj.dimension_espace_visible/2;
//			int mailleSwapMin_j = maille_observateur_j - (int)obj.dimension_espace_visible/2;	
			
			//3-passage en coordonnees geographiques - Extension Buffer
			int Xmin =  ( Tuile.Xmin + mailleMin_i * Tuile.DX );
			int Ymin =  ( Tuile.Ymin + mailleMin_j * Tuile.DY );
			int Xmax =  ( Tuile.Xmin + (mailleMax_i + 1) * Tuile.DX ); 
			int Ymax =  ( Tuile.Ymin + (mailleMax_j + 1) * Tuile.DY ); 

			//Decommenter si tu veux le swap
//			//4-passage en coordonnees geographiques - Extension Swap
//			int Xswapmin = mailleSwapMin_i * interval_de_maille;
//			int Yswapmin = mailleSwapMin_j * interval_de_maille;
//			int Xswapmax = (mailleSwapMax_i +1) * interval_de_maille;
//			int Yswapmax = (mailleSwapMax_j +1) * interval_de_maille;

			//Decommenter si tu veux le swap
//			String Polygone = "SRID=" + "4326" + ";" + "POLYGON(("+Xmin+" "+ Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+ Xmin+" "+ Ymin+")"
//					+ ",("+Xswapmin+" "+ Yswapmin+ ","+Xswapmax+" "+Yswapmin+","+ Xswapmax+" "+ Yswapmax+","+ Xswapmin+" "+ Yswapmax+","+ Xswapmin+" "+ Yswapmin +"))";
			
			//commenter si tu veux le swap
			String Polygone = "SRID=" + "2154" + ";" + "POLYGON(("+Xmin+" "+ Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+ Xmin+" "+ Ymin+"))";

			PGgeometry polygone;
			try {
				polygone = new PGgeometry(Polygone);			
		        ResultSet result = connection_statique
		                                .createStatement(
		                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
		                                            ResultSet.TYPE_SCROLL_INSENSITIVE
		                                         ).executeQuery(
		                                        		
		     //" SELECT *, ST_Centroid(geom) FROM maison2 WHERE ST_WITHIN(ST_Centroid(geom), ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
		     //" SELECT *, ST_Centroid(centroid) FROM maison2 WHERE ST_WITHIN(ST_Centroid(centroid), ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
		     //Multifoncteur - Selectionne l'integralite des champs de la table de objets generiques inclus dans le polygone passe en argument
		      " SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"

		                               );
		        
		        //Mise a disposition des conteneurs
	        	Maison home1 = new Maison();
	        	Lampadaire lamp = new Lampadaire();
	        	Arbre arbre = new Arbre();
	        	ObjetPonctuel objet_ponctuel = new ObjetPonctuel();
	    	        	
	     while(result.next()){
	 		if(result.absolute(result.getRow()))
	 			
	 			switch (result.getString("type")) {					
	 				case "maison":
	 					//Instanciation d'une maison					
	 					home1 = new Maison(
	 							result.getInt("gid"),
	 							result.getString("nom"),
	 							result.getFloat("rotz"),
	 							result.getFloat("f"),

	 							(PGgeometry)result.getObject("geom"),
	 							(PGgeometry)result.getObject("st_centroid")
	 						 	);
	 					//Ajout de l'objet dans le vecteur de vecteur d'objet
	 					obj.AjoutObjet(home1);
	 					break;
	 				case "lampadaire":
	 					//Instanciation d'un lampadaire				
	 					lamp = new Lampadaire(
	 							result.getInt("gid"),
	 							//result.getString("nom"),
	 							result.getFloat("rotz"),
	 							result.getFloat("f"),
	 							result.getString("type"),

	 							(PGgeometry)result.getObject("geom"),
	 							(PGgeometry)result.getObject("st_centroid")
	 						 	);
	 					//Ajout de l'objet dans le vecteur de vecteur d'objet
	 					obj.AjoutObjet(lamp);
	 					
	 				case "arbre":
	 					//Instanciation d'un qrbre				
	 					arbre = new Arbre(
	 							result.getInt("gid"),
	 							result.getFloat("rotz"),
	 							result.getFloat("f"),
	 							result.getString("type"),
	 							(PGgeometry)result.getObject("geom"),
	 							(PGgeometry)result.getObject("st_centroid")
	 						 	);
	 					//Ajout de l'objet dans le vecteur de vecteur d'objet
	 					obj.AjoutObjet(arbre);
	 					
	 					break;
	 				default:
	 					//throw new IllegalArgumentException("Invalid type: " + result.getString("type"));
	 					//instanciation d'un nouvel objet ponctuel
	 					objet_ponctuel = new ObjetPonctuel(
	 							result.getInt("gid"),
	 							result.getString("type"),
	 							result.getString("nom"),
	 							result.getFloat("rotz"),
	 							result.getFloat("f"),
	 							(PGgeometry)result.getObject("geom"),
	 							(PGgeometry)result.getObject("st_centroid")
	 							);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
	 					obj.AjoutObjet(objet_ponctuel);
	 			}
	     }
		        	
			    } catch (SQLException e) {
			            e.printStackTrace();
			    }
		}
	
	
	public static void selection_raster(Objet_Postgre<Raster_img_mnt> obj, int i, int j){
		
	    byte[] content = null;
//	    Dataset dattaset = null;
	    //type de raster enregistr� (img ou rast
	    String type_raster="img";
	    //conversion des valeurs de i et j vers l'�quivalent en indice
	    int id=(i-1)*99+j;
	    
		try {
			
			//vidage prealable du vecteur
	
	        ResultSet result = connection_statique
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
//	      " SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
//	      " SELECT ID, ST_AsPNG(rast) as image, I, J, type_Raster_img_mnt, Resolution FROM route WHERE ST_WITHIN(rast, ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"// WHERE gid>="+a+" AND gid<="+b+";"//
	      " SELECT rid, ST_AsPNG(rast) AS img, ST_DumpAsPolygons(rast) AS raster FROM mnt_img WHERE rid='"+ id +"';"
	                                        		 );
	        Raster_img_mnt raster_img_mnt;

			while(result.next()){
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
						raster_img_mnt = new Raster_img_mnt(
		        					result.getInt("rid"),
		        					image,i,j,"img",1
//		        					result.getInt("I"),
//		        					result.getInt("J"),
//		        					result.getString("type_Raster_img_mnt"),
//		        					result.getInt("Resolution")
		        		);
					}
					else{
						raster_img_mnt = new Raster_img_mnt(
	        					result.getInt("rid"),
	        					(PGgeometry)result.getObject("raster"),
	        					i,j,"mnt",1
//	        					result.getInt("I"),
//	        					result.getInt("J"),
//	        					result.getString("type_Raster_img_mnt"),
//	        					result.getInt("Resolution")
						);
					}
	        		//retourne adresses objets	
//					System.out.print("Maison_id "+home1.get_ID()+" ");System.out.println("@ "+home1.toString());
					obj.AjoutObjet(raster_img_mnt);
					System.out.println("id_ortho "+obj.getElement(0).get_ID()+" ");
	        	}
	        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }
		
	}

	
public static void selection_geographique_buffer(iasig.mobile.view.Buffer obj){
		
		//recuperation de la maille observateur
	
		int maille_observateur_i = obj.centre_buffer_auxiliaire_i;
		int maille_observateur_j = obj.centre_buffer_auxiliaire_j;
		
		System.out.println(maille_observateur_i+" "+maille_observateur_j);

		int demi_taille_buffer= obj.taille_buffer_memoire/2;
		//ecriture du Polygone de requête selon paramètre de generation
		//1-recuperation des mailles extremes de l'espace à mettre en memoire
		int mailleMax_i = maille_observateur_i +  demi_taille_buffer - 1 ;
		int mailleMin_i = maille_observateur_i -  demi_taille_buffer + 1 ;
		int mailleMax_j = maille_observateur_j +  demi_taille_buffer - 1 ;
		int mailleMin_j = maille_observateur_j -  demi_taille_buffer + 1 ;
		
		int Xmin =  ( Tuile.Xmin + mailleMin_i * Tuile.DX );
		int Ymin =  ( Tuile.Ymin + mailleMin_j * Tuile.DY );
		int Xmax =  ( Tuile.Xmin + (mailleMax_i + 1) * Tuile.DX ); 
		int Ymax =  ( Tuile.Ymin + (mailleMax_j + 1) * Tuile.DY ); 
		
		
		
		
		String Polygone = "SRID=" + "2154" + ";" + "POLYGON(("+Xmin+" "+ Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+ Xmin+" "+ Ymin+"))";
		PGgeometry polygone;
		try {
			polygone = new PGgeometry(Polygone);			
	        ResultSet result = connection_statique
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
	     // " SELECT *, ST_CENTROID(geom) FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
	      //Multifoncteur - Selectionne l'integralite des champs de la table de objets generiques inclus dans le polygone passe en argument
	      " SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"

	                               );
	        //System.out.println(polygone.getValue());
	        
	        //Mise a disposition des conteneurs
        	Maison home1 = new Maison();
        	Lampadaire lamp = new Lampadaire();
        	Arbre arbre = new Arbre();
        	ObjetPonctuel objet_ponctuel = new ObjetPonctuel();
        	
       	 System.out.println("parcours du resultSet");

     while(result.next()){
 		if(result.absolute(result.getRow()))

 			switch (result.getString("type")) {
 				case "maison":
 					//Instanciation d'une maison					
 					home1 = new Maison(
 							result.getInt("gid"),
 							result.getString("nom"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),

 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 						 	);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(home1);
 					break;
 				case "lampadaire":
 					//Instanciation d'un lampadaire				
 					lamp = new Lampadaire(
 							result.getInt("gid"),
 							//result.getString("nom"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),
 							result.getString("type"),

 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 						 	);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(lamp);
 					
 				case "arbre":
 					//Instanciation d'un qrbre				
 					arbre = new Arbre(
 							result.getInt("gid"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),
 							result.getString("type"),
 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 						 	);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(arbre);
 					
 					break;
 				default:
 					//throw new IllegalArgumentException("Invalid type: " + result.getString("type"));
 					//instanciation d'un nouvel objet ponctuel
 					objet_ponctuel = new ObjetPonctuel(
 							result.getInt("gid"),
 							result.getString("type"),
 							result.getString("nom"),
 							result.getFloat("rotz"),
 							result.getFloat("f"),
 							(PGgeometry)result.getObject("geom"),
 							(PGgeometry)result.getObject("st_centroid")
 							);
 					//Ajout de l'objet dans le vecteur de vecteur d'objet
 					obj.AjoutObjet(objet_ponctuel);
 			}
     }
        	
		    } catch (SQLException e) {
		            e.printStackTrace();
		    }

		
	}


	public static void selection_geographique_bati_buffer(iasig.mobile.view.Buffer obj){
		
		//recuperation de la maille observateur
	
		int maille_observateur_i = obj.centre_buffer_auxiliaire_i;
		int maille_observateur_j = obj.centre_buffer_auxiliaire_j;
		
		System.out.println(maille_observateur_i+" "+maille_observateur_j);
	
		int demi_taille_buffer= obj.taille_buffer_memoire/2;
		//ecriture du Polygone de requête selon paramètre de generation
		//1-recuperation des mailles extremes de l'espace à mettre en memoire
		int mailleMax_i = maille_observateur_i +  demi_taille_buffer - 1 ;
		int mailleMin_i = maille_observateur_i -  demi_taille_buffer + 1 ;
		int mailleMax_j = maille_observateur_j +  demi_taille_buffer - 1 ;
		int mailleMin_j = maille_observateur_j -  demi_taille_buffer + 1 ;
		
		int Xmin =  ( Tuile.Xmin + mailleMin_i * Tuile.DX );
		int Ymin =  ( Tuile.Ymin + mailleMin_j * Tuile.DY );
		int Xmax =  ( Tuile.Xmin + (mailleMax_i + 1) * Tuile.DX ); 
		int Ymax =  ( Tuile.Ymin + (mailleMax_j + 1) * Tuile.DY ); 
		

		String Polygone = "SRID=" + "2154" + ";" + "POLYGON(("+Xmin+" "+ Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+ Xmin+" "+ Ymin+"))";
		PGgeometry polygone;
		try {
			polygone = new PGgeometry(Polygone);			
	        ResultSet result = connection_statique
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	                                        		
	     //" SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;" 
	     // " SELECT *, ST_CENTROID(geom) FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
	      //Multifoncteur - Selectionne l'integralite des champs de la table de objets generiques inclus dans le polygone passe en argument
	      " SELECT hauteur,geom,ST_CENTROID(geom) FROM source.bati_indiferencie WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"
	                               );
	        //System.out.println(polygone.getValue());
	        
	     //Mise a disposition du conteneur bati_indiferencie
	    Batiment bati = new Batiment();
	    	
	    	
	   	 System.out.println("Bati: parcours du resultSet");
	
	   	 while(result.next()){
			if(result.absolute(result.getRow()))
	
				
						//Instanciation d'un bati					
						bati = new Batiment(
								(PGgeometry)result.getObject("geom"),
	 							(PGgeometry)result.getObject("st_centroid"),
								result.getInt("hauteur")
							 	);
						//Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutBati(bati);
						
	   	 }
	 
	    	
		} catch (SQLException e) {
		            e.printStackTrace();
		}
	}
	
}
	

	
	
	













