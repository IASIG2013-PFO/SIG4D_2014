package iasig.mobile.deplacement;

import iasig.mobile.elements.VoitureLibre;
import iasig.univers.view.Constante;
import iasig.univers.view.World;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


public class AvanceVoiture extends Deplacement implements Constante{

	private TransformGroup t1 = null; //TG ROUE 
	private Boolean bool = null;
	private Boolean bool1 = null; //Gestion direction de la voiture
	private Boolean bool2 = null; //Gestion direction de la voiture
	private DetecteurCollision collision = null;
	private DetecteurCollision collision2 = null;

	private int numroue;
	double ancienroll=0,ancienpitch=0,zinitial=0;
	double zapres=0,zancien = 0;
	Transform3D roll = new Transform3D();
	Transform3D pitch= new Transform3D();
	Transform3D rotationroue = new Transform3D(); //Gestion rotation roue
	Transform3D rot = new Transform3D(); //Gestion direction

	/**
	 * Constructure de la classe avance
	 * @param World world 
	 * @param Voiture voiture
	 * @param TransformGroup t
	 * @param TransformGroup t1
	 * @param boolean  bool (booleen utile à la gestion ou non de la direction et la rotation des roues)
	 * @param boolean bool1
	 * @param int numroue (numéro de roues de la voiture (une objet avance est nécessaire pour chaque roues)
	 * @param DetecteurCollision collision (avant de la voiture )
	 * @param DetecteurCollision collision2 (arrière de la voiture)
	 */
	public AvanceVoiture(World world, VoitureLibre voiture, TransformGroup t,TransformGroup t1,Boolean bool,Boolean bool1,Boolean bool2, int numroue,DetecteurCollision collision,DetecteurCollision collision2) {
		super(world,voiture,t);
		this.t1 = t1;
		this.bool = bool;
		this.bool1 = bool1;
		this.bool2 = bool2;
		this.numroue = numroue;
		this.collision = collision;
		this.collision2 = collision2;
		zapres = vehicule.getZ();

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
					//_________________________________________________

					if(!this.bool && !this.bool1){
						if(this.fwd && !this.back && /*!this.collision.avant &&*/ this.vehicule.getVitesse() <50){
							this.vehicule.setVitesse((float) (this.vehicule.getVitesse()+1)); //ON ACCELERE
							System.out.println(this.vehicule.getVitesse());
						}else if(this.back && !this.fwd /*&& !this.collision.arriere*/ && this.vehicule.getVitesse() > -5){
							this.vehicule.setVitesse((float) (this.vehicule.getVitesse()-1)); // ON DECCELERE
							System.out.println(this.vehicule.getVitesse());
						}
					}
					//__________________________________________________________________________
					if(this.bool1 && !this.bool){ //Gestion rotation roue
						if(right && !left && !fwd && !back ){

							if(vehicule.getangle()>=-15){
								if(bool2){
									vehicule.setangle(vehicule.getangle()-0.5);
									save.rotZ(-0.5*Math.PI/180);
									this.rot.mul(rot, save);
									this.t.setTransform(rot);
								}			    

								System.out.println("angle roue " + vehicule.getangle());
							}
						}else if (!right && left && !fwd && !back){
							if(vehicule.getangle() <=15){
								if(bool2){
									vehicule.setangle(vehicule.getangle()+0.5);
									save.rotZ(0.5* Math.PI/180);
									this.rot.mul(rot, save);
									this.t.setTransform(rot);
								}

								System.out.println("angle roue " + vehicule.getangle());	
							}
						}

					}
				}			
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

			//_____TOUTES LES 100 MS __________________________________________________________________
			if(this.bool && !this.bool1 && vehicule.getVitesse() > 0){ //Rotation en avant des roues
				this.RotationRoue(1);
			}else if (this.bool && !this.bool1 && vehicule.getVitesse() < 0){ //Rotation en arrière des roues
				this.RotationRoue(-1);
			}

