package ma.xproce.syndicconnect.config;

import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import ma.xproce.syndicconnect.dao.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 🔎 chercher l'utilisateur dans la base
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur non trouvé"));

        // 🔐 retourner un utilisateur compréhensible par Spring Security
        return User.builder()
                .username(utilisateur.getEmail()) // username = email
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole()) // RESIDENT ou SYNDIC
                .build();
    }
}

