package iasig.dao.user;

import iasig.mobile.view.Tuile;

import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 * Classe "legacy" - Classe permettant une mise en mémoire des objets generique de type ponctuel (rattachement à un point unique)
 *  contenus dans la BDD sous forme 
 * de vecteur de vecteur - Obsolete
 */
public class ObjetPonctuel {

	private Integer id;
	private String type;
	private String nom;
	private float rotz;
	private float echelle;	
	private PGgeometry geom;
	private PGgeometry centroid;
	//rattachement à une maille
	private Integer maillei;
	private Integer maillej;
	
			
	

			//constructeur1 - vide
			public ObjetPonctuel(){}
			//constructeur2
			public ObjetPonctuel(Integer id, String type, String nom, float echelle, float rotz, PGgeometry geom, PGgeometry centroid ){
				
				this();
				this.id = id;
				this.type = type; this.nom = nom;
				this.echelle = echelle; this.rotz = rotz;
				this.geom = geom;
				this.centroid = centroid;

				
				Point pt = (Point)centroid.getGeometry();
				
				this.maillei = (int) ((pt.x-Tuile.Xmin)/Tuile.DX); 
				this.maillej = (int) ((pt.y-Tuile.Ymin)/Tuile.DY); 
				
			}
		
			
			//méthodes publique Accesseur
			public PGgeometry getGeom()
			{
				return geom;
			}
			
			public PGgeometry getCentroid()
			{
				return centroid;
			}
			
			public float getRotz()
			{
				return rotz;	
			}
			
			public String getType()
			{
				return type;
			}
			
			public float getEchelle()
			{
				return echelle;	
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
			
			public Integer getMaille_i()
			{
				return maillei;
			}
			
			public Integer getMaille_j()
			{
				return maillej;
			}
			
			public String getNom()
			{
				return nom;
			}
			
}
