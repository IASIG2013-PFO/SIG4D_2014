package iasig.mobile.elements;

import iasig.mobile.deplacement.AvanceVoiture;
import iasig.mobile.view.Constante;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Primitive;

public class Roue extends Primitive  implements Constante {

	private float omega; //Vitesse de rotation (en rad/s)
	private float x;
	private float y;
	private float z;
	private float type;
	float[]	direction = new float[2];
	private Shape3D roue;
	private int numroue;
	
	public Roue(float x,float y,float z,float dirx, float diry,int numroue,int type){
		this.omega = 0 ;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction[0] = dirx;
		this.direction[1] = diry;
		this.numroue = numroue;
		this.type = type;
		this.roue = this.LoadObj(type);
		
		
	}
	public float getOmega() {
		return omega;
	}

	public void setOmega(float omega) {
		this.omega = omega;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	public void setPositionRoue(float x, float y, float z) {
			this.z = z;
		}

	public float[] getDirection() {
		return direction;
	}

	public void setDirection(float[] direction) {
		this.direction = direction;
	}
	public Vector3f getVWorldPosition(){
		Transform3D test = new Transform3D();
		this.getRoue().getLocalToVworld(test);
		Vector3f position = new Vector3f();
		test.get(position);
		return position;
	}
	@Override
	public String toString() {
		return "Roue [omega=" + omega + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", direction=" + Arrays.toString(direction) + "]";
	}
	
	public Shape3D getRoue() {
		return roue;
	}
	public Shape3D LoadObj(int type){
		ObjectFile obj = new ObjectFile(ObjectFile.RESIZE);
		Scene scene = null;
		URL url = null;
			try {
				if(type == 1){
				if(this.numroue ==0 || this.numroue == 2){ //Roue voiture de police
					url = new URL("file:src/Images/pneudroit.obj");}
					else{url  = new URL("file:src/Images/pneugauche.obj");}}
				else{
					if(this.numroue ==1 || this.numroue == 3){ // Roue 4x4
						url = new URL("file:src/Images/pneugauche2.obj");}
						else{url  = new URL("file:src/Images/pneudroite2.obj");}}
					
								
					try {
						scene = obj.load(url);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IncorrectFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParsingErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		BranchGroup group = scene.getSceneGroup();
		Shape3D b = (Shape3D) group.getAllChildren().nextElement();
		Shape3D c = (Shape3D) b.cloneTree();
		c.getAppearance().getMaterial();
		return c;
	}
 public BranchGroup getTGroue(AvanceVoiture rotroue,TransformGroup directionroue0,VoitureLibre voiture,int numroue,TransformGroup rotationroue0,
		 TransformGroup transformroue0,TransformGroup suiviMNTroue0, Transform3D trans0){
	 	
	 	Vector3f roue0 = new Vector3f();
		if(type == 1){
	 	roue0 = new Vector3f(voiture.getRoues(numroue).getX(),voiture.getRoues(numroue).getY(),(float)-0.8 * Constante.rayonroue);}
		else{roue0 = new Vector3f(voiture.getRoues(numroue).getX(),voiture.getRoues(numroue).getY(),(float)-4);}
		
		trans0.setTranslation(roue0);
		transformroue0.setTransform(trans0);
		transformroue0.addChild(suiviMNTroue0);
		if(numroue == 0 || numroue == 1){ //Pour les roues avant
		
		suiviMNTroue0.addChild(directionroue0);
		directionroue0.addChild(rotationroue0);
			rotationroue0.addChild(rotroue);
		if(type !=1){
		Transform3D scale = new Transform3D();
		scale.setScale(2.75);
		rotationroue0.setTransform(scale);}
		rotationroue0.addChild(voiture.getRoues(numroue).getRoue());}
		
		else{ //Pour les roues arrières
			suiviMNTroue0.addChild(rotationroue0);
			rotationroue0.addChild(rotroue);
			if(type !=1){
				Transform3D scale = new Transform3D();
				scale.setScale(2.75);
				rotationroue0.setTransform(scale);}
			rotationroue0.addChild(voiture.getRoues(numroue).getRoue());	
			
		}
		BranchGroup BG = new BranchGroup();
		BG.addChild(transformroue0);
		return BG;
 }
@Override
public Appearance getAppearance(int arg0) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Shape3D getShape(int arg0) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void setAppearance(Appearance arg0) {
	// TODO Auto-generated method stub
	
}
}
