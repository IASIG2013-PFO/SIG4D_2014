package iasig.camera.gestionaffichage;

import java.io.IOException;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import iasig.camera.math.Geometrie;
import iasig.camera.math.Quaternion;
import iasig.camera.math.Vector3;
import iasig.mobile.elements.VehiculeLibre;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * <b><u>Class Camera</u> : Gestion de la camera, initialisation et mise a jour
 * de la position, getters et setters.</b>
 * 
 * @author Groupe gestion de l'affichage (Chassin, Collot, Pain)
 * @version 14.06.10 (Annee, mois, jour)
 */

public class Camera {

	// ///////////////////////////////////////////////////
	// /////////////////Variables/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Position de la camera
	 * 
	 * @see Camera#getEye()
	 * @see Camera#setEye(Point3d)
	 */
	Point3d eye;

	/**
	 * Position du point vise par la camera
	 * 
	 * @see Camera#getAt()
	 * @see Camera#setAt(Point3d)
	 */
	Point3d at;

	/**
	 * Vecteur normal de la camera
	 * 
	 * @see Camera#getUp()
	 * @see Camera#setUp(Point3d)
	 */
	Vector3d up;

	/**
	 * Longitude entre l'oeil de la camera et l'objet vise
	 * 
	 */
	public double longitude = 0;

	/**
	 * Latitude entre l'oeil de la camera et l'objet vise
	 * 
	 */
	public double latitude = 0;

	/**
	 * Distance entre l'oeil de la camera et l'objet vise
	 * 
	 */
	private double zoom = 0;

	/**
	 * Quaternion gerant le deplacement le long de la longitude (gauche/droite)
	 * 
	 * @see Quaternion
	 * @see Camera#longitude
	 */
	private Quaternion qRightLeft;

	/**
	 * Quaternion gerant le deplacement le long de la latitude (haut/bas)
	 * 
	 * @see Quaternion
	 * @see Camera#latitude
	 */
	private Quaternion qUpDown;

	/**
	 * Pointeur sur la classe d'ecouteurs d'evenement associe au monde dans
	 * lequel la camera est situe
	 * 
	 * @see Listeners
	 */
	private Listeners ltn;

	// ///////////////////////////////////////////////////
	// ////////////////Constructeur///////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Constructeur Camera
	 * <p>
	 * A la construction d'un objet Camera, on positionne par d�faut la camera
	 * au point (0,0,5) de facon a etre au centre du MNT. Ce constructeur prend
	 * en parametre le listenes auquel il est associe.
	 * </p>
	 */
	Camera(Listeners ltn) {

		// Initialisation des coordonnées camera a la vertical au niveau de
		// Annecy
		this.eye = new Point3d(948000.0d, 6532000.0d, 20000);
		this.at = new Point3d(948000.0d, 6532000.0d, 0);
		this.up = new Vector3d(1.0d, 0.0d, 0.0d);
		this.ltn = ltn;

		// initialisation par defaut des booleens et angles spheriques (phi,
		// theta) permettant la gestion des listeners
		ltn.setPhi(-.89);
		ltn.setTheta(0);
		ltn.setFreeCar(true);
		ltn.setInOutCar(false);

		// On initialise les deux quaternions avec leurs valeurs par defaut
		qRightLeft = new Quaternion();
		qRightLeft.setIdentity();

		qUpDown = new Quaternion();
		qUpDown.setIdentity();

		this.moveView(ltn.getWorld().getUnivers(), getEye(), getAt(), getUp());
	}

	// ///////////////////////////////////////////////////
	// ///////////Getteurs et Setteurs////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Retourne la position de la camera.
	 * 
	 * @return eye position de l'oeil.
	 */
	public Point3d getEye() {
		return eye;
	}

	/**
	 * Met a jour la position de la camera.
	 * 
	 * @param eye
	 */
	public void setEye(Point3d eye) {
		this.eye = eye;
	}

	/**
	 * Retourne la position du point vise.
	 * 
	 * @return at position du point vise.
	 */
	public Point3d getAt() {
		return at;
	}

	/**
	 * Met a jour la position du point vise par camera.
	 * 
	 * @param at
	 */
	public void setAt(Point3d at) {
		this.at = at;
	}

	/**
	 * Retourne le vecteur normal de la camera.
	 * 
	 * @return up vecteur normal de la camera.
	 */
	public Vector3d getUp() {
		return up;
	}

	/**
	 * Met a jour le vecteur normal de la camera.
	 * 
	 * @param up
	 */
	public void setUp(Vector3d up) {
		this.up = up;
	}

	/**
	 * Retourne le quaternion longitude (droite/gauche).
	 * 
	 * @return the qRightLeft
	 */
	public Quaternion getqRightLeft() {
		return qRightLeft;
	}

