package iasig.mobile.view;

import iasig.mobile.elements.VehiculeLibre;
import iasig.mobile.elements.VoitureLibre;
import iasig.camera.gestionaffichage.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point2i;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.postgis.MultiPolygon;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;

import dao.GenericDAO;
//import dao.user.Buffer;

public class World extends JFrame {
	public float[][] MNT = null;
	private Tuile[] liste_tuiles;
	private Tuile tuileCourante = null;
	double x0, y0, DX, DY;
	private int nligne = 0, ncolonne = 0;
	private Vector<VehiculeLibre> listevehicule = new Vector<VehiculeLibre>();
	private static final long serialVersionUID = 1L;
	private Listeners ltn;
	private TransformGroup transformObjet;
	private TransformGroup transformMNT;
	private TransformGroup tg2;
	
	
	
	Buffer buffer;
	
	
	
	

	
	/**********************COULEURS*********************/
	static final Color3f Blue = new Color3f(0.f,0.f,1.f);
	static final Color3f Red = new Color3f(1.f,0.f,0.f);
	static final Color3f Green = new Color3f(0.f,1.f,0.f);
	static final Color3f Yellow = new Color3f(1.f,1.f,0.f);
	static final Color3f Magenta = new Color3f(1.f,0.f,1.f);
	static final Color3f Cyan = new Color3f(0.f,1.f,1.f);
	
	
	/**
	 * 
	 * @param i
	 *            : largeur fenetre
	 * @param L
	 *            : longueur fenetre
	 */
	public World(int i, int L) {
		
		this.setSize(i, L);
		
	}

	/**
	 * G�n�re l'affichage de la sc�ne, du MNT et des mobiles associ�s
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void DisplayWorld() throws SQLException, IOException {
		double t = System.currentTimeMillis();
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		System.out.println(c.getDoubleBufferEnable());
		conteneur.add("Center", c);

		univers = new SimpleUniverse(c);
		

		ltn = new Listeners(univers, this, listevehicule);
		c.addMouseListener(ltn);
		c.addMouseMotionListener(ltn);
		c.addMouseWheelListener(ltn);
		c.addKeyListener(ltn);
		
		racine = new BranchGroup();
		BranchGroup bg = new BranchGroup();
		tg2 = new TransformGroup();
		TransformGroup tg3 = new TransformGroup();
		TransformGroup tg4 = new TransformGroup();
		tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		bg.addChild(tg2);
		racine.addChild(bg);
		// tg2.addChild(DisplayMNT()[0]);
		// __________________________________
		ColorCube cc = new ColorCube(1000);
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(3000, 3000, 10));
		tg4.setTransform(t3d);
		ColorCube cc2 = new ColorCube(500);
		tg3.addChild(cc);
		tg4.addChild(cc2);
		cc.setUserData("Giga cc");
		cc2.setUserData("Giga cc2");
		cc.setCollidable(true);
		tg2.addChild(tg3);
		// ___________________________________
		tg3.addChild(tg4);

//		if (this.listevehicule.size() != 0) {
//			for (int i = 0; i < this.listevehicule.size(); i++) {
//				tg4.addChild(this.listevehicule.get(i).GetBranchGroup(this));
//			}
//		}
		Viewer viewer = univers.getViewer();
		View view2 = viewer.getView();
		view2.setBackClipDistance(1000000d);
		//this.GestionCamera(0);
		//this.GestionVoiture(0);
		
		// Creation d'une lumiere ambiante de couleur blanche
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 1000);
		Light ambientLight = new AmbientLight(new Color3f(Color.white));
		ambientLight.setInfluencingBounds(bounds);
		racine.addChild(ambientLight);

		// Creation d'une lumiere directionnelle de couleur blanche
		Light directionalLight = new DirectionalLight(new Color3f(Color.white),
				new Vector3f(1, -1, -1));
		directionalLight.setInfluencingBounds(bounds);
		racine.addChild(directionalLight);

		racine.addChild(create_universe()); // Cr�ation de l'univers

		univers.addBranchGraph(racine); // Ajout de l'univers � la racine
										// graphique
		setVisible(true);
		System.out.println("Temps de création de la fenêtre : "+(System.currentTimeMillis()-t));
	}

	/**
	 * 
	 * @param i
	 *            : num�ro de la voiture
	 * @return : l'i-�me objet voiture de la liste de voiture de l'objet World
	 */
	public VehiculeLibre getlistevehicule(int i) {
		return listevehicule.get(i);
	}

	public Vector<VehiculeLibre> getlistevehicule() {
		return listevehicule;
	}

	public SimpleUniverse getUnivers() {
		return univers;
	}

	public Listeners getListeners() {
		return ltn;
	}
	
	

	/**
	 * @return the tuileCourante
	 */
	public Tuile getTuileCourante() {
		return tuileCourante;
	}



	/**
	 * Calcul de distance
	 * 
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @return distance
	 */
	public static double Distance(double x, double y, double x2, double y2) {
		return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	}

