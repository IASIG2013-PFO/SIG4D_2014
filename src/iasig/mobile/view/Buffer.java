package iasig.mobile.view;

import java.io.IOException;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import dao.GenericDAO;
import dao.user.Arbre;
import dao.user.Lampadaire;
import dao.user.Maison;

public class Buffer {

	/**
	 * le transformGroup Principal
	 */
	public final TransformGroup tg;
	
	/**
	 * le vecteur de vecteur, portant les objets indexes selon leur maille d'appartenance
	 */
	//public  Vector<Vector<SuperBG>> buffer_memoire;
	public SuperBG[][] buffer_memoire;
	public int taille_buffer_memoire;
	public int centre_buffer_memoire_i;
	public int centre_buffer_memoire_j;
	/**
	 * le vecteur de vecteur, portant les objets indexes selon leur maille d'appartenance
	 */
	//public  Vector<Vector<SuperBG>> buffer_visible;
	public SuperBG[][] buffer_visible;
	public int taille_buffer_visible;
	public int centre_buffer_visible_i;
	public int centre_buffer_visible_j;
	/**
	 * le vecteur de vecteur de vecteur d'objet, portant les objets indexes selon leur maille d'appartenance
	 */
	public Vector<Vector<Vector<Object>>> buffer_objet;
	public Vector<Vector<Tuile>> buffer_tuile;
	
	public Buffer(int taille_buffer_memoire,int taille_buffer_visible , int centre_i, int centre_j, TransformGroup tg ) throws IOException {
		
		this.centre_buffer_memoire_i=centre_i;
		this.centre_buffer_memoire_j=centre_j;
		
		this.centre_buffer_visible_i=centre_i;
		this.centre_buffer_visible_j=centre_j;
		
		this.taille_buffer_memoire=taille_buffer_memoire;
		this.taille_buffer_visible=taille_buffer_visible;
		
		//Référence sur le TransformGroup parent
		this.tg = tg;
		
		//Initialisation des Buffers (matrices) à vide		
		buffer_memoire = new SuperBG[taille_buffer_memoire][taille_buffer_memoire];
//		buffer_memoire = new Vector<Vector<SuperBG>>();
//		for (int i = 0; i < taille_buffer_memoire; i++) {
//			buffer_memoire.addElement(new Vector<SuperBG>());
//			
//			for (int j = 0; j < taille_buffer_memoire; j++) {
//				buffer_memoire.lastElement().addElement(null);
//			}
//		}

		buffer_tuile = new Vector<Vector<Tuile>>();
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_tuile.addElement(new Vector<Tuile>());
			
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_tuile.lastElement().addElement(null);
			}
		}
		
		buffer_objet = new Vector<Vector<Vector<Object>>>();
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_objet.addElement(new Vector<Vector<Object>>());
			
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_objet.lastElement().addElement(new Vector<Object>());
			}
		}
		
		buffer_visible = new SuperBG[taille_buffer_visible][taille_buffer_visible];