	/**
	 * Retourne le quaternion latitude (haut/bas).
	 * 
	 * @return the qUpDown
	 */
	public Quaternion getqUpDown() {
		return qUpDown;
	}

	/**
	 * Retourne la distance entre entre l'oeil de la camera et l'objet vise
	 * 
	 * @return the zoom
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * Met a jour la distance entre entre l'oeil de la camera et l'objet vise
	 * 
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	// ///////////////////////////////////////////////////
	// //////////////////Methodes/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Met a jour les differents paramatres de la camera.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse dans lequel est rattachee la camera.
	 * @param lookAt
	 *            Transform3D dans lequel est rattachee la camera.
	 * @param p1
	 *            Point3d representant l'oeil (eye).
	 * @param p2
	 *            Point3d representant la position du point vise (at).
	 * @param v
	 *            Vector3d representant le vecteur normal (up).
	 */
	public void moveView(SimpleUniverse simpleU, Point3d p1, Point3d p2,
			Vector3d v) {
		// Positionnement du LookAt
		Transform3D lookAt = new Transform3D();
		lookAt.setTranslation(new Vector3f(0, 3f, -3f));
		lookAt.lookAt(p1, p2, v);
		lookAt.invert();

		try {
			// Mise a jour de la position de la camera au sein du Buffer
			// peut necessiter une requete à la base de donnees
			ltn.getWorld().CameraMovedTo(at.x, at.y);
		} catch (IOException e) {
			System.out
					.println(e
							+ " : Probleme de gestion du Buffer au moment d'un deplacement camera (cf. iasig.camera.gestionaffichage.camera.java)");
			e.printStackTrace();
		}

		simpleU.getViewingPlatform().getViewPlatformTransform()
				.setTransform(lookAt);
	}