	/**
	 * Pour un couple (X,Y) donn�, on r�cup�re le Z associ� sur le MNT
	 * 
	 * @param x
	 *            : position x
	 * @param y
	 *            : position y
	 * @return z : une altitude
	 */
	public double GetZMNTPlan(double x, double y) {
		Vector3d normale = new Vector3d();
		Point3d A = new Point3d();
		Point3d B = new Point3d();
		Point3d C = new Point3d();
		Vector3d BA = new Vector3d();
		Vector3d BC = new Vector3d();

		double z = 0;
		int xmin = (int) Math.floor((x - this.x0) / DX);
		int ymin = (int) Math.floor((y - this.y0) / DY);
		int ymax = ymin + 1;
		int xmax = xmin + 1;

		double distance1 = Distance(xmin * this.DX + this.x0, ymin * this.DY
				+ this.y0, x, y);
		double distance2 = Distance(xmax * this.DX + this.x0, ymax * this.DY
				+ this.y0, x, y);
		double distance3 = Distance(xmin * this.DX + this.x0, ymax * this.DY
				+ this.y0, x, y);
		double distance4 = Distance(xmax * this.DX + this.x0, ymin * this.DY
				+ this.y0, x, y);
		// -------------------------------------------
		double a = Math.min(distance1, distance2);
		double b = Math.min(distance3, distance4);
		double c = Math.min(a, b);
		if (c == distance1) {
			// On ne consid�re pas le point xmin/ymin

			A = new Point3d(xmin * this.DX + this.x0, ymax * this.DY + this.y0,
					this.MNT[ymax][xmin]);
			B = new Point3d(xmax * this.DX + this.x0, ymax * this.DY + this.y0,
					this.MNT[ymax][xmax]);
			C = new Point3d(xmax * this.DX + this.x0, ymin * this.DY + this.y0,
					this.MNT[ymin][xmax]);
		} else if (c == distance2) {
			// On ne consid�re pas le point xmax/ymax

			A = new Point3d(xmin * this.DX + this.x0, ymax * this.DY + this.y0,
					this.MNT[ymax][xmin]);
			B = new Point3d(xmin * this.DX + this.x0, ymin * this.DY + this.y0,
					this.MNT[ymin][xmin]);
			C = new Point3d(xmax * this.DX + this.x0, ymin * this.DY + this.y0,
					this.MNT[ymin][xmax]);
			// System.out.println("Point " + A.z + " "+ B.z + " "+ C.z);
		} else if (c == distance3) {// On ne consid�re pas le point xmin/ymax

			A = new Point3d(xmin * this.DX + this.x0, ymin * this.DY + this.y0,
					this.MNT[ymin][xmin]);
			B = new Point3d(xmax * this.DX + this.x0, ymax * this.DY + this.y0,
					this.MNT[ymax][xmax]);
			C = new Point3d(xmax * this.DX + this.x0, ymin * this.DY + this.y0,
					this.MNT[ymin][xmax]);
			// System.out.println("Point " +A.z + " "+ B.z + " "+ C.z);
		} else if (c == distance4) {// On ne consid�re pas le point xmax/ymin

			A = new Point3d(xmin * this.DX + this.x0, ymax * this.DY + this.y0,
					this.MNT[ymax][xmin]);
			B = new Point3d(xmax * this.DX + this.x0, ymax * this.DY + this.y0,
					this.MNT[ymax][xmax]);
			C = new Point3d(xmin * this.DX + this.x0, ymin * this.DY + this.y0,
					this.MNT[ymin][xmin]);
			// System.out.println("Point " +A.z + " "+ B.z + " "+ C.z);
		}

		BA = new Vector3d(A.x - B.x, A.y - B.y, A.z - B.z);
		BC = new Vector3d(C.x - B.x, C.y - B.y, C.z - B.z);
		normale = new Vector3d();
		normale.cross(BA, BC);
		normale.normalize();
		double d1 = -B.x * normale.x - B.y * normale.y - B.z * normale.z;

		z = -(d1 + normale.x * x + normale.y * y) / normale.z;

		return z;
	}

	/**
	 * M�thode permettant de mettre � jour les valeurs de pitch & roll de la
	 * i-�me voiture de l'objet world
	 * 
	 * @param i
	 *            : num�ro de voiture dans la liste de voiture de l'objet world
	 * 
	 */
	public void GestionVoiture(int i) {

		double tab[][] = null;
		double pitch = 0, roll = 0;
		double[] Z = new double[4];
		if (i > listevehicule.size()) {
			System.out.println("Erreur num�rotation voiture");
		}
		// 1) R�cup�ration position roues
		try {

			tab = ((VoitureLibre) (this.listevehicule.get(i)))
					.getVWorldPositionRoues();
			// 2) R�cup�ration des Z respectifs
			for (int j = 0; j <= 3; j++) {
				Z[j] = this.GetZMNTPlan(tab[j][0], tab[j][1]);
			}
			// 3) R�cup�ration pitch & roll
			// Pitch : roue 3 : Arri�re Gauche / roue 0 :Avant Gauche
			pitch = (Z[3] - Z[0]) / (2 * this.listevehicule.get(i).getLength());
			this.listevehicule.get(i).SetPitch(Math.asin(pitch));
			// Roll : roue 0: Avant Gauche / roue 1 :Avant Droite
			roll = (Z[0] - Z[1]) / (2 * this.listevehicule.get(i).getWidth());
			this.listevehicule.get(i).SetRoll(Math.asin(roll));
		} catch (Exception e) {
		}

	}

