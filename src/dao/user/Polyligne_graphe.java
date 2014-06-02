/**
 * 
 */
package dao.user;

import java.util.Vector;
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 * @author Francois
 *
 */
public class Polyligne_graphe {
	
	private int ID;
	private PGgeometry coordonnees;
	private String nature;
	private String sens;
	private String type_polyligne;
//	private String largeur;
	/**
	 * 
	 */
	
	//constructeur vide
	public Polyligne_graphe(){}
	
	public Polyligne_graphe(int ID, PGgeometry coordonnees, String nature, String sens, String type_polyligne) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
		this.coordonnees=coordonnees;
		this.nature=nature;
		this.sens=sens;
		this.type_polyligne=type_polyligne;
	}
	
	public int get_ID(){
		return ID;
	}
	
	public PGgeometry get_coordonnees(){
		return coordonnees;
	}
	
	public String get_nature(){
		return nature;
	}
	
	public String get_sens(){
		return sens;
	}
	
	public String get_type_polyligne(){
		return type_polyligne;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
