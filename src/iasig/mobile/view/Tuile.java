package iasig.mobile.view;

import iasig.dao.MNTDAO;
import iasig.dao.OrthoDAO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;

import com.sun.j3d.utils.image.TextureLoader;

/**
 * @author Jean
 *
 */
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
public class Tuile {
	/** Cette classe definit une tuile, c'est a dire un carre de MNT associe a un carre d'orthophoto, a une certaine resolution.*/
	/** Le maillage est defini par les constantes.*/
	
	/*/////////////////////CONSTANTES///////////////////*/

	/********************OBJETS DE DAO*******************/
	public static final MNTDAO mntDAO =new MNTDAO(); //Creation du loader de MNT
	public static final OrthoDAO orthoDAO=new OrthoDAO(); //Creation du loader d'orthophotos
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

	
	
	/****************CLASSES DE RESOLUTION***************/
	//La resolution de base est celle de l'orthophoto
	//Resolution MNT = 5 * resolution de base
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
	int i_maille; //coordonnees dans le maillage
	int j_maille; //coordonnees dans le maillage
	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/*********************CONSTRUCTEUR*******************/

	//---------------------------------------------------
	public Tuile(int i, int j, int r) throws IOException{
		/** 
		 * FONCTION :
		 * Creer la tuile d'indices i, j, a la resolution r
		 * 
		 * PARAMETRES EN ENTREE :
		 * - i [Scalaire, entier] : indice de ligne de la tuile a charger
		 * - j [Scalaire, entier] : indice de colonne de la tuile a charger
		 * - r [Scalaire, entier] : classe de resolution choisie (1-6)
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe Tuile] : une nouvelle tuile de position voulue.
		 * */
		
		if(i<0 || i>PX-1 || j<0 || j>PY || masque_noir[i][j]){
			mnt = null;
			ortho = null;
			return;
		}
		
		//Recuperation du mnt dans la base de donnees
		mnt= mntDAO.load_mnt(i, j, r);

		//Recuperation de l'orthophoto dans la base de donnees
		ortho = orthoDAO.load_ortho(i, j, r);
	}
	//---------------------------------------------------
	
	/****************************************************/
	
	
	
	/***********************GETTERS**********************/
	
	//---------------------------------------------------
	public MNT getMNT(){
		/** 
		 * FONCTION :
		 * Recuperer l'attribut private mnt.
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
			 * Recuperer l'attribut private ortho.
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
			 * Recuperer l'attribut private i_maille.
			 */
			return this.i_maille;
		}
	//---------------------------------------------------
	
	//---------------------------------------------------
		public int get_j_maille(){
			/** 
			 * FONCTION :
			 * Recuperer l'attribut private j_maille.
			 */
			return this.j_maille;
		}
	//---------------------------------------------------
		
	/****************************************************/	
		
		
		
	/*******************AUTRES METHODES******************/
	
