package ma.xproce.syndicconnect.web;

import ma.xproce.syndicconnect.dao.entities.*;
import ma.xproce.syndicconnect.dao.repositories.UtilisateurRepository;
import ma.xproce.syndicconnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/syndic")
public class SyndicController {

    private final ProblemeService problemeService;
    private final UtilisateurService utilisateurService;
    private final AnnonceService annonceService;
    private final CotisationService cotisationService;
    private final CommentaireService commentaireService;

    public SyndicController(ProblemeService problemeService,
                            UtilisateurService utilisateurService,
                            AnnonceService annonceService,
                            CotisationService cotisationService, CommentaireService commentaireService) {
        this.problemeService = problemeService;
        this.utilisateurService = utilisateurService;
        this.annonceService = annonceService;
        this.cotisationService = cotisationService;
        this.commentaireService = commentaireService;
    }

    @GetMapping("/home")
    public String home(Model model) {

        long enAttente = problemeService.countByStatut("En attente");
        long enCours   = problemeService.countByStatut("En cours");
        long resolus   = problemeService.countByStatut("Résolu");

        long nbResidents = utilisateurService.countResidents();
        model.addAttribute("cotisationsNonPayees",
                cotisationService.countNonPayees());
        model.addAttribute("enAttente", enAttente);
        model.addAttribute("enCours", enCours);
        model.addAttribute("resolus", resolus);
        model.addAttribute("nbResidents", nbResidents);

        return "syndic/home";
    }

    @GetMapping("/problemes")
    public String allProblemes(@RequestParam(required = false) String statut,
                               Model model) {

        List<Probleme> problemes;

        if (statut == null) {
            problemes = problemeService.getAllProblemes();
        } else {
            problemes = problemeService.getProblemesByStatut(statut);
        }

        model.addAttribute("problemes", problemes);
        return "syndic/problemes";
    }

    @GetMapping("/problemes/{id}")
    public String detailProbleme(@PathVariable Integer id, Model model) {

        Probleme probleme = problemeService.getProblemeById(id);
        List<Commentaire> commentaires = commentaireService.getCommentairesByProbleme(id);

        model.addAttribute("probleme", probleme);
        model.addAttribute("commentaires", commentaires);
        return "syndic/detail-probleme";
    }

    @PostMapping("/problemes/{id}/commentaires/add")
    public String addCommentaire(@PathVariable Integer id,
                                 @RequestParam String contenu,
                                 Principal principal) {

        Probleme probleme = problemeService.getProblemeById(id);
        Utilisateur syndic = utilisateurService.getUtilisateurByEmail(principal.getName());

        Commentaire commentaire = new Commentaire();
        commentaire.setProbleme(probleme);
        commentaire.setAuteur(syndic);
        commentaire.setContenu(contenu);
        commentaire.setDate(LocalDateTime.now());

        commentaireService.saveCommentaire(commentaire);

        return "redirect:/syndic/problemes/" + id;
    }

    @GetMapping("/annonces")
    public String annonces(Model model) {
        model.addAttribute("annonces", annonceService.getAllAnnonces());
        return "syndic/annonces";
    }

    @PostMapping("/problemes/{id}/statut")
    public String changerStatut(@PathVariable Integer id,
                                @RequestParam String statut) {
        Probleme probleme = problemeService.getProblemeById(id);
        probleme.setStatut(statut);
        problemeService.saveProbleme(probleme); // ou update selon ton service
        return "redirect:/syndic/problemes"; // retour à la liste des problèmes
    }

    @GetMapping("/annonces/new")
    public String newAnnonce(Model model) {
        model.addAttribute("annonce", new Annonce());
        return "syndic/new-annonce";
    }

    @PostMapping("/annonces/save")
    public String saveAnnonce(@ModelAttribute Annonce annonce,
                              Authentication authentication) {

        // 1️⃣ Date automatique
        annonce.setDatePublication(LocalDateTime.now());

        // 2️⃣ Auteur automatique (utilisateur connecté)
        Utilisateur auteur = utilisateurService
                .getUtilisateurByEmail(authentication.getName());

        annonce.setAuteur(auteur);

        // 3️⃣ Sauvegarde
        annonceService.saveAnnonce(annonce);

        return "redirect:/syndic/annonces";
    }

    @GetMapping("/annonces/edit/{id}")
    public String editAnnonce(@PathVariable Integer id, Model model) {
        Annonce annonce = annonceService.getAnnonceById(id);
        model.addAttribute("annonce", annonce);
        return "syndic/annonce-form";
    }

    @PostMapping("/annonces/update")
    public String updateAnnonce(@ModelAttribute Annonce annonce) {
        annonce.setDatePublication(LocalDateTime.now());
        annonceService.saveAnnonce(annonce);
        return "redirect:/syndic/annonces";
    }
    @PostMapping("/annonces/delete/{id}")
    public String deleteAnnonce(@PathVariable Integer id) {
        annonceService.deleteAnnonce(id);
        return "redirect:/syndic/annonces";
    }

    @PostMapping("/cotisations/payer/{id}")
    public String marquerPayee(@PathVariable Integer id) {
        Cotisation c = cotisationService.getCotisationById(id);
        c.setStatut("PAYE");
        cotisationService.saveCotisation(c);
        return "redirect:/syndic/cotisations";
    }

    @PostMapping("/cotisations/non-payer/{id}")
    public String marquerNonPaye(@PathVariable Integer id) {

        Cotisation c = cotisationService.getCotisationById(id);
        c.setStatut("NON_PAYE");
        cotisationService.saveCotisation(c);

        return "redirect:/syndic/cotisations";
    }

    @GetMapping("/cotisations")
    public String cotisations(
            @RequestParam(required = false) Integer residentId,
            @RequestParam(required = false) String mois,
            @RequestParam(required = false) String statut,
            Model model) {

        List<Cotisation> cotisations = cotisationService.getAllCotisations();

        if (residentId != null) {
            cotisations = cotisations.stream()
                    .filter(c -> c.getResident().getId().equals(residentId))
                    .toList();
        }

        if (mois != null && !mois.isEmpty()) {
            cotisations = cotisations.stream()
                    .filter(c -> c.getMois().equalsIgnoreCase(mois))
                    .toList();
        }

        if (statut != null && !statut.isEmpty()) {
            cotisations = cotisations.stream()
                    .filter(c -> c.getStatut().equals(statut))
                    .toList();
        }

        model.addAttribute("cotisations", cotisations);
        model.addAttribute("residents", utilisateurService.getAllUtilisateurs()
                .stream()
                .filter(u -> "RESIDENT".equals(u.getRole()))
                .toList());

        return "syndic/cotisations";
    }

    @GetMapping("/residents")
    public String listResidents(Model model) {

        List<Utilisateur> residents =
                utilisateurService.getAllResidents();

        // calculer les cotisations NON PAYÉES pour chaque résident
        residents.forEach(r -> {
            long nbNonPayees = cotisationService
                    .getCotisationsByResident(r)
                    .stream()
                    .filter(c -> c.getStatut().equals("NON_PAYE"))
                    .count();

            r.setNbCotisationsNonPayees(nbNonPayees);
        });

        model.addAttribute("residents", residents);
        return "syndic/residents";
    }

    @PostMapping("/residents/delete/{id}")
    public String deleteResident(@PathVariable Integer id) {

        Utilisateur resident =
                utilisateurService.getUtilisateurById(id);

        if (resident != null) {
            utilisateurService.deleteUtilisateur(id);
        }

        return "redirect:/syndic/residents";
    }
}

