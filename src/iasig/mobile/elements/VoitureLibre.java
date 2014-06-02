package iasig.mobile.elements;

import iasig.mobile.deplacement.AvanceVoiture;
import iasig.mobile.deplacement.DetecteurCollision;
import iasig.mobile.deplacement.Dynamique;
import iasig.mobile.view.*;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;


/**
 * 
 * @author Gaël
 * @
 */
public class VoitureLibre extends VehiculeLibre implements Constante {

	//Attributs de class de classe
	
	protected Roue[] roues = new Roue[4];
	public int typevoiture;

	
	
	/**
	 *Constructeur de voiture 
	 *	 
	 * @param x : position en x au démarrage
	 * @param y : position en y au démarrage
	 * @param z : position en z au démarrage
	 * @param length : longueur de la voiture
	 * @param width : largeur de la voiture
	 * @param heigth : hauteur de la voiture
	 * @param typevoiture (1 : voiture de police, 2 : BigFoot )
	 * @return : un objet voiture
	 */
	public VoitureLibre(float x,float y,float z,float length,float width,float heigth,int typevoiture){
		super(x, y, z, length, width, heigth);
		this.typevoiture = typevoiture;
		
		if(typevoiture == 1){ //Voiture de Police
		
		this.z = z + this.heigth + 2* Constante.rayonroue;
		//ROUE AVANT GAUCHE
		roues[0] = new Roue( this.length/0.89f,  - this.width, z,this.direction[0], this.direction[1],0,1);
		//ROUE AVANT DROITE
		roues[1] = new Roue(this.length/0.89f,  this.width,z ,this.direction[0], this.direction[1],1,1);
		//ROUE ARRIERE DROITE
		roues[2] = new Roue( - this.length/1.4f, - this.width,z,this.direction[0], this.direction[1],2,1);
		//ROUE ARRIERE GAUCHE
		roues[3] = new Roue( - this.length/1.4f,  this.width,z,this.direction[0], this.direction[1],3,1);
		
		this.shape = this.LoadObj("file:src/Images/caisse.obj");
		}
		else{     //4x4
			this.z = z  + 2 * Constante.rayonroue2;
			//ROUE AVANT GAUCHE
			roues[0] = new Roue( 8f,-4,z-5,this.direction[0], this.direction[1],0,2);
			//ROUE AVANT DROITE
			roues[1] = new Roue(8f,  4,z-5,this.direction[0], this.direction[1],1,2);
			//ROUE ARRIERE DROITE
			roues[2] = new Roue(-6.5f, 4,z-5,this.direction[0], this.direction[1],2,2);
			//ROUE ARRIERE GAUCHE
			roues[3] = new Roue(-6.5f,  -4,z-5,this.direction[0], this.direction[1],3,2);
			this.shape = this.LoadObj("file:src/Images/caisse2.obj");
		}
	}
	
