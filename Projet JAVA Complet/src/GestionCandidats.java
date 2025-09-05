import java.util.ArrayList;
import java.util.List;

public class GestionCandidats {

    // ------------------------------
    // Liste interne des candidats
    // ------------------------------
    private final List<Candidat> candidats;

    // ------------------------------
    // Constructeur
    // ------------------------------
    public GestionCandidats() {
        this.candidats = new ArrayList<>();

        // =========================
        // Initialisation avec candidats fictifs
        // =========================
        ajouterCandidatInitial("Jenny", "Wilson", "jenny.wilson@example.com",
                "Développeur Java", "Java,Spring,SQL",
                "https://exemple.com/cv/jenny.pdf", "En attente");

        ajouterCandidatInitial("Guy", "Hawkins", "guy.hawkins@example.com",
                "UI/UX Designer", "Figma,Sketch",
                "https://exemple.com/cv/guy.pdf", "En attente");

        ajouterCandidatInitial("Kathryn", "Murphy", "kathryn.murphy@example.com",
                "Développeur Full Stack", "React,Node.js,TypeScript",
                "https://exemple.com/cv/kathryn.pdf", "Refusé");

        ajouterCandidatInitial("Jerome", "Bell", "jerome.bell@example.com",
                "Ingénieur Logiciel", "Python,AWS,Docker",
                "https://exemple.com/cv/jerome.pdf", "Accepté");
    }

    // Méthode utilitaire pour initialiser plus proprement
    private void ajouterCandidatInitial(String prenom, String nom, String email,
                                        String poste, String competences, String lienCV,
                                        String statut) {
        Candidat c = new Candidat(prenom, nom, email, poste, competences, lienCV);
        c.setStatut(statut);
        candidats.add(c);
    }

    /**
     * Ajouter un candidat à la liste.
     * @param c le candidat à ajouter
     */
    public void ajouterCandidat(Candidat c) {
        if (c != null) {
            candidats.add(c);
        }
    }

    /**
     * Récupérer la liste complète des candidats.
     * @return liste des candidats
     */
    public List<Candidat> getCandidats() {
        return new ArrayList<>(candidats); // copie défensive
    }

    /**
     * Rechercher un candidat par email.
     * @param email email à rechercher
     * @return le candidat trouvé ou null si aucun ne correspond
     */
    public Candidat rechercherCandidat(String email) {
        if (email == null) return null;
        for (Candidat c : candidats) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Rechercher un candidat par nom de famille.
     * @param nom nom à rechercher
     * @return le candidat trouvé ou null si aucun ne correspond
     */
    public Candidat rechercherCandidatParNom(String nom) {
        if (nom == null) return null;
        for (Candidat c : candidats) {
            if (c.getLastname().equalsIgnoreCase(nom)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Supprimer un candidat par email.
     * @param email email du candidat à supprimer
     * @return true si un candidat a été supprimé, false sinon
     */
    public boolean supprimerCandidat(String email) {
        if (email == null) return false;
        boolean supprime = candidats.removeIf(c -> c.getEmail().equalsIgnoreCase(email));
        if (supprime) {
            System.out.println("[GestionCandidats] Candidat supprimé : " + email);
        } else {
            System.out.println("[GestionCandidats] Aucun candidat trouvé pour suppression : " + email);
        }
        return supprime;
    }

    /**
     * Mettre à jour le statut d'un candidat.
     * @param email email du candidat
     * @param nouveauStatut nouveau statut à appliquer
     * @return true si le statut a été mis à jour, false sinon
     */
    public boolean mettreAJourStatut(String email, String nouveauStatut) {
        Candidat c = rechercherCandidat(email);
        if (c != null && nouveauStatut != null && !nouveauStatut.isBlank()) {
            c.setStatut(nouveauStatut);
            System.out.println("[GestionCandidats] Statut mis à jour pour " + email + " -> " + nouveauStatut);
            return true;
        }
        return false;
    }

    /**
     * Obtenir la classe CSS associée à un statut.
     * Utile pour l'affichage des badges dans GET /candidats.
     */
    public static String getCssClassForStatut(String statut) {
        if (statut == null) return "en-attente";
        switch (statut.toLowerCase()) {
            case "accepté": return "accepte";
            case "refusé": return "refuse";
            case "entretien": return "entretien";
            default: return "en-attente";
        }
    }
}