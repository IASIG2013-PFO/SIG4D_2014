package iasig.mobile.elements;

/**
 * 
 * @author iasig.mobile
 * classe representant un mobile
 */
public abstract class Mobile implements Runnable {
	
	//Attributs d'objets
	protected static int ACCELERATE = 1;
	protected static int SLOWDOWN = -1;
	protected static int STOP = -2;
	protected static int CONSTANT = 0;
	
	//Nom du vehicule
	protected String name;
	//Vitesse actuelle du vehicule
	protected double currentSpeed;
	//Vitesse maximale du vehicule
	protected double maxSpeed;
	//Vitesse à atteindre
	protected double targetSpeed;
	//Arret, Deccelere, Vitesse constante, Freine [-2,-1,0,1]
	protected int situation;
	//Vitesse de rafraichissement
	protected int refreshStep = 2000;
	
	
	//Position du vehicule
	private org.postgis.Point positionMobile = null;

	
	/**
	 * 
	 * @param name nom du mobile
	 * @param maxSpeed vitesse maximal
	 */
	public Mobile (String name, int maxSpeed){
		this.name = name;
		this.currentSpeed = 0;
		this.maxSpeed = maxSpeed;
		this.targetSpeed = 0;
	}
	
	
	/**
	 * 
	 * @param power conditionne l'acceleration du mobile
	 */
	public abstract void move(double time);
	
	/**
	 * 
	 * @return nom du mobile
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name nom du mobile
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return vitesse actuelle
	 */
	public double getCurrentSpeed() {
		return currentSpeed;
	}
	
	/**
	 * 
	 * @param speed vitesse actuelle
	 */
	public void setCurrentSpeed(double speed) {
		this.currentSpeed = speed ;
	}
	
	/**
	 * 
	 * @return vitesse maximale
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * 
	 * @param maxSpeed vitesse maximale
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	/**
	 * 
	 * @return vitesse cible
	 */
	public double getTargetSpeed() {
		return targetSpeed;
	}
	
	/**
	 * 
	 * @param targetSpeed vitesse cible
	 */
	public void setTargetSpeed(double targetSpeed) {
		if(targetSpeed >= this.maxSpeed){
			this.targetSpeed = this.maxSpeed;
		}
		else if(targetSpeed <= 0){
			this.targetSpeed = 0;
		}
		else{
			this.targetSpeed = targetSpeed;	
		}
	}
	
	/**
	 * 
	 * @return action du mobile. accelere ou freine
	 */
	public int getSituation() {
		return situation;
	}
	
	/**
	 * 
	 * @param situation action du mobile. accelere ou freine
	 */
	public void setSituation(int situation) {
		this.situation = situation;
	}


	/**
	 * 
	 * @return position ponctuelle du vehicule
	 */
	public org.postgis.Point getPositionMobile() {
		return positionMobile;
	}

	/**
	 * 
	 * @param positionMobile position ponctuelle du vehicule
	 */
	public void setPositionMobile(org.postgis.Point positionMobile) {
		this.positionMobile = positionMobile;
	}


	@Override
	public abstract void run();
	/**
	 * 
	 * @return information sur le mobile
	 */
	public String getInfo(){
		
		String info = "";
		info += "***MOBILE***" + "\n";
		info += "VITESSE: " + this.currentSpeed + " km/h" + "\n";
		info += "VITESSE CIBLE: " + this.targetSpeed + " km/h" + "\n";
		return info;
		
	}
	


}
