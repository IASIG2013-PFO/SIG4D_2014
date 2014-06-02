package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPostgreSQL {

	//blabla2
	//blabla3
	//bkkaka
	
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
	
	/**
	 * Méthode qui va nous retourner notre instance
	 * et la créer si elle n'existe pas...
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
	
	
}
