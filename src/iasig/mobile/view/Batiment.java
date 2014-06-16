/**
 * 
 */
package iasig.mobile.view;

import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;

import com.sun.j3d.utils.geometry.GeometryInfo;

/**
 * @author Jean
 *
 */
public class Batiment {

	/**
	 * @param args
	 */
	
	/*/////////////////////ATTRIBUTS////////////////////*/
	public int H; //Hauteur du b�timent
	private PGgeometry geom;
	private PGgeometry centroid;
	private int maillei;
	private int maillej;

	private  MultiPolygon forme; //Forme de l'emprise du b�timent
	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/*********************CONSTRUCTEUR*******************/
	
	//---------------------------------------------------
	public Batiment(PGgeometry geom,PGgeometry centroid, int hauteur){
		this.geom = geom;
		this.centroid = centroid;
		this.forme  = (MultiPolygon)this.geom.getGeometry(); ;
		this.H = 10*hauteur;
		
		Point pt = (Point)centroid.getGeometry();

		this.maillei = (int) ((pt.x-Tuile.Xmin)/Tuile.DX); 
		this.maillej = (int) ((pt.y-Tuile.Ymin)/Tuile.DY); 
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	
	
	public Batiment() {
		// Vide
	}

	/*******************    GETTERS    ******************/
	
	//---------------------------------------------------
	public int getMaille_i(){
		return maillei;
	}
	//---------------------------------------------------

	//---------------------------------------------------
		public int getMaille_j(){
			return maillej;
		}
	//---------------------------------------------------

	
	
	/*******************AUTRES METHODES******************/
	
	//---------------------------------------------------
	public static void draw(BranchGroup bg, ArrayList<Batiment> vect){
		/** 
		 * FONCTION :
		 * Construire l'objet graphique repr�sentant le tunnel.
		 * 
		 * 
		 * RETOURNE :
		 * - [Scalaire, objet de la classe Shape3D] : objet graphique repr�sentant le tunnel, � attacher � un Group.
		 **/
		if (vect.size()==0) {
			return;
		 }
		
		 for (int j = 0; j < vect.size(); j++) {
		 
			 	//cr�ation du BranchGroup Objet
			 
			   	BranchGroup bgObj = new BranchGroup ( ) ;
			   	bgObj.setCapability(BranchGroup.ALLOW_DETACH);
			   	
				//vecteur d'objets ponctuels
				ObjetPonctuel objpt = new ObjetPonctuel();
				
				//System.out.println("objet: "+vect.get(j).getClass());

				GeometryArray[] geometrie = geometry(vect.get(j));
				
				Appearance apparence = apparence();
				
				Shape3D shape3d = new Shape3D(geometrie[0],apparence);
				shape3d.addGeometry(geometrie[1]);
				
//				for(int k=2;k<=geometrie.length;k++){
//					shape3d.addGeometry(geometrie[k-1]);
//				}
				bgObj.addChild(shape3d);
				bgObj.setName("BATI");
				
				
				//DECOMMENTER POUR ATTACHER le BATI au chargement
			    bg.addChild(bgObj);
				
		 
		 }
		
		
		
	}
	//---------------------------------------------------	
	
	//---------------------------------------------------
	public static GeometryArray[] geometry(Batiment bat){
		/** 
		 * FONCTION :
		 * Construire la geometrie du betiment, un ensemble de polygones.
		 * 
		 * RETOURNE :
		 * - [Vecteur(2 elements) d'objets de la classe GeometryArray] : Geometrie du batiment sous forme d'un ensemble de polygones.
		 **/
		
		GeometryArray[] geometrie = new GeometryArray[2];
		
		MultiPolygon ml = bat.forme;
		//System.out.println("le polygone: "+ml.getValue()+" "+"nombre de points: "+ ml.numPoints());
		int nbpoint = ml.numPoints()-1;

		

		GeometryInfo polygone;
	 	
	 	//PLAFOND
	 	polygone = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
	 	Point3d[] points = new Point3d[nbpoint];
		Color3f[] colors = new Color3f[nbpoint]; 
	 	int[] stripCounts = new int[1];
		int[] contourCount=new int[1];

		stripCounts[0] = nbpoint;
		contourCount[0]=1;
		 
	 	for (int i = 1; i <= nbpoint; i++) {
	 		points[i-1] = new Point3d(ml.getPoint(i-1).x,ml.getPoint(i-1).y,ml.getPoint(i-1).z+bat.H);
	 		colors[i-1]   = World.Magenta;
	 	}
	 	
	 	polygone.setCoordinates(points);
	 	polygone.setStripCounts(stripCounts);
	 	polygone.setContourCounts(contourCount);
	 	
	 	polygone.setColors(colors);

	 	geometrie[0] = polygone.getGeometryArray();
	 	
	 	//COTES
 		polygone = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
 		points = new Point3d[4*nbpoint];
	 	colors = new Color3f[4*nbpoint];
	 	
	 	for (int i = 1; i <= nbpoint-1; i++) {
	 		points[4*(i-1)] = new Point3d(ml.getPoint(i-1).x,ml.getPoint(i-1).y,ml.getPoint(i-1).z);
	 		points[4*(i-1)+1] = new Point3d(ml.getPoint(i-1).x,ml.getPoint(i-1).y,ml.getPoint(i-1).z+bat.H);
	 		points[4*(i-1)+2] = new Point3d(ml.getPoint(i).x,ml.getPoint(i).y,ml.getPoint(i).z+bat.H);
	 		points[4*(i-1)+3] = new Point3d(ml.getPoint(i).x,ml.getPoint(i).y,ml.getPoint(i).z);
	 		
	 		colors[4*(i-1)]   = World.Green;
	 		colors[4*(i-1)+1] = World.Yellow;
	 		colors[4*(i-1)+2] = World.Red;
	 		colors[4*(i-1)+3] = World.Blue;
		}
	 	
	 	points[4*nbpoint-4] = new Point3d(ml.getPoint(nbpoint-1).x,ml.getPoint(nbpoint-1).y,ml.getPoint(nbpoint-1).z);
 		points[4*nbpoint-3] = new Point3d(ml.getPoint(nbpoint-1).x,ml.getPoint(nbpoint-1).y,ml.getPoint(nbpoint-1).z+bat.H);
 		points[4*nbpoint-2] = new Point3d(ml.getPoint(0).x,ml.getPoint(0).y,ml.getPoint(0).z+bat.H);
 		points[4*nbpoint-1] = new Point3d(ml.getPoint(0).x,ml.getPoint(0).y,ml.getPoint(0).z);
 		
 		colors[4*nbpoint-4] = World.Green;
 		colors[4*nbpoint-3] = World.Yellow;
 		colors[4*nbpoint-2] = World.Red;
 		colors[4*nbpoint-1] = World.Blue;
	 	
	 	polygone.setCoordinates(points);
	 	polygone.setColors(colors);
	 	
	 	geometrie[1]=polygone.getGeometryArray();

	 	return geometrie;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public static Appearance apparence(){
		/** 
		 * FONCTION :
		 * D�finir les param�tres d�finissant l'apparence du tunnel.
		 * 
		 * RETOURNE :
		 * - [Scalaire, objet de la classe Appearance] : L'apparence de l'objet.
		 **/

		Appearance app = new Appearance();
		
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE); //Pas de culling : faces toujours visibles quelque-soit l'orientation de la cam�ra
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL); //Les faces sont r�pr�sent�es de mani�re surfacique
		app.setPolygonAttributes(polyAttrib);

		return app;	
	}
	//---------------------------------------------------	
	
	/****************************************************/

}
