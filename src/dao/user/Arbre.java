package dao.user;

import org.postgis.PGgeometry;
import org.postgis.Point;


public class Arbre {
	
	private Integer id;
	private Integer gid;
	private String nom;
	private float rotz;
	private float echelle;
	private String type;
	//attribut typés Postgis
	private PGgeometry geom;
	private PGgeometry centroid;
	//Rattachement à une maille
	private int maillei;
	private int maillej;

	
	private float X;
	private float Y;
	

	private Integer niveau;
	protected String table = "lampadaires";
	
	
	
		//constructeur1 - vide
		public Arbre(){}
		//constructeur2
		public Arbre(Integer id, Integer gid, float X, float Y, PGgeometry centroid){
			this();
			this.id = id; this.gid = gid;
			this.X = X ; this.Y = Y; 
			this.centroid = centroid;
			}
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
//TODO envoyer les coordonn�es du coin de l'univers proprement
					int x0=916000;
					int y0=6513000;
					int interval_de_maille = 1000;
					this.maillei = (int) (((int)pt.x-x0)/interval_de_maille); 
					this.maillej = (int) (((int)pt.y-y0)/interval_de_maille); 
				}
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
//TODO envoyer les coordonn�es du coin de l'univers proprement
					int x0=916000;
					int y0=6513000;
					int interval_de_maille = 1000;
					this.maillei = (int) (((int)pt.x-x0)/interval_de_maille); 
					this.maillej = (int) (((int)pt.y-y0)/interval_de_maille); 
				}

	
		//méthodes publique Accesseur
		public PGgeometry getCentroid()
		{
			return centroid;
		}
		
		public PGgeometry getGeom()
		{
			return geom;
		}
		
		public float getRotz()
		{
			return rotz;	
		}
		
		public float getEchelle()
		{
			return echelle;	
		}
		
		public double getX1(){
			Point pt = (Point)geom.getGeometry();
			return pt.x;
		}
		
		public double getY1(){
			Point pt = (Point)geom.getGeometry();
			return pt.y;
		}

		public double getZ1(){
			Point pt = (Point)geom.getGeometry();
			return pt.z;
		}

		public Integer getId()
		{
			return id;
		}
		
		public Integer getGid()
		{
			return gid;
		}
		
		public Integer getNiveau()
		{
			return niveau;
		}
		public String getType()
		{
			return type;
		}

		
		public float getX()
		{
			return X;
		}
		
		public float getY()
		{
			return Y; 
		}
	
		public String getNom()
		{
			return nom;
		}
		
		public Integer getMaille_i(){
			return maillei;
		}
		
		public Integer getMaille_j(){
			return maillej;
		}
}

