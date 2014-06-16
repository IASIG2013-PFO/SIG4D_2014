package iasig.mobile.deplacement;


import iasig.mobile.elements.VehiculeLibre;
import iasig.univers.view.World;

import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.media.j3d.WakeupOr;

import com.sun.j3d.utils.geometry.Primitive;

public abstract class Deplacement extends Behavior {
	
	protected TransformGroup t = null;  //TG DE LA dirigeable ENTIERE
	protected Primitive primitive = null;
	protected World world;
	protected VehiculeLibre vehicule;
	protected WakeupOr keyCriterion;
	protected int leftKey = KeyEvent.VK_LEFT;
	protected int rightKey = KeyEvent.VK_RIGHT;
	protected int forwardKey = KeyEvent.VK_UP;
	protected int backKey = KeyEvent.VK_DOWN;
	protected int upKey = KeyEvent.VK_S;
	protected int downKey = KeyEvent.VK_Q;
	protected boolean fwd = false, back = false,left=false,right=false,up = false,down = false;
	protected double deg2rad = 2 * Math.PI / 360 ;
	protected static float pas = 0.05f;
	
	public Deplacement(World world, VehiculeLibre vehicule, TransformGroup t) {
		this.world = world;
		this.t = t;
		this.vehicule = vehicule;
	}
	public void initialize(){
		WakeupCriterion[] keyEvents = new WakeupCriterion[3];
		keyEvents[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		keyEvents[1] = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
		keyEvents[2] = new WakeupOnElapsedTime(100);

		keyCriterion = new WakeupOr(keyEvents);
		wakeupOn(keyCriterion);
	}
	/**

	/**
	 * Met à jour les booleens suivant les touches pressées
	 * @param KeyEvent event
	 */
	protected void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();

		if (keyCode == forwardKey){
			fwd = true; 	            
		}else if (keyCode == leftKey){
			left = true; 	            
		}else if (keyCode == rightKey){
			right = true; 	            
		}else if (keyCode == backKey){
			back = true; 	            
		}else if (keyCode == upKey){
			up = true; 	            
		}else if (keyCode == downKey){
			down = true; 	            
		}

//		if (keyCode == KeyEvent.VK_V && this.vehicule.getCamera()){
//			this.vehicule.setCamera(false);
//		}else if(keyCode == KeyEvent.VK_V && !this.vehicule.getCamera()) {this.vehicule.setCamera(true);}
	}    
	/**
	 * Met à jour les booleens suivant les touches relâchées
	 * @param KeyEvent event
	 */
	protected void keyReleased(KeyEvent event) {
		int keyCode = event.getKeyCode();

		if (keyCode == forwardKey){
			fwd = false ;           
		}else if(keyCode == leftKey){
			left = false ;
		}else if (keyCode == rightKey){
			right = false; 	            
		}else if (keyCode == backKey){
			back = false; 	            
		}else if (keyCode == upKey){
			up = false; 	            
		}else if (keyCode == downKey){
			down = false; 	            
		}
	}
	
	@SuppressWarnings("rawtypes")
	public abstract void processStimulus(Enumeration arg0);
	public abstract void MouvementVertical(Boolean bool);
}
