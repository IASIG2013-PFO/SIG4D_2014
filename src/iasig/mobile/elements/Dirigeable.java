package iasig.mobile.elements;

import iasig.mobile.deplacement.AvanceDirigeable;
import iasig.univers.view.Constante;
import iasig.univers.view.World;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * 
 * @author Gaël
 * @
 */
public class Dirigeable extends VehiculeLibre implements Constante {
	/**
	 *Constructeur du dirigeable 
	 *	 
	 * @param x : position en x au démarrage
	 * @param y : position en y au démarrage
	 * @param z : position en z au démarrage
	 * @return : un objet dirigeable
	 */
	public Dirigeable(float x,float y,float z){
		super(x,y,z,0,0,0);			
		this.setShape(this.LoadObj("file:src/Images/dirigeable.obj"));
		}
	/**
	 * 
	 * @return un double[4] contenant la position x,y,z et la direction (en radian) du dirigeable dans le monde
	 */
	public double[] getVWorldPosition(){
		Transform3D test = new Transform3D();
		this.getCaisse().getLocalToVworld(test);
		Vector3f position = new Vector3f();
		test.get(position);
		Matrix3d testmatrice = new Matrix3d();
		test.get(testmatrice);
		double[] tab = new double[4];
		tab[0]=position.x;
		tab[1]=position.y;
		tab[2]=position.z;

		if (testmatrice.m00 >= 0 && testmatrice.m10 >=0 ){
			tab[3] = Math.asin(testmatrice.m10) ;//* 180 / Math.PI;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 > 0){
			tab[3] = (180 - Math.asin(testmatrice.m10));// * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 < 0 ){
			tab[3] = (180 + Math.abs(Math.asin(testmatrice.m10)));// * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 > 0 && testmatrice.m10 < 0 ){
			tab[3] = (360 - Math.abs(Math.asin(testmatrice.m10)));// * 180 / Math.PI)) ;
		}
		return tab;
	}
	
