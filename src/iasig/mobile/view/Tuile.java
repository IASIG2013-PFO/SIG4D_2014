package iasig.mobile.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.vecmath.Point2i;

import dao.MNTDAO;
import dao.OrthoDAO;

/**
 * @author jean
 *
 */
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/
public class Tuile {
	/** Cette classe définit une tuile, c'est à dire un carré de MNT associé à un  carré d'orthophoto, à une certaine résolution.*/
	/** Le maillage est défini par les constantes.*/
	
	/*/////////////////////CONSTANTES///////////////////*/

	/********************OBJETS DE DAO*******************/
	public static final MNTDAO mntDAO =new MNTDAO(); //Création du loader
	public static final OrthoDAO orthoDAO=new OrthoDAO(); //Création du loader
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
	public static final boolean DB = true; //Charger à partir de la BDD
	public static final boolean FILE = false; //Charger à partir de fichiers
	
	public static final boolean LOADALL = true; //Charger les MNT déjà découpés
	public static final boolean DIVISION = false; //Générer les MNT par division d'un MNT plus grand
	/****************************************************/
	
	
	
	/****************CLASSES DE RESOLUTION***************/
	//La résolution de base est celle de l'orthophoto
	//Résolution MNT = 5 * résolution de base
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
	int i_maille; //coordonnées dans le maillage
	int j_maille; //coordonnées dans le maillage
	/*//////////////////////////////////////////////////*/
	
	
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/*********************CONSTRUCTEUR*******************/
	

