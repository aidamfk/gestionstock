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
            System.out.println("Authentification échouée.");
            return;
        }

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = lireEntier("Choix : ");

            switch (choix) {
                case 1 -> ajouterProduit();
                case 2 -> modifierProduit();
                case 3 -> supprimerProduit();
                case 4 -> entreeStock();
                case 5 -> sortieStock();
                case 6 -> service.afficherStock();
                case 7 -> service.afficherMouvements();
                case 8 -> afficherMouvementsParProduit();
                case 0 -> {
                    continuer = false;
                    System.out.println("Au revoir.");
                }
                default -> System.out.println("Choix invalide");
            }
        }
    }

    /* ================= AUTH ================= */

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

    

    /* ================= MENU ================= */

    private static void afficherMenu() {
        System.out.println("""
    ╔════════════════════════════════════╗
    ║               MENU                 ║
    ╠════════════════════════════════════╣
    ║ 1. Ajouter un produit              ║
    ║ 2. Modifier un produit             ║
    ║ 3. Supprimer un produit            ║
    ║ 4. Entrée de stock                 ║
    ║ 5. Sortie de stock                 ║
    ║ 6. Afficher le stock               ║
    ║ 7. Historique des mouvements       ║
    ║ 8. Mouvements par produit          ║
    ║ 0. Quitter                         ║
    ╚════════════════════════════════════╝
    """);
    }

    /* ================= PRODUIT ================= */

    private static void ajouterProduit() {
        System.out.print("Code : ");
        String code = scanner.nextLine();
        System.out.print("Désignation : ");
        String des = scanner.nextLine();
        double prix = lireDouble("Prix : ");
        int qte = lireEntier("Quantité : ");
        int seuil = lireEntier("Seuil min : ");

        Categorie cat = new Categorie(1, "Informatique"); // simplifié TP

        Produit p = new Produit(code, des, prix, qte, seuil, cat);
        service.ajouterProduit(p);
    }

    private static void modifierProduit() {
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        System.out.print("Nouvelle désignation : ");
        String des = scanner.nextLine();
        double prix = lireDouble("Nouveau prix : ");

        service.modifierProduit(code, des, prix);
    }

    private static void supprimerProduit() {
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        service.supprimerProduit(code);
    }

    /* ================= STOCK ================= */

    private static void entreeStock() {
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        int qte = lireEntier("Quantité : ");

        service.entreeStock(code, qte, utilisateurConnecte);
    }

    private static void sortieStock() {
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        int qte = lireEntier("Quantité : ");

        service.sortieStock(code, qte, utilisateurConnecte);
    }

    private static void afficherMouvementsParProduit() {
        System.out.print("Code produit : ");
        String code = scanner.nextLine();
        service.afficherMouvementsParProduit(code);
    }

    /* ================= UTILS ================= */

    private static int lireEntier(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entier invalide");
            }
        }
    }

    private static double lireDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Nombre invalide");
            }
        }
    }
}
