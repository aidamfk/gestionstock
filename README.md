# Système de Gestion de Stock

## 📋 Description du Projet

Application de gestion de stock développée en Java permettant à une entreprise de gérer ses produits ainsi que les mouvements d'entrée et de sortie. Le système assure la fiabilité des données, la traçabilité des opérations et la consultation en temps réel de l'état du stock.

**Projet académique** - L3 ISIL Informatique  
**Module**: Génie du Logiciel  
**Année universitaire**: 2024/2025

## 👥 Réalisé par

- **Moufouki Warda**
- **Meklati Kenza**

## 🎯 Fonctionnalités Principales

### Gestion des Produits
- ✅ Ajouter un nouveau produit
- ✅ Modifier les informations d'un produit
- ✅ Supprimer un produit (si aucun mouvement associé)
- ✅ Rechercher un produit par code
- ✅ Consulter l'état du stock complet

### Gestion des Mouvements de Stock
- ✅ Enregistrer une entrée de stock (réception)
- ✅ Enregistrer une sortie de stock (vente, utilisation)
- ✅ Mise à jour automatique des quantités
- ✅ Historique complet des mouvements
- ✅ Historique filtré par produit

### Consultation et Alertes
- ✅ Affichage de l'état du stock
- ✅ Détection des stocks faibles (sous seuil minimal)
- ✅ Alertes automatiques lors de stock insuffisant
- ✅ Traçabilité complète des opérations

### Sécurité et Traçabilité
- ✅ Authentification utilisateur (Admin/Utilisateur)
- ✅ Journalisation de toutes les opérations
- ✅ Fichier historique persistant

## 🛠️ Technologies Utilisées

- **Langage**: Java 23
- **IDE**: Eclipse
- **Structure**: Programmation Orientée Objet (POO)
- **Collections**: Vector pour la gestion des données en mémoire

## 📁 Structure du Projet

```
GestionStock/
│
├── src/
│   └── gestionstock/
│       ├── main/
│       │   └── MenuPrincipal.java          # Point d'entrée de l'application
│       │
│       ├── model/
│       │   ├── Categorie.java              # Classe catégorie de produits
│       │   ├── Produit.java                # Classe produit
│       │   ├── Mouvement.java              # Classe mouvement de stock
│       │   ├── TypeMouvement.java          # Énumération ENTREE/SORTIE
│       │   └── Utilisateur.java            # Classe utilisateur
│       │
│       └── service/
│           └── StockService.java           # Logique métier (services)
│
├── bin/                                     # Fichiers compilés (.class)
├── historique_operations.txt                # Fichier de journalisation
├── Rapport_Gestion_Stock.pdf               # Rapport de modélisation
└── README.md                                # Ce fichier
```

## 🚀 Installation et Exécution

### Prérequis
- Java JDK 23 ou supérieur
- Eclipse IDE (ou tout autre IDE Java)

### Étapes d'installation

1. **Cloner ou extraire le projet**
   ```
   Extraire le dossier GestionStock dans votre espace de travail
   ```

2. **Importer dans Eclipse**
   - Ouvrir Eclipse
   - File → Import → Existing Projects into Workspace
   - Sélectionner le dossier `GestionStock`
   - Cliquer sur Finish

3. **Compiler le projet**
   - Le projet se compile automatiquement dans Eclipse
   - Les fichiers .class sont générés dans le dossier `bin/`

4. **Exécuter l'application**
   - Clic droit sur `MenuPrincipal.java`
   - Run As → Java Application
   - Ou appuyer sur `Ctrl + F11`

### Exécution en ligne de commande

```bash
# Compiler
javac -d bin src/gestionstock/**/*.java

# Exécuter
java -cp bin gestionstock.main.MenuPrincipal
```

## 🔐 Identifiants de Connexion

### Administrateur
- **Login**: `admin`
- **Mot de passe**: `admin`
- **Droits**: Accès complet à toutes les fonctionnalités

### Utilisateur
- **Login**: `user`
- **Mot de passe**: `user`
- **Droits**: Gestion des mouvements et consultation

