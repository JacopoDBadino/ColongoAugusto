package it.polito.tdp.LATE.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;



/**
 * Utility class for connecting to the database
 * 
 * Uses the HikariCP library for managing a connection pool
 * @see <a href="https://brettwooldridge.github.io/HikariCP/">HikariCP</a>
 */
public class ConnectDB {
	
	private static final String jdbcURL = "jdbc:mysql://localhost/faa_database";
	private static HikariDataSource ds;
	
	public static Connection getConnection() {
		if(ds == null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			config.setPassword("password");
			
			config.addDataSourceProperty("cachePrepStmts", true);
			config.addDataSourceProperty("prepStmtChacheSize", 250);
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			ds = new HikariDataSource(config);
		}
		
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore di connessione ad db");
			throw new RuntimeException(e);
		}
	}

	

}

