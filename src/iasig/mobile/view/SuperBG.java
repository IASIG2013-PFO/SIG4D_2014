package iasig.mobile.view;

import java.io.IOException;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

public class SuperBG {
	
	public BranchGroup sbg;
	public Shape3D mnt_plaque;
	public BranchGroup objets;
	int i;
	int j;
	
	/**
	 * Super Branchgroupe
	 * Cree une tuille i, j, r. Appel a tuie.draw pour creer le mnt_plaque
	 * @param args
	 * @throws IOException 
	 */
	public SuperBG(Tuile tuile, Vector<Object> vecteur_objet) throws IOException {
		this.mnt_plaque=tuile.draw(World.WITHORTHO);
		
		this.objets = new BranchGroup();
		
		Objet3d.dessin_obj_vecteur(this.objets, World.tabobj[2].pieces, vecteur_objet);
		
		sbg = new BranchGroup();
		
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.sbg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		
		this.sbg.addChild(mnt_plaque);
		//this.sbg.addChild(objets);
		
		this.i=tuile.i_maille;
		this.j=tuile.j_maille;
	}
	
	//Constructeur par copie
	public SuperBG(SuperBG s){
		
			this.mnt_plaque = s.mnt_plaque;
			this.objets = s.objets;
			this.sbg = s.sbg;
			this.i = s.i;
			this.j = s.j;
	}

}
