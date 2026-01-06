package gestionstock.model;

public class Produit {

    private String codeProduit;
    private String designation;
    private double prixUnitaire;
    private int quantiteStock;
    private int seuilMin;
    private Categorie categorie;

    public Produit() {
    }

    public Produit(String codeProduit, String designation, double prixUnitaire,
                   int quantiteStock, int seuilMin, Categorie categorie) {
        this.codeProduit = codeProduit;
        this.designation = designation;
        this.prixUnitaire = prixUnitaire;
        this.quantiteStock = quantiteStock;
        this.seuilMin = seuilMin;
        this.categorie = categorie;
    }

    /* ================= GETTERS ================= */

    public String getCodeProduit() {
        return codeProduit;
    }

    public String getDesignation() {
        return designation;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public int getSeuilMin() {
        return seuilMin;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    /* ================= SETTERS ================= */

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    // ✅ THIS WAS MISSING
    public void setSeuilMin(int seuilMin) {
        this.seuilMin = seuilMin;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    /* ================= AFFICHAGE ================= */

    @Override
    public String toString() {
        return codeProduit + " | " + designation +
               " | Prix: " + prixUnitaire +
               " | Stock: " + quantiteStock +
               " | Seuil: " + seuilMin ;
              
    }
}
