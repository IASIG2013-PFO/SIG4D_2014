package iasig.mobile.view;

import iasig.dao.GenericDAO;
import iasig.dao.user.Arbre;
import iasig.dao.user.Lampadaire;
import iasig.dao.user.Maison;

import java.io.IOException;
import java.util.ArrayList;

import javax.media.j3d.TransformGroup;

public class Buffer {

	/**
	 * le transformGroup Principal
	 */
	public World world;
	public final TransformGroup tg;
	public int marge;
	//le tread de rechargement du buffer
	Thread t;
	Thread t_select;
	Thread t_bati;
	Thread t_tuile;
	
	Thread t_buff_aux;
	Thread t_swap;
	
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
	public  ArrayList<ArrayList<SuperBG>> buffer_auxiliaire;
	public int centre_buffer_auxiliaire_i;
	public int centre_buffer_auxiliaire_j;
	
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
	
	public ArrayList<ArrayList<ArrayList<Batiment>>>buffer_bati;

	public ArrayList<ArrayList<Tuile>> buffer_tuile;
	
	public Buffer(int taille_buffer_memoire,int taille_buffer_visible , int centre_i, int centre_j, TransformGroup tg, World world ) throws IOException  {
		
		this.centre_buffer_auxiliaire_i=centre_i;
		this.centre_buffer_auxiliaire_j=centre_j;

		
		this.centre_buffer_visible_i=centre_i;
		this.centre_buffer_visible_j=centre_j;
		
		this.marge=taille_buffer_visible; //A adapter en fonction de la taille de buffers
		
		this.taille_buffer_memoire=taille_buffer_memoire;
		this.taille_buffer_visible=taille_buffer_visible;
		
		//Référence sur le TransformGroup parent
		this.tg = tg;
		this.world = world;
		
		
		
		//Initialisation des Buffers (matrices) à vide		
		buffer_memoire = new ArrayList<ArrayList<SuperBG>>();
		buffer_auxiliaire = new ArrayList<ArrayList<SuperBG>>();
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_memoire.add(new ArrayList<SuperBG>());
			buffer_auxiliaire.add(new ArrayList<SuperBG>());
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_memoire.get(i).add(null);
				buffer_auxiliaire.get(i).add(null);
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
		buffer_bati =  new ArrayList<ArrayList<ArrayList<Batiment>>>();
		
		for (int i = 0; i < taille_buffer_memoire; i++) {
			buffer_objet.add(new ArrayList<ArrayList<Object>>());
			buffer_bati.add(new ArrayList<ArrayList<Batiment>>());
			
			for (int j = 0; j < taille_buffer_memoire; j++) {
				buffer_objet.get(i).add(new ArrayList<Object>());
				buffer_bati.get(i).add(new ArrayList<Batiment>());
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
		

		
		//Thread Ajout des Objets
		
		 t_select = new Thread(new Runnable() {
			@Override
			public void run() {
				selection_Objet();
			}
		 });
		 
		//Thread Ajout du bati
		 
		 t_bati = new Thread(new Runnable() {
				@Override
				public void run() {
					selection_Bati();
				}
			 });
		 
		//Thread Ajout du tuilage

		 t_tuile = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						remplissage_Buffer_Tuile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			 });
		 
		double time = System.currentTimeMillis();
		
		t_select.start();
		t_bati.start();
		t_tuile.start();
		
		
		//Attendre completion des threads de selections BDD
		//avant MAJ du buffer auxilliaire
		try {
			t_select.join();
			t_bati.join();
			t_tuile.join();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Temps des selections d'objets (tuile/bati/objet) : "+(System.currentTimeMillis()-time));

		//Fin Join(); thread terminated


		
//		//Selection d'objets sans thread
//		//selection des objets
//		GenericDAO.selection_geographique_buffer(this);
//		System.out.println("Temps de selection des objets : "+(System.currentTimeMillis()-time));
//		//Ajout des Tuiles
//		time = System.currentTimeMillis();
//		remplissage_Buffer_Tuile();
//		System.out.println("Temps de selection des tuiles : "+(System.currentTimeMillis()-time));

		//remplissage du Buffer auxiliaire
		//initialisation de son propre buffer auxiliaire
		time = System.currentTimeMillis();
		remplissage_Buffer_Auxiliaire(this.buffer_auxiliaire);
		System.out.println("Temps de selection de construction du buffer auxiliaire : "+(System.currentTimeMillis()-time));

		//transfert du contenu du buffer auxiliaire vers le buffer mémoire
		time = System.currentTimeMillis();
		swap();
		System.out.println("Temps de swap : "+(System.currentTimeMillis()- time));

		//Initialisation du Buffer Visible
		initialisation_buffer_visible(this.buffer_memoire);
		
	
		
		
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

			 deltai = ((Maison) obj).getMaille_i() - this.centre_buffer_auxiliaire_i;
			 deltaj = ((Maison) obj).getMaille_j() - this.centre_buffer_auxiliaire_j;
		}
		
		if(obj instanceof Lampadaire){

			deltai = ((Lampadaire) obj).getMaille_i() - this.centre_buffer_auxiliaire_i;
			deltaj = ((Lampadaire) obj).getMaille_j() - this.centre_buffer_auxiliaire_j;
		
		}

		if(obj instanceof Arbre){
			deltai = ((Arbre) obj).getMaille_i() - this.centre_buffer_auxiliaire_i;
			deltaj = ((Arbre) obj).getMaille_j() - this.centre_buffer_auxiliaire_j;

		}
	
		
		buffer_objet.get(demi_taille_buffer + deltai ).get(demi_taille_buffer + deltaj ).add(obj);
	
	}
	
	
	/**
	 * Permet d'ajouter un objet 
	 * Recupere la maille de rattachement et calcul de la position relative par rapport 
	 * aux coordonnees de la maille centrale du buffer
	 * Insertion à l'index correspondant dans le vecteur de vecteur selon typage!
	 * @param obj Un objet
	 */
	public void AjoutBati(Batiment obj) {
		
		int demi_taille_buffer= this.taille_buffer_memoire/2;
		int deltai = 0;
		int deltaj = 0;
		
		
		
		if(obj instanceof Batiment){
			deltai = ((Batiment) obj).getMaille_i() - this.centre_buffer_auxiliaire_i;
			deltaj = ((Batiment) obj).getMaille_j() - this.centre_buffer_auxiliaire_j;

		}
		
		buffer_bati.get(demi_taille_buffer + deltai ).get(demi_taille_buffer + deltaj ).add(obj);
	
	}
	
