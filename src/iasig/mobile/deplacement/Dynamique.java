package iasig.mobile.deplacement;

import iasig.mobile.elements.VoitureLibre;
import iasig.mobile.view.Constante;
import iasig.mobile.view.World;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;



public class Dynamique extends Behavior implements Constante {


	private WakeupCondition wakeupCondition= new WakeupOnElapsedTime(50); 
	private TransformGroup t = null;  //TG GERANT LE PITCH
	private TransformGroup t1 = null; //TG GERANT LE ROLL
	private VoitureLibre voiture = null;
	private World world;
	Transform3D roll = new Transform3D();
	Transform3D pitch = new Transform3D();
	double ancienroll=0,ancienpitch=0;

	public Dynamique(World world, VoitureLibre voiture, TransformGroup t,TransformGroup t1) {
		this.voiture = voiture;
		this.t = t;
		this.t1 = t1;
		this.world = world;
	}

	public void initialize() {
		this.wakeupOn(wakeupCondition);
	}

	@SuppressWarnings("rawtypes")
	public void processStimulus(Enumeration enumevent) {

		this.PitchAndRoll();		
		
		if(this.voiture.getCamera()){this.world.GestionCamera(0);}
		else{this.world.GestionCameraConducteur(0);}
		
		this.wakeupOn(wakeupCondition);
	}

	/**
	 * @param save
	 * @return Modification des TG pour la gestion du pitch et du roll
	 */
	private void PitchAndRoll(){

		ancienroll = this.voiture.roll;
		ancienpitch = this.voiture.pitch;
		this.world.GestionVoiture(0);
		double coeff;
		if(this.voiture.typevoiture == 1){
			coeff = 5;
		}else{
			coeff = 5;
		}

		if( !Double.isNaN((this.voiture.roll - ancienroll))){
			roll.setIdentity();
			this.t1.setTransform(roll);
			roll.rotX(-this.voiture.roll/coeff);
			this.t1.setTransform(roll);
		}
		if(!Double.isNaN(this.voiture.pitch)){
			pitch.setIdentity();
			this.t.setTransform(pitch);
			pitch.rotY(this.voiture.pitch/coeff);
			this.t.setTransform(pitch);

		}
		
	}
}