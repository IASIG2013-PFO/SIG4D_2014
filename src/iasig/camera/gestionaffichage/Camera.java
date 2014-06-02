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
 * <b><u>Class Camera</u> : Gestion de la caméra, initialisation et mise à jour
 * de la position, getters et setters.</b>
 * 
 * @author Groupe gestion de l'affichage (Chassin, Collot, Pain)
 * @version 3 (28/05/2014)
 */

public class Camera {

	// ///////////////////////////////////////////////////
	// /////////////////Variables/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Position de la caméra
	 * 
	 * @see Camera#getEye()
	 * @see Camera#setEye(Point3d)
	 */
	Point3d eye;

	/**
	 * Position du point visé par la caméra
	 * 
	 * @see Camera#getAt()()
	 * @see Camera#setAt(Point3d)
	 */
	Point3d at;

	/**
	 * Vecteur normal de la caméra
	 * 
	 * @see Camera#getUp()
	 * @see Camera#setUp(Point3d)
	 */
	Vector3d up;

	/**
	 * Latitude et longitude entre l'oeil de la camera et l'objet vise
	 * 
	 */
	private double longitude = 0;
	private double latitude = 0;
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
	 * lequel la camera est situé
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
	 * A la construction d'un objet Camera, on positionne par défaut la caméra
	 * au point (0,0,5) de façon à être au centre du MNT. Ce constructeur ne
	 * prend aucun paramètre.
	 * </p>
	 * @throws IOException 
	 */
	Camera(Listeners ltn) throws IOException {
		this.eye = new Point3d(948000.0d,6532000.0d,40000);//948000.0d, 6532000.0d, 5000.0d);
		this.at = new Point3d(948000.0d,6532000.0d,0);//948000.0d, 6532000.0d, 500.0d);
		this.up = new Vector3d(0.0d, 1.0d, 0.0d);
		this.ltn = ltn;
		
		System.out.println(ltn.getWorld().getHeight());
		
		// On initialise les deux quaternions avec leurs valeurs par défaut
		qRightLeft = new Quaternion();
		qRightLeft.setIdentity();

		qUpDown = new Quaternion();
		qUpDown.setIdentity();
		
		
		this.moveView(ltn.getWorld().getUnivers(), getEye(), getAt(),
				getUp());
	}

	// ///////////////////////////////////////////////////
	// ///////////Getteurs et Setteurs////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Retourne la position de la caméra.
	 * 
	 * @return position de l'oeil.
	 */
	public Point3d getEye() {
		return eye;
	}

	/**
	 * Met à jour la position de la caméra.
	 * 
	 * @param eye
	 */
	public void setEye(Point3d eye) {
		this.eye = eye;
	}

	/**
	 * Retourne la position du point visé.
	 * 
	 * @return position du point visé.
	 */
	public Point3d getAt() {
		return at;
	}

	/**
	 * Met à jour la position du point visé de la caméra.
	 * 
	 * @param at
	 */
	public void setAt(Point3d at) {
		this.at = at;
	}

	/**
	 * Retourne le vecteur normal de la caméra.
	 * 
	 * @return le vecteur normal de la caméra.
	 */
	public Vector3d getUp() {
		return up;
	}

	/**
	 * Met à jour le vecteur normal de la caméra.
	 * 
	 * @param up
	 */
	public void setUp(Vector3d up) {
		this.up = up;
	}

	// ///////////////////////////////////////////////////
	// //////////////////Methodes/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Met à jour les différents paramètres de la caméra.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param lookAt
	 *            Transform3D dans lequel est rattaché la caméra.
	 * @param p1
	 *            Point3d représentant l'oeil.
	 * @param p2
	 *            Point3d représentant la position du point visé.
	 * @param v
	 *            Vector3d représentant le vecteur normal.
	 * @throws IOException 
	 */
	public void moveView(SimpleUniverse simpleU, Point3d p1, Point3d p2,
			Vector3d v) throws IOException {
		// Positionnement du LookAt
		Transform3D lookAt = new Transform3D();
		lookAt.setTranslation(new Vector3f(0, 3f, -3f));
		lookAt.lookAt(p1, p2, v);
		lookAt.invert();
		
		ltn.getWorld().setTuileCourante(eye.x, eye.y);
		
		simpleU.getViewingPlatform().getViewPlatformTransform()
				.setTransform(lookAt);
	}