## 📊 Données de Test Préchargées

L'application initialise automatiquement 3 produits de démonstration:

| Code  | Désignation          | Prix (DA) | Stock | Seuil | Catégorie    |
|-------|---------------------|-----------|-------|-------|--------------|
| P001  | Clavier Sans Fil    | 2500      | 15    | 5     | Informatique |
| P002  | Souris Optique      | 1200      | 8     | 3     | Informatique |
| P003  | Cahier A4           | 150       | 50    | 10    | Bureau       |

## 📝 Utilisation de l'Application

### Menu Principal

```
╔════════════════════════════════════════════╗
║         MENU PRINCIPAL                     ║
╠════════════════════════════════════════════╣
║  1. Gestion des produits                   ║
║  2. Gestion des mouvements de stock        ║
║  3. Consultation et rapports               ║
║  0. Quitter                                ║
╚════════════════════════════════════════════╝
```

### 1. Gestion des Produits
- Ajouter un produit avec code, désignation, prix, quantité, seuil et catégorie
- Modifier la désignation et le prix (code non modifiable)
- Supprimer un produit (seulement s'il n'a pas de mouvements)
- Rechercher un produit par son code

### 2. Gestion des Mouvements
- **Entrée de stock**: Ajoute des quantités (réception, achat)
- **Sortie de stock**: Retire des quantités (vente, utilisation, perte)
- Validation automatique: impossible de créer un stock négatif
- Alerte automatique si le stock passe sous le seuil minimal

### 3. Consultation et Rapports
- État complet du stock
- Liste des produits avec stock faible
- Historique complet des mouvements
- Historique filtré par produit

## 📄 Fichier d'Historique

Le fichier `historique_operations.txt` enregistre automatiquement:
- Toutes les connexions et déconnexions
- Tous les ajouts, modifications et suppressions de produits
- Toutes les entrées et sorties de stock
- Toutes les consultations

**Format**:
```
[31/12/2025 23:53:01] admin > AJOUT PRODUIT
   Détails: Code: P005 | "TELEPHONE"
```

## ⚠️ Règles de Gestion Implémentées

1. **Code produit unique**: Impossible d'ajouter deux produits avec le même code
2. **Stock non négatif**: Les sorties sont refusées si le stock est insuffisant
3. **Protection des données**: Un produit avec mouvements ne peut pas être supprimé
4. **Traçabilité**: Les mouvements ne peuvent pas être modifiés ou supprimés
5. **Alertes**: Notification automatique quand le stock passe sous le seuil minimal
6. **Catégories prédéfinies**: Informatique (1), Bureau (2), Électronique (3)

## 🔧 Extensions Possibles

- Connexion à une base de données (MySQL, PostgreSQL)
- Interface graphique (JavaFX, Swing)
- Gestion multi-entrepôts
- Génération de rapports PDF
- Statistiques et graphiques
- Gestion des fournisseurs et clients
- Système de commandes automatiques

## 📚 Documentation Complémentaire

Pour plus de détails sur la conception et la modélisation:
- Consulter le fichier `Rapport_Gestion_Stock.pdf`
- Diagrammes UML (cas d'utilisation, classes, séquence)
- Modèle Logique de Données (MLD)

## 🐛 Résolution de Problèmes

### Le fichier historique n'apparaît pas
- Vérifiez que l'application s'exécute correctement
- Rafraîchir le projet dans Eclipse (F5)
- Le fichier est créé à la racine du projet

### Erreur de compilation
- Vérifier que Java JDK 23 est installé
- Project → Clean → Clean All Projects
- Rebuild le projet

### L'application ne démarre pas
- Vérifier que `MenuPrincipal.java` est bien la classe principale
- Run Configurations → Main class: `gestionstock.main.MenuPrincipal`

## 📞 Contact

Pour toute question concernant ce projet:
- **École**: ESST (École Supérieure des Sciences et Technologies)
- **Module**: Génie du Logiciel
- **Enseignante**: Boulkrinat Samia

---

**© 2024-2025 - Projet Académique - Tous droits réservés**
