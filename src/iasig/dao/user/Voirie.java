package iasig.dao.user;


import org.postgis.MultiLineString;
import org.postgis.PGgeometry;


public class Voirie {
	
	
	private String id;
	private String nature;
	private String numero;
	private String nom_rue_g;		
	private String sens;
	private String importance;
	private PGgeometry geom;
	protected String table = "Routes74";
	
	
	
	
		//constructeur1 - vide
		public Voirie(){}
		//constructeur2
		public Voirie(String id, String nature, String numero, String nom_rue_g	, String sens, String importance,
				PGgeometry geom){
			this();
			this.id = id;
			this.nature = nature;
			this.numero = numero;
			this.nom_rue_g = nom_rue_g;
			this.sens = sens;
			this.importance = importance;
			this.geom = geom;
		
			}
	
	
		//méthodes publique Accesseur
		public PGgeometry getGeom()
		{
			return geom;
		}
		
		public String getNature(){
			return nature;
		}
		
		public String getId(){
			return id;
		}
		
		public MultiLineString getMultiLineString(){
			MultiLineString LS = (MultiLineString)geom.getGeometry();
			return LS;
		}
	
		public String getSens()
		{
			return sens;
		}
		
		public String getImportance()
		{
			return importance;
		}

		public String getnom_rue_g()
		{
			return nom_rue_g;
		}
		
		public String getToponyme()
		{
			return numero;
		}
		
}

