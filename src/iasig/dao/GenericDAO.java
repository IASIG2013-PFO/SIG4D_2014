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
import iasig.univers.view.Batiment;
import iasig.univers.view.Tuile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

/**
 * Classe Fournit les méthodes statiques dédiée pour lancer des requêtes
 * géographiques sur les tables de la BDD Les attributs sont homogènes avec les
 * champs de la BDD de la table des lampadaires
 */
public class GenericDAO extends DAO {

	// constructeur vide
	public GenericDAO() {

	}

	/**
	 * Methode "legacy" - Methode Statique pour l'initialisation d'un
	 * Conteneur_objet - Obsolete
	 */
	public static void selection_geographique_init(Conteneur_objet obj,
			Float Xobs, Float Yobs, int interval_de_maille) {

		// recuperation de la maille observateur
		// TODO ajouter False!!
		// int maille_observateur_i = (int)(Xobs/interval_de_maille);
		// int maille_observateur_j = (int)(Yobs/interval_de_maille);

		int maille_observateur_i = (int) ((Xobs - Tuile.Xmin) / Tuile.DX);
		int maille_observateur_j = (int) ((Yobs - Tuile.Ymin) / Tuile.DY);

		// inscription de la maille observateur dans L'objet en memoire
		obj.set_Maille_Observateur(maille_observateur_i, maille_observateur_j);

		// ecriture du Polygone de requête selon paramètre de generation
		// 1-recuperation des mailles extremes de l'espace à mettre en memoire
		int mailleMax_i = maille_observateur_i
				+ obj.demi_espace_memoire_maille();
		int mailleMin_i = maille_observateur_i
				- obj.demi_espace_memoire_maille();
		int mailleMax_j = maille_observateur_j
				+ obj.demi_espace_memoire_maille();
		int mailleMin_j = maille_observateur_j
				- obj.demi_espace_memoire_maille();
		// 2-passage en coordonnees geographiques
		int Xmin = (Tuile.Xmin + mailleMin_i * Tuile.DX);
		int Ymin = (Tuile.Ymin + mailleMin_j * Tuile.DY);
		int Xmax = (Tuile.Xmin + (mailleMax_i + 1) * Tuile.DX);
		int Ymax = (Tuile.Ymin + (mailleMax_j + 1) * Tuile.DY);

		String Polygone = "SRID=" + "2154" + ";" + "POLYGON((" + Xmin + " "
				+ Ymin + "," + Xmax + " " + Ymin + "," + Xmax + " " + Ymax
				+ "," + Xmin + " " + Ymax + "," + Xmin + " " + Ymin + "))";
		PGgeometry polygone;
		try {
			polygone = new PGgeometry(Polygone);
			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(

					// " SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;"
					// " SELECT *, ST_CENTROID(geom) FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
							" SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"
									+ polygone.getValue() + "', 2154) ) ;"

					);
			System.out.println(polygone.getValue());

			// Mise a disposition des conteneurs
			Maison home1 = new Maison();
			Lampadaire lamp = new Lampadaire();
			Arbre arbre = new Arbre();
			ObjetPonctuel objet_ponctuel = new ObjetPonctuel();

			while (result.next()) {
				if (result.absolute(result.getRow()))

					switch (result.getString("type")) {
					case "maison":
						// Instanciation d'une maison
						home1 = new Maison(result.getInt("gid"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(home1);
						break;
					case "lampadaire":
						// Instanciation d'un lampadaire
						lamp = new Lampadaire(
								result.getInt("gid"),
								// result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(lamp);

					case "arbre":
						// Instanciation d'un qrbre
						arbre = new Arbre(result.getInt("gid"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(arbre);

						break;
					default:
						// throw new IllegalArgumentException("Invalid type: " +
						// result.getString("type"));
						// instanciation d'un nouvel objet ponctuel
						objet_ponctuel = new ObjetPonctuel(
								result.getInt("gid"), result.getString("type"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(objet_ponctuel);
					}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode "legacy" - Methode Statique pour la MAJ globale d'un
	 * Conteneur_objet - Obsolete le Buffer ne recharge pas les objets swappes
	 * dans le buffer des objets visibles
	 */
	public static void selection_geographique(Conteneur_objet obj, Float Xobs,
			Float Yobs, int interval_de_maille) {

		// Vidage du buffer
		obj.vide_Objet_en_memoire();

		// recuperation de la maille observateur
		int maille_observateur_i = (int) ((Xobs - Tuile.Xmin) / Tuile.DX);
		int maille_observateur_j = (int) ((Yobs - Tuile.Ymin) / Tuile.DY);

		// inscription de la maille observateur dans L'objet en memoire
		obj.set_Maille_Observateur(maille_observateur_i, maille_observateur_j);

		// ecriture du Polygone de requête selon paramètre de generation
		// 1-recuperation des mailles extremes de l'espace à mettre en memoire
		int mailleMax_i = maille_observateur_i
				+ obj.demi_espace_memoire_maille();
		int mailleMin_i = maille_observateur_i
				- obj.demi_espace_memoire_maille();
		int mailleMax_j = maille_observateur_j
				+ obj.demi_espace_memoire_maille();
		int mailleMin_j = maille_observateur_j
				- obj.demi_espace_memoire_maille();

		// 2-Calcul de l'espace de swap ( zone du buffer correspondant à
		// l'espace de "SWAP" du visible)
		int mailleSwapMax_i = maille_observateur_i
				+ (int) obj.dimension_espace_visible / 2;
		int mailleSwapMin_i = maille_observateur_i
				- (int) obj.dimension_espace_visible / 2;
		int mailleSwapMax_j = maille_observateur_j
				+ (int) obj.dimension_espace_visible / 2;
		int mailleSwapMin_j = maille_observateur_j
				- (int) obj.dimension_espace_visible / 2;

		// 3-passage en coordonnees geographiques - Extension Buffer
		int Xmin = (Tuile.Xmin + mailleMin_i * Tuile.DX);
		int Ymin = (Tuile.Ymin + mailleMin_j * Tuile.DY);
		int Xmax = (Tuile.Xmin + (mailleMax_i + 1) * Tuile.DX);
		int Ymax = (Tuile.Ymin + (mailleMax_j + 1) * Tuile.DY);

		// 4-passage en coordonnees geographiques - Extension Swap
		int Xswapmin = mailleSwapMin_i * interval_de_maille;
		int Yswapmin = mailleSwapMin_j * interval_de_maille;
		int Xswapmax = (mailleSwapMax_i + 1) * interval_de_maille;
		int Yswapmax = (mailleSwapMax_j + 1) * interval_de_maille;

		// Polygon a trou
		String Polygone = "SRID=" + "4326" + ";" + "POLYGON((" + Xmin + " "
				+ Ymin + "," + Xmax + " " + Ymin + "," + Xmax + " " + Ymax
				+ "," + Xmin + " " + Ymax + "," + Xmin + " " + Ymin + ")"
				+ ",(" + Xswapmin + " " + Yswapmin + "," + Xswapmax + " "
				+ Yswapmin + "," + Xswapmax + " " + Yswapmax + "," + Xswapmin
				+ " " + Yswapmax + "," + Xswapmin + " " + Yswapmin + "))";

		// //Polygon plein
		// String Polygone = "SRID=" + "2154" + ";" + "POLYGON(("+Xmin+" "+
		// Ymin+ ","+Xmax+" "+Ymin+","+ Xmax+" "+ Ymax+","+ Xmin+" "+ Ymax+","+
		// Xmin+" "+ Ymin+"))";

		PGgeometry polygone;
		try {
			polygone = new PGgeometry(Polygone);
			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(

					// "Multifoncteur" - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
							" SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"
									+ polygone.getValue() + "', 2154) ) ;"

					);

			// Mise a disposition des conteneurs d'objet (instance vide)
			Maison home1 = new Maison();
			Lampadaire lamp = new Lampadaire();
			Arbre arbre = new Arbre();
			ObjetPonctuel objet_ponctuel = new ObjetPonctuel();

			while (result.next()) {
				if (result.absolute(result.getRow()))

					switch (result.getString("type")) {
					case "maison":
						// Instanciation d'une maison
						home1 = new Maison(result.getInt("gid"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(home1);
						break;
					case "lampadaire":
						// Instanciation d'un lampadaire
						lamp = new Lampadaire(
								result.getInt("gid"),
								// result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(lamp);

					case "arbre":
						// Instanciation d'un qrbre
						arbre = new Arbre(result.getInt("gid"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(arbre);

						break;
					default:
						// throw new IllegalArgumentException("Invalid type: " +
						// result.getString("type"));
						// instanciation d'un nouvel objet ponctuel
						objet_ponctuel = new ObjetPonctuel(
								result.getInt("gid"), result.getString("type"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le vecteur de vecteur d'objet
						obj.AjoutObjet(objet_ponctuel);
					}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode "legacy" - Methode Statique pour la selection globale d'un
	 * Conteneur_objet - Obsolete Cette methode est un prototype ayant servi
	 * pour la definition de la classe iasig.dao.OrthoDAO
	 */
	public static void selection_raster(Objet_Postgre<Raster_img_mnt> obj,
			int i, int j) {

		byte[] content = null;
		// Dataset dattaset = null;
		// type de raster enregistr� (img ou rast
		String type_raster = "img";
		// conversion des valeurs de i et j vers l'�quivalent en indice
		int id = (i - 1) * 99 + j;

		try {

			// vidage prealable du vecteur

			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(

					// " SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;"
					// " SELECT ID, ST_AsPNG(rast) as image, I, J, type_Raster_img_mnt, Resolution FROM route WHERE ST_WITHIN(rast, ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"//
					// WHERE gid>="+a+" AND gid<="+b+";"//
							" SELECT rid, ST_AsPNG(rast) AS img, ST_DumpAsPolygons(rast) AS raster FROM mnt_img WHERE rid='"
									+ id + "';");
			Raster_img_mnt raster_img_mnt;

			while (result.next()) {
				if (result.absolute(result.getRow()))

					content = result.getBytes("img");
				ByteArrayInputStream bis = new ByteArrayInputStream(content);
				BufferedImage image = null;
				try {
					image = ImageIO.read(bis);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (type_raster == "img") {
					raster_img_mnt = new Raster_img_mnt(result.getInt("rid"),
							image, i, j, "img", 1

					);
				} else {
					raster_img_mnt = new Raster_img_mnt(result.getInt("rid"),
							(PGgeometry) result.getObject("raster"), i, j,
							"mnt", 1

					);
				}
				obj.AjoutObjet(raster_img_mnt);
				System.out.println("id_ortho " + obj.getElement(0).get_ID()
						+ " ");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode Statique pour la selection globale des objets contenu dans la BDD
	 * Arbres/Lampadaires/Maisons/.... Cette méthode est dédiée à
	 * l'interrogation de la vue matérialisée "generic_object" indexant les
	 * objets ponctuels au sein d'un table unique, indexée géographiquement
	 * (index GiST) Les objets sont instanciés séquentiellement lors du parcours
	 * du resultat de la requete SQL et sont distribués dans le Buffer, selon
	 * leur indexation au maillage "monde" ... Cette méthode construit une
	 * requête géographique sur l'étendue des objets à selectionner en se basant
	 * sur l'indexation géographique de la Base permise par l'extension POSTGIS
	 * de POSTGRESQL
	 * 
	 * @param obj
	 *            , le buffer en cours d'utilisation
	 */
	public static void selection_geographique_buffer(
			iasig.univers.view.Buffer obj) {

		try {
			// polygone = new PGgeometry(Polygone);
			// connection statique à la BDD et envoi d'une requête géographiqe
			// SQL compilée pour rapidité d'execution
			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
							" SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_Difference(ST_GeomFromText('"
									+ /* polygone A; nouveau */obj.polygone_bufferA
											.getValue()
									+ "', 2154), ST_GeomFromText('"
									+ /* polygone B; Ancien */obj.polygone_bufferB
											.getValue() + "', 2154) )) ;"
					// " SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+/*polygone
					// A;
					// nouveau*/obj.polygone_bufferA.getValue()+"', 2154) ) ;"

					);
			// System.out.println(polygone.getValue());

			// Mise a disposition des conteneurs d'objets
			Maison home1 = new Maison();
			Lampadaire lamp = new Lampadaire();
			Arbre arbre = new Arbre();
			ObjetPonctuel objet_ponctuel = new ObjetPonctuel();

			System.out.println("Objet: parcours du resultSet de la requête");

			while (result.next()) {
				if (result.absolute(result.getRow()))

					switch (result.getString("type")) {
					case "maison":
						// Instanciation d'une maison
						home1 = new Maison(result.getInt("gid"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(home1);
						break;
					case "lampadaire":
						// Instanciation d'un lampadaire
						lamp = new Lampadaire(
								result.getInt("gid"),
								// result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(lamp);

					case "arbre":
						// Instanciation d'un qrbre
						arbre = new Arbre(result.getInt("gid"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(arbre);

						break;
					default:
						// throw new IllegalArgumentException("Invalid type: " +
						// result.getString("type"));
						// instanciation d'un nouvel objet ponctuel
						objet_ponctuel = new ObjetPonctuel(
								result.getInt("gid"), result.getString("type"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(objet_ponctuel);
					}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode Statique pour la selection globale des élément du bâti
	 * indiférenciée contenu dans la BDD Issu de la BDTOPO74 IGN Cette méthode
	 * est dédiée à l'interrogation de la table du bâti indiférenciée indexant
	 * Indexés géographiquement (index GiST) Les objets sont instanciés
	 * séquentiellement lors du parcours du resultat de la requete SQL et sont
	 * distribués dans le Buffer, selon leur indexation au maillage "monde" ...
	 * Cette méthode construit une requête géographique sur l'étendue des objets
	 * à selectionner en se basant sur l'indexation géographique de la Base
	 * permise par l'extension POSTGIS de POSTGRESQL
	 * 
	 * @param obj
	 *            , le buffer en cours d'utilisation
	 */
	public static void selection_geographique_bati_buffer(
			iasig.univers.view.Buffer obj) {

		try {
			// connection statique à la BDD et envoi d'une requête géographiqe
			// SQL compilée pour rapidité d'execution
			// polygone = new PGgeometry(Polygone);
			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
							" SELECT *, ST_CENTROID(geom) FROM bati_indif_74 WHERE ST_WITHIN(ST_CENTROID(geom), ST_Difference(ST_GeomFromText('"
									+ /* polygone A; nouveau */obj.polygone_bufferA
											.getValue()
									+ "', 2154), ST_GeomFromText('"
									+ /* polygone B; Ancien */obj.polygone_bufferB
											.getValue() + "', 2154) )) ;"

					// " SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;"
					// " SELECT *, ST_CENTROID(geom) FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
					// " SELECT hauteur,geom,ST_CENTROID(geom) FROM bati_indif_74 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"
					);
			// System.out.println(polygone.getValue());

			// Mise a disposition du conteneur bati_indiferencie
			Batiment bati = new Batiment();

			System.out.println("Bati: parcours du resultSet de la requête");

			while (result.next()) {
				if (result.absolute(result.getRow()))

					// Instanciation d'un bati
					bati = new Batiment((PGgeometry) result.getObject("geom"),
							(PGgeometry) result.getObject("st_centroid"),
							result.getInt("hauteur"));
				// Ajout de l'objet dans le buffer (arraylist de arraylist)
				obj.AjoutBati(bati);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void selection_complete_objet_buffer(
			iasig.univers.view.Buffer obj) {

		try {
			// polygone = new PGgeometry(Polygone);
			// connection statique à la BDD et envoi d'une requête géographiqe
			// SQL compilée pour rapidité d'execution
			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
					// " SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_Difference(ST_GeomFromText('"+/*polygone
					// A;
					// nouveau*/obj.polygone_bufferA.getValue()+"', 2154), ST_GeomFromText('"+/*polygone
					// B;
					// Ancien*/obj.polygone_bufferB.getValue()+"', 2154) )) ;"
							" SELECT *, ST_CENTROID(geom) FROM generic_object1 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"
									+ /* polygone A; nouveau */obj.polygone_bufferA
											.getValue() + "', 2154) ) ;"

					);
			// System.out.println(polygone.getValue());

			// Mise a disposition des conteneurs d'objets
			Maison home1 = new Maison();
			Lampadaire lamp = new Lampadaire();
			Arbre arbre = new Arbre();
			ObjetPonctuel objet_ponctuel = new ObjetPonctuel();

			System.out.println("Objet: parcours du resultSet de la requête");

			while (result.next()) {
				if (result.absolute(result.getRow()))

					switch (result.getString("type")) {
					case "maison":
						// Instanciation d'une maison
						home1 = new Maison(result.getInt("gid"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(home1);
						break;
					case "lampadaire":
						// Instanciation d'un lampadaire
						lamp = new Lampadaire(
								result.getInt("gid"),
								// result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),

								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(lamp);

					case "arbre":
						// Instanciation d'un qrbre
						arbre = new Arbre(result.getInt("gid"),
								result.getFloat("rotz"), result.getFloat("f"),
								result.getString("type"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(arbre);

						break;
					default:
						// throw new IllegalArgumentException("Invalid type: " +
						// result.getString("type"));
						// instanciation d'un nouvel objet ponctuel
						objet_ponctuel = new ObjetPonctuel(
								result.getInt("gid"), result.getString("type"),
								result.getString("nom"),
								result.getFloat("rotz"), result.getFloat("f"),
								(PGgeometry) result.getObject("geom"),
								(PGgeometry) result.getObject("st_centroid"));
						// Ajout de l'objet dans le buffer (arraylist de
						// arraylist)
						obj.AjoutObjet(objet_ponctuel);
					}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	 /* Methode Statique pour la selection globale des élément du bâti
	 * indiférenciée contenu dans la BDD Issu de la BDTOPO74 IGN Cette méthode
	 * est dédiée à l'interrogation de la table du bâti indiférenciée indexant
	 * Indexés géographiquement (index GiST) Les objets sont instanciés
	 * séquentiellement lors du parcours du resultat de la requete SQL et sont
	 * distribués dans le Buffer, selon leur indexation au maillage "monde" ...
	 * Cette méthode construit une requête géographique sur l'étendue des objets
	 * à selectionner en se basant sur l'indexation géographique de la Base
	 * permise par l'extension POSTGIS de POSTGRESQL
	 * 
	 * @param obj
	 *            , le buffer en cours d'utilisation
	 */
	public static void selection_complete_bati_buffer(iasig.univers.view.Buffer obj) {

		try {
			// connection statique à la BDD et envoi d'une requête géographiqe
			// SQL compilée pour rapidité d'execution
			// polygone = new PGgeometry(Polygone);
			ResultSet result = connection_statique
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.TYPE_SCROLL_INSENSITIVE)
					.executeQuery(
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
					" SELECT *, ST_CENTROID(geom) FROM bati_indif_74 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+ /* polygone A; nouveau */obj.polygone_bufferA.getValue()
									+ "', 2154)) ;"

					// " SELECT * FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('POLYGON((2000 2000,8000 2000, 8000 8000, 2000 8000, 2000 2000))', 4326) )  ;"
					// " SELECT *, ST_CENTROID(geom) FROM maison2 WHERE ST_WITHIN(centroid, ST_GeomFromText('"+polygone.getValue()+"', 4326) ) ;"
					// Multifoncteur - Selectionne l'integralite des champs de
					// la table de objets generiques inclus dans le polygone
					// passe en argument
					// " SELECT hauteur,geom,ST_CENTROID(geom) FROM bati_indif_74 WHERE ST_WITHIN(ST_CENTROID(geom), ST_GeomFromText('"+polygone.getValue()+"', 2154) ) ;"
					);
			// System.out.println(polygone.getValue());

			// Mise a disposition du conteneur bati_indiferencie
			Batiment bati = new Batiment();

			System.out.println("Bati: parcours du resultSet de la requête");

			while (result.next()) {
				if (result.absolute(result.getRow()))

					// Instanciation d'un bati
					bati = new Batiment((PGgeometry) result.getObject("geom"),
							(PGgeometry) result.getObject("st_centroid"),
							result.getInt("hauteur"));
				// Ajout de l'objet dans le buffer (arraylist de arraylist)
				obj.AjoutBati(bati);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
