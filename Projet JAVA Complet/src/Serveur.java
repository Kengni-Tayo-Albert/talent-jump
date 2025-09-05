import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Serveur {

    private static GestionCandidats gestion = new GestionCandidats();

    public static void main(String[] args) 
    {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur démarré sur le port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream())
                    );
                    OutputStream output = clientSocket.getOutputStream();

                    String requestLine = reader.readLine();
                    if (requestLine == null || requestLine.isEmpty()) continue;
                    System.out.println("➡ " + requestLine);

                    String[] parts = requestLine.split(" ");
                    String method = parts[0];
                    String path = parts[1];

                    int contentLength = 0;
                    String line;
                    while (!(line = reader.readLine()).isEmpty()) {
                        if (line.startsWith("Content-Length:")) {
                            contentLength = Integer.parseInt(line.split(":")[1].trim());
                        }
                    }
                    // --- POST /ajouter ---
                    if (method.equals("POST") && path.equals("/ajouter")) {
                        char[] bodyChars = new char[contentLength];
                        reader.read(bodyChars);
                        String body = new String(bodyChars);

                        System.out.println("=== [/ajouter] Requête reçue ===");
                        System.out.println("Body brut: " + body);

                        Map<String, String> params = parseFormData(body);
                        System.out.println("Params parsés: " + params);

                        Candidat c = new Candidat(
                                params.get("firstname"),
                                params.get("lastname"),
                                params.get("email"),
                                params.get("targetPosition"),
                                params.get("skills"),
                                params.get("cvLink")
                        );
                        gestion.ajouterCandidat(c);

                        System.out.println("Candidat ajouté: " + c.getFirstname() + " " + c.getLastname());
                        System.out.println("Nombre total de candidats: " + gestion.getCandidats().size());

                        String filePath = "web/ajouter.html";
                        String html;
                        if (new File(filePath).exists()) {
                            html = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                            html = html.replace(
                                    "<h2>Ajouter un candidat</h2>",
                                    "<h2>Ajouter un candidat</h2><p style='color:green;font-weight:bold;'>✅ Candidat ajouté avec succès</p>"
                            );
                        } else {
                            html = "<p>✅ Candidat ajouté avec succès</p>";
                        }

                        sendHttpResponse(output, "text/html", html.getBytes(StandardCharsets.UTF_8));
                        continue;
                    }

                    // --- POST /modifier-statut ---
                    if (method.equals("POST") && path.equals("/modifier-statut")) 
                    {
                        char[] bodyChars = new char[contentLength];
                        reader.read(bodyChars);
                        String body = new String(bodyChars);

                        Map<String, String> params = parseFormData(body);
                        String email = params.get("email");
                        String nouveauStatut = params.get("statut");

                        gestion.mettreAJourStatut(email, nouveauStatut);

                        String header = "HTTP/1.1 302 Found\r\n" +
                                        "Location: /candidats\r\n\r\n";
                        output.write(header.getBytes());
                        output.flush();
                        continue;
                    }

                    // --- POST /supprimer ---
                    if (method.equals("POST") && path.equals("/supprimer")) {
                        char[] bodyChars = new char[contentLength];
                        reader.read(bodyChars);
                        String body = new String(bodyChars);

                        Map<String, String> params = parseFormData(body);
                        String email = params.get("email");

                        System.out.println("=== [/supprimer] ===");
                        System.out.println("Email à supprimer: " + email);

                        boolean supprime = gestion.supprimerCandidat(email);
                        System.out.println("Suppression réussie ? " + supprime);
                        System.out.println("Nombre total de candidats après suppression: " + gestion.getCandidats().size());

                        String header = "HTTP/1.1 302 Found\r\n" + "Location: /candidats\r\n\r\n";
                        output.write(header.getBytes());
                        output.flush();
                        continue;
                    }
                    // --- GET /candidats ---
                    if (method.equals("GET") && path.equals("/candidats")) 
                    {
                        StringBuilder html = new StringBuilder();
                        html.append("<!DOCTYPE html><html lang='fr'><head><meta charset='UTF-8'>");
                        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                        html.append("<title>TalentJump - Candidats</title>");
                        html.append("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap' rel='stylesheet'>");
                        html.append("<link rel='stylesheet' href='css/style.css'>");

                        // Styles pour badges et boutons compacts
                        html.append("<style>");
                        html.append(".status-feedback {position:fixed;top:20px;right:20px;background:#4caf50;color:white;padding:10px 15px;border-radius:5px;opacity:0;transition:opacity 0.3s ease;}");
                        html.append(".status-feedback.show {opacity:1;}");
                        html.append(".status-badge {display:inline-block;padding:4px 8px;border-radius:12px;font-size:0.8rem;font-weight:600;color:white;margin-bottom:8px;}");
                        html.append(".status-badge.accepte {background-color:#4CAF50;}");
                        html.append(".status-badge.refuse {background-color:#F44336;}");
                        html.append(".status-badge.entretien {background-color:#2196F3;}");
                        html.append(".status-badge.en-attente {background-color:#FF9800;}");
                        html.append(".status-select {padding:4px 6px;font-size:0.85rem;border-radius:6px;border:1px solid #ccc;}");
                        html.append(".btn {padding:4px 8px;font-size:0.8rem;border-radius:6px;border:none;cursor:pointer;}");
                        html.append(".btn-delete {background-color:#f44336;color:white;}");
                        html.append(".btn-cv {background-color:#2196F3;color:white;text-decoration:none;display:inline-block;}");
                        html.append(".btn:hover {opacity:0.85;}");
                        html.append("</style>");

                        html.append("</head><body>");

                        // Header
                        html.append("<header class='site-header'><div class='header__inner'>");
                        html.append("<a class='brand' href='index.html'>TalentJump</a>");
                        html.append("<nav><ul class='nav__links'>");
                        html.append("<li><a href='index.html'>Accueil</a></li>");
                        html.append("<li><a href='/candidats' class='active'>Candidats</a></li>");
                        html.append("<li><a href='ajouter.html'>Ajouter un candidat</a></li>");
                        html.append("</ul></nav></div></header>");

                        // Filtres
                        html.append("<div class='filters'>");
                        html.append("<input type='text' placeholder='Rechercher un candidat...' aria-label='Rechercher un candidat'>");
                        html.append("<select aria-label='Filtrer par statut'><option>Statut</option></select>");
                        html.append("<select aria-label='Filtrer par poste'><option>Poste</option></select>");
                        html.append("<select aria-label='Trier par'><option>Trier par</option></select>");
                        html.append("</div>");

                        // Liste des candidats
                        html.append("<main><div class='candidates'>");

                        if (gestion.getCandidats().isEmpty()) {
                            html.append("<p style='color:gray;font-style:italic;'>Aucun candidat n'a été enregistré.</p>");
                        } else {
                            for (Candidat cand : gestion.getCandidats()) {
                                String statutClass = GestionCandidats.getCssClassForStatut(cand.getStatut());

                                html.append("<article class='candidate-card ").append(statutClass).append("'>");

                                // Badge de statut
                                html.append("<div class='status-badge ").append(statutClass).append("'>")
                                    .append(cand.getStatut()).append("</div>");

                                // Avatar
                                html.append("<div class='candidate-avatar'>");
                                if (cand.getPhotoUrl() != null && !cand.getPhotoUrl().isEmpty()) {
                                    html.append("<img src='").append(cand.getPhotoUrl())
                                        .append("' alt='Photo de ").append(cand.getFirstname()).append(" ").append(cand.getLastname()).append("'>");
                                } else {
                                    String initiales = cand.getFirstname().substring(0, 1).toUpperCase()
                                                    + cand.getLastname().substring(0, 1).toUpperCase();
                                    html.append("<span class='avatar-initiales'>").append(initiales).append("</span>");
                                }
                                html.append("</div>");

                                // Infos principales
                                html.append("<div class='info-block'>");
                                html.append("<h3>").append(cand.getFirstname()).append(" ").append(cand.getLastname()).append("</h3>");
                                html.append("<p><strong>Email :</strong> ").append(cand.getEmail()).append("</p>");
                                html.append("<p><strong>Poste visé :</strong> ").append(cand.getTargetPosition()).append("</p>");
                                html.append("<p><strong>Compétences :</strong> ").append(cand.getSkills()).append("</p>");

                                // Formulaire de modification de statut (instantané)
                                html.append("<form method='POST' action='/modifier-statut'>");
                                html.append("<input type='hidden' name='email' value='").append(cand.getEmail()).append("'>");
                                html.append("<select name='statut' class='status-select' onchange='this.form.submit()'>");
                                String[] statuts = {"En attente", "Entretien", "Accepté", "Refusé"};
                                for (String s : statuts) {
                                    html.append("<option value='").append(s).append("'");
                                    if (s.equalsIgnoreCase(cand.getStatut())) {
                                        html.append(" selected");
                                    }
                                    html.append(">").append(s).append("</option>");
                                }
                                html.append("</select>");
                                html.append("</form>");

                                // Bouton de suppression
                                html.append("<form method='POST' action='/supprimer' onsubmit='return confirm(\"Supprimer ce candidat ?\")'>");
                                html.append("<input type='hidden' name='email' value='").append(cand.getEmail()).append("'>");
                                html.append("<button type='submit' class='btn btn-delete'>Supprimer</button>");
                                html.append("</form>");

                                // Lien vers le CV
                                if (cand.getCvLink() != null && !cand.getCvLink().isEmpty()) {
                                    html.append("<p><a href='").append(cand.getCvLink())
                                        .append("' target='_blank' class='btn btn-cv'>Voir le CV</a></p>");
                                }

                                html.append("</div>"); // fin info-block
                                html.append("</article>");
                            }
                        }

                        html.append("</div></main>");
                        html.append("<footer>© 2025 TalentJump</footer>");

                        // Message de feedback
                        html.append("<div id='statusFeedback' class='status-feedback'>Statut mis à jour ✅</div>");
                        html.append("<script>");
                        html.append("if (window.location.search.includes('updated=true')) {");
                        html.append("  const fb = document.getElementById('statusFeedback');");
                        html.append("  fb.classList.add('show');");
                        html.append("  setTimeout(()=>fb.classList.remove('show'), 3000);");
                        html.append("}");
                        html.append("</script>");

                        html.append("</body></html>");

                        sendHttpResponse(output, "text/html", html.toString().getBytes(StandardCharsets.UTF_8));
                        continue;
                    }
                                        // --- Fichiers statiques ---
                    if (path.equals("/")) path = "/index.html";
                    String filePath = "web" + path;
                    File file = new File(filePath);

                    if (file.exists() && !file.isDirectory()) 
                    {
                        String contentType = guessContentType(path);
                        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                        sendHttpResponse(output, contentType, fileBytes);
                    } else 
                    {
                        String notFound = "<h1>404 - Page non trouvée</h1>";
                        sendHttpResponse(output, "text/html", notFound.getBytes(StandardCharsets.UTF_8), 404);
                    }

                } catch (IOException e) 
                {
                    System.err.println("Erreur avec un client : " + e.getMessage());
                }
            }

        } catch (IOException e) 
        {
            System.err.println("Impossible de démarrer le serveur : " + e.getMessage());
        }
    }

    // --- Méthodes utilitaires ---
    private static void sendHttpResponse(OutputStream output, String contentType, byte[] content) throws IOException 
    {
        sendHttpResponse(output, contentType, content, 200);
    }

    private static void sendHttpResponse(OutputStream output, String contentType, byte[] content, int statusCode) throws IOException 
    {
        String statusText;
        switch (statusCode) 
        {
            case 200: statusText = "OK"; break;
            case 302: statusText = "Found"; break;
            case 404: statusText = "Not Found"; break;
            default:  statusText = "OK";
        }

        String header =
                "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Type: " + contentType + "; charset=UTF-8\r\n" +
                "Content-Length: " + content.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        output.write(header.getBytes(StandardCharsets.UTF_8));
        output.write(content);
        output.flush();
    }

    private static String guessContentType(String path) {
        String p = path.toLowerCase(Locale.ROOT);
        if (p.endsWith(".css"))  return "text/css";
        if (p.endsWith(".js"))   return "application/javascript";
        if (p.endsWith(".png"))  return "image/png";
        if (p.endsWith(".jpg") || p.endsWith(".jpeg")) return "image/jpeg";
        if (p.endsWith(".svg"))  return "image/svg+xml";
        if (p.endsWith(".gif"))  return "image/gif";
        if (p.endsWith(".ico"))  return "image/x-icon";
        if (p.endsWith(".json")) return "application/json";
        return "text/html";
    }

    private static Map<String, String> parseFormData(String body) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        if (body == null || body.isEmpty()) return params;

        for (String pair : body.split("&")) {
            if (pair.isEmpty()) continue;
            String[] keyValue = pair.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }
}
