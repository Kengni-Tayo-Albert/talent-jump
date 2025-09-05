import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1️⃣ Préparation des outils
        // --------------------------
        // - Scanner pour lire les saisies clavier
        // - GestionCandidats pour stocker et manipuler la liste des candidats
        Scanner scanner = new Scanner(System.in);
        GestionCandidats gestionCandidats = new GestionCandidats();

        // 2️⃣ Boucle principale du menu
        // -----------------------------
        // On affiche un menu et on répète jusqu'à ce que l'utilisateur quitte
        boolean continuer = true;
        while (continuer) {

            // Affichage du menu
            System.out.println("\n===== MENU =====");
            System.out.println("1. Afficher tous les candidats");
            System.out.println("2. Rechercher un candidat par email");
            System.out.println("3. Rechercher un candidat par nom");
            System.out.println("4. Ajouter un candidat");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choix) {

                case 1:
                    // Affiche tous les candidats enregistrés
                    if (gestionCandidats.getCandidats().isEmpty()) {
                        System.out.println("Aucun candidat enregistré.");
                    } else {
                        System.out.println("\n--- Liste des candidats ---");
                        for (Candidat c : gestionCandidats.getCandidats()) {
                            // On utilise toString() si défini dans Candidat.java
                            System.out.println(c);
                        }
                    }
                    break;

                case 2:
                    // Recherche par email
                    System.out.print("Entrez l'email du candidat à rechercher : ");
                    String email = scanner.nextLine();
                    Candidat candidatEmail = gestionCandidats.rechercherCandidat(email);
                    if (candidatEmail != null) {
                        System.out.println("Candidat trouvé : " + candidatEmail);
                    } else {
                        System.out.println("Aucun candidat trouvé avec cet email.");
                    }
                    break;

                case 3:
                    // Recherche par nom
                    System.out.print("Entrez le nom du candidat à rechercher : ");
                    String nom = scanner.nextLine();
                    Candidat candidatNom = gestionCandidats.rechercherCandidatParNom(nom);
                    if (candidatNom != null) {
                        System.out.println("Candidat trouvé : " + candidatNom);
                    } else {
                        System.out.println("Aucun candidat trouvé avec ce nom.");
                    }
                    break;

                case 4:
                    // Ajout d'un nouveau candidat
                    System.out.print("Prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Nom : ");
                    String nomFamille = scanner.nextLine();
                    System.out.print("Email : ");
                    String emailAjout = scanner.nextLine();
                    System.out.print("Poste visé : ");
                    String poste = scanner.nextLine();
                    System.out.print("Compétences : ");
                    String competences = scanner.nextLine();
                    System.out.print("Lien vers le CV : ");
                    String lienCV = scanner.nextLine();

                    Candidat nouveau = new Candidat(prenom, nomFamille, emailAjout, poste, competences, lienCV);
                    gestionCandidats.ajouterCandidat(nouveau);
                    System.out.println("✅ Candidat ajouté avec succès !");
                    break;

                case 0:
                    // Quitter le programme
                    continuer = false;
                    System.out.println("Fermeture du programme...");
                    break;

                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }

        scanner.close();
    }
}
