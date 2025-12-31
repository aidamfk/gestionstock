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

    public String getCodeProduit() {
        return codeProduit;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    @Override
    public String toString() {
        return codeProduit + " | " + designation +
               " | Prix: " + prixUnitaire +
               " | Stock: " + quantiteStock;
    }
}
