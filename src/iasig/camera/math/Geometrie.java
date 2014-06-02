package iasig.camera.math;

import java.awt.Point;
import javax.vecmath.Point3d;

/**
 * <b><u>Class Geometrie</u> : contient toutes les méthodes géométriques comme
 * le calcul de distance ou d'équation de droite.</b>
 * 
 * @author Groupe gestion de l'affichage (Chassin,Collot,Pain)
 * @version 1
 */
public class Geometrie {

	/**
	 * Retourne la distance entre deux points en 2D.
	 * 
	 * @return distance entre deux points en 2D.
	 */
	public static double distance(Point p1, Point p2) {
		double dist = 0;
		dist = Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
				+ (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
		System.out.println("La distance entre les 2 points est : " + dist);
		return dist;
	}

	/**
	 * Retourne la distance entre deux points en 3D.
	 * 
	 * @return distance entre deux points en 3D.
	 */
	public static double distance(Point3d p1, Point3d p2) {
		double dist = 0;
		dist = Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
				+ (p1.getY() - p2.getY()) * (p1.getY() - p2.getY())
				+ (p1.getZ() - p2.getZ()) * (p1.getZ() - p2.getZ()));
		System.out.println("La distance entre les 2 points est : " + dist);
		return dist;
	}

}
