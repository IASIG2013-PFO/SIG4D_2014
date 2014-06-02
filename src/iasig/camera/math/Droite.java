package iasig.camera.math;

import java.awt.Point;

import javax.vecmath.Point2d;

/**
 * <b><u>Class Droite</u> : Creation d'objet geometrique de type Droite
 * de la position, getters et setters.</b>
 * 
 * @author Groupe gestion de l'affichage (Chassin, Collot, Pain)
 * @version 0 (28/05/2014)
 */
public class Droite {
	
	/**
	 * Coefficients de la droite y = a*x +b
	 * 
	 */
	private double a;
	private double b;

	/**
	 * Constructeur Droite
	 * <p>
	 * A la construction d'un objet Droite, on peut définir une droite avec ces
	 * coefficients (coefficient directeur et ordonnée à l'origine) directement
	 * ou pas.
	 * </p>
	 * 
	 * @param a
	 * 			coefficient directeur de la droite
	 * @param b
	 * 			ordonnee a l'origine de la droite
	 */
	Droite(double a, double b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Constructeur par defaut
	 */
	public Droite() {

	}

	/**
	 * @return the a
	 */
	public double getA() {
		return a;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(double a) {
		this.a = a;
	}

	/**
	 * @return the b
	 */
	public double getB() {
		return b;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * Retourne les coefficients a et b d'une droite type y = a*x + b.
	 * 
	 * @param p1
	 * 			premier Point2d de la droite recherchee
	 * @param p2
	 * 			second Point2d de la droite recherchee
	 * 
	 * @return coefficient a et b d'une droite.
	 */
	public static Droite EquationDroite(Point2d p1, Point2d p2) {

		Droite d = new Droite();
		// Calcul du coefficient directeur
		if ((p2.x - p1.x) == 0) {
			d.a = 0;
		} else {
			d.a = ((double) (p2.y - p1.y)) / ((double) (p2.x - p1.x));
		}
		d.b = p2.y - p2.x * d.a;
		return d;

	}

	/**
	 * Retourne les coefficients a et b d'une droite type y = a*x + b.
	 * 
	 * @param p1
	 * 			premier Point de la droite recherchee
	 * @param p2
	 * 			second Point de la droite recherchee
	 * 
	 * @return coefficient a et b d'une droite.
	 */
	public static Droite EquationDroite(Point p1, Point p2) {

		Droite d = new Droite();
		// Calcul du coefficient directeur
		d.a = ((double) (p2.y - p1.y)) / ((double) (p2.x - p1.x));
		// Calcul de l'ordonnée à l'origine
		d.b = p2.y - p2.x * d.a;
		return d;

	}

	/**
	 * Methode retournant le point 2D d'intersection de deux droites 2D
	 * 
	 * @param d1
	 * 			premiere droite
	 * @param d2
	 * 			seconde droite
	 * 
	 * @return Point2D d'intersection de deux droites
	 */
	public static Point2d PI2D(Droite d1, Droite d2) {
		Point2d point = new Point2d();

		if (d1.a != 0 && d2.a != 0) {
			point.x = (d2.b - d1.b) / (d1.a - d2.a);
			point.y = d1.a * point.x + d1.b;
		}

		return point;
	}
}
