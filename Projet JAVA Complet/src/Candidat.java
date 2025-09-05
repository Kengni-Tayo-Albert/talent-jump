public class Candidat {

    // ------------------------------
    // Attributs privés
    // ------------------------------
    private String firstname;       // Prénom
    private String lastname;        // Nom
    private String email;           // Email (identifiant unique)
    private String targetPosition;  // Poste visé
    private String skills;          // Compétences
    private String cvLink;          // Lien vers le CV
    private String statut;          // Statut du candidat
    private String photoUrl;        // URL de la photo du candidat

    // ------------------------------
    // Constructeur
    // ------------------------------
    public Candidat(String firstname, String lastname, String email,
                    String targetPosition, String skills, String cvLink) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.targetPosition = targetPosition;
        this.skills = skills;
        this.cvLink = cvLink;
        this.statut = "En attente"; // Valeur par défaut
        this.photoUrl = "";         // Par défaut, pas de photo
    }

    // ------------------------------
    // Getters
    // ------------------------------
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getTargetPosition() {
        return targetPosition;
    }

    public String getSkills() {
        return skills;
    }

    public String getCvLink() {
        return cvLink;
    }

    public String getStatut() {
        return statut;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    // ------------------------------
    // Setters
    // ------------------------------
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // ------------------------------
    // Méthode d'affichage console
    // ------------------------------
    public void displayInfo() {
        System.out.println(this);
    }

    // ------------------------------
    // toString() pour affichage direct
    // ------------------------------
    @Override
    public String toString() {
        return firstname + " " + lastname +
               " | Email: " + email +
               " | Poste: " + targetPosition +
               " | Compétences: " + skills +
               " | CV: " + cvLink +
               " | Statut: " + statut +
               " | Photo: " + (photoUrl != null && !photoUrl.isEmpty() ? photoUrl : "Aucune");
    }
}
