package iasig.mobile.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

public class SuperBG {
	
	public BranchGroup sbg;
	public BranchGroup mnt_plaque;
	public BranchGroup objets;
	public BranchGroup bati;
	public Tuile tuile;
	
	/**
	 * Super Branchgroup
	 * Cree une tuille i, j, r. Appel a tuie.draw pour creer le mnt_plaque
	 * @param args
	 * @throws IOException 
	 */
	public SuperBG(Tuile tuile, ArrayList<Object> vecteur_objet, ArrayList<Batiment> vecteur_bati) throws IOException {
		

		if(tuile.mnt!=null){
			this.mnt_plaque=tuile.draw(World.WITHORTHO);
		}
		else{
			this.mnt_plaque=new BranchGroup();
		}
		this.mnt_plaque.setCapability(BranchGroup.ALLOW_DETACH);
		this.tuile = tuile;
		
		
	
		this.objets = new BranchGroup();
		this.objets.setCapability(BranchGroup.ALLOW_DETACH);
		Objet3d.dessin_obj_vecteur(this.objets, World.tabobj[2].pieces, vecteur_objet);

		this.bati = new BranchGroup();
		this.bati.setName("BATI");
		this.bati.setCapability(BranchGroup.ALLOW_DETACH);

		Batiment.draw(this.bati, vecteur_bati);

		sbg = new BranchGroup();
		
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.sbg.setCapability(BranchGroup.ALLOW_DETACH);
		

	}
	
	//Constructeur par copie
	/**
	 * Constructeur par copie
	 * @param s le SuperBG a copier
	 */
	public SuperBG(SuperBG s){
	
		
		
		this.mnt_plaque = (BranchGroup) s.mnt_plaque.cloneTree();
		this.objets = (BranchGroup) s.objets.cloneTree();
		this.bati = (BranchGroup) s.bati.cloneTree();
		this.sbg = (BranchGroup) s.sbg.cloneTree();
		

		this.tuile = s.tuile;
		
		this.bati.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.bati.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.bati.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.bati.setCapability(BranchGroup.ALLOW_DETACH);
		
		this.objets.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.objets.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.objets.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.objets.setCapability(BranchGroup.ALLOW_DETACH);
		
		this.mnt_plaque.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.mnt_plaque.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.mnt_plaque.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.mnt_plaque.setCapability(BranchGroup.ALLOW_DETACH);
		
	}

	
	/**
	 * Attache tout les composants de l'univers (mnt, objets, bati)
	 * Methode a appliquer au rafraichissement du buffer ou du visible
	 */
	public void attachAllComponents( ){
		
		this.sbg.addChild(mnt_plaque);
		this.sbg.addChild(objets);
		this.sbg.addChild(bati);
		
	}

	
	/**
	 * Detache tous le mobilier (objets ponctuels; bati;...) composant l'univers
	 * Methode a appliquer au rafraichissement du buffer ou du visible
	 * @param b_mnt_plaque
	 * @param b_objets
	 * @param b_bati
	 */
	public void detachAllComponents(){
	
			this.sbg.removeAllChildren();
			
	}
	

	
	/**
	 * Detache/Attache les Objets; Methode a appliquer au rafraichissement du buffer ou du visible
	 * @param b booleen
	 */
	public void switchObjet(Boolean b){
		
		if ( !b ){
			this.objets.detach();
		}
		else{
			this.sbg.addChild(objets);

		}
	}
	
	/**
	 * Detache/Atache le Bati
	 * @param b booleen
	 */
	public void switchBati(Boolean b){
		
		if ( !b ){
			this.bati.detach();
		}
		else{
			this.sbg.addChild(bati);
		}
	}
	
	/**
	 * Detache/Atache le Bati; Methode a appliquer au rafraichissement du buffer ou du visible
	 * @param b booleen
	 * @param b booleen
	 */
	public void switchTuile(Boolean b){
		
		if ( !b ){
			this.mnt_plaque.detach();
		}
		else{
			this.sbg.addChild(mnt_plaque);
		}
	}
	
	
}


