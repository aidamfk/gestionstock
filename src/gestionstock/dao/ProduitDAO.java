package gestionstock.dao;

import gestionstock.model.*;
import gestionstock.util.DBConnection;

import java.sql.*;
import java.util.Vector;

public class ProduitDAO {

    /* ================= INSERT ================= */

    public void insert(Produit p) {
        String sql = """
            INSERT INTO produit
            (codeProduit, designation, prixUnitaire, quantiteStock, seuilMin, idCategorie)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getCodeProduit());
            ps.setString(2, p.getDesignation());
            ps.setDouble(3, p.getPrixUnitaire());
            ps.setInt(4, p.getQuantiteStock());
            ps.setInt(5, p.getSeuilMin());
            ps.setInt(6, p.getCategorie().getIdCategorie());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FIND BY CODE ================= */

    public Produit findByCode(String code) {
        String sql = "SELECT * FROM produit WHERE codeProduit = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Produit(
                        rs.getString("codeProduit"),
                        rs.getString("designation"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("quantiteStock"),
                        rs.getInt("seuilMin"),
                        null  
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ================= UPDATE FULL PRODUCT ================= */

    public void update(Produit p) {
        String sql = """
            UPDATE produit
            SET designation = ?, prixUnitaire = ?, quantiteStock = ?, seuilMin = ?
            WHERE codeProduit = ?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getDesignation());
            ps.setDouble(2, p.getPrixUnitaire());
            ps.setInt(3, p.getQuantiteStock());
            ps.setInt(4, p.getSeuilMin());
            ps.setString(5, p.getCodeProduit());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= DELETE ================= */

    public void delete(String code) {
        String sql = "DELETE FROM produit WHERE codeProduit = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FIND ALL ================= */

    public Vector<Produit> findAll() {
        Vector<Produit> list = new Vector<>();
        String sql = "SELECT * FROM produit";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Produit p = new Produit(
                        rs.getString("codeProduit"),
                        rs.getString("designation"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("quantiteStock"),
                        rs.getInt("seuilMin"),
                        null
                );
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
