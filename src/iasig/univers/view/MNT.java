package iasig.univers.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * @author Jean
 *
 */
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
public class MNT {
	/** Cette classe definit un MNT sous forme matricielle (grille d'altitude), avec ses informations de georeferencement.*/	
	
	/*/////////////////////ATTRIBUTS////////////////////*/
	private int NX; //Nombre de colonnes
	private int NY; //Nombre de lignes
	private int Xmin; //Easting minimal
	private int Xmax; //Easting maximal
	private int DX; //Pas en X entre chaque point mesure (resolution en X)
	private int Ymin; //Northing minimal
	private int Ymax; //Northing maximal
	private int DY; //Pas en Y entre chaque point mesure (resolution en Y)
	private int Zmin; //Altitude minimale
	private int Zmax; //Altitude maximale
	
	public int maille_i;
	public int maille_j;
	
	private int[][] M; //Matrice des altitudes, le premier point est le coin Nord-Ouest de la grille (Xmin,Ymax)
	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/********************CONSTRUCTEURS*******************/
		
	public MNT(){};
	
	
	

	
	//---------------------------------------------------
	public MNT(String fich_entree) throws IOException {
		/** 
		 * FONCTION :
		 * Charger un MNT a partir d'un fichier csv.
		 * Ce fichier doit respecter la forme suivante :
		 * ---------------------------
		 * NX NY
		 * Xmin Xmax DX
		 * Ymin Ymax DY
		 * Zmin Zmax
		 * <matrice des valeurs, NX en colonne, NY en ligne, a partir du coin Nord-Ouest>
		 * ---------------------------
		 * 
		 * PARAMETRE EN ENTREE :
		 * - fich_entree [Scalaire, cha�ne de caracteres] : chemin du fichier du MNT a charger.
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT, comportant les memes informations que dans le fichier.
		 * */
		
		Scanner flux = new Scanner(new File(fich_entree)); //Flux de lecture
		
		NX = flux.nextInt(); //Prend le premier entier dans le fichier..
		NY = flux.nextInt(); //Le deuxieme...
		Xmin = flux.nextInt(); //Etc.
		Xmax = flux.nextInt();
		DX = flux.nextInt();
		Ymin = flux.nextInt();
		Ymax = flux.nextInt();
		DY = flux.nextInt();
		Zmin = flux.nextInt();
		Zmax = flux.nextInt();
		
		M = new int[NY][NX]; //Allocation de la matrice des valeurs
		
		for(int i=1;i<=NY;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=1;j<=NX;j++){ //Indices des colonnes (varie d'Ouest en Est)
				M[i-1][j-1]=flux.nextInt(); //Lecture de l'altitude (i,j)
			}
		}
		
