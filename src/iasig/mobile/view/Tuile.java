package iasig.mobile.view;

import iasig.dao.MNTDAO;
import iasig.dao.OrthoDAO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point2i;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;

import com.sun.j3d.utils.image.TextureLoader;

/**
 * @author jean
 *
 */
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
public class Tuile {
	/** Cette classe d�finit une tuile, c'est � dire un carr� de MNT associ� � un  carr� d'orthophoto, � une certaine r�solution.*/
	/** Le maillage est d�fini par les constantes.*/
	
	/*/////////////////////CONSTANTES///////////////////*/

	/********************OBJETS DE DAO*******************/
	public static final MNTDAO mntDAO =new MNTDAO(); //Cr�ation du loader
	public static final OrthoDAO orthoDAO=new OrthoDAO(); //Cr�ation du loader
	/****************************************************/
	
	/***************PARAMETRES DU MAILLAGE***************/
	public static final int Xmin = 916000;
	public static final int Ymin = 6513000;
	public static final int Xmax = 1015000;
	public static final int Ymax = 6598000;
	public static final int DX = 1000;
	public static final int DY = 1000;
	public static final int PX = 99;
	public static final int PY = 85;
	/****************************************************/
		
	/***********OPTIONS DE CREATION DES TUILES***********/
	public static final boolean DB = true; //Charger � partir de la BDD
	public static final boolean FILE = false; //Charger � partir de fichiers
	
	public static final boolean LOADALL = true; //Charger les MNT d�j� d�coup�s
	public static final boolean DIVISION = false; //G�n�rer les MNT par division d'un MNT plus grand
	/****************************************************/
	
	
	
	/****************CLASSES DE RESOLUTION***************/
	//La r�solution de base est celle de l'orthophoto
	//R�solution MNT = 5 * r�solution de base
	public static final int[] resolution={5,10,20,40,100,200};
	public static final int R5 = 1;
	public static final int R10 = 2;
	public static final int R20 = 3;
	public static final int R40 = 4;
	public static final int R100 = 5;
	public static final int R200 = 6;
	/****************************************************/
	
	
	
	/***************FILTRE DES TUILES NOIRES*************/
	public static final boolean[][] masque_noir =
	{
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , false, false, false, false, false, false, true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, false, false, false, false, true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , false, false, false, true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
		{true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , false, false, false, false, false, true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , true , }, 
	};
	/****************************************************/

	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////ATTRIBUTS////////////////////*/
	MNT mnt;
	BufferedImage ortho;
	int i_maille; //coordonn�es dans le maillage
	int j_maille; //coordonn�es dans le maillage
	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/*********************CONSTRUCTEUR*******************/
	