	/**
	 * Permet d'ajouter un objet voiture � la liste de voiture de l'objet World
	 * 
	 * @param voiture
	 */
	public void AddVehicule(VehiculeLibre vehicule) {
		this.listevehicule.add(vehicule);
	}

	/**
	 * M�thode mettant � jour la position de la cam�ra en vue ext�rieure de la
	 * i-�me voiture de la liste
	 * 
	 * @param i
	 *            : num�ro de la voiture � suivre
	 */
	public void GestionCamera(int i) {
		// VehiculeLibre car = null;
		// double[] tab = null;
		// double[] tab2 = null;
		// car = this.listevehicule.get(i);
		// tab =car.getPositionCamExt();
		// tab2 =car.getVWorldPosition();
		// Point3d direction = new Point3d(tab2[0],tab2[1],tab2[2]);
		// Point3d positioncam = new Point3d(tab[0],tab[1],tab[2]);
		// Vector3d up = new Vector3d(0,0,1);
		// ViewingPlatform view = this.univers.getViewingPlatform ( );
		// TransformGroup tg = viegetViewPlatformTransform();
		// Transform3D t3d = new Transform3D();
		// t3d.lookAt(positioncam,direction,up);
		// t3d.invert();
		// tg.setTransform(t3d);
	}

	/**
	 * M�thode mettant � jour la position de la cam�ra en vue conducteur de la
	 * i-�me voiture de la liste
	 * 
	 * @param i
	 *            : num�ro de la voiture � suivre
	 */
	public void GestionCameraConducteur(int i) {

		// VehiculeLibre car = this.listevehicule.get(i);
		// double[][] tab = car.getPositionCamConducteur();
		// Point3d positioncam = new Point3d(tab[0][0],tab[0][1],tab[0][2]);
		// Point3d direction = new Point3d(tab[1][0],tab[1][1],tab[1][2]);
		// Vector3d up = new Vector3d(0,0,1);
		//
		// ViewingPlatform view = this.univers.getViewingPlatform ( );
		// TransformGroup tg = viegetViewPlatformTransform();
		// Transform3D t3d = new Transform3D();
		// t3d.lookAt(positioncam,direction,up);
		// t3d.invert();
		// tg.setTransform(t3d);

	}

	public void CameraMovedTo(double x, double y) throws IOException {
		
		if(buffer==null){return;}
		
		System.out.println("setT");
		System.out.println(x+" "+y);
		int i = (int) ((x - Tuile.Xmin) / Tuile.DX);
		int j = (int) ((y - Tuile.Ymin) / Tuile.DY);
		
		
		int delta_i = i-buffer.centre_buffer_visible_i ;
		int delta_j = j-buffer.centre_buffer_visible_j ;
		
		if (delta_i == 0 && delta_j == 0 ){
			//la camera n'a pas change de tuile
			return;
		}
		
//		buffer.centre_buffer_visible_i = i;
//		buffer.centre_buffer_visible_j = j;
		
		System.out.println(i+" "+j);
		
		buffer.rafraichissement_visible(delta_i, delta_j);

//		//System.out.println("limite rechargement: "+buffer_objets.getLimiteRechargement());
//			if(Math.abs(i-buffer_objets.mailleobservateur_i) >= buffer_objets.getLimiteRechargement() ||Math.abs(j-buffer_objets.mailleobservateur_j) >= buffer_objets.getLimiteRechargement()){
//				System.out.println(" chargement! ");
//				GenericDAO.selection_geographique(buffer_objets, (float) ltn.getView().getEye().x, (float)ltn.getView().getEye().y, 1000);
//				
//			}
//			System.out.println("Mise en memoire du visible");
//			buffer_visible =  buffer_objets.getObjet_Visible(i, j);
//			
//			dessin();
//			
		
	}

	// /////////////////DEBUT EMILIE JEAN FLO ......
	// //////////////////////////////

	/**
	 * Cette classe est la classe d'affichage graphique de tous les objets de
	 * l'univers dans une fen�tre applicative. HERITE DE : -JFrame application
	 * graphique standard
	 */

	/* /////////////////////CONSTANTES/////////////////// */

	/************ OPTIONS D'AFFICHAGE GRAPHIQUE ***********/
	public static final boolean WITHORTHO = true; // Afficher l'orthophoto
	public static final boolean WITHOUTORTHO = false; // Ne pas afficher
														// l'orthophoto
	/****************************************************/



