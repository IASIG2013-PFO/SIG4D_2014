package iasig.camera.gestionaffichage;

import iasig.mobile.elements.VehiculeLibre;
import iasig.camera.math.*;
import iasig.univers.view.World;

import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * <b><u>Class Listeners</u> : Permet l'interaction entre l'utilisateur et le
 * programme. Cette classe contient tous les listeners associes a la camera.</b>
 * 
 * @see MouseListener
 * @see MouseWheelListener
 * @see MouseMotionListener
 * @see KeyListener
 * 
 * @author Groupe gestion de l'affichage (Chassin,Collot,Pain)
 * @version 14.06.10 (Annee, mois, jour)
 * 
 */
public class Listeners implements MouseListener, MouseWheelListener,
		MouseMotionListener, KeyListener {

	// ///////////////////////////////////////////////////
	// /////////////////Variables/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Camera associee au dessin.
	 * 
	 * @see Camera
	 */
	private Camera view;

	/**
	 * Point enregistre au moment d'un "released" de la souris. En pixel.
	 * 
	 * @see Point
	 */
	private Point ReleasedPosition;

	/**
	 * Point enregistre au moment d'un "pressed" de la souris. En pixel.
	 * 
	 * @see Point
	 */
	private Point PressedPosition;

	/**
	 * Point courant de la souris pendant un "dragged". En pixel.
	 * 
	 * @see Point
	 */
	private Point DraggedPosition;

	/**
	 * Booleen permettant de definir s'il s'agit oui ou non d'un nouveau clic.
	 * true : nouveau clic. false : pas de nouveau clic.
	 */
	private boolean nouvclick;

	/**
	 * Booleen permettant de changer la vue en freeCam ou en voiture.
	 * True:Freecam False : camera sur la voiture
	 */
	private boolean FreeCar;

	/**
	 * Booleen permettant de changer la vue interieure en vue exterieure pour la
	 * voiture. True : position de la camera au dessus de la voiture False :
	 * camera interieure
	 */
	private boolean InOutCar;

	/**
	 * Univers dans lequel se trouve le dessin et auquel on ajoute les
	 * listeners.
	 * 
	 * @see SimpleUniverse
	 */
	private SimpleUniverse simpleU;

	/**
	 * World dans lequel se trouve le dessin et auquel on ajoute les listeners.
	 * 
	 * @see World
	 */
	private World world;

	/**
	 * Angle entre la droite Camera/point vise et l'axe des z.
	 */
	private double theta;

	/**
	 * Angle entre la droite Camera/point vise et l'axe des x.
	 */
	private double phi;

	/**
	 * TransformGroup auquel est rattachee la camera.
	 * 
	 * @see TransformGroup
	 */
	private TransformGroup mnt;

	/**
	 * Vecteur contenant la liste de tous les vehicules present dans l'univers.
	 */
	private Vector<VehiculeLibre> listevehicule;

	// ///////////////////////////////////////////////////
	// ////////////////Constructeur///////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Constructeur Listeners
	 * <p>
	 * A la construction d'un objet Listeners, on recupere l'univers courant
	 * aisni que le MNT, ce qui permet d'y associer les differents mouvements de
	 * la souris. On ajoute une vue par defaut.
	 * </p>
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant dans lequel sont rattaches les
	 *            listeners.
	 * @param world
	 *            World courant dans lequel sont rattaches les listeners.
	 * @param listevehicule
	 *            Vecteur contenant la liste des vehicules libres contenue dans
	 *            la scène (world).
	 */
	public Listeners(SimpleUniverse simpleU, World world,
			Vector<VehiculeLibre> listevehicule) {
		// Initialisation de la classe listener
		this.listevehicule = listevehicule;
		this.simpleU = simpleU;
		this.world = world;
		this.mnt = world.getTg2();
		view = new Camera(this);
		view.setUp(new Vector3d(Math.cos(Math.toRadians(theta)), Math.sin(Math
				.toRadians(theta)), 0));
	}
	
	// ///////////////////////////////////////////////////
	// ///////////Getteurs et Setteurs////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Recuperation de la camera courante qui est rattachee les listeners
	 * 
	 * @return the view
	 */
	public Camera getView() {
		return view;
	}

	/**
	 * Recuperation du World courant auquel sont rattaches les listeners
	 * 
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Recuperation du booleen permettant de savoir si la camera est en position
	 * exterieure ou FreeCam.
	 * 
	 * @return freeCar
	 */
	public boolean isFreeCar() {
		return FreeCar;
	}

	/**
	 * Mise a jour du booleen FreeCar permettant de placer la camera est en
	 * position exterieure ou FreeCam.
	 * 
	 * @param freeCar
	 */
	public void setFreeCar(boolean freeCar) {
		this.FreeCar = freeCar;
	}

	/**
	 * Recuperation du booleen permettant de passer de la camera exterieure en
	 * camera interieure seulement pour la vue vehicule
	 * 
	 * @return InOutCar
	 */
	public boolean isInOutCar() {
		return InOutCar;
	}

	/**
	 * Mise a jour du booleen InOutCar permettant de placer la camera en
	 * position interieure ou exterieure dans le vehicule courant.
	 * 
	 * @param inOutCar
	 */
	public void setInOutCar(boolean inOutCar) {
		this.InOutCar = inOutCar;
	}

	/**
	 * Mise a jour de l'angle entre la droite Camera/point vise et l'axe des x
	 * (theta)
	 * 
	 * @param theta
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * Mise a jour de l'angle entre la droite Camera/point vise et l'axe des z
	 * (phi)
	 * 
	 * @param phi
	 */
	public void setPhi(double phi) {
		this.phi = phi;
	}

	// ///////////////////////////////////////////////////
	// /////////////Listerners Souris/////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Listener permettant de gerer le zoom de la Camera.
	 * <p>
	 * Listener active a l'activation de la molette de la souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseWheelListener
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {

		// Difference en x,y et z entre le point vise et l'oeil de la camera
		double dx, dy, dz;

		if (arg0.getWheelRotation() < 0) {
			// rotation molette UP
			if (FreeCar) { // cam mnt
				double z = world.GetZMNTPlan(view.getEye().x, view.getEye().y);
				// double z =
				// world.getTuileCourante().getMNT().altitude(view.getEye().x,
				// view.getEye().y);
				// System.out.println(z+" z");
				if (view.mntAlt(view.getEye().z, z)) {
					// Point vise au moment de l'action zoom
					Point3d pt = new Point3d(getPosition(arg0, simpleU, mnt));
					// System.out.println(pt);
					// // angle d'horizon
					int horizon = -68;
					if (phi < horizon) {
						dx = (double) (view.getAt().x - view.getEye().x) / 15;
						dy = (double) (view.getAt().y - view.getEye().y) / 15;
						dz = (double) (view.getAt().z - view.getEye().z) / 15;
						System.out.println(dx + " " + dy + " " + dz);
					} else {
						dx = (double) ((pt.x - view.getEye().x) / 15);
						dy = (double) ((pt.y - view.getEye().y) / 15);
						dz = (double) ((pt.z - view.getEye().z) / 15);
					}
					// Actualisation de la position de la cam�ra
					view.setEye(new Point3d(view.getEye().x + dx,
							view.getEye().y + dy, view.getEye().z + dz));

					// Actualisation du point vis� lors du ZOOM
					view.setAt(new Point3d(view.getAt().x + dx, view.getAt().y
							+ dy, view.getAt().z + dz));

					// MAJ de la view
					view.moveView(simpleU, view.getEye(), view.getAt(),
							view.getUp());

				} else {
//					 this.setFreeCar(false);
//					 VoitureLibre car = new VoitureLibre(
//					 (float) view.getEye().getX(), (float)
//					 view.getEye().getY(), (float) z ,4f,2f,1f,2);
//					 world.AddVehicule(car);
					 
					 
				 	}
			} else if (!InOutCar) {// derri�re voiture
				view.setEye(new Point3d(view.getEye().getX()
						- (view.getEye().getX() - view.getAt().getX()) / 15,
						view.getEye().getY()
								- (view.getEye().getY() - view.getAt().getY())
								/ 15, view.getEye().getZ()
								- (view.getEye().getZ() - view.getAt().getZ())
								/ 15));

				view.moveView(simpleU, view.getEye(), view.getAt(),
						view.getUp());
				view.incrZoom();

			}
		} else if (arg0.getWheelRotation() > 0) {
			// rotation molette UP
			System.out.println("Zoom-");

			if (FreeCar) { // cam mnt
				// Calcul de l'alti
				// double z = world.GetZMNTPlan(view.getEye().x,
				// view.getEye().y);
				// double z =
				// world.getTuileCourante().getMNT().altitude(view.getEye().x,
				// view.getEye().y);
				// if (view.mntAlt(view.getEye().z, z) || !zp) {
				Point3d pt = new Point3d(getPosition(arg0, simpleU, mnt));
				// System.out.println(pt);
				// // angle d'horizon
				int horizon = -68;
				if (phi < horizon) {
					dx = (double) (view.getAt().x - view.getEye().x) / 15;
					dy = (double) (view.getAt().y - view.getEye().y) / 15;
					dz = (double) (view.getAt().z - view.getEye().z) / 15;
					System.out.println(dx + " " + dy + " " + dz);
				} else {
					dx = (double) ((pt.x - view.getEye().x) / 15);
					dy = (double) ((pt.y - view.getEye().y) / 15);
					dz = (double) ((pt.z - view.getEye().z) / 15);
				}
				// Actualisation de la position de la cam�ra
				view.setEye(new Point3d(view.getEye().x - dx, view.getEye().y
						- dy, view.getEye().z - dz));

				// Actualisation du point vis� lors du ZOOM
				view.setAt(new Point3d(view.getAt().x - dx,
						view.getAt().y - dy, view.getAt().z - dz));

				// MAJ de la view
				view.moveView(simpleU, view.getEye(), view.getAt(),
						view.getUp());
			} else if (!InOutCar) {// derri�re voiture
				view.setEye(new Point3d(view.getEye().getX()
						+ (view.getEye().getX() - view.getAt().getX()) / 15,
						view.getEye().getY()
								+ (view.getEye().getY() - view.getAt().getY())
								/ 15, view.getEye().getZ()
								+ (view.getEye().getZ() - view.getAt().getZ())
								/ 15));

				view.moveView(simpleU, view.getEye(), view.getAt(),
						view.getUp());
				view.decrZoom();

			}

		}
	}

	/**
	 * Listener permettant de gerer le deplacement de la Camera ainsi que sa
	 * rotation sur l'axe phi et theta.
	 * <p>
	 * Listener active au deplacement de la souris ainsi qu'un clic.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {

		// aucun deplacement n'a encore ete fait ou il s'agit d'un nouveau
		// clic
		if (ReleasedPosition == null || nouvclick == true) {
			ReleasedPosition = PressedPosition;
		}
		// recuperation du Point courant
		DraggedPosition = arg0.getPoint();

		// Difference de pixels en x et en y entre le point courant et le
		// dernier point connu.
		double dx, dy;

		if (SwingUtilities.isLeftMouseButton(arg0)) {
			// click gauche
			// la souris se trouve dans l'ecran
			if (DraggedPosition.getX() == 0
					|| DraggedPosition.getY() == 0
					|| DraggedPosition.getX() == KeyboardFocusManager
							.getCurrentKeyboardFocusManager().getActiveWindow()
							.getSize().width
					|| DraggedPosition.getY() == KeyboardFocusManager
							.getCurrentKeyboardFocusManager().getActiveWindow()
							.getSize().height) {
				System.out.println("souris en dehors de l'ecran");
			} else {
				// calcul du deplacement global
				dx = (DraggedPosition.getX() - ReleasedPosition.getX());
				dy = (DraggedPosition.getY() - ReleasedPosition.getY());

				if (InOutCar) { // dans la voiture
					if (Math.abs(dx) > Math.abs(dy)) {
						if (dx > 0) {
							view.incrLon();
						} else {
							view.decrLon();
						}
					} else {
						if (dy > 0) {
							view.incrLat();
						} else {
							view.decrLat();
						}
					}
				} else if (FreeCar) { // cam mnt
					int scale = (int) (view.getEye().z / 200 + 1);
					dx *= .15 * scale;
					dy *= .15 * scale;

					Point3d eye = new Point3d();
					Point3d at = new Point3d();

					if (view.mntAlt(view.getEye().z,
							world.GetZMNTPlan(view.getEye().x, view.getEye().y))) {
						// Calcul des nouvelles coordonnees de la camera
						eye = new Point3d(view.getEye().x + dy
								* Math.cos(Math.toRadians(theta)) - dx
								* Math.sin(Math.toRadians(theta)),
								view.getEye().y + dy
										* Math.sin(Math.toRadians(theta)) + dx
										* Math.cos(Math.toRadians(theta)),
								view.getEye().z);

						at = new Point3d(view.getAt().x + dy
								* Math.cos(Math.toRadians(theta)) - dx
								* Math.sin(Math.toRadians(theta)),
								view.getAt().y + dy
										* Math.sin(Math.toRadians(theta)) + dx
										* Math.cos(Math.toRadians(theta)), view
										.getAt().getZ());

					} else {
						// Calcul des nouvelles coordonnees de la camera
						eye = new Point3d(view.getEye().x + dy
								* Math.cos(Math.toRadians(theta)) - dx
								* Math.sin(Math.toRadians(theta)),
								view.getEye().y + dy
										* Math.sin(Math.toRadians(theta)) + dx
										* Math.cos(Math.toRadians(theta)),
								view.getEye().z);

						eye.z = world.GetZMNTPlan(view.getEye().x,
								view.getEye().y) + 50;

						at = new Point3d(view.getAt().x + dy
								* Math.cos(Math.toRadians(theta)) - dx
								* Math.sin(Math.toRadians(theta)),
								view.getAt().y + dy
										* Math.sin(Math.toRadians(theta)) + dx
										* Math.cos(Math.toRadians(theta)), view
										.getAt().getZ());
					}
					// MAJ CAM

					view.setEye(eye);
					view.setAt(at);
					view.moveView(simpleU, view.getEye(), view.getAt(),
							view.getUp());
				}
			}

		} else if (SwingUtilities.isRightMouseButton(arg0)) {
			// click droit
		}

		else if (SwingUtilities.isMiddleMouseButton(arg0)) {
			// click molette
			Point pt_vis = arg0.getPoint();

			// on gere la rotation autour du point clique
			double diffx = pt_vis.getX() - ReleasedPosition.getX();
			double diffy = pt_vis.getY() - ReleasedPosition.getY();

			if (!InOutCar && FreeCar) { // camera free (mnt)

				// MAJ des angles phi et theta
				updateAngles(Math.abs(diffx), Math.abs(diffy));
				Point3d pAt = new Point3d();
				pAt.x = view.getAt().x;
				pAt.y = view.getAt().y;
				pAt.z = world.GetZMNTPlan(pAt.x, pAt.y);
				view.setAt(pAt);
				double dist = Geometrie.distance(view.getAt(), view.getEye());
				if (view.mntAlt(view.getEye().z,
						world.GetZMNTPlan(view.getEye().x, view.getEye().y))) {
					// view.setAt(view.getAt());
					view.setEye(new Point3d(view.getAt().x + dist
							* Math.cos(Math.toRadians(theta))
							* Math.sin(Math.toRadians(phi)), view.getAt().y
							+ dist * Math.sin(Math.toRadians(theta))
							* Math.sin(Math.toRadians(phi)), view.getAt().z
							+ dist * Math.cos(Math.toRadians(phi))));

					view.setUp(new Vector3d(Math.cos(Math.toRadians(theta)),
							Math.sin(Math.toRadians(theta)), 0));
					// view.setUp(new Vector3d(.0,.0,.0));
					view.moveView(simpleU, view.getEye(), view.getAt(),
							view.getUp());
				} else {
					phi = phi + 2;
					view.setEye(new Point3d(view.getAt().x + dist
							* Math.cos(Math.toRadians(theta))
							* Math.sin(Math.toRadians(phi)), view.getAt().y
							+ dist * Math.sin(Math.toRadians(theta))
							* Math.sin(Math.toRadians(phi)), view.getAt().z
							+ dist * Math.cos(Math.toRadians(phi))));

					view.setUp(new Vector3d(Math.cos(Math.toRadians(theta)),
							Math.sin(Math.toRadians(theta)), 0));
					// view.setUp(new Vector3d(.0,.0,.0));
					view.moveView(simpleU, view.getEye(), view.getAt(),
							view.getUp());
				}

			} else if (!InOutCar && !FreeCar) { // camera ext suivant la voiture
				// modification des angles longitude et lattitude présent dans
				// la classe iasig.camera.gestionaffichage.camera.java
				if (Math.abs(diffx) > Math.abs(diffy)) {
					if (diffx > 0) {
						view.incrLon();
					} else {
						view.decrLon();
					}
				} else {
					if (diffy > 0) {
						view.incrLat();
					} else {
						view.decrLat();
					}
				}
			}
		}

		// MAJ de la position du released
		ReleasedPosition = DraggedPosition;
		// RAZ du nouveau clic
		nouvclick = false;
	}

	/**
	 * Listener mouseMoved par defaut.
	 * <p>
	 * Listener active au deplacement de la souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	/**
	 * Listener permettant de d'enregistrer permet de zoomer avec une animation
	 * au moment d'un double clic.
	 * <p>
	 * Listener active au deplacement au clic de la souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 */
	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {
		// Cette methode est appelee quand l'utilisateur a clique (appuye puis
		// relache) sur le composant ecoute
		if (arg0.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(arg0)) {
			// Un double clic a ete effectue
			// recuperation de la position du double clic
			Point3d doubleclic = new Point3d();
			doubleclic = getPosition(arg0, simpleU, mnt);

			// calcul des pas pour l'animation du double clic
			double pasEyex = (view.getEye().getX() - doubleclic.getX()) / 100;
			double pasEyey = (view.getEye().getY() - doubleclic.getY()) / 100;
			double pasEyez = ((view.getEye().getZ() - view.getAt().getZ()) / 2) / 100;

			double pasAtx = (view.getAt().getX() - doubleclic.getX()) / 100;
			double pasAty = (view.getAt().getY() - doubleclic.getY()) / 100;

			double pasphi = (phi / 100);

			// lancement de l'animation
			for (int i = 0; i < 100; i++) {
				view.setEye(new Point3d(view.getEye().getX() - pasEyex, view
						.getEye().getY() - pasEyey, view.getEye().getZ()
						- pasEyez));
				view.setAt(new Point3d(view.getAt().getX() - pasAtx, view
						.getAt().getY() - pasAty, .0));
				phi -= pasphi;
				// MAJ CAM
				view.moveView(simpleU, view.getEye(), view.getAt(),
						view.getUp());

				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					System.out
							.println(e
									+ " : Probleme sur l'animation double clic de la cam (cf. iasig.camera.gestionaffichage.listeners.java)");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Listener mouseEntered par defaut.
	 * <p>
	 * Listener active lorsque que la souris rentre dans la fenetre courante.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/**
	 * Listener mouseExited par defaut.
	 * <p>
	 * Listener active lorsque que la souris sort dans la fenetre active.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Listener permettant de recuperer la position de la souris au moment du
	 * Pressed et d'initialiser un nouveau clic (MAJ d'un booleen)
	 * <p>
	 * Listener active a l'enfoncement du bouton souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 * @see Listeners#PressedPosition
	 * @see Listeners#nouvclick
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// Initialisation du point souris
		PressedPosition = arg0.getPoint();
		// creation d'un nouveau clic
		nouvclick = true;
	}

	/**
	 * Listener permettant de recuperer la derniere position.
	 * <p>
	 * Listener active au relachement du bouton souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associe au listener.
	 * 
	 * @see MouseEvent
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// recuperation de la position souris au moment du released
		ReleasedPosition = arg0.getPoint();
		// lancement de l'animation vue interieure si vue vehicule interieure
		if (InOutCar)
			view.mazAnglesByStep();
	}

	// ///////////////////////////////////////////////////
	// /////////////Listerners Clavier////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Listeners KeyPressed. Permet de mettre a jour deux booleen : InOutCar,
	 * Freecar
	 * <p>
	 * Listener active quand une touche est pressee.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement clavier associe au listener.
	 * 
	 * @see KeyEvent
	 * @see Listeners#FreeCar
	 * @see Listeners#InOutCar
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		case ('a'): {
			// Gestion du la vue en freeCam ou exterieure
			// MAJ d'un Booleen
			FreeCar = !FreeCar;
			view.setZoom(0.);
			InOutCar = false;
			view.mazAngles();
			if (FreeCar) {
				// RAZ des angles phi et theta car mise en place de la Freecam
				phi = -89;
				theta = 45;
				view.IniCamFree(listevehicule.firstElement(), simpleU);
			} else {
				// Mise en place de la camera vehicule exterieure
				view.GestionCamExterieure(listevehicule.firstElement(), simpleU);
			}
			// Have a Break...
			break;
		}
		case ('z'): {
			// Gestion du la vue en interieure ou exterieure de la camera
			// MAJ d'un Booleen
			InOutCar = !InOutCar;
			view.setZoom(0.);
			view.mazAngles();
			if (InOutCar) {
				// Mise en place de la camera vehicule interieure
				view.GestionCamInterieure(listevehicule.firstElement(), simpleU);
			} else {
				// Mise en place de la camera vehicule exterieure
				view.GestionCamExterieure(listevehicule.firstElement(), simpleU);
			}
			// Have a Break...
			break;
		}
		}
	}

	/**
	 * Listener KeyReleased par defaut.
	 * <p>
	 * Listener active lorsque qu'un touche clavier est relachee.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement clavier associe au listener.
	 * 
	 * @see KeyEvent
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Listener keyTyped par defaut.
	 * <p>
	 * Listener active lorsque qu'un touche clavier est tapee
	 * (Pressed+Released).
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement clavier associe au listener.
	 * 
	 * @see KeyEvent
	 * 
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// ///////////////////////////////////////////////////
	// //////////////////Methodes/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Methode de passage des coordonnees pixels au coordonnees 3D dans la
	 * scene.
	 * 
	 * @param event
	 *            MouseEvent regroupant les informations de la position de la
	 *            souris au moment du "pressed".
	 * @param simpleU
	 *            SimpleUniverse courant ou sont rattaches les listeners.
	 * @param mnt
	 *            TransformGroup ayant subi les transformations 3D faite
	 *            precedemment.
	 * 
	 * @return intersection Point3d representant les coordonnees en 3D du point
	 *         visé.
	 */
	public Point3d getPosition(MouseEvent event, SimpleUniverse simpleU,
			TransformGroup mnt) {
		Point3d eyePos = new Point3d();
		Point3d mousePos = new Point3d();
		simpleU.getCanvas().getCenterEyeInImagePlate(eyePos);
		simpleU.getCanvas().getPixelLocationInImagePlate(event.getX(),
				event.getY(), mousePos);
		Transform3D transform = new Transform3D();
		simpleU.getCanvas().getImagePlateToVworld(transform);
		transform.transform(eyePos);
		transform.transform(mousePos);
		Vector3d direction = new Vector3d(eyePos);
		direction.sub(mousePos);
		double Xmean = this.world.getTuileCourante().getMNT().getXmean();
		double Ymean = this.world.getTuileCourante().getMNT().getYmean();
		// three points on the plane
		Point3d p1 = new Point3d(Xmean, Ymean - .5
				* Math.cos(Math.toRadians(phi)), .5 * Math.sin(Math
				.toRadians(phi)));
		Point3d p2 = new Point3d(-Xmean, Ymean + .5
				* Math.cos(Math.toRadians(phi)), .5 * Math.sin(Math
				.toRadians(phi)));
		Point3d p3 = new Point3d(Xmean, Ymean + .5
				* Math.cos(Math.toRadians(phi)), .5 * Math.sin(Math
				.toRadians(phi)));
		Transform3D currentTransform = new Transform3D();
		currentTransform.transform(p1);
		currentTransform.transform(p2);
		currentTransform.transform(p3);
		Point3d intersection = getIntersection(eyePos, mousePos, p1, p2, p3);
		currentTransform.invert();
		currentTransform.transform(intersection);
		return intersection;
	}

	/**
	 * Methode de passage des coordonnees pixels au coordonnees 3D dans la
	 * scene.
	 * 
	 * @param x
	 *            pixels en x de la souris au moment du clic.
	 * @param y
	 *            pixels en y de la souris au moment du clic.
	 * @param simpleU
	 *            SimpleUniverse courant ou sont rattaches les listeners.
	 * @param mnt
	 *            TransformGroup ayant subi les transformations 3D faite
	 *            precedemment.
	 * 
	 * @return intersection Point3d representant les coordonnees en 3D du point
	 *         visé.
	 */
	public Point3d getPosition(int x, int y, SimpleUniverse simpleU,
			TransformGroup mnt) {
		Point3d eyePos = new Point3d();
		Point3d mousePos = new Point3d();
		simpleU.getCanvas().getCenterEyeInImagePlate(eyePos);
		simpleU.getCanvas().getPixelLocationInImagePlate(x, y, mousePos);
		Transform3D transform = new Transform3D();
		simpleU.getCanvas().getImagePlateToVworld(transform);
		transform.transform(eyePos);
		transform.transform(mousePos);
		Vector3d direction = new Vector3d(eyePos);
		direction.sub(mousePos);
		// three points on the plane
		double Xmean = this.world.getTuileCourante().getMNT().getXmean();
		double Ymean = this.world.getTuileCourante().getMNT().getYmean();
		// three points on the plane
		Point3d p1 = new Point3d(Xmean, Ymean - .5
				* Math.cos(Math.toRadians(phi)), .5 * Math.sin(Math
				.toRadians(phi)));
		Point3d p2 = new Point3d(-Xmean, Ymean + .5
				* Math.cos(Math.toRadians(phi)), .5 * Math.sin(Math
				.toRadians(phi)));
		Point3d p3 = new Point3d(Xmean, Ymean + .5
				* Math.cos(Math.toRadians(phi)), .5 * Math.sin(Math
				.toRadians(phi)));
		Transform3D currentTransform = new Transform3D();
		currentTransform.transform(p1);
		currentTransform.transform(p2);
		currentTransform.transform(p3);
		Point3d intersection = getIntersection(eyePos, mousePos, p1, p2, p3);
		currentTransform.invert();
		currentTransform.transform(intersection);
		return intersection;
	}

	/**
	 * 
	 * Methode permettant de calculer les coordonnees du point d'intersection
	 * entre un plan et une droite en 3D. Les trois points representant le plan
	 * ne doivent pas se trouver sur la meme droite.
	 * 
	 * @param line1
	 *            Un Point3d appartenant a la droite
	 * @param line2
	 *            Un Point3d appartenant a la droite
	 * @param plane1
	 *            Point3d appartenant au la plan
	 * @param plane2
	 *            Point3d appartenant au la plan
	 * @param plane3
	 *            Point3d appartenant au la plan
	 * 
	 * @return intersectionPoint Point3d comprennant les coordonnees du point
	 *         d'intersection entre le plan et la droite. En cas de non
	 *         intersection, on retourne null.
	 */
	public Point3d getIntersection(Point3d line1, Point3d line2,
			Point3d plane1, Point3d plane2, Point3d plane3) {
		// definition du plan plane1,plane2,plane3
		Vector3d p1 = new Vector3d(plane1);
		Vector3d p2 = new Vector3d(plane2);
		Vector3d p3 = new Vector3d(plane3);
		Vector3d p2minusp1 = new Vector3d(p2);
		p2minusp1.sub(p1);
		Vector3d p3minusp1 = new Vector3d(p3);
		p3minusp1.sub(p1);
		Vector3d normal = new Vector3d();
		normal.cross(p2minusp1, p3minusp1);
		// Le plan peut etre defini par p1, n + d = 0
		double d = -p1.dot(normal);
		Vector3d i1 = new Vector3d(line1);
		Vector3d direction = new Vector3d(line1);
		direction.sub(line2);
		double dot = direction.dot(normal);
		// on n'a pas trouve d'intersection : @return null
		if (dot == 0)
			return null;
		// calcul des coordonnees de l'intersection
		double t = (-d - i1.dot(normal)) / (dot);
		Vector3d intersection = new Vector3d(line1);
		Vector3d scaledDirection = new Vector3d(direction);
		scaledDirection.scale(t);
		intersection.add(scaledDirection);
		Point3d intersectionPoint = new Point3d(intersection);
		return intersectionPoint;
	}

	/**
	 * Methode permettant de mettre a jour theta et phi.
	 * <p>
	 * Ici, on calcule la difference de pixels sur l'axe des x et des y, si
	 * celle en x est superieure a celle en y alors on modifie l'angle theta,
	 * dans le cas inverse, phi est modifie.
	 * </p>
	 * 
	 * @param diffx
	 *            difference de pixels en x.
	 * @param diffy
	 *            diffeerence de pixels en y.
	 * 
	 * @see Listeners#phi
	 * @see Listeners#theta
	 */
	private void updateAngles(double diffx, double diffy) {
		if (diffx > diffy) {
			if (DraggedPosition.getX() > ReleasedPosition.getX()) {
				if (theta > 0) {
					theta--;
				}
				if (theta == 0) {
					theta = 360;
				}
			} else {
				if (theta < 360) {
					theta++;
				}
				if (theta == 360) {
					theta = 0;
				}
			}
		} else {
			// on gere l'atterissage de la camera sur la terre.
			if (DraggedPosition.getY() > ReleasedPosition.getY()) {
				if (phi > -89) {
					phi--;
				}
			} else {
				if (phi < 0) {
					phi++;
				}
			}
		}
	}
}