	/**
	 * Permet de remplir le Buffer Memoire de Tuiles
	 * @throws IOException 
	 */
	public void remplissage_Buffer_Tuile() throws IOException {
		System.out.println("remplissage du buffer de tuiles");
		int demi_taille_buffer= this.taille_buffer_memoire/2;
		
		for (int i = 0; i< taille_buffer_memoire; i++){
			
			int deltai = i - demi_taille_buffer;
			int i_terrain = this.centre_buffer_auxiliaire_i +  deltai;
			
				for(int j = 0; j< taille_buffer_memoire; j++){
					int deltaj = j - demi_taille_buffer;
					int j_terrain = this.centre_buffer_auxiliaire_j +  deltaj;
					
					if(i_terrain>=0 && i_terrain<=Tuile.PX-1 && j_terrain>=0 && j_terrain<=Tuile.PY ){
						buffer_tuile.get(i).set(j,new Tuile(i_terrain, j_terrain, Tuile.R40, Tuile.DB) );
					}
					else{
						buffer_tuile.get(i).set(j,null);
					}
					
				}
		}
			
	}
	
	/**
	 * Permet de remplir le Buffer Memoire de Tuiles auxiliaire
	 * @throws IOException 
	 */
	public void remplissage_Buffer_Auxiliaire(ArrayList<ArrayList<SuperBG>> buffaux) throws IOException{
		
		for (int i = 0; i< taille_buffer_memoire; i++){
			for(int j = 0; j< taille_buffer_memoire; j++){
			
				buffaux.get(i).set(j, new SuperBG(buffer_tuile.get(i).get(j),
			
						buffer_objet.get(i).get(j), buffer_bati.get(i).get(j) ));
				
				//Liberation mémoire
				buffer_tuile.get(i).set(j, null);
				buffer_objet.get(i).set(j, new ArrayList<Object>());
				buffer_bati.get(i).set(j, new ArrayList<Batiment>());
				
			}
		}
		
	}
	
	public void swap(){
		
		world.getCanvas().removeMouseListener(world.getListeners());
		world.getCanvas().removeMouseMotionListener(world.getListeners());
		world.getCanvas().removeMouseWheelListener(world.getListeners());
		world.getCanvas().removeKeyListener(world.getListeners());
		
		//Refenetrage du buffer_memoire autour du buffer_auxiliaire
		this.centre_buffer_memoire_i = this.centre_buffer_auxiliaire_i;
		this.centre_buffer_memoire_j = this.centre_buffer_auxiliaire_j;

		for (int i = 0; i< taille_buffer_memoire; i++){
			for(int j = 0; j< taille_buffer_memoire; j++){
			
				buffer_memoire.get(i).set(j, new SuperBG(buffer_auxiliaire.get(i).get(j)));
				buffer_auxiliaire.get(i).set(j,null);
			}
		}

		world.getCanvas().addMouseListener(world.getListeners());
		world.getCanvas().addMouseMotionListener(world.getListeners());
		world.getCanvas().addMouseWheelListener(world.getListeners());
		world.getCanvas().addKeyListener(world.getListeners());
		
		
	}
	