	/* ////////////////////////////////////////////////// */

	/* /////////////////////ATTRIBUTS//////////////////// */

	/******************* BASE GRAPHIQUE *******************/
	private SimpleUniverse univers;
	private BranchGroup racine;
	/****************************************************/

	/***************** CHOIX DES OPTIONS ******************/
	private boolean source = Tuile.DB;
	private boolean orthophoto = WITHORTHO;
	/****************************************************/

	/****************** OBJETS A CHARGER ******************/
	private Vector<int[]> listeAffichage;
	public Tunnel tunnel;
	public Buffer buffer_objets;
	public Buffer buffer_BG;
	public Vector<Vector<Object>> buffer_visible ;
	Shape3D[] pieces;
	public static Objet3d[] tabobj;

	/****************************************************/

	/* ////////////////////////////////////////////////// */

	/* /////////////////////METHODES///////////////////// */

	/********************* CONSTRUCTEUR *******************/

	/******************* AUTRES METHODES ******************/

	// ---------------------------------------------------
	private BranchGroup create_universe() throws IOException {
		/**
		 * FONCTION : D�finir l'ensemble du graphe de sc�ne associ� � l'univers,
		 * avec initialisation de la cam�ra.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe BranchGroup] : le
		 * BranchGroup principal supportant l'ensemble du graphe de sc�ne, �
		 * attacher � la racine.
		 **/

		// cr�ation du BranchGroup principal
		BranchGroup bg = new BranchGroup();

		// Cr�ation du TransformGroup principal
		TransformGroup transform = new TransformGroup();
		transform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		transform.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transform.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transform.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
		// CHARGEMENT .OBJ
		URLobj urlobj = new URLobj();
		String[][] listeobj = urlobj.getTabobj();
		// chargement de tous les types .obj --> methode sans BDD
		tabobj = new Objet3d[listeobj.length];
		for (int i = 0; i < listeobj.length; i++) {
			int nbobj = Integer.parseInt(listeobj[i][1]);
			tabobj[i] = new Objet3d(nbobj, listeobj[i][0]);
		}// endfor(i)
		
		//Initialisation du buffer
		buffer = new Buffer(11, 5, 32, 19, transform);
		
	/*System.out.println(transform.numChildren());
	
	SuperBG sbg = (SuperBG)transform.getChild(39);
	TriangleArray mnt_graphique = (TriangleArray)sbg.mnt_plaque.getGeometry();
	int n = mnt_graphique.getVertexCount();
	//int n = mnt_graphique.getVertexCount();
	System.out.println("nb points MNT :"+n);
	System.out.println(sbg.objets.numChildren()		);*/
		System.out.println("Initialisation buffer terminée !!!");
		
		/*
		transformObjet = new TransformGroup();
		
		transformObjet.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transformObjet.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transformObjet.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
		transform.addChild(transformObjet);

		transformMNT = new TransformGroup();
		
		transformMNT.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transformMNT.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transformMNT.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
		transform.addChild(transformMNT);

		// R�cup�ration des Tuiles � partir de la BDD
		Point2i Centre = Tuile.Center(listeAffichage);

		// R�cup�ration des tuiles de la liste d'affichage depuis la BDD
		liste_tuiles = Tuile.load(listeAffichage, source);

		
		
		// chargement du buffer d'objets
		System.out.println("initialisation du buffer");
		buffer_objets = new Buffer(9, Centre.x, Centre.y, 1000,3);// 943000.f, 6538000.f, 1000, 9);
		this.setTuileCourante(ltn.getView().getEye().x, ltn.getView().getEye().y);
		
		
		

		// Cr�ation des branches graphiques correspondant � chaque tuile. Elles
		// s'attachent au TransformGroup principal.
		for (Tuile t : liste_tuiles) {
			transformMNT.addChild(define_tile_bg(t, orthophoto));
		}

		// Cr�ation de la branche graphique correspondant aux objets de l'espace visible. 
		System.out.println("taille embryon" +buffer_objets.embryon_buffer_visible.size());
		for (int i = 0; i < buffer_objets.embryon_buffer_visible.size(); i++) {

			//transformObjet.addChild(define_tile_buffer_bg(0));
			transformObjet.addChild(define_tile_buffer_bg(i));

	
		}*/


		bg.addChild(transform); // Attachement du TransformGroup � son
								// BranchGroup

		bg.compile(); // Compilation de l'ensemble du graphe => optimisation du
						// temps d'affichage

		return bg;
	}
	
	/*public void dessin() throws IOException{
		System.out.println("je dessineeeee");
		transformObjet.removeAllChildren();
	for (int i = 0; i < buffer_objets.embryon_buffer_visible.size(); i++) {

		transformObjet.addChild(define_tile_buffer_bg(i));
	
		}
	
	transformMNT.removeAllChildren();	
	for (Tuile t : liste_tuiles) {
		transformMNT.addChild(define_tile_bg(t, orthophoto));
	}
	
	
	}*/

	// ---------------------------------------------------

