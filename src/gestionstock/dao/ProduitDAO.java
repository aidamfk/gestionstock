package gestionstock.dao;

import gestionstock.model.*;
import gestionstock.util.DBConnection;

import java.sql.*;
import java.util.Vector;

public class ProduitDAO {

    /* ================= INSERT PRODUCT================= */

    public void insert(Produit p) {
        String sql = """
            INSERT INTO produit
            (codeProduit, designation, prixUnitaire, quantiteStock, seuilMin, idCategorie)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
//CONN ET  DBCON OUVRE CONNEXION
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
 //EXECUTE REQUETE
        	//REMPLIR PARAMETRE RECUOPPERE DONNER ET ENVOYER VERS DB
            ps.setString(1, p.getCodeProduit());
            ps.setString(2, p.getDesignation());
            ps.setDouble(3, p.getPrixUnitaire());
            ps.setInt(4, p.getQuantiteStock());
            ps.setInt(5, p.getSeuilMin());
            ps.setInt(6, p.getCategorie().getIdCategorie());




            ps.executeUpdate();//ENVOYER A DB(just insert delet update

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FIND BY CODE ================= */
    // Exécute la requête SQL
    // Si le code existe → retourne un Produit
    // Si le code n'existe pas → retourne null

    public Produit findByCode(String code) {
        String sql = "SELECT * FROM produit WHERE codeProduit = ?";
//we replace ? by code given by user 
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
//RS IS A LINE FOUNDED
            if (rs.next()) {

                int idCat = rs.getInt("idCategorie");

              
                Categorie cat;
                if (idCat == 1) {
                    cat = new Categorie(1, "Informatique");
                } else if (idCat == 2) {
                    cat = new Categorie(2, "Bureau");
                } else if (idCat == 3) {
                    cat = new Categorie(3, "Électronique");
                } else {
                    cat = null;
                }
//TRANSFORM LIGNE SQL INTO OBJECT JAVA (((ROLE OF DAO
                return new Produit(
                        rs.getString("codeProduit"),
                        rs.getString("designation"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("quantiteStock"),
                        rs.getInt("seuilMin"),
                        cat
                );
            }


     

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ================= UPDATE modify FULL PRODUCT ================= */

    public void update(Produit p) {
        String sql = """
            UPDATE produit
            SET designation = ?, prixUnitaire = ?, quantiteStock = ?, seuilMin = ?
            WHERE codeProduit = ? 
        """;
//WHERE = SEARCH CODE SPICIFY
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
//NUMBERS 1 TO 5 WILL REFFER TO EVERY VALEUR EN REQUETE SQL 1 =DESIGNATION
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

            ps.setString(1, code);//REMPLIS LE (?)
            ps.executeUpdate();//EXCUTE

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= FIND ALL ================= */

    public Vector<Produit> findAll(){
    	//recuper tous product from bdd AND RETURN THEM INTO LIST OBJECT
        Vector<Produit> list = new Vector<>();
        String sql = "SELECT * FROM produit";//TAKE ALL LINE FROM TABLE PRODUCT

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {// SELECT =EXECUTEQUERY

            while (rs.next()) {//PASS TO NEXT LINE  RETURN TRUE IF IT HAS A LINE 
                Produit p = new Produit(
                        rs.getString("codeProduit"),
                        rs.getString("designation"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("quantiteStock"),
                        rs.getInt("seuilMin"),
                        null
                );
                list.add(p);//ADD PRODUCT INTO THE LIST
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;// SEND LIST TO SERVICE
    }
    }