		flux.close();
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public MNT(MNT mnt, int xmin, int ymin, int xmax, int ymax){
		/** 
		 * FONCTION :
		 * Construire un MNT a partir d'un autre objet MNT plus grand.
		 * Le decoupage se fait entre des bornes definies.
		 * La resolution est inchangee.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - mnt [Scalaire, objet de la classe MNT] : MNT source.
		 * - xmin [Scalaire, entier] : Borne Ouest.
		 * - ymin [Scalaire, entier] : Borne Sud.
		 * - xmax [Scalaire, entier] : Borne Est.
		 * - ymax [Scalaire, entier] : Borne Ouest.
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT, a la meme resolution que le MNT source.
		 * */
		
		//Renseignement des nouvelles bornes
		Xmin=xmin;
		Ymin=ymin;
		Xmax=xmax;
		Ymax=ymax;
		
		//La resolution est inchangee
		DX=mnt.getDX();
		DY=mnt.getDY();
		
		//Calcul des indices limites entre lesquels recuperer les valeurs sur la grille
		int imin=(mnt.getYmax()-Ymax)/DY + 1;
		int imax=(mnt.getYmax()-Ymin)/DY + 1;
		int jmin=(Xmin-mnt.getXmin())/DX + 1;
		int jmax=(Xmax-mnt.getXmin())/DX + 1;
		
		//Renseignement de la taille du MNT construit
		NX = jmax-jmin+1;
		NY = imax-imin+1;
		
		M=new int[NY][NX]; //Allocation de la matrice des valeurs
		
		//Initialisation des altitudes minimale et maximale
		Zmin=mnt.getZAt(imin,jmin);
		Zmax=mnt.getZAt(imin,jmin);
		
		int Z;
		
		//Cette double boucle :
		//-Remplit la matrice des valeurs du MNT construite a partir de celle du MNT source
		//-Recherche les altitudes minimale et maximale du MNT construit
		for(int i=imin;i<=imax;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=jmin;j<=jmax;j++){ //Indices des colonnes (varie d'Ouest en Est)
				Z=mnt.getZAt(i-1,j-1); //Recuperation de Z(i,j)
				M[i-imin][j-jmin]=Z; //Ajout dans la matrice
				
				if(Z<Zmin){
					Zmin=Z; //Actualisation de Zmin
				}
				else if(Z>Zmax){
					Zmax=Z; //Actualisation de Zmax
				}
				else{}
			}
		}
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public MNT(MNT mnt, int dx, int dy){
		/** 
		 * FONCTION :
		 * Construire un MNT par sous-echantillonage d'un MNT plus resolu.
		 * La nouvelle resolution est indiquee.
		 * Les bornes sont inchangees.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - mnt [Scalaire, objet de la classe MNT] : MNT source.
		 * - dx [Scalaire, entier] : Resolution en X souhaitee, doit etre multiple de mnt.DX.
		 * - dy [Scalaire, entier] : Resolution en Y souhaitee, doit etre multiple de mnt.DY.
		 *  
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT a resolution moindre, ayant la meme emprise que le MNT source.
		 * */
		
		//Meme emprise
		Xmin=mnt.getXmin();
		Ymin=mnt.getYmin();
		Xmax=mnt.getXmax();
		Ymax=mnt.getYmax();
		
		//Recuperation des resolutions en X et en Y
		DX=dx;
		DY=dy;
		
		//Rapport des resolutions
		int FX = dx/mnt.getDX();
		int FY = dx/mnt.getDY();
		
		//Calcul du nouveau nombre d'elements en colonne et en ligne
		NX=(mnt.getNX()-1)/FX+1;
		NY=(mnt.getNY()-1)/FY+1;
		
		M=new int[NY][NX]; //Allocation de la grille des altitudes
		
		//Initialisation des altitudes minimale et maximale
		Zmin=mnt.getZAt(0,0); 
		Zmax=mnt.getZAt(0,0);
		
		int Z;
		
		//Cette double boucle :
		//-Remplit la matrice des valeurs du MNT construite a partir de celle du MNT source
		//-Recherche les altitudes minimale et maximale du MNT construit
		for(int i=1;i<=NY;i++){
			for(int j=1;j<=NX;j++){
				
				//On prend un point tous les (FX,FY) du MNT source
				Z=mnt.getZAt((i-1)*FY,(j-1)*FX);
				M[i-1][j-1]=Z;
				
				if(Z<Zmin){
					Zmin=Z; //Actualisation de Zmin
				}
				else if(Z>Zmax){
					Zmax=Z; //Actualisation de Zmax
				}
				else{}
			}
		}
	}
	//---------------------------------------------------

