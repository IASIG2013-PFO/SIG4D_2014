package iasig.mobile.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 */


/**
 * @author Jean
 *
 */
public class Orthophoto {

	/**
	 * 
	 */
	private int NX;
	private int NY;
	private int Xmin;
	private int Xmax;
	private int DX;
	private int Ymin;
	private int Ymax;
	private int DY;
	
	private int[][][] RGB;
	
	public Orthophoto(String fich_ppm, MNT mnt) throws FileNotFoundException { //L'Ortho repose sur un MNT
		// TODO Auto-generated constructor stub
		Xmin = mnt.getXmin();
		Ymin = mnt.getYmin();
		Xmax = mnt.getXmax();
		Ymax = mnt.getYmax();
		
		Scanner flux = new Scanner(new File(fich_ppm));
		
		flux.nextLine(); //Ligne ignorée : P3
		flux.nextLine(); //Ligne ignorée : Commentaire de GIMP
		
		NY=flux.nextInt();
		NX=flux.nextInt();
	
		DX = mnt.getDX()*NX/(mnt.getNX()-1);
		DY = mnt.getDY()*NY/(mnt.getNY()-1);
		
		flux.nextLine(); //Ligne ignorée : valeur du blanc
		
		System.out.println("Avant");
		
		RGB = new int[NY][NX][3]; //Initialisation du tenseur des valeurs des pixels
		
		System.out.println("Après");
		
		for(int i=1;i<=NY;i++){
			System.out.println(i);
			for(int j=1;j<=NX;j++){
				for(int k=1;k<=3;k++){
					RGB[i-1][j-1][k-1]=flux.nextInt();
				}
			}
		}
		
		flux.close();
	}
	
	public void generate_csv(String fich_sortie) throws IOException{
		PrintWriter flux = new PrintWriter(new File(fich_sortie));
		
		int k=1;
		
		flux.println("ID X Y R G B");
		
		for(int i=1;i<=NY;i++){
			for(int j=1;j<=NX;j++){
				flux.print(k); //ID
				flux.print(" ");
				flux.print(Xmin+(j-1)*DX); //X
				flux.print(" ");
				flux.print(Ymax-(i-1)*DY); //Y
				flux.print(" ");
				flux.print(RGB[i-1][j-1][1]); //R
				flux.println();
				flux.print(RGB[i-1][j-1][2]); //G
				flux.println();
				flux.print(RGB[i-1][j-1][1]); //B
				flux.println();
				k++;	
			}
		}
		
		flux.close();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
	}

}
