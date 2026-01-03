package gestionstock.service;

import gestionstock.dao.MouvementDAO;
import gestionstock.dao.ProduitDAO;
import gestionstock.model.Mouvement;
import gestionstock.model.Produit;
import gestionstock.model.TypeMouvement;
import gestionstock.model.Utilisateur;

import java.util.Date;
import java.util.List;

public class StockService {//REGLE DE GESTION

    private ProduitDAO produitDAO = new ProduitDAO();
    private MouvementDAO mouvementDAO = new MouvementDAO();

    /* ===================== PRODUIT ===================== */

    public boolean ajouterProduit(Produit p) {
        if (produitDAO.findByCode(p.getCodeProduit()) != null) {
            System.out.println("Erreur : produit déjà existant");
            return false;
        }
        produitDAO.insert(p);
        System.out.println("Produit ajouté");
        return true;
    }

    public boolean modifierProduit(String code, String newDesignation, double newPrix) {
        Produit p = produitDAO.findByCode(code);
        if (p == null) {
            System.out.println("Produit introuvable");
            return false;
        }
        p.setDesignation(newDesignation);
        p.setPrixUnitaire(newPrix);
        produitDAO.update(p);
        System.out.println("Produit modifié");
        return true;
    }

    public boolean supprimerProduit(String code) {
        if (mouvementDAO.existsForProduit(code)) {
            System.out.println("Impossible : produit lié à des mouvements");
            return false;
        }
        produitDAO.delete(code);
        System.out.println("Produit supprimé");
        return true;
    }

    public void afficherStock() {
        List<Produit> produits = produitDAO.findAll();
        if (produits.isEmpty()) {
            System.out.println("Aucun produit");
            return;
        }
        for (Produit p : produits) {
            System.out.println(p);
        }
    }

    /* ===================== MOUVEMENTS ===================== */

    public boolean entreeStock(String codeProduit, int qte, Utilisateur u) {
        if (qte <= 0) {
            System.out.println("Quantité invalide");
            return false;
        }

        Produit p = produitDAO.findByCode(codeProduit);
        if (p == null) {
            System.out.println("Produit introuvable");
            return false;
        }

        p.setQuantiteStock(p.getQuantiteStock() + qte);
        produitDAO.update(p);

        Mouvement m = new Mouvement(
                0,
                new Date(),
                qte,
                TypeMouvement.ENTREE,
                p,
                u
        );
        mouvementDAO.insert(m);

        System.out.println("Entrée stock enregistrée");
        return true;
    }

    public boolean sortieStock(String codeProduit, int qte, Utilisateur u) {
        if (qte <= 0) {
            System.out.println("Quantité invalide");
            return false;
        }

        Produit p = produitDAO.findByCode(codeProduit);
        if (p == null) {
            System.out.println("Produit introuvable");
            return false;
        }

        if (p.getQuantiteStock() < qte) {
            System.out.println("Stock insuffisant");
            return false;
        }

        p.setQuantiteStock(p.getQuantiteStock() - qte);
        produitDAO.update(p);

        Mouvement m = new Mouvement(
                0,
                new Date(),
                qte,
                TypeMouvement.SORTIE,
                p,
                u
        );
        mouvementDAO.insert(m);

        if (p.getQuantiteStock() < p.getSeuilMin()) {
            System.out.println("⚠️ ALERTE : stock sous le seuil");
        }

        System.out.println("Sortie stock enregistrée");
        return true;
    }

    public void afficherMouvements() {
        List<Mouvement> mouvements = mouvementDAO.findAll();
        if (mouvements.isEmpty()) {
            System.out.println("Aucun mouvement");
            return;
        }
        for (Mouvement m : mouvements) {
            System.out.println(m);
        }
    }
    
    public Produit rechercherProduit(String code) {
        return produitDAO.findByCode(code);
    }

    public void afficherMouvementsParProduit(String codeProduit) {
        List<Mouvement> mouvements = mouvementDAO.findByProduit(codeProduit);
        if (mouvements.isEmpty()) {
            System.out.println("Aucun mouvement pour ce produit");
            return;
        }
        for (Mouvement m : mouvements) {
            System.out.println(m);
        }
    }
}
