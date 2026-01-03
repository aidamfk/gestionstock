package gestionstock.model;

import java.util.Date;

public class Mouvement {

    private int idMouvement;
    private Date dateMouvement;
    private int quantite;
    private TypeMouvement type;
    private Produit produit;
    private Utilisateur utilisateur;

    public Mouvement(int idMouvement, Date dateMouvement, int quantite,
                     TypeMouvement type, Produit produit, Utilisateur utilisateur) {
        this.idMouvement = idMouvement;
        this.dateMouvement = dateMouvement;
        this.quantite = quantite;
        this.type = type;
        this.produit = produit;
        this.utilisateur = utilisateur;
    }

    public Produit getProduit() {
    	
        return produit;
    }
    public int getQuantite() { return quantite; }
    public TypeMouvement getType() { return type; }
    
    public Utilisateur getUtilisateur() { return utilisateur; }


    public String toString() {
        return type + " | " + produit.getCodeProduit() + " | Qte: " + quantite;
    }
}
