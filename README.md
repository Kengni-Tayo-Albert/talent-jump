# TalentJump

Mini-site de gestion interne des candidatures — Projet Java (B3).  
Équipe : Albert (lead), Dorian, Josué.

---

## Structure du projet

```
PROJET JAVA COMPLET/
├─ src/                # Back-end (Java)
│  ├─ Candidat.java            # Classe représentant un candidat
│  ├─ GestionCandidats.java    # Gestion des opérations sur les candidats
│  ├─ Main.java                # Point d’entrée du programme
│  └─ Serveur.java             # Serveur Java pour faire tourner le projet
│
└─ web/                # Front-end (HTML/CSS)
   ├─ css/                      # Styles
   ├─ index.html                # Page d’accueil
   ├─ Candidats.html            # Liste des candidats
   └─ Ajouter.html              # Formulaire d’ajout
```

---

## Partie Front-end

- **Technologies** : HTML5, CSS (un fichier `css/style.css`).  
- **Navigation** : en-tête commun entre toutes les pages.  
- **Pages disponibles** :  
  - `index.html` → page d’accueil,  
  - `Candidats.html` → liste des candidats sous forme de cartes,  
  - `Ajouter.html` → formulaire d’ajout de candidat.  

Le front peut être ouvert directement dans un navigateur ou servi par le serveur Java.

---

## Partie Back-end

- **Langage** : Java  
- **JDK requis** : version 17 ou supérieure  
- **Serveur** : classe `Serveur.java` permettant de lancer le projet côté back-end.  
- **Classes principales** :  
  - `Candidat.java` : modèle de données pour un candidat,  
  - `GestionCandidats.java` : logique métier (ajout, suppression, liste…),  
  - `Main.java` : lance l’application,  
  - `Serveur.java` : gère le serveur pour relier back-end et front-end.  

---

## Démarrage rapide

### Pré-requis  
- Installer **JDK 17+**  
- Vérifier que la variable d’environnement `JAVA` est configurée  

### Lancer le projet  
1. ouvrir le dossier sur visual studio code
   ```  
2. apres avoir installer java et le jdk ce rendre sur le fichier "serveur.java"
puis lancer le serveur via la function run java  
   ```  
3. Le serveur (`Serveur.java`) démarre et rend accessible les pages HTML situées dans le dossier `web/`.  
4. Ouvrir un navigateur et accéder à :  
   ```
   http://localhost:1234
   ```  

---

## Conformité

✅ Séparation claire **front / back**  
✅ HTML5 + CSS pour le front-end  
✅ Java ( gestion serveur) pour le back-end  
✅ Respect des consignes projet  

---

## Crédits

- Développement : Albert (lead), Dorian, Josué  
- UI/Front-end : HTML5 + CSS
- Back-end : Java (serveur)  
