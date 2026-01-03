package gestionstock.util;

import java.sql.Connection;
import java.sql.Statement;

public class InitDB {

    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS categorie (
                    idCategorie INTEGER PRIMARY KEY,
                    libelle TEXT
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS produit (
                    codeProduit TEXT PRIMARY KEY,
                    designation TEXT,
                    prixUnitaire REAL,
                    quantiteStock INTEGER,
                    seuilMin INTEGER,
                    idCategorie INTEGER
                )
            """);

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

            System.out.println("DB READY");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
