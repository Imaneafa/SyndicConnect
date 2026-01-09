package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Probleme;
import java.util.List;

public interface ProblemeService {
    List<Probleme> getAllProblemes();                  // Afficher tous les problèmes
    Probleme getProblemeById(Integer id);                 // Rechercher un problème par ID
    Probleme saveProbleme(Probleme probleme);          // Ajouter ou modifier un problème
    void deleteProbleme(Integer id);                      // Supprimer un problème
    List<Probleme> getProblemesByAuteur(Integer auteurId); // Afficher les problèmes d’un utilisateur
    List<Probleme> getProblemesByStatut(String statut); // Afficher les problèmes selon leur statut
    long countByStatut(String statut);
}