	/**
	 * Getter de position vue arrière
	 * @return un double[4] contenant la position x,y,z et la direction (en radian) de la caméra "vue arrière" de la voiture
	 */
	public double[] getPositionCamExt(){
		Transform3D test = new Transform3D();
		this.spherecam.getLocalToVworld(test);
		Vector3f position = new Vector3f();
		test.get(position);
		Matrix3d testmatrice = new Matrix3d();
		test.get(testmatrice);
		double[] tab = new double[4];
		tab[0]=position.x;
		tab[1]=position.y;
		tab[2]=position.z;

		if (testmatrice.m00 >= 0 && testmatrice.m10 >=0 ){
			tab[3] = Math.asin(testmatrice.m10) * 180 / Math.PI;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 > 0){
			tab[3] = (180 - Math.asin(testmatrice.m10) * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 < 0 ){
			tab[3] = (180 + Math.abs(Math.asin(testmatrice.m10)) * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 > 0 && testmatrice.m10 < 0 ){
			tab[3] = (360 - Math.abs(Math.asin(testmatrice.m10) * 180 / Math.PI)) ;
		}
		return tab;
	}

	/**
	 * Getter de la position de la caméra conducteur
	 * @return un double[2][4] à la première ligne : position du conducteur dans la voiture  // 2ème ligne : point à regarder (pour le glulookat) 
	 */
public double[][] getPositionCamConducteur(){
		
		Transform3D test = new Transform3D();
		this.spherecamconducteur.getLocalToVworld(test);
		Vector3f position = new Vector3f();
		test.get(position);
		Matrix3d testmatrice = new Matrix3d();
		test.get(testmatrice);
		double[][] tab = new double[2][4];
		tab[0][0]=position.x;
		tab[0][1]=position.y;
		tab[0][2]=position.z;

		if (testmatrice.m00 >= 0 && testmatrice.m10 >=0 ){
			tab[0][3] = Math.asin(testmatrice.m10) * 180 / Math.PI;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 > 0){
			tab[0][3] = (180 - Math.asin(testmatrice.m10) * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 < 0 ){
			tab[0][3] = (180 + Math.abs(Math.asin(testmatrice.m10)) * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 > 0 && testmatrice.m10 < 0 ){
			tab[0][3] = (360 - Math.abs(Math.asin(testmatrice.m10) * 180 / Math.PI)) ;
		}
		Transform3D test2 = new Transform3D();
		this.spherecamconducteur2.getLocalToVworld(test2);
		Vector3f position2 = new Vector3f();
		test2.get(position2);
		Matrix3d testmatrice2 = new Matrix3d();
		test2.get(testmatrice2);
		tab[1][0]=position2.x;
		tab[1][1]=position2.y;
		tab[1][2]=position2.z;
		
		if (testmatrice2.m00 >= 0 && testmatrice2.m10 >=0 ){
			tab[1][3] = Math.asin(testmatrice2.m10) * 180 / Math.PI;
		}
		else if(testmatrice2.m00 < 0 && testmatrice.m10 > 0){
			tab[1][3] = (180 - Math.asin(testmatrice2.m10) * 180 / Math.PI) ;
		}
		else if(testmatrice2.m00 < 0 && testmatrice.m10 < 0 ){
			tab[1][3] = (180 + Math.abs(Math.asin(testmatrice2.m10)) * 180 / Math.PI) ;
		}
		else if(testmatrice2.m00 > 0 && testmatrice.m10 < 0 ){
			tab[1][3] = (360 - Math.abs(Math.asin(testmatrice2.m10) * 180 / Math.PI)) ;
		}
		System.out.println("tab " + tab[0][0] + " " + tab[0][1] + " " + tab[0][2]);
		System.out.println("tab2 " + tab[1][0] + " " + tab[1][1] + " " + tab[1][2]);
		return tab;
	}
/**
 * 
 * @param world
 * @return Display le dirigeable
 */
public BranchGroup GetBranchGroup(World world){
	
	BranchGroup BG = new BranchGroup();
	//TG GLOBAL
	TransformGroup transformG = new TransformGroup();
	TransformGroup transform = new TransformGroup();
	TransformGroup transformvirage = new TransformGroup();
	
	BoundingSphere sphere = new BoundingSphere(new Point3d(),100000); 
	
	transform.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transform.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformG.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformG.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformvirage.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformvirage.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
		
	Transform3D t3d = new Transform3D() ;
	t3d.setTranslation(new Vector3d(this.getX(), this.getY(),this.getZ()));
	transformG.setTransform(t3d);
	transformG.addChild(transformvirage);
	transformvirage.addChild(this.getCaisse());
	DirectionalLight sun = new DirectionalLight(new Color3f(0.5f,0.5f,0.5f),new Vector3f(1,0,-1));
	sun.setEnable(true);
	sun.setInfluencingBounds(sphere);
	transformG.addChild(sun);
	//--------------Camera Exterieur
	TransformGroup camera = new TransformGroup();
	Transform3D tcam = new Transform3D() ;
	tcam.setTranslation(new Vector3f(-100,5,2));
	camera.setTransform(tcam);
	camera.addChild(this.spherecam);
	//------------------Camera Conducteur
	TransformGroup cameraconducteur = new TransformGroup();
	TransformGroup cameraconducteur2 = new TransformGroup();
	Transform3D tcam2 = new Transform3D() ;
	tcam2.setTranslation(new Vector3f(2f,0f,-15.f));
	cameraconducteur.setTransform(tcam2);
	cameraconducteur.addChild(this.spherecamconducteur);
	Transform3D tcam3 = new Transform3D() ;
	cameraconducteur.addChild(cameraconducteur2);
	tcam3.setTranslation(new Vector3f(20f,0f,0f));
	cameraconducteur2.setTransform(tcam3);
	cameraconducteur2.addChild(this.spherecamconducteur2);
	AvanceDirigeable avance = new AvanceDirigeable(transformG,this, world);
	transformG.addChild(cameraconducteur);
	transformG.addChild(avance);
	avance.setSchedulingBounds(sphere);
	transformG.addChild(camera);
	transform.addChild(transformG);
	BG.addChild(transform);
	BG.compile();
	
	return BG;
	}
}
	



