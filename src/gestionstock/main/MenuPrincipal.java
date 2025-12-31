package gestionstock.main;

import gestionstock.model.*;
import gestionstock.service.StockService;
import java.util.Scanner;

public class MenuPrincipal {

    private static StockService service = new StockService();
    private static Scanner scanner = new Scanner(System.in);
    private static Utilisateur utilisateurConnecte = null;

    public static void main(String[] args) {
        // Initialiser quelques données de test
        initialiserDonnees();
        
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE GESTION DE STOCK             ║");
        System.out.println("║   Version 1.0 - 2025                      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        // Authentification
        if (!authentifier()) {
            System.out.println("Authentification échouée. Au revoir.");
            return;
        }
        
        // Menu principal
        boolean continuer = true;
        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    menuGestionProduits();
                    break;
                case 2:
                    menuGestionMouvements();
                    break;
                case 3:
                    menuConsultation();
                    break;
                case 0:
                    continuer = false;
                    System.out.println("\n✓ Déconnexion réussie. Au revoir " + utilisateurConnecte.getLogin() + "!");
                    break;
                default:
                    System.out.println("✗ Choix invalide!");
            }
        }
        
        scanner.close();
    }

    private static void initialiserDonnees() {
        // Créer des catégories
        Categorie catInfo = new Categorie(1, "Informatique");
        Categorie catBureau = new Categorie(2, "Bureau");
        Categorie catElectro = new Categorie(3, "Électronique");
        
        // Créer des produits de test
        Produit p1 = new Produit("P001", "Clavier Sans Fil", 2500, 15, 5, catInfo);
        Produit p2 = new Produit("P002", "Souris Optique", 1200, 8, 3, catInfo);
        Produit p3 = new Produit("P003", "Cahier A4", 150, 50, 10, catBureau);
        
        service.ajouterProduit(p1);
        service.ajouterProduit(p2);
        service.ajouterProduit(p3);
    }

    private static boolean authentifier() {
        System.out.println("\n═══ AUTHENTIFICATION ═══");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        
        // Utilisateurs de test (normalement vérifiés en base de données)
        if (login.equals("admin") && motDePasse.equals("admin")) {
            utilisateurConnecte = new Utilisateur(1, "admin", "admin", "ADMIN");
            System.out.println("\n✓ Connexion réussie! Bienvenue " + login + " (Administrateur)");
            return true;
        } else if (login.equals("user") && motDePasse.equals("user")) {
            utilisateurConnecte = new Utilisateur(2, "user", "user", "UTILISATEUR");
            System.out.println("\n✓ Connexion réussie! Bienvenue " + login + " (Utilisateur)");
            return true;
        }
        
        System.out.println("\n✗ Login ou mot de passe incorrect!");
        return false;
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         MENU PRINCIPAL                     ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  1. Gestion des produits                   ║");
        System.out.println("║  2. Gestion des mouvements de stock        ║");
        System.out.println("║  3. Consultation et rapports               ║");
        System.out.println("║  0. Quitter                                ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }

    private static void menuGestionProduits() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n═══ GESTION DES PRODUITS ═══");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Modifier un produit");
            System.out.println("3. Supprimer un produit");
            System.out.println("4. Rechercher un produit");
            System.out.println("0. Retour");
            
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    ajouterProduit();
                    break;
                case 2:
                    modifierProduit();
                    break;
                case 3:
                    supprimerProduit();
                    break;
                case 4:
                    rechercherProduit();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("✗ Choix invalide!");
            }
        }
    }

    private static void ajouterProduit() {
        System.out.println("\n--- Ajouter un nouveau produit ---");
        
        System.out.print("Code produit: ");
        String code = scanner.nextLine();
        
        System.out.print("Désignation: ");
        String designation = scanner.nextLine();
        
        double prix = lireDouble("Prix unitaire: ");
        int quantite = lireEntier("Quantité initiale: ");
        int seuil = lireEntier("Seuil minimal: ");
        
        System.out.print("ID Catégorie (1=Informatique, 2=Bureau, 3=Électronique): ");
        int idCat = lireEntier("");
        String libelleCat = idCat == 1 ? "Informatique" : idCat == 2 ? "Bureau" : "Électronique";
        Categorie cat = new Categorie(idCat, libelleCat);
        
        Produit p = new Produit(code, designation, prix, quantite, seuil, cat);
        
        if (service.ajouterProduit(p)) {
            System.out.println("✓ Produit ajouté avec succès!");
        }
    }

    private static void modifierProduit() {
        System.out.println("\n--- Modifier un produit ---");
        System.out.print("Code du produit à modifier: ");
        String code = scanner.nextLine();
        
        Produit p = service.rechercherProduit(code);
        if (p == null) {
            System.out.println("✗ Produit introuvable!");
            return;
        }
        
        System.out.println("Produit actuel: " + p);
        System.out.print("Nouvelle désignation (Entrée pour garder): ");
        String newDesignation = scanner.nextLine();
        if (newDesignation.isEmpty()) {
            newDesignation = p.getDesignation();
        }
        
        System.out.print("Nouveau prix (0 pour garder): ");
        double newPrix = lireDouble("");
        if (newPrix == 0) {
            newPrix = p.getPrixUnitaire();
        }
        
        service.modifierProduit(code, newDesignation, newPrix);
    }

    private static void supprimerProduit() {
        System.out.println("\n--- Supprimer un produit ---");
        System.out.print("Code du produit à supprimer: ");
        String code = scanner.nextLine();
        
        Produit p = service.rechercherProduit(code);
        if (p == null) {
            System.out.println("✗ Produit introuvable!");
            return;
        }
        
        System.out.println("Produit: " + p);
        System.out.print("Confirmer la suppression? (oui/non): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            service.supprimerProduit(code);
        } else {
            System.out.println("✗ Suppression annulée.");
        }
    }

    private static void rechercherProduit() {
        System.out.println("\n--- Rechercher un produit ---");
        System.out.print("Code du produit: ");
        String code = scanner.nextLine();
        
        Produit p = service.rechercherProduit(code);
        if (p != null) {
            System.out.println("\n✓ Produit trouvé:");
            System.out.println("  Code: " + p.getCodeProduit());
            System.out.println("  Désignation: " + p.getDesignation());
            System.out.println("  Prix: " + p.getPrixUnitaire() + " DA");
            System.out.println("  Stock: " + p.getQuantiteStock());
            System.out.println("  Seuil min: " + p.getSeuilMin());
        } else {
            System.out.println("✗ Produit introuvable!");
        }
    }

    private static void menuGestionMouvements() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n═══ GESTION DES MOUVEMENTS ═══");
            System.out.println("1. Enregistrer une entrée de stock");
            System.out.println("2. Enregistrer une sortie de stock");
            System.out.println("0. Retour");
            
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    enregistrerEntree();
                    break;
                case 2:
                    enregistrerSortie();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("✗ Choix invalide!");
            }
        }
    }

    private static void enregistrerEntree() {
        System.out.println("\n--- Enregistrer une entrée de stock ---");
        System.out.print("Code du produit: ");
        String code = scanner.nextLine();
        
        Produit p = service.rechercherProduit(code);
        if (p == null) {
            System.out.println("✗ Produit introuvable!");
            return;
        }
        
        System.out.println("Produit: " + p.getDesignation() + " (Stock actuel: " + p.getQuantiteStock() + ")");
        int quantite = lireEntier("Quantité à ajouter: ");
        
        if (service.entreeStock(p, quantite, utilisateurConnecte)) {
            System.out.println("✓ Entrée enregistrée! Nouveau stock: " + p.getQuantiteStock());
        }
    }

    private static void enregistrerSortie() {
        System.out.println("\n--- Enregistrer une sortie de stock ---");
        System.out.print("Code du produit: ");
        String code = scanner.nextLine();
        
        Produit p = service.rechercherProduit(code);
        if (p == null) {
            System.out.println("✗ Produit introuvable!");
            return;
        }
        
        System.out.println("Produit: " + p.getDesignation() + " (Stock actuel: " + p.getQuantiteStock() + ")");
        int quantite = lireEntier("Quantité à retirer: ");
        
        if (service.sortieStock(p, quantite, utilisateurConnecte)) {
            System.out.println("✓ Sortie enregistrée! Nouveau stock: " + p.getQuantiteStock());
        }
    }

    private static void menuConsultation() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n═══ CONSULTATION ET RAPPORTS ═══");
            System.out.println("1. Afficher l'état du stock");
            System.out.println("2. Afficher les stocks faibles");
            System.out.println("3. Historique des mouvements (tous)");
            System.out.println("4. Historique par produit");
            System.out.println("0. Retour");
            
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    afficherStock();
                    break;
                case 2:
                    afficherStocksFaibles();
                    break;
                case 3:
                    afficherHistorique();
                    break;
                case 4:
                    afficherHistoriqueParProduit();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("✗ Choix invalide!");
            }
        }
    }

    private static void afficherStock() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ÉTAT DU STOCK                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        service.afficherStock();
    }

    private static void afficherStocksFaibles() {
        System.out.println();
        service.afficherStocksFaibles();
    }

    private static void afficherHistorique() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              HISTORIQUE DES MOUVEMENTS                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        service.afficherMouvements();
    }

    private static void afficherHistoriqueParProduit() {
        System.out.println("\n--- Historique par produit ---");
        System.out.print("Code du produit: ");
        String code = scanner.nextLine();
        
        System.out.println("\n═══ Mouvements du produit " + code + " ═══");
        service.afficherMouvementsParProduit(code);
    }

    // Méthodes utilitaires pour la saisie sécurisée
    private static int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("✗ Veuillez entrer un nombre entier valide!");
            }
        }
    }

    private static double lireDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("✗ Veuillez entrer un nombre décimal valide!");
            }
        }
    }
}