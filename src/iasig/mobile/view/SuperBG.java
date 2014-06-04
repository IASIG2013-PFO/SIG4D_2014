package iasig.mobile.view;

import java.io.IOException;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

public class SuperBG extends BranchGroup {
	
	public Shape3D mnt_plaque;
	public BranchGroup objets;
	
	
	/**
	 * Super Branchgroupe
	 * Cree une tuille i, j, r. Appel a tuie.draw pour creer le mnt_plaque
	 * @param args
	 * @throws IOException 
	 */
	public SuperBG(Tuile tuile, Vector<Object> vecteur_objet) throws IOException {
		super();
		this.mnt_plaque=tuile.draw(World.WITHORTHO);
		
		this.objets = new BranchGroup();
		
		Objet3d.dessin_obj_vecteur(this.objets, World.tabobj[2].pieces, vecteur_objet);
		
		this.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		
		this.addChild(mnt_plaque);
		this.addChild(objets);
	}
	
	//Constructeur par copie
	public SuperBG(SuperBG sbg){
		
			this.mnt_plaque = sbg.mnt_plaque;
			this.objets = sbg.objets;
		
	}

}
