package iasig.mobile.view;
/** @author emilie
 *
 */
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import jogamp.opengl.util.jpeg.JPEGDecoder.EXIF;

import org.postgis.MultiLineString;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import dao.user.Arbre;
import dao.user.Lampadaire;

/**
 * @author emilie
 *
 */
public class Objet3d {

	/**
	 * @param args
	 */

    Shape3D[] pieces;
    
  //constructeur
  		public Objet3d(int nbobj, String repobj) {
  			//chargement des pieces 3d
  			this.pieces=charge_piece_obj(nbobj, repobj); //.obj
  		}
 
  		 //constructeur
  		public Objet3d(String nomobj[]) {
  			//chargement des pieces 3d
  			this.pieces=charge_piece_obj_BDD(nomobj); //.obj
  		}
 
		 /** charge_piece_obj
		  * 
		  * chargement dans un tableau de toutes les parties textur�es d'un objet .obj 
		  * ENTREE :
		  * nobj : nombre de parties dont est compos� l'objet
		  * repobj : nom du r�pertoire dans lequel se trouvent les fichiers .obj de l'objet
		  * 
		  * SORTIE :
		  * tableau de Shape3D contenant toutes les parties d'un objets 3D
		  * 
		  * */
		public  Shape3D[]  charge_piece_obj(int nobj, String repobj) {
			
			Shape3D[] pieces=new Shape3D[nobj];
			
			//remplissage du tableau de chemins
			for (int i = 1; i <= nobj; i++) {
			
				Scene s=null;
				ObjectFile loader = new ObjectFile();
//				loader.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY | ObjectFile.REVERSE);
				/* liste des options qu'on peut mettre au chargement :
				ObjectFile.RESIZE : redimensionne l'objet dans un carr� de -1 � 1 centr� en 0
				ObjectFile.TRIANGULATE : change les triangles pour gagner du temps de dessin
				ObjectFile.STRIPIFY : gagne du temp au dessin
				ObjectFile.REVERSE : inverse le sens de dessin des faces
				*/
				
		        try {
		            URL fileUrl = new URL("file:data"+"/"+repobj+"/"+repobj+i+".obj");
		            s = loader.load(fileUrl);
		          }
		          catch (MalformedURLException ex2) {
		            System.out.println("url pas bonne ");
		          } catch (FileNotFoundException e) {
				    System.out.println("file not found ");
					e.printStackTrace();
				} catch (IncorrectFormatException e) {
				    System.out.println("format  incorrect ");
					e.printStackTrace();
				} catch (ParsingErrorException e) {
				    System.out.println("erreur de lecture ");
					e.printStackTrace();
				}
				
				pieces[i-1]=(Shape3D)s.getSceneGroup().getAllChildren().nextElement();			
			}//endfor i

			return pieces;
		}


