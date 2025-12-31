package gestionstock.main;

import gestionstock.model.*;
import gestionstock.service.StockService;

public class Programme {

    public static void main(String[] args) {

        Utilisateur admin = new Utilisateur(1, "admin", "1234", "ADMIN");
        Categorie cat = new Categorie(1, "Informatique");

        Produit p1 = new Produit("P01", "Clavier", 2500, 10, 2, cat);

        StockService service = new StockService();
        service.ajouterProduit(p1);

        service.entreeStock(p1, 5, admin);
        service.sortieStock(p1, 3, admin);

        System.out.println("=== ETAT DU STOCK ===");
        service.afficherStock();

        System.out.println("=== HISTORIQUE DES MOUVEMENTS ===");
        service.afficherMouvements();
    }
}
