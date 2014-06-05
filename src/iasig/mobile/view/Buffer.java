package iasig.mobile.view;

import java.io.IOException;
import java.util.ArrayList;
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
	public  ArrayList<ArrayList<SuperBG>> buffer_memoire;
	public int taille_buffer_memoire;
	public int centre_buffer_memoire_i;
	public int centre_buffer_memoire_j;
	/**
	 * le vecteur de vecteur, portant les objets indexes selon leur maille d'appartenance
	 */
	public  ArrayList<ArrayList<SuperBG>> buffer_visible;
	public int taille_buffer_visible;
	public int centre_buffer_visible_i;
	public int centre_buffer_visible_j;
	/**
	 * le vecteur de vecteur de vecteur d'objet, portant les objets indexes selon leur maille d'appartenance
	 */
	public ArrayList<ArrayList<ArrayList<Object>>>buffer_objet;
	
	public ArrayList<ArrayList<Tuile>> buffer_tuile;
	
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
		buffer_memoire = new ArrayList<ArrayList<SuperBG>>();
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_memoire.add(new ArrayList<SuperBG>());
			
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_memoire.get(i).add(null);
			}
		}

		buffer_tuile = new ArrayList<ArrayList<Tuile>>();
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_tuile.add(new ArrayList<Tuile>());
			
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_tuile.get(i).add(null);
			}
		}
		
		buffer_objet = new ArrayList<ArrayList<ArrayList<Object>>>();
		
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_objet.add(new ArrayList<ArrayList<Object>>());
			
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_objet.get(i).add(new ArrayList<Object>());
			}
		}
		
		buffer_visible = new ArrayList<ArrayList<SuperBG>>();
		for (int i = 0; i < taille_buffer_visible; i++) {
			buffer_visible.add(new ArrayList<SuperBG>());
			
			for (int j = 0; j < this.taille_buffer_visible; j++) {
				buffer_visible.get(i).add(null);
			}
		}
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
		
		int demi_taille_buffer= this.taille_buffer_memoire/2;
		int deltai = 0;
		int deltaj = 0;
		
		if(obj instanceof Maison){

			 deltai = ((Maison) obj).getMaille_i() - this.centre_buffer_visible_i;
			 deltaj = ((Maison) obj).getMaille_j() - this.centre_buffer_visible_j;
		}
		
		if(obj instanceof Lampadaire){

			deltai = ((Lampadaire) obj).getMaille_i() - this.centre_buffer_visible_i;
			deltaj = ((Lampadaire) obj).getMaille_j() - this.centre_buffer_visible_j;
		
		}

		if(obj instanceof Arbre){
			deltai = ((Arbre) obj).getMaille_i() - this.centre_buffer_visible_i;
			deltaj = ((Arbre) obj).getMaille_j() - this.centre_buffer_visible_j;

		}
		
		buffer_objet.get(demi_taille_buffer + deltai ).get(demi_taille_buffer + deltaj ).add(obj);

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
					
					buffer_tuile.get(i).set(j,new Tuile(i_terrain, j_terrain, Tuile.R5, Tuile.DB) );
					
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
			
				buffer_memoire.get(i).set(j, new SuperBG(buffer_tuile.get(i).get(j),
			
						buffer_objet.get(i).get(j)));
				
				//Liberation mémoire
				buffer_tuile.get(i).set(j, null);
				buffer_objet.get(i).set(j, new ArrayList<Object>());
				
			}
		}
		
	}
	
	/**
	 * Met à disposition le visible
	 * @throws IOException 
	 */
	public void initialisation_buffer_visible() {
		System.out.println("init buffer visible");
	
		
		for (int i = 0; i< taille_buffer_visible; i++){
			for(int j = 0; j< taille_buffer_visible; j++){
				
				buffer_visible.get(i).set(j, copieAttache_SuperBG(i, j));	
			}
		}
	}
		
		/**
		 * Met à jour le visible sur déplacement de la caméra
		 */
		public void rafraichissement_visible(int delta_i, int delta_j) {
			
			System.out.println("rafraichissement du buffer visible : "+delta_i+":"+delta_j);
			this.centre_buffer_visible_i+=delta_i;
			this.centre_buffer_visible_j+=delta_j;

			
			if ( delta_i !=0 && delta_j == 0  ){
				//Calcul des determinants
				int cas1 = (1 + delta_i)/2;
				int cas2 = (1 - delta_i)/2;
				
				int i_detach = cas2 * (taille_buffer_visible - 1 ); 
				int i_transfert = cas1 * (taille_buffer_visible - 1 ); 

				for ( int j = 0; j < taille_buffer_visible; j++ ){
					//Detachement des SuperBG qui sortent de la zone visible
					System.out.println("Mvmt horizontal, A detacher : "+i_detach+"/"+j);
					buffer_visible.get(i_detach).get(j).sbg.detach();
					
					//Re-indexation des SuperBG à conserver
					for ( int i = cas2 * ( taille_buffer_visible - 1 ) ; i*delta_i <= cas1*(taille_buffer_visible-2)+ cas2*(-1) ; i+=delta_i){
				
						buffer_visible.get(i).set(j, buffer_visible.get(i+delta_i).get(j));		
				
					}
					
					//Copie et attachement de nouveaux SuperBG à partir du buffer_memoire
					buffer_visible.get(i_transfert).set(j, copieAttache_SuperBG(i_transfert, j));				
				}
				
			}
			else if ( delta_i == 0 && delta_j != 0){
				
				//Calcul des determinants
				int cas3 = (1 + delta_j )/2;
				int cas4 = (1 - delta_j )/2;
				
				int j_detach = cas4 * (taille_buffer_visible - 1 ); 
				int j_transfert = cas3 * (taille_buffer_visible -1);
				
				
				for ( int i = 0; i < taille_buffer_visible; i++ ){
					//Detachement des SuperBG qui sortent de la zone visible
					System.out.println("Mvmt vertical, A detacher : "+i+"/"+j_detach);
					System.out.println(buffer_visible.get(i).get(j_detach));
					buffer_visible.get(i).get(j_detach).sbg.detach();
					
					//Re-indexation des SuperBG à conserver
					for (int j = cas4 * ( taille_buffer_visible - 1 ) ; j*delta_j <= cas3 *(taille_buffer_visible-2)+cas4*(-1); j+=delta_j){
						
						buffer_visible.get(i).set(j, buffer_visible.get(i).get(j+delta_j));		
						
					}
					
					//Copie et attachement de nouveaux SuperBG à partir du buffer_memoire
					buffer_visible.get(i).set(j_transfert, copieAttache_SuperBG(i, j_transfert));
				}
				
			}
			//deplacement diagonal (tous sens)
			else{
				System.out.println("MVT DIAGONAL");
				//Calcul des determinants
				int cas1 = (1 + delta_i)/2;
				int cas2 = (1 - delta_i)/2;
				int cas3 = (1 + delta_j )/2;
				int cas4 = (1 - delta_j )/2;
				
				int i_transfert = cas1 * ( taille_buffer_visible -1 );
				int i_detach = cas2 * (taille_buffer_visible - 1 ); 
				int j_transfert = cas3 * ( taille_buffer_visible -1 );
				int j_detach = cas4 * (taille_buffer_visible - 1 ); 
				
				//Detachement des SuperBG qui sortent de la zone visible
				for ( int i = 0 ; i < taille_buffer_visible; i++){
					buffer_visible.get(i).set(j_detach, copieAttache_SuperBG(i, j_detach));
				}
				for ( int j = 0; j < taille_buffer_visible -1 ; j++){
					buffer_visible.get(i_detach).set(j, copieAttache_SuperBG(i_detach, j));
				}
				
				//Re-indexation des SuperBG à conserver
				for ( int i = cas2 * ( taille_buffer_visible - 1 ) ; i*delta_i <= cas1*(taille_buffer_visible-2)+ cas2*(-1) ; i+=delta_i){

				//for ( int i = (1 - delta_i)/2 * ( taille_buffer_visible - 1 ) ; i*delta_i <= ((delta_i + 1)/2)*(taille_buffer_visible-2)+( (-1)*(1 - delta_i)/2) ; i+=delta_i){
					for (int j = cas4 * ( taille_buffer_visible - 1 ) ; i*delta_j <= cas3 *(taille_buffer_visible-2)+cas4*(-1); i+=delta_j){
						
						//buffer_visible[i][j] = buffer_visible[i-delta_i][j-delta_j]
						buffer_visible.get(i).set(j, buffer_visible.get(i+delta_i).get(j+delta_j));		
						
					}	
				}
				
				//Copie et attachement de nouveaux SuperBG à partir du buffer_memoire
				for ( int i = 0 ; i < taille_buffer_visible; i++){
					buffer_visible.get(i).set(j_transfert, copieAttache_SuperBG(i, j_transfert));
				}
				for ( int j = 0; j < taille_buffer_visible -1 ; j++){
					buffer_visible.get(i_transfert).set(j, copieAttache_SuperBG(i_transfert, j));
				}
				
				
			}
		}
			
			
			//Affectaction des SuperBG à attacher
			/**
			 * copie_et_attache_SuperBG_depuis_buffer_memoire_vers visible
			 */
			public SuperBG copieAttache_SuperBG(int i, int j) {
				
				int ecart = (taille_buffer_memoire - taille_buffer_visible ) / 2 ;
				int i_memoire = i + ecart + centre_buffer_visible_i - centre_buffer_memoire_i;
				int j_memoire = j + ecart + centre_buffer_visible_j - centre_buffer_memoire_j;	
				
				SuperBG copie_sbg = new SuperBG(buffer_memoire.get(i_memoire).get(j_memoire));
				tg.addChild(copie_sbg.sbg);
				
				return copie_sbg;
			}
			
	
		
		
	
	
}
