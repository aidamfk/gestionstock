package gestionstock.dao;

import gestionstock.model.*;
import gestionstock.util.DBConnection;

import java.sql.*;
import java.util.Date;
import java.util.Vector;

public class MouvementDAO {

    /* ================= INSERT MOVEMENT ================= */

    public void insert(Mouvement m) {

        // SQL request: insert a stock movement (entry or exit)
        String sql = """
            INSERT INTO mouvement(dateMouvement, quantite, type, codeProduit, idUser)
            VALUES (?, ?, ?, ?, ?)
        """;

        // OPEN CONNECTION + PREPARE SQL STATEMENT
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // FILL PARAMETERS (?):
            // 1 = movement date
            ps.setString(1, new Date().toString());

            // 2 = quantity moved
            ps.setInt(2, m.getQuantite());

            // 3 = movement type (ENTREE or SORTIE)
            ps.setString(3, m.getType().name());

            // 4 = product code concerned by the movement
            ps.setString(4, m.getProduit().getCodeProduit());

            // 5 = user who performed the movement
            ps.setInt(5, m.getUtilisateur().getIdUser());

            // EXECUTE INSERT IN DATABASE
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= CHECK IF PRODUCT HAS MOVEMENTS ================= */

    public boolean existsForProduit(String codeProduit) {

        // SQL request: count movements for a given product
        String sql = "SELECT COUNT(*) FROM mouvement WHERE codeProduit = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Replace ? by product code
            ps.setString(1, codeProduit);

            // Execute SELECT query
            ResultSet rs = ps.executeQuery();

            // If count > 0, product has movements
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= FIND ALL MOVEMENTS ================= */

    public Vector<Mouvement> findAll() {

        // List that will contain all movements
        Vector<Mouvement> list = new Vector<>();

        // SQL request: get all movements
        String sql = "SELECT * FROM mouvement";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // LOOP THROUGH EACH ROW
            while (rs.next()) {

                // Transform SQL row into Java object (DAO role)
                Mouvement m = new Mouvement(
                        rs.getInt("idMouvement"),
                        new Date(), // simplified date handling (TP style)
                        rs.getInt("quantite"),
                        TypeMouvement.valueOf(rs.getString("type")),
                        new Produit(rs.getString("codeProduit"), "", 0, 0, 0, null),
                        new Utilisateur(rs.getInt("idUser"), "", "", "")
                );

                // Add movement to list
                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return all movements to service layer
        return list;
    }

    /* ================= FIND MOVEMENTS BY PRODUCT ================= */

    public Vector<Mouvement> findByProduit(String codeProduit) {

        // List of movements for one product
        Vector<Mouvement> list = new Vector<>();

        // SQL request: get movements of one product
        String sql = "SELECT * FROM mouvement WHERE codeProduit = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Replace ? by product code
            ps.setString(1, codeProduit);

            ResultSet rs = ps.executeQuery();

            // LOOP THROUGH RESULT
            while (rs.next()) {

                // Create movement object from SQL row
                Mouvement m = new Mouvement(
                        rs.getInt("idMouvement"),
                        new Date(),
                        rs.getInt("quantite"),
                        TypeMouvement.valueOf(rs.getString("type")),
                        new Produit(codeProduit, "", 0, 0, 0, null),
                        new Utilisateur(rs.getInt("idUser"), "", "", "")
                );

                // Add to list
                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return movements list to service
        return list;
    }
}
