package iasig.dao.user;

import iasig.dao.GenericDAO;
import iasig.univers.view.Tuile;

import java.util.Vector;


/**
 * Classe "legacy" - Classe prototype du buffer - Obsolete
 */
public class Conteneur_objet {

	/**
	 * le vecteur de vecteur, portant les objets indexes selon leur maille d'appartenance
	 */
	private Vector<Vector<Object>> objets_instanciated = new Vector<Vector<Object>>();
	/**
	 * la matrice indexant les elements en memoire en reference au maillage
	 */
	//La taille du Buffer Memoire
	private int parametre_de_generation;
	//La dimension metrique d'une maille
	private int interval_de_maille;
	//les indices de la maille centrale fonction de la taille du buffer memoire
	public int centre_relatif;
	//La matrice indexant le vecteur de de vecteur d'objet relativement à la taille choisie du buffer memoire
	private int[][] matrice_indexation;
	private int demi_largeur_maillage_memoire;
	//Critere de rechargement du buffer
	int limite_rechargement; 

	
	//L'objet Buffer porte en attribut les indices de la maille observateur au moment du chargement des objets à
	//partir de la lecture de la BDD
	public int mailleobservateur_i;
	public int mailleobservateur_j;
	
	//La largeur de maille de l'espace visible
	public int dimension_espace_visible;
	//Un vecteur de int[] pour calcul des mailles de l'espaces visible (relatif->monde et vice versa)
	public Vector<int[]> embryon_buffer_visible = new Vector<int[]>();
	
	//un attribut de contrôle
	private int nombre_objets;
	
	
	/**
	 * Constructeur 1 - Vide
	 */
	public Conteneur_objet() {
		// RIEN
	}
	
	/**
	 * Constructeur 2 - contruit un 2d array pour le stockage des objets
	 * A la construction le Buffer des objets en memoire s'initialise automatiquement et met à disposition les objets. 
	 * @param parametre_de_generation - entier, l'interval de maille pris en memoire (identique selon les deux dimensions)
	 * Il est possible de maintenir des buffers distinct pour differents points de vue.
	 * @param Xinit le point d'entree dans le monde
	 * @param Yinit le point d'entree dans le monde
	 * Le pas du maillage doit pouvoir être modifie pour permettre de travailler apres une modification de ce parametres sur les objets de a BDD
	 * @param interval_de_maille le pas du maillage
	 * @param dimension_espace_visible la dimension en maille de l'espace "visible" (les objets precharge dans le buffer d'affichage)
	 */
	public Conteneur_objet(int parametre_de_generation, float Xinit, float Yinit, int interval_de_maille, int dimension_espace_visible ){
		this();

		//blindage - le parametre doit être impair
		if (parametre_de_generation%2 == 0){
			parametre_de_generation++;
		}
		//inscription des attributs; initialisation des tableaux et des listes
		this.parametre_de_generation = parametre_de_generation;
		this.dimension_espace_visible = dimension_espace_visible;
		this.centre_relatif = (int)(parametre_de_generation/2);
		this.demi_largeur_maillage_memoire = (int)(parametre_de_generation/2);
		this.interval_de_maille = interval_de_maille;
		this.limite_rechargement =  demi_espace_memoire_maille() - (int) ( dimension_espace_visible/2 ) ;
		//creation de la matrice 2D; Representant le maillage relatif au point d'entree fixe dans la maille centrale.
		this.matrice_indexation = new int[parametre_de_generation][parametre_de_generation];
		//creation indexation du vecteur de vecteur d'objet
		//initialisation de la variable de remplissage "index"
		int index = 0;
		for (int i = 0; i < parametre_de_generation; i++ ){
			for (int j = 0; j < parametre_de_generation; j++ ){
				//TODO faire verifier l'indexation
				this.matrice_indexation[i][j] = index;
				//System.out.println(index);
				index++;
				
				this.objets_instanciated.add(new Vector<Object>());	
			}
		}
		//Creation du la liste embryon pour buffer affichage
		
		for (int j =0 ;j < this.dimension_espace_visible; j++ ){
			int l = -1 * (int)((this.dimension_espace_visible)/2);
			int m = -1 * (int)((this.dimension_espace_visible)/2) + j;
			for (int k =0 ;k < this.dimension_espace_visible; k++ ){
				//int[] maill = {l, m};
				int[] maill = { m,l};
							l = l + 1;
				this.embryon_buffer_visible.add(maill);
			}
		}
		System.out.println("Selection Geographique Init");
		GenericDAO.selection_geographique_init(this, Xinit, Yinit, interval_de_maille);
	}
	
