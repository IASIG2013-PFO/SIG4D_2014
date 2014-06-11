package iasig.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe STATIQUE definissant la connection sur une base de donnée postgreSQL
 * requiert les drivers JDBC Postgresql spécifiques dans le classpath
 * Conserve mot de passe en clair
 */
public class ConnectionPostgreSQL {


	/**
	 * URL de connection
	 */
	private static String url = "jdbc:postgresql://localhost:5432/SIG4D";
	/**
	 * Nom du user
	 */
	private static String user = "postgres";
	/**
	 * Mot de passe du user
	 */
	private static String passwd = "1969_Rhodes";
	/**
	 * Objet Connection
	 */
	private static Connection connect;
	
	
	
	/*/////////////////////METHODES/////////////////////*/
	
	/**
	 * Méthode qui retourne notre instance de connection
	 * et la créer si elle n'existe pas.
	 */
	public static Connection getInstance(){
		if(connect == null){
			try {
				connect = DriverManager.getConnection(url, user, passwd);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.print("No connection ");
			}
		}		
		return connect;	
	}	
	
	
	/***********************SETTERS**********************/
	//---------------------------------------------------
	public void setURL_de_connection(String url_user){
		url = url_user; 
	}
	//---------------------------------------------------

	
	//---------------------------------------------------	
	public void setUser_de_connection(String user_user){
		user = user_user;
	}
	//---------------------------------------------------

	//---------------------------------------------------	
	public void setPassw_de_connection(String user_passwd){
		passwd = user_passwd;
	}
	//---------------------------------------------------
	/****************************************************/	

	
	
	/***********************GETTERS**********************/
	//---------------------------------------------------
	public String getURL_de_connection(){
		return url; 
	}
	//---------------------------------------------------

	//---------------------------------------------------
	public String getPassw_de_connection(){
		return passwd; 
	}
	//---------------------------------------------------

	//---------------------------------------------------
	public String getUser_de_connection(){
		return user; 
	}
	//---------------------------------------------------
	/****************************************************/	

}
