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
 * <b><u>Class Camera</u> : Gestion de la cam�ra, initialisation et mise � jour
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
	 * Position de la cam�ra
	 * 
	 * @see Camera#getEye()
	 * @see Camera#setEye(Point3d)
	 */
	Point3d eye;

	/**
	 * Position du point vis� par la cam�ra
	 * 
	 * @see Camera#getAt()()
	 * @see Camera#setAt(Point3d)
	 */
	Point3d at;

	/**
	 * Vecteur normal de la cam�ra
	 * 
	 * @see Camera#getUp()
	 * @see Camera#setUp(Point3d)
	 */
	Vector3d up;

	/**
	 * Latitude et longitude entre l'oeil de la camera et l'objet vise
	 * 
	 */
	public double longitude = 0;
	public double latitude = 0;
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
	 * lequel la camera est situ�
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
	 * A la construction d'un objet Camera, on positionne par d�faut la cam�ra
	 * au point (0,0,5) de fa�on � �tre au centre du MNT. Ce constructeur ne
	 * prend aucun param�tre.
	 * </p>
	 */
	Camera(Listeners ltn) {
		this.eye = new Point3d(948000.0d,6532000.0d,40000);//948000.0d, 6532000.0d, 5000.0d);
		this.at = new Point3d(948000.0d,6532000.0d,0);//948000.0d, 6532000.0d, 500.0d);
		this.up = new Vector3d(1.0d, 0.0d, 0.0d);
		this.ltn = ltn;
		


		// On initialise les deux quaternions avec leurs valeurs par d�faut
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
	 * @return the qRightLeft
	 */
	public Quaternion getqRightLeft() {
		return qRightLeft;
	}

	/**
	 * @return the qUpDown
	 */
	public Quaternion getqUpDown() {
		return qUpDown;
	}

	/**
	 * Retourne la position de la cam�ra.
	 * 
	 * @return position de l'oeil.
	 */
	public Point3d getEye() {
		return eye;
	}

	/**
	 * Met � jour la position de la cam�ra.
	 * 
	 * @param eye
	 */
	public void setEye(Point3d eye) {
		this.eye = eye;
	}

	/**
	 * Retourne la position du point vis�.
	 * 
	 * @return position du point vis�.
	 */
	public Point3d getAt() {
		return at;
	}

	/**
	 * Met � jour la position du point vis� de la cam�ra.
	 * 
	 * @param at
	 */
	public void setAt(Point3d at) {
		this.at = at;
	}

	/**
	 * Retourne le vecteur normal de la cam�ra.
	 * 
	 * @return le vecteur normal de la cam�ra.
	 */
	public Vector3d getUp() {
		return up;
	}

	/**
	 * Met � jour le vecteur normal de la cam�ra.
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
	 * Met � jour les diff�rents param�tres de la cam�ra.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param lookAt
	 *            Transform3D dans lequel est rattach� la cam�ra.
	 * @param p1
	 *            Point3d repr�sentant l'oeil.
	 * @param p2
	 *            Point3d repr�sentant la position du point vis�.
	 * @param v
	 *            Vector3d repr�sentant le vecteur normal.
	 */
	public void moveView(SimpleUniverse simpleU, Point3d p1, Point3d p2,
			Vector3d v) {
		// Positionnement du LookAt
		Transform3D lookAt = new Transform3D();
		lookAt.setTranslation(new Vector3f(0, 3f, -3f));
		lookAt.lookAt(p1, p2, v);
		lookAt.invert();
		
//		try {
//			ltn.getWorld().setTuileCourante(at.x, at.y);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		simpleU.getViewingPlatform().getViewPlatformTransform()
				.setTransform(lookAt);
	}

	/**
	 * Methode de gestion de la cam�ra plac� dans un v�hicule.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param car
	 *            Vehicule dans lequel la cam�ra est plac�e.
	 */
	public void GestionCamInterieure(VehiculeLibre car, SimpleUniverse simpleU) {

		// Initialisation d'un tableau contenant la position cam�ra par rapport
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

		// MAJ Cam�ra
		this.setEye(new Point3d(tab3[0][0], tab3[0][1], tab3[0][2]));
		// this.setAt(new Point3d(tab3[1][0], tab3[1][1], tab3[1][2]));

		this.setAt(Quaternion.translate(qUpDown, qRightLeft, this.getEye(), old));

		this.setUp(new Vector3d(0., 0., 1.));

		this.moveView(simpleU, this.getEye(), this.getAt(), this.getUp());

	}

	/**
	 * Methode de gestion de la cam�ra plac� � l'exterieure d'un v�hicule.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param car
	 *            Vehicule auquel la cam�ra est rattach�e.
	 */
	public void GestionCamExterieure(VehiculeLibre car, SimpleUniverse simpleU) {

		// Initialisation de deux tableau comprenant la position du v�hicule
		// dans un monde 3D
		double[] tab2 = null;
		double[] tab = null;

		tab2 = car.getVWorldPosition();
		tab = car.getPositionCamExt();


		// MAJ Cam�ra

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
	 * Methode de gestion de la cam�ra FreeCam.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param car
	 *            Vehicule auquel la cam�ra est rattach�e.
	 */
	public void GestionCamFree(VehiculeLibre car, SimpleUniverse simpleU) {

		// Initialisation d'un tableau contenant les position du v�hicule
		double[] tab2 = car.getVWorldPosition();

		// MAJ cam�ra
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
						.println("Probl�me sur la MAZ de la cam @see mazAngles");
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
