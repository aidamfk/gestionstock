package gestionstock.service;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import gestionstock.service.LoggerService;
import gestionstock.model.Mouvement;
import gestionstock.model.Produit;
import gestionstock.model.TypeMouvement;
import gestionstock.model.Utilisateur;

public class StockService {

    private Vector<Produit> produits = new Vector<>();
    private Vector<Mouvement> mouvements = new Vector<>();
    private int compteurMouvement = 1;

    /**
     * Ajoute un produit au stock
     * Vérifie l'unicité du code produit
     */
    public boolean ajouterProduit(Produit p) {
        if (rechercherProduit(p.getCodeProduit()) != null) {
            System.out.println("Erreur : Un produit avec ce code existe déjà !");
            return false;
        }
        produits.add(p);
        System.out.println("Produit ajouté avec succès.");
        return true;
    }

    /**
     * Recherche un produit par son code
     */
    public Produit rechercherProduit(String code) {
        Iterator<Produit> it = produits.iterator();
        while (it.hasNext()) {
            Produit p = it.next();
            if (p.getCodeProduit().equals(code)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Modifie les informations d'un produit (sauf le code)
     */
    public boolean modifierProduit(String code, String newDesignation, double newPrix) {
        Produit p = rechercherProduit(code);
        if (p != null) {
            p.setDesignation(newDesignation);
            p.setPrixUnitaire(newPrix);
            System.out.println("Produit modifié avec succès.");
            return true;
        }
        System.out.println("Erreur : Produit introuvable.");
        return false;
    }

    /**
     * Vérifie si un produit a des mouvements associés
     */
    private boolean produitADesMouvements(String code) {
        Iterator<Mouvement> it = mouvements.iterator();
        while (it.hasNext()) {
            if (it.next().getProduit().getCodeProduit().equals(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Supprime un produit (uniquement s'il n'a pas de mouvements)
     */
    public boolean supprimerProduit(String code) {
        if (produitADesMouvements(code)) {
            System.out.println("Erreur : Impossible de supprimer un produit avec des mouvements associés.");
            return false;
        }

        Iterator<Produit> it = produits.iterator();
        while (it.hasNext()) {
            Produit p = it.next();
            if (p.getCodeProduit().equals(code)) {
                it.remove();
                System.out.println("Produit supprimé avec succès.");
                return true;
            }
        }
        System.out.println("Erreur : Produit introuvable.");
        return false;
    }

    /**
     * Affiche l'état actuel du stock
     */
    public void afficherStock() {
        if (produits.isEmpty()) {
            System.out.println("Aucun produit en stock.");
            return;
        }
        Iterator<Produit> it = produits.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * Enregistre une entrée de stock
     */
    public boolean entreeStock(Produit p, int quantite, Utilisateur u) {
        if (quantite <= 0) {
            System.out.println("Erreur : La quantité doit être positive.");
            return false;
        }
        
        p.setQuantiteStock(p.getQuantiteStock() + quantite);
        mouvements.add(new Mouvement(
                compteurMouvement++, new Date(), quantite,
                TypeMouvement.ENTREE, p, u
        ));
        System.out.println("Entrée de stock enregistrée : +" + quantite);
        return true;
    }

    /**
     * Enregistre une sortie de stock
     * Vérifie que le stock ne devient pas négatif
     */
    public boolean sortieStock(Produit p, int quantite, Utilisateur u) {
        if (quantite <= 0) {
            System.out.println("Erreur : La quantité doit être positive.");
            return false;
        }
        
        if (p.getQuantiteStock() < quantite) {
            System.out.println("Erreur : Stock insuffisant ! (Disponible : " 
                + p.getQuantiteStock() + ")");
            return false;
        }
        
        p.setQuantiteStock(p.getQuantiteStock() - quantite);
        mouvements.add(new Mouvement(
                compteurMouvement++, new Date(), quantite,
                TypeMouvement.SORTIE, p, u
        ));
        System.out.println("Sortie de stock enregistrée : -" + quantite);
        
        // Alerte si le stock passe sous le seuil minimal
        if (p.getQuantiteStock() < p.getSeuilMin()) {
            System.out.println("⚠️  ALERTE : Stock faible pour " + p.getDesignation() 
                + " (Seuil : " + p.getSeuilMin() + ")");
        }
        
        return true;
    }

    /**
     * Affiche tous les mouvements
     */
    public void afficherMouvements() {
        if (mouvements.isEmpty()) {
            System.out.println("Aucun mouvement enregistré.");
            return;
        }
        Iterator<Mouvement> it = mouvements.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * Affiche les mouvements d'un produit spécifique
     */
    public void afficherMouvementsParProduit(String codeProduit) {
        boolean trouve = false;
        Iterator<Mouvement> it = mouvements.iterator();
        while (it.hasNext()) {
            Mouvement m = it.next();
            if (m.getProduit().getCodeProduit().equals(codeProduit)) {
                System.out.println(m);
                trouve = true;
            }
        }
        if (!trouve) {
            System.out.println("Aucun mouvement pour ce produit.");
        }
    }
    
    /**
     * Affiche les produits avec un stock faible
     */
    public void afficherStocksFaibles() {
        System.out.println("=== PRODUITS AVEC STOCK FAIBLE ===");
        boolean trouve = false;
        Iterator<Produit> it = produits.iterator();
        while (it.hasNext()) {
            Produit p = it.next();
            if (p.getQuantiteStock() < p.getSeuilMin()) {
                System.out.println(p + " ⚠️ Sous le seuil (" + p.getSeuilMin() + ")");
                trouve = true;
            }
        }
        if (!trouve) {
            System.out.println("Aucun stock faible détecté.");
        }
    }
}