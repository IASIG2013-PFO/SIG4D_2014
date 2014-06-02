package iasig.mobile.view;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author emilie
 *
 */
public class URLobj {

	URL[] urlobj;
	String[][] repobj;
	
	//constructeur
	public URLobj() throws MalformedURLException {
		this.repobj = new String[11][2];
		this.repobj[0][0]="feutricolore";
		this.repobj[0][1]="4";
		this.repobj[1][0]="lampadaire";
		this.repobj[1][1]="1";
		this.repobj[2][0]="cone";
		this.repobj[2][1]="1";
		this.repobj[3][0]="arbre2_";
		this.repobj[3][1]="1";
		this.repobj[4][0]="poubelle";
		this.repobj[4][1]="0";
		this.repobj[5][0]="mouton";
		this.repobj[5][1]="1";
		this.repobj[6][0]="maison1";
		this.repobj[6][1]="0";
		this.repobj[7][0]="maison2";
		this.repobj[7][1]="0";
		this.repobj[8][0]="priorite_";
		this.repobj[8][1]="3";
		this.repobj[9][0]="stop_";
		this.repobj[9][1]="3";
		this.repobj[10][0]="banc";
		this.repobj[10][1]="0";
	}
	
	public URL[] getURL(){
		return urlobj;
	}
	public String[][] getTabobj(){
		return repobj;
	}
}
