package ma.xproce.syndicconnect.dao.repositories;
import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer> {

    // 🔑 Spring Security va utiliser l'email comme username
    Optional<Utilisateur> findByEmail(String email);
    long countByRole(String role);
    boolean existsByEmail(String email);
    List<Utilisateur> findByRole(String role);
}