	// ---------------------------------------------------
	private BranchGroup define_tile_bg(Tuile t, boolean orthophoto)
			throws IOException {
		/**
		 * FONCTION : Construire le BranchGroup graphique d'une tuile donn�e.
		 * 
		 * PARAMETRES EN ENTREE : - [Scalaire, objet de la classe Tuile] : tuile
		 * source. - [Scalaire, bool�en] : option : orthophoto ou non.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe BranchGroup] : le
		 * BranchGroup construit avec MNT graphique
		 **/

		TriangleArray MNT_graphique = trace_MNT(t.mnt); // D�finition de la
														// g�om�trie du MNT
														// graphique

		Appearance apparence = new Appearance();

		if (orthophoto) { // WITHORTHO
			Texture textureOrtho = plaquer_ortho(MNT_graphique, t.ortho,
					t.mnt.getXmin(), t.mnt.getYmin(), t.mnt.getXmax(),
					t.mnt.getYmax(), t.mnt.getDX() / 5, t.mnt.getDY() / 5); // Application
																			// de
																			// la
																			// texture
																			// sur
																			// le
																			// MNT
																			// graphique
			apparence = set_apparence(textureOrtho,
					PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE); // D�finition
																					// de
																					// l'apparence
																					// du
																					// MNT
																					// graphique
																					// incluant
																					// les
																					// param�tres
																					// de
																					// la
																					// texture
		} else { // WITHOUTORTHO
			apparence = set_apparence(PolygonAttributes.POLYGON_LINE,
					PolygonAttributes.CULL_NONE); // D�finition de l'apparence
													// du MNT graphique non
													// textur�
		}

		// Shape du MNT graphique = Geom�trie + apparence
		Shape3D shape3d = new Shape3D(MNT_graphique, apparence);

		// Cr�ation du BranchGroup de la tuile
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);


		// Ajout de la forme
		bg.addChild(shape3d);

		// ////////////////////////
		// Dessin du tunnel
		if (t.mnt.getXmin() == 999000 && t.mnt.getYmin() == 6540000
				&& t.mnt.getDX() == 25) {
			GeometryInfo[] tunnel_graphique = trace_tunnel(tunnel);

			apparence = set_apparence(PolygonAttributes.POLYGON_FILL,
					PolygonAttributes.CULL_FRONT); // ///

			for (int k = 1; k <= tunnel_graphique.length; k++) {
				shape3d = new Shape3D(
						tunnel_graphique[k - 1].getIndexedGeometryArray(),
						apparence);
				bg.addChild(shape3d);
			}
		} else {
		}
		// ////////////////////////

		return bg;
	}

	// ---------------------------------------------------


	// ---------------------------------------------------
	
		