	/**
	 * Methode de gestion de la camera interieure placee dans un vehicule.
	 * 
	 * 
	 * @param car
	 *            Vehicule dans lequel la camera est placee.
	 * @param simpleU
	 *            SimpleUniverse courant dans lequel est placee la camera.
	 */
	public void GestionCamInterieure(VehiculeLibre car, SimpleUniverse simpleU) {

		// Initialisation d'un tableau contenant la position camera par rapport
		// au vehicule et mise en place du Point3d correspondant.
		double[][] tab3 = car.getPositionCamConducteur();
		Point3d old = new Point3d(tab3[1][0], tab3[1][1], tab3[1][2]);

		// utilisation des quaternions lors des rotations de la camera
		qRightLeft.setRotation(
				new Vector3(this.getUp().x, this.getUp().y, this.getUp().z),
				(float) Math.toRadians(longitude));
		qUpDown.setRotation(
				(new Vector3(this.getUp().x, this.getUp().y, this.getUp().z))
						.cross(new Vector3(old.x - this.getEye().x, old.y
								- this.getEye().y, old.z - this.getEye().z)),
				(float) Math.toRadians(latitude));

		// MAJ Camera : position, point vise, normal
		this.setEye(new Point3d(tab3[0][0], tab3[0][1], tab3[0][2]));
		this.setAt(Quaternion.translate(qUpDown, qRightLeft, this.getEye(), old));
		this.setUp(new Vector3d(0., 0., 1.));

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());

	}

	/**
	 * Methode de gestion de la camera placee a l'exterieure d'un vehicule.
	 * 
	 * 
	 * @param car
	 *            Vehicule auquel la camera est rattachee.
	 * @param simpleU
	 *            SimpleUniverse courant dans lequel est placee la camera.
	 */
	public void GestionCamExterieure(VehiculeLibre car, SimpleUniverse simpleU) {

		// Initialisation de deux tableau comprenant la position du vehicule
		// dans un monde 3D
		double[] tab2 = null;
		double[] tab = null;

		tab2 = car.getVWorldPosition();
		tab = car.getPositionCamExt();

		// MAJ Camera : point vise, position, normal

		this.setAt(new Point3d(tab2[0], tab2[1], tab2[2]));

		Point3d old = new Point3d(tab[0] + zoom * (tab2[0] - tab[0]), tab[1]
				+ zoom * (tab2[1] - tab[1]), tab[2]);

		// rotation quaternionique
		qRightLeft.setRotation(
				new Vector3(this.getUp().x, this.getUp().y, this.getUp().z),
				(float) Math.toRadians(longitude));
		qUpDown.setRotation(
				(new Vector3(this.getUp().x, this.getUp().y, this.getUp().z)).cross(new Vector3(
						this.getAt().x - old.x, this.getAt().y - old.y, this
								.getAt().z - old.z)), (float) Math
						.toRadians(latitude));

		this.setEye(Quaternion.translate(qUpDown, qRightLeft, this.getAt(), old));
		this.setUp(new Vector3d(0., 0., 1.));

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());
	}

	/**
	 * Initialisation de la camera FreeCam par rapport au dernier vehicule dans
	 * lequel a ete rattache la camera pour creer une animation.
	 * 
	 * 
	 * @param car
	 *            Vehicule auquel la cam�ra est rattachee.
	 * @param simpleU
	 *            SimpleUniverse courant dans lequel est placee la camera.
	 */
	public void IniCamFree(VehiculeLibre car, SimpleUniverse simpleU) {

		// Initialisation d'un tableau contenant les position du vehicule
		double[] tab2 = car.getVWorldPosition();

		// MAJ camera : point vise, position
		this.setAt(new Point3d(tab2[0] + 15, tab2[1], tab2[2]));
		this.setEye(new Point3d(tab2[0], tab2[1] - 15, tab2[2] + 0.3));

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());
	}

	/**
	 * Methode permettant d'incrementer la latitude
	 * 
	 * @see Camera#latitude
	 */
	public void incrLat() {
		if (ltn.isInOutCar() && latitude > 45 || !ltn.isInOutCar()
				&& latitude > 84)
			return; // on limite a l'interieur de la voiture
		this.latitude += .5;
	}

	/**
	 * Methode permettant de decrementer la latitude
	 * 
	 * @see Camera#latitude
	 */
	public void decrLat() {
		if (ltn.isInOutCar() && latitude < -45 || !ltn.isInOutCar()
				&& latitude < 0)
			return; // on limite a l'interieur de la voiture
		this.latitude -= .5;
	}

	/**
	 * Methode permettant d'incrementer la longitude
	 * 
	 * @see Camera#longitude
	 */
	public void incrLon() {
		if (ltn.isInOutCar() && longitude > 90) {
			return; // on limite a l'interieur de la voiture
		}
		this.longitude += .5;
		if (!ltn.isInOutCar() && longitude > 180) {
			longitude = -180;
		}
	}

	/**
	 * Methode permettant de decrementer la longitude
	 * 
	 * @see Camera#longitude
	 */
	public void decrLon() {
		if (ltn.isInOutCar() && longitude < -90) {
			return; // on limite a l'interieur de la voiture
		}
		this.longitude -= .5;
		if (!ltn.isInOutCar() && longitude < -180) {
			longitude = 180;
		}
	}

	/**
	 * Methode permettant d'incrementer la distance entre la position de l'oeil
	 * et le vehicule.
	 * 
	 * @see Camera#zoom
	 */
	public void incrZoom() {
		zoom += Geometrie.distance(this.getEye(), this.getAt()) / 100;
		if (zoom > 0.9) {
			ltn.setInOutCar(true);
			this.mazAngles();
			zoom = 0.9;
		}
	}

	/**
	 * Methode permettant de decrementer la distance entre la position de l'oeil
	 * et le vehicule.
	 * 
	 * @see Camera#zoom
	 */
	public void decrZoom() {
		zoom -= Geometrie.distance(this.getEye(), this.getAt()) / 100;
	}

	/**
	 * Methode permettant de savoir si un point est au-dessus du mnt
	 * 
	 * @param z
	 *            altitude du point
	 * @param zmnt
	 *            altitude du mnt en ce point (x, y)
	 * @return Boolean
	 */
	public boolean mntAlt(double z, double zmnt) {
		// prise en compte de la valeur en Z du mnt plus un delta (50m)
		// permettant de palier aux erreurs d'arrondie dues a une interpolation
		// quadratique du zmnt.
		zmnt += 50;
		if (z <= zmnt) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Methode permettant de gerer la mise a zero des angles longitude/latitude
	 * grace a une animation.
	 * <p>
	 * Il s'agit dans cette methode une fois que la camera a ete bougee en vue
	 * interieure de replacer la camera a une position "normale" afin de simuler
	 * le comportement d'un conducteur bougeant la tete pendnat la conduite.
	 * </p>
	 * 
	 * @see Camera#latitude
	 * @see Camera#longitude
	 */
	public void mazAnglesByStep() {
		// ccreation d'un pas de rotation
		float pasLon = (float) (longitude / 100);
		float pasLat = (float) (latitude / 100);
		// boucle gerant l'animation
		for (int i = 0; i < 100; i++) {
			longitude -= pasLon;
			latitude -= pasLat;
			// utilisation de sleep permettant de ralentir l'execution de la
			// methode afin d'arriver a une animation progressive.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out
						.println(e + " : Probleme sur l'animation de la MAZ de la cam (cf. iasig.camera.gestionaffichage.camera.java)");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Methode permettant de mettre a zero les angles longitude/latitude
	 * 
	 * @see Camera#latitude
	 * @see Camera#longitude
	 */
	public void mazAngles() {
		longitude = 0;
		latitude = 0;
	}
}
