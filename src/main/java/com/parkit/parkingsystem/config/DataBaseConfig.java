package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * 
 */
public class DataBaseConfig {

	/**
	 * 
	 */
	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {

		logger.info("Create DB connection");

		Class.forName("com.mysql.cj.jdbc.Driver");

		return DriverManager.getConnection(
			"jdbc:mysql://localhost:4000/projet_4_Park'it?useSSL=true&serverTimezone=Europe/Paris",
			"parkit", 
			"123456789"
		);

	}

	/**
	 * @param connexion
	 */
	public void closeConnection(Connection connexion) {

		if (connexion != null) {

			try {

				connexion.close();
				logger.info("Closing DB connection");

			} catch (SQLException e) {

				logger.error("Error while closing connection", e);

			}

		}

	}

	/**
	 * @param ps
	 */
	public void closePreparedStatement(PreparedStatement ps) {

		if (ps != null) {

			try {

				ps.close();

				logger.info("Closing Prepared Statement");

			} catch (SQLException e) {

				logger.error("Error while closing prepared statement", e);

			}

		}

	}

	/**
	 * @param rs
	 */
	public void closeResultSet(ResultSet rs) {

		if (rs != null) {

			try {

				rs.close();

				logger.info("Closing Result Set");

			} catch (SQLException e) {

				logger.error("Error while closing result set", e);

			}

		}

	}

}
