package ma.xproce.syndicconnect.service;


import ma.xproce.syndicconnect.dao.entities.Commentaire;
import ma.xproce.syndicconnect.dao.repositories.CommentaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentaireManager implements CommentaireService {
    @Autowired
    private CommentaireRepository commentaireRepository;

    @Override
    public List<Commentaire> getAllCommentaires() {
        return commentaireRepository.findAll();
    }

    @Override
    public Commentaire getCommentaireById(Integer id) {
        return commentaireRepository.findById(id).orElse(null);
    }

    @Override
    public Commentaire saveCommentaire(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    @Override
    public void deleteCommentaire(Integer id) {
        commentaireRepository.deleteById(id);
    }

    @Override
    public List<Commentaire> getCommentairesByProbleme(Integer problemeId) {
        return commentaireRepository.findByProblemeId(problemeId);
    }

    @Override
    public List<Commentaire> getCommentairesByUtilisateur(Integer utilisateurId) {
        return commentaireRepository.findByAuteurId(utilisateurId);
    }
}

