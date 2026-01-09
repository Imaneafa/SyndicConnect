package ma.xproce.syndicconnect.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import ma.xproce.syndicconnect.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegisterController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Page d'inscription
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "register";
    }

    // Traitement de l'inscription
    @PostMapping("/register")
    public String registerResident(Utilisateur utilisateur, Model model, HttpServletRequest request) {
        // Vérifier si email existe déjà
        if (utilisateurService.existsByEmail(utilisateur.getEmail())) {
            model.addAttribute("error", "Cet email est déjà utilisé !");
            return "register";
        }

        // Encoder le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));

        // Définir le rôle RESIDENT
        utilisateur.setRole("RESIDENT");

        // Enregistrer dans la base
        utilisateurService.saveUtilisateur(utilisateur);

        // 3️⃣ connecter automatiquement le nouvel utilisateur
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                utilisateur.getEmail(), null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_RESIDENT"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4️⃣ rediriger vers la page home du resident
        return "redirect:/resident/home";
    }
}

