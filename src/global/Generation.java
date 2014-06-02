package global;

public class Generation {

	private static int taille_de_maille = 100 ;
	private static int taille_buffer_objet = 10 ;
	private static double Xmin = 0 ;
	private static double Ymin = 0 ;
	public static double x0=916000;
	public static double y0=6513000;
	
	//getter static
	public static int get_taille_de_maille(){
		return taille_de_maille; 
	}
	public static int get_taille_buffer_objet(){
		return taille_buffer_objet; 
	}
	public static double get_Ymin() {
		return Ymin ;
	}
	public static double get_Xmin() {
		return Xmin ;
	}
	
	
	//méthodes publique 'Set'
	public static void set_taille_de_maille(int taille_de_maille) {
		Generation.taille_de_maille = taille_de_maille;
	}
	public static void set_taille_buffer_objet(int taille_buffer_objet) {
		Generation.taille_buffer_objet = taille_buffer_objet;
	}
	public static void set_Xmin(double Xmin) {
			Generation.Xmin = Xmin;
	}
	public static void set_Ymin(double Ymin) {
			Generation.Ymin = Ymin;
	}
	
}
