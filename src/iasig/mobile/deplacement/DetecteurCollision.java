package iasig.mobile.deplacement;



import java.util.*;

import javax.media.j3d.*;

/**
 * Comportement personnalise sur les collisions
 */
public class DetecteurCollision extends Behavior {

	// Objet qui subit la collision
	protected Node node;
	public Boolean avant=false; //False = non collision
	public Boolean arriere=false;
	public Boolean dessous=false;
	// Liste des criteres qui declenchent le stimulus pour le comportement
	// DetecteurCollision
	protected WakeupCriterion[] criteres;

	// Sert a declencher le stimulus si un des criteres de la liste ci-dessus
	// est verifie
	protected WakeupOr oredCriteria;

	/**
	 * Constructeur
	 * @param node objet qui va subir la collision
	 * @param appearance apparence de l'objet node
	 * @param bounds zone d'influence du comportement collision
	 */
	public DetecteurCollision(Node node,Bounds bounds) {
		this.node = node;
		this.setSchedulingBounds(bounds);
	}

	/**
	 * Initialise le comportement :
	 * Le stimulus est declenche si un objet entre dans la zone de collision
	 * de this.node, si il bouge dans la zone de collision ou si il sort de
	 * la zone de collision.
	 * 
	 * 
	 * WakeupOnSensorEntry
	 * WakeupOnSensorExit
	 * 
	 */
	public void initialize() {
		criteres = new WakeupCriterion[2];

		// Premier critere : entree dans la zone de collision
		WakeupOnCollisionEntry entreZoneCollision =
				new WakeupOnCollisionEntry(node);

		// Deuxieme critere : sortie de la zone de collision
		WakeupOnCollisionExit sortZoneCollision =
				new WakeupOnCollisionExit(node);
		criteres[0] = entreZoneCollision;
		criteres[1] = sortZoneCollision;

		// Stimulus declenche si l'un des 2 criteres est verifie
		oredCriteria = new WakeupOr(criteres);
		wakeupOn(oredCriteria);
	}

	/**
	 * Le stimulus est traite ici.
	 * @param criteria criteres ayant declenche le stimulus
	 */
	@SuppressWarnings("rawtypes")
	public void processStimulus(Enumeration criteria) {

		// On boucle sur les criteres ayant declenche le comportement
		while (criteria.hasMoreElements()) {

			// On recupere le premier critere de l'enumeration
			WakeupCriterion theCriterion = (WakeupCriterion)criteria.nextElement();

			// Cas ou le critere correspond a l'entree dans la zone de collision
			if (theCriterion instanceof WakeupOnCollisionEntry) {
				if(node.getUserData() == "devant"){
					this.avant = true;
				}else if(node.getUserData() == "arriere"){
					this.arriere = true;
				}else if(node.getUserData() == "dessous"){
					this.dessous = true;
				}
			}  else if (theCriterion instanceof WakeupOnCollisionExit) {
				if(node.getUserData() == "devant"){
					this.avant = false;
				}else if(node.getUserData() == "arriere"){
					this.arriere = false;
				}else if(node.getUserData() == "dessous"){
					this.dessous = false;
				}


			}  // fin while (criteria.hasMoreElements())
			System.out.println("arriere " + this.arriere + " " + "avant " + this.avant);

			// Une fois le stimulus traite, on reinitialise le comportement
			wakeupOn(oredCriteria);
		}
	}}