	/**
	 * 
	 * @return un double[4] contenant la position x,y,z et la direction (en radian) de la voiture dans le monde
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
		
		return tab;
	}

/**
 * Getter des positions de toutes les roues de la voiture
 * @return un double[4][4] contenant la position x,y,z et la direction (en radian) de chacune des roues de la voiture
 */
	public double[][] getVWorldPositionRoues(){
		
		double[][] tab = new double[4][4];
		for(int i =0;i<4;i++){
		Transform3D test = new Transform3D();
		this.getRoues(i).getRoue().getLocalToVworld(test);
		Vector3f position = new Vector3f();
		test.get(position);
		Matrix3d testmatrice = new Matrix3d();
		test.get(testmatrice);
	
		
		tab[i][0]=position.x;
		tab[i][1]=position.y;
		tab[i][2]=position.z;
				
		if (testmatrice.m00 >= 0 && testmatrice.m10 >=0 ){
			tab[i][3] = Math.asin(testmatrice.m10) * 180 / Math.PI;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 > 0){
			tab[i][3] = (180 - Math.asin(testmatrice.m10) * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 < 0 && testmatrice.m10 < 0 ){
			tab[i][3] = (180 + Math.abs(Math.asin(testmatrice.m10)) * 180 / Math.PI) ;
		}
		else if(testmatrice.m00 > 0 && testmatrice.m10 < 0 ){
			tab[i][3] = (360 - Math.abs(Math.asin(testmatrice.m10) * 180 / Math.PI)) ;
		}
		}
		return tab;
	}
	
	/**
	 * 
	 * @param i : numéro de roues (entre 0 et 3)
	 * @return : objet roues numéro i de l'objet voiture
	 */
	public Roue getRoues(int i) {
		try {
			return roues[i];
		} catch (Exception e) {
			System.out.println("Erreur numérotation roue");
			return null;
		}
	}
	/**
	 * 
	 * @return : Roues[4] contenant les 4 roues de l'objet voiture
	 */
	public Roue[] getRoues() {
		
			return roues;
	}

/**
 * Méthode toString de la méthode Voiture
 */
	public String toString() {
		return "Voiture [x=" + x + ", y=" + y + ", z=" + z + ", length="
				+ length + ", width=" + width + ", vitesse=" + vitesse
				+ ", rouesAG=" + roues[0] + ", rouesAD=" + roues[1]+", rouesArG=" + roues[2]
				+", rouesAG=" + roues[3] + ", directionX =" + direction[0]+ ", directionY =" 
				+ direction[1]+ ", directionZ =" + direction[2];
	}

/**
 * 
 * @return un tableau deux dimension avec pitch & roll de la voiture
 */
public double[] GetRollandPitch(){

	this.getVWorldPositionRoues();
	double pitch=0,roll=0;
	//Récupération des world coordinates des roues
	double[][] tab = new double[4][4];
	tab = this.getVWorldPositionRoues(); 
	//Calcul du pitch et roll de la voiture
	//Pitch : roue 3 : Arrière Gauche / roue 0 :Avant Gauche
	pitch = tab[3][3] - tab[0][3] ;  
	//Roll : roue 0: Avant Gauche / roue 1 :Avant Droite
	roll = tab[0][3] - tab[1][3] ;  
	double[] tab2 = {pitch, roll};
	return tab2;
}
/**
 * 
 * @param world
 * @return Display la voiture
 */
public BranchGroup GetBranchGroup(World world){
	
	BranchGroup BG = new BranchGroup();
	//TG GLOBAL
	TransformGroup transformG = new TransformGroup();
	TransformGroup transform = new TransformGroup();
	//TG DE LA CAISSE
	TransformGroup transformpitch = new TransformGroup();
	TransformGroup transformroll = new TransformGroup();
	
	//TG PERMETTANT LA MISE EN PLACE DES OBJETS 3D
	TransformGroup transformroue0 = new TransformGroup();
	TransformGroup transformroue1 = new TransformGroup();
	TransformGroup transformroue2 = new TransformGroup();
	TransformGroup transformroue3 = new TransformGroup();
    //_____________________________________________________________
	TransformGroup suiviMNTroue0 = new TransformGroup();
	TransformGroup rotationroue0 = new TransformGroup();
	TransformGroup directionroue0 = new TransformGroup();
	
	TransformGroup suiviMNTroue1 = new TransformGroup();
	TransformGroup rotationroue1 = new TransformGroup();
	TransformGroup directionroue1 = new TransformGroup();
	
	TransformGroup suiviMNTroue2 = new TransformGroup();
	TransformGroup rotationroue2 = new TransformGroup();
	
	TransformGroup suiviMNTroue3 = new TransformGroup();
	TransformGroup rotationroue3 = new TransformGroup();
	

	BoundingSphere sphere = new BoundingSphere(new Point3d(),100000); 
	
	transform.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transform.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformG.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformG.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	
	transformpitch.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformpitch.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformroll.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformroll.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	
	directionroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	directionroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	directionroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	directionroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	
	suiviMNTroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	suiviMNTroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	suiviMNTroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	suiviMNTroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	suiviMNTroue2.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	suiviMNTroue2.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	suiviMNTroue3.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	suiviMNTroue3.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	
	rotationroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	rotationroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	rotationroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	rotationroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	rotationroue2.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	rotationroue2.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	rotationroue3.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	rotationroue3.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	
	transformroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformroue0.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformroue1.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformroue2.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformroue2.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	transformroue3.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
	transformroue3.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
	
	
		
	Transform3D t3d = new Transform3D() ;
	t3d.setTranslation(new Vector3d(this.getX(), this.getY(),this.getZ()));
	transformG.setTransform(t3d);
	transformpitch.addChild(transformroll);
	DirectionalLight sun = new DirectionalLight(new Color3f(0.5f,0.5f,0.5f),new Vector3f(1,0,-1));
	sun.setEnable(true);
	sun.setInfluencingBounds(sphere);
	DirectionalLight sun1 = new DirectionalLight(new Color3f(0.5f,0.5f,0.5f),new Vector3f(1,0,-1));
	sun1.setEnable(true);
	sun1.setInfluencingBounds(sphere);
	transformpitch.addChild(sun1);
	transformroll.addChild(this.getCaisse());
	
	//__TEST COLLISION_______________________________________________________
	transformG.setUserData("voiture");
	Transform3D t3ddevant = new Transform3D();
	TransformGroup transformG2 = new TransformGroup();
	TransformGroup transformG3 = new TransformGroup();
	transformG.addChild(transformG2);
	transformG2.addChild(transformG3);
	t3ddevant.setTranslation(new Vector3f(40,0,5));
	ColorCube cc = new ColorCube(10);
	ColorCube cc2 = new ColorCube(10);
	Appearance app = new Appearance();
	TransparencyAttributes tapp = new TransparencyAttributes(TransparencyAttributes.NICEST,0.8f);
	app.setTransparencyAttributes(tapp);
	cc.setAppearance(app);
	cc.setUserData("devant");
	transformG2.setTransform(t3ddevant);
	transformG2.addChild(cc);
	Transform3D t3dderriere = new Transform3D();
	t3dderriere.setTranslation(new Vector3f(-80,0,5));
	transformG3.setTransform(t3dderriere);
	cc2.setAppearance(app);
	cc2.setUserData("arriere");
	transformG.setUserData("tg");
	//__________________________________________________________________________
	this.getCaisse().setUserData("voiture");
	this.getCaisse().setCollidable(false);
	this.getRoues(0).setCollidable(false);
	this.getRoues(1).setCollidable(false);
	this.getRoues(2).setCollidable(false);
	this.getRoues(3).setCollidable(false);
	transformG3.addChild(cc2);
	DetecteurCollision collision = new DetecteurCollision(cc,sphere2);
	DetecteurCollision collision2 = new DetecteurCollision(cc2,sphere2);
	transformG.addChild(collision);
	transformG.addChild(collision2);
	collision.setCollidable(true);
	collision2.setCollidable(true);
	//__________________________________________________________
	
	AvanceVoiture avance1 = new AvanceVoiture(world,this,directionroue0,null,false,true,-10,null,null);
	AvanceVoiture avance2 = new AvanceVoiture(world,this,directionroue1,null,false,true,-10,null,null);
	directionroue0.addChild(avance1);
	directionroue1.addChild(avance2);
	
	avance1.setSchedulingBounds(sphere);
	avance2.setSchedulingBounds(sphere);
	
	AvanceVoiture avance = new AvanceVoiture(world,this,transformG,null,false,false,-10,collision,collision2);
	
    avance.setSchedulingBounds(sphere);
	transformG.addChild(avance);
	transformG.addChild(transformpitch);
	
	Dynamique dynamique = new Dynamique(world,this,transformpitch,transformroll);
	dynamique.setSchedulingBounds(sphere);
	transformpitch.addChild(dynamique);
	
	//--------------Camera Exterieur
	TransformGroup camera = new TransformGroup();
	Transform3D tcam = new Transform3D() ;
	if(this.typevoiture == 1){
	tcam.setTranslation(new Vector3f(-30,0,2));}
	else{tcam.setTranslation(new Vector3f(-50,0,5));}
	
	camera.setTransform(tcam);
	camera.addChild(this.spherecam);
	//------------------Camera Conducteur
	TransformGroup cameraconducteur = new TransformGroup();
	TransformGroup cameraconducteur2 = new TransformGroup();
	Transform3D tcam2 = new Transform3D() ;
	if(this.typevoiture == 1){
	tcam2.setTranslation(new Vector3f(0.5f,1f,1.5f));}
	else{tcam2.setTranslation(new Vector3f(0.6f,1.2f,2f));}

	cameraconducteur.setTransform(tcam2);
	cameraconducteur.addChild(this.spherecamconducteur);
	Transform3D tcam3 = new Transform3D() ;
	cameraconducteur.addChild(cameraconducteur2);
	tcam3.setTranslation(new Vector3f(10.6f,1f,1.5f));
	cameraconducteur2.setTransform(tcam3);
	cameraconducteur2.addChild(this.spherecamconducteur2);
	transformroll.setCollidable(false);
	transformpitch.setCollidable(false);
	camera.setCollidable(false);
	cameraconducteur.setCollidable(false);
	cameraconducteur2.setCollidable(false);
	camera.setUserData("cam");
	cameraconducteur.setUserData("cam2");
	
	//-----------------------------------------
	transformG.addChild(camera);
	transformroll.addChild(cameraconducteur);
	//-----------------------------------------		
	Transform3D trans0 = new Transform3D();
	Transform3D trans1 = new Transform3D();
	Transform3D trans2 = new Transform3D();
	Transform3D trans3 = new Transform3D();
	//_________________________________________________________
	//ROUE 0____________________________________________________________________________
	AvanceVoiture rotroue = new AvanceVoiture(world,this,null,rotationroue0,true,false,0,null,null);
	rotroue.setSchedulingBounds(sphere);
	BranchGroup BG0 = this.getRoues(0).getTGroue(rotroue, directionroue0, this, 0, rotationroue0, transformroue0, suiviMNTroue0, trans0);
	//__________________________________________________________
	//ROUE 1___________________________________________________________________________
	AvanceVoiture rotroue1 = new AvanceVoiture(world,this,null,rotationroue1,true,false,1,null,null);
	rotroue1.setSchedulingBounds(sphere);
	BranchGroup BG1 =  this.getRoues(1).getTGroue(rotroue1, directionroue1, this, 1, rotationroue1, transformroue1, suiviMNTroue1, trans1);
	//__________________________________________________________
	//ROUE 2___________________________________________________________________________
	AvanceVoiture rotroue2 = new AvanceVoiture(world,this,null,rotationroue2,true,false,2,null,null);
	rotroue2.setSchedulingBounds(sphere);
	BranchGroup BG2 =  this.getRoues(2).getTGroue(rotroue2,null, this, 2, rotationroue2, transformroue2, suiviMNTroue2, trans2);
	//__________________________________________________________	
	//ROUE 3___________________________________________________________________________
	AvanceVoiture rotroue3 = new AvanceVoiture(world,this,null,rotationroue3,true,false,3,null,null);
	rotroue3.setSchedulingBounds(sphere);
	BranchGroup BG3 =  this.getRoues(3).getTGroue(rotroue3, null, this, 3, rotationroue3, transformroue3, suiviMNTroue3, trans3);
	//__________________________________________________________	
	
	transformG.addChild(BG0);
	transformG.addChild(BG1);
	transformG.addChild(BG2);
	transformG.addChild(BG3);
	transform.addChild(transformG);
	BG.addChild(transform);
	BG.compile();
	
	return BG;
	}
public int getTypevoiture() {
	return typevoiture;
}
}


