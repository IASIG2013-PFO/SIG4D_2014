package iasig.dao.user;

import org.postgis.PGgeometry;
import org.postgis.Point;


public class Maison {
	
	private Integer id;
	private String X;
	private String Y;
	private String Z;
	private float rotz;
	private float echelle;
	private String nom = "noname";		
	private PGgeometry geom;
	private PGgeometry centroid;
	private Integer niveau;
	protected String table = "maison2";
	//rattachement à une maille
	private Integer maillei;
	private Integer maillej;
	
	
	
		//constructeur1 - vide
		public Maison(){}
		//constructeur2
		public Maison(Integer id, String X, String Y, String Z, String nom, PGgeometry centroid, Integer niveau){
			this();
			this.id = id;
			this.X = X ; this.Y = Y ; this.Z = Z ; 
			this.nom = nom; 
			this.niveau = niveau;
			this.centroid = centroid;
			Point pt = (Point)centroid.getGeometry();
			this.maillei = (int) ((int)pt.x/100); this.maillej = (int) ((int)pt.y/100); 
			}
		//constructeur3
		public Maison(Integer id, String X, String Y, String Z, String nom, PGgeometry centroid, Integer niveau, Integer maillei, Integer maillej){
			this();
			this.id = id;
			this.X = X ; this.Y = Y ; this.Z = Z ; 
			this.nom = nom; 
			this.niveau = niveau;
			this.centroid = centroid;
			this.maillei = maillei; this.maillej = maillej;
			}
		//constructeur4
		public Maison(Integer id, String nom, float rotz,float echelle, PGgeometry geom, PGgeometry centroid){
			this();
			this.id = id;
			this.nom = nom; 
			this.rotz = rotz;
			this.echelle = echelle;
			this.geom = geom;
			this.centroid = centroid;
			Point pt = (Point)centroid.getGeometry();
			this.maillei = (int) ((int)pt.x/100); this.maillej = (int) ((int)pt.y/100); 
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
		
		public Integer getMaille_i(){
			return maillei;
		}
		
		public Integer getMaille_j(){
			return maillej;
		}
		
		public double getX1(){
			Point pt = (Point)centroid.getGeometry();

			return pt.x;
			
		}
		
		public double getY1(){
			Point pt = (Point)centroid.getGeometry();
			return pt.y;
		}
		
		public double getZ1(){
			Point pt = (Point)centroid.getGeometry();
			return pt.z;
		}
		
		public Integer getId()
		{
			return id;
		}
		
		public Integer getNiveau()
		{
			return niveau;
		}
		
		public String getX()
		{
			return X;
		}
		public String getY()
		{
			return Y; 
		}
		public String getZ()
		{
			return Z; 
		}
		
		public String getNom()
		{
			return nom; 
		}
		
		
		//méthodes publique 'Set'
		public void setNom(String nom) {
			this.nom = nom;
		}
}

