package gestionstock.main;

import gestionstock.model.*;
import gestionstock.service.StockService;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuPrincipal {

    private static StockService service = new StockService();
    private static Scanner scanner = new Scanner(System.in);
    private static Utilisateur utilisateurConnecte = null;
    private static final String LOG_FILE = "historique_operations.txt";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) {
        // Initialiser le fichier log
        initLog();
        
        // Initialiser quelques données de test
        initialiserDonnees();
        
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE GESTION DE STOCK             ║");
        System.out.println("║   Version 1.0 - 2025                      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("\n✓ Historique sauvegardé dans: " + LOG_FILE + "\n");
        
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
                    log("DÉCONNEXION", "Utilisateur: " + utilisateurConnecte.getLogin());
                    System.out.println("\n✓ Déconnexion réussie. Au revoir " + utilisateurConnecte.getLogin() + "!");
                    System.out.println("✓ Historique sauvegardé dans: " + LOG_FILE);
                    break;
                default:
                    System.out.println("✗ Choix invalide!");
            }
        }
        
        scanner.close();
    }

    private static void initLog() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, false))) {
            writer.println("╔══════════════════════════════════════════════════════════════╗");
            writer.println("║        HISTORIQUE DES OPÉRATIONS - GESTION DE STOCK         ║");
            writer.println("║              Généré le: " + dateFormat.format(new Date()) + "                ║");
            writer.println("╚══════════════════════════════════════════════════════════════╝");
            writer.println();
        } catch (Exception e) {
            System.err.println("Erreur log: " + e.getMessage());
        }
    }

    private static void log(String operation, String details) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String user = utilisateurConnecte != null ? utilisateurConnecte.getLogin() : "SYSTÈME";
            writer.println("[" + dateFormat.format(new Date()) + "] " + user + " > " + operation);
            if (details != null && !details.isEmpty()) {
                writer.println("   Détails: " + details);
            }
            writer.println();
        } catch (Exception e) {
            // Ignore
        }
    }

    private static void initialiserDonnees() {
        Categorie catInfo = new Categorie(1, "Informatique");
        Categorie catBureau = new Categorie(2, "Bureau");
        
        Produit p1 = new Produit("P001", "Clavier Sans Fil", 2500, 15, 5, catInfo);
        Produit p2 = new Produit("P002", "Souris Optique", 1200, 8, 3, catInfo);
        Produit p3 = new Produit("P003", "Cahier A4", 150, 50, 10, catBureau);
        
        service.ajouterProduit(p1);
        service.ajouterProduit(p2);
        service.ajouterProduit(p3);
        
        log("INITIALISATION", "3 produits ajoutés au système");
    }

    private static boolean authentifier() {
        System.out.println("\n═══ AUTHENTIFICATION ═══");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        
        if (login.equals("admin") && motDePasse.equals("admin")) {
            utilisateurConnecte = new Utilisateur(1, "admin", "admin", "ADMIN");
            System.out.println("\n✓ Connexion réussie! Bienvenue " + login + " (Administrateur)");
            log("CONNEXION RÉUSSIE", "Utilisateur: " + login + " | Rôle: ADMIN");
            return true;
        } else if (login.equals("user") && motDePasse.equals("user")) {
            utilisateurConnecte = new Utilisateur(2, "user", "user", "UTILISATEUR");
            System.out.println("\n✓ Connexion réussie! Bienvenue " + login + " (Utilisateur)");
            log("CONNEXION RÉUSSIE", "Utilisateur: " + login + " | Rôle: UTILISATEUR");
            return true;
        }
        
        System.out.println("\n✗ Login ou mot de passe incorrect!");
        log("CONNEXION ÉCHOUÉE", "Login: " + login);
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
                case 1: ajouterProduit(); break;
                case 2: modifierProduit(); break;
                case 3: supprimerProduit(); break;
                case 4: rechercherProduit(); break;
                case 0: retour = true; break;
                default: System.out.println("✗ Choix invalide!");
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
        
        int idCat = lireEntier("ID Catégorie (1=Informatique, 2=Bureau, 3=Électronique): ");
        String libelleCat = idCat == 1 ? "Informatique" : idCat == 2 ? "Bureau" : "Électronique";
        Categorie cat = new Categorie(idCat, libelleCat);
        
        Produit p = new Produit(code, designation, prix, quantite, seuil, cat);
        
        if (service.ajouterProduit(p)) {
            System.out.println("✓ Produit ajouté avec succès!");
            log("AJOUT PRODUIT", "Code: " + code + " | " + designation);
        } else {
            log("AJOUT PRODUIT ÉCHOUÉ", "Code: " + code + " (déjà existant)");
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
        if (newDesignation.isEmpty()) newDesignation = p.getDesignation();
        
        double newPrix = lireDouble("Nouveau prix (0 pour garder): ");
        if (newPrix == 0) newPrix = p.getPrixUnitaire();
        
        service.modifierProduit(code, newDesignation, newPrix);
        log("MODIFICATION PRODUIT", "Code: " + code);
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
            boolean succes = service.supprimerProduit(code);
            if (succes) {
                log("SUPPRESSION PRODUIT", "Code: " + code);
            } else {
                log("SUPPRESSION ÉCHOUÉE", "Code: " + code + " (a des mouvements)");
            }
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
            log("RECHERCHE PRODUIT", "Code: " + code + " (Trouvé)");
        } else {
            System.out.println("✗ Produit introuvable!");
            log("RECHERCHE PRODUIT", "Code: " + code + " (Introuvable)");
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
                case 1: enregistrerEntree(); break;
                case 2: enregistrerSortie(); break;
                case 0: retour = true; break;
                default: System.out.println("✗ Choix invalide!");
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
            log("ENTRÉE DE STOCK", "Produit: " + code + " | Qté: +" + quantite + " | Stock: " + p.getQuantiteStock());
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
        
        int stockAvant = p.getQuantiteStock();
        if (service.sortieStock(p, quantite, utilisateurConnecte)) {
            System.out.println("✓ Sortie enregistrée! Nouveau stock: " + p.getQuantiteStock());
            String alerte = p.getQuantiteStock() < p.getSeuilMin() ? " ⚠️ STOCK FAIBLE!" : "";
            log("SORTIE DE STOCK", "Produit: " + code + " | Qté: -" + quantite + " | Stock: " + p.getQuantiteStock() + alerte);
        } else {
            log("SORTIE ÉCHOUÉE", "Produit: " + code + " | Demandé: " + quantite + " | Disponible: " + stockAvant);
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
                case 1: afficherStock(); break;
                case 2: afficherStocksFaibles(); break;
                case 3: afficherHistorique(); break;
                case 4: afficherHistoriqueParProduit(); break;
                case 0: retour = true; break;
                default: System.out.println("✗ Choix invalide!");
            }
        }
    }

    private static void afficherStock() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ÉTAT DU STOCK                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        service.afficherStock();
        log("CONSULTATION", "État du stock complet");
    }

    private static void afficherStocksFaibles() {
        System.out.println();
        service.afficherStocksFaibles();
        log("CONSULTATION", "Stocks faibles");
    }

    private static void afficherHistorique() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              HISTORIQUE DES MOUVEMENTS                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        service.afficherMouvements();
        log("CONSULTATION", "Historique complet des mouvements");
    }

    private static void afficherHistoriqueParProduit() {
        System.out.println("\n--- Historique par produit ---");
        System.out.print("Code du produit: ");
        String code = scanner.nextLine();
        
        System.out.println("\n═══ Mouvements du produit " + code + " ═══");
        service.afficherMouvementsParProduit(code);
        log("CONSULTATION", "Historique produit: " + code);
    }

    private static int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("✗ Veuillez entrer un nombre entier valide!");
            }
        }
    }

    private static double lireDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("✗ Veuillez entrer un nombre décimal valide!");
            }
        }
    }
}