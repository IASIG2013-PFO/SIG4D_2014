package iasig.mobile.deplacement;

import iasig.mobile.elements.Dirigeable;
import iasig.mobile.view.Constante;
import iasig.mobile.view.World;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;



public class AvanceDirigeable extends Deplacement implements Constante{
	
	/**
	 * Constructure de la classe avance
	 * @param World world 
	 * @param vehicule vehicule
	 * @param TransformGroup t
	 * @param DetecteurCollision collision (dessous du vehicule )
	 * 
	 */
	public AvanceDirigeable(TransformGroup t, Dirigeable dirigeable,World world) {
	super(world,dirigeable,t);
	}


	
	@SuppressWarnings("rawtypes")
	public void processStimulus(Enumeration enumevent) {
		WakeupCriterion critere;
		AWTEvent[] event;

		Transform3D save = new Transform3D();

		//On boucle sur l'énumération
		while(enumevent.hasMoreElements()){
			critere = (WakeupCriterion)enumevent.nextElement() ; //Récupération de l'élément suivant

			try { // Pour récupérer un élément clavier				
				event = ((WakeupOnAWTEvent)critere).getAWTEvent();

				for (AWTEvent anEvent : event) { 

					//________________________________________________
					if (anEvent.getID() == KeyEvent.KEY_PRESSED) {
						keyPressed((KeyEvent) anEvent);
					}if (anEvent.getID() == KeyEvent.KEY_RELEASED) {
						keyReleased((KeyEvent) anEvent);
					}
					//_____________________GESTION VITESSE____________________________

					if(fwd && !back && vehicule.getVitesse() <10){
						vehicule.setVitesse((float) (vehicule.getVitesse()+1)); //ON ACCELERE
					}else if(back && !fwd && vehicule.getVitesse() > -10){
						vehicule.setVitesse((float) (vehicule.getVitesse()-1)); // ON DECCELERE
					}System.out.println(vehicule.getVitesse());
					//______________________GESTION VIRAGE____________________________________________________
					if(right && !left && !fwd && !back && !up && !down){
						vehicule.setangle(vehicule.getangle()-1); 

					}else if (!right && left && !fwd && !back && !up && !down){
						vehicule.setangle(vehicule.getangle()+1); 
					}
				}
				//______________________GESTION ALTITUDE____________________________________________________
				if(up && !down && !right && !left && !fwd && !back){
					this.MouvementVertical(true);											
				}else if (down && !right && !left && !fwd && !back && !up){
					this.MouvementVertical(false);
				}
				//_________________________________________________________________________________________________
//
//				if(vehicule.getCamera()){this.world.GestionCamera(0);}
//				else{this.world.GestionCameraConducteur(0);}
				
				if(!this.world.getListeners().isFreeCar()){
					if(!this.world.getListeners().isInOutCar()){
						this.world.getListeners().getView().GestionCamExterieure(this.world.getlistevehicule().firstElement(),this.world.getUnivers());
								
					}else{ this.world.getListeners().getView().GestionCamInterieure(this.world.getlistevehicule().firstElement(),this.world.getUnivers());
					}
				}
				this.wakeupOn(keyCriterion);

			}catch (Exception e) {
				//nothing
			}
			if(vehicule.getVitesse() > 0 && vehicule.getangle() == 0){

				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				save.mul(this.MouvementHorizontal()); //Gestion suivi de MNT
				this.t.setTransform(save); //Modification du TG de la vehicule		

			}else if(vehicule.getVitesse() < 0 && vehicule.getangle() == 0){
				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				save.mul(this.MouvementHorizontal()); //Gestion suivi de MNT
				this.t.setTransform(save); //Modification du TG de la vehicule	

			}else if(vehicule.getVitesse() > 0 && vehicule.getangle() < 0){

				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				this.Virage(1,save);
				this.t.setTransform(save);

			}else if(vehicule.getVitesse() > 0 && vehicule.getangle() > 0){

				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				this.Virage(-1,save);
				this.t.setTransform(save);

			}else if(vehicule.getVitesse() < 0 && vehicule.getangle() < 0){

				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				this.Virage(-1,save);
				this.t.setTransform(save);	

			}else if(vehicule.getVitesse() < 0 && vehicule.getangle() > 0){

				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				this.Virage(1,save);
				this.t.setTransform(save);
			}
		}
//		if(vehicule.getCamera()){this.world.GestionCamera(0);}
//		else{this.world.GestionCameraConducteur(0);}
		if(!this.world.getListeners().isFreeCar()){
			if(!this.world.getListeners().isInOutCar()){
				this.world.getListeners().getView().GestionCamExterieure(this.world.getlistevehicule().firstElement(),this.world.getUnivers());
						
			}else{ this.world.getListeners().getView().GestionCamInterieure(this.world.getlistevehicule().firstElement(),this.world.getUnivers());
			}
		}
		this.wakeupOn(keyCriterion);

	}
	/**
	 * Plaquage de la vehicule sur le MNT au cours de son déplacement
	 * @param i : donne le signe de la translation (suivant marche avant ou arrière)
	 * @return : la transformation à faire subir à la vehicule
	 */
	private Transform3D MouvementHorizontal(){
		Vector3f newvecteur = new Vector3f();
		Transform3D trans = new Transform3D();	
		
		newvecteur.setX((float)( Deplacement.pas * vehicule.getVitesse()));
		double altitude = vehicule.getZ()-this.world.GetZMNTPlan(vehicule.getVWorldPosition()[0],vehicule.getVWorldPosition()[1]);

		if(altitude < 10){
			vehicule.setZ((float) (vehicule.getZ() + (10-altitude)) );
			newvecteur.setZ((float) (10-altitude));
		}
		trans.setTranslation(newvecteur);
		return trans;
	}

	public void MouvementVertical(Boolean bool) {
		Vector3f newvecteur = new Vector3f();
		Transform3D trans = new Transform3D();	
		Transform3D save = new Transform3D();	
		this.t.getTransform(save);
		if(bool){
			vehicule.setZ(vehicule.getZ() + 1);
			newvecteur.setZ((float)(1));}
		else if(!bool && (vehicule.getZ()-this.world.GetZMNTPlan(vehicule.getVWorldPosition()[0],vehicule.getVWorldPosition()[1]))>10){
			vehicule.setZ(vehicule.getZ() - 1);
			newvecteur.setZ((float)(-1));
		}
		trans.setTranslation(newvecteur);
		save.mul(trans);
		this.t.setTransform(save);
	}

	/**
	 * Méthode permettant de faire réaliser des virages à la vehicule
	 * @param i  = 1 ou - 1
	 * @param j = 1 ou - 1 (i et j permette de gérer le sens de virage suivant les conditions de la vehicule)
	 * @param save : sauvegarde de l'état de la vehicule avant le virage
	 */
	private void Virage( int i,Transform3D save){

		Transform3D save2 = new Transform3D();
		Transform3D rot = new Transform3D();
		Transform3D trans = new Transform3D();

		double coeff = vehicule.getangle(); 
		trans.setTranslation(new Vector3d(0, coeff*100, 0));
		save2.set(trans);
		rot.rotZ(-i * Math.abs(vehicule.getVitesse()/1000) *Math.cos(vehicule.getangle()*Math.PI/180));
		save2.mul(rot);
		trans.invert();
		save2.mul(trans);
		save.mul(save2);
	}

}
