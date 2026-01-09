package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Annonce;
import java.util.List;

public interface AnnonceService {
    public List<Annonce> getAllAnnonces();                    // Afficher toutes les annonces
    public Annonce getAnnonceById(Integer id);                   // Rechercher une annonce par ID
    public Annonce saveAnnonce(Annonce annonce);              // Ajouter ou modifier une annonce
    public void deleteAnnonce(Integer id);                       // Supprimer une annonce
    public List<Annonce> getAnnoncesByAuteur(Integer auteurId);  // Annonces créées par un utilisateur
}
