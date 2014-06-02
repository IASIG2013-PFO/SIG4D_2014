package iasig.mobile.elements;

import iasig.mobile.view.World;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Sphere;

public abstract class VehiculeLibre  {
	
	protected float x;
	protected float y;
	protected float z;
	protected float length;
	protected float width;
	protected float heigth;
	protected float vitesse=0;
	protected double angle = 0;
	protected int altitude = 0;
	public double pitch=0;
	public double roll=0;
	protected double drift=0;
	protected float[] direction = new float[3];
	protected Shape3D shape;
	protected Sphere spherecam= new Sphere(0.001f);
	protected Sphere spherecamconducteur= new Sphere(0.001f);
	protected Sphere spherecamconducteur2= new Sphere(0.001f);
	protected BoundingSphere sphere2 = new BoundingSphere(new Point3d(0,0,0),1);
	protected Boolean camera= true;
	
	public abstract double[] getVWorldPosition();
	public abstract double[] getPositionCamExt();
	public abstract double[][] getPositionCamConducteur();
	public abstract BranchGroup GetBranchGroup(World world);
	
	
	public VehiculeLibre(float x,float y,float z,float length,float width,float heigth){
		this.x = x;
		this.y = y;
		this.z = z ;
		this.length = length;
		this.width = width;
		this.heigth = heigth;
		this.direction[0] = 1;this.direction[1] = 0; this.direction[2] = 0;
	}
	/**
	 * Getter de hauteur
	 * @return : hauteur de la voiture
	 */
	public float getHeigth() {
		return heigth;
	}


/**
 * Set une nouvelle hauteur de voiture
 * @param heigth : hauteur de la voiture
 */
	public void setHeigth(float heigth) {
		this.heigth = heigth;
	}

	/**
	 * Getter du Shape3D de la voiture
	 * @return : le shape3D associé à l"objet voiture
	 */
	public Shape3D getCaisse() {
		return shape;
	}
	public void setShape(Shape3D shape) {
		this.shape = shape;
	}
	/**
	 * 
	 * @return : x position de la voiture
	 */
	public float getX() {
		return x;
	}

	/**
	 * Defini une nouvelle position x de la voiture
	 * @param x : x position de la voiture
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * 
	 * @return : y position de la voiture
	 */
	public float getY() {
		return y;
	}

	/**
	 * Defini une nouvelle position y de la voiture
	 * @param y : y position de la voiture
	 */
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * 
	 * @return : z position de la voiture
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Defini une nouvelle position y de la voiture
	 * @param z : z position de la voiture
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Defini une nouvelle position (x,y,z) de la voiture
	 * @param : triple (x,y,z) de float 
	 */
	public void setPosition(float x,float y,float z){
		this.setX(x);
		this.setX(y);
		this.setZ(z);
	}
	
	/**
	 * Getter angle roues avant (en degrés)
	 * @return double angle
	 */
	public double getangle() {
		return angle;
	}

	/**
	 * Setter angle
	 * @param double angle 
	 */
	public void setangle(double angle) {
		this.angle = angle;
	}
	/**
	 * 
	 * @return valeur du booleen caméra (true : vue extèrieure / false : vue intèrieure)
	 */
	public Boolean getCamera() {
		return camera;
	}
	/**
	 * Setter booleen caméra de la classe voiture
	 * @param booleen camera (true : vue extèrieure / false : vue intèrieure)
	 */
	public void setCamera(Boolean camera) {
		this.camera = camera;
	}
	/**
	 * 
	 * @return vitesse
	 */
	public float getVitesse() {
		return vitesse;
	}

	/**
	 * 
	 * @param float vitesse
	 */
	public void setVitesse(float vitesse) {
		this.vitesse = vitesse;
	}


	/**
	 * 
	 * @return length voiture
	 */
	public float getLength() {
		return length;
	}

	/**
	 * 
	 * @param float length voiture
	 */
	public void setLength(float length) {
		this.length = length;
	}
	/**
	 * 
	 * @return width voiture
	 */
	public float getWidth() {
		return width;
	}
	/**
	 * 
	 * @param float width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	public void SetPitch(double pitch) {
		this.pitch = pitch;
	}
	public void SetRoll(double roll) {
		this.roll = roll;
	}
	public void SetDrift(double drift) {
		this.drift = drift;
	}


	public float getDirection(int i) {
		if(i <=2 & i >=0){
			return direction[i];}
			else{
				return -9999;
			}	
	}

/** Setter de direction 
 * 
 * @param direction
 */
	public void setDirection(float[] direction) {
		this.direction = direction;
	}

/**
 * 
 * @param filename : url du .OBJ à loader
 * @return : le shape3D de l'obj
 */

	public Shape3D LoadObj(String filename){
		ObjectFile obj = new ObjectFile();
		Scene scene = null;
		try {
			try {
				URL url = new URL(filename);
				scene = obj.load(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IncorrectFormatException e) {
			e.printStackTrace();
		} catch (ParsingErrorException e) {
			e.printStackTrace();
		}

		BranchGroup group = scene.getSceneGroup();
		Shape3D a = (Shape3D) group.getAllChildren().nextElement();
		Shape3D c = (Shape3D) a.cloneTree();
		c.getAppearance().getMaterial();

		return c;
	}
	public int getAltitude() {
		return altitude;
	}
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}
	
}
