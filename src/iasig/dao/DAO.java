package iasig.dao;

import iasig.dao.ConnectionPostgreSQL;

import java.sql.Connection;


/**
 * Classe instanciant une connection sur la BDD
 */
public abstract class DAO {
	
		protected  Connection connect = ConnectionPostgreSQL.getInstance();
		protected static Connection connection_statique = ConnectionPostgreSQL.getInstance();
		
	}