//		// ---------------------------------------------------
//		private BranchGroup define_tile_buffer_bg(int i)
//				throws IOException {
//			/**
//			 * FONCTION : Construire le BranchGroup graphique d'une tuile donnee.
//			 * 
//			 * PARAMETRES EN ENTREE : - [Scalaire, objet de la classe Tuile] : tuile
//			 * source.
//			 * 
//			 * RETOURNE : - [Scalaire, objet de la classe BranchGroup] : le
//			 * BranchGroup construit avec objets du buffer graphique
//			 **/
//	
//			// Cr�ation du BranchGroup de la tuile
//			BranchGroup bg = new BranchGroup();
//			bg.setCapability(BranchGroup.ALLOW_DETACH);
//
//	
//
//			//System.out.println("PLOUP: Dessin des elements du vecteur objet visible");
//			Objet3d.dessin_obj_vecteur(
//					bg,
//					tabobj[2].pieces,
////					buffer_objets.getObjet_Visible(buffer_objets.mailleobservateur_i, buffer_objets.mailleobservateur_j).elementAt(i));					
////					buffer_objets.getObjet_Visible(tuileCourante.i_maille, tuileCourante.j_maille).elementAt(i));
//					buffer_visible.elementAt(i)
//					);
//	
//			return bg;
//		}

	// ---------------------------------------------------
	public TriangleArray trace_MNT(MNT mnt) {
		/**
		 * FONCTION : Construire la g�om�trie du MNT, un ensemble de triangles
		 * 3D. Chaque maille carr�e est divis�e en deux de ces triangles par une
		 * diagonale.
		 * 
		 * PARAMETRES EN ENTREE : - [Scalaire, objet de la classe MNT] : MNT
		 * source.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe TriangleArray] : G�om�trie
		 * du MNT sous forme de triangles 3D.
		 **/

		// Instanciation de la g�om�trie avec indication du nombre total de
		// triangles.
		TriangleArray mnt_graphique = new TriangleArray(6 * (mnt.getNX() - 1)
				* (mnt.getNY() - 1), TriangleArray.COORDINATES
				| TriangleArray.COLOR_3 | TriangleArray.TEXTURE_COORDINATE_2);

		int X;
		int Y;
		int k;

		int DX = mnt.getDX();
		int DY = mnt.getDY();
		int Xmin = mnt.getXmin();
		int Ymax = mnt.getYmax();

		Point3d p1 = new Point3d();
		Point3d p2 = new Point3d();
		Point3d p3 = new Point3d();
		Point3d p4 = new Point3d();

		Color3f color = new Color3f();

		int n = 0;

		for (int i = 1; i <= mnt.getNY() - 1; i++) { // Indice des lignes (varie
														// du Nord au Sud)
			Y = Ymax - (i - 1) * DY; // Northing correspondant

			for (int j = 1; j <= mnt.getNX() - 1; j++) { // Indices des colonnes
															// (varie d'Ouest en
															// Est)
				X = Xmin + (j - 1) * DX; // Easting correspondant

//				if (X == tunnel.getX() && Y == tunnel.getY()) {
//					// Pr�voir un trou pour le tunnel
//					continue;
//				} else {
//				}

				// TRACE DE LA MAILLE D'ORIGINE (X,Y) (COIN SUD-OUEST)

				k = (i + j) % 2; // Param�tre d'orientation de la diagonale

				// Calcul des coordonn�es (X,Y,Z) des 4 coins de la maille
				// La diagonale correspond � [p2 p3]. p1 et p2 sont au Sud, p3
				// et p4 au Nord.
				p1.set(X + k * DX, Y, mnt.getZAt(i - 1, j + k - 1));
				p2.set(X + k * DX, Y - DY, mnt.getZAt(i, j + k - 1));
				p3.set(X + (1 - k) * DX, Y, mnt.getZAt(i - 1, j + (1 - k) - 1));
				p4.set(X + (1 - k) * DX, Y - DY, mnt.getZAt(i, j + (1 - k) - 1));

				// Orientation des triangles dans le sens direct
				if (k == 0) {
					// Triangle p1 p2 p3
					mnt_graphique.setCoordinate(n, p1);
					mnt_graphique.setCoordinate(n + 1, p3);
					mnt_graphique.setCoordinate(n + 2, p2);

					// Triangle p2 p3 p4
					mnt_graphique.setCoordinate(n + 3, p2);
					mnt_graphique.setCoordinate(n + 4, p3);
					mnt_graphique.setCoordinate(n + 5, p4);
				} else {
					// Triangle p1 p2 p3
					mnt_graphique.setCoordinate(n, p1);
					mnt_graphique.setCoordinate(n + 1, p2);
					mnt_graphique.setCoordinate(n + 2, p3);

					// Triangle p2 p3 p4
					mnt_graphique.setCoordinate(n + 3, p3);
					mnt_graphique.setCoordinate(n + 4, p2);
					mnt_graphique.setCoordinate(n + 5, p4);
				}

				for (int m = 0; m <= 5; m++) { // D�finition de la couleur en
												// fonction de la r�solution
					switch (DX) {
					case 25: {
						color.set(Blue);
						;
						break;
					}
					case 50: {
						color.set(Red);
						break;
					}
					case 100: {
						color.set(Green);
						break;
					}
					case 200: {
						color.set(Yellow);
						break;
					}
					case 500: {
						color.set(Magenta);
						break;
					}
					case 1000: {
						color.set(Cyan);
						break;
					}
					default: {
					}
					}
					mnt_graphique.setColor(n + m, color);
				}
				n += 6;
			}
		}

		return mnt_graphique;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	public GeometryInfo[] trace_tunnel(Tunnel tunnel) {
		/**
		 * FONCTION : Construire la g�om�trie du MNT, un ensemble de triangles
		 * 3D. Chaque maille carr�e est divis�e en deux de ces triangles par une
		 * diagonale. IMPORTANT : l'ordre des poins est choisi tel que le
		 * culling ne laisse que l'int�rieur du tunnel visible.
		 * 
		 * PARAMETRES EN ENTREE : - [Scalaire, objet de la classe MNT] : MNT
		 * source.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe TriangleArray] : G�om�trie
		 * du MNT sous forme de triangles 3D.
		 **/

		GeometryInfo[] tunnel_graphique = new GeometryInfo[5];

		MultiPolygon ml = (MultiPolygon) tunnel.getForme().getGeometry();
		int nbpoint = ml.numPoints() - 1;

		GeometryInfo polygone;
		// SOL
		polygone = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		Point3d[] points = new Point3d[nbpoint];
		Color3f[] colors = new Color3f[nbpoint];

		for (int i = 1; i <= nbpoint; i++) {
			points[i - 1] = new Point3d(ml.getPoint(i - 1).x,
					ml.getPoint(i - 1).y, tunnel.getZ());
			colors[i - 1] = Red;
		}

		polygone.setCoordinates(points);
		polygone.setColors(colors);
		polygone.setStripCounts(new int[] { nbpoint });

		tunnel_graphique[0] = polygone;

		// PLAFOND
		polygone = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		points = new Point3d[nbpoint - 2];
		colors = new Color3f[nbpoint - 2];

		for (int i = 2; i <= nbpoint - 1; i++) {
			points[nbpoint - i - 1] = new Point3d(ml.getPoint(i - 1).x,
					ml.getPoint(i - 1).y, tunnel.getZ() + tunnel.getH());
			colors[nbpoint - i - 1] = Blue;
		}

		polygone.setCoordinates(points);
		polygone.setColors(colors);
		polygone.setStripCounts(new int[] { nbpoint - 2 });

		tunnel_graphique[1] = polygone;

		// COTE
		polygone = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		points = new Point3d[4 * (nbpoint - 3)];
		colors = new Color3f[4 * (nbpoint - 3)];

		for (int i = 2; i <= nbpoint - 2; i++) {
			points[4 * (i - 2)] = new Point3d(ml.getPoint(i - 1).x,
					ml.getPoint(i - 1).y, tunnel.getZ());
			points[4 * (i - 2) + 1] = new Point3d(ml.getPoint(i - 1).x,
					ml.getPoint(i - 1).y, tunnel.getZ() + tunnel.getH());
			points[4 * (i - 2) + 2] = new Point3d(ml.getPoint(i).x,
					ml.getPoint(i).y, tunnel.getZ() + tunnel.getH());
			points[4 * (i - 2) + 3] = new Point3d(ml.getPoint(i).x,
					ml.getPoint(i).y, tunnel.getZ());

			colors[4 * (i - 2)] = Green;
			colors[4 * (i - 2) + 1] = Green;
			colors[4 * (i - 2) + 2] = Green;
			colors[4 * (i - 2) + 3] = Green;
		}

		polygone.setCoordinates(points);
		polygone.setColors(colors);

		tunnel_graphique[2] = polygone;

		// COTE ENTREE 1
		polygone = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
		points = new Point3d[3];
		colors = new Color3f[3];

		points[0] = new Point3d(ml.getPoint(0).x, ml.getPoint(0).y,
				tunnel.getZ());
		points[1] = new Point3d(ml.getPoint(1).x, ml.getPoint(1).y,
				tunnel.getZ() + tunnel.getH());
		points[2] = new Point3d(ml.getPoint(1).x, ml.getPoint(1).y,
				tunnel.getZ());

		colors[0] = Yellow;
		colors[1] = Yellow;
		colors[2] = Yellow;

		polygone.setCoordinates(points);
		polygone.setColors(colors);

		tunnel_graphique[3] = polygone;

		// COTE ENTREE 2
		polygone = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
		points = new Point3d[3];
		colors = new Color3f[3];

		points[0] = new Point3d(ml.getPoint(nbpoint - 1).x,
				ml.getPoint(nbpoint - 1).y, tunnel.getZ());
		points[1] = new Point3d(ml.getPoint(nbpoint - 2).x,
				ml.getPoint(nbpoint - 2).y, tunnel.getZ());
		points[2] = new Point3d(ml.getPoint(nbpoint - 2).x,
				ml.getPoint(nbpoint - 2).y, tunnel.getZ() + tunnel.getH());

		colors[0] = Cyan;
		colors[1] = Cyan;
		colors[2] = Cyan;

		polygone.setCoordinates(points);
		polygone.setColors(colors);

		tunnel_graphique[4] = polygone;

		return tunnel_graphique;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	public Texture plaquer_ortho(TriangleArray MNT_graphique,
			BufferedImage ortho, int Xmin, int Ymin, int Xmax, int Ymax,
			int RX, int RY) throws IOException {
		/**
		 * FONCTION : Cr�er la texture orthophoto associ� au MNT. Les
		 * coordonn�es textures sont calcul�es r�guli�rement en associant chaque
		 * sommet � sa position dans l'image.
		 * 
		 * PARAMETRES EN ENTREE-SORTIE : - MNT_graphique [Scalaire, objet de la
		 * classe TriangleArray] : MNT � plaquer. En sortie chaque sommet de
		 * triangle est muni de ses coordonn�es texture.
		 * 
		 * PARAMETRE EN ENTREE : - ortho [Scalaire, objet de la classe
		 * BufferedImage] : Image de l'orthophoto � utiliser pour la texture. -
		 * Xmin [Scalaire, entier] : Borne Ouest du MNT. - Ymin [Scalaire,
		 * entier] : Borne Nord du MNT. - Xmax [Scalaire, entier] : Borne Est du
		 * MNT. - Ymax [Scalaire, entier] : Borne Sud du MNT. - RX [Scalaire,
		 * entier] : Largeur d'une maille de MNT. - RY [Scalaire, entier] :
		 * Hauteur d'une maille de MNT.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Texture] : La texture
		 * cr��e � partir de l'orthophoto.
		 **/

		Texture texture = new TextureLoader(ortho).getTexture(); // Chargement
																	// de la
																	// texture �
																	// partir de
																	// l'image
		Point3d point3d = new Point3d();

		// Etendue g�ographique de la maille
		int EX = Xmax - Xmin + 2 * RX;
		int EY = Ymax - Ymin + 2 * RY;

		for (int i = 0; i < MNT_graphique.getVertexCount(); i++) {
			MNT_graphique.getCoordinate(i, point3d); // R�cup�ration des
														// coordonn�es (X,Y,Z)
														// du i� point de la
														// g�om�trie
			// Calcul des coordonn�es textures de ce point. Syst�me de
			// coordonn�es : (x,y) variant de 0 � 1. L'origine (0,0) est le coin
			// Sud-Ouest de l'orthophoto. Le coin Nord-Est correspond aux
			// coordonn�es maximales (1,1).
			MNT_graphique.setTextureCoordinate(0, i, new TexCoord2f(
					(float) (point3d.x - Xmin + RX) / EX, (float) (point3d.y
							- Ymin + RY)
							/ EY));
		}

		return texture;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	public Appearance set_apparence(int polygon_mode, int culling_mode) {
		/**
		 * FONCTION : D�finir les param�tres d�finissant l'apparence de l'objet,
		 * dans le cas o� il n'y a pas d'orthophoto.
		 * 
		 * PARAMETRES EN ENTREE : - polygon_mode [Scalaire, entier] : choix de
		 * repr�sentation des polygones : PolygonAttributes.LINE (filaire) ou
		 * PolygonAttributes.FILL (surface). - culling_mode [Scalaire, entier] :
		 * choix de culling : PolygonAttributes.CULL_NONE,
		 * PolygonAttributes.CULL_FRONT ou PolygonAttributes.CULL_BACK.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Appearance] : L'apparence
		 * de l'objet.
		 **/

		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(culling_mode); // Seules les faces orient�es dans
												// le sens direct par rapport �
												// la cam�ra seront visibles =>
												// pas de vision en sous-sol
		polyAttrib.setPolygonMode(polygon_mode);
		app.setPolygonAttributes(polyAttrib);

		return app;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	public Appearance set_apparence(Texture texture, int polygon_mode,
			int culling_mode) {
		/**
		 * FONCTION : D�finir les param�tres d�finissant l'apparence de l'objet,
		 * dans le cas o� il y une pas d'orthophoto.
		 * 
		 * PARAMETRES EN ENTREE : - texture [Scalaire, objet de la classe
		 * Texture] : Texture (orthophoto) appliqu�e � l'objet (MNT). -
		 * polygon_mode [Scalaire, entier] : choix de repr�sentation des
		 * polygones : PolygonAttributes.LINE (filaire) ou
		 * PolygonAttributes.FILL (surface). - culling_mode [Scalaire, entier] :
		 * choix de culling : PolygonAttributes.CULL_NONE,
		 * PolygonAttributes.CULL_FRONT ou PolygonAttributes.CULL_BACK.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Appearance] : L'apparence
		 * de l'objet.
		 **/

		Appearance app = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE); // Seules les faces
																// orient�es
																// dans le sens
																// direct par
																// rapport � la
																// cam�ra seront
																// visibles =>
																// pas de vision
																// en sous-sol
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL); // Aspect
																	// surfacique
		app.setPolygonAttributes(polyAttrib);

		TextureAttributes textureAttributes = new TextureAttributes(
				TextureAttributes.REPLACE, // La couleur de la texture remplace
											// celle de l'objet
				new Transform3D(), new Color4f(0.0f, 0.0f, 0.0f, 0.0f),
				TextureAttributes.NICEST); // Meilleure interpolation possible
											// pour la correction de la
											// perspective
		app.setTextureAttributes(textureAttributes);
		app.setTexture(texture);

		return app;
	}

	// ---------------------------------------------------

	/****************************************************/

	/***************** METHODES STATIQUES *****************/

	// ---------------------------------------------------
	static public int user_value_input(String mess) {
		/**
		 * FONCTION : Demander � l'utilisateur de saisir une valeur enti�re.
		 * 
		 * PARAMETRE EN ENTREE - mess [Scalaire, cha�ne de caract�re] : message
		 * � afficher pour indiquer � l'utilisateur qu'il doit saisir une
		 * valeur.
		 * 
		 * RETOURNE : - [Scalaire, entier] : valeur entr�e par l'utilisateur.
		 **/

		Scanner saisieUtilisateur = new Scanner(System.in);
		System.out.println(mess); // Affichage du message
		int n = saisieUtilisateur.nextInt();
		saisieUtilisateur.close();
		return n;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	static public void pause() {
		/**
		 * FONCTION : Effectuer une pause dans le programme. La pause se
		 * prolonge jusqu'� ce que l'utilisateur appuie sur une touche.
		 **/
		Scanner saisieUtilisateur = new Scanner(System.in);
		System.out.println("Appuyez sur une touche pour continuer...");
		saisieUtilisateur.next();
		saisieUtilisateur.close();
	}
	// ---------------------------------------------------

	/****************************************************/

	/* ////////////////////////////////////////////////// */
	public TransformGroup getTg2(){
		return tg2;
	}
	
	
	
	
}
/* CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC */

