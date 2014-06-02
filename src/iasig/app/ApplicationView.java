package iasig.app;

import java.io.IOException;
import java.sql.SQLException;

import iasig.mobile.elements.VoitureLibre;
import iasig.mobile.view.World;

public class ApplicationView {

public ApplicationView() throws SQLException, IOException{
	World test = new World(700,700);
	/*VoitureLibre car = new VoitureLibre(500*Constante.DX),(float)(500*Constante.DY),
			((float)test.GetZMNTPlan(500*Constante.DX,500*Constante.DY)
			948000.0f, 6532000.0f, 650f ,4f,2f,1f,2);*/
	//Dirigeable diri = new Dirigeable(100, 100, 100);
 	//test.AddVehicule(car);
	test.DisplayWorld();
}
}
