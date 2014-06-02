package iasig.mobile.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.vecmath.Point3d;

/**
 * @author jean
 *
 */
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
public class MNT {
	/** Cette classe définit un MNT sous forme matricielle (grille d'altitude), avec ses informations de géoréférencement.*/	
	
	/*/////////////////////ATTRIBUTS////////////////////*/
	private int NX; //Nombre de colonnes
	private int NY; //Nombre de lignes
	private int Xmin; //Easting minimal
	private int Xmax; //Easting maximal
	private int DX; //Pas en X entre chaque point mesuré (résolution en X)
	private int Ymin; //Northing minimal
	private int Ymax; //Northing maximal
	private int DY; //Pas en Y entre chaque point mesuré (résolution en Y)
	private int Zmin; //Altitude minimale
	private int Zmax; //Altitude maximale
	
	private int[][] M; //Matrice des altitudes, le premier point est le coin Nord-Ouest de la grille (Xmin,Ymax)
	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/********************CONSTRUCTEURS*******************/
		
	//---------------------------------------------------
	public MNT(String fich_entree) throws IOException {
		/** 
		 * FONCTION :
		 * Charger un MNT à partir d'un fichier csv.
		 * Ce fichier doit respecter la forme suivante :
		 * ---------------------------
		 * NX NY
		 * Xmin Xmax DX
		 * Ymin Ymax DY
		 * Zmin Zmax
		 * <matrice des valeurs, NX en colonne, NY en ligne, à partir du coin Nord-Ouest>
		 * ---------------------------
		 * 
		 * PARAMETRE EN ENTREE :
		 * - fich_entree [Scalaire, chaîne de caractères] : chemin du fichier du MNT à charger.
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT, comportant les mêmes informations que dans le fichier.
		 * */
		
		Scanner flux = new Scanner(new File(fich_entree)); //Flux de lecture
		
		NX = flux.nextInt(); //Prend le premier entier dans le fichier..
		NY = flux.nextInt(); //Le deuxième...
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
		 * Construire un MNT à partir d'un autre objet MNT plus grand.
		 * Le découpage se fait entre des bornes définies.
		 * La résolution est inchangée.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - mnt [Scalaire, objet de la classe MNT] : MNT source.
		 * - xmin [Scalaire, entier] : Borne Ouest.
		 * - ymin [Scalaire, entier] : Borne Sud.
		 * - xmax [Scalaire, entier] : Borne Est.
		 * - ymax [Scalaire, entier] : Borne Ouest.
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT, à la même résolution que le MNT source.
		 * */
		
		//Renseignement des nouvelles bornes
		Xmin=xmin;
		Ymin=ymin;
		Xmax=xmax;
		Ymax=ymax;
		
		//La résolution est inchangée
		DX=mnt.getDX();
		DY=mnt.getDY();
		
		//Calcul des indices limites entre lesquels récupérer les valeurs sur la grille
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
		//-Remplit la matrice des valeurs du MNT construite à partir de celle du MNT source
		//-Recherche les altitudes minimale et maximale du MNT construit
		for(int i=imin;i<=imax;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=jmin;j<=jmax;j++){ //Indices des colonnes (varie d'Ouest en Est)
				Z=mnt.getZAt(i-1,j-1); //Récupération de Z(i,j)
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
		 * Construire un MNT par sous-échantillonage d'un MNT plus résolu.
		 * La nouvelle résolution est indiquée.
		 * Les bornes sont inchangées.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - mnt [Scalaire, objet de la classe MNT] : MNT source.
		 * - dx [Scalaire, entier] : Résolution en X souhaitée, doit être multiple de mnt.DX.
		 * - dy [Scalaire, entier] : Résolution en Y souhaitée, doit être multiple de mnt.DY.
		 *  
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT à résolution moindre, ayant la même emprise que le MNT source.
		 * */
		
		//Même emprise
		Xmin=mnt.getXmin();
		Ymin=mnt.getYmin();
		Xmax=mnt.getXmax();
		Ymax=mnt.getYmax();
		
		//Récupération des résolutions en X et en Y
		DX=dx;
		DY=dy;
		
		//Rapport des résolutions
		int FX = dx/mnt.getDX();
		int FY = dx/mnt.getDY();
		
		//Calcul du nouveau nombre d'éléments en colonne et en ligne
		NX=(mnt.getNX()-1)/FX+1;
		NY=(mnt.getNY()-1)/FY+1;
		
		M=new int[NY][NX]; //Allocation de la grille des altitudes
		
		//Initialisation des altitudes minimale et maximale
		Zmin=mnt.getZAt(0,0); 
		Zmax=mnt.getZAt(0,0);
		
		int Z;
		
		//Cette double boucle :
		//-Remplit la matrice des valeurs du MNT construite à partir de celle du MNT source
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
		 * Construire un MNT par lecture systématique des attributs fournis en entrée.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - nx [Scalaire, entier] : valeur à attribuer à NX.
		 * - ny [Scalaire, entier] : valeur à attribuer à NY.
		 * - xmin [Scalaire, entier] : valeur à attribuer à Xmin.
		 * - xmax [Scalaire, entier] : valeur à attribuer à Xmax.
		 * - dx [Scalaire, entier] : valeur à attribuer à DX.
		 * - ymin [Scalaire, entier] : valeur à attribuer à Ymin.
		 * - ymax [Scalaire, entier] : valeur à attribuer à Ymax.
		 * - dy [Scalaire, entier] : valeur à attribuer à DY.
		 * - zmin [Scalaire, entier] : valeur à attribuer à Zmin.
		 * - zmax [Scalaire, entier] : valeur à attribuer à Zmax.
		 * - m [Matrice[NY lignes, NX colonnes] : valeur à attribuer à M.
		 *  
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe MNT] : un nouveau MNT avec les valeurs d'attributs calqués sur ceux fournis en entrée.
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
	
	/****************************************************/
	
	
	
	/***********************GETTERS**********************/
	
	//---------------------------------------------------
	public int getXmin(){
		/** 
		 * FONCTION :
		 * Récupérer l'attribut private Xmin.
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
		 * Récupérer l'attribut private Ymin.
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
		 * Récupérer l'attribut private Xmax.
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
		 * Récupérer l'attribut private Ymax.
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
		 * Calculer et récupérer l'Easting moyen du MNT.
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
		 * Calculer et récupérer le Northing moyen du MNT.
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
		 * Calculer et récupérer l'altitude moyenne du MNT.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : Z moyen du MNT.
		 */
		
		//Z moyen = somme(somme(M(i,j), i variant de 1 à NY), j variant de 1 à NX) / (NX*NY)  
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
		 * Récupérer l'attribut private DX.
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
		 * Récupérer l'attribut private DY.
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
		 * Récupérer l'attribut private NX.
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
		 * Récupérer l'attribut private NX.
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
		 * Calculer et récupérer l'emprise en Easting du MNT.
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
		 * Calculer et récupérer l'emprise en Northing du MNT.
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
		 * Récupérer une certaine altitude dans la matrice M (attribut private)
		 * 
		 * PARAMETRES EN ENTREE :
		 * - i [Scalaire, entier] : indice de ligne (attention, la numérotation commence à zéro).
		 * - j [Scalaire, entier] : indice de colonne (attention, la numérotation commence à zéro).
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
		 * <matrice des valeurs, NX en colonne, NY en ligne, à partir du coin Nord-Ouest>
		 * ---------------------------
		 * 
		 * PARAMETRE EN ENTREE :
		 * - fich_sortie [Scalaire, chaîne de caractères] : chemin du fichier dans lequel écrire. Si le fichier existe déjà, il est écrasé.
		 */
		
		PrintWriter flux = new PrintWriter(new File(fich_sortie)); //Flux d'écriture
		
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
		 * ID X Y Z      <= En-tête
		 * ID1 X1 Y1 Z1
		 * ID2 X2 Y2 Z2
		 * ID3 X3 Y3 Z3
		 * ...
		 * ---------------------------
		 * Le premier point indiqué correspond au coin Nord-Ouest. Puis le balayage se fait d'abord en X, puis en Y.
		 * 
		 * PARAMETRE EN ENTREE :
		 * - fich_sortie [Scalaire, chaîne de caractères] : chemin du fichier dans lequel écrire. Si le fichier existe déjà, il est écrasé.
		 */
		
		PrintWriter flux = new PrintWriter(new File(fich_sortie)); //Flux d'écriture
		
		int k=1;
		
		flux.println("ID X Y Z"); //En-tête
		
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
		 * Générer une grille de MNT par division du MNT source.
		 * Tous ces MNT générés ont la même résolution que le MNT source.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - PX [Scalaire, entier] : Nombre de MNT à générer en colonne.
		 * - PY [Scalaire, entier] : Nombre de MNT à générer en ligne.
		 * 
		 * RETOUR :
		 * - [Matrice(PY lignes,PX colonnes) d'objets de la classe MNT] : grille de MNT, l'élément (1,1) de la matrice correspond au MNT Nord-Ouest.
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
				jeu_MNT[i-1][j-1] = new MNT(this,X,Y-EY,X+EX,Y); //Construction du MNT (i,j) par découpage du MNT source
			}
		}
		return jeu_MNT;
	}
	//---------------------------------------------------	
	
	//---------------------------------------------------
	public MNT[][][] generateMNTPyramid(int PX, int PY, int[] resolution){
		/** 
		 * FONCTION :
		 * Générer une pyramide de MNT, c'est à dire un tuilage où chaque tuile correspond à une collection de MNT à différentes résolutions.
		 * Le résultat est stocké dans un tenseur : une résolution par couche.
		 * Tuiles_<Résolution du MNT>_MNT/MNT_<X du coin Sud-Ouest (Xmin)>_<Y du coin Sud-Ouest (Ymin)>.txt
		 * 
		 * PARAMETRES EN ENTREE :
		 * - PX [Scalaire, entier] : Nombre de tuiles à générer en colonne.
		 * - PY [Scalaire, entier] : Nombre de tuiles à générer en ligne.
		 * - resolution [Vecteur(longueur quelconque >= 1) d'entiers] : Liste des résolutions à générer. La résolution est la même en X et Y. La première résolution doit être celle du MNT source ; les autres résolutions indiquées doivent être multiples de celle-ci. 
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
				pyramide_mnt[i-1][j-1][0]=tuile_mnt; //Pleine résolution
				for(int r=2;r<=resolution.length;r++){
					pyramide_mnt[i-1][j-1][r-1]=new MNT(tuile_mnt,resolution[r-1],resolution[r-1]); //Sous-échantillonage
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
		 * Le résultat est stocké dans des fichiers matriciels contenus dans des dossiers eux-mêmes contenus dans le répertoire de travail. Le chemin relatif est de la forme :
		 * Tuiles_<Résolution du MNT>_MNT/MNT_<X du coin Sud-Ouest (Xmin)>_<Y du coin Sud-Ouest (Ymin)>.txt
		 * 
		 * PARAMETRE EN ENTREE :
		 * - pyramide_mnt [Tenseur<3d>(PY lignes, PX colonnes, resolution.length couches)] : Pyramide de MNT dont on veut exporter les MNT.
		 */
		
		MNT mnt;
		for(int i=1;i<=pyramide_mnt.length;i++){ //Indice des lignes (varie du Nord au Sud)
			for(int j=1;j<=pyramide_mnt[0].length;j++){ //Indices des colonnes (varie d'Ouest en Est)
				for(int r=1;r<=pyramide_mnt[0][0].length;r++){
					mnt = pyramide_mnt[i-1][j-1][r-1];
					mnt.generate_mat("Tuiles_"+Tuile.resolution[r-1]+"_MNT/MNT_"+mnt.getXmin()+"_"+mnt.getYmin()+".txt"); //Ecriture du MNT sous-échantilloné dans le fichier
				}
			}
		}
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public double altitude(double X, double Y){
		/** 
		 * FONCTION :
		 * Récupérer l'altitude d'un point quelconque repéré en planimétrie sur le MNT.
		 * L'altitude est calculée par interpolation triangulaire :
		 * L'altitude du point est calculée de sorte qu'il appartiennent la face triangulaire correspondant à sa position dans la représentation graphique du MNT.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - X [Scalaire, entier] : Easting du point.
		 * - Y [Scalaire, entier] : Northing du point.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : Altitude du point (X,Y) sur le MNT source.
		 */
					
		//Recherche de la maille à laquelle appartient le point
		int i = (int)((Ymax-Y)/DY)+1;
		int j = (int)((X-Xmin)/DX)+1;
		
		if(X%DX==0 && Y%DY==0)
			return getZAt(i-1, j-1);
		
		int k = (i+j)%2;
		System.out.println(i+" "+j+" "+k);
		System.out.println(X + " " + Xmin);
		System.out.println(Y);
		
		//Coin Sud-Ouest de la maille à laquelle appartient le point
		int Xd = Xmin+(j-1)*DX;
		int Yd = Ymax-(i-1)*DY;
		
		//Recherche des trois points du triangle auquel appartient le point
		Point3d p1;
		Point3d p2 = new Point3d(X+k*DX     , Y-DY , getZAt(i  , j+k-1)     );
		Point3d p3 = new Point3d(X+(1-k)*DX , Y    , getZAt(i-1, j+(1-k)-1) );
			 
		if(X-Xd<Y-Yd){
			//Le point se situe sur la face triangulaire supérieure
			p1 = new Point3d(X+(1-k)*DX , Y-DY , getZAt(i  , j+(1-k)-1) );
		}
		else {
			//Le point se situe sur la face triangulaire inférieure
			p1 = new Point3d(X+k*DX     , Y    , getZAt(i-1, j+k-1)     );
		}
		
		//Résolution du système (a,b)
		// X = a*x1 + b*x2 + x3
		// Y = a*y1 + b*y2 + y3
		double a=(X*p2.y-Y*p2.x-p3.x*p2.y+p3.y*p2.x)/(p1.x*p2.y-p1.y*p2.x);
		double b=(X*p1.y-Y*p1.x-p3.x*p1.y+p1.x*p3.y)/(p1.y*p2.x-p1.x*p2.y);
		
		//Calcul de Z :
		// Z = a*z1+b*z2+z3
		return a*p1.z + b*p2.z+ p3.z;
	}
	//---------------------------------------------------

	//---------------------------------------------------
	public static void main(String[] args) throws IOException {
		/** 
		 * FONCTION :
		 * Programme.
		 * 
		 * PARAMETRE EN ENTREE :
		 * -args [Vecteur(longueur quelconque >=0) de chaînes de caractères] : Eventuels paramètres passés à l'exécuteur. ICI AUCUN.
		 */
		
		/*System.out.println("a");
		int[] resolution_MNT = {25,50,100,200,500,1000};
		
		for(int r=2;r<=6;r++){
			for(int X=916000;X<1015000;X+=1000){
				for(int Y=6513000;Y<6598000;Y+=1000){
					MNT mnt = new MNT("Tuiles_"+resolution_MNT[r-1]+"_MNT/MNT_"+X+"_"+Y+".txt");
					mnt.generate_csv("Tuiles_"+resolution_MNT[r-1]+"_MNT/MNT_"+X+"_"+Y+".csv");
				}
			}
		}
		//MNT mnt = new MNT("MNT_74.txt");*/
		
		//mnt.generateMNTPyramidInSeparateFiles(99,85,resolution_MNT);
		
		/*Orthophoto orthophoto = new Orthophoto("Ortho_Annecy.ppm",mnt);
		orthophoto.generate_csv("Ortho_Annecy_xyrgb.csv");*/
		
		MNT mnt = new MNT("Tuiles_25_MNT/MNT_999000_6540000.txt");
		//MNT[][] mat_MNT = mnt.explode(20,20);
		//System.out.println(mat_MNT[19][19].getZAt(40,40));
		//MNT petit_MNT = new MNT(mnt,10000,10000);
		mnt.generate_csv("Tuiles_25_MNT/MNT_999000_6540000.csv");
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	/*//////////////////////////////////////////////////*/
	

	
	
}
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