//		buffer_visible = new Vector<Vector<SuperBG>>();
//		for (int i = 0; i < taille_buffer_visible; i++) {
//			buffer_visible.addElement(new Vector<SuperBG>());
//			
//			for (int j = 0; j < taille_buffer_visible; j++) {
//				buffer_visible.lastElement().addElement(null);
//			}
//		}
		//Fin initialisation
		
		//Ajout des Objets
		GenericDAO.selection_geographique_buffer(this);
		//Ajout des Tuiles
		remplissage_Buffer_Tuile();
		//remplissage du Buffer memoire
		remplissage_Buffer_Memoire();
		//Initialisation du Buffer Visible
		initialisation_buffer_visible();
		
		
	}

	/**
	 * Permet d'ajouter un objet 
	 * Recupere la maille de rattachement et calcul de la position relative par rapport 
	 * aux coordonnees de la maille centrale du buffer
	 * Insertion à l'index correspondant dans le vecteur de vecteur selon typage!
	 * @param obj Un objet
	 */
	public void AjoutObjet(Object obj) {
		
		if(obj instanceof Maison){

			int deltai = ((Maison) obj).getMaille_i() - this.centre_buffer_visible_i;
			int deltaj = ((Maison) obj).getMaille_j() - this.centre_buffer_visible_j;
		
			int demi_taille_buffer= this.taille_buffer_memoire/2;

			buffer_objet.elementAt(demi_taille_buffer + deltai ).elementAt(demi_taille_buffer + deltaj ).add(obj);
			
		}
		
		if(obj instanceof Lampadaire){

			int deltai = ((Lampadaire) obj).getMaille_i() - this.centre_buffer_visible_i;
			int deltaj = ((Lampadaire) obj).getMaille_j() - this.centre_buffer_visible_j;
		
			int demi_taille_buffer= this.taille_buffer_memoire/2;

			buffer_objet.elementAt(demi_taille_buffer + deltai ).elementAt(demi_taille_buffer + deltaj ).add(obj);
			
		}

		if(obj instanceof Arbre){
			int deltai = ((Arbre) obj).getMaille_i() - this.centre_buffer_visible_i;
			int deltaj = ((Arbre) obj).getMaille_j() - this.centre_buffer_visible_j;
		
			int demi_taille_buffer= this.taille_buffer_memoire/2;
			
			buffer_objet.elementAt(demi_taille_buffer + deltai ).elementAt(demi_taille_buffer + deltaj ).add(obj);
			
		}
	}
	
	/**
	 * Permet de remplir le Buffer Memoire de Tuiles
	 * @throws IOException 
	 */
	public void remplissage_Buffer_Tuile() throws IOException {
	
		int demi_taille_buffer= this.taille_buffer_memoire/2;
		
		for (int i = 0; i< taille_buffer_memoire; i++){
			
			int deltai = i - demi_taille_buffer;
			int i_terrain = this.centre_buffer_memoire_i +  deltai;
			
				for(int j = 0; j< taille_buffer_memoire; j++){
					int deltaj = j - demi_taille_buffer;
					int j_terrain = this.centre_buffer_memoire_j +  deltaj;
					
					//buffer_tuile[i][j] =  new Tuile(i_terrain, j_terrain, Tuile.R200, Tuile.DB)
					buffer_tuile.elementAt(i).setElementAt(new Tuile(i_terrain, j_terrain, Tuile.R5, Tuile.DB), j);	
				}
		}
			
	}
	
	/**
	 * Permet de remplir le Buffer Memoire de Tuiles
	 * @throws IOException 
	 */
	public void remplissage_Buffer_Memoire() throws IOException{
		
		for (int i = 0; i< taille_buffer_memoire; i++){
			for(int j = 0; j< taille_buffer_memoire; j++){
		
				//buffer_memoire[i][j] = new SuperBG(buffer_tuile[i][j], buffer_objet[i][j] ) 
				buffer_memoire[i][j] = new SuperBG(new SuperBG(buffer_tuile.elementAt(i).elementAt(j), 
						buffer_objet.elementAt(i).elementAt(j))) ;
//				buffer_memoire.elementAt(i).setElementAt(new SuperBG(buffer_tuile.elementAt(i).elementAt(j), 
//						buffer_objet.elementAt(i).elementAt(j)), j);
							
				//Liberation mémoire
				buffer_tuile.elementAt(i).setElementAt(null, j);
				buffer_objet.elementAt(i).setElementAt(new Vector<>(), j);
				
			}
		}
		
	}
	
	/**
	 * Met à disposition le visible
	 * @throws IOException 
	 */
	public void initialisation_buffer_visible() {
		System.out.println("init buffer visble");
		int ecart = (taille_buffer_memoire - taille_buffer_visible ) / 2 ;
	
		
		for (int i = 0; i< taille_buffer_visible; i++){
			int i_memoire = i + ecart ;
			for(int j = 0; j< taille_buffer_visible; j++){
				int j_memoire = j + ecart ;
				
				//buffer_visible[i][j] = new SuperBG(buffer_memoire[i_memoire][j_memoire]);
				//En copie constructeur
				//buffer_visible.elementAt(i).setElementAt(new SuperBG( buffer_memoire.elementAt(i_memoire).elementAt(j_memoire) ), j);
				buffer_visible[i][j] = new SuperBG(buffer_memoire[i_memoire][j_memoire]);
				//buffer_visible.elementAt(i).setElementAt( new SuperBG( buffer_memoire.elementAt(i_memoire).elementAt(j_memoire) ), j);
				
				tg.addChild(buffer_visible[i][j].sbg);
				//tg.addChild(buffer_visible.elementAt(i).elementAt(j).sbg);
			}
		}
		
		
	}
	
	
}
