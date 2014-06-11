/**
 * 
 */
package iasig.dao.user;

import java.util.Vector;


/**
 * @author Francois
 *
 */
public class Graphe extends Objet_Postgre<Polyligne_graphe> {
	
	private int ID;
	private Vector <Polyligne_graphe> vecteur_de_polylignes=new Vector <Polyligne_graphe>();
	private String type_graphe;
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
	
	
	@Override
	public void getObjet_par_niveau(int niveau) {
		//VIDE
	}
	
	@Override
	public void VideObjets() {
		vecteur_de_polylignes.clear();
	}

	@Override
	public int NbreObjets() {
		return vecteur_de_polylignes.size();
		
	}

	@Override
	public Polyligne_graphe getElement(int index) {
		return vecteur_de_polylignes.elementAt(index);
	}


	@Override
	public void AjoutObjet(Polyligne_graphe obj) {
		vecteur_de_polylignes.add(obj);
	}



	

}
