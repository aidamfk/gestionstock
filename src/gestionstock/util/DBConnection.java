package gestionstock.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // DATABASE URL
    // jdbc:sqlite means we are using SQLite
    // gestionstock.db is the database file name
    private static final String URL = "jdbc:sqlite:gestionstock.db";

    // This method opens and returns a connection to the database
    public static Connection getConnection() throws SQLException {

        // DriverManager connects Java to the SQLite database
        // It uses the URL defined above
        return DriverManager.getConnection(URL);
    }
}
