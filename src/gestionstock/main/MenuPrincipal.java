package gestionstock.main;

import gestionstock.model.*;
import gestionstock.service.StockService;

import java.util.Scanner;

public class MenuPrincipal {

    private static StockService service = new StockService();
    private static Scanner scanner = new Scanner(System.in);
    private static Utilisateur utilisateurConnecte;

    public static void main(String[] args) {

        System.out.println("""
            ╔══════════════════════════════════════════════╗
            ║        SYSTÈME DE GESTION DE STOCK           ║
            ║                                              ║
            ╚══════════════════════════════════════════════╝
            """);

        // Authentification
        if (!authentifier()) {
            System.out.println("❌ Authentification échouée.");
            return;
        }

        // Affichage du rôle connecté
        System.out.println("✅ Connexion réussie - Rôle: " + utilisateurConnecte.getRole());
        System.out.println();

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = lireEntier("Choix : ");

            switch (choix) {
                case 1 -> {
                    if (estAdmin()) {
                        ajouterProduit();
                    } else {
                        afficherAccesRefuse();
                    }
                }
                case 2 -> {
                    if (estAdmin()) {
                        modifierProduit();
                    } else {
                        afficherAccesRefuse();
                    }
                }
                case 3 -> {
                    if (estAdmin()) {
                        supprimerProduit();
                    } else {
                        afficherAccesRefuse();
                    }
                }
                case 4 -> entreeStock();
                case 5 -> sortieStock();
                case 6 -> service.afficherStock();
                case 7 -> service.afficherMouvements();
                case 8 -> afficherMouvementsParProduit();
                case 9 -> afficherDetailsProduit();
                case 0 -> {
                    continuer = false;
                    System.out.println("👋 Déconnexion de " + utilisateurConnecte.getLogin());
                    System.out.println("Au revoir.");
                }
                default -> System.out.println("❌ Choix invalide");
            }
        }
    }

    /* ================= AUTHENTIFICATION ================= */

    private static boolean authentifier() {
        System.out.print("Login : ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        if (login.equals("admin") && mdp.equals("admin")) {
            utilisateurConnecte = new Utilisateur(1, "admin", "admin", "ADMIN");
            return true;
        }

        if (login.equals("user") && mdp.equals("user")) {
            utilisateurConnecte = new Utilisateur(2, "user", "user", "USER");
            return true;
        }

        return false;
    }

    /* ================= CONTRÔLE D'ACCÈS ================= */

    private static boolean estAdmin() {
        return utilisateurConnecte != null && 
               utilisateurConnecte.getRole().equals("ADMIN");
    }

    private static void afficherAccesRefuse() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║          ❌ ACCÈS REFUSÉ                       ║");
        System.out.println("║                                                ║");
        System.out.println("║  Cette fonctionnalité est réservée             ║");
        System.out.println("║  aux administrateurs uniquement.               ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
    }

    /* ================= MENU DYNAMIQUE ================= */

    private static void afficherMenu() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║               MENU PRINCIPAL                   ║");
        System.out.println("║ Connecté: " + String.format("%-37s", utilisateurConnecte.getLogin() + " (" + utilisateurConnecte.getRole() + ")") + "║");
        System.out.println("╠════════════════════════════════════════════════╣");

        // Menu ADMIN uniquement
        if (estAdmin()) {
            System.out.println("║ 📦 GESTION DES PRODUITS (Admin)                ║");
            System.out.println("║   1. Ajouter un produit                        ║");
            System.out.println("║   2. Modifier un produit                       ║");
            System.out.println("║   3. Supprimer un produit                      ║");
            System.out.println("╠════════════════════════════════════════════════╣");
        }

        // Menu accessible à TOUS (USER + ADMIN)
        System.out.println("║ 📊 GESTION DES MOUVEMENTS                      ║");
        System.out.println("║   4. Entrée de stock                           ║");
        System.out.println("║   5. Sortie de stock                           ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ 📋 CONSULTATION                                ║");
        System.out.println("║   6. Afficher le stock                         ║");
        System.out.println("║   7. Historique des mouvements                 ║");
        System.out.println("║   8. Mouvements par produit                    ║");
        System.out.println("║   9. Détails d'un produit                      ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║   0. Quitter                                   ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
    }

    /* ================= GESTION PRODUITS (ADMIN SEULEMENT) ================= */

    private static void ajouterProduit() {
        System.out.println("\n=== AJOUTER UN PRODUIT ===");
        
        System.out.print("Code : ");
        String code = scanner.nextLine();

        System.out.print("Désignation : ");
        String des = scanner.nextLine();

        double prix = lireDouble("Prix : ");
        int qte = lireEntier("Quantité : ");
        int seuil = lireEntier("Seuil min : ");

        System.out.println("""
            Choisir la catégorie :
            1. Informatique
            2. Bureau
            3. Électronique
            """);

        int choixCat = lireEntier("Votre choix : ");

        Categorie cat;
        if (choixCat == 1) {
            cat = new Categorie(1, "Informatique");
        } else if (choixCat == 2) {
            cat = new Categorie(2, "Bureau");
        } else if (choixCat == 3) {
            cat = new Categorie(3, "Électronique");
        } else {
            System.out.println("⚠️ Choix invalide → catégorie Informatique par défaut");
            cat = new Categorie(1, "Informatique");
        }

        Produit p = new Produit(code, des, prix, qte, seuil, cat);
        service.ajouterProduit(p);
    }

    private static void modifierProduit() {
        System.out.println("\n=== MODIFIER UN PRODUIT ===");
        
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        
        System.out.print("Nouvelle désignation : ");
        String des = scanner.nextLine();
        
        double prix = lireDouble("Nouveau prix : ");

        service.modifierProduit(code, des, prix);
    }

    private static void supprimerProduit() {
        System.out.println("\n=== SUPPRIMER UN PRODUIT ===");
        
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        
        System.out.print("⚠️ Confirmer la suppression (oui/non) : ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            service.supprimerProduit(code);
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    /* ================= GESTION STOCK (TOUS) ================= */

    private static void entreeStock() {
        System.out.println("\n=== ENTRÉE DE STOCK ===");
        
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        
        int qte = lireEntier("Quantité à ajouter : ");

        service.entreeStock(code, qte, utilisateurConnecte);
    }

    private static void sortieStock() {
        System.out.println("\n=== SORTIE DE STOCK ===");
        
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        
        int qte = lireEntier("Quantité à retirer : ");

        service.sortieStock(code, qte, utilisateurConnecte);
    }

    /* ================= CONSULTATION (TOUS) ================= */

    private static void afficherMouvementsParProduit() {
        System.out.println("\n=== MOUVEMENTS PAR PRODUIT ===");
        
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        
        service.afficherMouvementsParProduit(code);
    }

    private static void afficherDetailsProduit() {
        System.out.println("\n=== DÉTAILS DU PRODUIT ===");
        
        System.out.print("Code produit : ");
        String code = scanner.nextLine();

        Produit p = service.rechercherProduit(code);

        if (p == null) {
            System.out.println("❌ Produit introuvable");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        INFORMATIONS PRODUIT            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Code       : " + String.format("%-26s", p.getCodeProduit()) + "║");
        System.out.println("║ Nom        : " + String.format("%-26s", p.getDesignation()) + "║");
        System.out.println("║ Prix       : " + String.format("%-26s", p.getPrixUnitaire() + " DA") + "║");
        System.out.println("║ Stock      : " + String.format("%-26s", p.getQuantiteStock()) + "║");
        System.out.println("║ Seuil min  : " + String.format("%-26s", p.getSeuilMin()) + "║");
        System.out.println("║ Catégorie  : " + String.format("%-26s", 
            (p.getCategorie() != null ? p.getCategorie().getLibelle() : "N/A")) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        
        if (p.getQuantiteStock() < p.getSeuilMin()) {
            System.out.println("\n⚠️  ALERTE: Stock en dessous du seuil minimal!");
        }
    }

    /* ================= UTILITAIRES ================= */

    private static int lireEntier(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre entier valide.");
            }
        }
    }

    private static double lireDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide.");
            }
        }
    }
}