	//Accesseurs et setters	
	/**
	 * Permet de mettre à jour la maille_observateur courante à la MAJ de l'objet 
	 * @param obj Un objet
	 */
	public void set_Maille_Observateur(int i, int j){
		this.mailleobservateur_i = i; 
		this.mailleobservateur_j = j;
	}
	
	
	/**
	 * Permet de mettre à jour la dimension de l'espace visible  
	 * @param espace_visible entier, la dimension de l'espace visible en maille
	 */
	public void set_Taille_Buffer(int espace_visible){
		this.dimension_espace_visible = espace_visible;
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
//			int deltai = ((Maison) obj).getMaille_i() - this.mailleobservateur_i;
//			int deltaj =((Maison) obj).getMaille_j() - this.mailleobservateur_j;
			int deltai =  this.mailleobservateur_i-((Maison) obj).getMaille_i() ;
			int deltaj = this.mailleobservateur_j-((Maison) obj).getMaille_j() ;
			this.objets_instanciated.elementAt(this.matrice_indexation[deltai + this.centre_relatif][deltaj + this.centre_relatif]).add(obj);
			nombre_objets++;
		}
		
		else if(obj instanceof Lampadaire){
//			int deltai = ((Lampadaire) obj).getMaille_i() - this.mailleobservateur_i;
//			int deltaj =((Lampadaire) obj).getMaille_j() - this.mailleobservateur_j;
			int deltai =  this.mailleobservateur_i-((Lampadaire) obj).getMaille_i() ;
			int deltaj = this.mailleobservateur_j-((Lampadaire) obj).getMaille_j() ;
			this.objets_instanciated.elementAt(this.matrice_indexation[deltai + this.centre_relatif][deltaj + this.centre_relatif]).add(obj);
			nombre_objets++;
		}	