	//---------------------------------------------------
	public Tuile(int i, int j, int r, boolean source) throws IOException{
		/** 
		 * FONCTION :
		 * Cr�er la tuile d'indices i, j, � partir de la souce indiqu�e
		 * 
		 * PARAMETRES EN ENTREE :
		 * - i [Scalaire, entier] : indice de ligne de la tuile � charger
		 * - j [Scalaire, entier] : indice de colonne de la tuile � charger
		 * - r [Scalaire, entier] : classe de r�solution choisie (1-6)
		 * - source [Scalaire, bool�en] : provenance : soit DB pour la base de donn�es, soit FILE pour les fichiers
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe Tuile] : une nouvelle tuile de position voulue.
		 * */
	
		int res = resolution[r-1];

		if(source){ //DB
			//R�cup�ration du mnt dans la base de donn�es
			mnt= mntDAO.load_mnt(i, j, r);

			//R�cup�ration de l'orthophoto dans la base de donn�es
			ortho = orthoDAO.load_ortho(i, j, r);
			
			//calcul des coordonn�es du maillage
			int X=Xmin+i*DX; //
			int Y=Ymin+j*DY; // Coin Sud-Ouest du MNT � charger

			this.i_maille=i;
			this.j_maille=j;
			
		}
		else{ //FILE
			String file_mnt; //Chemin du fichier de MNT
			String file_ortho; //Chemin du fichier d'ortho
			
			int X=Xmin+i*DX; //
			int Y=Ymin+j*DY; // Coin Sud-Ouest du MNT � charger
			
			file_mnt="Tuiles_"+5*res+"_MNT/MNT_"+X+"_"+Y+".txt";
			file_ortho="Tuiles_"+res+"_jpeg/Ortho_"+(X-res)+"_"+(Y-res)+".jpeg";
			
			mnt = new MNT(file_mnt);
			ortho = ImageIO.read(new File(file_ortho));
		}
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	
	
	/***********************GETTERS**********************/
	
	//---------------------------------------------------
	public MNT getMNT(){
		/** 
		 * FONCTION :
		 * R�cup�rer l'attribut private mnt.
		 *
		 * RETOUR :
		 * - [Scalaire, objet de la classe MNT] : this.mnt.
		 */
		return mnt;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
		public BufferedImage getOrtho(){
			/** 
			 * FONCTION :
			 * R�cup�rer l'attribut private ortho.
			 * 
			 * RETOUR :
			 * - [Scalaire, objet de la classe BufferedImage] : this.ortho.
			 */
			return ortho;
		}
	//---------------------------------------------------
	
	//---------------------------------------------------
		public int get_i_maille(){
			/** 
			 * FONCTION :
			 * R�cup�rer l'attribut private i_maille.
			 */
			return this.i_maille;
		}
	//---------------------------------------------------
	
	//---------------------------------------------------
		public int get_j_maille(){
			/** 
			 * FONCTION :
			 * R�cup�rer l'attribut private j_maille.
			 */
			return this.j_maille;
		}
	//---------------------------------------------------
		
	/****************************************************/	
		
		
		
	/*******************AUTRES METHODES******************/
	
	//---------------------------------------------------
	public boolean DarkTile(){
		/** 
		 * FONCTION :
		 * Tester si la tuile ne contient que des pixels noirs.
		 * 
		 * RETOUR :
		 * - [Scalaire, bool�en] : VRAI si la tuile est totalement noire, FAUX sinon.
		 */
		
		int[] Pixels = ortho.getData().getPixels(0, 0, ortho.getWidth(), ortho.getHeight(), new int[1000000]);
		
		for(int i=1;i<=Pixels.length;i++){
			if(Pixels[i-1]!=0){
				return false;
			}
			else{}
		}
		return true;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public static boolean[][] getAllDarkTiles() throws IOException{
		/** 
		 * FONCTION :
		 * R�cup�rer le masque matriciel des tuiles noires sur l'ensemble du maillage.
		 * 
		 * RETOUR :
		 * - [Matrice<PY lignes, PX colonnes> d'entiers] : masque : l'�l�ment (i,j) est VRAI si et seulement si la tuile (i,j) est noire.
		 * LE RESULTAT EST STOCKE EN DUR EN TANT QUE CONSTANTE DE LA CLASSE => GAIN DE RAPIDITE. 
		 */
		
		//Instanciation du masque
		boolean[][] masque = new boolean[PX][PY];
		
		for(int i=0;i<PX;i++){
			for(int j=0;j<PY;j++){
				Tuile t = new Tuile(i,j,R5,DB);
				masque[i][j]=t.DarkTile();
			}
		}
		
		return masque;
	}
	//---------------------------------------------------	
		
	//---------------------------------------------------
	public static Point2i Center(Vector<int[]> listeIndices){
		/** 
		 * FONCTION :
		 * R�cup�rer le centre du rectangle englobant une collection de tuiles.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - listeIndices [Matrice(nombre de lignes quelconque>=1, 3 colonnes) d'entiers] : liste des indices et des classes de r�solutions des tuiles de la collection.
		 * 
		 * RETOUR :
		 * - [Scalaire, objet de la classe Point2i] : centre de la s�lection, en plani.
		 */

		if(listeIndices.size()==0){
			return new Point2i(0,0);
		}
		else{}

		int nb_tuiles = listeIndices.size();
		
		int imin=listeIndices.elementAt(0)[0];
		int imax=imin;
		int jmin=listeIndices.elementAt(0)[1];
		int jmax=jmin;
		
		int i;
		int j;
		
		//Recherche des indices minimaux et maximaux de la s�lection
		for(int k=1;k<=nb_tuiles;k++){
			i=listeIndices.elementAt(k-1)[0];
			
			if(i<imin){
				imin=i;
			}
			else if(i>imax){
				imax=i;
			}
			else{}
			
			j=listeIndices.elementAt(k-1)[1];
			
			if(j<jmin){
				jmin=j;
			}
			else if(j>jmax){
				jmax=j;
			}
			else{}
		}
		
		//Calcul du rectangle englobant la s�lection
		int Xmi=Xmin+imin*DX;
		int Xma=Xmin+(imax+1)*DX;
		
		int Ymi=Ymin+jmin*DY;
		int Yma=Ymin+(jmax+1)*DY;
		
		//Calcul du centre g�ographique de la s�lection
		return new Point2i((Xmi+Xma)/2,(Ymi+Yma)/2);
	}
	//---------------------------------------------------	
	
	//---------------------------------------------------
	public static Vector<int[]> getAllIndicesBetween(int imin, int jmin, int imax, int jmax, int r){
		/** 
		 * FONCTION :
		 * R�cup�rer tous le indices de tuiles entre des bornes. Les tuiles noires sont �limin�es lors du processus.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - imin [Scalaire, entier] : indice de ligne minimal (borne Nord).
		 * - jmin [Scalaire, entier] : indice de colonne minimal (borne Ouest).
		 * - imax [Scalaire, entier] : indice de ligne maximal (borne Sud).
		 * - jmax [Scalaire, entier] : indice de colonne maximal (borne Est).
		 * - r [Scalaire, entier] : classe de r�solution choisi (1-6) pour toutes les tuiles.
		 * 
		 * RETOUR :
		 * - listeIndices [Matrice(nombre de lignes quelconque>=0, 3 colonnes) d'entiers] : liste des indices des tuiles de la s�lection et leur classe de r�solution (r), tuiles noires exclues.
		 */
		
		//Instanciation de la liste de sortie
		Vector<int[]> listeIndices = new Vector<int[]>();
		
		for(int i=imin;i<=imax;i++){
			for(int j=jmin;j<=jmax;j++){
				if(masque_noir[i][j]){
					//Tuile noire : on l'ignore
					continue;
				}
				else{}
				
				//Ajout dans la liste
				int[] triplet = {i,j,r};
				listeIndices.addElement(triplet);
			}
		}		
		return listeIndices;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public static Vector<int[]> removeBlackTiles(Vector<int[]> listeIndices){
		/** 
		 * FONCTION :
		 * Filtrer une liste d'indices de tuiles en �liminant les noires.
		 * 
		 * PARAMETRE EN ENTREE :
		 * - [Matrice(nombre de lignes quelconque>=1, 3 colonnes) d'entiers] : liste des indices des tuiles de la s�lection � filtrer.
		 * 
		 * RETOUR :
		 * - [Matrice(nombre de lignes quelconque>=0, 3 colonnes) d'entiers] : liste des indices des tuiles de la s�lection et leur classe de r�solution (r), tuiles noires exclues.
		 */
		
		//Instanciation de la liste de sortie
		Vector<int[]> listeIndicesSortie = new Vector<int[]>();
		
		int[] triplet;
		
		for(int k=1;k<=listeIndices.size();k++){
			triplet = listeIndices.elementAt(k-1);
			
			if(masque_noir[triplet[0]][triplet[1]]){
				//Tuile noire : on l'ignore
				continue;
			}
			else{}
			
			//Ajout dans la liste
			listeIndicesSortie.addElement(triplet);
		}
		
		return listeIndicesSortie;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public static Tuile[] load(Vector<int[]> listeIndices, boolean source) throws IOException {
		/** 
		 * FONCTION :
		 * Charger une liste de tuiles � partir de leurs indices � partir de la BDD ou de fichiers.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - listeIndices [Matrice(nombre de lignes quelconque>=1, 3 colonnes) d'entiers] : liste des indices des tuiles � charger et leurs classes de r�solutions.
		 * - source [Scalaire, bool�en] : indication de la provenance des donn�es : DB or FILE.
		 * 
		 * RETOUR :
		 * - [Matrice(PY lignes,PX colonnes) d'objets de la classe MNT] : grille de MNT, l'�l�ment (1,1) de la matrice correspond au MNT Nord-Ouest.
		 */
		
		int nb_tuiles = listeIndices.size();
		
		int i;
		int j;
	
		//Instanciation de la liste des tuiles
		Tuile[] listeTuiles = new Tuile[nb_tuiles];
		
		int r;
		//Chargement des tuiles
		for(int k=1;k<=nb_tuiles;k++){
			i=listeIndices.elementAt(k-1)[0];
			j=listeIndices.elementAt(k-1)[1];
			r=listeIndices.elementAt(k-1)[2];
			
			listeTuiles[k-1]=new Tuile(i,j,r,source);
		}
			
		return listeTuiles;
	}
	//---------------------------------------------------
	
	//---------------------------------------------------
	public Shape3D draw(boolean b_orthophoto) throws IOException{

		TriangleArray MNT_graphique = trace_MNT(mnt); // D�finition de la
														// g�om�trie du MNT
														// graphique

		Appearance apparence = new Appearance();

		if (b_orthophoto) { // WITHORTHO
			Texture textureOrtho = plaquer_ortho(MNT_graphique, ortho,
					mnt.getXmin(), mnt.getYmin(), mnt.getXmax(),
					mnt.getYmax(), mnt.getDX() / 5, mnt.getDY() / 5); // Application
																			// de
																			// la
																			// texture
																			// sur
																			// le
																			// MNT
																			// graphique
			apparence = set_apparence(textureOrtho,
					PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE); // D�finition
																					// de
																					// l'apparence
																					// du
																					// MNT
																					// graphique
																					// incluant
																					// les
																					// param�tres
																					// de
																					// la
																					// texture
		} else { // WITHOUTORTHO
			apparence = set_apparence(PolygonAttributes.POLYGON_LINE,
					PolygonAttributes.CULL_NONE); // D�finition de l'apparence
													// du MNT graphique non
													// textur�
		}

		// Shape du MNT graphique = Geom�trie + apparence
		return new Shape3D(MNT_graphique, apparence);
	}
	//---------------------------------------------------
	
	// ---------------------------------------------------
	public TriangleArray trace_MNT(MNT mnt) {
		/**
		 * FONCTION : Construire la g�om�trie du MNT, un ensemble de triangles
		 * 3D. Chaque maille carr�e est divis�e en deux de ces triangles par une
		 * diagonale.
		 * 
		 * PARAMETRES EN ENTREE : - [Scalaire, objet de la classe MNT] : MNT
		 * source.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe TriangleArray] : G�om�trie
		 * du MNT sous forme de triangles 3D.
		 **/

		// Instanciation de la g�om�trie avec indication du nombre total de
		// triangles.
		TriangleArray mnt_graphique = new TriangleArray(6 * (mnt.getNX() - 1)
				* (mnt.getNY() - 1), TriangleArray.COORDINATES
				| TriangleArray.COLOR_3 | TriangleArray.TEXTURE_COORDINATE_2);

		int X;
		int Y;
		int k;

		int DX = mnt.getDX();
		int DY = mnt.getDY();
		int Xmin = mnt.getXmin();
		int Ymax = mnt.getYmax();

		Point3d p1 = new Point3d();
		Point3d p2 = new Point3d();
		Point3d p3 = new Point3d();
		Point3d p4 = new Point3d();

		Color3f color = new Color3f();

		int n = 0;

		for (int i = 1; i <= mnt.getNY() - 1; i++) { // Indice des lignes (varie
														// du Nord au Sud)
			Y = Ymax - (i - 1) * DY; // Northing correspondant

			for (int j = 1; j <= mnt.getNX() - 1; j++) { // Indices des colonnes
															// (varie d'Ouest en
															// Est)
				X = Xmin + (j - 1) * DX; // Easting correspondant

//					if (X == tunnel.getX() && Y == tunnel.getY()) {
//						// Pr�voir un trou pour le tunnel
//						continue;
//					} else {
//					}

				// TRACE DE LA MAILLE D'ORIGINE (X,Y) (COIN SUD-OUEST)

				k = (i + j) % 2; // Param�tre d'orientation de la diagonale

				// Calcul des coordonn�es (X,Y,Z) des 4 coins de la maille
				// La diagonale correspond � [p2 p3]. p1 et p2 sont au Sud, p3
				// et p4 au Nord.
				p1.set(X + k * DX, Y, mnt.getZAt(i - 1, j + k - 1));
				p2.set(X + k * DX, Y - DY, mnt.getZAt(i, j + k - 1));
				p3.set(X + (1 - k) * DX, Y, mnt.getZAt(i - 1, j + (1 - k) - 1));
				p4.set(X + (1 - k) * DX, Y - DY, mnt.getZAt(i, j + (1 - k) - 1));

				// Orientation des triangles dans le sens direct
				if (k == 0) {
					// Triangle p1 p2 p3
					mnt_graphique.setCoordinate(n, p1);
					mnt_graphique.setCoordinate(n + 1, p3);
					mnt_graphique.setCoordinate(n + 2, p2);

					// Triangle p2 p3 p4
					mnt_graphique.setCoordinate(n + 3, p2);
					mnt_graphique.setCoordinate(n + 4, p3);
					mnt_graphique.setCoordinate(n + 5, p4);
				} else {
					// Triangle p1 p2 p3
					mnt_graphique.setCoordinate(n, p1);
					mnt_graphique.setCoordinate(n + 1, p2);
					mnt_graphique.setCoordinate(n + 2, p3);

					// Triangle p2 p3 p4
					mnt_graphique.setCoordinate(n + 3, p3);
					mnt_graphique.setCoordinate(n + 4, p2);
					mnt_graphique.setCoordinate(n + 5, p4);
				}

				for (int m = 0; m <= 5; m++) { // D�finition de la couleur en
												// fonction de la r�solution
					switch (DX) {
					case 25: {
						color.set(World.Blue);
						;
						break;
					}
					case 50: {
						color.set(World.Red);
						break;
					}
					case 100: {
						color.set(World.Green);
						break;
					}
					case 200: {
						color.set(World.Yellow);
						break;
					}
					case 500: {
						color.set(World.Magenta);
						break;
					}
					case 1000: {
						color.set(World.Cyan);
						break;
					}
					default: {
					}
					}
					mnt_graphique.setColor(n + m, color);
				}
				n += 6;
			}
		}

		return mnt_graphique;
	}
	// ---------------------------------------------------

	// ---------------------------------------------------
	public Texture plaquer_ortho(TriangleArray MNT_graphique,
			BufferedImage ortho, int Xmin, int Ymin, int Xmax, int Ymax,
			int RX, int RY) throws IOException {
		/**
		 * FONCTION : Cr�er la texture orthophoto associ� au MNT. Les
		 * coordonn�es textures sont calcul�es r�guli�rement en associant chaque
		 * sommet � sa position dans l'image.
		 * 
		 * PARAMETRES EN ENTREE-SORTIE : - MNT_graphique [Scalaire, objet de la
		 * classe TriangleArray] : MNT � plaquer. En sortie chaque sommet de
		 * triangle est muni de ses coordonn�es texture.
		 * 
		 * PARAMETRE EN ENTREE : - ortho [Scalaire, objet de la classe
		 * BufferedImage] : Image de l'orthophoto � utiliser pour la texture. -
		 * Xmin [Scalaire, entier] : Borne Ouest du MNT. - Ymin [Scalaire,
		 * entier] : Borne Nord du MNT. - Xmax [Scalaire, entier] : Borne Est du
		 * MNT. - Ymax [Scalaire, entier] : Borne Sud du MNT. - RX [Scalaire,
		 * entier] : Largeur d'une maille de MNT. - RY [Scalaire, entier] :
		 * Hauteur d'une maille de MNT.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Texture] : La texture
		 * cr��e � partir de l'orthophoto.
		 **/

		Texture texture = new TextureLoader(ortho).getTexture(); // Chargement
																	// de la
																	// texture �
																	// partir de
																	// l'image
		Point3d point3d = new Point3d();

		// Etendue g�ographique de la maille
		int EX = Xmax - Xmin + 2 * RX;
		int EY = Ymax - Ymin + 2 * RY;

		for (int i = 0; i < MNT_graphique.getVertexCount(); i++) {
			MNT_graphique.getCoordinate(i, point3d); // R�cup�ration des
														// coordonn�es (X,Y,Z)
														// du i� point de la
														// g�om�trie
			// Calcul des coordonn�es textures de ce point. Syst�me de
			// coordonn�es : (x,y) variant de 0 � 1. L'origine (0,0) est le coin
			// Sud-Ouest de l'orthophoto. Le coin Nord-Est correspond aux
			// coordonn�es maximales (1,1).
			MNT_graphique.setTextureCoordinate(0, i, new TexCoord2f(
					(float) (point3d.x - Xmin + RX) / EX, (float) (point3d.y
							- Ymin + RY)
							/ EY));
		}

		return texture;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	public Appearance set_apparence(int polygon_mode, int culling_mode) {
		/**
		 * FONCTION : D�finir les param�tres d�finissant l'apparence de l'objet,
		 * dans le cas o� il n'y a pas d'orthophoto.
		 * 
		 * PARAMETRES EN ENTREE : - polygon_mode [Scalaire, entier] : choix de
		 * repr�sentation des polygones : PolygonAttributes.LINE (filaire) ou
		 * PolygonAttributes.FILL (surface). - culling_mode [Scalaire, entier] :
		 * choix de culling : PolygonAttributes.CULL_NONE,
		 * PolygonAttributes.CULL_FRONT ou PolygonAttributes.CULL_BACK.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Appearance] : L'apparence
		 * de l'objet.
		 **/

		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(culling_mode); // Seules les faces orient�es dans
												// le sens direct par rapport �
												// la cam�ra seront visibles =>
												// pas de vision en sous-sol
		polyAttrib.setPolygonMode(polygon_mode);
		app.setPolygonAttributes(polyAttrib);

		return app;
	}

	// ---------------------------------------------------

	// ---------------------------------------------------
	public Appearance set_apparence(Texture texture, int polygon_mode,
			int culling_mode) {
		/**
		 * FONCTION : D�finir les param�tres d�finissant l'apparence de l'objet,
		 * dans le cas o� il y une pas d'orthophoto.
		 * 
		 * PARAMETRES EN ENTREE : - texture [Scalaire, objet de la classe
		 * Texture] : Texture (orthophoto) appliqu�e � l'objet (MNT). -
		 * polygon_mode [Scalaire, entier] : choix de repr�sentation des
		 * polygones : PolygonAttributes.LINE (filaire) ou
		 * PolygonAttributes.FILL (surface). - culling_mode [Scalaire, entier] :
		 * choix de culling : PolygonAttributes.CULL_NONE,
		 * PolygonAttributes.CULL_FRONT ou PolygonAttributes.CULL_BACK.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Appearance] : L'apparence
		 * de l'objet.
		 **/

		Appearance app = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE); // Seules les faces
																// orient�es
																// dans le sens
																// direct par
																// rapport � la
																// cam�ra seront
																// visibles =>
																// pas de vision
																// en sous-sol
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL); // Aspect
																	// surfacique
		app.setPolygonAttributes(polyAttrib);

		TextureAttributes textureAttributes = new TextureAttributes(
				TextureAttributes.REPLACE, // La couleur de la texture remplace
											// celle de l'objet
				new Transform3D(), new Color4f(0.0f, 0.0f, 0.0f, 0.0f),
				TextureAttributes.NICEST); // Meilleure interpolation possible
											// pour la correction de la
											// perspective
		app.setTextureAttributes(textureAttributes);
		app.setTexture(texture);

		return app;
	}

	// ---------------------------------------------------

	/****************************************************/
	
	/*//////////////////////////////////////////////////*/
}
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/