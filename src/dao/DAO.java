package dao;

import dao.ConnectionPostgreSQL;

import java.sql.Connection;



public abstract class DAO {
	
		protected  Connection connect = ConnectionPostgreSQL.getInstance();
		protected static Connection connection_statique = ConnectionPostgreSQL.getInstance();
		
	}






