package gestionstock.service;

import gestionstock.dao.MouvementDAO;
import gestionstock.dao.ProduitDAO;
import gestionstock.model.Mouvement;
import gestionstock.model.Produit;
import gestionstock.model.TypeMouvement;
import gestionstock.model.Utilisateur;

import java.util.Date;
import java.util.List;





/**
 * StockService
 * --------------------------------------------------
 * Cette classe représente la couche SERVICE.
 * Elle contient les règles de gestion (logique métier)
 * et fait le lien entre l’interface (Menu) et la base
 * de données (DAO).
 */
public class StockService {//REGLE DE GESTION

	 // Accès aux données Produit
    private ProduitDAO produitDAO = new ProduitDAO();

    // Accès aux données Mouvement
    private MouvementDAO mouvementDAO = new MouvementDAO();

    /* =====================GESTION PRODUIT ===================== */

    public boolean ajouterProduit(Produit p) {//check if PRODUCT already exist in db
       // LE INCLUDE DE UNICITE DU CODE 
    	if (produitDAO.findByCode(p.getCodeProduit()) != null) {
        	
            System.out.println("Erreur : produit déjà existant");
            return false;
        }//ELSE          WE INSERT INTO BDD IF (NO PRODUCT 
        produitDAO.insert(p);
        System.out.println("Produit ajouté");
        return true;
    }
    /**
     * Modifier les informations d’un produit existant
     */
    public boolean modifierProduit(String code, String newDesignation, double newPrix) {
    	 // Recherche du produit dans la base
    	Produit p = produitDAO.findByCode(code);
    	 // Si le produit n’existe pas, modification impossible
        if (p == null) {
            System.out.println("Produit introuvable");
            return false;
        }
        // Mise à jour des valeurs dans l’objet Java
        p.setDesignation(newDesignation);
        p.setPrixUnitaire(newPrix);

        // Sauvegarde des modifications dans la base
        produitDAO.update(p);
        System.out.println("Produit modifié");
        return true;
    }

    /**
     * Supprimer un produit
     * Règle de gestion : un produit lié à des mouvements
     * ne peut pas être supprimé
     */
    public boolean supprimerProduit(String code) { //WE CHECK IF A PRODUCT HAS A MOUVEMENT SO WE CANT DELET IT 
    	  // Vérifier si le produit a déjà des mouvements
    	if (mouvementDAO.existsForProduit(code)) {
            System.out.println("Impossible : produit lié à des mouvements");
            return false;
        }
    	// Suppression du produit dans la base
        produitDAO.delete(code);
        System.out.println("Produit supprimé");
        return true;
    }

    

    /**
     * Afficher l’état du stock (tous les produits)
     */
    public void afficherStock() {
    	 // Récupérer tous les produits depuis la base
        List<Produit> produits = produitDAO.findAll();
        // Vérifier si la liste est vide
        if (produits.isEmpty()) {
        	
            System.out.println("Aucun produit");
            return;
        }

        // Affichage de chaque produit
        for (Produit p : produits) {
            System.out.println(p);
        }
    }

    /* =====================  GESTION MOUVEMENTS ===================== */


    /**
     * Enregistrer une entrée de stock
     */
    public boolean entreeStock(String codeProduit, int qte, Utilisateur u) {
        if (qte <= 0) {//MUST BE POSIITIVE
            System.out.println("Quantité invalide");
            return false;
        }

        Produit p = produitDAO.findByCode(codeProduit);//PRODUCT MUST EXIST
        if (p == null) {
            System.out.println("Produit introuvable");
            return false;
        }
        // Mise à jour du stock
        p.setQuantiteStock(p.getQuantiteStock() + qte);
        produitDAO.update(p);
        
        // Création du mouvement d’entrée
        Mouvement m = new Mouvement(
        		 0,                    // ID auto-généré
                 new Date(),            // Date du mouvement
                 qte,                   // Quantité
                 TypeMouvement.ENTREE,  // Type du mouvement
                 p,                     // Produit concerné
                 u                      // Utilisateur
        );
        // Sauvegarde du mouvement dans la base
        mouvementDAO.insert(m);

        System.out.println("Entrée stock enregistrée");
        return true;
    }

    /**
     * Enregistrer une sortie de stock
     */
    public boolean sortieStock(String codeProduit, int qte, Utilisateur u) {
    	   // Quantité valide
    	if (qte <= 0) {
            System.out.println("Quantité invalide");
            return false;
        }
    	  // Produit existant
        Produit p = produitDAO.findByCode(codeProduit);
        if (p == null) {
            System.out.println("Produit introuvable");
            return false;
        }

        // Règle : stock suffisant
        if (p.getQuantiteStock() < qte) {
            System.out.println("Stock insuffisant");
            return false;
        }
        // Mise à jour du stock
        p.setQuantiteStock(p.getQuantiteStock() - qte);
        produitDAO.update(p);
 

        // Création du mouvement de sortie
        Mouvement m = new Mouvement(
                0,
                new Date(),
                qte,
                TypeMouvement.SORTIE,
                p,
                u
        );
        // Sauvegarde du mouvement
        mouvementDAO.insert(m);

        // ⬇CECI EST LE <<extend>> "Déclencher alerte seuil"
        // Alerte si le stock passe sous le seuil minimum
        if (p.getQuantiteStock() < p.getSeuilMin()) {
            System.out.println("⚠️ ALERTE : stock sous le seuil");
        }
        // ⬆️ Cette alerte est CONDITIONNELLE (pas toujours déclenchée)
        System.out.println("Sortie stock enregistrée");
        return true;
    }
    
    

    /**
     * Afficher tous les mouvements de stock
     */

    public void afficherMouvements() {

        /**
         * Afficher tous les mouvements de stock
         */
        List<Mouvement> mouvements = mouvementDAO.findAll();
        if (mouvements.isEmpty()) {
            System.out.println("Aucun mouvement");
            return;
        }
        for (Mouvement m : mouvements) {
            System.out.println(m);
        }
    }
    
    /**
     * Rechercher un produit par son code
     */
    public Produit rechercherProduit(String code) {
        return produitDAO.findByCode(code);
    }


    /**
     * Afficher les mouvements d’un produit spécifique
     */
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