	/**
	 * Met à disposition le visible
	 * @throws IOException 
	 */
	public void initialisation_buffer_visible(ArrayList<ArrayList<SuperBG>> buffmem) {
		System.out.println("init buffer visible");
	
		
		for (int i = 0; i< taille_buffer_visible; i++){
			for(int j = 0; j< taille_buffer_visible; j++){
				
				buffer_visible.get(i).set(j, copieAttache_SuperBG(i, j, buffmem));	
			}
		}
	}
		
		/**
		 * Met à jour le visible sur déplacement de la caméra
		 * Recharge le buffer en memoire sur condition de deplacement
		 * @throws IOException 
		 */
		public void rafraichissement_visible(int delta_i, int delta_j) throws IOException {
			int pas_i;
			int pas_j;
			
			
			System.out.println("Centre visible :"+centre_buffer_visible_i+" "+centre_buffer_visible_j+
								" Centre memoire :"+centre_buffer_memoire_i+" "+centre_buffer_memoire_j+
								" Rafraichissement du buffer visible : "+delta_i+":"+delta_j);

			
			if ( delta_i !=0  ){
				
				pas_i = (delta_i > 0 ? 1 : -1);
				//Calcul des determinants
				int cas1 = (1 + pas_i)/2;
				int cas2 = (1 - pas_i)/2;
				
				int i_detach = cas2 * (taille_buffer_visible - 1 ); 
				int i_transfert = cas1 * (taille_buffer_visible - 1 ); 
				
				for(int k=0; k<Math.abs(delta_i); k++){
					this.centre_buffer_visible_i+=pas_i;
		
					for ( int j = 0; j < taille_buffer_visible; j++ ){
						//System.out.println("Mvmt horizontal, A detacher : "+i_detach+"/"+j);
						
						//Detachement des SuperBG qui sortent de la zone visible
						buffer_visible.get(i_detach).get(j).sbg.detach();
						
						//Re-indexation des SuperBG à conserver
						for ( int i = cas2 * ( taille_buffer_visible - 1 ) ; i*pas_i <= cas1*(taille_buffer_visible-2)+ cas2*(-1) ; i+=pas_i){
							//System.out.println("\tRe-indexation : "+(i+delta_i)+"/"+j+" -> "+i+"/"+j);
							buffer_visible.get(i).set(j, buffer_visible.get(i+pas_i).get(j));		
						}
						
						//Copie et attachement de nouveaux SuperBG à partir du buffer_memoire
						//System.out.println("A attacher : "+i_transfert+"/"+j);
						buffer_visible.get(i_transfert).set(j, copieAttache_SuperBG(i_transfert, j, this.buffer_memoire));				
					}
				}
				
			}
			if (delta_j != 0){
				
				pas_j = (delta_j > 0 ? 1 : -1);
				
				//Calcul des determinants
				int cas3 = (1 + pas_j )/2;
				int cas4 = (1 - pas_j )/2;
				
				int j_detach = cas4 * (taille_buffer_visible - 1 ); 
				int j_transfert = cas3 * (taille_buffer_visible -1);
				
				for(int k=0; k<Math.abs(delta_j); k++){
					this.centre_buffer_visible_j+=pas_j;
					
					for ( int i = 0; i < taille_buffer_visible; i++ ){
						//Detachement des SuperBG qui sortent de la zone visible
						//System.out.println("Mvmt vertical, A detacher : "+i+"/"+j_detach);
						buffer_visible.get(i).get(j_detach).sbg.detach();
						
						//Re-indexation des SuperBG à conserver
						for (int j = cas4 * ( taille_buffer_visible - 1 ) ; j*pas_j <= cas3 *(taille_buffer_visible-2)+cas4*(-1); j+=pas_j){
							//System.out.println("\tRe-indexation : "+i+"/"+(j+delta_j)+" -> "+i+"/"+j);
							buffer_visible.get(i).set(j, buffer_visible.get(i).get(j+pas_j));		
							
						}
						
						//Copie et attachement de nouveaux SuperBG à partir du buffer_memoire
						//System.out.println("A attacher : "+i+"/"+j_transfert);
						buffer_visible.get(i).set(j_transfert, copieAttache_SuperBG(i, j_transfert,this.buffer_memoire));
					}
				}
				
			}

			
			
			//Rafraichissement du buffer si requis
			
			//Test si procesus de rechargement en cours
			if (t != null){
				//si le buffer est en cours de mise à jour; empêche le rechargement
				if (t.isAlive()){return;}
			}
			
			//Condition de rechargement basé sur l'attribut marge	
			if(Math.abs(centre_buffer_visible_i-centre_buffer_memoire_i)>=this.marge || Math.abs(centre_buffer_visible_j-centre_buffer_memoire_j)>=this.marge){
				
				 t = new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							rechargement_buffer_memoire();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				System.out.println("etat ante rechargement: "+t.getState());

				t.start();
				System.out.println(t.getState());

			}
		}
			
			
			//Affectaction des SuperBG à attacher
			/**
			 * copie_et_attache_SuperBG_depuis_buffer_memoire_vers visible
			 */
			public SuperBG copieAttache_SuperBG(int i, int j, ArrayList<ArrayList<SuperBG>> buffmem) {
				
				int ecart = (taille_buffer_memoire - taille_buffer_visible ) / 2 ;
				int i_memoire = i + ecart + centre_buffer_visible_i - centre_buffer_memoire_i;
				int j_memoire = j + ecart + centre_buffer_visible_j - centre_buffer_memoire_j;	
				//System.out.println("etat avant copi: "+buffmem.get(i_memoire).get(j_memoire).sbg.isLive()+" "+buffmem.get(i_memoire).get(j_memoire).sbg);
				SuperBG copie_sbg = new SuperBG(buffmem.get(i_memoire).get(j_memoire));
				tg.addChild(copie_sbg.sbg);
				//System.out.println("Etat : "+copie_sbg.sbg.isLive()+" "+copie_sbg.sbg);

				return copie_sbg;
			}
			