	//---------------------------------------------------
	public BranchGroup draw(boolean b_orthophoto) throws IOException{

		TriangleArray MNT_graphique = trace_MNT(mnt); // Definition de la
														// geometrie du MNT
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
					PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE); // Definition
																					// de
																					// l'apparence
																					// du
																					// MNT
																					// graphique
																					// incluant
																					// les
																					// parametres
																					// de
																					// la
																					// texture
		} else { // WITHOUTORTHO
			apparence = set_apparence(PolygonAttributes.POLYGON_LINE,
					PolygonAttributes.CULL_NONE); // Definition de l'apparence
													// du MNT graphique non
													// texture
		}
			
		BranchGroup bg = new BranchGroup();
		
		// Shape du MNT graphique = Geometrie + apparence
		bg.addChild(new Shape3D(MNT_graphique, apparence));
		
		return bg;
	}
	//---------------------------------------------------
	
	// ---------------------------------------------------
	public TriangleArray trace_MNT(MNT mnt) {
		/**
		 * FONCTION : Construire la geometrie du MNT, un ensemble de triangles
		 * 3D. Chaque maille carree est divisee en deux de ces triangles par une
		 * diagonale.
		 * 
		 * PARAMETRES EN ENTREE : - [Scalaire, objet de la classe MNT] : MNT
		 * source.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe TriangleArray] : Geometrie
		 * du MNT sous forme de triangles 3D.
		 **/

		// Instanciation de la geometrie avec indication du nombre total de
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
//						// Prevoir un trou pour le tunnel
//						continue;
//					} else {
//					}

				// TRACE DE LA MAILLE D'ORIGINE (X,Y) (COIN SUD-OUEST)

				k = (i + j) % 2; // Parametre d'orientation de la diagonale

				// Calcul des coordonnees (X,Y,Z) des 4 coins de la maille
				// La diagonale correspond a [p2 p3]. p1 et p2 sont au Sud, p3
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

				for (int m = 0; m <= 5; m++) { // Definition de la couleur en
												// fonction de la resolution
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
		 * FONCTION : Creer la texture orthophoto associe au MNT. Les
		 * coordonnees textures sont calculees regulierement en associant chaque
		 * sommet a sa position dans l'image.
		 * 
		 * PARAMETRES EN ENTREE-SORTIE : - MNT_graphique [Scalaire, objet de la
		 * classe TriangleArray] : MNT a plaquer. En sortie chaque sommet de
		 * triangle est muni de ses coordonnees texture.
		 * 
		 * PARAMETRE EN ENTREE : - ortho [Scalaire, objet de la classe
		 * BufferedImage] : Image de l'orthophoto a utiliser pour la texture. -
		 * Xmin [Scalaire, entier] : Borne Ouest du MNT. - Ymin [Scalaire,
		 * entier] : Borne Nord du MNT. - Xmax [Scalaire, entier] : Borne Est du
		 * MNT. - Ymax [Scalaire, entier] : Borne Sud du MNT. - RX [Scalaire,
		 * entier] : Largeur d'une maille de MNT. - RY [Scalaire, entier] :
		 * Hauteur d'une maille de MNT.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Texture] : La texture
		 * creee a partir de l'orthophoto.
		 **/

		Texture texture = new TextureLoader(ortho).getTexture(); // Chargement
																	// de la
																	// texture e
																	// partir de
																	// l'image
		Point3d point3d = new Point3d();

		// Etendue geographique de la maille
		int EX = Xmax - Xmin + 2 * RX;
		int EY = Ymax - Ymin + 2 * RY;

		for (int i = 0; i < MNT_graphique.getVertexCount(); i++) {
			MNT_graphique.getCoordinate(i, point3d); // Recuperation des
														// coordonnees (X,Y,Z)
														// du ie point de la
														// geometrie
			// Calcul des coordonnees textures de ce point. Systeme de
			// coordonnees : (x,y) variant de 0 a 1. L'origine (0,0) est le coin
			// Sud-Ouest de l'orthophoto. Le coin Nord-Est correspond aux
			// coordonnees maximales (1,1).
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
		 * FONCTION : Definir les parametres definissant l'apparence de l'objet,
		 * dans le cas oe il n'y a pas d'orthophoto.
		 * 
		 * PARAMETRES EN ENTREE : - polygon_mode [Scalaire, entier] : choix de
		 * representation des polygones : PolygonAttributes.LINE (filaire) ou
		 * PolygonAttributes.FILL (surface). - culling_mode [Scalaire, entier] :
		 * choix de culling : PolygonAttributes.CULL_NONE,
		 * PolygonAttributes.CULL_FRONT ou PolygonAttributes.CULL_BACK.
		 * 
		 * RETOURNE : - [Scalaire, objet de la classe Appearance] : L'apparence
		 * de l'objet.
		 **/

		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(culling_mode); // Seules les faces orientees dans
												// le sens direct par rapport e
												// la camera seront visibles =>
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
		 * FONCTION : Definir les parametres definissant l'apparence de l'objet,
		 * dans le cas oe il y une pas d'orthophoto.
		 * 
		 * PARAMETRES EN ENTREE : - texture [Scalaire, objet de la classe
		 * Texture] : Texture (orthophoto) appliquee a l'objet (MNT). -
		 * polygon_mode [Scalaire, entier] : choix de representation des
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
																// orientees
																// dans le sens
																// direct par
																// rapport a la
																// camera seront
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