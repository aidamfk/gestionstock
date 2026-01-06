package gestionstock.util;

import java.sql.Connection;
import java.sql.Statement;

public class InitDB {

    public static void main(String[] args) {

        // OPEN CONNECTION TO DATABASE
        // Statement is used to execute SQL commands (DDL: CREATE TABLE)
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement()) {

            // CREATE TABLE categorie
            // IF NOT EXISTS prevents error if table already exists
            st.execute("""
                CREATE TABLE IF NOT EXISTS categorie (
                    idCategorie INTEGER PRIMARY KEY,
                    libelle TEXT
                )
            """);

            // CREATE TABLE produit
            // This table stores all products information
            st.execute("""
                CREATE TABLE IF NOT EXISTS produit (
                    codeProduit TEXT PRIMARY KEY,
                    designation TEXT,
                    prixUnitaire REAL,
                    quantiteStock INTEGER,
                    seuilMin INTEGER,
                    categorie TEXT
                )
            """);

            // CREATE TABLE mouvement
            // This table stores stock history (entries / exits)
            st.execute("""
                CREATE TABLE IF NOT EXISTS mouvement (
                    idMouvement INTEGER PRIMARY KEY AUTOINCREMENT,
                    dateMouvement TEXT,
                    quantite INTEGER,
                    type TEXT,
                    codeProduit TEXT,
                    idUser INTEGER
                )
            """);

            // If all tables are created successfully
            System.out.println("DB READY");

        } catch (Exception e) {
            // Display SQL or connection errors
            e.printStackTrace();
        }
    }
}