			if(!this.bool && !this.bool1){
				this.t.getTransform(save); //Sauvegarde rotation au moment du behavior
				if((vehicule.getVitesse() > 0 || vehicule.getVitesse() < 0) && vehicule.getangle() == 0){

					save.mul(this.MouvementVertical(true)); //Gestion suivi de MNT
				}else if(vehicule.getVitesse() > 0 && vehicule.getangle() < 0){

					this.Virage(1,1,save);
					save.mul(this.MouvementVertical(false)); //Gestion suivi de MNT

				}else if(vehicule.getVitesse() > 0 && vehicule.getangle() > 0){

					this.Virage(-1,-1,save);
					save.mul(this.MouvementVertical(false)); //Gestion suivi de MNT

				}else if(vehicule.getVitesse() < 0 && vehicule.getangle() < 0){

					this.Virage(-1,1,save);
					save.mul(this.MouvementVertical(false)); //Gestion suivi de MNT

				}else if(vehicule.getVitesse() < 0 && vehicule.getangle() > 0){

					this.Virage(1,-1,save);
					save.mul(this.MouvementVertical(false)); //Gestion suivi de MNT
				}
				this.t.setTransform(save); //Modification du TG de la voiture			
			}

			if(!this.world.getListeners().isFreeCar()){
				if(!this.world.getListeners().isInOutCar()){
					this.world.getListeners().getView().GestionCamExterieure(this.world.getlistevehicule().firstElement(),this.world.getUnivers());

				}else{ this.world.getListeners().getView().GestionCamInterieure(this.world.getlistevehicule().firstElement(),this.world.getUnivers());
				}
			}
			this.wakeupOn(keyCriterion);
		}


	}
	/**
	 * Plaquage de la voiture sur le MNT au cours de son déplacement
	 * @param i : donne le signe de la translation (suivant marche avant ou arrière)
	 * @return : la transformation à faire subir à la voiture
	 */
	private Transform3D MouvementVertical(boolean bool){
		Vector3f newvecteur = new Vector3f();
		Transform3D trans = new Transform3D();	
		double zactuel=0;

				if(vehicule.getVitesse() > 0){
					if(this.collision.avant == true){
						this.vehicule.setVitesse(0);}}
		
				if(vehicule.getVitesse() < 0){
					if(this.collision2.arriere == true ){
						this.vehicule.setVitesse(0);
					}}




		if((float)(this.world.GetZMNTPlan(this.vehicule.getVWorldPosition()[0]+vehicule.getVitesse(),this.vehicule.getVWorldPosition()[1])) < 800){
			if(bool){
				newvecteur.setX((float)(vehicule.getVitesse()));
			}

			if(((VoitureLibre) this.vehicule).getTypevoiture() ==1){
				zactuel = (float)(this.world.GetZMNTPlan(this.vehicule.getVWorldPosition()[0]+vehicule.getVitesse(),this.vehicule.getVWorldPosition()[1])-7.5);
								
			}else if(((VoitureLibre) this.vehicule).getTypevoiture() == 2){
				zactuel = (float)(this.world.GetZMNTPlan(this.vehicule.getVWorldPosition()[0]+vehicule.getVitesse(),this.vehicule.getVWorldPosition()[1])+9 );
				}
			newvecteur.setZ((float)(zactuel - zapres));
			zapres= zactuel;
		}else {
			this.vehicule.setVitesse(0);
			newvecteur.set(0,0,0);
		}
		trans.setTranslation(newvecteur);
		return trans;
	}
	/**
	 * Méthode permettant de faire réaliser des virages à la voiture
	 * @param i  = 1 ou - 1
	 * @param j = 1 ou - 1 (i et j permette de gérer le sens de virage suivant les conditions de la voiture)
	 * @param save : sauvegarde de l'état de la voiture avant le virage
	 */
	private void Virage( int i,int j,Transform3D save){

		Transform3D save2 = new Transform3D();
		Transform3D rot = new Transform3D();
		Transform3D trans = new Transform3D();

		if(((VoitureLibre) this.vehicule).getTypevoiture()== 2){
			j = -j ;
		}
		float[] tab = this.CalculCIR(j); // Calcul position CIR
		trans.setTranslation(new Vector3d(-tab[0], -tab[1], -tab[2]));
		save2.set(trans);
		rot.rotZ(-i * Math.abs(this.vehicule.getVitesse())/3 *Math.cos(this.vehicule.getangle()*deg2rad)*deg2rad);
		save2.mul(rot);
		trans.invert();
		save2.mul(trans);
		save.mul(save2);
	}
	/**
	 * Gère la rotation des roues 
	 * @param i : vaut 1: marche avant / - 1: marche arrière
	 * @return : permet de gérer la rotation des roues et leur "amortissement"
	 */
	private void RotationRoue(int i){

		Transform3D save = new Transform3D();
		Vector3d newvecteur = new Vector3d();

		this.t1.getTransform(save);

		double zactuel = this.world.GetZMNTPlan(((VoitureLibre) this.vehicule).getVWorldPositionRoues()[this.numroue][0],
				((VoitureLibre) this.vehicule).getVWorldPositionRoues()[this.numroue][1]) - Constante.rayonroue/2;
		double z ;
		if((zactuel - zancien) >Constante.rayonroue/1.8){
			z = Constante.rayonroue/1.8;
		}else if((zactuel - zancien) < - Constante.rayonroue/1.8){
			z = -Constante.rayonroue/1.8;
		}else{z= zactuel - zancien;}

		newvecteur.setZ(z);

		zancien= zactuel;
		save.mul(rotationroue);
		rotationroue.rotY(-i * 4*this.vehicule.getVitesse()/rayonroue *  deg2rad);
		save.mul(rotationroue);
		save.setTranslation(newvecteur);
		this.t1.setTransform(save);

	}		
	/**
	 * Permet de calculer le centre instantané de rotation de la voiture
	 * @param i vaut 1 ou -1 suivant sens du virage
	 * @return float[3] : position du CIR de la voiture
	 */
	public float[] CalculCIR(int i){

		double a = this.vehicule.getLength()/Math.tan(this.vehicule.getangle()*deg2rad) * this.vehicule.getLength()/Math.tan(this.vehicule.getangle()*deg2rad) ;

		double b= (((VoitureLibre) this.vehicule).getRoues(2).getX() - ((VoitureLibre) this.vehicule).getRoues(3).getX()) * (((VoitureLibre) this.vehicule).getRoues(2).getX() - ((VoitureLibre) this.vehicule).getRoues(3).getX())
				+ (((VoitureLibre) this.vehicule).getRoues(2).getY() - ((VoitureLibre) this.vehicule).getRoues(3).getY()) * (((VoitureLibre) this.vehicule).getRoues(2).getY() - ((VoitureLibre) this.vehicule).getRoues(3).getY())
				+ (((VoitureLibre) this.vehicule).getRoues(2).getZ() - ((VoitureLibre) this.vehicule).getRoues(3).getZ()) * (((VoitureLibre) this.vehicule).getRoues(2).getZ() - ((VoitureLibre) this.vehicule).getRoues(3).getZ());

		double c = Math.sqrt(a/b);

		float[] tab = new float[3];
		tab[0] = (float) (((VoitureLibre) this.vehicule).getRoues(2).getX() + c * (((VoitureLibre) this.vehicule).getRoues(3).getX() - ((VoitureLibre) this.vehicule).getRoues(2).getX())* i );
		tab[1] = (float) (((VoitureLibre) this.vehicule).getRoues(2).getY() + c * (((VoitureLibre) this.vehicule).getRoues(3).getY() - ((VoitureLibre) this.vehicule).getRoues(2).getY())* i );
		tab[2] = (float) (((VoitureLibre) this.vehicule).getRoues(2).getZ() + c * (((VoitureLibre) this.vehicule).getRoues(3).getZ() - ((VoitureLibre) this.vehicule).getRoues(2).getZ())* i );

		return tab;
	}

	@Override
	public void MouvementVertical(Boolean bool) {
		// Nothing

	}

}
