package ma.xproce.syndicconnect.dao.repositories;
import ma.xproce.syndicconnect.dao.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentaireRepository extends JpaRepository<Commentaire,Integer> {
    // Retourner tous les commentaires liés à un problème
    List<Commentaire> findByProblemeId(Integer problemeId);

    // Retourner les commentaires par utilisateur
    List<Commentaire> findByAuteurId(Integer auteurId);

}