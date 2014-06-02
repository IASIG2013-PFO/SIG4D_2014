package iasig.camera.gestionaffichage;

import iasig.mobile.elements.VehiculeLibre;
import iasig.camera.math.*;
import iasig.mobile.view.*;

import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.Vector;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * <b><u>Class Listeners</u> :Permet l'interaction entre l'utilisateur et le
 * programme. Cette classe contient tous les listeners associés à la caméra.</b>
 * 
 * @see MouseListener
 * @see MouseWheelListener
 * @see MouseMotionListener
 * 
 * @author Groupe gestion de l'affichage (Chassin,Collot,Pain)
 * @version 2
 * 
 */
public class Listeners implements MouseListener, MouseWheelListener,
		MouseMotionListener, KeyListener {

	// ///////////////////////////////////////////////////
	// /////////////////Variables/////////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Camera associée au dessin.
	 * 
	 * @see Camera
	 */
	private Camera view;

	/**
	 * Point enregistré au moment d'un "released" de la souris. En pixel.
	 * 
	 * @see Point
	 */
	private Point LastPosition;

	/**
	 * Point enregistré au moment d'un "pressed" de la souris. En pixel.
	 * 
	 * @see Point
	 */
	private Point FirstPosition;

	/**
	 * Point courant de la souris pendant un "dragged". En pixel.
	 * 
	 * @see Point
	 */
	Point pt = new Point();

	/**
	 * Booleen permettant de définir s'il s'agit oui ou non d'un nouveau clic.
	 * 
	 */
	private boolean nouvclick;

	/**
	 * Booleen permettant de changer la vue en freeCam ou en voiture. true :
	 * Freecam False : camera sur la voiture
	 * 
	 */
	private boolean FreeCar = true;

	/**
	 * Booleen permettant de changer la vue interieur et exterieur pour la
	 * voiture. true : position de la camera au dessus de la voiture False :
	 * camera interieure
	 * 
	 */
	private boolean InOutCar = false;

	/**
	 * Différence en pixel (en x et y) entre le point courant et le dernier
	 * point connu.
	 * 
	 * @see pt
	 * @see LastPosition
	 */
	private double dx, dy, dz;

	/**
	 * Univers dans lequel se trouve le dessin.
	 * 
	 * @see SimpleUniverse
	 */
	private SimpleUniverse simpleU;
	private World world;
	/**
	 * Latitude et longitude de la Camera par rapport au point visé.
	 * 
	 */
	private double theta = 0;
	private double phi = 0;

	/**
	 * TransformGroup auquel est rattachée la caméra.
	 * 
	 * @see TransformGroup
	 */
	private TransformGroup mnt;

	/**
	 * Vecteur contenant la liste de tous les vehicule présent dans l'univers.
	 * 
	 */
	private Vector<VehiculeLibre> listevehicule;

	// ENCOURSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	// SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	private boolean zp = true;
	private boolean zm = true;

	// ///////////////////////////////////////////////////
	// ////////////////Constructeur///////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Constructeur Listeners
	 * <p>
	 * A la construction d'un objet Listeners, on recupère l'univers courant
	 * aisni que le MNT, ce qui permet d'y associer les différents mouvements de
	 * la souris. On ajoute une vue par défaut.
	 * </p>
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @throws IOException 
	 * @see Listeners#simpleU
	 * @see Listeners#mnt
	 */
	public Listeners(SimpleUniverse simpleU, World world,
			Vector<VehiculeLibre> listevehicule) throws IOException {
		this.listevehicule = listevehicule;
		this.simpleU = simpleU;
		this.world = world;
		System.out.println("ltn construit "+world.getHeight());
		view = new Camera(this);

	}

	// ///////////////////////////////////////////////////
	// ///////////Getteurs et Setteurs////////////////////
	// ///////////////////////////////////////////////////
	
	public World getWorld(){
		return world;
	}

	/**
	 * Booleen permettant de passer de caméra exterieure en FreeCam et
	 * inversement
	 * 
	 * @return the freeCar
	 * 
	 */
	public boolean isFreeCar() {
		return FreeCar;
	}

	/**
	 * Booleen permettant de passer de caméra exterieure en caméra intérieure
	 * 
	 * @return the InOutCar
	 * 
	 */
	public boolean isInOutCar() {
		return InOutCar;
	}

	/**
	 * Getteur permettant de récupérer le lookAt gerant la caméra
	 * 
	 * @return the view
	 */
	public Camera getView() {
		return view;
	}

	// ///////////////////////////////////////////////////
	// /////////////Listerners Souris/////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Listener permettant de gèrer le zoom de la Camera.
	 * <p>
	 * Listener activé à l'activation de la molette de la souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseWheelListener
	 * 
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {

		if (arg0.getWheelRotation() < 0) {
			// rotation molette UP
			System.out.println("Zoom+");
//			if (FreeCar) { // cam mnt
				//double z = world.GetZMNTPlan(view.getEye().x, view.getEye().y);
				double z = world.getTuileCourante().getMNT().altitude(view.getEye().x, view.getEye().y);
//				System.out.println(z+" z");
//				if (view.mntAlt(view.getEye().z, z) || !zm) {
//					zp = true;
					// Point visé au moment de l'action zoom
//					Point3d pt = new Point3d(getPosition(arg0, simpleU, mnt));
//					System.out.println(pt);
//					// angle d'horizon
//					int horizon = -68;
//					if (phi < horizon) {
						dx = (double) (view.getAt().x - view.getEye().x)/15;
						dy = (double) (view.getAt().y - view.getEye().y)/15;
						dz = (double) (view.getAt().z - view.getEye().z)/15;
						System.out.println(dx + " "+dy+" "+dz);
//					} else {
//						dx = (double) ((pt.x - view.getEye().x) / 15);
//						dy = (double) ((pt.y - view.getEye().y) / 15);
//						dz = (double) ((pt.z - view.getEye().z) / 15);
//					}
					// Actualisation de la position de la caméra
					view.setEye(new Point3d(view.getEye().x + dx,
							view.getEye().y + dy, view.getEye().z + dz));

					// Actualisation du point visé lors du ZOOM
//					view.setAt(new Point3d(view.getAt().x + dx, view.getAt().y
//							+ dy, view.getAt().z + dz));

					// MAJ de la view
					try {
						view.moveView(simpleU, view.getEye(), view.getAt(),
								view.getUp());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//				} else {
//					zp = false;
//				}
//			} else if (!InOutCar) {// derrière voiture
////				view.setEye(new Point3d(view.getEye().getX()
////						- (view.getEye().getX() - view.getAt().getX()) / 15,
////						view.getEye().getY()
////								- (view.getEye().getY() - view.getAt().getY())
////								/ 15, view.getEye().getZ()
////								- (view.getEye().getZ() - view.getAt().getZ())
////								/ 15));
////
////				view.moveView(simpleU, view.getEye(), view.getAt(),
////						view.getUp());
//				view.incrZoom();

//			}
		} else if (arg0.getWheelRotation() > 0) {
			// rotation molette UP
			System.out.println("Zoom-");

//			if (FreeCar) { // cam mnt
				// Calcul de l'alti
				//double z = world.GetZMNTPlan(view.getEye().x, view.getEye().y);
				double z = world.getTuileCourante().getMNT().altitude(view.getEye().x, view.getEye().y);
//				if (view.mntAlt(view.getEye().z, z) || !zp) {
//					zm = true;
//					Point3d pt = new Point3d(getPosition(arg0, simpleU, mnt));
//					System.out.println(pt);
//					// angle d'horizon
//					int horizon = -68;
//					if (phi < horizon) {
						dx = (double) (view.getAt().x - view.getEye().x)/15;
						dy = (double) (view.getAt().y - view.getEye().y)/15;
						dz = (double) (view.getAt().z - view.getEye().z)/15;
						System.out.println(dx + " "+dy+" "+dz);
//					} else {
//						dx = (double) ((pt.x - view.getEye().x) / 15);
//						dy = (double) ((pt.y - view.getEye().y) / 15);
//						dz = (double) ((pt.z - view.getEye().z) / 15);
//					}
					// Actualisation de la position de la caméra
					view.setEye(new Point3d(view.getEye().x - dx,
							view.getEye().y - dy, view.getEye().z - dz));

					// Actualisation du point visé lors du ZOOM
//					view.setAt(new Point3d(view.getAt().x - dx, view.getAt().y
//							- dy, view.getAt().z - dz));

					// MAJ de la view
					try {
						view.moveView(simpleU, view.getEye(), view.getAt(),
								view.getUp());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//				} else {
//					zm = false;
//				}
//			}else if (!InOutCar) {// derrière voiture
//				view.setEye(new Point3d(view.getEye().getX()
//						+ (view.getEye().getX() - view.getAt().getX()) / 15,
//						view.getEye().getY()
//								+ (view.getEye().getY() - view.getAt().getY())
//								/ 15, view.getEye().getZ()
//								+ (view.getEye().getZ() - view.getAt().getZ())
//								/ 15));
//
//				view.moveView(simpleU, view.getEye(), view.getAt(),
//						view.getUp());
//				view.decrZoom();
//
//			}
			
		}
	}

	/**
	 * @param inOutCar the inOutCar to set
	 */
	public void setInOutCar(boolean inOutCar) {
		InOutCar = inOutCar;
	}

	/**
	 * Listener permettant de gèrer le déplacement de la Camera ainsi que sa
	 * rotation sur l'axe phi et theta.
	 * <p>
	 * Listener activé au déplacement de la souris ainsi qu'un clic.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mouseDragged(java.awt.event.MouseEvent arg0) {

		// aucun déplacement n'a encore été fait ou il s'agit d'un nouveau
		// click
		if (LastPosition == null | nouvclick == true) {
			LastPosition = FirstPosition;
		}

		// récupération du Point courant
		pt = arg0.getPoint();

		if (SwingUtilities.isLeftMouseButton(arg0)) {
			// click gauche
			System.out.println("click gauche");

			// la souris se trouve dans l'écran
			if (pt.getX() == 0
					| pt.getY() == 0
					| pt.getX() == KeyboardFocusManager
							.getCurrentKeyboardFocusManager().getActiveWindow()
							.getSize().width
					| pt.getY() == KeyboardFocusManager
							.getCurrentKeyboardFocusManager().getActiveWindow()
							.getSize().height) {
				System.out.println("souris en dehors de l'écran");
			} else {
				// calcul du déplacement global
				dx = (pt.getX() - LastPosition.getX());
				dy = (pt.getY() - LastPosition.getY());

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
					System.out.println(scale);
					dx *= .15 * scale;
					dy *= .15 * scale;

					// Calcul des nouvelles coordonnées de la caméra
					Point3d eye = new Point3d(view.getEye().x + dy
							* Math.cos(Math.toRadians(theta)) - dx
							* Math.sin(Math.toRadians(theta)), view.getEye().y
							+ dy * Math.sin(Math.toRadians(theta)) + dx
							* Math.cos(Math.toRadians(theta)), view.getEye().z);

					Point3d at = new Point3d(view.getAt().x + dy
							* Math.cos(Math.toRadians(theta)) - dx
							* Math.sin(Math.toRadians(theta)), view.getAt().y
							+ dy * Math.sin(Math.toRadians(theta)) + dx
							* Math.cos(Math.toRadians(theta)), view.getAt()
							.getZ());

					// MAJ CAM
					view.setEye(eye);
					view.setAt(at);
					try {
						view.moveView(simpleU, view.getEye(), view.getAt(),
								view.getUp());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println(pt.getX() + " " + pt.getY() + " ");
				}

			}
		} else if (SwingUtilities.isRightMouseButton(arg0)) {
			// click droit
			System.out.println("click droit");

		}

		else if (SwingUtilities.isMiddleMouseButton(arg0)) {
			// click molette
			System.out.println("click roulette");

			// on gere la rotation autour du point clique
			double diffx = pt.getX() - LastPosition.getX();
			double diffy = pt.getY() - LastPosition.getY();

			// Calcul des nouvelles coordonnées de la caméra

			if (!InOutCar && FreeCar) { // camera free (mnt)

				updateAngles(Math.abs(diffx), Math.abs(diffy));
				System.out.println("molette freeeeee");

				double dist = Geometrie.distance(view.getAt(), view.getEye());
				view.setEye(new Point3d(view.getAt().x + dist
						* Math.cos(Math.toRadians(theta))
						* Math.sin(Math.toRadians(phi)), view.getAt().y + dist
						* Math.sin(Math.toRadians(theta))
						* Math.sin(Math.toRadians(phi)), view.getAt().z + dist
						* Math.cos(Math.toRadians(phi))));

				view.setUp(new Vector3d(Math.cos(Math.toRadians(theta)), Math
						.sin(Math.toRadians(theta)), 0));

				try {
					view.moveView(simpleU, view.getEye(), view.getAt(),
							view.getUp());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (!InOutCar && !FreeCar) { // camera ext suivant la voiture
				System.out.println("molette derriere");
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

		LastPosition = pt;
		nouvclick = false;
	}

	/**
	 * Listener permettant de gèrer le déplacement de la Camera.
	 * <p>
	 * Listener activé au déplacement de la souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mouseMoved(java.awt.event.MouseEvent arg0) {
	}

	/**
	 * Listener permettant de d'enregistrer les positions courantes et de
	 * zoomer.
	 * <p>
	 * Listener activé au déplacement au clic de la souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {
		// Cette méthode est appelée quand l'utilisateur a cliqué (appuyé puis
		// relâché) sur le composant écouté
		if (arg0.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(arg0)) {
			System.out.println("Il y a eu un double clic");

			Point3d aa = new Point3d();

			aa = getPosition(arg0, simpleU, mnt);

			view.setEye(new Point3d(aa.getX(), aa.getY(), view.getEye().z / 5));
			view.setAt(new Point3d(aa.getX(), aa.getY(), .0));
			phi = 0;

			// MAJ CAM
			try {
				view.moveView(simpleU, view.getEye(), view.getAt(), view.getUp());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Listener par défaut.
	 * <p>
	 * Listener activé lorsque que la souris rentre dans la fenêtre courante.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {

	}

	/**
	 * Listener par défaut.
	 * <p>
	 * Listener activé lorsque que la souris sort dans la fenêtre courante.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {

	}

	/**
	 * Listener permettant de récuperer la première position et le double clic
	 * zoom.
	 * <p>
	 * Listener activé à l'enfoncement du bouton souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mousePressed(java.awt.event.MouseEvent arg0) {
		FirstPosition = arg0.getPoint();
		nouvclick = true;

	}

	/**
	 * Listener permettant de récuperer la dernière position.
	 * <p>
	 * Listener activé au relachement du bouton souris.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement souris associé au listener.
	 * 
	 * @see MouseEvent
	 * 
	 */
	@Override
	public void mouseReleased(java.awt.event.MouseEvent arg0) {
		LastPosition = arg0.getPoint();

		if (InOutCar)
			view.mazAnglesByStep();

	}

	// ///////////////////////////////////////////////////
	// /////////////Listerners Clavier////////////////////
	// ///////////////////////////////////////////////////

	/**
	 * Listeners KeyPressed.
	 * <p>
	 * Listener activé quand une touche est pressée.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement clavier associé au listener.
	 * 
	 * @see KeyEvent
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {

		// Gestion du la vue en freeCam ou extérieure
		// MAJ d'un Booleen
		case ('a'): {

			FreeCar = !FreeCar;
			view.setZoom(0.);
			InOutCar = false;
			view.mazAngles();
			if (FreeCar) {
				phi = -89;
				theta = 45;
				try {
					view.GestionCamFree(listevehicule.firstElement(), simpleU);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					view.GestionCamExterieure(listevehicule.firstElement(), simpleU);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Have a Break...
			break;
		}

		// Gestion du la vue en interieure ou extérieure de la caméra
		// (MAJ d'un Booleen)
		case ('z'): {
			
			InOutCar = !InOutCar;
			view.setZoom(0.);
			view.mazAngles();
			if (InOutCar) {
				try {
					view.GestionCamInterieure(listevehicule.firstElement(), simpleU);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					view.GestionCamExterieure(listevehicule.firstElement(), simpleU);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Have a Break...
			break;
		}
		}

	}

	/**
	 * Listener par défaut.
	 * <p>
	 * Listener activé lorsque qu'un touche clavier est relachée.
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement clavier associé au listener.
	 * 
	 * @see KeyEvent
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * Listener par défaut.
	 * <p>
	 * Listener activé lorsque qu'un touche clavier est tapée
	 * (Pressed+Released).
	 * </p>
	 * 
	 * @param arg0
	 *            Evenement clavier associé au listener.
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
	 * Méthode de passage des coordonnées pixels au coordonnées 3D dans la
	 * scene.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param MouseEvent
	 *            Evenements souris pour récuperer les pixels x et y.
	 * @param mnt
	 *            TransformGroup ayant subi les transformations 3D faite
	 *            précédemment.
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
		// three points on the plane
		Point3d p1 = new Point3d(.5, -.5 * Math.cos(Math.toRadians(phi)),
				.5 * Math.sin(Math.toRadians(phi)));
		Point3d p2 = new Point3d(.5, .5 * Math.cos(Math.toRadians(phi)),
				.5 * Math.sin(Math.toRadians(phi)));
		Point3d p3 = new Point3d(-.5, .5 * Math.cos(Math.toRadians(phi)),
				.5 * Math.sin(Math.toRadians(phi)));
		Transform3D currentTransform = new Transform3D();
		mnt.getLocalToVworld(currentTransform);
		currentTransform.transform(p1);
		currentTransform.transform(p2);
		currentTransform.transform(p3);
		Point3d intersection = getIntersection(eyePos, mousePos, p1, p2, p3);
		currentTransform.invert();
		currentTransform.transform(intersection);
		return intersection;
	}

	/**
	 * Méthode de passage des coordonnées pixels au coordonnées 3D dans la
	 * scene.
	 * 
	 * 
	 * @param simpleU
	 *            SimpleUniverse courant.
	 * @param x
	 *            pixel en x de la souris au moment du clic.
	 * 
	 * @param y
	 *            pixel en y de la souris au moment du clic.
	 * 
	 * @param mnt
	 *            TransformGroup ayant subi les transformations 3D faite
	 *            précédemment.
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
		Point3d p1 = new Point3d(.5, -.5 * Math.cos(Math.toRadians(phi)),
				.5 * Math.sin(Math.toRadians(phi)));
		Point3d p2 = new Point3d(.5, .5 * Math.cos(Math.toRadians(phi)),
				.5 * Math.sin(Math.toRadians(phi)));
		Point3d p3 = new Point3d(-.5, .5 * Math.cos(Math.toRadians(phi)),
				.5 * Math.sin(Math.toRadians(phi)));
		Transform3D currentTransform = new Transform3D();
		mnt.getLocalToVworld(currentTransform);
		currentTransform.transform(p1);
		currentTransform.transform(p2);
		currentTransform.transform(p3);
		Point3d intersection = getIntersection(eyePos, mousePos, p1, p2, p3);
		currentTransform.invert();
		currentTransform.transform(intersection);
		return intersection;
	}

	/**
	 * Returns the point where a line crosses a plane
	 */
	public Point3d getIntersection(Point3d line1, Point3d line2,
			Point3d plane1, Point3d plane2, Point3d plane3) {
		Vector3d p1 = new Vector3d(plane1);
		Vector3d p2 = new Vector3d(plane2);
		Vector3d p3 = new Vector3d(plane3);
		Vector3d p2minusp1 = new Vector3d(p2);
		p2minusp1.sub(p1);
		Vector3d p3minusp1 = new Vector3d(p3);
		p3minusp1.sub(p1);
		Vector3d normal = new Vector3d();
		normal.cross(p2minusp1, p3minusp1);
		// The plane can be defined by p1, n + d = 0
		double d = -p1.dot(normal);
		Vector3d i1 = new Vector3d(line1);
		Vector3d direction = new Vector3d(line1);
		direction.sub(line2);
		double dot = direction.dot(normal);
		if (dot == 0)
			return null;
		double t = (-d - i1.dot(normal)) / (dot);
		Vector3d intersection = new Vector3d(line1);
		Vector3d scaledDirection = new Vector3d(direction);
		scaledDirection.scale(t);
		intersection.add(scaledDirection);
		Point3d intersectionPoint = new Point3d(intersection);
		return intersectionPoint;
	}

	/**
	 * Méthode permettant de mettre à jour qLon2 et qLat2.
	 * 
	 * 
	 * @param diffx
	 *            différence de pixels en x.
	 * @param diffy
	 *            différence de pixels en y.
	 * 
	 */
	private void updateAngles(double diffx, double diffy) {
		if (diffx > diffy) {
			if (pt.getX() > LastPosition.getX()) {
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
			// on gere l'atterissage de la camera sur la terre (en gros)
			if (pt.getY() > LastPosition.getY()) {
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

//	/**
//	 * Méthode permettant de récupérer les coordonnées des limites de la
//	 * fenetre. Cette méthode utilise le centre de la fenetre et les coordonnées
//	 * pixels de la souris pour en calculer les coordonnées limite de la fenetre
//	 * passant par la droite construite par les deux points précédents
//	 * 
//	 * 
//	 * @param diffx
//	 *            différence de pixels en x.
//	 * @param diffy
//	 *            différence de pixels en y.
//	 * 
//	 */
//	private void recupExt(Point2d Ext, MouseEvent arg0, int height, int width) {
//		// calcul des droite des diagonales de l'écran en pixels.
//		// point en bas à gauche au point en haut à droite
//		// demi diagonale
//		double Ademi = (double) -height / width;
//		double Bdemi = height;
//
//		// point en haut à gauche au point en bas à droite
//		// min-max diagonale
//		double Amm = (double) height / width;
//		double Bmm = 0;
//
//		// initialisation l'équation de droite et du point extreme
//		Droite d = new Droite();
//
//		// Définition du triangle dans lequel le click souris se trouve
//		// HAUT
//		if (arg0.getY() < Ademi * arg0.getX() + Bdemi) {
//			if (arg0.getY() < Amm * arg0.getX() + Bmm) {
//				System.out.println("haut");
//				// calcul de la droite point cliqué/centre
//
//				d = Droite.EquationDroite(new Point(width / 2, height / 2),
//						arg0.getPoint());
//
//				// Point d'intersection des deux droites
//				Ext.x = (0 - d.getB()) / d.getA();
//				Ext.y = 0;
//				// GAUCHE
//			} else {
//				System.out.println("gauche");
//				// calcul de la droite point cliqué/centre
//				d = Droite.EquationDroite(new Point(width / 2, height / 2),
//						arg0.getPoint());
//
//				// Point d'intersection des deux droites
//				Ext.x = 0;
//				Ext.y = d.getA() * 0 + d.getB();
//			}
//		} else {
//			// BAS
//			if (arg0.getY() > Amm * arg0.getX() + Bmm) {
//				System.out.println("bas");
//				// calcul de la droite point cliqué/centre
//				d = Droite.EquationDroite(new Point(width / 2, height / 2),
//						arg0.getPoint());
//				// penser à mettre AB en droite
//				Ext.x = (height - d.getB()) / d.getA();
//				Ext.y = height;
//				// DROITE
//			} else {
//				System.out.println("droite");
//				// calcul de la droite point cliqué/centre
//				d = Droite.EquationDroite(new Point(width / 2, height / 2),
//						arg0.getPoint());
//
//				Ext.x = width;
//				Ext.y = width * d.getA() + d.getB();
//			}
//		}
//	}
}
