package ma.xproce.syndicconnect.web;

import ma.xproce.syndicconnect.dao.entities.Commentaire;
import ma.xproce.syndicconnect.dao.entities.Cotisation;
import ma.xproce.syndicconnect.dao.entities.Probleme;
import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import ma.xproce.syndicconnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


@Controller
@RequestMapping("/resident")
public class ResidentController {

    @Autowired
    private ProblemeService problemeService;

    @Autowired
    private CotisationService cotisationService;

    @Autowired
    private CommentaireService commentaireService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private AnnonceService annonceService;

    // ================= DASHBOARD =================
    @GetMapping("/home")
    public String home(Model model, Authentication auth) {

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        model.addAttribute("resident", resident);

        long enAttente = resident.getProblemes().stream()
                .filter(p -> "En attente".equals(p.getStatut())).count();
        long enCours = resident.getProblemes().stream()
                .filter(p -> "En cours".equals(p.getStatut())).count();
        long resolus = resident.getProblemes().stream()
                .filter(p -> "Résolu".equals(p.getStatut())).count();

        long totalProblemes = resident.getProblemes().size();

        // ===== Cotisation du mois =====
        String moisActuel = LocalDate.now()
                .getMonth()
                .getDisplayName(TextStyle.FULL, Locale.FRENCH)
                + " " + LocalDate.now().getYear();

        Cotisation cotisationMois = cotisationService
                .getCotisationsByResident(resident)
                .stream()
                .filter(c -> c.getMois().equalsIgnoreCase(moisActuel))
                .findFirst()
                .orElse(null);

        model.addAttribute("cotisationMois", cotisationMois);
        model.addAttribute("enAttente", enAttente);
        model.addAttribute("enCours", enCours);
        model.addAttribute("resolus", resolus);
        model.addAttribute("totalProblemes", totalProblemes);
        return "resident/home";
    }

    // ================= AJOUT PROBLÈME =================
    @GetMapping("/problemes/add")
    public String addForm(Model model) {
        model.addAttribute("probleme", new Probleme());
        return "resident/AddProbleme";
    }

    @PostMapping("/problemes/add")
    public String saveProbleme(@ModelAttribute Probleme probleme,
                               Authentication auth) {

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        probleme.setAuteur(resident);
        probleme.setStatut("En attente");
        probleme.setDateCreation(LocalDateTime.now());

        problemeService.saveProbleme(probleme);

        return "redirect:/resident/home";
    }

    // ================= MES PROBLÈMES =================
    @GetMapping("/problemes")
    public String mesProblemes(Model model, Authentication auth) {

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        model.addAttribute("problemes", resident.getProblemes());

        return "resident/MesProblemes";
    }

    // ================= DÉTAIL PROBLÈME =================
    @GetMapping("/problemes/view/{id}")
    public String detail(@PathVariable Integer id, Model model,Authentication auth) {

        Probleme probleme = problemeService.getProblemeById(id);
        List<Commentaire> commentaires = commentaireService.getCommentairesByProbleme(id);

        // Transformation pour l'affichage
        List<Commentaire> commentairesAffichage = commentaires.stream().map(c -> {
            if ("SYNDIC".equals(c.getAuteur().getRole())) {
                Commentaire copie = new Commentaire();
                copie.setId(c.getId());
                copie.setContenu(c.getContenu());
                copie.setDate(c.getDate());
                copie.setProbleme(c.getProbleme());

                // Auteur fictif "Syndic"
                Utilisateur fauxSyndic = new Utilisateur();
                fauxSyndic.setNom("Syndic");
                copie.setAuteur(fauxSyndic);

                return copie;
            }
            return c;
        }).toList();

        model.addAttribute("probleme", probleme);
        model.addAttribute("commentaires", commentairesAffichage);
        model.addAttribute("currentUserEmail", auth.getName());
        return "resident/DetailProbleme";
    }

    // ================= AJOUT COMMENTAIRE =================
    @PostMapping("/problemes/{id}/commentaires/add")
    public String addCommentaire(@PathVariable Integer id,
                                 @RequestParam String contenu,
                                 Authentication auth) {

        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            return "redirect:/resident/problemes";
        }

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        Commentaire c = new Commentaire();
        c.setProbleme(probleme);
        c.setAuteur(resident);
        c.setContenu(contenu);
        c.setDate(LocalDateTime.now());

        commentaireService.saveCommentaire(c);

        return "redirect:/resident/problemes/view/" + id;
    }

    // ================= ANNONCES =================
    @GetMapping("/annonces")
    public String annonces(Model model) {
        model.addAttribute("annonces", annonceService.getAllAnnonces());
        return "resident/Annonces";
    }

    @GetMapping("/cotisations")
    public String mesCotisations(Model model, Authentication auth) {

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        model.addAttribute("cotisations",
                cotisationService.getCotisationsByResident(resident));

        return "resident/mes-cotisations";
    }

    @GetMapping("/problemes/edit/{id}")
    public String editProbleme(@PathVariable Integer id,
                               Authentication auth,
                               Model model) {

        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            return "redirect:/resident/problemes";
        }

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        // sécurité : seulement le propriétaire
        if (!probleme.getAuteur().getId().equals(resident.getId())) {
            return "redirect:/resident/problemes";
        }

        model.addAttribute("probleme", probleme);
        return "resident/EditProbleme";
    }

    @PostMapping("/problemes/edit/{id}")
    public String updateProbleme(@PathVariable Integer id,
                                 @RequestParam String titre,
                                 @RequestParam String description,
                                 Authentication auth) {

        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            return "redirect:/resident/problemes";
        }

        Utilisateur resident =
                utilisateurService.getUtilisateurByEmail(auth.getName());

        // sécurité
        if (!probleme.getAuteur().getId().equals(resident.getId())) {
            return "redirect:/resident/problemes";
        }

        probleme.setTitre(titre);
        probleme.setDescription(description);

        problemeService.saveProbleme(probleme);

        return "redirect:/resident/problemes/view/" + id;
    }

}

