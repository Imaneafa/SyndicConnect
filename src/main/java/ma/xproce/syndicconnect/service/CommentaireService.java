package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Commentaire;
import java.util.List;

public interface CommentaireService {
    public List<Commentaire> getAllCommentaires();            // Afficher tous les commentaires
    public Commentaire getCommentaireById(Integer id);           // Rechercher un commentaire par ID
    public Commentaire saveCommentaire(Commentaire commentaire); // Ajouter ou modifier un commentaire
    public void deleteCommentaire(Integer id);                   // Supprimer un commentaire
    public List<Commentaire> getCommentairesByProbleme(Integer problemeId); // Commentaires liés à un problème
    // Commentaires d'un utilisateur
    List<Commentaire> getCommentairesByUtilisateur(Integer utilisateurId);
}