		 /** charge_piece_obj_BDD
		  * 
		  * chargement dans un tableau de toutes les parties textur�es d'un objet .obj 
		  * ENTREE :
		  * nobj : nombre de parties dont est compos� l'objet
		  * repobj : nom du r�pertoire dans lequel se trouvent les fichiers .obj de l'objet
		  * 
		  * SORTIE :
		  * tableau de Shape3D contenant toutes les parties d'un objets 3D
		  * 
		  * */
		public  Shape3D[]  charge_piece_obj_BDD(String nomobj[]) {
			
			Shape3D[] pieces=new Shape3D[nomobj.length];
			
			//remplissage du tableau de chemins
			for (int i = 0; i < nomobj.length; i++) {
			
				Scene s=null;
				ObjectFile loader = new ObjectFile();

				try {
		            URL fileUrl = new URL(nomobj[i]);
		            s = loader.load(fileUrl);
		          }
		          catch (MalformedURLException ex2) {
		            System.out.println("url pas bonne ");
		          } catch (FileNotFoundException e) {
				    System.out.println("file not found ");
					e.printStackTrace();
				} catch (IncorrectFormatException e) {
				    System.out.println("format  incorrect ");
					e.printStackTrace();
				} catch (ParsingErrorException e) {
				    System.out.println("erreur de lecture ");
					e.printStackTrace();
				}
				
				pieces[i]=(Shape3D)s.getSceneGroup().getAllChildren().nextElement();			
			}//endfor i

			return pieces;
		}


//		 /** dessin_obj_vecteur_tab
//		  * 
//		  * dessine tous les objets situ�s dans le tableau vect. 
//		  * pieces : tableau de l'ensemble des pieces d'un objet .obj
//		  * vect : ensemble des objets java � dessiner dans la sc�ne poss�dant les param�tres (x,y,z,rotx,roty,rotz,f)
//		  * 
//		  * */
//		 public  void dessin_obj_vecteur_tab(BranchGroup bg, Shape3D[] pieces, double[][] vect) {
//			 
//			 for (int j = 0; j < vect.length; j++) {
//				
//				//cr�ation du BranchGroup Objet
//			   	BranchGroup bgObj = new BranchGroup ( ) ;
//			        
//				TransformGroup tgObj = new TransformGroup ( ) ;
//			   	tgObj.setCapability ( TransformGroup.ALLOW_TRANSFORM_READ ) ;
//			   	tgObj.setCapability ( TransformGroup.ALLOW_TRANSFORM_WRITE ) ; 
//				
//			   	Transform3D transObj=new Transform3D();
//			    transObj.setScale(new Vector3d( vect[j][6], vect[j][6], vect[j][6]));
//			    transObj.setTranslation(new Vector3d(vect[j][0], vect[j][1], vect[j][2]));
//			    transObj.setRotation(new AxisAngle4d(vect[j][3], vect[j][4], vect[j][5], Math.PI/2));
//			    
//			    
//			    //objet = ensemble des pieces
//			   	for (int i = 0; i < pieces.length; i++) {
//			   		Shape3D objet=pieces[i];
//				    tgObj.addChild(objet.cloneTree());
//			   	}
//			   			   	
//			   	tgObj.setTransform(transObj);
//			         
//			   	bgObj.addChild ( tgObj ) ;
//			    bg.addChild(bgObj);
//			}//endfor(j)
//		     
//		}

		 
//		 /** dessin_obj_vecteur
//		  * 
//		  * dessine tous les objets situ�s dans le vecteur d'objets java vect. -> dans ce vecteur il n'y a que des objets
//		  *  de m�me type : celui d�fini dans le tableau pieces.
//		  * pieces : tableau de l'ensemble des pieces d'un objet .obj
//		  * vect : ensemble des objets java � dessiner dans la sc�ne
//		  * 
//		  * */
//		 public static  void dessin_obj_vecteur(TransformGroup transform, Shape3D[] pieces, Vector<ObjetPonctuel> vect) {
//			 
//			 System.out.println(vect.size());
//			 if (vect.size()==0) {
//				return;
//			 }
//			
//			 for (int j = 0; j < vect.size(); j++) {
//				
//				//cr�ation du BranchGroup Objet
//			   	BranchGroup bgObj = new BranchGroup ( ) ;
//			   	
//			   	//cr�ation du TransformGroup associ� au BranchGroup bjObj			        
//				TransformGroup tgObj = new TransformGroup ( ) ;
//				
//			   	//param�tres de translation, rotation et �chelle propres � chaque objet
//			   	Transform3D transObj=new Transform3D();
//			    transObj.setScale(vect.elementAt(j).getf());
//			    transObj.setTranslation(new Vector3d( vect.elementAt(j).getX(),  vect.elementAt(j).getY(), vect.elementAt(j).getZ()));
//			    transObj.setRotation(new AxisAngle4d( 1,  0,  vect.elementAt(j).getRotZ(), Math.PI/2));
//			    
//			    //objet = ensemble des pieces
//			   	for (int i = 0; i < pieces.length; i++) {
//			   		Shape3D objet=pieces[i];
//				    tgObj.addChild(objet.cloneTree());
//			   	}
//			   	
//			   	tgObj.setTransform(transObj);
//			         
//			   	bgObj.addChild ( tgObj ) ;
//			    transform.addChild(bgObj);
//			}//endfor(j)
//		     
//		}

		 /** dessin_obj_vecteur
		  * 
		  * dessine tous les objets situ�s dans le vecteur d'objets java vect. -> dans ce vecteur il n'y a que des objets
		  *  de m�me type : celui d�fini dans le tableau pieces.
		  * pieces : tableau de l'ensemble des pieces d'un objet .obj
		  * vect : ensemble des objets java � dessiner dans la sc�ne
		  * 
		  * */
		 public static  void dessin_obj_vecteur(BranchGroup bg, Shape3D[] pieces, ArrayList<Object> vect) {
			 
 //System.out.println(vect.size());
			 if (vect.size()==0) {
				return;
			 }
			
			 for (int j = 0; j < vect.size(); j++) {
				
				//cr�ation du BranchGroup Objet
			   	BranchGroup bgObj = new BranchGroup ( ) ;
			   	
			   	//cr�ation du TransformGroup associ� au BranchGroup bjObj			        
				TransformGroup tgObj = new TransformGroup ( ) ;
				
				//vecteur d'objets ponctuels
				ObjetPonctuel objpt = new ObjetPonctuel();
				
				if (vect.get(j) instanceof Lampadaire) {
					Lampadaire obj = (Lampadaire)(vect.get(j));
		//			System.out.println("lamp: "+ obj.getX1()+"  "+  obj.getY1()+"   "+ obj.getZ1());
					objpt = new ObjetPonctuel(obj.getId(), obj.getGeom(), 0, 2, obj.getType());
				}else if (vect.get(j) instanceof Arbre) {
					Arbre obj = (Arbre)(vect.get(j));
			//		System.out.println("arbre: "+ obj.getX1()+"  "+  obj.getY1()+"   "+ obj.getZ1());
					objpt = new ObjetPonctuel(obj.getId(), obj.getGeom(), 0, 2, obj.getType());
				}
//Lampadaire lamp = (Lampadaire)(vect.elementAt(j));
//
//ObjetPonctuel objpt = new ObjetPonctuel(lamp.getId(), lamp.getCentroid(), 0, 2, "lampadaire");
//				ObjetPonctuel objpt = (ObjetPonctuel)(vect.elementAt(j));
				
			   	//param�tres de translation, rotation et �chelle propres � chaque objet
			   	Transform3D transObj=new Transform3D();
			    transObj.setScale(objpt.getf());
			    transObj.setTranslation(new Vector3d( objpt.getX(),  objpt.getY(), objpt.getZ()));
			    //System.out.println( objpt.getX()+"/"+objpt.getY()+"/"+objpt.getZ());
 //System.out.println( objpt.getX()+"  "+  objpt.getY()+"   "+ objpt.getZ());
 
			    transObj.setRotation(new AxisAngle4d( 1,  0, objpt.getRotZ(), Math.PI/2));
			    
//	System.out.println(j+ " objtype= "+objpt.getType());
			    
			    //objet = ensemble des pieces
			   	for (int i = 0; i < pieces.length; i++) {
			   		Shape3D objet=pieces[i];
				    tgObj.addChild(objet.cloneTree());
			   	}
			   	
			   	tgObj.setTransform(transObj);
			         
			   	bgObj.addChild ( tgObj ) ;
			    bg.addChild(bgObj);
			}//endfor(j)
		     
		}

		 
		 
