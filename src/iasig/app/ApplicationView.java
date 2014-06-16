package iasig.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;

import iasig.mobile.elements.Dirigeable;
import iasig.mobile.elements.VoitureLibre;
import iasig.univers.view.World;

public class ApplicationView {

public ApplicationView() throws SQLException, IOException{
	World test = new World(700,700);
	VoitureLibre car = new VoitureLibre(
			948000.0f, 6532000.0f, 650f ,4f,2f,1f,2);
	Dirigeable diri = new Dirigeable(948000.0f, 6532000.0f, 1500);
 	test.AddVehicule(diri);
 	test.AddVehicule(car);
	test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	test.DisplayWorld();
}
}