	//---------------------------------------------------
	public MNT(int nx, int ny, int xmin, int xmax, int dx, int ymin, int ymax, int dy, int zmin, int zmax, int[][] m){
		/** 
		 * FONCTION :
		 * Construire un MNT par lecture systematique des attributs fournis en entree.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - nx [Scalaire, entier] : valeur a attribuer a NX.
		 * - ny [Scalaire, entier] : valeur a attribuer a NY.
		 * - xmin [Scalaire, entier] : valeur a attribuer a Xmin.
		 * - xmax [Scalaire, entier] : valeur a attribuer a Xmax.
		 * - dx [Scalaire, entier] : valeur a attribuer a DX.
		 * - ymin [Scalaire, entier] : valeur a attribuer a Ymin.
		 * - ymax [Scalaire, entier] : valeur a attribuer a Ymax.
		 * - dy [Scalaire, entier] : valeur a attribuer a DY.
		 * - zmin [Scalaire, entier] : valeur a attribuer a Zmin.
		 * - zmax [Scalaire, entier] : valeur a attribuer a Zmax.
		 * - m [Matrice[NY lignes, NX colonnes] : valeur a attribuer a M.
		 *  
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT avec les valeurs d'attributs calques sur ceux fournis en entree.
		 * */
		
		NX=nx;
		NY=ny;
		Xmin=xmin;
		Xmax=xmax;
		DX=dx;
		Ymin=ymin;
		Ymax=ymax;
		DY=dy;
		Zmin=zmin;
		Zmax=zmax;
		M=m;	
	}
	//---------------------------------------------------
	
	
	public MNT(int nx, int ny, int xmin, int xmax, int dx, int ymin, int ymax, int dy, int zmin, int zmax, int[][] m, int maillei, int maillej){
		/** 
		 * FONCTION :
		 * Construire un MNT par lecture systematique des attributs fournis en entree.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - nx [Scalaire, entier] : valeur a attribuer a NX.
		 * - ny [Scalaire, entier] : valeur a attribuer a NY.
		 * - xmin [Scalaire, entier] : valeur a attribuer a Xmin.
		 * - xmax [Scalaire, entier] : valeur a attribuer a Xmax.
		 * - dx [Scalaire, entier] : valeur a attribuer a DX.
		 * - ymin [Scalaire, entier] : valeur a attribuer a Ymin.
		 * - ymax [Scalaire, entier] : valeur a attribuer a Ymax.
		 * - dy [Scalaire, entier] : valeur a attribuer a DY.
		 * - zmin [Scalaire, entier] : valeur a attribuer a Zmin.
		 * - zmax [Scalaire, entier] : valeur a attribuer a Zmax.
		 * - m [Matrice[NY lignes, NX colonnes] : valeur a attribuer a M.
		 *  
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT avec les valeurs d'attributs calques sur ceux fournis en entree.
		 * */
		
		NX=nx;
		NY=ny;
		Xmin=xmin;
		Xmax=xmax;
		DX=dx;
		Ymin=ymin;
		Ymax=ymax;
		DY=dy;
		Zmin=zmin;
		Zmax=zmax;
		M=m;	
		maille_i=maillei;
		maille_j=maillej;
	}
	//---------------------------------------------------
	
	
	/****************************************************/
	
	
	
	/***********************GETTERS**********************/
	