		 /** dessin_surf_vecteur
		  * 
		  * dessine tous les objets situ�s dans le vecteur d'objets java vect. -> dans ce vecteur il n'y a que des objets
		  *  de m�me type : celui d�fini dans le tableau pieces.
		  * pieces : tableau de l'ensemble des pieces d'un objet .obj
		  * vect : ensemble des objets java � dessiner dans la sc�ne
		  * 
		  * */
		 public static  void dessin_surf_vecteur(TransformGroup transform, Vector<ObjetSurfacique> vect) {
			 
			 if (vect.size()==0) {
				return;
			 }
//			 for (int j = 0; j < vect.size(); j++) {
//				
//				//cr�ation du BranchGroup Objet
//			   	BranchGroup bgObj = new BranchGroup ( ) ;
//			   	
//			   	//cr�ation du TransformGroup associ� au BranchGroup bjObj			        
//				TransformGroup tgObj = new TransformGroup ( ) ;
//							    
//			    //dessin du polygone
//				System.out.println(vect.size());

			
//			         
//			   	bgObj.addChild ( tgObj ) ;
//			    transform.addChild(bgObj);
//			}//endfor(j)
		     
		}

		 
		 /** dessin_lin_vecteur
		  * 
		  * dessine tous les objets situ�s dans le vecteur d'objets java vect. -> dans ce vecteur il n'y a que des objets
		  *  de m�me type : celui d�fini dans le tableau pieces.
		  * pieces : tableau de l'ensemble des pieces d'un objet .obj
		  * vect : ensemble des objets java � dessiner dans la sc�ne
		  * 
		  * */
		 public static  void dessin_lin_vecteur(TransformGroup transform, Vector<ObjetLineaire> vect) {
			 
			 if (vect.size()==0) {
				return;
			 }

			 for (int j = 0; j < vect.size(); j++) {
System.out.println("j="+j);				
				//cr�ation du BranchGroup Objet
			   	BranchGroup bgObj = new BranchGroup ( ) ;
			   	//cr�ation du TransformGroup associ� au BranchGroup bjObj			        
				TransformGroup tgObj = new TransformGroup ( ) ;
							    
			    //dessin du lin�aire
				MultiLineString ml = (MultiLineString) vect.elementAt(0).getCentroid().getGeometry();

System.out.print(vect.elementAt(j).getCentroid().getGeometry()+"\n");

				int nbpoint = ml.numPoints();
System.out.print("  nb points  "+nbpoint+"\n");
			 	int[] tab=new int[1];
			 	for (int i = 0; i < tab.length; i++) {
					tab[i]=nbpoint;
				}
			 	
			 	LineStripArray ligne = new LineStripArray(nbpoint, LineStripArray.COORDINATES|LineStripArray.COLOR_3,tab);
			 	
			 	Point3f[] points = new Point3f[nbpoint];
			 	for (int i = 0; i < nbpoint; i++) {
			 		 points[i] = new Point3f((float)ml.getPoint(i).x,(float)ml.getPoint(i).y,(float)ml.getPoint(i).z);	
				}
			 	
				ligne.setCoordinates(0, points);
			 	ligne.setColor(1, new Color3f(1.f,0.f,0.f));
				
			   	bgObj.addChild ( tgObj ) ;
			    transform.addChild(bgObj);
			}//endfor(j)
		     
		}

}