			public void rechargement_buffer_memoire() throws IOException{
			
				System.out.println("XXXXXXXXXXXXXXXXXXXXXRechargement BUFFERXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				//Refenetrage du buffer_memoire autour du buffer_auxiliaire
				centre_buffer_auxiliaire_i=centre_buffer_visible_i;
				centre_buffer_auxiliaire_j=centre_buffer_visible_j;

				
//				//Ajout des Objets
//				//GenericDAO.selection_geographique_buffer(this);
//				
//				 t_select = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						selection_Objet();
//					}
//				});
//				 
//				 
//				t_select.start();
//				
//				
//				
//				
//				
//				System.out.println("fin rafraichissement des objets");
//				//Ajout des Tuiles
//				remplissage_Buffer_Tuile();
//				System.out.println("fin rafraichissement des tuiles");
				
				
				//Thread Ajout des Objets
				
				 t_select = new Thread(new Runnable() {
					@Override
					public void run() {
						selection_Objet();
					}
				 });
				 
				//Thread Ajout du bati
				 
				 t_bati = new Thread(new Runnable() {
						@Override
						public void run() {
							selection_Bati();
						}
					 });
				 
				//Thread Ajout du tuilage

				 t_tuile = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								remplissage_Buffer_Tuile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					 });
				 
				double time = System.currentTimeMillis();
				
				t_select.start();
				t_bati.start();
				t_tuile.start();
				
				
				//Attendre completion des threads de selections BDD
				//avant MAJ du buffer auxilliaire
				try {
					t_select.join();
					t_bati.join();
					t_tuile.join();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Temps des selections d'objets (tuile/bati/objet) : "+(System.currentTimeMillis()-time));

				//Fin Join(); thread terminated

				
				
				//remplissage du Buffer auxiliaire
				//if (t_select.isAlive() == false ){
				remplissage_Buffer_Auxiliaire(this.buffer_auxiliaire);
				//transfert du contenu du buffer auxiliaire vers le buffer mémoire
		
				swap();

				System.out.println("XXXXXXXXXXXXXXXXXXXXXFIN Rechargement BUFFERXXXXXXXXXXXXXXXXXXXXXXXX");
				
			}
				

			
			public void selection_Objet(){
				//Ajout des Objets
				GenericDAO.selection_geographique_buffer(this);
				System.out.println("fin rafraichissement des objets");
			}
			
			public void selection_Bati(){
				//Ajout des Objets
				GenericDAO.selection_geographique_bati_buffer(this);
				System.out.println("fin rafraichissement du bati");
			}
			
			
	
		
		
	
	
}
