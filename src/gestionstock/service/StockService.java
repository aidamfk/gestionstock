package gestionstock.service;

import gestionstock.model.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class StockService {

    private Vector<Produit> produits = new Vector<>();
    private Vector<Mouvement> mouvements = new Vector<>();
    private int compteurMouvement = 1;

    public void ajouterProduit(Produit p) {
        produits.add(p);
    }

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

    public void modifierProduit(String code, String newDesignation, double newPrix) {
        Produit p = rechercherProduit(code);
        if (p != null) {
            p.setDesignation(newDesignation);
            p.setPrixUnitaire(newPrix);
        }
    }

    public void supprimerProduit(String code) {
        Iterator<Produit> it = produits.iterator();
        while (it.hasNext()) {
            Produit p = it.next();
            if (p.getCodeProduit().equals(code)) {
                it.remove();
                break;
            }
        }
    }

    public void afficherStock() {
        Iterator<Produit> it = produits.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void entreeStock(Produit p, int quantite, Utilisateur u) {
        p.setQuantiteStock(p.getQuantiteStock() + quantite);
        mouvements.add(new Mouvement(
                compteurMouvement++, new Date(), quantite,
                TypeMouvement.ENTREE, p, u
        ));
    }

    public void sortieStock(Produit p, int quantite, Utilisateur u) {
        if (p.getQuantiteStock() >= quantite) {
            p.setQuantiteStock(p.getQuantiteStock() - quantite);
            mouvements.add(new Mouvement(
                    compteurMouvement++, new Date(), quantite,
                    TypeMouvement.SORTIE, p, u
            ));
        } else {
            System.out.println("Stock insuffisant !");
        }
    }

    public void afficherMouvements() {
        Iterator<Mouvement> it = mouvements.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void afficherMouvementsParProduit(String codeProduit) {
        Iterator<Mouvement> it = mouvements.iterator();
        while (it.hasNext()) {
            Mouvement m = it.next();
            if (m.getProduit().getCodeProduit().equals(codeProduit)) {
                System.out.println(m);
            }
        }
    }
}
