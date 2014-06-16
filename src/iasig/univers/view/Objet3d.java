package iasig.univers.view;
/** @author emilie
 *
 */
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;


import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import iasig.dao.user.Arbre;
import iasig.dao.user.Lampadaire;
import iasig.dao.user.Maison;
import iasig.dao.user.Voirie;

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



		 /** dessin_objets
		  * 
		  * fonction de dessin de tous les objets (ponctuels, lineaires, surfaciques)
		  * 
		  * */
		 public static  void dessin_objets(BranchGroup objets, ArrayList<Object> vecteur_objet) {
			 
			//creation d'un branchgroup par type d'objet
				BranchGroup objets_ponctuels = new BranchGroup();
				 BranchGroup objets_lineaires = new BranchGroup();
				 BranchGroup objets_surfaciques = new BranchGroup();
				
				 
				 //tri des objets en fonction de leur type (ponctuel, lineaire ou surfacique)
				 ArrayList<Object> objptarbre = new ArrayList<>();
				 ArrayList<Object> objptlamp = new ArrayList<>();
				 ArrayList<Object> objptfeux = new ArrayList<>();
				 ArrayList<Object> objlin = new ArrayList<>();
				 ArrayList<Object> objsurf = new ArrayList<>();
				 
				 for (int j = 0; j < vecteur_objet.size(); j++) {
					 if (vecteur_objet.get(j) instanceof Arbre) {
						objptarbre.add(vecteur_objet.get(j)); 
					 }else if (vecteur_objet.get(j) instanceof Lampadaire) {
						objptlamp.add((Object) vecteur_objet.get(j)); 
					 }else if (vecteur_objet.get(j) instanceof Maison) {
							objptlamp.add((Object) vecteur_objet.get(j)); 
						 }
					 if (vecteur_objet.get(j) instanceof Voirie) {
						objlin.add((ObjetLineaire) vecteur_objet.get(j)); 
					 }
//					 if (vecteur_objet.get(j) instanceof Route || vecteur_objet.get(j) instanceof Hydro) {
//						objlin.add((ObjetLineaire) vecteur_objet.get(j)); 
//					 }
//					 if (vecteur_objet.get(j) instanceof Bati) {
//						objsurf.add((ObjetSurfacique) vecteur_objet.get(j)); 
//					 }
				 }
			
				 //dessin objets ponctuels
				 //lampadaires
				 //gestion de l'affichage des objets :
				 boolean bool_lampadaire=true;
				 if (bool_lampadaire) {
					 Objet3d.dessin_obj_vecteur(objets_ponctuels, World.tabobj[0].pieces, objptlamp);					
				}
				 Objet3d.dessin_obj_vecteur(objets_ponctuels, World.tabobj[0].pieces, objptlamp);
				 //arbres
				 Objet3d.dessin_obj_vecteur(objets_ponctuels, World.tabobj[3].pieces, objptarbre);
				 //panneaux
				 Objet3d.dessin_obj_vecteur(objets_ponctuels, World.tabobj[1].pieces, objptfeux);
				//dessin objets lineaires
				 Objet3d.dessin_lin_vecteur(objets_lineaires, objlin);
//				//dessin objets surfaciques
//				 Objet3d.dessin_surf_vecteur(objets_surfaciques, objsurf);


					objets.addChild(objets_ponctuels);
					objets.addChild(objets_lineaires);
					objets.addChild(objets_surfaciques);
				
		}



		 /** dessin_obj_vecteur
		  * 
		  * dessine tous les objets situes dans la liste d'objets java vect. -> dans cette liste il n'y a que des objets
		  *  de meme type : celui defini dans le tableau pieces.
		  * pieces : tableau de l'ensemble des pieces d'un objet .obj
		  * vect : ensemble des objets java a dessiner dans la scene
		  * 
		  * */
		 public static  void dessin_obj_vecteur(BranchGroup bg, Shape3D[] pieces, ArrayList<Object> vect) {
			 
			 if (vect.size()==0) {
				return;
			 }
			
			 for (int j = 0; j < vect.size(); j++) {
				
				//creation du BranchGroup Objet
			   	BranchGroup bgObj = new BranchGroup ( ) ;
			   	//creation du TransformGroup associ� au BranchGroup bjObj			        
				TransformGroup tgObj = new TransformGroup ( ) ;
				
				ObjetPonctuel objpt = new ObjetPonctuel();
				//transtypage de l'objet en ObjetPonctuel
				if (vect.get(j) instanceof Arbre) {
					Arbre lamp = (Arbre)vect.get(j);
					objpt = new ObjetPonctuel(lamp.getId(), lamp.getGeom(), 0, 2, lamp.getType());		
				}else if(vect.get(j) instanceof Lampadaire) {
					Lampadaire lamp = (Lampadaire)vect.get(j);
					objpt = new ObjetPonctuel(lamp.getId(), lamp.getGeom(), 0, 2, lamp.getType());		
				}else if(vect.get(j) instanceof Maison) {
					Maison lamp = (Maison)vect.get(j);
					objpt = new ObjetPonctuel(lamp.getId(), lamp.getGeom(), 0, 2, lamp.getType());		
				}
						
			   	//parametres de translation, rotation et echelle propres a chaque objet
			   	Transform3D transObj=new Transform3D();
			    transObj.setScale(objpt.getf());
			    transObj.setTranslation(new Vector3d( objpt.getX(),  objpt.getY(), objpt.getZ()));

			    transObj.setRotation(new AxisAngle4d( 1,  0, objpt.getRotZ(), Math.PI/2));
			    
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
		 public static  void dessin_lin_vecteur(BranchGroup bg, ArrayList<Object> vect) {
			 
			 if (vect.size()==0) {
				return;
			 }

			 for (int j = 0; j < vect.size(); j++) {
System.out.println("j="+j);				
				//creation du BranchGroup Objet
			   	BranchGroup bgObj = new BranchGroup ( ) ;
			   	//creation du TransformGroup associe au BranchGroup bjObj			        
				TransformGroup tgObj = new TransformGroup ( ) ;
System.out.println(vect.get(j).getClass().getName());	

			 }
			    //dessin du lin�aire
//				MultiLineString ml = (MultiLineString) vect.elementAt(0).getCentroid().getGeometry();
//
//System.out.print(vect.elementAt(j).getCentroid().getGeometry()+"\n");
//
//				int nbpoint = ml.numPoints();
//System.out.print("  nb points  "+nbpoint+"\n");
//			 	int[] tab=new int[1];
//			 	for (int i = 0; i < tab.length; i++) {
//					tab[i]=nbpoint;
//				}
//			 	
//			 	LineStripArray ligne = new LineStripArray(nbpoint, LineStripArray.COORDINATES|LineStripArray.COLOR_3,tab);
//			 	
//			 	Point3f[] points = new Point3f[nbpoint];
//			 	for (int i = 0; i < nbpoint; i++) {
//			 		 points[i] = new Point3f((float)ml.getPoint(i).x,(float)ml.getPoint(i).y,(float)ml.getPoint(i).z);	
//				}
//			 	
//				ligne.setCoordinates(0, points);
//			 	ligne.setColor(1, new Color3f(1.f,0.f,0.f));
//				
//			   	bgObj.addChild ( tgObj ) ;
//			    transform.addChild(bgObj);
//			}//endfor(j)
		     
		}

}