	//---------------------------------------------------
	public int getXmin(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private Xmin.
		 * -
		 * RETOUR :
		 * - [Scalaire, entier] : this.Xmin.
		 */
		return Xmin;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getYmin(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private Ymin.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.Ymin.
		 */
		return Ymin;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getXmax(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private Xmax.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.Xmax.
		 */
		return Xmax;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getYmax(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private Ymax.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.Ymax.
		 */
		return Ymax;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getXmean(){
		/** 
		 * FONCTION :
		 * Calculer et recuperer l'Easting moyen du MNT.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : X moyen du MNT.
		 */
		return (Xmin+Xmax)/2;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getYmean(){
		/** 
		 * FONCTION :
		 * Calculer et recuperer le Northing moyen du MNT.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : Y moyen du MNT.
		 */
		return (Ymin+Ymax)/2;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getZmean(){
		/** 
		 * FONCTION :
		 * Calculer et recuperer l'altitude moyenne du MNT.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : Z moyen du MNT.
		 */
		
		//Z moyen = somme(somme(M(i,j), i variant de 1 a NY), j variant de 1 a NX) / (NX*NY)  
		int Z=0;
		for(int i=1;i<=NY;i++){
			for(int j=1;j<=NX;j++){
				Z+=M[i-1][j-1];
			}
		}
		Z/=NX*NY;
		return Z;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getDX(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private DX.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.DX.
		 */
		return DX;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getDY(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private DY.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.DY.
		 */
		return DY;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getNX(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private NX.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.NX.
		 */
		return NX;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getNY(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private NX.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.NX.
		 */
		return NY;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getEX(){
		/** 
		 * FONCTION :
		 * Calculer et recuperer l'emprise en Easting du MNT.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : emprise en X du MNT.
		 */
		return Xmax-Xmin;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getEY(){
		/** 
		 * FONCTION :
		 * Calculer et recuperer l'emprise en Northing du MNT.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : emprise en Y du MNT.
		 */
		return Ymax-Ymin;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getZAt(int i, int j){
		/** 
		 * FONCTION :
		 * Recuperer une certaine altitude dans la matrice M (attribut private)
		 * 
		 * PARAMETRES EN ENTREE :
		 * - i [Scalaire, entier] : indice de ligne (attention, la numerotation commence a zero).
		 * - j [Scalaire, entier] : indice de colonne (attention, la numerotation commence a zero).
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : altitude en (i+1,j+1).
		 */
		return M[i][j];
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	
	
	/*******************AUTRES METHODES******************/
	
	//---------------------------------------------------
	public void generate_mat(String fich_sortie) throws IOException{
		/** 
		 * FONCTION :
		 * Ecrire le MNT dans un fichier.
		 * Ce fichier aura la forme suivante :
		 * ---------------------------
		 * NX NY
		 * Xmin Xmax DX
		 * Ymin Ymax DY
		 * Zmin Zmax
		 * <matrice des valeurs, NX en colonne, NY en ligne, a partir du coin Nord-Ouest>
		 * ---------------------------
		 * 
		 * PARAMETRE EN ENTREE :
		 * - fich_sortie [Scalaire, cha�ne de caracteres] : chemin du fichier dans lequel ecrire. Si le fichier existe deja, il est ecrase.
		 */
		
		PrintWriter flux = new PrintWriter(new File(fich_sortie)); //Flux d'ecriture
		
		flux.print(NX); //Ecrire la valeur de NX...
		flux.print(" "); //Ecrire un blanc...
		flux.print(NY); //Etc.
		flux.println();
		flux.print(Xmin);
		flux.print(" ");
		flux.print(Xmax);
		flux.print(" ");
		flux.print(DX);
		flux.print(" ");
		flux.println();
		flux.print(Ymin);
		flux.print(" ");
		flux.print(Ymax);
		flux.print(" ");
		flux.print(DY);
		flux.println();
		flux.print(Zmin);
		flux.print(" ");
		flux.print(Zmax);
		flux.println();
		
		for(int i=1;i<=NY;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=1;j<=NX;j++){ //Indices des colonnes (varie d'Ouest en Est)
				flux.print(M[i-1][j-1]);
				flux.print(" ");
			}
			flux.println();
		}
		flux.close();
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public void generate_csv(String fich_sortie) throws IOException{
		/** 
		 * FONCTION :
		 * Ecrire le MNT dans un fichier csv.
		 * Ce fichier aura la forme suivante :
		 * ---------------------------
		 * ID X Y Z      <= En-tete
		 * ID1 X1 Y1 Z1
		 * ID2 X2 Y2 Z2
		 * ID3 X3 Y3 Z3
		 * ...
		 * ---------------------------
		 * Le premier point indique correspond au coin Nord-Ouest. Puis le balayage se fait d'abord en X, puis en Y.
		 * 
		 * PARAMETRE EN ENTREE :
		 * - fich_sortie [Scalaire, cha�ne de caracteres] : chemin du fichier dans lequel ecrire. Si le fichier existe deja, il est ecrase.
		 */
		
		PrintWriter flux = new PrintWriter(new File(fich_sortie)); //Flux d'ecriture
		
		int k=1;
		
		flux.println("ID X Y Z"); //En-tete
		
		for(int i=1;i<=NY;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=1;j<=NX;j++){ //Indices des colonnes (varie d'Ouest en Est)
				flux.print(k); //ID
				flux.print(" ");
				flux.print(Xmin+(j-1)*DX); //X
				flux.print(" ");
				flux.print(Ymax-(i-1)*DY); //Y
				flux.print(" ");
				flux.print(M[i-1][j-1]); //Z
				flux.println();
				k++;
				
			}
		}
		
		flux.close();
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public MNT[][] explode(int PX, int PY){
		/** 
		 * FONCTION :
		 * Generer une grille de MNT par division du MNT source.
		 * Tous ces MNT generes ont la meme resolution que le MNT source.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - PX [Scalaire, entier] : Nombre de MNT a generer en colonne.
		 * - PY [Scalaire, entier] : Nombre de MNT a generer en ligne.
		 * 
		 * RETOUR :
		 * - [Matrice(PY lignes,PX colonnes) d'objets de la classe MNT] : grille de MNT, l'element (1,1) de la matrice correspond au MNT Nord-Ouest.
		 */
		
		MNT[][] jeu_MNT = new MNT[PY][PX]; //Allocation de la grille de MNT
		int X;
		int Y;
		
		//Calcul de la taille commune des MNT de la grille
		int EX=getEX()/PX;
		int EY=getEY()/PY;
		
		for(int i=1;i<=PY;i++){ //Indice des lignes (varie du Nord au Sud)
			Y=Ymax-(i-1)*EY;
			for(int j=1;j<=PX;j++){ //Indices des colonnes (varie d'Ouest en Est)
				X=Xmin+(j-1)*EX;
				jeu_MNT[i-1][j-1] = new MNT(this,X,Y-EY,X+EX,Y); //Construction du MNT (i,j) par decoupage du MNT source
			}
		}
		return jeu_MNT;
	}
	//---------------------------------------------------	
	
	//---------------------------------------------------
	public MNT[][][] generateMNTPyramid(int PX, int PY, int[] resolution){
		/** 
		 * FONCTION :
		 * Generer une pyramide de MNT, c'est a dire un tuilage o� chaque tuile correspond a une collection de MNT a differentes resolutions.
		 * Le resultat est stocke dans un tenseur : une resolution par couche.
		 * Tuiles_<Resolution du MNT>_MNT/MNT_<X du coin Sud-Ouest (Xmin)>_<Y du coin Sud-Ouest (Ymin)>.txt
		 * 
		 * PARAMETRES EN ENTREE :
		 * - PX [Scalaire, entier] : Nombre de tuiles a generer en colonne.
		 * - PY [Scalaire, entier] : Nombre de tuiles a generer en ligne.
		 * - resolution [Vecteur(longueur quelconque >= 1) d'entiers] : Liste des resolutions a generer. La resolution est la meme en X et Y. La premiere resolution doit etre celle du MNT source ; les autres resolutions indiquees doivent etre multiples de celle-ci. 
		 *
		 * RETOUR :
		 * - [Tenseur<3d>(PY lignes, PX colonnes, resolution.length couches)] : Pyramide de MNT.
		 */
			
			
		//Division du MNT (tuilage)
		MNT[][] jeu_mnt = explode(PX,PY);
		
		//Instanciation de la pyramide de MNT
		MNT[][][] pyramide_mnt = new MNT[PY][PX][6];
		for(int i=1;i<=PY;i++){
			for(int j=1;j<=PX;j++){
				MNT tuile_mnt=jeu_mnt[i-1][j-1];
				pyramide_mnt[i-1][j-1][0]=tuile_mnt; //Pleine resolution
				for(int r=2;r<=resolution.length;r++){
					pyramide_mnt[i-1][j-1][r-1]=new MNT(tuile_mnt,resolution[r-1],resolution[r-1]); //Sous-echantillonage
				}
			}
		}
		
		return pyramide_mnt;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public void WritePyramidInFiles(MNT[][][] pyramide_mnt) throws IOException{
		/** 
		 * FONCTION :
		 * Ecrire tous les MNT contenus dans une pyramide de MNT dans des fichiers externes.
		 * Le resultat est stocke dans des fichiers matriciels contenus dans des dossiers eux-memes contenus dans le repertoire de travail. Le chemin relatif est de la forme :
		 * Tuiles_<Resolution du MNT>_MNT/MNT_<X du coin Sud-Ouest (Xmin)>_<Y du coin Sud-Ouest (Ymin)>.txt
		 * 
		 * PARAMETRE EN ENTREE :
		 * - pyramide_mnt [Tenseur<3d>(PY lignes, PX colonnes, resolution.length couches)] : Pyramide de MNT dont on veut exporter les MNT.
		 */
		
		MNT mnt;
		for(int i=1;i<=pyramide_mnt.length;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=1;j<=pyramide_mnt[0].length;j++){ //Indices des colonnes (varie d'Ouest en Est)
				for(int r=1;r<=pyramide_mnt[0][0].length;r++){
					mnt = pyramide_mnt[i-1][j-1][r-1];
					mnt.generate_mat("Tuiles_"+Tuile.resolution[r-1]+"_MNT/MNT_"+mnt.getXmin()+"_"+mnt.getYmin()+".txt"); //Ecriture du MNT sous-echantillone dans le fichier
				}
			}
		}
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public double altitude(double X, double Y){
		/** 
		 * FONCTION :
		 * Recuperer l'altitude d'un point quelconque repere en planimetrie sur le MNT.
		 * L'altitude est calculee par interpolation triangulaire :
		 * L'altitude du point est calculee de sorte qu'il appartiennent la face triangulaire correspondant a sa position dans la representation graphique du MNT.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - X [Scalaire, entier] : Easting du point.
		 * - Y [Scalaire, entier] : Northing du point.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : Altitude du point (X,Y) sur le MNT source.
		 */
					
		//Recherche de la maille a laquelle appartient le point
		int i = (int)((Ymax-Y)/DY)+1;
		int j = (int)((X-Xmin)/DX)+1;
		
		if(X%DX==0 && Y%DY==0)
			return getZAt(i-1, j-1);
		
		if(X!= Xmin && X%DX==0){
			j--;
		}
		else if(Y!= Ymax && Y%DY==0){
			i--;
		}
		
		int k = (i+j)%2;
		
		//Coin Nord-Ouest de la maille a laquelle appartient le point
		int Xd = Xmin+(j-1)*DX;
		int Yd = Ymax-(i-1)*DY;
		
		//Recherche des trois points du triangle auquel appartient le point
		Point3d p1;
		Point3d p2 = new Point3d(Xd+k*DX     , Yd-DY , getZAt(i  , j+k-1)     );
		Point3d p3 = new Point3d(Xd+(1-k)*DX , Yd    , getZAt(i-1, j+(1-k)-1) );
			 
//		if(X-Xd<Yd-Y){
		if (k==1) {
			if(Math.abs(X-Xd)<Math.abs(Yd-Y)){//k==1
				//Le point se situe sur la face triangulaire inf�rieure
				p1 = new Point3d(Xd+(1-k)*DX , Yd-DY , getZAt(i  , j+(1-k)-1) );
			}
			else {
				//Le point se situe sur la face triangulaire sup�rieure
				p1 = new Point3d(Xd+k*DX     , Yd    , getZAt(i-1, j+k-1)     );				
			}	
		}else{//k==0
			if(Math.abs(X-Xd)>DY-Math.abs(Yd-Y)){//k==1
				//Le point se situe sur la face triangulaire inf�rieure
				p1 = new Point3d(Xd+(1-k)*DX , Yd-DY , getZAt(i  , j+(1-k)-1) );
			}
			else {
				//Le point se situe sur la face triangulaire sup�rieure
				p1 = new Point3d(Xd+k*DX     , Yd    , getZAt(i-1, j+k-1)     );
				
			}
			
		}//fin if k==0
		
	Vector3d BA = new Vector3d(p1.x - p2.x,p1.y - p2.y,p1.z - p2.z);
	Vector3d BC = new Vector3d(p3.x - p2.x,p3.y - p2.y,p3.z - p2.z);
	Vector3d normale = new Vector3d();
	normale.cross(BA, BC);
	double d1 = -p2.x*normale.x-p2.y*normale.y-p2.z*normale.z;
	double Z = - (d1 +normale.x * X + normale.y * Y) / normale.z ; 
	return Z;
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	/*//////////////////////////////////////////////////*/	
}
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
