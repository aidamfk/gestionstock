package gestionstock.dao;

import gestionstock.model.*;
import gestionstock.util.DBConnection;

import java.sql.*;
import java.util.Date;
import java.util.Vector;

public class MouvementDAO {

    /* ================= INSERT ================= */

    public void insert(Mouvement m) {
        String sql = """
            INSERT INTO mouvement(dateMouvement, quantite, type, codeProduit, idUser)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, new Date().toString());
            ps.setInt(2, m.getQuantite());
            ps.setString(3, m.getType().name());
            ps.setString(4, m.getProduit().getCodeProduit());
            ps.setInt(5, m.getUtilisateur().getIdUser());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= CHECK IF PRODUCT HAS MOVEMENTS ================= */

    public boolean existsForProduit(String codeProduit) {
        String sql = "SELECT COUNT(*) FROM mouvement WHERE codeProduit = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, codeProduit);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= FIND ALL ================= */

    public Vector<Mouvement> findAll() {
        Vector<Mouvement> list = new Vector<>();
        String sql = "SELECT * FROM mouvement";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Mouvement m = new Mouvement(
                        rs.getInt("idMouvement"),
                        new Date(),
                        rs.getInt("quantite"),
                        TypeMouvement.valueOf(rs.getString("type")),
                        new Produit(rs.getString("codeProduit"), "", 0, 0, 0, null),
                        new Utilisateur(rs.getInt("idUser"), "", "", "")
                );
                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ================= FIND BY PRODUCT ================= */

    public Vector<Mouvement> findByProduit(String codeProduit) {
        Vector<Mouvement> list = new Vector<>();
        String sql = "SELECT * FROM mouvement WHERE codeProduit = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, codeProduit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Mouvement m = new Mouvement(
                        rs.getInt("idMouvement"),
                        new Date(),
                        rs.getInt("quantite"),
                        TypeMouvement.valueOf(rs.getString("type")),
                        new Produit(codeProduit, "", 0, 0, 0, null),
                        new Utilisateur(rs.getInt("idUser"), "", "", "")
                );
                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
