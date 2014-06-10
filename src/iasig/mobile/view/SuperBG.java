package iasig.mobile.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

public class SuperBG {
	
	public BranchGroup sbg;
	public Shape3D mnt_plaque;
	public BranchGroup objets;
	public Tuile tuile;
	
	/**
	 * Super Branchgroupe
	 * Cree une tuille i, j, r. Appel a tuie.draw pour creer le mnt_plaque
	 * @param args
	 * @throws IOException 
	 */
	public SuperBG(Tuile tuile, ArrayList<Object> vecteur_objet) throws IOException {
		
		if(tuile!=null){
			this.mnt_plaque=tuile.draw(World.WITHORTHO);
		}
		else{
			this.mnt_plaque=new Shape3D();
		}
		
		this.tuile = tuile;
		
		this.objets = new BranchGroup();
		
		Objet3d.dessin_obj_vecteur(this.objets, World.tabobj[2].pieces, vecteur_objet);
		
		sbg = new BranchGroup();
		
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.sbg.setCapability(BranchGroup.ALLOW_DETACH);
		
		this.sbg.addChild(mnt_plaque);
		this.sbg.addChild(objets);

	}
	
	//Constructeur par copie
	/**
	 * Constructeur par copie
	 * @param s le SuperBG a copier
	 */
	public SuperBG(SuperBG s){
		this.mnt_plaque = (Shape3D) s.mnt_plaque.cloneTree();
		this.objets = (BranchGroup) s.objets.cloneTree();
		this.sbg = (BranchGroup) s.sbg.cloneTree();
		this.tuile = s.tuile;
	}

}
