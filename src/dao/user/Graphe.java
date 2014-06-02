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
public class Graphe extends Objet_Postgre<Polyligne_graphe> {
	
	private int ID;
	private Vector <Polyligne_graphe> vecteur_de_polylignes=new Vector <Polyligne_graphe>();
	private String type_graphe;
	private int nombre_objets;
	/**
	 * 
	 */
	
//	public Graphe(int ID, Vector<Polyligne_graphe> vecteur_de_polylignes, String type_graphe) {
//		// TODO Auto-generated constructor stub
//		this.ID=ID;
//		this.vecteur_de_polylignes=vecteur_de_polylignes;
//		this.type_graphe=type_graphe;
//	}
	
	public int get_ID(){
		return ID;
	}
	
	public Vector <Polyligne_graphe> get_vecteur_de_polylignes(){
		return vecteur_de_polylignes;
	}
	
	public String get_type_graphe(){
		return type_graphe;
	}
	
//	@Override
//	public void AjoutObjet(Polyligne_graphe Polyligne_graphe){
//		vecteur_de_polylignes.add(Polyligne_graphe);
//		nombre_objets++;
//	}
	
	@Override
	public void getObjet_par_niveau(int niveau) {
//		for (int i = 0; i<vecteur_de_polylignes.size(); i++)
//			if (vecteur_de_polylignes.elementAt(i).getNiveau() == 1)
//				System.out.println(vecteur_de_polylignes.elementAt(i).getNom());
	}
	
	@Override
	public void VideObjets() {
		vecteur_de_polylignes.clear();
		nombre_objets =0;
	}

	@Override
	public int NbreObjets() {
		return vecteur_de_polylignes.size();
		
	}

	@Override
	public Polyligne_graphe getElement(int index) {
		return vecteur_de_polylignes.elementAt(index);
	}

//	@Override
//	public void AjoutObjet(Polyligne_graphe obj, int mailleobservateur_i,
//			int mailleobservateur_j) {
//		vecteur_de_polylignes.add(obj);
//		nombre_objets ++;
//	}

	@Override
	public void AjoutObjet(Polyligne_graphe obj) {
		vecteur_de_polylignes.add(obj);
		nombre_objets ++;
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		 TODO Auto-generated method stub
//		Vector v=new Vector();
//		
//		for(int i=0; i<10;i++){
//			int tab[]=new int[2];
//			tab[0]=i*2; tab[1]=i*4;
//			v.addElement(tab);
//		}
//		
//		for(int i=0; i<10;i++){
//			int deb[]=((int[])(v.elementAt(i)));
//			System.out.println(deb[0]+"  "+deb[1]);
//		}
//	}

	

}
