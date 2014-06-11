/**
 * 
 */
package iasig.dao;

import iasig.mobile.view.MNT;
import iasig.mobile.view.Tuile;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Jean-Fran�ois
 *
 */
public class MNTDAO extends DAO {
	
	public MNTDAO(){
		//Appel au constructeur de la classe m�re
		super();
	}
	
public MNT load_mnt(int i, int j, int r){
		 
	    //conversion des valeurs de i et j vers l'�quivalent en indice
	    int id=i+j*99+1;

	    int res = 5*Tuile.resolution[r-1];
	    
	    int nx = Tuile.DX/res + 1;
	    int ny = Tuile.DY/res + 1;
	    
	    int xmin = Tuile.Xmin+i*Tuile.DX;
	    int ymin = Tuile.Ymin+j*Tuile.DY;
	    
	    int xmax=xmin+Tuile.DX;
	    int ymax=ymin+Tuile.DY;
	    
	    int zmin = 999999999;
	    int zmax = -999999999;
	    
	    int[][] m=new int[ny][nx];
	    
	    try {
	
	        ResultSet result = this .connect
	                                .createStatement(
	                                        	ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                                            ResultSet.TYPE_SCROLL_INSENSITIVE
	                                         ).executeQuery(
	        
	        "SELECT x, y, ST_Value(rast, 1, x, y) As z FROM mnt_"+res+" CROSS JOIN generate_series(1,"+nx+") As x CROSS JOIN generate_series(1,"+ny+") As y WHERE rid='"+id+"';");
	       
	        while(result.next()){
				int pi=result.getInt("y");
				int pj=result.getInt("x");
				int z=result.getInt("z");
				
				if(z<zmin){
					zmin=z;
				}
				else{}
				
				if(z>zmax){
					zmax=z;
				}
				else{}

				m[pi-1][pj-1]=z;			
	        }
	    }
    	catch (SQLException e) {
	            e.printStackTrace();
	    }
	    
	    return new MNT(nx,ny,xmin,xmax,res,ymin,ymax,res,zmin,zmax,m);
	}
	
}