	/**
	 * Methode de gestion de la caméra placé dans un véhicule.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param car
	 *            Vehicule dans lequel la caméra est placée.
	 * @throws IOException 
	 */
	public void GestionCamInterieure(VehiculeLibre car, SimpleUniverse simpleU) throws IOException {

		// Initialisation d'un tableau contenant la position caméra par rapport
		// au vehicule
		double[][] tab3 = car.getPositionCamConducteur();

		Point3d old = new Point3d(tab3[1][0], tab3[1][1], tab3[1][2]);

		qRightLeft.setRotation(
				new Vector3(this.getUp().x, this.getUp().y, this.getUp().z),
				(float) Math.toRadians(longitude));
		qUpDown.setRotation(
				(new Vector3(this.getUp().x, this.getUp().y, this.getUp().z))
						.cross(new Vector3(old.x - this.getEye().x, old.y
								- this.getEye().y, old.z - this.getEye().z)),
				(float) Math.toRadians(latitude));

		// MAJ Caméra
		this.setEye(new Point3d(tab3[0][0], tab3[0][1], tab3[0][2]));
		// this.setAt(new Point3d(tab3[1][0], tab3[1][1], tab3[1][2]));

		this.setAt(Quaternion.translate(qUpDown, qRightLeft, this.getEye(), old));

		this.setUp(new Vector3d(0., 0., 1.));

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());

	}

	/**
	 * Methode de gestion de la caméra placé à l'exterieure d'un véhicule.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param car
	 *            Vehicule auquel la caméra est rattachée.
	 * @throws IOException 
	 */
	public void GestionCamExterieure(VehiculeLibre car, SimpleUniverse simpleU) throws IOException {

		// Initialisation de deux tableau comprenant la position du véhicule
		// dans un monde 3D
		double[] tab2 = null;
		double[] tab = null;

		tab2 = car.getVWorldPosition();
		tab = car.getPositionCamExt();


		// MAJ Caméra

		this.setAt(new Point3d(tab2[0], tab2[1], tab2[2]));

		System.out.println(latitude + "   " + longitude);
		Point3d old = new Point3d(tab[0]+zoom*(tab2[0]-tab[0]), tab[1]+zoom*(tab2[1]-tab[1]), tab[2]);

		qRightLeft.setRotation(
				new Vector3(this.getUp().x, this.getUp().y, this.getUp().z),
				(float) Math.toRadians(longitude));
		qUpDown.setRotation(
				(new Vector3(this.getUp().x, this.getUp().y, this.getUp().z)).cross(new Vector3(
						this.getAt().x - old.x, this.getAt().y - old.y, this
								.getAt().z - old.z)), (float) Math
						.toRadians(latitude));

		this.setEye(Quaternion.translate(qUpDown, qRightLeft, this.getAt(), old));

		// this.setEye(new Point3d(tab[0], tab[1], tab[2] + 10));
		this.setUp(new Vector3d(0., 0., 1.));
		
		

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());
	}

	/**
	 * @return the zoom
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	/**
	 * Methode de gestion de la caméra FreeCam.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param car
	 *            Vehicule auquel la caméra est rattachée.
	 * @throws IOException 
	 */
	public void GestionCamFree(VehiculeLibre car, SimpleUniverse simpleU) throws IOException {

		// Initialisation d'un tableau contenant les position du véhicule
		double[] tab2 = car.getVWorldPosition();

		// MAJ caméra
		this.setAt(new Point3d(tab2[0] + 15, tab2[1], tab2[2]));
		this.setEye(new Point3d(tab2[0], tab2[1] - 15, tab2[2] + 0.3));

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());
	}

	/**
	 * Methode permettant d'incrementer la latitude
	 * 
	 * @see Camera#zoom
	 */
	public void incrZoom() {

		zoom += Geometrie.distance(this.getEye(),this.getAt())/100;
		if (zoom > 0.9){
			ltn.setInOutCar(true);
			this.mazAngles();
			zoom = 0.9;
		}

	}

	/**
	 * Methode permettant de decrementer la latitude
	 * 
	 * @see Camera#zoom
	 */
	public void decrZoom() {

		zoom -= Geometrie.distance(this.getEye(),this.getAt())/100;
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
	 * Methode permettant de gerer la mise a zero des angles longitude/latitude
	 * grace a une animation
	 * 
	 * @see Camera#latitude
	 * @see Camera#longitude
	 */
	public void mazAnglesByStep() {
		float pasLon = (float) (longitude / 100);
		float pasLat = (float) (latitude / 100);
		for (int i = 0; i < 100; i++) {
			longitude -= pasLon;
			latitude -= pasLat;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out
						.println("Problème sur la MAZ de la cam @see mazAngles");
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
		zmnt += 10;
		if (z <= zmnt) {
			return false;
		} else {
			return true;
		}
	}
}
