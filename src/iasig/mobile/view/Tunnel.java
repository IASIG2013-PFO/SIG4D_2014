package iasig.mobile.view;

import iasig.dao.Polyligne_grapheDAO;

import java.sql.SQLException;

import javax.media.j3d.Shape3D;

import org.postgis.PGgeometry;

/**
 * @author jean
 *
 */
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
public class Tunnel {
	/** Cette classe d�finit un tunnel � partir de sa forme et ses coordonn�es de percement du MNT.*/
	
	/*public final int I_tuile = 58;
	public final int J_tuile = 84;
	
	public final int I_tuile = 58;
	public final int J_tuile = 84;
	
	public final int i_maille = 11;
	public final int j_maille = 9;*/
	
	/*/////////////////////ATTRIBUTS////////////////////*/
	private int X; //Borne Ouest de la maille du MNT � percer 
	private int Y; //Borne Sud de la maille du MNT � percer
	private int Z; //Altitude du bas du tunnel
	private int H; //Auteur du tunnel
	private PGgeometry forme; //Forme du tunnel : polygone ferm�
	/*//////////////////////////////////////////////////*/
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/*********************CONSTRUCTEUR*******************/
	
	//---------------------------------------------------
	public Tunnel(int x, int y, int z, int h) throws SQLException{
		/** 
		 * FONCTION :
		 * Construire un objet tunnel � partir d'une forme charg�e depuis la BDD.
		 * 
		 * PARAMETRES EN ENTREE
		 * - x [Scalaire, entier] : renseignement de l'attribut X
		 * - y [Scalaire, entier] : renseignement de l'attribut Y
		 * - z [Scalaire, entier] : renseignement de l'attribut Z
		 * - h [Scalaire, entier] : renseignement de l'attribut H
		 * 		 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe Tunnel] : un nouveau tunnel avec tous ses attributs renseign�s.
		 * */
		X=x;
		Y=y;
		Z=z;
		H=h;
		
		forme=Polyligne_grapheDAO.getTunnelFromBDD();
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	
	
	/***********************GETTERS**********************/
	
	//---------------------------------------------------
	public int getX(){
		/** 
		 * FONCTION :
		 * R�cup�rer l'attribut private X.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.X.
		 */
		
		return X;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getY(){
		/** 
		 * FONCTION :
		 * R�cup�rer l'attribut private Y.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.Y.
		 */
		
		return Y;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getZ(){
		/** 
		 * FONCTION :
		 * R�cup�rer l'attribut private Z.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.Z.
		 */
		
		return Z;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public int getH(){
		/** 
		 * FONCTION :
		 * R�cup�rer l'attribut private H.
		 * 
		 * RETOUR :
		 * - [Scalaire, entier] : this.H.
		 */
		
		return H;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
		PGgeometry getForme(){
			/** 
			 * FONCTION :
			 * R�cup�rer l'attribut private forme.
			 * 
			 * RETOUR :
			 * - [Scalaire, entier] : this.forme.
			 */
			
			return forme;
		}
		//---------------------------------------------------
	
	/****************************************************/
	
	
	
	/*******************AUTRES METHODES******************/
	
	//---------------------------------------------------
	public static void main(String[] args) {
		/** 
		 * FONCTION :
		 * Programme.
		 * 
		 * PARAMETRE EN ENTREE :
		 * -args [Vecteur(longueur quelconque >=0) de cha�nes de caract�res] : Eventuels param�tres pass�s � l'ex�cuteur. ICI AUCUN.
		 */	
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	/*//////////////////////////////////////////////////*/
}
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