	//---------------------------------------------------
	public Tuile(int i, int j, int r, boolean source) throws IOException{
		/** 
		 * FONCTION :
		 * Créer la tuile d'indices i, j, à partir de la souce indiquée
		 * 
		 * PARAMETRES EN ENTREE :
		 * - i [Scalaire, entier] : indice de ligne de la tuile à charger
		 * - j [Scalaire, entier] : indice de colonne de la tuile à charger
		 * - r [Scalaire, entier] : classe de résolution choisie (1-6)
		 * - source [Scalaire, booléen] : provenance : soit DB pour la base de données, soit FILE pour les fichiers
		 * 
		 * CONSTRUIT :
		 * - [Scalaire, objet de la classe Tuile] : une nouvelle tuile de position voulue.
		 * */
	
		int res = resolution[r-1];

		if(source){ //DB
			//Récupération du mnt dans la base de données
			mnt= mntDAO.load_mnt(i, j, r);

			//Récupération de l'orthophoto dans la base de données
			ortho = orthoDAO.load_ortho(i, j, r);
			
			//calcul des coordonnées du maillage
			int X=Xmin+i*DX; //
			int Y=Ymin+j*DY; // Coin Sud-Ouest du MNT à charger

			this.i_maille=i;
			this.j_maille=j;
			
		}
		else{ //FILE
			String file_mnt; //Chemin du fichier de MNT
			String file_ortho; //Chemin du fichier d'ortho
			
			int X=Xmin+i*DX; //
			int Y=Ymin+j*DY; // Coin Sud-Ouest du MNT à charger
			
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
		 * Récupérer l'attribut private mnt.
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
			 * Récupérer l'attribut private ortho.
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
			 * Récupérer l'attribut private i_maille.
			 */
			return this.i_maille;
		}
	//---------------------------------------------------
	
	//---------------------------------------------------
		public int get_j_maille(){
			/** 
			 * FONCTION :
			 * Récupérer l'attribut private j_maille.
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
		 * - [Scalaire, booléen] : VRAI si la tuile est totalement noire, FAUX sinon.
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
		 * Récupérer le masque matriciel des tuiles noires sur l'ensemble du maillage.
		 * 
		 * RETOUR :
		 * - [Matrice<PY lignes, PX colonnes> d'entiers] : masque : l'élément (i,j) est VRAI si et seulement si la tuile (i,j) est noire.
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
		 * Récupérer le centre du rectangle englobant une collection de tuiles.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - listeIndices [Matrice(nombre de lignes quelconque>=1, 3 colonnes) d'entiers] : liste des indices et des classes de résolutions des tuiles de la collection.
		 * 
		 * RETOUR :
		 * - [Scalaire, objet de la classe Point2i] : centre de la sélection, en plani.
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
		
		//Recherche des indices minimaux et maximaux de la sélection
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
		
		//Calcul du rectangle englobant la sélection
		int Xmi=Xmin+imin*DX;
		int Xma=Xmin+(imax+1)*DX;
		
		int Ymi=Ymin+jmin*DY;
		int Yma=Ymin+(jmax+1)*DY;
		
		//Calcul du centre géographique de la sélection
		return new Point2i((Xmi+Xma)/2,(Ymi+Yma)/2);
	}
	//---------------------------------------------------	
	
	//---------------------------------------------------
	public static Vector<int[]> getAllIndicesBetween(int imin, int jmin, int imax, int jmax, int r){
		/** 
		 * FONCTION :
		 * Récupérer tous le indices de tuiles entre des bornes. Les tuiles noires sont éliminées lors du processus.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - imin [Scalaire, entier] : indice de ligne minimal (borne Nord).
		 * - jmin [Scalaire, entier] : indice de colonne minimal (borne Ouest).
		 * - imax [Scalaire, entier] : indice de ligne maximal (borne Sud).
		 * - jmax [Scalaire, entier] : indice de colonne maximal (borne Est).
		 * - r [Scalaire, entier] : classe de résolution choisi (1-6) pour toutes les tuiles.
		 * 
		 * RETOUR :
		 * - listeIndices [Matrice(nombre de lignes quelconque>=0, 3 colonnes) d'entiers] : liste des indices des tuiles de la sélection et leur classe de résolution (r), tuiles noires exclues.
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
		 * Filtrer une liste d'indices de tuiles en éliminant les noires.
		 * 
		 * PARAMETRE EN ENTREE :
		 * - [Matrice(nombre de lignes quelconque>=1, 3 colonnes) d'entiers] : liste des indices des tuiles de la sélection à filtrer.
		 * 
		 * RETOUR :
		 * - [Matrice(nombre de lignes quelconque>=0, 3 colonnes) d'entiers] : liste des indices des tuiles de la sélection et leur classe de résolution (r), tuiles noires exclues.
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
		 * Charger une liste de tuiles à partir de leurs indices à partir de la BDD ou de fichiers.
		 * 
		 * PARAMETRES EN ENTREE :
		 * - listeIndices [Matrice(nombre de lignes quelconque>=1, 3 colonnes) d'entiers] : liste des indices des tuiles à charger et leurs classes de résolutions.
		 * - source [Scalaire, booléen] : indication de la provenance des données : DB or FILE.
		 * 
		 * RETOUR :
		 * - [Matrice(PY lignes,PX colonnes) d'objets de la classe MNT] : grille de MNT, l'élément (1,1) de la matrice correspond au MNT Nord-Ouest.
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
		

	
//	//---------------------------------------------------
//	public static void main(String[] args) throws IOException {
//		/** 
//		 * FONCTION :
//		 * Programme.
//		 * 
//		 * PARAMETRE EN ENTREE :
//		 * -args [Vecteur(longueur quelconque >=0) de chaînes de caractères] : Eventuels paramètres passés à l'exécuteur. ICI AUCUN.
//		 */
//
//		boolean[][] masque = Tuile.getAllDarkTiles();
//		boolean b;
//		
//		System.out.println("{");
//		for(int i=0;i<PX;i++){
//			System.out.print("{");
//			for(int j=0;j<PY;j++){
//				b=masque[i][j];
//				System.out.print(b+(b?" ":"")+(j!=PY?", ":""));
//			}
//			System.out.println("}"+(i!=PX?", ":""));
//		}
//		System.out.println("}");
//	}
//	//---------------------------------------------------
//	
//	/****************************************************/
	
	/*//////////////////////////////////////////////////*/
}
/*CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC*/