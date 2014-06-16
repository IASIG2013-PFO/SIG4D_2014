package iasig.dao.user;

import iasig.univers.view.Tuile;

import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 * Classe Définissant un Arbre
 * Les attributs sont homogènes avec les champs de la BDD de la table des lampadaires
 */
public class Arbre {
	
	private Integer id;
	private Integer gid;
	private String nom;
	private float rotz;
	private float echelle;
	private String type;
	//attribut types Postgis
	private PGgeometry geom;
	private PGgeometry centroid;
	//Rattachement à une maille
	private int maillei;
	private int maillej;

	
//	private float X;
//	private float Y;
	

	private Integer niveau;
	protected String table = "lampadaires";
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/*********************CONSTRUCTEUR*******************/
		//---------------------------------------------------
		//constructeur1 - vide
		public Arbre(){}
		//---------------------------------------------------
		//constructeur2
		public Arbre(Integer id, Integer gid, float X, float Y, PGgeometry centroid){
			this();
			this.id = id; this.gid = gid;
			//this.X = X ; this.Y = Y; 
			this.centroid = centroid;
			
			}
		//---------------------------------------------------
		//constructeur3
				public Arbre(Integer id, String nom, float rotz,float echelle, PGgeometry geom, PGgeometry centroid){
					this();
					this.id = id;
					this.nom = nom; 
					this.rotz = rotz;
					this.echelle = echelle;
					this.geom = geom;
					this.centroid = centroid;
					Point pt = (Point)centroid.getGeometry();

					this.maillei = (int) ((pt.x-Tuile.Xmin)/Tuile.DX); 
					this.maillej = (int) ((pt.y-Tuile.Ymin)/Tuile.DY); 
				}
		//---------------------------------------------------
		//constructeur4
				public Arbre(Integer id, float rotz,float echelle, String type, PGgeometry geom, PGgeometry centroid){
					this();
					this.id = id;
					this.rotz = rotz;
					this.echelle = echelle;
					this.type = type;
					this.geom = geom;
					this.centroid = centroid;
					Point pt = (Point)centroid.getGeometry();

					this.maillei = (int) ((pt.x-Tuile.Xmin)/Tuile.DX); 
					this.maillej = (int) ((pt.y-Tuile.Ymin)/Tuile.DY); 
				}
		//---------------------------------------------------
	
				
		/***********************GETTERS**********************/
		//---------------------------------------------------
		/**
		 * Getter sur le centroid
		 * retourne un objet PGgeometry; type étendu Postgis
		 */
		public PGgeometry getCentroid()
		{
			return centroid;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur la geometrie globale
		 * retourne un objet PGgeometry; type étendu Postgis
		 */
		public PGgeometry getGeom()
		{
			return geom;
		}
		//---------------------------------------------------
		
		/**
		 * Getter sur la rotation de l'objet
		 * retourne un float
		 */
		//---------------------------------------------------
		public float getRotz()
		{
			return rotz;	
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur l'echelle de l'objet
		 * retourne un float
		 */
		public float getEchelle()
		{
			return echelle;	
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur la coordonnée X de l'objet
		 * retourne un double
		 */
		public double getX1(){
			Point pt = (Point)geom.getGeometry();
			return pt.x;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur la coordonnée Y de l'objet
		 * retourne un double
		 */
		public double getY1(){
			Point pt = (Point)geom.getGeometry();
			return pt.y;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur la coordonnée Z de l'objet
		 * retourne un double
		 */
		public double getZ1(){
			Point pt = (Point)geom.getGeometry();
			return pt.z;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur l'ID de l'objet
		 * retourne un integer
		 */
		public int getId()
		{
			return id;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur le GID de l'objet - clef unique, position dans la table
		 * spécifique aux lampadaires
		 * retourne un integer
		 */
		public Integer getGid()
		{
			return gid;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur le niveau de l'objet - OBSOLETE
		 * retourne un integer
		 */
		public Integer getNiveau()
		{
			return niveau;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur le type de l'objet - NON UTILISE
		 * retourne un String
		 */
		public String getType()
		{
			return type;
		}
		//---------------------------------------------------
		
//		//---------------------------------------------------
//		public float getX()
//		{
//			return X;
//		}
//		//---------------------------------------------------
//		
//		//---------------------------------------------------
//		public float getY()
//		{
//			return Y; 
//		}
//		//---------------------------------------------------
	
		//---------------------------------------------------
		public String getNom()
		{
			return nom;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur la maille i (sur l'axe des X) de l'objet
		 * retourne un int
		 */
		public Integer getMaille_i(){
			return maillei;
		}
		//---------------------------------------------------
		
		//---------------------------------------------------
		/**
		 * Getter sur la maille j (sur l'axe des Y) de l'objet
		 * retourne un int
		 */
		public Integer getMaille_j(){
			return maillej;
		}
		//---------------------------------------------------
		/****************************************************/	

}