		else if(obj instanceof Arbre){
//			int deltai = ((Arbre) obj).getMaille_i() - this.mailleobservateur_i;
//			int deltaj =((Arbre) obj).getMaille_j() - this.mailleobservateur_j;
			int deltai =  this.mailleobservateur_i-((Arbre) obj).getMaille_i() ;
			int deltaj = this.mailleobservateur_j-((Arbre) obj).getMaille_j() ;
			this.objets_instanciated.elementAt(this.matrice_indexation[deltai + this.centre_relatif][deltaj + this.centre_relatif]).add(obj);
			nombre_objets++;
		}	
	}
	
	/**
	 * Permet de recuperer le parametre de generation de l'espace à mettre en memoire
	 * valeur en nombre de maille
	 * @param obj Un objet
	 */
	public int get_parametre_maille(){
		return this.parametre_de_generation;
	}
	
	/**
	 * Permet de recuperer la largeur de l'interval de l'espace à mettre en memoire 
	 * de chaque côte de la maille centrale.
	 * valeur en nombre de maille
	 * @param obj Un objet
	 */
	public int demi_espace_memoire_maille(){
		return this.demi_largeur_maillage_memoire;
	}
	
	
	/**
	 * Retourne le vecteur stockant tous les elements d'une maille disponible en memoire
	 */
	public Vector<Object> getElement_parMaille(int maille_i, int maille_j) {
		
		//Passage en reference relative	
		Integer Relatif_i = maille_i - this.mailleobservateur_i + this.centre_relatif;
		Integer Relatif_j = maille_j - this.mailleobservateur_j + this.centre_relatif;
		//Copie
		return this.objets_instanciated.elementAt(this.matrice_indexation[Relatif_i][Relatif_j]);
	}
	/**
	 * Retourne le nombre d'objets disponibles en memoire
	 */
	public   int NbreObjets(){
		return this.nombre_objets;
	}
	
	/**
	 * @return the objets_instanciated
	 */
	public Vector<Vector<Object>> getVectObj() {
		return objets_instanciated;
	}

	/**
	 * Calcul du numero d'une maille dans le buffer d'eqfses coordonnees geographiques
	 *  
	 * @param i coordonnees geographique de la maille
	 * @param j coordonnees geographique de la maille
	 * @return nummaille
	 */
	public int getNumMaille(float i, float j) {

		int deltai=(int) (this.mailleobservateur_i-i);
		int deltaj=(int) (this.mailleobservateur_j-j); 
		
		int pos_reli = this.centre_relatif + deltai;
		int pos_relj = this.centre_relatif + deltaj;
System.out.println(mailleobservateur_i +"  "+ mailleobservateur_j +" / "+ deltai +"  "+ deltaj);			
System.out.println(" i-j: "+ i + "  "+ j);

System.out.println(" crel: "+ centre_relatif);

System.out.println(pos_reli +"  "+ pos_relj);			
		int nummaille = matrice_indexation[pos_reli][pos_relj];
System.out.println("nummaille="+nummaille);
	
		return nummaille;
	}
	
	
	/**
	 * Permet de recuperer tous les objets situes dans une(ou plusieurs) mailles via une liste_de_maille
	 * @param liste_de_maille	vector[int] representant la liste des mailles à interroger
	 */
	public Vector<Vector<Object>> getObjet_par_maille(Vector<int[]> liste_de_maille){
		
		Vector<Vector<Object>> tmp = new Vector<Vector<Object>>();
		//transformation du maillage vers coordonnees relatives et extraction du vecteur coorespondant
		for (int i = 0; i< liste_de_maille.size(); i++){
//			System.out.print(niveau.elementAt(i)[0] - this.mailleobservateur_i + this.centre_relatif + " ");
//			System.out.print(niveau.elementAt(i)[1] - this.mailleobservateur_j + this.centre_relatif + " ");
//			System.out.println(matrice_indexation[niveau.elementAt(i)[0] - this.mailleobservateur_i + this.centre_relatif][niveau.elementAt(i)[1] - this.mailleobservateur_j + this.centre_relatif]);
//			System.out.println(this.objets_instanciated.size());
			tmp.add(this.objets_instanciated.elementAt(matrice_indexation[liste_de_maille.elementAt(i)[0] - this.mailleobservateur_i + this.centre_relatif][liste_de_maille.elementAt(i)[1] - this.mailleobservateur_j + this.centre_relatif]));

		}
		return tmp;
	}
	
	/**
	 * Permet de conversion coordonnees monde vers coordonnees mailee
	 * @param Xobs	coordonnees monde
	 * @param Yobs	coordonnees monde
	 * @return nummaille les coordonnees maile
	 */
	public int[] Conversion_Terrain_Maillage(float Xobs, float Yobs){
		int[] tmp = new int[2];
		
		tmp[0] = (int)((Xobs-Tuile.Xmin)/Tuile.DX);
		tmp[1] = (int)((Yobs-Tuile.Ymin)/Tuile.DY);
		return tmp;
		
	}

	
	/**
	 * Permet de conversion indice d'indexation vers coordonnees maille monde
	 * @param indice l'indice
	 * @return tmp maille les coordonnees maille
	 */
	public int[] Conversion_Indice_Monde(int indice){
		
		int[] tmp = new int[2];
		
		tmp[1] = indice/interval_de_maille;
		tmp[0] = indice%interval_de_maille;
		
		return tmp;
		
	}
	
	/**
	 * Permet de conversion coordonnees maille vers coordonnees relatives buffer
	 * @param Xobs	coordonnees maille
	 * @param Yobs	coordonnees maille
	 * @return nummaille les coordonnees relative
	 */
	public int[] Conversion_Maille_Buffer(int i , int j){
		int[] tmp = new int[2];
		
		tmp[0] = (mailleobservateur_i - i) + centre_relatif ;
		tmp[1] = (mailleobservateur_j - j) + centre_relatif ;
		return tmp;
		
	}
	
	
	/**
	 * Permet de recuperer tous les objets situes selon l'embryon de buffer visible
	 * @param i	entier maille i
	 * @param j	entier maille j
	 */
	public Vector<Vector<Object>> getObjet_Visible(int i,int j){
		
		System.out.println("getObjet-Visible");
		Vector<Vector<Object>> tmp = new Vector<Vector<Object>>();
		
		int[] tmp2 = Conversion_Maille_Buffer(i,j);
		
		for (int k = 0; k < this.embryon_buffer_visible.size(); k++){
			int[] tmp4 = {this.embryon_buffer_visible.elementAt(k)[0] + tmp2[0], this.embryon_buffer_visible.elementAt(k)[1] + tmp2[1] };
			//System.out.println("tmp4 : "+tmp4[0]+" / "+tmp4[1]);
			int tmp5 = matrice_indexation[tmp4[0]][tmp4[1]];
			tmp.add(this.objets_instanciated.elementAt(tmp5));
			
		}

		return tmp;
	}
	
	
	/**
	 * Permet de vider le buffer d'objet en conservant la taille (nbre d'element) du conteneur
	 */
	public void vide_Objet_en_memoire(){
		
		for (int i =0; i < this.objets_instanciated.size(); i++)
				this.objets_instanciated.elementAt(i).clear();
	
			this.nombre_objets = 0;
		}
			


	/**
	 * Retourne la distance limite, en maille, au delà de laquelle on doit recharger le buffer
	 */
	public int getLimiteRechargement() {
		return limite_rechargement;
		
	}
	
	
}
